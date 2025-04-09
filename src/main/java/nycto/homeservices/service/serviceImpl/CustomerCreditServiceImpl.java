package nycto.homeservices.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.CustomerCredit;
import nycto.homeservices.exceptions.CreditException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.CustomerCreditRepository;
import nycto.homeservices.service.serviceInterface.CustomerCreditService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerCreditServiceImpl implements CustomerCreditService {
    private final CustomerCreditRepository customerCreditRepository;

    @Override
    public void increaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException {
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        if (amount == null || amount <= 0) {
            throw new CreditException("Amount must be more than 0");
        }

        CustomerCredit credit = new CustomerCredit();
        credit.setCustomer(customer);
        credit.setAmount(amount);

        customerCreditRepository.save(credit);
    }

    @Override
    public void decreaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException {
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        if (amount == null || amount <= 0) {
            throw new CreditException("Amount must be more than 0");
        }

        Long totalCredit = customerCreditRepository.findTotalCreditByCustomerId(customer.getId());
        if (totalCredit == null || totalCredit < amount) {
            throw new CreditException("Insufficient credit");
        }

        CustomerCredit credit = new CustomerCredit();
        credit.setCustomer(customer);
        credit.setAmount(-amount);

        customerCreditRepository.save(credit);
    }

    @Override
    public Long getTotalCredit(Long customerId) {
        return Optional.ofNullable(customerCreditRepository.findTotalCreditByCustomerId(customerId))
                .orElse(0L);
    }


}
