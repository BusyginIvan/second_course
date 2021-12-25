package rest.json;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Credentials {
    @NotNull(message = "Требуется имя пользователя.")
    @NotEmpty(message = "Имя пользователя не может быть пустым.")
    @Size(min = 4, message = "Слишком короткое имя пользователя.")
    @Size(max = 15, message = "Слишком длинное имя пользователя.")
    private String username;
    @NotNull(message = "Требуется пароль.")
    @NotEmpty(message = "Пароль не может быть пустым.")
    @Size(min = 4, message = "Пароль должен быть длиннее 4-х символов.")
    private String password;
}
