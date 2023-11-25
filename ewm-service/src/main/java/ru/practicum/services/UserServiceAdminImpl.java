package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ApiError;
import ru.practicum.error.ErrorStatus;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceAdminImpl {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        log.info("Get request for users:");

        if (Objects.isNull(ids)) {
            return userRepository.findAllPageable(pageable).stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            for (Long id : ids) {
                log.info("id={}", id);
            }
            return userRepository.findAllByIds(ids, pageable)
                    .getContent()
                    .stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    public UserDto create(NewUserRequest user) {
        log.info("Create request for user {}", user);
        checkEmail(user.getEmail());
        return userMapper.toDto(userRepository.save(userMapper.toUserFromNew(user)));
    }

    @Transactional
    public UserDto deleteUser(Long id) {
        log.info("Delete request for user with id={} ", id);
        checkUserExistence(id);
        User deletedUser = userRepository.findById(id).orElseGet(User::new);
        userRepository.deleteById(id);
        log.info("User deleted: {} ", deletedUser);
        return userMapper.toDto(deletedUser);
    }

    private void checkUserExistence(Long id) {
        if (!userRepository.existsById(id)) {
            ApiError apiError = new ApiError(ErrorStatus.E_404_NOT_FOUND.getValue(),
                    "The required object was not found.",
                    "User with id=" + id + " not exists.", LocalDateTime.now());
            throw new NotFoundException(apiError);
        }
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            ApiError apiError = new ApiError(ErrorStatus.E_409_CONFLICT.getValue(),
                    "Integrity constraint has been violated.",
                    "User with email=" + email + " already exists.", LocalDateTime.now());
            throw new ConflictException(apiError);
        }
    }
}