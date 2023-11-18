package ru.practicum.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.services.interfaces.CompilationServiceAdmin;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.request.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@Validated
public class CompilationControllerAdmin {

    private final CompilationServiceAdmin service;

    public CompilationControllerAdmin(CompilationServiceAdmin service) {
        this.service = service;
    }

    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Valid final NewCompilationDto compilation) {
        return service.addCompilation(compilation);
    }

    @DeleteMapping("/{compId}")
    public CompilationDto deleteCompilation(@PathVariable(name = "compId") @Positive Long compId) {
        return service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(name = "compId") @Positive Long compId,
                                            @RequestBody @Valid final UpdateCompilationRequest compilation) {
        return service.updateCompilation(compId, compilation);
    }
}