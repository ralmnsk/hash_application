package tt.hashtranslator.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HashResponseDTO implements Serializable {
    private static final long serialVersionUID = 9084507607704112302L;
    @JsonIgnore
    private String uuid;
    private List<Map<String,String>> hashes;
}
