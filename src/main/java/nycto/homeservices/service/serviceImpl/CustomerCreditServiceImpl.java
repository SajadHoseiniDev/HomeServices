package nycto.homeservices.service.serviceImpl;

import jakarta.transaction.Transactional;
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
    @Transactional
    public void increaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException {
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        if (amount == null || amount <= 0) {
            throw new CreditException("Amount must be more than 0");
        }

        CustomerCredit credit = customerCreditRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    CustomerCredit newCredit = new CustomerCredit();
                    newCredit.setCustomer(customer);
                    newCredit.setAmount(0L);
                    return customerCreditRepository.save(newCredit);
                });
        credit.setAmount(credit.getAmount() + amount);
        customerCreditRepository.save(credit);
    }

    @Override
    @Transactional
    public void decreaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException {
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        if (amount == null || amount <= 0) {
            throw new CreditException("Amount must be more than 0");
        }

        CustomerCredit credit = customerCreditRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new NotFoundException("Credit record not found for customer"));
        if (credit.getAmount() < amount) {
            throw new CreditException("Insufficient credit");
        }

        credit.setAmount(credit.getAmount() - amount);
        customerCreditRepository.save(credit);
    }

    @Override
    public Long getTotalCredit(Long customerId) {
        return customerCreditRepository.findByCustomerId(customerId)
                .map(CustomerCredit::getAmount)
                .orElse(0L);
    }

}
