Feature: Modify cost of a role

  Scenario: Successfully modify cost of a role
    Given I am on the page that allows me to edit the costs of all roles
    And I previously added a role cost with name "Desarrollador", experience "Senior", cost "3000"
    When I update the role cost to "2000"
    Then the system should return the role with the new cost "2000" correctly

  Scenario: Fail to modify cost of a role due to negative amount
    Given I am on the page that allows me to edit the costs of all roles
    And I previously added a role cost with name "Desarrollador", experience "Senior", cost "3000"
    When I update the role cost to "-2000"
    Then the system should return an error

  Scenario: Fail to modify cost of a role due to non existence rol
    Given I am on the page that allows me to edit the costs of all roles
    And I have not added any role cost
    When I update the role cost to "2000"
    Then the system should return an error
