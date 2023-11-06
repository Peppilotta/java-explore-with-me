package ru.practicum.ewm.compilation.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.EventCompilation;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    @Query("delete from EventCompilation e where e.compilationId = :compilationsId and e.eventId in :ids")
    void deleteEvents(Long compilationsId, List<Long> ids);

   @Query("select e.eventId from EventCompilation e where e.compilationId = :compId")
    List<Long> findByCompilationId(Long compId);
}