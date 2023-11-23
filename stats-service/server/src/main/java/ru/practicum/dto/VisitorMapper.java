package ru.practicum.dto;

import org.springframework.stereotype.Component;
import ru.practicum.model.Visitor;

@Component
public class VisitorMapper {
    private final EndpointMapper endpointMapper;

    public VisitorMapper(EndpointMapper endpointMapper) {
        this.endpointMapper = endpointMapper;
    }

    public Visitor toVisitor(VisitorWithoutEndpoint visitor, EndpointWithoutVisitors endpoint) {
        Visitor converted = new Visitor();
        converted.setEndpoint(endpointMapper.fromWithoutVisitors(endpoint));
        converted.setTimestamp(visitor.getTimestamp());
        converted.setIp(visitor.getIp());
        return converted;
    }
}