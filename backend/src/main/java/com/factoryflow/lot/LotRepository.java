package com.factoryflow.lot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Long> {

    Optional<Lot> findByLotNo(String lotNo);

    List<Lot> findByWorkOrderId(Long workOrderId);

    boolean existsByLotNo(String lotNo);
}