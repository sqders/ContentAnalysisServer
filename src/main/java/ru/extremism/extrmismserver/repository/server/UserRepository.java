package ru.extremism.extrmismserver.repository.server;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.extremism.extrmismserver.model.server.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsUserByUsername(@Size(min = 5, message = "Не меньше 5 знаков") String username);
}
