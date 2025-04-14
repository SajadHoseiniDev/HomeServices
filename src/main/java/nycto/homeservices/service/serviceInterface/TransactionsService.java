package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.entity.Transactions;

import java.util.List;

public interface TransactionsService {

    void recordTransaction(Long customerId, Long specialistId, Long creditDeducted, Long creditAdded, Long orderId, String description);

    List<Transactions> getTransactionHistory(Long customerId);

    List<Transactions> getTransactionsByOrderId(Long orderId);
}
