package ru.practicum.ewm.user.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where  lower(u.email) = lower(:text)")
    List<User> findByEmail(@Nullable String text);

    Boolean existsByEmail(@Nullable String text);

    @Query("select u from User u where u.id in :ids")
    Page<User> findAllByIds(List<Long> ids, Pageable pageable);
}