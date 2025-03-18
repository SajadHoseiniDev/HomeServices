package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.userDto.UserCreateDto;
import nycto.homeservices.dto.userDto.UserDto;
import nycto.homeservices.dto.userDto.UserMapper;
import nycto.homeservices.dto.userDto.UserResponseDto;
import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserCreateDto createDto)
            throws NotValidInputException, DuplicateDataException {

        if (!validationUtil.validate(createDto))
            throw new NotValidInputException("Not valid user data");
        if (userRepository.findByEmail(createDto.email()).isPresent())
            throw new DuplicateDataException
                    ("user with email:" + createDto.email()
                            + "is already exists");
        User user = new User();
        user.setFirstName(createDto.firstName());
        user.setLastName(createDto.lastName());
        user.setEmail(createDto.email());
        user.setPassword(createDto.password());
        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus(UserStatus.NEW);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);


    }

}
