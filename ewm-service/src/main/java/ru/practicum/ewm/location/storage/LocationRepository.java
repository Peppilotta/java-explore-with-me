package ru.practicum.ewm.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByLatAndLon(Float lat, Float lon);

    @Query("select l from Location l where l.lat = :lat and l.lon = :lon")
    Location findByLatAndLon(Float lat, Float lon);
}