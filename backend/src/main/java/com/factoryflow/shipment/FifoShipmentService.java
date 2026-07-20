package com.factoryflow.shipment;

import com.factoryflow.common.exception.InsufficientStockException;
import com.factoryflow.inventory.Inventory;
import com.factoryflow.inventory.InventoryRepository;
import com.factoryflow.inventory.WarehouseType;
import com.factoryflow.lot.Lot;
import com.factoryflow.lot.LotRepository;
import com.factoryflow.lot.LotStatus;
import com.factoryflow.lot.ProcessStage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.factoryflow.common.exception.InsufficientStockException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FifoShipmentService {

    private final InventoryRepository inventoryRepository;
    private final LotRepository lotRepository;

    /**
     * 선입선출(FIFO) 출하 처리.
     * 완제품 창고 재고 중 생성일자가 가장 빠른 LOT부터 순차적으로 차감한다.
     * 여러 LOT에 걸쳐 출하되는 경우 자동으로 다음 LOT까지 이어서 처리한다.
     */
    @Transactional
    public ShipmentDto.Response ship(ShipmentDto.Request request) {
        List<Inventory> stocks = inventoryRepository
                .findAvailableStockByProductOrderByUpdatedAtAsc(
                        request.productName(), WarehouseType.FINISHED_GOODS);

        int remaining = request.requestQty();
        List<ShipmentDto.LotDeduction> deductions = new ArrayList<>();

        for (Inventory stock : stocks) {
            if (remaining <= 0) break;

            int deduct = Math.min(remaining, stock.getQty());
            stock.setQty(stock.getQty() - deduct);
            remaining -= deduct;

            Lot lot = stock.getLot();
            if (stock.getQty() == 0) {
                lot.setStatus(LotStatus.SHIPPED);
                lot.setStage(ProcessStage.SHIPPED);
                lotRepository.save(lot);
            }

            inventoryRepository.save(stock);
            deductions.add(new ShipmentDto.LotDeduction(lot.getLotNo(), deduct));
        }

        if (remaining > 0) {
            throw new InsufficientStockException(
                    "재고가 부족합니다. 부족 수량: " + remaining + " EA");
        }

        return new ShipmentDto.Response(
                request.productName(),
                request.requestQty(),
                deductions
        );
    }
}