package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select count(r.id) from Request as r where r.event.id = :eventId and r.status = :status")
    Long getConfirmedRequestsForEventWithId(Long eventId, RequestStatus status);

    @Query("select r from Request as r where r.requester.id = :userId")
    List<Request> findByUserId(Long userId);

    @Query("select r from Request as r where r.requester.id = :userId and r.event.id = :eventId")
    List<Request> findByUserIdAndEventId(Long userId, Long eventId);

    @Query("select r from Request as r where r.event.initiator.id = :userId and r.event.id = :eventId")
    List<Request> findByOwnerIdAndEventId(Long userId, Long eventId);

    @Query("select r from Request as r where r.id in :ids")
    List<Request> findAllByIds(List<Long> ids);
}