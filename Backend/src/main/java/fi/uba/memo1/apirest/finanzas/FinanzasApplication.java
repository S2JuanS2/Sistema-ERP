package fi.uba.memo1.apirest.finanzas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) // activar para no usar base de datos
@SpringBootApplication
public class FinanzasApplication {

	public static void main(String[] args) {
		String id = "a6e2167f-67a1-4f60-b9e9-6bae7bc3a15";
		UUID uuid = UUID.fromString(id);
		System.out.println("UUID válido: " + uuid);
		SpringApplication.run(FinanzasApplication.class, args);
	}
}
