package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

        import java.util.List;

@FeignClient(value = "statsClient", url = "${EWM_SERVER_URL}")
public interface StatsClient {

    @PostMapping(path = "/hit")
    EndpointHitDto postHit(@RequestBody EndpointHitDto endpointHitDto);

    @GetMapping("/stats")
    List<VisitorsStatsDto> getStat(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique);

    @GetMapping("/visitors")
    Long getVisitorsIp(@RequestParam String app, @RequestParam String uri, @RequestParam String ip);
}
