Feature: Test de cucumber
  Scenario: Ping
    Given the route "/api/v1/finanzas/hola-mundo"
    When I send a GET request
    Then the response status code should be 200
    And the response body should be "Hola mundo"