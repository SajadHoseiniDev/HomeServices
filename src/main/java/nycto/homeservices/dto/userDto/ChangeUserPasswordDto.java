package nycto.homeservices.dto.userDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUserPasswordDto(

        @NotEmpty(message = "password can't be empty!")
        @Size(min = 8, message = "password must be at least 8 characters!")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "password should have letters and numbers!")
        String password
) {
}
