package tt.authorization.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tt.authorization.dto.AuthDTO;
import tt.authorization.service.AuthenticatedPrincipalService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {
    @Mock
    private AuthenticatedPrincipalService principalService;
    @InjectMocks
    private AuthorizationController authorizationController;
    private AuthDTO auth;

    @BeforeEach
    void setUp() {
        auth = new AuthDTO();
        auth.setAuthority("USER");
        auth.setName("auth");
    }

    @Test
    void checkIfAuthenticated() {
        when(principalService.getPrincipal()).thenReturn(auth);
        ResponseEntity<AuthDTO> response = authorizationController.checkIfAuthenticated("auth");
        assertEquals(auth, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void checkIfAuthenticatedNotAuthenticated() {
        when(principalService.getPrincipal()).thenReturn(null);
        ResponseEntity<AuthDTO> response = authorizationController.checkIfAuthenticated("auth");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}