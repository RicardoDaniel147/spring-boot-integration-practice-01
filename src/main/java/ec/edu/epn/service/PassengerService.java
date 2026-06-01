package ec.edu.epn.service;

import ec.edu.epn.dto.PassengerRequest;
import ec.edu.epn.exception.ResourceNotFoundException;
import ec.edu.epn.model.Passenger;
import ec.edu.epn.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger create(PassengerRequest request) {
        if (passengerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.getEmail());
        }
        if (passengerRepository.existsByPassportNumber(request.getPassportNumber())) {
            throw new IllegalArgumentException("El pasaporte ya está registrado: " + request.getPassportNumber());
        }
        Passenger passenger = new Passenger(
            request.getFirstName(), request.getLastName(),
            request.getEmail(), request.getPassportNumber()
        );
        return passengerRepository.save(passenger);
    }

    @Transactional(readOnly = true)
    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Passenger findById(Long id) {
        return passengerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Passenger findByEmail(String email) {
        return passengerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con email: " + email));
    }

    @Transactional(readOnly = true)
    public Passenger findByPassportNumber(String passportNumber) {
        return passengerRepository.findByPassportNumber(passportNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con pasaporte: " + passportNumber));
    }

    public Passenger update(Long id, PassengerRequest request) {
        Passenger passenger = findById(id);
        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setEmail(request.getEmail());
        passenger.setPassportNumber(request.getPassportNumber());
        return passengerRepository.save(passenger);
    }

    public void delete(Long id) {
        Passenger passenger = findById(id);
        passengerRepository.delete(passenger);
    }
}
