package nycto.homeservices.repository;

import nycto.homeservices.entity.CustomerCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, Long> {
}
