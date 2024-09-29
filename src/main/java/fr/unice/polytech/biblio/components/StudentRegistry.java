package fr.unice.polytech.biblio.components;

import fr.unice.polytech.biblio.entities.Etudiant;
import fr.unice.polytech.biblio.interfaces.StudentFinder;
import fr.unice.polytech.biblio.interfaces.StudentManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * Mireille Blay-Fornarino

 */

/*
    * This class is used to manage the students of the university.
    * We choose to return optional and not to deal with exceptions as we did in the library.
    * We could have used exceptions, but we wanted to show that we can use optional.
 */
public class StudentRegistry implements StudentManager, StudentFinder<Etudiant> {

    Map<Integer, Etudiant> students = new HashMap<>();

    @Override
    public List<Etudiant> findAll() {
        return List.of(students.values().toArray(new Etudiant[0]));
    }

    @Override
    public Optional<Etudiant> findByName(String name) {
        return students.values().stream().filter(e -> e.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Etudiant> findByNumber(int studentNumber) {
        return Optional.ofNullable(students.get(studentNumber));
    }

    @Override
    public void addStudent(String name, int studentNumber) {
        Etudiant student = new Etudiant();
        student.setName(name);
        student.setStudentNumber(studentNumber);
        students.put(studentNumber, student);
    }

    @Override
    public void removeStudent(int studentNumber) {
        students.remove(studentNumber);
    }

    @Override
    public void updateStudent(int studentNumber, String name) {
        if (students.get(studentNumber) != null)
            students.get(studentNumber).setName(name);
        else {
            Etudiant student = new Etudiant();
            student.setName(name);
            student.setStudentNumber(studentNumber);
            students.put(studentNumber, student);
        }
    }

    public StudentRegistry() {
        // Mimic loading of members from a database
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setName("John Doe");
        etudiant1.setStudentNumber(123456);
        students.put(123456, etudiant1);

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setName("Jane Doe");
        etudiant2.setStudentNumber(654321);
        students.put(654321, etudiant2);
    }
}
