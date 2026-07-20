package com.factoryflow.workorder;

public class WorkOrderDto {

    // 생산지시 등록 요청
    public record CreateRequest(
            String orderNo,
            String productName,
            int targetQty
    ) {}

    // 생산지시 수정 요청
    public record UpdateRequest(
            String productName,
            int targetQty
    ) {}

    // 응답
    public record Response(
            Long id,
            String orderNo,
            String productName,
            int targetQty,
            int producedQty,
            int defectQty,
            double progressRate,
            WorkOrderStatus status
    ) {
        public static Response from(WorkOrder wo) {
            return new Response(
                    wo.getId(),
                    wo.getOrderNo(),
                    wo.getProductName(),
                    wo.getTargetQty(),
                    wo.getProducedQty(),
                    wo.getDefectQty(),
                    wo.getProgressRate(),
                    wo.getStatus()
            );
        }
    }
}