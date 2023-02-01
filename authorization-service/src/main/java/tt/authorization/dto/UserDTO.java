package tt.authorization.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 8302980035720919967L;
    private Long id;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Incorrect email. Email example: name@gmail.com")
    private String email;
    @Pattern(regexp = "[A-Za-z0-9]{3,8}", message = "Password should include 3-8 symbols: A-Z, a-z, 0-9")
    private String password;
}
