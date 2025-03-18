package nycto.homeservices.repository;

import nycto.homeservices.entity.SpecialistCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistCreditRepository extends JpaRepository<SpecialistCredit, Long> {
}
