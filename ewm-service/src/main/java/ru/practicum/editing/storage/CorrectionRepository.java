package ru.practicum.editing.storage;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.model.Correction;

import java.util.List;

public interface CorrectionRepository extends JpaRepository<Correction, Long>, JpaSpecificationExecutor<Correction> {

    Correction findByEventIdAndEventField(Long eventId, EventField eventField);

    @Override
    List<Correction> findAll(Specification<Correction> specification);


    Boolean existsByEventIdAndEventField(Long eventId, EventField eventFields);

    @Query("select c from Correction c where c.eventField in :eventFields and c.event.id = :eventId")
    List<Correction> findAllByEventFieldsAndEventId(List<EventField> eventFields, Long eventId);

    List<Correction> findAllByEventId(Long eventId);
}