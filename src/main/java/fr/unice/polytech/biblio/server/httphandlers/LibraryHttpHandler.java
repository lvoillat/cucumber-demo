package fr.unice.polytech.biblio.server.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.biblio.components.Bibliotheque;
import fr.unice.polytech.biblio.components.BookNotFoundException;
import fr.unice.polytech.biblio.components.StudentRegistry;
import fr.unice.polytech.biblio.entities.Etudiant;
import fr.unice.polytech.biblio.entities.Livre;
import fr.unice.polytech.biblio.server.JaxsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Mireille Blay-Fornarino
 */

/**
 * This class is used to manage the library.
 * It handles the following requests:
 * - GET /api/library : return the list of all books
 * - POST /api/library : add a new book
 * - GET /api/library/{id} : return the book with the given id
 * <p>
 * and to borrow a book
 * - POST /api/library/{id}/borrow : borrow the book with the given id to the student with the given student number
 */
public class LibraryHttpHandler implements HttpHandler {
    private final Bibliotheque bibliotheque;
    private final StudentRegistry studentRegistry;

    public LibraryHttpHandler(Bibliotheque bibliotheque, StudentRegistry studentRegistry) {
        this.bibliotheque = bibliotheque;
        this.studentRegistry = studentRegistry;
        logger.setLevel(Level.OFF);
    }

    static Logger logger = Logger.getLogger("LibraryHttpHandler");

    static {
        logger.setLevel(Level.OFF);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("LibraryHandler called");
        // CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // Remplacez par votre origine cliente
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");


        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET":
                    logger.log(Level.FINE, "GET method called");
                    if (exchange.getRequestURI().getPath().equals("/api/library")) {
                        logger.log(Level.FINE, "GET all books");
                        answerWithAllBooks(exchange);
                    } else {
                        String id = exchange.getRequestURI().getPath().substring("/api/library/".length());
                        logger.log(Level.INFO, "GET book with id " + id);
                        validateId(id);
                        answerWithBook(exchange, id);
                    }
                    break;
                case "POST":
                    logger.log(Level.FINE, "POST method called");
                    if (exchange.getRequestURI().getPath().equals("/api/library")) {
                        askToCreateBook(exchange);
                    } else {
                        //borrow a book idOfTheBook/borrow
                        String id = exchange.getRequestURI().getPath().substring("/api/library/".length());
                        id = id.substring(0, id.length() - "/borrow".length());
                        validateId(id);
                        logger.log(Level.FINE, "POST borrow book with id " + id);
                        askToBorrowBook(exchange, id);
                    }
                    break;
                case "OPTIONS":
                    // Just send a 200 OK response to the preflight request
                    //a preflight request is a CORS request that checks if the actual request is safe to send
                    exchange.sendResponseHeaders(HttpUtils.OK_CODE, -1);
                    break;
                default:
                    logger.log(Level.SEVERE, "Unknown method called");
                    exchange.sendResponseHeaders(HttpUtils.BAD_REQUEST, 0);
                    exchange.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while processing the request : " + e.getMessage());
            logger.info("call to GlobalExceptionHandler with exception: " + e);
            GlobalExceptionHandler.handleException(exchange, e);
        }

    }


    private void askToBorrowBook(HttpExchange exchange, String bookId) throws IOException, BookNotFoundException, StudentNotFoundException {
            InputStream is = exchange.getRequestBody();
            String jsonBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            logger.log(Level.INFO, "Request body: " + jsonBody);
            var sto = JaxsonUtils.fromJson(jsonBody, LibraryHttpHandler.StudentDTO.class);
            validateStudentDTO(sto);
            logger.log(Level.INFO, "STO : " + sto);
            int studentNumber = sto.studentNumber();
            //It can throw a BookNotFoundException
            Livre book = bibliotheque.getLivrebyId(bookId);
            logger.log(Level.INFO, "Book: " + book);

            var student = studentRegistry.findByNumber(studentNumber);
            if (student.isEmpty()) {
                throw  new StudentNotFoundException("This student does not exist");
            }

            Etudiant e = student.get();
            boolean borrowed = bibliotheque.emprunte(e, book);
            logger.log(Level.FINE, "Statut de l'emprunt: " + borrowed);
            //build the response
            if (!borrowed) {
                throw  new BookNotFoundException("This Book cannot be borrowed");
            }

            String response = "Book borrowed";
            logger.log(Level.FINE, "Response: " + response);
            //send the response to the client
            exchange.getResponseHeaders().set(HttpUtils.CONTENT_TYPE, "text/plain");
            exchange.sendResponseHeaders(201, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

    }


    private void askToCreateBook(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            String jsonBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            logger.log(Level.FINE, "Request body: " + jsonBody);
            Livre livre = JaxsonUtils.fromJson(jsonBody, Livre.class);
            if (livre == null) {
                throw new IllegalArgumentException("Invalid book data");
            }
            bibliotheque.addLivre(livre);
            //build the response
            String response = "Book created";
            //send the response to the client
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(201, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

    }

    private void answerWithBook(HttpExchange exchange, String id) throws IOException, BookNotFoundException {
            Livre livre = bibliotheque.getLivrebyId(id);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = JaxsonUtils.toJson(livre);
            logger.log(Level.FINE, "Response: " + response);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();

    }

    private void answerWithAllBooks(HttpExchange exchange) throws IOException {
            List<Livre> livres = bibliotheque.getLivres();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = JaxsonUtils.toJson(livres);
            logger.log(Level.FINE, "Response: " + response);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
    }

    public record StudentDTO(int studentNumber) {
    }

    /***** Validation methods *****/
    private void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
    }

    private void validateStudentDTO(LibraryHttpHandler.StudentDTO dto) {
        if (dto == null || dto.studentNumber() <= 0) {
            throw new IllegalArgumentException("Invalid student data");
        }
    }


}
