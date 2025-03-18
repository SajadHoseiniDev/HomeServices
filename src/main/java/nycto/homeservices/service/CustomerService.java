package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.util.dtoMapper.CustomerMapper;
import nycto.homeservices.dto.customerDto.CustomerResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.CustomerRepository;
import nycto.homeservices.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private CustomerMapper customerMapper;


    public CustomerResponseDto createCustomer(CustomerCreateDto createDto)
            throws NotValidInputException, DuplicateDataException {

        if (!validationUtil.validate(createDto))
            throw new NotValidInputException("Not valid user data");

        if (customerRepository.findByEmail(createDto.email()).isPresent())
            throw new DuplicateDataException
                    ("user with email:" + createDto.email()
                            + "is already exists");


        Customer customer = customerMapper.toEntity(createDto);
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setStatus(UserStatus.NEW);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponseDto(savedCustomer);

    }


}
