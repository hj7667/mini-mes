package com.factoryflow.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final FifoShipmentService fifoShipmentService;

    @PostMapping
    public ResponseEntity<ShipmentDto.Response> ship(@RequestBody ShipmentDto.Request request) {
        return ResponseEntity.ok(fifoShipmentService.ship(request));
    }
}touch src/main/resources/application.properties