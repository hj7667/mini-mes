package com.factoryflow.lot;

import java.time.LocalDateTime;

public class LotDto {

    public record CreateRequest(
            Long workOrderId,
            String machineCode, // LOT 번호 생성용 설비 코드
            int qty
    ) {}

    public record Response(
            Long id,
            String lotNo,
            Long workOrderId,
            ProcessStage stage,
            int qty,
            LotStatus status,
            Long parentLotId,
            LocalDateTime createdAt
    ) {
        public static Response from(Lot lot) {
            return new Response(
                    lot.getId(),
                    lot.getLotNo(),
                    lot.getWorkOrder().getId(),
                    lot.getStage(),
                    lot.getQty(),
                    lot.getStatus(),
                    lot.getParentLotId(),
                    lot.getCreatedAt()
            );
        }
    }
}