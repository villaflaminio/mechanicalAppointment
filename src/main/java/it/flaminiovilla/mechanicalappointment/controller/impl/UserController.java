package it.flaminiovilla.mechanicalappointment.controller.impl;

import it.flaminiovilla.mechanicalappointment.exception.ResourceNotFoundException;
import it.flaminiovilla.mechanicalappointment.repository.UserRepository;
import it.flaminiovilla.mechanicalappointment.service.impl.CustomUserDetailsService;
import it.flaminiovilla.mechanicalappointment.model.entity.User;
import it.flaminiovilla.mechanicalappointment.model.entity.UserPrincipal;
import it.flaminiovilla.mechanicalappointment.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller with the REST endpoints for user managament.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;
        /**
         * Get the current user.
         *
         * @param userPrincipal the current user
         * @return the current user
         */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
//        // Return the current user found by id.
//        UserMe user =userRepository.findProjectedById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
//        User user2 =userRepository.findByIdAndSubscriptions_active(userPrincipal.getId(), true).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
//
//
//
        return ResponseEntity.ok("");
    }

    /**
     * Update the user password
     * @param userPrincipal the current user
     * @param newPassword the new password
     * @return the updated user
     */
    @PostMapping("/changePassword")
    public User changePassword(@CurrentUser UserPrincipal userPrincipal , @RequestBody String newPassword ){
        // Find the current user by id.
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // Update the password.
        user.setPassword(newPassword);

        // Encode the new password.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user.
        return userRepository.save(user);
    }

}
