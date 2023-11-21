package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.EndpointHitDto;
import ru.practicum.VisitorsStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto addHit(@RequestBody @Valid final EndpointHitDto endpointHitDto) {
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<VisitorsStatsDto> getStats(@RequestParam @Past String start,
                                           @RequestParam @PastOrPresent String end,
                                           @RequestParam List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStatistic(start, end, uris, unique);
    }

    @GetMapping("/visitors")
    Long getVisitorsIp(@RequestParam String app, @RequestParam String uri, String ip) {
        return statsService.getVisitorsIp(app, uri, ip);
    }
}