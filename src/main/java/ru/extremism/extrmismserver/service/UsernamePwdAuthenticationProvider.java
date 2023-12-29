package ru.extremism.extrmismserver.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import ru.extremism.extrmismserver.model.server.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    UserService userService;
    @Override public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails user = userService.loadUserByUsername(username);

        if(encoder.matches(pwd,user.getPassword())){
            List<GrantedAuthority> authorityList = new ArrayList<>(user.getAuthorities());
            return new UsernamePasswordAuthenticationToken(username,pwd,authorityList);
        }else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    public Authentication authenticate(@RequestAttribute("user") User user) {
        return new UsernamePasswordAuthenticationToken(user.getUsername(),
                                                        user.getPassword(),
                                                        new ArrayList<>(user.getAuthorities()));
    }
    @Override public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
