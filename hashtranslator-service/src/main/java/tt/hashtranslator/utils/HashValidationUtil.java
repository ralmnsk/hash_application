package tt.hashtranslator.utils;

import lombok.experimental.UtilityClass;
import tt.hashtranslator.dto.HashRequestDTO;

import javax.validation.ValidationException;
import java.util.Set;

@UtilityClass
public class HashValidationUtil {
    private static final String REGEX = "^[a-f0-9]{32}$";
    private static final String NO_HASHES = "Hashes are not presented. ";
    private static final String INVALID = "Invalid hashes:";

    public static void validate(HashRequestDTO requestDTO) {
        StringBuilder builder = new StringBuilder();
        Set<String> hashes = requestDTO.getHashes();
        if (requestDTO.getHashes() == null || hashes.isEmpty()) {
            builder.append(NO_HASHES);
            errorMessage(builder);
        }

        String invalidHashes = getInvalidHashes(hashes);
        if (!invalidHashes.isEmpty()) {
            builder.append(INVALID).append(invalidHashes).append(" ");
            errorMessage(builder);
        }
    }

    private static void errorMessage(StringBuilder builder) {
        String errorMessage = builder.toString();
        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }

    private static String getInvalidHashes(Set<String> hashes) {
        return hashes
                .stream()
                .filter(h -> !h.matches(REGEX))
                .reduce("", (a, b) -> a + " " + b);
    }

}
