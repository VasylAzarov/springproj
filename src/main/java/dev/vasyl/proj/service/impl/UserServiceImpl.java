package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.user.UserRegistrationRequestDto;
import dev.vasyl.proj.dto.user.UserResponseDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.exception.RegistrationException;
import dev.vasyl.proj.mapper.UserMapper;
import dev.vasyl.proj.model.Role;
import dev.vasyl.proj.model.RoleName;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.repository.RoleRepository;
import dev.vasyl.proj.repository.UserRepository;
import dev.vasyl.proj.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("user with email ["
                    + requestDto.getEmail()
                    + "] already exist");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new EntityNotFoundException("Error when set user role"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
