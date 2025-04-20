package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.userDto.*;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserCreateDto createDto) ;

    UserResponseDto getUserById(Long id) throws NotFoundException;

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserUpdateDto updateDto)
            throws NotFoundException, NotValidInputException;

    void deleteUser(Long id) throws NotFoundException;

    void changePassword(Long id, ChangeUserPasswordDto passwordDto) throws NotFoundException, NotValidInputException;

    List<UserResponseDto> getUsersByFilter(FilteringDto filterParams);

    UserHistoryDto getUserHistory(Long userId, String userType);

    List<UserReportDto> getCustomerReport(LocalDateTime startDate, LocalDateTime endDate);

    List<UserReportDto> getSpecialistReport(LocalDateTime startDate, LocalDateTime endDate);
}
