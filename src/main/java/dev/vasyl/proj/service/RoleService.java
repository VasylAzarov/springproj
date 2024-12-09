package dev.vasyl.proj.service;

import dev.vasyl.proj.model.Role;
import dev.vasyl.proj.model.RoleName;

public interface RoleService {
    Role save(Role role);

    Role findByName(RoleName roleName);
}
