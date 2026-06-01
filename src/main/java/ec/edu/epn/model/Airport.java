package ec.edu.epn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(unique = true, nullable = false, length = 3)
    private String code;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false)
    private String country;

    @JsonIgnoreProperties({"origin", "destination"})
    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> departures = new ArrayList<>();

    @JsonIgnoreProperties({"origin", "destination"})
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> arrivals = new ArrayList<>();

    public Airport() {}

    public Airport(String name, String code, String city, String country) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public List<Flight> getDepartures() { return departures; }
    public void setDepartures(List<Flight> departures) { this.departures = departures; }

    public List<Flight> getArrivals() { return arrivals; }
    public void setArrivals(List<Flight> arrivals) { this.arrivals = arrivals; }
}
