package ec.edu.epn.repository;

import ec.edu.epn.model.Airport;
import ec.edu.epn.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByOrigin(Airport origin);

    List<Flight> findByDestination(Airport destination);

    List<Flight> findByStatus(String status);

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE f.departureTime BETWEEN :start AND :end")
    List<Flight> findFlightsBetweenDates(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query("SELECT f FROM Flight f WHERE f.origin.code = :originCode AND f.destination.code = :destCode")
    List<Flight> findByOriginAndDestinationCodes(@Param("originCode") String originCode,
                                                  @Param("destCode") String destCode);
}
