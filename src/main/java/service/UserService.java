package service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import entity.Role;
import entity.User;
import entity.UserRole;
import repository.UserRepository;
import repository.UserRoleRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails)this.userRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException(username);
        });
    }

    @Transactional
    public User registerNewUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return (User)this.userRepository.save(user);
    }

    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        User user = (User)this.userRepository.findById(userId).orElseThrow(() -> {
            return new IllegalArgumentException("User not found");
        });
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(Role.valueOf(roleName));
        this.userRoleRepository.save(userRole);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, String roleName) {
        User user = (User)this.userRepository.findById(userId).orElseThrow(() -> {
            return new IllegalArgumentException("User not found");
        });
        Optional<UserRole> userRole = user.getUserRoles().stream().filter((role) -> {
            return role.getRole().name().equals(roleName);
        }).findFirst();
        UserRoleRepository var10001 = this.userRoleRepository;
        Objects.requireNonNull(var10001);
        userRole.ifPresent(var10001::delete);
    }
}
