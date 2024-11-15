package fi.uba.memo1.apirest.finanzas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSpringConfiguration {
    @Autowired
    protected TestRestTemplate testRestTemplate;

}