package fr.unice.polytech.biblio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.biblio.components.Bibliotheque;
import fr.unice.polytech.biblio.components.StudentRegistry;
import fr.unice.polytech.biblio.entities.Livre;
import fr.unice.polytech.biblio.server.JaxsonUtils;
import fr.unice.polytech.biblio.server.SimpleHttpServer4Library;
import fr.unice.polytech.biblio.server.SimpleHttpServer4Scolarity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class KarateLikeAPIStepdefs {


//Copied from APIStepdefs.java
// TODO: refactor this class to use the same setup as APIStepdefs.java

        static HttpServer scolarity;
        static HttpServer library;

        static StudentRegistry studentRegistry = new StudentRegistry();
        static Bibliotheque biblio = new Bibliotheque();

        static Logger logger = Logger.getLogger("KarateLikeAPITesting");
        {
            logger.setLevel(Level.OFF);
        }


    @AfterEach
    public void teardown() {
        // Arrêter le serveur après les tests
        logger.info("J arrete le serveur");
        SimpleHttpServer4Library.stopServer(PORT4LIBRARY);
        SimpleHttpServer4Scolarity.stopServer(PORT4SCOLARITY);
    }

/*-----------------------------------
Background:
  * libraryPort = 8004
  * scolarityPort = 8005
  * urlbase 'http://localhost'
  * url4library = urlbase + ':' + libraryPort + '/api/library'
 --------------------------------------- */

    private int PORT4LIBRARY ;
    private int PORT4SCOLARITY;
    private String urlbase;
    private String url4library;
    @Given("libraryPort = {int}")
    public void library_port(Integer port) {
        PORT4LIBRARY = port;

    }
    @Given("scolarityPort = {int}")
    public void scolarity_port(Integer port) {
        PORT4SCOLARITY = port;
    }

    @Given("urlbase {string}")
    public void urlbase(String base) {
        urlbase = base;
    }

    @Given("url4library = urlbase + {string} + libraryPort + {string}")
    public void url4library_urlbase_library_port(String intermediaire, String complement) throws IOException {
        url4library = urlbase + intermediaire + PORT4LIBRARY + complement;
        logger.info("============== Starting servers");
        logger.info("====> Starting Scolarity");
        scolarity = SimpleHttpServer4Scolarity.startServer(PORT4SCOLARITY, studentRegistry);
        logger.info("====> Starting Library");
        library = SimpleHttpServer4Library.startServer(PORT4LIBRARY, biblio, studentRegistry);

    }

  /*  Given url url4library
      Given request { title: "Guernica", author: [ "Dave Boling"], isbn: "2-84893-019-5", identifiant: "G-0" }
      When method post
      Then status 200
      And match response == { "Book created" }
   */

    String url;
    URI uri ;
    HttpClient client;

    @Given("url url4library")
    public void urlUrlLibraryG() {
        url = url4library;
        uri = URI.create(url);
        client = HttpClient.newHttpClient();
    }

    HttpRequest.BodyPublisher body ;
    @Given("request \\{ title: {string}, author: [ {string}], isbn: {string}, identifiant: {string} }")
    public void request_author_isbn_identifiant(String title, String author, String isbn, String identifiant) throws JsonProcessingException {
        Livre livre = Livre.createLivre(title, identifiant);
        livre.setIsbn(isbn);
        livre.setAuteurs(new String[]{author});
        String newBook = JaxsonUtils.toJson(livre);
        System.out.println(newBook);
        logger.log(Level.FINE, "Json before : " + newBook);
        body = HttpRequest.BodyPublishers.ofString(newBook);
    }

    HttpResponse<String> response;
    @When("method post")
    public void method_post() throws IOException, InterruptedException {
        response = client.send(
                HttpRequest.newBuilder()
                        .POST(body)
                        .uri(uri)
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }


    @Then("status {int}")
    public void status(Integer int1) {
        assertEquals(int1, response.statusCode());
    }

    @Then("match response == \\{ {string} }")
    public void match_response(String contents) {
        assertEquals(contents, response.body());
    }


    /**
     # get by id
     Given url4library+ '/G-0'
     When method get
     Then status 200
     And match response == {"titre":"Java","auteurs":["Gosling","Holmes"],"isbn":"2000","identifiant":"J-1"}
     */

    @Given("url4library+ {string}")
    public void url4library(String complement) {
        url = url4library + complement;
        uri = URI.create(url);
    }


    @When("method get")
    public void method_get() {
        try {
            response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(uri)
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Then("match response == \\{titre:{string},auteurs:[{string}],isbn: {string},identifiant:{string}}")
    public void match_response_auteurs_isbn_identifiant(String title, String author, String isbn, String identifiant) {
        assertEquals("{\"titre\":\""+title+"\",\"auteurs\":[\""+author+"\"],\"isbn\":\""+isbn+"\",\"identifiant\":\""+identifiant+"\"}", response.body());
    }


    @Then("match response contains \\{titre:{string},auteurs:[{string}],isbn: {string},identifiant:{string}}")
    public void match_response_contains_auteurs_isbn_identifiant(String title, String author, String isbn, String identifiant) {
        assertTrue(response.body().contains("{\"titre\":\""+title+"\",\"auteurs\":[\""+author+"\"],\"isbn\":\""+isbn+"\",\"identifiant\":\""+identifiant+"\"}"));

    }


}
