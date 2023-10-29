package ru.practicum.ewm.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.CompilationDto;
import ru.practicum.ewm.compilation.NewCompilationDto;

@Service
@Slf4j
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {

    public CompilationDto addCompilation(NewCompilationDto compilation) {
        return new CompilationDto();
    }

    public CompilationDto deleteCompilation(Long compId) {
        return new CompilationDto();
    }

    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilation) {
        return new CompilationDto();
    }
}