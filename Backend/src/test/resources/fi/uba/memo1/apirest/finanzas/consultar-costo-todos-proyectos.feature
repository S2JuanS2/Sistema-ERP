Feature: Get cost of all projects

  Scenario: Successfully get cost of all projects
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" that lasted 3 month and cost "1000"
    And I have a project with name "Aplicación de Comercio Electrónico (E-commerce)" that lasted 1 month and cost "1000"
    When I get the total cost of all projects
    Then the system should return the total cost of all the projects "4000"