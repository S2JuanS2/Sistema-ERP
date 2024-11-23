Feature: Getting costs

  Scenario: Successfully get cost of all roles
    Given I add a role cost with name "Desarrollador", experience "Senior", cost "3000"
    And I add a role cost with name "Desarrollador", experience "Semi-Senior", cost "2000"
    When I GET to the route "/api/v1/finanzas/costos"
    Then the response should have status 200
    And the response should only contain the first and second roles

  Scenario: Successfully get cost of a role
    Given I add a role cost with name "Desarrollador", experience "Senior", cost "3000"
    And I add a role cost with name "Desarrollador", experience "Semi-Senior", cost "2000"
    When I GET to the route "/api/v1/finanzas/costos/" with the id of the first role cost
    Then the response should have status 200
    And the response should contain the first role cost

  Scenario: Fail to get cost of a role with an invalid id
    Given I add a role cost with name "Desarrollador", experience "Senior", cost "3000"
    And I add a role cost with name "Desarrollador", experience "Semi-Senior", cost "2000"
    When I GET to the route "/api/v1/finanzas/costos/" with an invalid id
    Then the response should have status 404
    And the response should contain the message "No se encontró un rol con nombre y experiencia coincidentes"