package nycto.homeservices.repository;

import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN o.subService ss " +
            "LEFT JOIN ss.service s " +
            "WHERE (cast(:startDate as timestamp) IS NULL OR o.orderDate >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR o.orderDate <= :endDate) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:serviceId IS NULL OR s.id = :serviceId) " +
            "AND (:subServiceId IS NULL OR ss.id = :subServiceId)")
    List<Order> findOrdersByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status,
            @Param("serviceId") Long serviceId,
            @Param("subServiceId") Long subServiceId
    );

}
