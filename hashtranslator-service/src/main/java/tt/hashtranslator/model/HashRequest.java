package tt.hashtranslator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "hashes")
public class HashRequest implements Serializable {
    private static final long serialVersionUID = 9167273410250650826L;
    @Id
    private String uuid;
    private List<Hash> hashes;
}
