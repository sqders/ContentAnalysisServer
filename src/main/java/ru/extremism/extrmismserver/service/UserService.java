package ru.extremism.extrmismserver.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.extremism.extrmismserver.model.server.Role;
import ru.extremism.extrmismserver.model.server.User;
import ru.extremism.extrmismserver.repository.server.RoleRepository;
import ru.extremism.extrmismserver.repository.server.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override public User loadUserByUsername(@Nullable String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public boolean saveUser(User user){
        if(userRepository.existsUserByUsername(user.getUsername())){
            return false;
        }
        user.setRoles(Collections.singleton(new Role(2L, "USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean userExist(String username){
        return userRepository.existsUserByUsername(username);
    }

}
