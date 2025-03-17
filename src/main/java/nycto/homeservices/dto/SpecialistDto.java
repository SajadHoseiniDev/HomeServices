package nycto.homeservices.dto;

import jakarta.validation.constraints.*;

public record SpecialistDto(
        Long id
        ,
        @NotEmpty(message = "firstName can't be empty!")
        @Size(min = 3, max = 30, message = "fistName should be between 3 and 30 characters!")
        String firstName

        ,

        @NotEmpty(message = "lastName can't be empty!")
        @Size(min = 3, max = 30, message = "lastName should be between 3 and 30 characters!")
        String lastName

        ,
        @NotEmpty(message = "email can't be empty!")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String email

        , @Min(1)
        @Max(5)
        int rating

        ,
        @NotEmpty(message = "url can't be empty!")
        @Pattern(regexp = "^(?:https?:\\\\/\\\\/)?([a-zA-Z0-9\\\\-\\\\.]+\\\\.)*[a-zA-Z0-9\\\\-\\\\.]+\\\\.[a-zA-Z]{2,6}\\\\/?.*\\\\.(jpg|jpeg|png|gif)$|^[a-zA-Z0-9\\\\-\\\\.]+\\\\.(jpg|jpeg|png|gif)$")
        String profilePicUrl
) {
}
