package es.tubalcain.security;

import es.tubalcain.domain.User;
import es.tubalcain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Simple helper to get the current logged-in user.
 * Uses Spring Security to find who is authenticated.
 */
@Component
public class UserContext {

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets the ID of the currently logged-in user.
     * Throws exception if no user is logged in.
     */
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No user is logged in");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new IllegalStateException("User is not authenticated");
        }
        
        String username = ((UserDetails) principal).getUsername();
        
        // Step 3: Find the user in database and return their ID
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        
        return user.getId();
    }

    /**
     * Gets the full User object of the currently logged-in user.
     * Throws exception if no user is logged in.
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No user is logged in");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new IllegalStateException("User is not authenticated");
        }
        
        String username = ((UserDetails) principal).getUsername();
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
    }
}
