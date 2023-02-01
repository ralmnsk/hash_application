package tt.hashtranslator.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Data
public class HashRequestDTO implements Serializable {
    private static final long serialVersionUID = 4761082198111669699L;
    private String uuid;
    @NotEmpty
    private Set<String> hashes;
}
