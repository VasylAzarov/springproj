package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.Role;
import dev.vasyl.proj.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
