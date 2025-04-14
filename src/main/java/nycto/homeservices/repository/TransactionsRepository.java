package nycto.homeservices.repository;

import nycto.homeservices.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByCustomerIdOrderByTransactionDateDesc(Long customerId);

    List<Transactions> findByOrderId(Long orderId);

    List<Transactions> findBySpecialistIdOrderByTransactionDateDesc(Long specialistId);
}
