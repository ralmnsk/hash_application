package tt.hashtranslator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@ActiveProfiles("retry")
@TestPropertySource("classpath:test-config.properties")
class HashDecryptServiceRetryTest {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HashDecryptService hashDecryptService;

    @EnableRetry
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @Profile("retry")
    @TestConfiguration
    public static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            String url = "https://md5.opiums.eu/api.php?type=md5&hash=c4ca4238a0b923820dcc509a6f75849b&uot=0/1";
            RestTemplate template = Mockito.mock(RestTemplate.class);
            when(template.getForEntity(url, String.class)).thenThrow(new RuntimeException("test"));
            return Mockito.spy(template);
        }

        @Bean
        public HashDecryptService hashDecryptService(RestTemplate restTemplate) {
            return new HashDecryptService(restTemplate);
        }

    }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(hashDecryptService, "url", "https://md5.opiums.eu/api.php?type=md5&hash=");
    }

    @Test
    void getDecryptedHashRetry() {
        String url = "https://md5.opiums.eu/api.php?type=md5&hash=c4ca4238a0b923820dcc509a6f75849b&uot=0/1";
        ResponseEntity<String> forEntity = new ResponseEntity<>("1", HttpStatus.OK);
        hashDecryptService.getDecryptedHash("c4ca4238a0b923820dcc509a6f75849b");
        verify(restTemplate, times(3)).getForEntity(url, String.class);
    }

}