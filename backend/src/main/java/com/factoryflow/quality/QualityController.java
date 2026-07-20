package com.factoryflow.quality;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityInspectionService qualityInspectionService;

    @PostMapping("/inspect")
    public ResponseEntity<QualityDto.Response> inspect(@RequestBody QualityDto.InspectRequest request) {
        return ResponseEntity.ok(qualityInspectionService.inspect(request));
    }
}