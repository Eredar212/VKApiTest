package vk.utils;

import vk.model.User;
import vk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

    // BEGIN
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email).get();
    }
    // END

    public User getTestUser() {
        return  userRepository.findByEmail("admin@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
