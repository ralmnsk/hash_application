package tt.hashtranslator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import tt.hashtranslator.model.Hash;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashDecryptServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private HashDecryptService hashDecryptService;
    private String url = "https://md5.opiums.eu/api.php?type=md5&hash=c4ca4238a0b923820dcc509a6f75849b&uot=0/1";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(hashDecryptService, "url", "https://md5.opiums.eu/api.php?type=md5&hash=");
    }

    @Test
    void getDecryptedHash() {
        ResponseEntity<String> forEntity = new ResponseEntity<>("1", HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(forEntity);
        hashDecryptService.getDecryptedHash("c4ca4238a0b923820dcc509a6f75849b");
        verify(restTemplate, times(1)).getForEntity(url, String.class);
    }

    @Test
    void getDecryptedHashFallBack() {
        Hash hash = hashDecryptService
                .getDecryptedHashFallBack(new RuntimeException("test"), "c4ca4238a0b923820dcc509a6f75849b");
        assertEquals("c4ca4238a0b923820dcc509a6f75849b", hash.getMd5());
        assertEquals("", hash.getResult());
        assertFalse(hash.isDecoded());
        assertFalse(hash.isProcessed());
    }
}