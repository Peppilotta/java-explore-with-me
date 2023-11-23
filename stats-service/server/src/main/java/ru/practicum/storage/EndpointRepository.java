package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.VisitorsStatsDto;
import ru.practicum.dto.EndpointWithoutVisitors;
import ru.practicum.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long>, JpaSpecificationExecutor<Endpoint> {

    boolean existsByUriAndApp(String uri, String app);

    @Query("select  new ru.practicum.dto.EndpointWithoutVisitors(e.id, e.app, e.uri) " +
            "from Endpoint as e " +
            "where e.uri = :uri " +
            "and e.app = :app ")
    EndpointWithoutVisitors findEndpointByUriAndApp(String uri, String app);

    @Query("select new ru.practicum.VisitorsStatsDto(e.app, e.uri, count(distinct v.ip)) " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where e.uri in :uris " +
            "and v.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(distinct v.ip) desc")
    List<VisitorsStatsDto> getUniqueUrisFromList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.VisitorsStatsDto(e.app, e.uri, count(v.ip)) " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where e.uri in :uris " +
            "and v.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(v.ip) desc")
    List<VisitorsStatsDto> getUrisFromList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.VisitorsStatsDto(e.app, e.uri, count(v.ip)) " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where v.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(v.ip) desc")
    List<VisitorsStatsDto> getAllUrisFromList(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.VisitorsStatsDto(e.app, e.uri, count(distinct v.ip))  " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where v.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(distinct v.ip) desc ")
    List<VisitorsStatsDto> getAllUniqueUrisFromList(LocalDateTime start, LocalDateTime end);

    @Query("select count(v.ip)  " +
            "from Endpoint as e " +
            "inner join e.visitors v " +
            "where e.app = :app and e.uri = :uri and v.ip = :visitorIp ")
    Long getUniqueIp(@Param("app") String app, @Param("uri") String uri, @Param("visitorIp") String visitorIp);
}