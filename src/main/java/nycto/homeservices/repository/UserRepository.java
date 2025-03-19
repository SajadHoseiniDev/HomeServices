package nycto.homeservices.repository;

import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    @Query("SELECT u FROM User u " +
            "LEFT JOIN Specialist s ON u.id = s.id " +
            "LEFT JOIN s.services srv " +
            "WHERE (:firstName IS NULL OR u.firstName LIKE %:firstName%) " +
            "AND (:lastName IS NULL OR u.lastName LIKE %:lastName%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:userType IS NULL OR u.userType = :userType) " +
            "AND (:serviceName IS NULL OR srv.name LIKE %:serviceName%)")
    List<User> findUsersByFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("userType") UserType userType,
            @Param("serviceName") String serviceName
    );

}
