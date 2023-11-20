package ru.practicum.compilation.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select co from Compilation co where co.pinned = :pinned")
    Page<Compilation> getByPinned(@Param("pinned") boolean pinned, Pageable pageable);
}