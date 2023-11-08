package ru.practicum.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query("select e from Event e where e.category.id = :categoryId order by e.id")
    List<Event> findAllByCategory(Long categoryId);

    @Query("select e from Event e where e.id in :eventIds")
    List<Event> findAllByIds(List<Long> eventIds);

    @Override
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    @Override
    List<Event> findAll(Specification<Event> specification);

    @Query("select e from Event e where e.initiator.id = :userId")
    Page<Event> findAllByUserId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.category.id = :catId")
    List<Event> findAllByCategoryId(Long catId);

    @Query("select e from Event e where e in :events")
    List<Event> findAllByEvents(List<Event> events);

    @Transactional
    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e.id = :id")
    int updateViewsById(Long id);

    @Transactional
    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e in :events")
    int updateViewsByEvents(List<Event> events);



}