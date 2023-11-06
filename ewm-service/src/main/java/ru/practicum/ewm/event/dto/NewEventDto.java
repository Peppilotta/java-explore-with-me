package ru.practicum.ewm.event.dto;

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
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewEventDto {

    @NotNull(message = "Field: annotation. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: annotation. Error: must not be blank. Value: blank")
    @Size(min = 20, max = 2000, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String annotation;

    @NotNull(message = "Field: category. Error: must not be blank. Value: null")
    private Long category;

    @NotNull(message = "Field: description. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: description. Error: must not be blank. Value: blank")
    @Size(min = 20, max = 7000, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Field: initiator. Error: must not be blank. Value: null")
    private UserShortDto initiator;

    @NotNull(message = "Field: location. Error: must not be blank. Value: null")
    private LocationDto location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotNull(message = "Field: title. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: title. Error: must not be blank. Value: blank")
    @Size(min = 3, max = 120, message = "Size of '${validatedValue}' must be between {min) and {max}")
    private String title;
}