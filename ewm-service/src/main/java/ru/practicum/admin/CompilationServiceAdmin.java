package ru.practicum.admin;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.request.dto.UpdateCompilationRequest;

public interface CompilationServiceAdmin {

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation);
}