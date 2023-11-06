package ru.practicum.ewm.location.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.location.model.Location;

@Component
public class LocationMapper {

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}