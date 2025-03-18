package nycto.homeservices;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.customerDto.CustomerCreateDto;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerTestRunner implements CommandLineRunner {


    private final CustomerService customerService;


    @Override
    public void run(String... args) throws Exception {

        try {
            CustomerCreateDto createDto = new CustomerCreateDto(
                    "Sajad",
                    "Hoseini",
                    "nyctoxr@gmail.com",
                    "sajad123",
                    0L
            );


            var result = customerService.createCustomer(createDto);
            System.out.println("Customer created successfully: " + result);

        } catch (NotValidInputException | DuplicateDataException e) {
            System.out.println("Error occurred: " + e.getMessage());

        }

    }
}
