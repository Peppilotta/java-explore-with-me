package ru.practicum.ewm.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {

    @Query("select e from Event e where e.category.id = :categoryId order by e.id")
    List<Event> findAllByCategory(Long categoryId);

    @Query("select e from Event e where e.id in :eventIds")
    List<Event> findAllByIds(List<Long> eventIds);

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

}
