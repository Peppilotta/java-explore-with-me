package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserControllerAdmin {

    private final UserServiceAdminImpl service;

    @GetMapping()
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return service.getUsers(ids, pageable);
    }

    @PostMapping()
    public UserDto create(@RequestBody @Valid final NewUserRequest userDto) {
        log.info("Create user with body {}", userDto);
        return service.create(userDto);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable("id") @Positive long id) {
        log.info("Delete user with id = {}", id);
        return service.deleteUser(id);
    }
}