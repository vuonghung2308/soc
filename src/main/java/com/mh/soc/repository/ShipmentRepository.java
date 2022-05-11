package com.mh.soc.repository;

import com.mh.soc.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query(value = "SELECT shipment_id FROM user_shipment WHERE user_id = :id", nativeQuery = true)
    List<Long> getShipmentIds(Long id);
}