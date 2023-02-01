package tt.authorization.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tt.authorization.model.Role;
import tt.authorization.model.User;
import tt.authorization.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private String username;
    private User user;

    @BeforeEach
    void setUp() {
        username = "test@test.com";
        user = new User();
        user.setIsActive(true);
        user.setPassword("111");
        user.setEmail(username);
        user.setId(1L);
        user.setRole(Role.USER);
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Assertions.assertTrue(userDetails.isEnabled());
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertEquals(new SimpleGrantedAuthority(user.getRole().toString()), userDetails.getAuthorities().toArray()[0]);
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));
        Assertions.assertEquals("User not found", exception.getMessage());
    }
}