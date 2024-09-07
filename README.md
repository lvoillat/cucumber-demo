# Cucumber-demo: Example Code for Cucumber Tests

This GitHub repository contains example code to help you learn how to use Cucumber for testing.

**Key Features**:
- Cucumber 7 and JUnit 5
- Compatible with Maven
- Requires JDK 21
- Mockito 4.8 (_Non utilisé dans cette version pour l'instant_)
- Gherkin and stepDefs in both French (FR) and English (EN), including integration of _Examples_
- GitHub Actions (Check the .github/workflows) for straightforward Maven compilation and testing.


## Execution of tests

`mvn test`

expected result:

```
...... (output shortened)

[INFO] Results:
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 43, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.053 s
[INFO] Finished at: 2024-09-05T19:37:26+02:00
[INFO] ------------------------------------------------------------------------
```

## Organisation of the code

:warning: **Please use the** **pom.xml** file provided to establish the link between JUnit 5 and Cucumber.


:arrow_forward: **Features:** 
The feature description files (Gherkin files) are accessible under [test/resources/features](./src/test/resources/features)

:arrow_forward: **StepDefs:** The implementations corresponding to the steps are defined under tests and follow the same structure as the files describing the scenarios. They refer to the sources located under main.

## Enabling Cucumber Support in IntelliJ IDEA

https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html

## Warning
1. Attention: if the classes defining the steps are not public, they are not accessible during execution.
2. Don't forget to adapt **RunCucumberTest.java** classes to your needs.


## Running test with maven (@PhilippeCollet)

By default, maven use its *surefire* plugin to run tests. This plugin is especially built for running unit tests, as it will diretly fail if any test fails. 
This is a good property for preventing the build to be made (the goal *package* will typically fail).

It must be noted that *surefire* will, by default, find tests with the following names and run them during the `test` phase (i.e. just before `package`):

* `"**/Test*.java"` - includes all of its subdirectories and all Java filenames that start with "Test".
* `"**/*Test.java"` - includes all of its subdirectories and all Java filenames that end with "Test".
* `"**/*Tests.java"` - includes all of its subdirectories and all Java filenames that end with "Tests".
* `"**/*TestCase.java"` - includes all of its subdirectories and all Java filenames that end with "TestCase".`


## Tests

Seuls des tests unitaires, d'intégration et de validation vous sont demandés dans la première partie de ce module.

### Unit tests
Les tests unitaires sont des tests qui vérifient le bon fonctionnement d'une unité de code (une méthode, une classe, etc.) de manière isolée.

[EtudiantTest.java](src%2Ftest%2Fjava%2Ffr%2Funice%2Fpolytech%2Fbiblio%2Fentities%2FEtudiantTest.java),
 [LivreTest.java](src%2Ftest%2Fjava%2Ffr%2Funice%2Fpolytech%2Fbiblio%2Fentities%2FLivreTest.java),  
[JaxsonUtilsTest.java](src%2Ftest%2Fjava%2Ffr%2Funice%2Fpolytech%2Fbiblio%2Fserver%2FJaxsonUtilsTest.java)    sont des exemples de tests unitaires.

### Integration tests
Les tests d'intégration sont des tests qui vérifient le bon fonctionnement de plusieurs unités de code ensemble.

[IntegrationOfScolarityTest.java](src%2Ftest%2Fjava%2Ffr%2Funice%2Fpolytech%2Fbiblio%2Fserver%2FIntegrationOfScolarityTest.java) est un exemple de tests d'intégration où le StudentRegistry est mocké.

### Validation tests
Les tests de validation sont des tests qui vérifient que le code respecte certaines règles de validation.
Sous [back](src%2Ftest%2Fresources%2Ffeatures%2Fbiblio%2Fback): les scénarios visent à tester la partie arrière, métier de l'application.

### End-to-end tests
Les tests end-to-end sont des tests qui vérifient le bon fonctionnement de l'application dans son ensemble.
Ces tests s'expriment en considérant l'application comme une boîte noire, c'est-à-dire que nous ne nous intéressons pas à la manière dont l'application est implémentée, mais seulement à son comportement.

[APITesting.feature](src%2Ftest%2Fresources%2Ffeatures%2Fbiblio%2FAPITesting.feature) correspond à des tests end-to-end.

### API tests
Les tests API sont des tests qui vérifient le bon fonctionnement de l'API de l'application.
[KarateLikeAPITesting.feature](src%2Ftest%2Fresources%2Ffeatures%2Fbiblio%2FKarateLikeAPITesting.feature) explicite des tests API.
Ils s'inspirent de Karate, un outil de test d'API qui permet de tester des API REST.