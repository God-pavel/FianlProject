package springBoot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springBoot.dto.UserDTO;
import springBoot.entity.User;
import springBoot.entity.enums.Role;
import springBoot.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("User not found!");
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean saveNewUser(UserDTO userdto) {
        User userFromDb = userRepository.findByUsername(userdto.getUsername());

        if (userFromDb != null) {
            log.warn("login not unique!");
            return false;
        }
        User user = User
                .builder()
                .firstName(userdto.getFirstName())
                .lastName(userdto.getLastName())
                .email(userdto.getEmail())
                .username(userdto.getUsername())
                .password(passwordEncoder.encode(userdto.getPassword()))
                .active(true)
                .roles(Collections.singleton(Role.USER))
                .build();
        userRepository.save(user);
        log.info("User was saved. Username : " + user.getUsername());
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void userEdit(User user, Map<String, String> form, String username) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        form.keySet().stream()
                .filter(roles::contains)
                .forEach(key -> user.getRoles().add(Role.valueOf(key)));


        userRepository.save(user);
    }

    @PostConstruct
    public void add() {
        if (userRepository.findByUsername("admin") == null) {
            User user = User.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin")
                    .username("admin")
                    .password(passwordEncoder.encode("test"))
                    .build();
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.ADMIN));
            userRepository.save(user);
        }

    }
}
