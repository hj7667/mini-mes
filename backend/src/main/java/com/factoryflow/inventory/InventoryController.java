package com.factoryflow.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryDto.Response>> getAll(
            @RequestParam(required = false) WarehouseType warehouse) {
        if (warehouse != null) {
            return ResponseEntity.ok(inventoryService.getByWarehouse(warehouse));
        }
        return ResponseEntity.ok(inventoryService.getAll());
    }
}