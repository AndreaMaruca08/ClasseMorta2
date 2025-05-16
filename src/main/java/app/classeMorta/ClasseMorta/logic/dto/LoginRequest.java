package app.classeMorta.ClasseMorta.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private char[] password;
}
