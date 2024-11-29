Feature: Modify cost of a role

  Scenario: Successfully modify cost of a role
    Given I add a role cost with name "Desarrollador", experience "Senior", cost "3000"
    And the new cost "2000"
    When I PUT to the route "/api/v1/finanzas/actualizar-costo/1" with rol id 1
    Then The response status must be 200
    And the cost should now be "2000"

  Scenario: Fail to modify cost of a role due to negative amount
    Given I add a role cost with name "Desarrollador", experience "Senior", cost "1000"
    And the new cost "-500"
    When I PUT to the route "/api/v1/finanzas/actualizar-costo/2" with rol id 2
    Then The response status must be 400
    And the cost should remain "1000"

    Scenario: Fail to modify cost of a role due to non existence rol
    Given I do not have a role with ID 999
    When I PUT to the route "/api/v1/finanzas/actualizar-costo/999" with rol id 999
    Then The response status must be 404
