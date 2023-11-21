package ru.practicum.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query("select e from Event e where e.id in :eventIds")
    List<Event> findAllByIds(@Param("eventIds") List<Long> eventIds);

    @Override
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    @Override
    List<Event> findAll(Specification<Event> specification);

    @Query("select e from Event e where e.initiator.id = :userId")
    Page<Event> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select e from Event e where e.category.id = :catId")
    List<Event> findAllByCategoryId(@Param("catId") Long catId);

    @Transactional
    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e.id = :id")
    void updateViewsById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e in :events")
    void updateViewsByEvents(@Param("events") List<Event> events);
}