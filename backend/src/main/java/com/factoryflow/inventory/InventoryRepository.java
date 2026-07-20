package com.factoryflow.inventory;

import com.factoryflow.lot.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByLot(Lot lot);

    List<Inventory> findByWarehouse(WarehouseType warehouse);

    // FIFO 출하용: 동일 제품군, 완제품 창고 재고를 생성일자 오름차순(오래된 것 먼저)으로 조회
    @org.springframework.data.jpa.repository.Query("""
            SELECT i FROM Inventory i
            JOIN i.lot l
            JOIN l.workOrder wo
            WHERE wo.productName = :productName
              AND i.warehouse = :warehouse
              AND i.qty > 0
            ORDER BY i.updatedAt ASC
            """)
    List<Inventory> findAvailableStockByProductOrderByUpdatedAtAsc(
            String productName, WarehouseType warehouse);
}