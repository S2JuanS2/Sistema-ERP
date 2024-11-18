package fi.uba.memo1.apirest.finanzas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) // activar para no usar base de datos
@SpringBootApplication
public class FinanzasApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanzasApplication.class, args);
	}
}
