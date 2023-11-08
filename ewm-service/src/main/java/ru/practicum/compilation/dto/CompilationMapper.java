package ru.practicum.compilation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.EventCompilation;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public EventCompilation toEventCompilation(NewEventCompilationDto eventCompilationDto) {
        return EventCompilation.builder()
                .compilationId(eventCompilationDto.getCompilationId())
                .eventId(eventCompilationDto.getEventId())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}