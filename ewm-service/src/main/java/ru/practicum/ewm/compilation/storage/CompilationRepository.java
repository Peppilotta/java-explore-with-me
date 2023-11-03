package ru.practicum.ewm.compilation.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {


    @Query("select co from Compilation co where co.pinned = :pinned")
    Page<Compilation> getCompilationsByPinned(String pinned, Pageable pageable);

}
