package ru.practicum.ewm.admin;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

public interface CompilationServiceAdmin {

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, NewCompilationDto compilation);
}