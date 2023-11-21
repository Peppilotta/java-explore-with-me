package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventShortDto {

    private Long id;

    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Long confirmedRequests;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    private LocationDto location;

    @NotNull
    private Boolean paid;

    @NotNull
    @NotBlank
    private String title;

    private Long views;
}