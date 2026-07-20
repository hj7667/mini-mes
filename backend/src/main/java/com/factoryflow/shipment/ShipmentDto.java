package com.factoryflow.shipment;

import java.util.List;

public class ShipmentDto {

    // 출하 요청
    public record Request(
            String productName,
            int requestQty
    ) {}

    // LOT 하나에서 차감된 내역
    public record LotDeduction(
            String lotNo,
            int deductedQty
    ) {}

    // 출하 전체 결과
    public record Response(
            String productName,
            int totalShippedQty,
            List<LotDeduction> deductions
    ) {}
}