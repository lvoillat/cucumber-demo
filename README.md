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

