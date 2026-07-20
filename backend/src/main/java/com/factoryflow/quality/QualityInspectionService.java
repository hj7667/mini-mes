package com.factoryflow.quality;

import com.factoryflow.common.exception.EntityNotFoundException;
import com.factoryflow.common.util.LotNumberGenerator;
import com.factoryflow.inventory.InventoryService;
import com.factoryflow.inventory.WarehouseType;
import com.factoryflow.lot.Lot;
import com.factoryflow.lot.LotRepository;
import com.factoryflow.lot.LotStatus;
import com.factoryflow.lot.ProcessStage;
import com.factoryflow.workorder.WorkOrder;
import com.factoryflow.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QualityInspectionService {

    private final LotRepository lotRepository;
    private final WorkOrderRepository workOrderRepository;
    private final InventoryService inventoryService;
    private final LotNumberGenerator lotNumberGenerator;

    /**
     * 성능검사 결과를 반영한다.
     * 불량 발생 시: 기존 LOT 수량 차감 → 불량 전용 LOT 자동 생성 → 재고 자동 분리 입고
     * 이 메서드 안 어디서든 예외가 발생하면 전체가 롤백된다 (LOT 차감도 원복됨).
     */
    @Transactional
    public QualityDto.Response inspect(QualityDto.InspectRequest request) {
        Lot originalLot = lotRepository.findByLotNo(request.lotNo())
                .orElseThrow(() -> new EntityNotFoundException("LOT을 찾을 수 없습니다: " + request.lotNo()));

        int defectQty = request.defectQty();
        if (defectQty < 0 || defectQty > originalLot.getQty()) {
            throw new IllegalArgumentException("불량 수량이 유효하지 않습니다.");
        }

        int goodQty = originalLot.getQty() - defectQty;

        // 1. 기존 LOT: 양품 수량만 남기고 다음 공정(포장)으로 이동
        originalLot.setQty(goodQty);
        originalLot.setStage(ProcessStage.PACKAGING);

        String defectLotNo = null;

        // 2. 불량 발생 시에만 별도 LOT 생성 + 격리창고 입고
        if (defectQty > 0) {
            Lot defectLot = new Lot();
            defectLotNo = lotNumberGenerator.generateDefectLotNo(originalLot.getLotNo());
            defectLot.setLotNo(defectLotNo);
            defectLot.setWorkOrder(originalLot.getWorkOrder());
            defectLot.setStage(ProcessStage.INSPECTION);
            defectLot.setQty(defectQty);
            defectLot.setStatus(LotStatus.DEFECT);
            defectLot.setParentLotId(originalLot.getId());
            lotRepository.save(defectLot);

            inventoryService.stockIn(defectLot, WarehouseType.QUARANTINE, defectQty);
        }

        // 3. 양품 재고 → 완제품 창고 입고 (양품이 남아있을 때만)
        if (goodQty > 0) {
            inventoryService.stockIn(originalLot, WarehouseType.FINISHED_GOODS, goodQty);
        }

        // 4. WorkOrder 불량 수량 누적
        WorkOrder workOrder = originalLot.getWorkOrder();
        workOrder.setDefectQty(workOrder.getDefectQty() + defectQty);
        workOrderRepository.save(workOrder);

        lotRepository.save(originalLot);

        return new QualityDto.Response(
                originalLot.getLotNo(),
                goodQty,
                defectLotNo,
                defectQty
        );
    }
}