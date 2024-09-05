package fr.unice.polytech.biblio.server.httphandlers;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
