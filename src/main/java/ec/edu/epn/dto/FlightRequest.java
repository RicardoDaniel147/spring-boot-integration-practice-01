package ec.edu.epn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FlightRequest {

    @NotBlank(message = "El número de vuelo es obligatorio")
    private String flightNumber;

    @NotNull(message = "El ID del aeropuerto de origen es obligatorio")
    private Long originId;

    @NotNull(message = "El ID del aeropuerto de destino es obligatorio")
    private Long destinationId;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime departureTime;

    @NotNull(message = "La fecha de llegada es obligatoria")
    private LocalDateTime arrivalTime;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Long getOriginId() { return originId; }
    public void setOriginId(Long originId) { this.originId = originId; }

    public Long getDestinationId() { return destinationId; }
    public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
