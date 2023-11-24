package ru.practicum.editing.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.model.Correction;

import java.util.List;

public interface CorrectionRepository extends JpaRepository<Correction, Long>, JpaSpecificationExecutor<Correction> {

    Correction findByEventIdAndEventField(Long eventId, EventField eventField);

    List<Correction> findAll(CorrectionSpecification specification);

    Boolean existsByEventIdAndEventField(Long eventId, EventField eventField);

    Boolean existsByEventIdAndEventFields(Long eventId, List<EventField> eventFields);

    List<Correction> findAllByEventFields(List<EventField> eventFields);
}