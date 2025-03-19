package nycto.homeservices;

import lombok.RequiredArgsConstructor;

import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.service.CustomerService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerTestRunner implements CommandLineRunner {


    private final CustomerService customerService;


    @Override
    public void run(String... args) {

        try {
            CustomerCreateDto createDto = new CustomerCreateDto(
                    "Mohamad",
                    "rezaei",
                    "mmgmail.com",
                    "mamad123",
                    0L
            );


            var result = customerService.createCustomer(createDto);
            System.out.println("Customer created successfully: " + result);

        } catch (NotValidInputException | DuplicateDataException e) {
            System.out.println("Error occurred: " + e.getMessage());

        }


        try {
            var customer = customerService.getCustomerById(1L);
            System.out.println("Customer found: " + customer);


        }catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

    }

}
