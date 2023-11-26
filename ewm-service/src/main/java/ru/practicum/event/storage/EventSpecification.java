package ru.practicum.event.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.Category_;
import ru.practicum.event.dto.AdminEventsFindParameters;
import ru.practicum.event.dto.EventLifeState;
import ru.practicum.event.dto.PublicEventsFindParameters;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Event_;
import ru.practicum.user.model.User;
import ru.practicum.user.model.User_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventSpecification {

    public Specification<Event> getEventsByParameters(AdminEventsFindParameters findParameters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            log.debug("Into Specification parameters for admin");
            CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);

            List<Long> users = findParameters.getUsers();
            if (!Objects.isNull(users) && !users.isEmpty()) {
                Subquery<User> userSubQuery = criteriaQuery.subquery(User.class);
                Root<User> userRoot = userSubQuery.from(User.class);
                userSubQuery.select(userRoot)
                        .distinct(true)
                        .where(userRoot.get(User_.id).in(users));

                predicates.add(criteriaBuilder.in(root.get(Event_.initiator)).value(userSubQuery));
                log.debug("Users criteria added");
            }
            List<Long> categories = findParameters.getCategories();
            if (!Objects.isNull(categories) && !categories.isEmpty()) {
                Subquery<Category> categorySubQuery = criteriaQuery.subquery(Category.class);
                Root<Category> categoryRoot = categorySubQuery.from(Category.class);
                categorySubQuery.select(categoryRoot)
                        .distinct(true)
                        .where(categoryRoot.get(Category_.id).in(categories));
                predicates.add(criteriaBuilder.in(root.get(Event_.category)).value(categorySubQuery));
                StringBuilder categoriesToLog = new StringBuilder();
                categories.forEach(c -> categoriesToLog.append(" id=").append(c));
                log.debug("Categories criteria added. {}", categoriesToLog);
            }
            List<String> states = findParameters.getStates();
            if (!states.isEmpty()) {
                CriteriaBuilder.In<EventLifeState> inStates = criteriaBuilder.in(root.get(Event_.state));
                for (String state : states) {
                    EventLifeState eventLifeState = EventLifeState.valueOf(state);
                    inStates.value(eventLifeState);
                }
                predicates.add(inStates);
                StringBuilder statesToLog = new StringBuilder();
                states.forEach(c -> statesToLog.append(" state=").append(c));
                log.debug("States criteria added. {}", statesToLog);
            }

            LocalDateTime start = findParameters.getRangeStart();
            LocalDateTime end = findParameters.getRangeEnd();
            if (Objects.isNull(end)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.eventDate), start));
                log.debug("Time criteria added - only Start");
            } else {
                predicates.add(criteriaBuilder.between(root.get(Event_.eventDate), start, end));
                log.debug("Time criteria added - start and end");
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Event> getEventsByParametersPublic(PublicEventsFindParameters findParameters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            log.debug("Into Specification parameters for public");

            CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
            predicates.add(criteriaBuilder.equal(root.get(Event_.state), EventLifeState.PUBLISHED));
            String text = findParameters.getText();
            if (!Objects.isNull(text)) {
                String modified = text.trim();
                if (modified.length() > 0) {
                    String pattern = "%" + modified.toLowerCase() + "%";
                    predicates
                            .add(criteriaBuilder.or(
                                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.annotation)), pattern),
                                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.description)), pattern)));
                    log.debug("Criteria added: text = {}", text);
                }
            }
            List<Long> categories = findParameters.getCategories();
            if (!Objects.isNull(categories) && !categories.isEmpty()) {
                Subquery<Category> categorySubQuery = criteriaQuery.subquery(Category.class);
                Root<Category> categoryRoot = categorySubQuery.from(Category.class);
                categorySubQuery.select(categoryRoot)
                        .distinct(true)
                        .where(categoryRoot.get(Category_.id).in(categories));
                predicates.add(criteriaBuilder.in(root.get(Event_.category)).value(categorySubQuery));
                StringBuilder categoriesToLog = new StringBuilder();
                categories.forEach(c -> categoriesToLog.append(" id=").append(c));
                log.debug("Categories criteria added. {}", categoriesToLog);
            }
            Boolean paid = findParameters.getPaid();
            if (!Objects.isNull(paid)) {
                predicates.add(criteriaBuilder.equal(root.get(Event_.paid), paid));
                log.debug("Criteria added: paid");
            }
            LocalDateTime start = findParameters.getRangeStart();
            LocalDateTime end = findParameters.getRangeEnd();
            if (Objects.isNull(end)) {
                predicates.add(criteriaBuilder.greaterThan(root.get(Event_.eventDate), start));
                log.debug("Time criteria added - only Start");
            } else {
                predicates.add(criteriaBuilder.between(root.get(Event_.eventDate), start, end));
                log.debug("Time criteria added - start and end");
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}