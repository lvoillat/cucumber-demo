#language: fr
Fonctionnalité: gérer les etudiants


  Contexte:

  Scénario: Ajout d'un étudiant
    Etant donné que la base ne contient pas d'étudiant "Marcel" avec le numéro d'étudiant 123456
    Quand la scolarité ajoute un étudiant "Marcel" avec le numéro d'étudiant 123456
    Alors la base contient maintenant un étudiant "Marcel" avec le numéro d'étudiant 123456

  Scénario: chercher un étudiant
    Etant donné que la base contient un étudiant "Eva" avec le numéro d'étudiant 876543
    Quand la scolarité cherche un étudiant avec le numéro d'étudiant 876543
    Alors la scolarité trouve un étudiant "Eva" avec le numéro d'étudiant 876543