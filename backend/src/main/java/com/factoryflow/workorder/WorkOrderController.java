package com.factoryflow.workorder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<WorkOrderDto.Response> create(@RequestBody WorkOrderDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderDto.Response>> getAll() {
        return ResponseEntity.ok(workOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderDto.Response> update(
            @PathVariable Long id,
            @RequestBody WorkOrderDto.UpdateRequest request) {
        return ResponseEntity.ok(workOrderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<WorkOrderDto.Response> start(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.start(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<WorkOrderDto.Response> complete(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.complete(id));
    }
}