package ru.practicum.services.interfaces;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationWithoutEvent;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

public interface CompilationServiceAdmin {

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationWithoutEvent deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation);
}