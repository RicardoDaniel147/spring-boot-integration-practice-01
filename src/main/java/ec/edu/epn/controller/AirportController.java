package ec.edu.epn.controller;

import ec.edu.epn.dto.AirportRequest;
import ec.edu.epn.model.Airport;
import ec.edu.epn.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping
    public ResponseEntity<Airport> create(@Valid @RequestBody AirportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(airportService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Airport>> findAll() {
        return ResponseEntity.ok(airportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> findById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Airport> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(airportService.findByCode(code));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Airport>> findByCity(@RequestParam(required = false) String city,
                                                     @RequestParam(required = false) String country) {
        if (city != null) return ResponseEntity.ok(airportService.findByCity(city));
        if (country != null) return ResponseEntity.ok(airportService.findByCountry(country));
        return ResponseEntity.ok(airportService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> update(@PathVariable Long id, @Valid @RequestBody AirportRequest request) {
        return ResponseEntity.ok(airportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        airportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
