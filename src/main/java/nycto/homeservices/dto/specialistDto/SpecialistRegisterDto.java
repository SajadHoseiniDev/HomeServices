package nycto.homeservices.dto.specialistDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class SpecialistRegisterDto {
    @NotEmpty(message = "firstName can't be empty!")
    @Size(min = 3, max = 30, message = "firstName should be between 3 and 30 characters!")
    private String firstName;

    @NotEmpty(message = "lastName can't be empty!")
    @Size(min = 3, max = 30, message = "lastName should be between 3 and 30 characters!")
    private String lastName;

    @NotEmpty(message = "email can't be empty!")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "password can't be empty!")
    @Size(min = 8, message = "password must be at least 8 characters!")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "password must contain letters and numbers!")
    private String password;

    @NotNull(message = "profilePic can't be null!")
    private MultipartFile profilePic;

    }