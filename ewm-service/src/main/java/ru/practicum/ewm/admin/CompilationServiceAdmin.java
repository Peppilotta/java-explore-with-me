package ru.practicum.ewm.admin;

import ru.practicum.ewm.compilation.CompilationDto;
import ru.practicum.ewm.compilation.NewCompilationDto;

public interface CompilationServiceAdmin {

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, NewCompilationDto compilation);
}