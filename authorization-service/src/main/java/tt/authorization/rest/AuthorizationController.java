package tt.authorization.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt.authorization.dto.AuthDTO;
import tt.authorization.service.AuthenticatedPrincipalService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthorizationController {
    private AuthenticatedPrincipalService principalService;

    @GetMapping("/authenticated")
    public ResponseEntity<AuthDTO> checkIfAuthenticated(@RequestHeader("Authorization") String authorization) {
        ResponseEntity<AuthDTO> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        AuthDTO principal = principalService.getPrincipal();
        if (principal != null) {
            responseEntity = new ResponseEntity<>(principal, HttpStatus.OK);
        }
        return responseEntity;
    }

}
