package nycto.homeservices.dto.dtoMapper;


import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.dto.customerDto.CustomerResponseDto;
import nycto.homeservices.dto.customerDto.CustomerUpdateDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerCreateDto createDto) {
        Customer customer = new Customer();
        customer.setFirstName(createDto.firstName());
        customer.setLastName(createDto.lastName());
        customer.setEmail(createDto.email());
        customer.setPassword(createDto.password());
        customer.setUserType(UserType.CUSTOMER);
        return customer;
    }

    public Customer toEntity(CustomerUpdateDto updateDto, Customer existingCustomer) {
        existingCustomer.setFirstName(updateDto.firstName());
        existingCustomer.setLastName(updateDto.lastName());
        existingCustomer.setEmail(updateDto.email());
        existingCustomer.setStatus(updateDto.status());
        return existingCustomer;

    }

    public CustomerResponseDto toResponseDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getStatus(),
                customer.getRegistrationDate()
        );
    }

}