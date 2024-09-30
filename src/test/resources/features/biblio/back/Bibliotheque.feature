#language: fr
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

  Scénario: ajout de plusieurs livres
    Etant donné que la bibliothèque contient le livre "UML pour les nuls" en 3 exemplaires
    Quand le bibliothécaire ajoute les livres suivants:
      | ISBN  | Titre                    |nb exemplaires|
      | 2212092806 | UML pour les nuls        |2|
      | 0471798541 | Design Patterns for dummies |4|
    Alors la bibliothèque contient au moins les livres suivants:
      | ISBN  | Titre                    | nb exemplaires|
      | 2212092806 | UML pour les nuls        | 5|
      | 0471798541 | Design Patterns for dummies |  4|
