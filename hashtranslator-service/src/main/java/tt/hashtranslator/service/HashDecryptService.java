package tt.hashtranslator.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tt.hashtranslator.model.Hash;

@Slf4j
@Service
public class HashDecryptService {
    @Value("${md5.application.url}")
    private String url;
    private RestTemplate restTemplate;

    @Autowired
    public HashDecryptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(maxDelayExpression = "${retry.maxDelay}"))
    public Hash getDecryptedHash(String hashString) {
        log.info("getting hash:{}", hashString);
        Hash hash = initHash(hashString);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url + hashString + "&uot=0/1", String.class);
        String result = forEntity.getBody();
        if (result != null) {
            hash.setResult(result);
            hash.setDecoded(true);
        }
        hash.setProcessed(true);
        return hash;
    }

    @Recover
    public Hash getDecryptedHashFallBack(Exception e, String hash) {
        log.info("hash was not processed:{}", hash);
        return initHash(hash);
    }

    private Hash initHash(String hashString) {
        Hash hash = new Hash();
        hash.setMd5(hashString);
        hash.setDecoded(false);
        hash.setProcessed(false);
        hash.setResult(Strings.EMPTY);
        return hash;
    }
}
