Feature: Getting costs

  Scenario: Successfully get cost of all roles
    Given I am on the page that allows me to get the costs of all roles
    And I added a role cost with name "Desarrollador", experience "Senior", cost "3000"
    And I added a role cost with name "Desarrollador", experience "Semi-Senior", cost "2000"
    When I try to get the costs of all roles
    Then the system should return the costs of the roles correctly

  Scenario: Successfully get cost of a role
    Given I am on the page that allows me to get the costs of all roles
    And I added a role cost with name "Desarrollador", experience "Senior", cost "3000"
    When I try to get the costs of the role
    Then the system should return the cost of the role correctly

  Scenario: Fail to get cost of a role with an invalid id
    Given I am on the page that allows me to get the costs of all roles
    And I added a role cost with name "Desarrollador", experience "Senior", cost "3000"
    When I try to get the costs of the role with an invalid id
    Then the system should return an error message