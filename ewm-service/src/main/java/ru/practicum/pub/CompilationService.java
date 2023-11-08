package ru.practicum.pub;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(String pined, Pageable pageable);

    CompilationDto getCompilation(Long compId);
}