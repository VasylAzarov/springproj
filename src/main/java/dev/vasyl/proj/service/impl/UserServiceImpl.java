package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.user.UserRegistrationRequestDto;
import dev.vasyl.proj.dto.user.UserResponseDto;
import dev.vasyl.proj.exception.RegistrationException;
import dev.vasyl.proj.mapper.UserMapper;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.repository.UserRepository;
import dev.vasyl.proj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("user with email ["
                    + requestDto.getEmail()
                    + "] already exist");
        }
        User user = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.toUserResponse(user);
    }
}
