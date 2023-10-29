package ru.practicum.ewm.pub;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(String pined, Pageable pageable);

    CompilationDto getCompilation(Long compId);
}