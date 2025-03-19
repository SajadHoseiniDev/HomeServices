package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.dto.customerDto.CustomerUpdateDto;
import nycto.homeservices.exceptions.NotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final CustomerMapper customerMapper;


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

    public CustomerResponseDto getCustomerById(Long id) throws NotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toResponseDto(customer);
    }

    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public CustomerResponseDto updateCustomer(Long id, CustomerUpdateDto updateDto) throws NotFoundException, NotValidInputException {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");

        Customer updatedCustomer = customerMapper.toEntity(updateDto, existingCustomer);
        updatedCustomer.setId(id);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    public void deleteCustomer(Long id) throws NotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        customerRepository.delete(customer);
    }




}
