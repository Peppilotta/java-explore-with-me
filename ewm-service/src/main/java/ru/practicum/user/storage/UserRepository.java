package ru.practicum.user.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u where u.id in :ids")
    Page<User> findAllByIds(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("select u from User as u")
    Page<User> findAllPageable(Pageable pageable);

    @Query("select new ru.practicum.user.dto.UserShortDto(u.id, u.name) from User u where u.id = :id ")
    UserShortDto findByIdToShort(@Param("id") Long id);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("delete from User as r where r.id = :id")
    void deleteById(@Param("id") Long id);
}