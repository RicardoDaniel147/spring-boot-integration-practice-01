package ec.edu.epn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String flightNumber;

    @JsonIgnoreProperties({"departures", "arrivals"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", nullable = false)
    private Airport origin;

    @JsonIgnoreProperties({"departures", "arrivals"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Airport destination;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime departureTime;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @NotBlank
    @Column(nullable = false)
    private String status;

    @JsonIgnoreProperties({"flights"})
    @ManyToMany
    @JoinTable(
        name = "flight_passengers",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private Set<Passenger> passengers = new HashSet<>();

    public Flight() {}

    public Flight(String flightNumber, Airport origin, Airport destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, String status) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Airport getOrigin() { return origin; }
    public void setOrigin(Airport origin) { this.origin = origin; }

    public Airport getDestination() { return destination; }
    public void setDestination(Airport destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Set<Passenger> getPassengers() { return passengers; }
    public void setPassengers(Set<Passenger> passengers) { this.passengers = passengers; }
}
