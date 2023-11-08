package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.EndpointHitDto;
import ru.practicum.VisitorsStatsDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHitDto addHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<VisitorsStatsDto> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStatistic(start, end, uris, unique);
    }
}