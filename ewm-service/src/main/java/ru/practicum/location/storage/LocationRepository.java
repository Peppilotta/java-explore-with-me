package ru.practicum.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByLatAndLon(Float lat, Float lon);

    @Query("select l from Location l where l.lat = :lat and l.lon = :lon")
    Location findByLatAndLon(@Param("lat") Float lat, @Param("lon") Float lon);
}