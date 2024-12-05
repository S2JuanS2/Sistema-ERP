Feature: Get cost of a project on a specific date
  Scenario: Successfully get total cost of project
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" in the year "2020" and month "1"
    And a with role "Desarrollador" and experience "Junior" and cost "1000"
    When I get the total cost of the project
    Then the total cost of the project should be "1000"

  Scenario: Successfully get total cost of project with multiple roles
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" in the year "2020" and month "1"
    And a with role "Desarrollador" and experience "Junior" and cost "1000"
    And a with role "Desarrollador" and experience "Semi Senior" and cost "2000"
    When I get the total cost of the project
    Then the total cost of the project should be "3000"

  Scenario: Successfully get total cost of project with multiple dates
    Given I am on the page that allows me to get the total cost of a project
    And I have a project with name "Sistema de Gestión de Inventarios" in the year "2020" and month "1"
    And a with role "Desarrollador" and experience "Junior" and cost "1000"
    And I have a project with name "Sistema de Gestión de Inventarios" in the year "2020" and month "2"
    And a with role "Desarrollador" and experience "Semi Senior" and cost "2000"
    When I get the total cost of the project
    Then the total cost of the project should be "3000"

  Scenario: Failed to get total cost of a project in non valid date
    Given I am on the page that allows me to get the total cost of a project
    When I try to get the cost of a project in the year "1990" and month "1"
    Then The system should return an error message
