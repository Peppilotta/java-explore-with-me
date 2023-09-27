package ru.practicum.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.stats.model.Endpoint;
import ru.practicum.stats.model.Visitor;

@Component
public class VisitorMapper {

    public VisitorDto toWithEndpoint(VisitorDtoWithoutEndpoint visitor, Long id) {
        return VisitorDto.builder()
                .endpointId(id)
                .timestamp(visitor.getTimestamp())
                .ip(visitor.getIp())
                .build();
    }

    public Visitor toVisitor(VisitorDtoWithoutEndpoint visitor, Endpoint endpoint) {
        return Visitor.builder()
                .endpoint(endpoint)
                .timestamp(visitor.getTimestamp())
                .ip(visitor.getIp())
                .build();
    }
}