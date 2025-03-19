package nycto.homeservices;

import lombok.RequiredArgsConstructor;

import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.dto.customerDto.CustomerUpdateDto;
import nycto.homeservices.entity.enums.UserStatus;
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
                    "Sina",
                    "Asadi",
                    "sina@gmail.com",
                    "sina123",
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


        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        try {
            var allCustomers = customerService.getAllCustomers();
            System.out.println("All customers: " + allCustomers);
        } catch (Exception e) {
            System.out.println("Error during read all: " + e.getMessage());
        }

//        try {
//            CustomerUpdateDto updateDto = new CustomerUpdateDto(
//                    "SajadUp ",
//                    "HoseiniUp ",
//                    "nyctoxr@example.com",
//                    UserStatus.APPROVED,
//                    1500L
//            );
//            var updatedCustomer = customerService.updateCustomer(1L, updateDto);
//            System.out.println("Customer updated successfully: " + updatedCustomer);
//
//        }catch (Exception e) {
//            System.out.println("Error occurred: " + e.getMessage());
//        }


        try {
            customerService.deleteCustomer(1L);
            System.out.println("Customer deleted successfully");
        } catch (Exception e) {
            System.out.println("Error during delete: " + e.getMessage());
        }





        System.out.println("Customer Test Runner finished.");




    }

}
