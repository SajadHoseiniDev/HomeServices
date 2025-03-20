package nycto.homeservices;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.userDto.ChangeUserPasswordDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.service.CustomerService;
import nycto.homeservices.service.ServiceService;
import nycto.homeservices.service.SpecialistService;
import nycto.homeservices.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {


    private final CustomerService customerService;
    private final SpecialistService specialistService;
    private final UserService userService;
    private final ServiceService serviceService;


    @Override
    public void run(String... args) {

        try {
            SpecialistCreateDto newSpecialist = new SpecialistCreateDto(
                    "Reza",
                    "Golestani",
                    "reza@gmail.com",
                    "reza123",
                    "\"E:\\MaktabSharif\\FinalProject\\HomeService\\asset\\RadioHead.jpg\""
            );


            var result = specialistService.createSpecialist(newSpecialist);
            System.out.println("specialist created successfully: " + result);

        } catch (NotValidInputException | DuplicateDataException e) {
            System.out.println("Error occurred: " + e.getMessage());

        }

        try {
            ServiceCreateDto newService = new ServiceCreateDto(
                    "Home Appliance"

            );
            var result = serviceService.createService(newService);
            System.out.println("service created successfully: " + result);

        } catch (Exception e) {
            System.out.println("Error during change password: " + e.getMessage());
        }

//
//        try {
//            var customer = customerService.getCustomerById(1L);
//            System.out.println("Customer found: " + customer);
//
//
//        } catch (Exception e) {
//            System.out.println("Error occurred: " + e.getMessage());
//        }
//
//        try {
//            var allCustomers = customerService.getAllCustomers();
//            System.out.println("All customers: " + allCustomers);
//        } catch (Exception e) {
//            System.out.println("Error during read all: " + e.getMessage());
//        }

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

//
//        try {
//            customerService.deleteCustomer(1L);
//            System.out.println("Customer deleted successfully");
//        } catch (Exception e) {
//            System.out.println("Error during delete: " + e.getMessage());
//        }





        System.out.println("Customer Test Runner finished.");




    }

}
