package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.VisitorsStatsDto;
import ru.practicum.stats.dto.EndpointMapper;
import ru.practicum.stats.dto.VisitorDtoWithoutEndpoint;
import ru.practicum.stats.dto.VisitorMapper;
import ru.practicum.stats.model.Endpoint;
import ru.practicum.stats.model.Visitor;
import ru.practicum.stats.storage.EndpointRepository;
import ru.practicum.stats.storage.VisitorRepository;

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

    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        String uri = endpointHitDto.getUri();
        String app = endpointHitDto.getApp();
        Endpoint endpoint;
        if (!endpointRepository.existsByUriAndApp(uri, app)) {
            endpoint = endpointRepository.save(endpointMapper.toEndpoint(endpointHitDto));
        } else {
            endpoint = endpointRepository.getByUriAndApp(uri, app);
        }
        VisitorDtoWithoutEndpoint visitorDtoWithoutEndpoint = endpointMapper.toVisitor(endpointHitDto);
        Visitor visitor = visitorRepository.save(visitorMapper.toVisitor(visitorDtoWithoutEndpoint, endpoint));
        return endpointMapper.toEndpointHitDto(visitor);
    }

    public List<VisitorsStatsDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);
        List<VisitorsStatsDto> stats = new ArrayList<>();
        if (uris.isEmpty()) {
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
}