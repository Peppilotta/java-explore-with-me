package ru.practicum.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.model.Endpoint;
import ru.practicum.stats.model.Visitor;

import java.util.ArrayList;

@Component
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointHitDto endpointHitDto) {
        return Endpoint.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .build();
    }

    public VisitorWithoutEndpoint toVisitor(EndpointHitDto endpointHitDto) {
        return VisitorWithoutEndpoint.builder()
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public EndpointHitDto toEndpointHitDto(Visitor visitor, EndpointWithoutVisitors endpoint) {
        return EndpointHitDto.builder()
                .id(visitor.getId())
                .app(endpoint.getApp())
                .uri(endpoint.getUri())
                .ip(visitor.getIp())
                .timestamp(visitor.getTimestamp())
                .build();
    }

    public EndpointWithoutVisitors toWithoutVisitors(Endpoint endpoint) {
        return EndpointWithoutVisitors.builder()
                .id(endpoint.getId())
                .app(endpoint.getApp())
                .uri(endpoint.getUri())
                .build();
    }

    public Endpoint fromWithoutVisitors(EndpointWithoutVisitors endpoint) {
        return Endpoint.builder()
                .id(endpoint.getId())
                .app(endpoint.getApp())
                .uri(endpoint.getUri())
                .visitors(new ArrayList<>())
                .build();
    }
}