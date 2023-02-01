package tt.authorization.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tt.authorization.dto.AuthDTO;

@Service
public class AuthenticatedPrincipalService {
    public AuthDTO getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String authority = authentication.getAuthorities().toArray()[0].toString();
        return new AuthDTO(authentication.getName(),authority);
    }
}
