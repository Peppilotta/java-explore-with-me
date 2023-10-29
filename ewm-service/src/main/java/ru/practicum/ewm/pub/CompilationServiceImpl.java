package ru.practicum.ewm.pub;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.CompilationDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {

    public List<CompilationDto> getCompilations(String pined, Pageable pageable) {
        return new ArrayList<>();
    }

    public CompilationDto getCompilation(Long compId) {
        return new CompilationDto();
    }
}