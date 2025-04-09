package nycto.homeservices.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.dto.customerDto.CustomerUpdateDto;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.service.serviceInterface.CustomerService;
import nycto.homeservices.dto.dtoMapper.CustomerMapper;
import nycto.homeservices.dto.customerDto.CustomerResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;


    @Override
    public CustomerResponseDto createCustomer(CustomerCreateDto createDto)
            throws NotValidInputException, DuplicateDataException {


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
    @Override
    public CustomerResponseDto getCustomerById(Long id) throws NotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toResponseDto(customer);
    }
    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerUpdateDto updateDto) throws NotFoundException, NotValidInputException {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));


        Customer updatedCustomer = customerMapper.toEntity(updateDto, existingCustomer);

        updatedCustomer.setId(id);

        return customerMapper.toResponseDto(customerRepository.save(updatedCustomer));
    }

    @Override
    public void deleteCustomer(Long id) throws NotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        customerRepository.delete(customer);
    }




}
