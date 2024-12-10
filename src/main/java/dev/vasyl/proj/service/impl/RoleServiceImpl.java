package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.model.Role;
import dev.vasyl.proj.model.RoleName;
import dev.vasyl.proj.repository.RoleRepository;
import dev.vasyl.proj.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
    }
}
