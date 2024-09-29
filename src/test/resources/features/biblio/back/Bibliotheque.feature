# language: fr
Fonctionnalité: gérer les livres

  Contexte:
    Etant donné une bibliothèque

  Scénario: Ajout d'un livre
    Quand le bibliothécaire  ajoute le livre "UML pour les nuls"
    Alors la bibliothèque contient le livre "UML pour les nuls" en un exemplaire

  Scénario: Ajout de deux exemplaires
    Quand le bibliothécaire  ajoute deux exemplaires du livre "Design Patterns for dummies"
    Alors la bibliothèque contient deux exemplaires du livre "Design Patterns for dummies"

  Scénario: Accès à un livre inexistant
    Quand le lecteur cherche le livre avec l'ID U-100
    Alors une exception est levée avec le message "Book not found"
