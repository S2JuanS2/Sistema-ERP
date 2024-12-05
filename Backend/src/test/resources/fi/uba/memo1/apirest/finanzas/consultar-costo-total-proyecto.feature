Feature: Get total cost of project
  Scenario: Successfully get total cost of project
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" that lasted 1 month and cost "1000"
    When I get the total cost of the project
    Then the system should return the total cost of the project "1000"

  Scenario: Successfully get total cost of project that lasted 3 months
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" that lasted 3 months and cost "1000" per month
    When I get the total cost of the project
    Then the system should return the total cost of the project "3000"