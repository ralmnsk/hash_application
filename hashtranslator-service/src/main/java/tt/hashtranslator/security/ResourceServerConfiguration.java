package tt.hashtranslator.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ResourceServerConfiguration {
    private CustomTokenIntrospector customTokenIntrospector;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(new AntPathRequestMatcher("/api/applications"))
                                .hasAuthority("USER")
                                .and())
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .opaqueToken(opaqueToken ->
                                        opaqueToken.introspector(customTokenIntrospector))
                                .bearerTokenResolver(CustomTokenIntrospector::getAuthorizationToken).and())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
