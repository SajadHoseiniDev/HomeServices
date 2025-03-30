package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.dto.customerDto.CustomerResponseDto;
import nycto.homeservices.dto.customerDto.CustomerUpdateDto;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerCreateDto createDto)
            throws NotValidInputException, DuplicateDataException;

    CustomerResponseDto getCustomerById(Long id) throws NotFoundException;

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto updateCustomer(Long id, CustomerUpdateDto updateDto) throws NotFoundException, NotValidInputException;

    void deleteCustomer(Long id) throws NotFoundException;
}
