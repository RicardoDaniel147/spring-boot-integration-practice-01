package ec.edu.epn.service;

import ec.edu.epn.dto.FlightRequest;
import ec.edu.epn.exception.ResourceNotFoundException;
import ec.edu.epn.model.Airport;
import ec.edu.epn.model.Flight;
import ec.edu.epn.model.Passenger;
import ec.edu.epn.repository.AirportRepository;
import ec.edu.epn.repository.FlightRepository;
import ec.edu.epn.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final PassengerRepository passengerRepository;

    public FlightService(FlightRepository flightRepository,
                         AirportRepository airportRepository,
                         PassengerRepository passengerRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.passengerRepository = passengerRepository;
    }

    public Flight create(FlightRequest request) {
        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new IllegalArgumentException("El número de vuelo ya existe: " + request.getFlightNumber());
        }
        if (request.getArrivalTime().isBefore(request.getDepartureTime())) {
            throw new IllegalArgumentException("La llegada no puede ser antes de la salida");
        }

        Airport origin = airportRepository.findById(request.getOriginId())
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto origen no encontrado"));
        Airport destination = airportRepository.findById(request.getDestinationId())
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto destino no encontrado"));

        Flight flight = new Flight(
            request.getFlightNumber(), origin, destination,
            request.getDepartureTime(), request.getArrivalTime(), request.getStatus()
        );
        return flightRepository.save(flight);
    }

    @Transactional(readOnly = true)
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Flight findById(Long id) {
        return flightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Flight findByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado: " + flightNumber));
    }

    @Transactional(readOnly = true)
    public List<Flight> findByStatus(String status) {
        return flightRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Flight> findFlightsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return flightRepository.findFlightsBetweenDates(start, end);
    }

    public Flight update(Long id, FlightRequest request) {
        Flight flight = findById(id);
        Airport origin = airportRepository.findById(request.getOriginId())
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto origen no encontrado"));
        Airport destination = airportRepository.findById(request.getDestinationId())
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto destino no encontrado"));

        flight.setFlightNumber(request.getFlightNumber());
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setStatus(request.getStatus());
        return flightRepository.save(flight);
    }

    public Flight addPassenger(Long flightId, Long passengerId) {
        Flight flight = findById(flightId);
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));
        flight.getPassengers().add(passenger);
        return flightRepository.save(flight);
    }

    public Flight removePassenger(Long flightId, Long passengerId) {
        Flight flight = findById(flightId);
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));
        flight.getPassengers().remove(passenger);
        return flightRepository.save(flight);
    }

    public void delete(Long id) {
        Flight flight = findById(id);
        flightRepository.delete(flight);
    }
}
