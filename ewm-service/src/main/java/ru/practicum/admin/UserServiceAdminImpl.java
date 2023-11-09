package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceAdminImpl {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    // поправить
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        for (Long id : ids) {
            log.info("id={}", id);
        }
        return userRepository.findAllByIds(ids, pageable)
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
        User deletedUser = userRepository.findById(id).orElseGet(User::new);
        userRepository.deleteById(id);
        log.info("User deleted: {} ", deletedUser);
        return userMapper.toDto(deletedUser);
    }

    private void checkUserExistence(Long id) {
        if (!userRepository.existsById(id)) {
            ApiError apiError = ApiError.builder()
                    .message("User with id=" + id + " not exists.")
                    .reason("The required object was not found.")
                    .status(ErrorStatus.E_404_NOT_FOUND.getValue())
                    .timestamp(LocalDateTime.now())
                    .build();
            throw new NotFoundException(apiError);
        }
    }
}