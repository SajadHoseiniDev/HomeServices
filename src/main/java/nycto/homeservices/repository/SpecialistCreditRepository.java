package nycto.homeservices.repository;

import nycto.homeservices.entity.SpecialistCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SpecialistCreditRepository extends JpaRepository<SpecialistCredit, Long> {

    @Query("SELECT SUM(sc.amount) FROM SpecialistCredit sc WHERE sc.specialist.id = :specialistId")
    Long findTotalCreditBySpecialistId(@Param("specialistId") Long specialistId);
}
