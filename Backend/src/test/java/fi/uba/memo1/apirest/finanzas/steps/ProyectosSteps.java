package fi.uba.memo1.apirest.finanzas.steps;


import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.TotalCostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.netty.channel.ChannelOption;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProyectosSteps {
    @Autowired
    private CostosMensualesService costosMensualesService;

    private MockWebServer mockWebServer;

    private final String anio = "2024";
    private String mes;

    private Double totalCost;

    private Exception exception;

    private TotalCostosProyectoResponse response;

    @Before
    public void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
        costosMensualesService = null;
    }

    @Given("I am on the page that allows me to get the total cost of a project")
    public void iAmOnThePageThatAllowsMeToGetTheTotalCostOfAProject() {
    }

    @When("I get the total cost of all projects")
    public void iGetTheTotalCostOfAllProjects() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "projects": [
                                {
                                    "id": "a6e2167f-67a1-4f60-b9e9-6bae7bc3a15",
                                    "years": {
                                        "2024": {
                                            "1": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            },
                                            "2": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            },
                                            "3": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            }
                                        }
                                    }
                                },
                                {
                                    "id": "1635b4ca-c091-472c-8b5a-cb3086d197b",
                                    "years": {
                                        "2024": {
                                            "1": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                        """)
        );

        WebClient horasWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(Duration.ofSeconds(5))))
                .build();

        costosMensualesService.setHorasWebClient(horasWebClient);

        this.response = costosMensualesService.obtenerCostosDeProyectos(anio).block();
    }

    @And("I have a project with name {string} that lasted {int} month and cost {string}")
    public void iHaveAProjectWithNameThatLastedMonthAndCost(String name, int months, String cost) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        List<CostosMensualesResponse> costosYaAgregados = costosMensualesService.findAll().block();


        for (int i = 0; i < months; i++) {
            boolean encontrado = false;
            for (CostosMensualesResponse costosMensualesResponse : Objects.requireNonNull(costosYaAgregados)) {
                if(costosMensualesResponse.getMes().equalsIgnoreCase(Integer.toString(i + 1)) && costosMensualesResponse.getAnio().equalsIgnoreCase(anio)){
                    encontrado = true;
                    break;
                }
            }

            if(encontrado){
                continue;
            }

            CostosMensualesRequest request = new CostosMensualesRequest();
            String rol = "Desarrollador";
            request.setNombre(rol);
            String experiencia = "Senior";
            request.setExperiencia(experiencia);
            request.setCosto(Double.parseDouble(cost));
            request.setAnio(anio);
            request.setMes((Integer.toString(i + 1)));

            requestList.add(request);
        }

        costosMensualesService.save(requestList).block();

    }

    @Then("the system should return the total cost of the project {string}")
    public void theSystemShouldReturnTheTotalCostOfTheProject(String cost) {
        assertEquals(Double.parseDouble(cost), this.totalCost);
    }

    @When("I get the total cost of the project")
    public void iGetTheTotalCostOfTheProject() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "projects": [
                                {
                                    "id": "1635b4ca-c091-472c-8b5a-cb3086d197b",
                                    "years": {
                                        "2024": {
                                            "1": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1,
                                                "a21147f8-6538-46d8-95ac-9ddf95ff8c29": 1
                                            },
                                            "2": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            },
                                            "3": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                        """)
        );

        WebClient horasWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(Duration.ofSeconds(5))))
                .build();

        costosMensualesService.setHorasWebClient(horasWebClient);

        this.totalCost = Objects.requireNonNull(costosMensualesService.obtenerCostosDeProyectos(anio).block()).getProyectos().get(0).getCostoTotal();
    }

    @Then("the system should return the total cost of all the projects {string}")
    public void theSystemShouldReturnTheTotalCostOfAllTheProjects(String cost) {
        assertEquals(Double.parseDouble(cost), this.response.getCostoTotalGlobal());
    }

    @When("I try to get the cost of a project in the year {string} and month {string}")
    public void iTryToGetTheCostOfAProjectInTheYearAndMonth(String year, String month) {
        try {
            this.costosMensualesService.obtenerCostosDeProyectos(year).block();
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("The system should return an error message")
    public void theSystemShouldReturnAnErrorMessage() {
        assertEquals("No se encontraron costos para el proyecto", this.exception.getMessage());
    }

    @And("I have a project with name {string} in the year {string} and month {string}")
    public void iHaveAProjectWithNameInTheYearAndMonth(String name, String year, String month) {
        this.mes = month;
    }

    @And("a role {string} and experience {string} and cost {string}")
    public void aRoleAndExperienceAndCost(String role, String experience, String cost) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        List<CostosMensualesResponse> costosYaAgregados = costosMensualesService.findAll().block();

        for (CostosMensualesResponse costosMensualesResponse : Objects.requireNonNull(costosYaAgregados)) {
            if(costosMensualesResponse.getMes().equalsIgnoreCase(this.mes) && costosMensualesResponse.getAnio().equalsIgnoreCase(anio) && costosMensualesResponse.getRol().getNombre().equalsIgnoreCase(role) && costosMensualesResponse.getRol().getExperiencia().equalsIgnoreCase(experience)){
                return;
            }
        }

        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(role);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));
        request.setAnio(anio);
        request.setMes(this.mes);

        requestList.add(request);

        costosMensualesService.save(requestList).block();
    }

    @Then("the total cost of the project should be {string}")
    public void theTotalCostOfTheProjectShouldBe(String cost) {
        assertEquals(Double.parseDouble(cost), this.totalCost);
    }


    @When("I get total cost of the project")
    public void iGetTotalCostOfTheProject() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "projects": [
                                {
                                    "id": "1635b4ca-c091-472c-8b5a-cb3086d197b",
                                    "years": {
                                        "2024": {
                                            "1": {
                                                "ff14a491-e26d-4092-86ea-d76f20c165d1": 1
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                        """)
        );

        WebClient horasWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(Duration.ofSeconds(5))))
                .build();

        costosMensualesService.setHorasWebClient(horasWebClient);

        this.totalCost = Objects.requireNonNull(costosMensualesService.obtenerCostosDeProyectos(anio).block()).getProyectos().get(0).getCostoTotal();
    }
}
