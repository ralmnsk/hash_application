package tt.hashtranslator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Hash implements Serializable {
    private static final long serialVersionUID = 5402105243008169037L;
    private String md5;
    private String result;
    private boolean processed;
    private boolean decoded;
}
