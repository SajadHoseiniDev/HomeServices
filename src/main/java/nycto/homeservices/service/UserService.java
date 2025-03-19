package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.userDto.*;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.util.dtoMapper.UserMapper;
import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


        User user = userMapper.toEntity(createDto);
        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus(UserStatus.NEW);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);

    }


    public UserResponseDto getUserById(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        return userMapper.toResponseDto(user);
    }


    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    public UserResponseDto updateUser(Long id, UserUpdateDto updateDto)
            throws NotFoundException, NotValidInputException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");

        User updatedUser = userMapper.toEntity(updateDto, existingUser);
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);
        return userMapper.toResponseDto(savedUser);
    }


    public void deleteUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

    public void changePassword(Long id, ChangeUserPasswordDto passwordDto) throws NotFoundException, NotValidInputException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        if (!validationUtil.validate(passwordDto))
            throw new NotValidInputException("Not valid specialist data");

        user.setPassword(passwordDto.password());

        userRepository.save(user);

    }

    public List<UserResponseDto> getUsersByFilter(FilteringDto filterParams) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserType(
                filterParams.firstName(),
                filterParams.lastName(),
                filterParams.email(),
                filterParams.userType()
        );
        return users.stream().map(userMapper::toResponseDto).collect(Collectors.toList());

    }


}
