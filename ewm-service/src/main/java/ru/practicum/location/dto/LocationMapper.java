package ru.practicum.location.dto;

import org.springframework.stereotype.Component;
import ru.practicum.location.model.Location;

@Component
public class LocationMapper {

    public LocationDto toDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}