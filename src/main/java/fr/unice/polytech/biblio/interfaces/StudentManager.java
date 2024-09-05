package fr.unice.polytech.biblio.interfaces;

/*
 * Mireille Blay-Fornarino
 */
public interface StudentManager {
    void addStudent(String name, int studentNumber);
    void removeStudent(int studentNumber);
    void updateStudent(int studentNumber, String name);
}
