package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000, message = "Size of annotation must be between {min} and {max}")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Size of description must be between {min} and {max}")
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, max = 120, message = "Size of title must be between {min} and {max}")
    private String title;
}