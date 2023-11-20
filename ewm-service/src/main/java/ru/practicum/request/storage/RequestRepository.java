package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.dto.RequestStatus;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select count(r.id) from Request as r where r.event.id = :eventId and r.status = :status")
    Long getConfirmedRequestsForEventWithId(@Param("eventId") Long eventId, @Param("status") RequestStatus status);

    @Query("select r from Request as r where r.requester.id = :userId")
    List<Request> findByUserId(@Param("userId") Long userId);

    @Query("select r from Request as r where r.requester.id = :userId and r.event.id = :eventId")
    List<Request> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select r from Request as r where r.event.initiator.id = :userId and r.event.id = :eventId")
    List<Request> findByOwnerIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select r from Request as r where r.id in :ids")
    List<Request> findAllByIds(@Param("ids") List<Long> ids);

    @Query("select r.id from Request as r where r.event.id = :eventId and r.status = :status")
    List<Long> findAllByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") RequestStatus status);

}