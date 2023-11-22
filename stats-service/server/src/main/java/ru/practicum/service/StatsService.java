package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.VisitorsStatsDto;
import ru.practicum.dto.EndpointMapper;
import ru.practicum.dto.EndpointWithoutVisitors;
import ru.practicum.dto.VisitorMapper;
import ru.practicum.dto.VisitorWithoutEndpoint;
import ru.practicum.model.Endpoint;
import ru.practicum.model.Visitor;
import ru.practicum.statexception.BadRequestException;
import ru.practicum.storage.EndpointRepository;
import ru.practicum.storage.VisitorRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final EndpointRepository endpointRepository;

    private final VisitorRepository visitorRepository;

    private final EndpointMapper endpointMapper;

    private final VisitorMapper visitorMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        String uri = endpointHitDto.getUri();
        String app = endpointHitDto.getApp();
        log.info("app = {}, uri = {}", app, uri);
        EndpointWithoutVisitors endpoint;
        boolean endpointExists = endpointRepository.existsByUriAndApp(uri, app);
        log.info("endpointExists = {}", endpointExists);
        if (endpointExists) {
            endpoint = endpointRepository.findEndpointByUriAndApp(uri, app);
            log.info("endpoint Exists = {}", endpoint.toString());
        } else {
            Endpoint fromHit = endpointMapper.toEndpoint(endpointHitDto);
            log.info("fromHit = {}", fromHit.toString());
            endpoint = endpointMapper.toWithoutVisitors(endpointRepository.save(fromHit));

            log.info("endpoint = {}", endpoint);
        }
        VisitorWithoutEndpoint visitorWithoutEndpoint = endpointMapper.toVisitor(endpointHitDto);
        Visitor visitor = visitorRepository.save(visitorMapper.toVisitor(visitorWithoutEndpoint, endpoint));
        log.info("visitor = {}", visitor);
        return endpointMapper.toEndpointHitDto(visitor, endpoint);
    }

    public List<VisitorsStatsDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {

        LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);

        if (dateTimeEnd.isBefore(dateTimeEnd)) {
            throw new BadRequestException("End is before start");
        }
        List<VisitorsStatsDto> stats = new ArrayList<>();
        if (Objects.isNull(uris)) {
            stats.addAll((Objects.isNull(unique) || !unique)
                    ? endpointRepository.getAllUrisFromList(dateTimeStart, dateTimeEnd)
                    : endpointRepository.getAllUniqueUrisFromList(dateTimeStart, dateTimeEnd));
        } else {
            stats.addAll((Objects.isNull(unique) || !unique)
                    ? endpointRepository.getUrisFromList(dateTimeStart, dateTimeEnd, uris)
                    : endpointRepository.getUniqueUrisFromList(dateTimeStart, dateTimeEnd, uris));
        }
        return stats;
    }

    public Long getVisitorsIp(String app, String uri, String ip) {
        return endpointRepository.getUniqueIp(app, uri, ip);
    }
}