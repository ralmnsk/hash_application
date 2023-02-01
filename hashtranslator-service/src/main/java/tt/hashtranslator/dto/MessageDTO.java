package tt.hashtranslator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -5886180119683347459L;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    public MessageDTO() {
    }

    public MessageDTO(String message) {
        this.message = message;
    }

    public MessageDTO(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
