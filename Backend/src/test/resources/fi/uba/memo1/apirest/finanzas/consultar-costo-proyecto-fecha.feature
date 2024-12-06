Feature: Get cost of a project on a specific date
  Scenario: Successfully get total cost of project
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Aplicación de Comercio Electrónico (E-commerce)" in the year "2024" and month "1"
    And a role "Desarrollador" and experience "Senior" and cost "1000"
    When I get the total cost of the project
    Then the total cost of the project should be "1000"

  Scenario: Successfully get total cost of project with multiple roles
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Aplicación de Comercio Electrónico (E-commerce)" in the year "2024" and month "1"
    And a role "Desarrollador" and experience "Senior" and cost "1000"
    And a role "Analista" and experience "Nivel II" and cost "2000"
    When I get the total cost of the project
    Then the total cost of the project should be "3000"