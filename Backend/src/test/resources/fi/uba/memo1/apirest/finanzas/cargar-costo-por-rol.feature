Feature: Load cost of a role

  Scenario: Successfully load cost of a role
    Given I have a role name "Desarrollador"
    And a role experience "Senior"
    And a role cost "3000"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 201
    And the response should be the object

  Scenario: Fail to load cost with non existing role name
    Given I have a role name "Administrador"
    And a role experience "Senior"
    And a role cost "3000"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 404
    And the response should be "No se encontró un rol con nombre y experiencia coincidentes"

  Scenario: Fail to load cost with non existing role experience
    Given I have a role name "Desarrollador"
    And a role experience "Entry"
    And a role cost "3000"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 404
    And the response should be "No se encontró un rol con nombre y experiencia coincidentes"

  Scenario: Fail to load cost with invalid month
    Given I have a role name "Desarrollador"
    And a role experience "Senior"
    And a role cost "3000"
    And a month "13"
    And a year "2023"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 400
    And the response should be "Fecha inválida"

  Scenario: Fail to load cost with invalid year
    Given I have a role name "Desarrollador"
    And a role experience "Senior"
    And a role cost "3000"
    And a month "12"
    And a year "2025"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 400
    And the response should be "Fecha inválida"

  Scenario: Fail to load cost with a negative amount
    Given I have a role name "Desarrollador"
    And a role experience "Senior"
    And a role cost "-100"
    When I POST to the route "/api/v1/finanzas/cargar-costo"
    Then the status code should be 400
    And the response should be "El nuevo costo del recurso no puede ser un monto negativo"
