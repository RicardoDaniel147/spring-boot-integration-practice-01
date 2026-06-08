package ec.edu.epn.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void shouldDeleteAirport() throws Exception {
        // Arrange
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Santiago de Chile");
        airportRequest.setCity("Santiago");
        airportRequest.setCode("SCL");
        airportRequest.setCountry("Chile");

        String response = createAirport(airportRequest);

        Long id = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/api/airport/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/airport/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateAirport() throws Exception {
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setName("Santiago de Chile");
        airportRequest.setCity("Santiago");
        airportRequest.setCode("SCL");
        airportRequest.setCountry("Chile");

        String response = createAirport(airportRequest);
        Long id = objectMapper.readTree(response).get("id").asLong();

        airportRequest.setName("Aeropuerto Santiago de Chile");

        mockMvc.perform(put("/api/airport/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        )
    }

    private String createAirport(AirportRequest airportRequest) throws Exception {
        return mockMvc.perform(post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

}
