package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.user.UserRegistrationRequestDto;
import dev.vasyl.proj.dto.user.UserResponseDto;
import dev.vasyl.proj.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
