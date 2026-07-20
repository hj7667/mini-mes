package com.factoryflow.inventory;

import com.factoryflow.lot.Lot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryDto.Response> getAll() {
        return inventoryRepository.findAll().stream()
                .map(InventoryDto.Response::from)
                .toList();
    }

    public List<InventoryDto.Response> getByWarehouse(WarehouseType warehouse) {
        return inventoryRepository.findByWarehouse(warehouse).stream()
                .map(InventoryDto.Response::from)
                .toList();
    }

    // 품질검사 서비스에서 호출: 특정 LOT을 특정 창고에 입고 처리
    public Inventory stockIn(Lot lot, WarehouseType warehouse, int qty) {
        Inventory inventory = new Inventory();
        inventory.setLot(lot);
        inventory.setWarehouse(warehouse);
        inventory.setQty(qty);
        return inventoryRepository.save(inventory);
    }
}