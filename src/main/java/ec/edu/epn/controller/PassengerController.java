package ec.edu.epn.controller;

import ec.edu.epn.dto.PassengerRequest;
import ec.edu.epn.model.Passenger;
import ec.edu.epn.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public ResponseEntity<Passenger> create(@Valid @RequestBody PassengerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Passenger>> findAll() {
        return ResponseEntity.ok(passengerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> findById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Passenger> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(passengerService.findByEmail(email));
    }

    @GetMapping("/passport/{passportNumber}")
    public ResponseEntity<Passenger> findByPassportNumber(@PathVariable String passportNumber) {
        return ResponseEntity.ok(passengerService.findByPassportNumber(passportNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> update(@PathVariable Long id, @Valid @RequestBody PassengerRequest request) {
        return ResponseEntity.ok(passengerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
