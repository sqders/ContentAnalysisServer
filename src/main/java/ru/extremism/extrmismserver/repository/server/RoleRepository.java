package ru.extremism.extrmismserver.repository.server;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.extremism.extrmismserver.model.server.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
