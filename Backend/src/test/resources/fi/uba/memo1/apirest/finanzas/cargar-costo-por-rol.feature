Feature: Load cost of a role

  Scenario: Successfully load cost of a role
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Desarrollador" and experience "Senior" and cost "3000"
    Then the system should load the role correctly

  Scenario: Fail to load cost with non existing role name
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Administrador" and experience "Senior" and cost "3000"
    Then the system should throw an error and not load the role

  Scenario: Fail to load cost with non existing role experience
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Administrador" and experience "Entry" and cost "3000"
    Then the system should throw an error and not load the role

  Scenario: Successfully load cost of a role in a specific date
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Desarrollador" and experience "Senior" and cost "3000" and month "12" and year "2023"
    Then the system should load the role correctly

  Scenario: Fail to load cost with invalid month
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Desarrollador" and experience "Senior" and cost "3000" and month "13" and year "2023"
    Then the system should throw an error and not load the role

  Scenario: Fail to load cost with invalid year
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Desarrollador" and experience "Senior" and cost "3000" and month "12" and year "1980"
    Then the system should throw an error and not load the role

  Scenario: Fail to load cost with a negative amount
    Given I am on the page that allows me to load the cost of a role
    When I try to add a role with name "Desarrollador" and experience "Senior" and cost "-3000"
    Then the system should throw an error and not load the role
