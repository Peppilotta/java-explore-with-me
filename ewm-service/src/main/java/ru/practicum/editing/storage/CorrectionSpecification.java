package ru.practicum.editing.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.editing.model.Correction;
import ru.practicum.editing.model.Correction_;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Event_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CorrectionSpecification {
    public Specification<Correction> get(Long eventId,
                                         List<EventField> eventFields,
                                         List<RevisionState> revisionStates) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            log.debug("Into Specification parameters for correction");
            CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);

            Subquery<Event> eventSubQuery = criteriaQuery.subquery(Event.class);
            Root<Event> userRoot = eventSubQuery.from(Event.class);
            eventSubQuery.select(userRoot)
                    .distinct(true)
                    .where(criteriaBuilder.equal(userRoot.get(Event_.id), (eventId)));

            predicates.add(criteriaBuilder.in(root.get(Correction_.event)).value(eventSubQuery));

            if (!revisionStates.isEmpty()) {
                CriteriaBuilder.In<RevisionState> inAction = criteriaBuilder.in(root.get(Correction_.state));
                for (RevisionState revisionState : revisionStates) {
                    inAction.value(revisionState);
                }
                predicates.add(inAction);
            }
            if (!eventFields.isEmpty()) {
                CriteriaBuilder.In<EventField> inAction = criteriaBuilder.in(root.get(Correction_.eventField));
                for (EventField eventField : eventFields) {
                    inAction.value(eventField);
                }
                predicates.add(inAction);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}