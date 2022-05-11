package com.mh.soc.repository;

import com.mh.soc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    @Query(value = "SELECT order_id FROM user_order WHERE user_id = :id", nativeQuery = true)
    List<Long> getOrderIds(Long id);

    @Query(value = "SELECT user_id FROM user_order WHERE order_id = :id", nativeQuery = true)
    Long getUserId(Long id);
}