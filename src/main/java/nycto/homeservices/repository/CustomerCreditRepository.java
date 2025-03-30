package nycto.homeservices.repository;

import nycto.homeservices.entity.CustomerCredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, Long> {
}
