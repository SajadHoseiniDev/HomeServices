package nycto.homeservices.repository;

import nycto.homeservices.entity.Service;
import nycto.homeservices.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService,Long> {

    Optional<SubService> findByName(String name);
}
