package fr.unice.polytech.biblio.components;

import fr.unice.polytech.biblio.entities.Emprunt;
import fr.unice.polytech.biblio.entities.Etudiant;
import fr.unice.polytech.biblio.entities.Livre;

import java.time.LocalDate;
import java.util.*;

/**
 * Ph. Collet
 * modifié par M. Blay-Fornarino
 *
 */
public class Bibliotheque {

	public static final int DUREE_MAX_EMPRUNT = 15;
	//Nous séparons les étudiants et les livres
	//La bibliothèque ne connait que les livres mais interagit avec les étudiants pour les emprunts
	//@Todo : Ce point devrait être amélioré, en ne mettant aucune information concernant les emprunts dans étudiant.
	private Map<String,List<Livre>> livres = new HashMap<>();
	private Map<Livre, Emprunt> emprunts = new HashMap<>();

	private Map<String,Livre> livreById = new HashMap<>();


	public Bibliotheque()	 {
		initLibrary();
	}

	//To mimic loading of books from a database

	private void initLibrary() {
		addLivre(new Livre("UML", new String[]{"Booch", "Rumbaugh", "Jacobson"}, "1999",0));
		addLivre(new Livre("Java", new String[]{"Gosling", "Holmes"}, "2000",1));
		addLivre(new Livre("Design Patterns", new String[]{"Erich Gamma"}, "1994",2));
		addLivre(new Livre("Refactoring", new String[]{"Martin Fowler"}, "1999",3));
	}



   /************* Gestion des livres *******************/
	public void addLivre(Livre l) {
		livres.putIfAbsent(l.getTitre(), new ArrayList<>());
		livres.get(l.getTitre()).add(l);
		livreById.put(l.getIdentifiant(), l);
	}

    public List<Livre> getLivres() {
		List<Livre> res = new ArrayList<>();
		res.addAll(livres.values().stream().reduce(new ArrayList<>(), (acc, l) -> {
			acc.addAll(l);
			return acc;
		}));
		return res;
	}

	/********** Gestion des emprunts de livres **********/
	public Optional<Livre> getLivreDisponibleByTitle(String titre) {
		if ( (livres.get(titre) == null) || (livres.get(titre).isEmpty()) ) {
			return Optional.empty();
		}
		for (Livre l : livres.get(titre)) {
			if (!l.estEmprunte()) {
				return Optional.of(l);
			}
		}
		return Optional.empty();
	}

	/********** Gestion des emprunts de livres **********/
	public List<Livre> getLivresByTitle(String titre) {
		if ( (livres.get(titre) == null) || (livres.get(titre).isEmpty()) ) {
			return new ArrayList<>();
		}
		return livres.get(titre);
	}




	public boolean emprunte(Etudiant e, Livre l) {
		if (l.estEmprunte()) {
			return false;
		}
		Emprunt emprunt = new Emprunt(LocalDate.now().plusDays(DUREE_MAX_EMPRUNT), e, l);
		emprunts.put(l,emprunt);
		l.setEstEmprunte(true);
		e.addEmprunt(emprunt);

		return true;
	}

	public Emprunt getEmpruntByLivre(Livre l) {
		return emprunts.get(l);
	}

	//Cette méthode viole la loi de Demeter car elle connait trop de choses sur l'étudiant...
    public boolean rend(Livre l) {
		if (!l.estEmprunte()) {
			return false;
		}
		Emprunt emprunt = emprunts.remove(l);
		l.setEstEmprunte(false);
		emprunt.getEmprunteur().removeEmprunt(emprunt);
		return true;
	}


	public List<Emprunt> getEmprunts() {
		return new ArrayList<>(emprunts.values());
	}

	public Livre getLivrebyId(String id) throws BookNotFoundException{
		var livre = livreById.get(id);
		if (livre == null) {
			throw new BookNotFoundException("Book not found");
		}
		return livre;
	}



}