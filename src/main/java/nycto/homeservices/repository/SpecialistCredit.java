package nycto.homeservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistCredit extends JpaRepository<SpecialistCredit, Long> {
}
