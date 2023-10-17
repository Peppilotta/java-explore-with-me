package ru.practicum.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.stats.model.Visitor;

@Component
public class VisitorMapper {
    private final EndpointMapper endpointMapper;

    public VisitorMapper(EndpointMapper endpointMapper) {
        this.endpointMapper = endpointMapper;
    }

    public Visitor toVisitor(VisitorWithoutEndpoint visitor, EndpointWithoutVisitors endpoint) {
        return Visitor.builder()
                .endpoint(endpointMapper.fromWithoutVisitors(endpoint))
                .timestamp(visitor.getTimestamp())
                .ip(visitor.getIp())
                .build();
    }
}