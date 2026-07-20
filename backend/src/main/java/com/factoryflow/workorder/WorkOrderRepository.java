package com.factoryflow.workorder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    Optional<WorkOrder> findByOrderNo(String orderNo);

    boolean existsByOrderNo(String orderNo);
}