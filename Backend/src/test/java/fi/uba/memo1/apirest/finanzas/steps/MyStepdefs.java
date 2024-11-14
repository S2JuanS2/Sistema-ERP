package fi.uba.memo1.apirest.finanzas.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MyStepdefs {
    private String route;
    private ResponseEntity<String> response;

    @Given("the route {string}")
    public void theRoute(String endpoint) {
        this.route = "http://localhost:8080" + endpoint;
    }

    @When("I send a GET request")
    public void iSendAGETRequest() {
        RestTemplate restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(route, String.class);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertEquals(HttpStatusCode.valueOf(statusCode), response.getStatusCode());
    }

    @And("the response body should be {string}")
    public void theResponseBodyShouldBe(String body) {
        assertEquals(body, response.getBody());
    }
}
