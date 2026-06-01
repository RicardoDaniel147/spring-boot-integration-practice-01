package ec.edu.epn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AirportRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El código IATA es obligatorio")
    @Size(min = 3, max = 3, message = "El código debe tener 3 caracteres")
    private String code;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
