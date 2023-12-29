package ru.extremism.extrmismserver.controler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.extremism.extrmismserver.model.server.User;
import ru.extremism.extrmismserver.service.UserService;
import ru.extremism.extrmismserver.service.UsernamePwdAuthenticationProvider;

@Controller
public class AuthorizationController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UsernamePwdAuthenticationProvider authenticationProvider;


    AuthorizationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerModel(Model model){
        model.addAttribute(new User());
        return "register";
    }

    @GetMapping("/login")
    public String loginModel(Model model){
        model.addAttribute(new User());
        return "login";
    }

    @PostMapping("/perform-login")
    public ResponseEntity<String> authenticateUser(@ModelAttribute("user") User user) {
        ResponseEntity response;
        User savedUser;
        if(userService.userExist(user.getUsername())) {
            savedUser = userService.loadUserByUsername(user.getUsername());
            Authentication authentication = authenticationProvider.authenticate(savedUser);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response = ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder.fromCurrentContextPath().path("/index").build().toUri())
                    .body("Given user details are successfully registered");
        }else{
            response = new ResponseEntity<>("User login is failed", HttpStatus.BAD_REQUEST);
        }
        return response;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute("user") User user) {
        ResponseEntity response = null;
        try {
            if (userService.saveUser(user)) {
                response = ResponseEntity
                        .status(HttpStatus.FOUND)
                        .location(ServletUriComponentsBuilder.fromCurrentContextPath().path("/index").build().toUri())
                        .body("Given user details are successfully registered");
                SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(user));
            }else{
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This username is already exist!");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + ex.getMessage());
        }
        return response;
    }

    @RequestMapping("/user")
    public User getUserDetailsAfterLogin(Authentication authentication) {
        return userService.loadUserByUsername(authentication.getName());
    }
}
