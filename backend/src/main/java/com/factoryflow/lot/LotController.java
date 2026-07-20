package com.factoryflow.lot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @PostMapping
    public ResponseEntity<LotDto.Response> create(@RequestBody LotDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lotService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LotDto.Response>> getAll() {
        return ResponseEntity.ok(lotService.getAll());
    }

    @GetMapping("/{lotNo}")
    public ResponseEntity<LotDto.Response> getByLotNo(@PathVariable String lotNo) {
        return ResponseEntity.ok(lotService.getByLotNo(lotNo));
    }

    @PatchMapping("/{lotNo}/stage")
    public ResponseEntity<LotDto.Response> advanceStage(
            @PathVariable String lotNo,
            @RequestParam ProcessStage nextStage) {
        return ResponseEntity.ok(lotService.advanceStage(lotNo, nextStage));
    }
}