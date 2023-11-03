package ru.practicum.ewm.admin;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.request.dto.UpdateCompilationRequest;

public interface CompilationServiceAdmin {

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation);
}