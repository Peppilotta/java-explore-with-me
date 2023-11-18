package ru.practicum.services.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {

    List<CompilationDto> getCompilations(Boolean pined, Pageable pageable);

    CompilationDto getCompilation(Long compId);
}