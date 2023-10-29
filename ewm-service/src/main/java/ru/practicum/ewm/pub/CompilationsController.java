package ru.practicum.ewm.pub;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.compilation.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
public class CompilationsController {

    private static final String COMPILATION_ID_FIELD_NAME = "id";

    private final CompilationService compilationService;

    public CompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    List<CompilationDto> getCompilationsPageable(@RequestParam(defaultValue = "true") String pinned,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, COMPILATION_ID_FIELD_NAME));

        return compilationService.getCompilations(pinned, pageable);
    }

    @GetMapping("/{compId}")
    CompilationDto getCompilation(@PathVariable(name = "compId") @Positive Long compId) {
        return compilationService.getCompilation(compId);
    }
}