package ru.practicum.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.model.Endpoint;
import ru.practicum.stats.model.Visitor;

@Component
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointHitDto endpointHitDto) {
        return Endpoint.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .build();
    }

    public VisitorDtoWithoutEndpoint toVisitor(EndpointHitDto endpointHitDto) {
        return VisitorDtoWithoutEndpoint.builder()
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public EndpointHitDto toEndpointHitDto(Visitor visitor) {
        return EndpointHitDto.builder()
                .id(visitor.getId())
                .app(visitor.getEndpoint().getApp())
                .uri(visitor.getEndpoint().getUri())
                .ip(visitor.getIp())
                .timestamp(visitor.getTimestamp())
                .build();
    }
}