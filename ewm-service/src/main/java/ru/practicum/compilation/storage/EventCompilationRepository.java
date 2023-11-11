package ru.practicum.compilation.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilation.model.EventCompilation;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    @Query("delete from EventCompilation e where e.compilationId = :compilationsId and e.eventId in :ids")
    void deleteEvents(@Param("compilationsId") Long compilationsId, @Param("ids") List<Long> ids);

   @Query("select e.eventId from EventCompilation e where e.compilationId = :compId")
    List<Long> findByCompilationId(@Param("compId") Long compId);
}