package ec.edu.epn.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ec.edu.epn.dto.AirportRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AirportControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Crear un aeropuerto y verificar HTTP 201 + datos en la respuesta")
    public void shouldCreateAirport() throws Exception {
        // Arrange
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Aeropuerto Mariscal Sucre");
        airportRequest.setCity("Quito");
        airportRequest.setCode("UIO");
        airportRequest.setCountry("Ecuador");

        // Act + Assert
        mockMvc.perform(post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Aeropuerto Mariscal Sucre"))
                .andExpect(jsonPath("$.code").value("UIO"))
                .andExpect(jsonPath("$.city").value("Quito"))
                .andExpect(jsonPath("$.country").value("Ecuador"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Eliminar y verificar que ya no existe (GET → 404)")
    void shouldDeleteAirport() throws Exception {
        // Arrange
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Santiago de Chile");
        airportRequest.setCity("Santiago");
        airportRequest.setCode("SCL");
        airportRequest.setCountry("Chile");

        String response = createAirport(airportRequest);

        Long id = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/api/airports/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/airports/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Actualizar y verificar los cambios")
    void shouldUpdateAirport() throws Exception {
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Santiago de Chile");
        airportRequest.setCity("Santiago");
        airportRequest.setCode("SCL");
        airportRequest.setCountry("Chile");

        String response = createAirport(airportRequest);
        Long id = objectMapper.readTree(response).get("id").asLong();

        airportRequest.setName("Aeropuerto Santiago de Chile");

        mockMvc.perform(put("/api/airports/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aeropuerto Santiago de Chile"));

    }

    @Test
    @DisplayName("Intentar crear dos aeropuertos con el mismo código IATA")
    void shouldRejectDuplicateAirportCode() throws Exception {
        AirportRequest air1 = new AirportRequest();
        air1.setName("Aeropuerto de España");
        air1.setCity("Madrid");
        air1.setCode("ESP");
        air1.setCountry("España");

        createAirport(air1);

        AirportRequest air2 = new AirportRequest();
        air2.setName("Aeropuerto de España");
        air2.setCity("Asturias");
        air2.setCode("ESP");
        air2.setCountry("España");

        mockMvc.perform(post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(air2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar todos los aeropuertos (Crea 2 antes)")
    void shouldFindAllAirports() throws Exception {
        AirportRequest air1 = new AirportRequest();
        air1.setName("Aeropuerto de España");
        air1.setCity("Madrid");
        air1.setCode("ESP");
        air1.setCountry("España");

        createAirport(air1);

        AirportRequest air2 = new AirportRequest();
        air2.setName("Aeropuerto de México");
        air2.setCity("Ciudad de México");
        air2.setCode("MEX");
        air2.setCountry("Mexico");

        createAirport(air2);

        mockMvc.perform(get("/api/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    @DisplayName("Buscar por ID y verificar los datos")
    void shouldFindAirportById() throws Exception {
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Aeropuerto de Quito");
        airportRequest.setCity("Quito");
        airportRequest.setCode("UIO");
        airportRequest.setCountry("Ecuador");

        String response = createAirport(airportRequest);

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/airports/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Aeropuerto de Quito"))
                .andExpect(jsonPath("$.city").value("Quito"))
                .andExpect(jsonPath("$.code").value("UIO"))
                .andExpect(jsonPath("$.country").value("Ecuador"));
    }

    @Test
    @DisplayName("Buscar por código IATA")
    void shouldFindAirportByCode() throws Exception {
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Aeropuerto de Guayaquil");
        airportRequest.setCity("Guayaquil");
        airportRequest.setCode("GUA");
        airportRequest.setCountry("Ecuador");

        createAirport(airportRequest);

        mockMvc.perform(get("/api/airports/code/GUA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("GUA"))
                .andExpect(jsonPath("$.city").value("Guayaquil"))
                .andExpect(jsonPath("$.country").value("Ecuador"));
    }

    // isNotFound()
    @Test
    @DisplayName("")
    void shouldReturn404WhenAirportNotFound() throws Exception {
        mockMvc.perform(get("/api/airports/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Enviar datos inválidos y verificar HTTP 400")
    void shouldRejectInvalidAirportRequest() throws JsonProcessingException, Exception {
        AirportRequest airportRequest = new AirportRequest();

        airportRequest.setName("");
        airportRequest.setCity("");
        airportRequest.setCode("AB");
        airportRequest.setCountry("");

        mockMvc.perform(post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportRequest)))
                .andExpect(status().isBadRequest());

    }

    private String createAirport(AirportRequest airportRequest) throws Exception {
        return mockMvc.perform(post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

}
