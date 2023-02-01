package tt.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO implements Serializable {
    private static final long serialVersionUID = -2207484149330036549L;
    private String name;
    private String authority;
}
