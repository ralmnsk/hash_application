package tt.authorization.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import tt.authorization.dto.AuthDTO;
import tt.authorization.model.Role;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticatedPrincipalServiceTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getPrincipal() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken("name", "credentials", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthenticatedPrincipalService authenticatedPrincipalService = new AuthenticatedPrincipalService();
        AuthDTO auth = authenticatedPrincipalService.getPrincipal();
        assertEquals("name", auth.getName());
        assertEquals("USER", auth.getAuthority());
    }

    @Test
    void getPrincipalNotAuthenticated() {
        AuthenticatedPrincipalService authenticatedPrincipalService = new AuthenticatedPrincipalService();
        AuthDTO auth = authenticatedPrincipalService.getPrincipal();
        assertNull(auth);
    }
}