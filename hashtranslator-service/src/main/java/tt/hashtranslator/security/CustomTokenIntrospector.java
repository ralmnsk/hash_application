package tt.hashtranslator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tt.hashtranslator.dto.AuthDTO;
import tt.hashtranslator.exception.AuthException;
import tt.hashtranslator.exception.URIException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTokenIntrospector implements OpaqueTokenIntrospector {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-type";
    private static final String NAME_ATTRIBUTE = "name";

    @Value("${introspection.url}")
    private String url;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        return requestCredentials(token);
    }

    public static String getAuthorizationToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null) {
            return header;
        }
        return null;
    }

    private OAuth2AuthenticatedPrincipal requestCredentials(String token) {
        try {
            ResponseEntity<AuthDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createEntity(token),
                    AuthDTO.class
            );
            AuthDTO auth = response.getBody();
            if (auth == null) {
                throw new AuthException("Authentication Exception happened.");
            }
            Map<String, Object> attributes = new HashMap<>();
            attributes.put(NAME_ATTRIBUTE, auth.getName());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(auth.getAuthority()));
            return new DefaultOAuth2AuthenticatedPrincipal(auth.getName(), attributes, authorities);
        } catch (HttpClientErrorException ex) {
            throw new AuthException(ex.getMessage(), ex);
        }
    }

    private RequestEntity<Void> createEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString());
        headers.add(AUTHORIZATION_HEADER, token);
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new URIException("Problem with uri happened.", e);
        }
        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

}
