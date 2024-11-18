package fi.uba.memo1.apirest.finanzas.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FinanzasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void cargarCostoExitosamente(){
		CargarCostoRequest request = new CargarCostoRequest();
		request.setCosto(1000);
		request.setNombre("Desarrollador");
		request.setExperiencia("Senior");

		HttpEntity<CargarCostoRequest> entity = new HttpEntity<>(request);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/finanzas/cargar-costo", entity, String.class);

		assertEquals(200, response.getStatusCode().value());
		assertEquals("Costo cargado correctamente", response.getBody());
	}

	@Test
	void noSePuedeCargarCostoConNombreInexistente(){
		CargarCostoRequest request = new CargarCostoRequest();
		request.setCosto(1000);
		request.setNombre("Administrador");
		request.setExperiencia("Senior");

		HttpEntity<CargarCostoRequest> entity = new HttpEntity<>(request);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/finanzas/cargar-costo", entity, String.class);

		assertEquals(400, response.getStatusCode().value());
		assertEquals("Rol no encontrado", response.getBody());
	}

	@Test
	void noSePuedeCargarCostoConExperienciaInexistente(){
		CargarCostoRequest request = new CargarCostoRequest();
		request.setCosto(1000);
		request.setNombre("Desarrollador");
		request.setExperiencia("Entry");

		HttpEntity<CargarCostoRequest> entity = new HttpEntity<>(request);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/finanzas/cargar-costo", entity, String.class);

		assertEquals(400, response.getStatusCode().value());
		assertEquals("Rol no encontrado", response.getBody());
	}

	@Test
	void seAgregaElCostoALaTablaCorrectamente() throws JsonProcessingException {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/finanzas/costos", String.class);
		ObjectMapper mapper = new ObjectMapper();
		List items = mapper.readValue(response.getBody(), List.class);
		int count = items.size();

		CargarCostoRequest request = new CargarCostoRequest();
		request.setCosto(1000);
		request.setNombre("Desarrollador");
		request.setExperiencia("Senior");

		HttpEntity<CargarCostoRequest> entity = new HttpEntity<>(request);
		restTemplate.postForEntity("/api/v1/finanzas/cargar-costo", entity, String.class);

		response = restTemplate.getForEntity("/api/v1/finanzas/costos", String.class);
		items = mapper.readValue(response.getBody(), List.class);
		int newCount = items.size();

		assertEquals(count + 1, newCount);
	}
}
