package ru.practicum.dto;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.Endpoint;
import ru.practicum.model.Visitor;

import java.util.ArrayList;

@Component
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointHitDto endpointHitDto) {
        Endpoint endpoint = new Endpoint();
        endpoint.setApp(endpointHitDto.getApp());
        endpoint.setUri(endpointHitDto.getUri());
        return endpoint;
    }

    public VisitorWithoutEndpoint toVisitor(EndpointHitDto endpointHitDto) {
        VisitorWithoutEndpoint visitor = new VisitorWithoutEndpoint();
        visitor.setIp(endpointHitDto.getIp());
        visitor.setTimestamp(endpointHitDto.getTimestamp());
        return visitor;
    }

    public EndpointHitDto toEndpointHitDto(Visitor visitor, EndpointWithoutVisitors endpoint) {
        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp(endpoint.getApp());
        hit.setUri(endpoint.getUri());
        hit.setIp(visitor.getIp());
        hit.setTimestamp(visitor.getTimestamp());
        return hit;
    }

    public EndpointWithoutVisitors toWithoutVisitors(Endpoint endpoint) {
        EndpointWithoutVisitors endpointWithoutVisitors = new EndpointWithoutVisitors();
        endpointWithoutVisitors.setId(endpoint.getId());
        endpointWithoutVisitors.setApp(endpoint.getApp());
        endpointWithoutVisitors.setUri(endpoint.getUri());
        return endpointWithoutVisitors;
    }

    public Endpoint fromWithoutVisitors(EndpointWithoutVisitors endpoint) {
        Endpoint converted = new Endpoint();
        converted.setId(endpoint.getId());
        converted.setApp(endpoint.getApp());
        converted.setUri(endpoint.getUri());
        converted.setVisitors(new ArrayList<>());
        return converted;
    }
}