package com.factoryflow.quality;

public class QualityDto {

    // 검사 결과 등록 요청
    public record InspectRequest(
            String lotNo,
            int defectQty,
            DefectReason defectReason // 불량 없으면 null
    ) {}

    // 검사 결과 응답
    public record Response(
            String originalLotNo,
            int goodQty,
            String defectLotNo,  // 불량 없으면 null
            int defectQty
    ) {}
}