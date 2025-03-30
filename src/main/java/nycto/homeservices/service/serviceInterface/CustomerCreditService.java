package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.entity.Customer;
import nycto.homeservices.exceptions.CreditException;
import nycto.homeservices.exceptions.NotFoundException;

public interface CustomerCreditService {
    void increaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException;

    void decreaseCustomerCredit(Customer customer, Long amount) throws NotFoundException, CreditException;
}
