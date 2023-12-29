package ru.extremism.extrmismserver.configurers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import ru.extremism.extrmismserver.model.server.Role;
import ru.extremism.extrmismserver.model.server.User;
import ru.extremism.extrmismserver.repository.server.RoleRepository;
import ru.extremism.extrmismserver.service.UserService;

import java.util.HashSet;

@Configuration
public class DataInitializer {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;

    @PostConstruct
    public void init(){
        roleRepository.save(new Role(1L,"ADMIN"));
        roleRepository.save(new Role(2L,"USER"));
        User admin = new User();
        admin.setPassword("admin");
        admin.setUsername("admin");
        admin.setRoles(new HashSet<>(roleRepository.findAll(Sort.unsorted())));
        userService.saveUser(admin);
    }
}
