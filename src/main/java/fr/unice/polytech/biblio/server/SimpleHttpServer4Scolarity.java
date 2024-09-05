package fr.unice.polytech.biblio.server;

import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.biblio.components.StudentRegistry;
import fr.unice.polytech.biblio.server.httphandlers.MembersHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/*
    * This class is used to start a simple HTTP server
    * that will be used to manage the members of the university.
    * The server will be listening on port 8001.
    * The server will be able to manage the following requests:
    * - GET /api/members : return the list of all members
    * - POST /api/members : add a new member
    * - GET /api/members/{id} : return the member with the given id
    * - PUT /api/members/{id} : update the member with the given id
    * - DELETE /api/members/{id} : delete the member with the given id
    * The server will be able to manage the following members:
    * - id : the identifier of the member (student number)
    * - name : the name of the member
    * - email : the email of the member
    *
 */
public class SimpleHttpServer4Scolarity {

    private static final Map<Integer,HttpServer> servers = new HashMap<>();

    public static final int DEFAULT_PORT = 8001;

    static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("SimpleHttpServer4Scolarity");

    static {
        logger.setLevel(Level.SEVERE);
    }
    public static void main(String[] args) {
        try {
            startServer(DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HttpServer startServer(int port) throws IOException {
        return startServer(port, new StudentRegistry());
    }

    public static HttpServer startServer(int port, StudentRegistry studentRegistry) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/members", new MembersHttpHandler(studentRegistry));
        server.setExecutor(null); // creates a default executor
        server.start();
        servers.put(port,server);
        logger.log(Level.INFO, "Scolarity Server started on port "+port);
        //System.out.println(STR."Scolarity Server started on port \{port}");
        return server;
    }


    public static void stopServer(int port) {
        if (servers.get(port) != null) {
            servers.get(port).stop(0);
        }
    }



}
