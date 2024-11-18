package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CargarCostosSteps {
    private String name;
    private double cost;
    private String experience;
    private ResponseEntity<String> response;

    @Autowired
    private TestRestTemplate restTemplate;


    @And("a role experience {string}")
    public void aRoleExperience(String experience) {
        this.experience = experience;
    }

    @Given("I have a role name {string}")
    public void iHaveARoleName(String name) {
        this.name = name;
    }

    @When("I POST to the route {string}")
    public void iPOSTToTheRoute(String route) {
        CargarCostoRequest request = new CargarCostoRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(cost);

        HttpEntity<CargarCostoRequest> requestEntity = new HttpEntity<>(request);
        this.response = restTemplate.postForEntity(restTemplate.getRootUri() + route, requestEntity, String.class);
    }

    @And("a role cost {string}")
    public void aRoleCost(String cost) {
        this.cost = Double.parseDouble(cost);
    }

    @Then("the status code should be {int}")
    public void theStatusCodeShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("the response should be {string}")
    public void theResponseShouldBe(String response) {
        assertEquals(response, this.response.getBody());
    }
}
