package tt.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ConfigurationPropertiesScan("tt.authorization.config")
@EnableJpaRepositories("tt.authorization")
@EnableWebSecurity
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(tt.authorization.AuthorizationApplication.class, args);
    }
}
