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
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());

        return compilation;
    }

    public EventCompilation toEventCompilation(NewEventCompilationDto eventCompilationDto) {
        EventCompilation eventCompilation = new EventCompilation();
        eventCompilation.setCompilationId(eventCompilationDto.getCompilationId());
        eventCompilation.setEventId(eventCompilationDto.getEventId());

        return eventCompilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(events);

        return compilationDto;
    }
}