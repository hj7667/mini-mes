package com.factoryflow.inventory;

import java.time.LocalDateTime;

public class InventoryDto {

    public record Response(
            Long id,
            String lotNo,
            String productName,
            WarehouseType warehouse,
            int qty,
            LocalDateTime updatedAt
    ) {
        public static Response from(Inventory inventory) {
            return new Response(
                    inventory.getId(),
                    inventory.getLot().getLotNo(),
                    inventory.getLot().getWorkOrder().getProductName(),
                    inventory.getWarehouse(),
                    inventory.getQty(),
                    inventory.getUpdatedAt()
            );
        }
    }
}