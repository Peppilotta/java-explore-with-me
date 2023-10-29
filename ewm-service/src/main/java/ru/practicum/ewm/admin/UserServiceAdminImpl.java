package ru.practicum.ewm.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ItemDoesNotExistException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceAdminImpl {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    // поправить
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        log.info("GET request - all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto create(NewUserRequest user) {
        log.info("Create request for user {}", user);
        return userMapper.toDto(userRepository.save(userMapper.toUserFromNew(user)));
    }

    public UserDto deleteUser(Long id) {
        log.info("Delete request - user id={} ", id);
        checkUserExistence(id);
        User deletedUser = userRepository.findById(id).get();
        userRepository.deleteById(id);
        log.info("User deleted: {} ", deletedUser);
        return userMapper.toDto(deletedUser);
    }

    private void checkUserExistence(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ItemDoesNotExistException("User with id=" + id + " not exists.");
        }
    }
}