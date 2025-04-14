package nycto.homeservices.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.Transactions;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.CustomerRepository;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.repository.TransactionsRepository;
import nycto.homeservices.service.serviceInterface.TransactionsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final SpecialistRepository specialistRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void recordTransaction(Long customerId, Long specialistId, Long creditDeducted, Long creditAdded, Long orderId, String description) {
        Transactions transaction = new Transactions();
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(description);

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new NotFoundException("Customer with id " + customerId + " not found"));
            transaction.setCustomer(customer);
        }

        if (specialistId != null) {
            Specialist specialist = specialistRepository.findById(specialistId)
                    .orElseThrow(() -> new NotFoundException("Specialist with id " + specialistId + " not found"));
            transaction.setSpecialist(specialist);
        }

        if (orderId != null) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));
            transaction.setOrder(order);
        }


        transaction.setCreditDeducted(creditDeducted);
        transaction.setCreditAdded(creditAdded);

        transactionsRepository.save(transaction);
    }

    @Override
    public List<Transactions> getTransactionHistory(Long customerId) {
        return transactionsRepository.findByCustomerIdOrderByTransactionDateDesc(customerId);
    }
    @Override
    public List<Transactions> getTransactionsByOrderId(Long orderId){
        return transactionsRepository.findByOrderId(orderId);
    }
}
