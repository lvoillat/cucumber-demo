package fr.unice.polytech.biblio.server;

import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.biblio.components.Bibliotheque;
import fr.unice.polytech.biblio.components.StudentRegistry;
import fr.unice.polytech.biblio.server.httphandlers.LibraryHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
   * Mireille Blay-Fornarino
   * 2024
   * This class is a simple HTTP server that can be used to serve a library.
   * The server will be listening on port 8000.
   * The server will be able to manage the following requests:
   * - GET /api/library : return the list of all books
   * - POST /api/library : add a new book
   * - GET /api/library/{id} : return the book with the given id
   * - PUT /api/library/{id} : update the book with the given id
   * - DELETE /api/library/{id} : delete
   * and to borrow a book
   * - POST /api/library/{id}/borrow : borrow the book with the given id to the student with the given student number
   * The server will be able to manage the following books:
   * - id : the identifier of the book
   * - title : the title of the book
   * - authors : the authors of the book (array of strings)
   * - isbn : the ISBN of the book
   *
 */
public class SimpleHttpServer4Library {




    private static final Map<Integer, HttpServer> servers = new HashMap<>();

    static Logger logger = Logger.getLogger("SimpleHttpServer4Library");

    static {
        logger.setLevel(Level.SEVERE);
    }
    public static final int DEFAULT_PORT4LIBRARY = 8000;
    public static final int DEFAULT_PORT_4_SCOLARITY = 8001;
    public static void main(String[] args)  {
        try {
            StudentRegistry studentRegistry = new StudentRegistry();
            SimpleHttpServer4Scolarity.startServer(DEFAULT_PORT_4_SCOLARITY, studentRegistry);
            startServer(DEFAULT_PORT4LIBRARY, new Bibliotheque(), studentRegistry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static HttpServer startServer(int port, Bibliotheque bibliotheque, StudentRegistry studentRegistry) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/library", new LibraryHttpHandler(bibliotheque, studentRegistry));
        server.setExecutor(null); // creates a default executor
        server.start();
        servers.put(port,server);
        logger.log(Level.FINE,"Library Server started on port "+port);
        //logger.log(java.util.logging.Level.FINE, (STR."Library Server started on port \{port}");
        return server;
    }

    public static void stopServer(int port) {
        if (servers.get(port) != null) {
            servers.get(port).stop(0);
        }
        servers.remove(port);
    }


}
