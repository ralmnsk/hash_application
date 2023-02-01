package tt.hashtranslator.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tt.hashtranslator.dto.HashRequestDTO;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


class HashValidationUtilTest {

    @Test
    void validateThrowsValidationExceptionInvalidMD5() {
        String md5First = "c81e728d9d4c2f636f067f89cc14862c";
        String md5Second = "eccbc87e4b5ce2fe28308fd9f2a7baf31";
        String md5Third = "eccbc87e4b5ce2fe28308fd9f2a7baf.";
        HashRequestDTO request = new HashRequestDTO();
        Set<String> hashes = new HashSet<>();
        hashes.add(md5First);
        hashes.add(md5Second);
        hashes.add(md5Third);
        request.setHashes(hashes);
        ValidationException validationException = assertThrows(ValidationException.class, () -> HashValidationUtil.validate(request));
        assertFalse(validationException.getMessage().contains(md5First));
        Assertions.assertTrue(validationException.getMessage().contains(md5Second));
        Assertions.assertTrue(validationException.getMessage().contains(md5Third));
    }

    @Test
    void validateThrowsValidationExceptionEmptyHashes() {
        HashRequestDTO request = new HashRequestDTO();
        Set<String> hashes = new HashSet<>();
        request.setHashes(hashes);
        ValidationException validationException = assertThrows(ValidationException.class, () -> HashValidationUtil.validate(request));
        Assertions.assertTrue(validationException.getMessage().contains("Hashes are not presented."));
    }

    @Test
    void validateThrowsValidationExceptionNullHashes() {
        HashRequestDTO request = new HashRequestDTO();
        Set<String> hashes = new HashSet<>();
        request.setHashes(hashes);
        ValidationException validationException = assertThrows(ValidationException.class, () -> HashValidationUtil.validate(request));
        Assertions.assertTrue(validationException.getMessage().contains("Hashes are not presented."));
    }
}