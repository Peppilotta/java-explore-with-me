package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select count(r.id) from Request as r where r.event.id = :eventId and r.status = :status")
    Long getConfirmedRequestsForEventWithId(Long eventId, String status) ;
}