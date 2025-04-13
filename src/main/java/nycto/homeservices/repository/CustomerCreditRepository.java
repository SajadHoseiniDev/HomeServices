package nycto.homeservices.repository;

import nycto.homeservices.entity.CustomerCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, Long> {

    @Query("SELECT SUM(cc.amount) FROM CustomerCredit cc WHERE cc.customer.id = :customerId")
    Long findTotalCreditByCustomerId(@Param("customerId") Long customerId);

    CustomerCredit findByCustomerId(@Param("customerId") Long customerId);
}
