package ec.edu.epn.service;

import ec.edu.epn.dto.AirportRequest;
import ec.edu.epn.exception.ResourceNotFoundException;
import ec.edu.epn.model.Airport;
import ec.edu.epn.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport create(AirportRequest request) {
        if (airportRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("El código de aeropuerto ya existe: " + request.getCode());
        }
        Airport airport = new Airport(
            request.getName(), request.getCode(),
            request.getCity(), request.getCountry()
        );
        return airportRepository.save(airport);
    }

    @Transactional(readOnly = true)
    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Airport findById(Long id) {
        return airportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Airport findByCode(String code) {
        return airportRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto no encontrado con código: " + code));
    }

    @Transactional(readOnly = true)
    public List<Airport> findByCity(String city) {
        return airportRepository.findByCity(city);
    }

    @Transactional(readOnly = true)
    public List<Airport> findByCountry(String country) {
        return airportRepository.findByCountry(country);
    }

    public Airport update(Long id, AirportRequest request) {
        Airport airport = findById(id);
        airport.setName(request.getName());
        airport.setCode(request.getCode());
        airport.setCity(request.getCity());
        airport.setCountry(request.getCountry());
        return airportRepository.save(airport);
    }

    public void delete(Long id) {
        Airport airport = findById(id);
        airportRepository.delete(airport);
    }
}
