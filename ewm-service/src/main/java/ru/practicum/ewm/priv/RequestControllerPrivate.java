package ru.practicum.ewm.priv;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
public class RequestControllerPrivate {

    private final RequestServicePrivate service;

    public RequestControllerPrivate(RequestServicePrivate service) {
        this.service = service;
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable(name = "userId") @Positive Long userId) {
        return service.getRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto getRequest(@PathVariable(name = "userId") @Positive Long userId,
                                              @RequestParam @Positive Long eventId) {
        return service.getRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                 @PathVariable(name = "requestId") @Positive Long requestId) {
        return service.cancelRequest(userId, requestId);
    }
}