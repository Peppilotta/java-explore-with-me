package ru.practicum.stats.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.VisitorsStatsDto;
import ru.practicum.stats.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long>, JpaSpecificationExecutor<Endpoint> {

    boolean existsByUriAndApp(String uri, String app);

    Endpoint getByUriAndApp(String uri, String app);

    @Query("select e.app as app, e.uri as uri, count(distinct v.ip) as hits " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where e.uri in :uris " +
            "and v.timestamp between :start and :end ")
    List<VisitorsStatsDto> getUniqueUrisFromList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select e.app as app, e.uri as uri, count(v.ip) as hits " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where e.uri in :uris " +
            "and v.timestamp between :start and :end ")
    List<VisitorsStatsDto> getUrisFromList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select e.app as app, e.uri as uri, count(v.ip) as hits " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where v.timestamp between :start and :end ")
    List<VisitorsStatsDto> getAllUrisFromList(LocalDateTime start, LocalDateTime end);

    @Query("select e.app as app, e.uri as uri, count(distinct v.ip) as hits " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where v.timestamp between :start and :end ")
    List<VisitorsStatsDto> getAllUniqueUrisFromList(LocalDateTime start, LocalDateTime end);
}