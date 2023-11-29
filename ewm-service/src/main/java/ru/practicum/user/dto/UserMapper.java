package ru.practicum.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;

    public User fromShort(UserShortDto userShort) {
        return userRepository.findById(userShort.getId()).orElseGet(User::new);
    }

    public UserShortDto toUserShort(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public User toUserFromNew(NewUserRequest userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}