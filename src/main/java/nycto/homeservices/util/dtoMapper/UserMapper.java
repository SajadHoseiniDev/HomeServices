package nycto.homeservices.util.dtoMapper;

import nycto.homeservices.dto.userDto.UserCreateDto;
import nycto.homeservices.dto.userDto.UserResponseDto;
import nycto.homeservices.dto.userDto.UserUpdateDto;
import nycto.homeservices.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto createDto) {
        User user = new User();
        user.setFirstName(createDto.firstName());
        user.setLastName(createDto.lastName());
        user.setEmail(createDto.email());
        user.setPassword(createDto.password());
        return user;
    }


    public User toEntity(UserUpdateDto updateDto, User existingUser) {
        existingUser.setFirstName(updateDto.firstName());
        existingUser.setLastName(updateDto.lastName());
        existingUser.setEmail(updateDto.email());
        existingUser.setStatus(updateDto.status());
        return existingUser;
    }



    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                user.getRegistrationDate()
        );
    }
}
