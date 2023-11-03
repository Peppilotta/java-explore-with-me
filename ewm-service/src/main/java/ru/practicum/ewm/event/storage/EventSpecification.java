package ru.practicum.ewm.event.storage;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.model.Category_;
import ru.practicum.ewm.event.dto.EventFindParameters;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Event_;
import ru.practicum.ewm.user.model.User_;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class EventSpecification {

    public Specification<Event> getEventsByParameters(EventFindParameters parameters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Long> users = parameters.getUsers();
            if (!users.isEmpty()) {
                for (Long id : users) {
                    predicates.add(criteriaBuilder.equal((root.get(Event_.initiator)).get(User_.ID), id));
                }
            }
            List<Long> categories = parameters.getCategories();

            if (!categories.isEmpty()) {
                for (Long id : categories) {
                    predicates.add(criteriaBuilder.equal((root.get(Event_.category)).get(Category_.ID), id));
                }
            }

            List<String> states = parameters.getStates();
            if (!states.isEmpty()) {
                for (String state : states) {
                    predicates.add(criteriaBuilder.equal(root.get(Event_.state), state));
                }
            }

            if (!Objects.isNull(parameters.getRangeEnd())) {
                predicates.add(criteriaBuilder.greaterThan(root.get(Event_.createdOn), parameters.getRangeStart()));
            } else {
                predicates.add(criteriaBuilder.between(root.get(Event_.createdOn)
                        , parameters.getRangeStart(), parameters.getRangeEnd()));

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
