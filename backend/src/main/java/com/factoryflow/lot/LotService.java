package com.factoryflow.lot;

import com.factoryflow.common.exception.EntityNotFoundException;
import com.factoryflow.common.util.LotNumberGenerator;
import com.factoryflow.workorder.WorkOrder;
import com.factoryflow.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final WorkOrderRepository workOrderRepository;
    private final LotNumberGenerator lotNumberGenerator;

    @Transactional
    public LotDto.Response create(LotDto.CreateRequest request) {
        WorkOrder workOrder = workOrderRepository.findById(request.workOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "생산지시를 찾을 수 없습니다: " + request.workOrderId()));

        // 오늘 생성된 LOT 개수 기준으로 순번 부여
        long todaySeq = lotRepository.count() + 1;
        String lotNo = lotNumberGenerator.generate(request.machineCode(), (int) todaySeq);

        Lot lot = new Lot();
        lot.setLotNo(lotNo);
        lot.setWorkOrder(workOrder);
        lot.setStage(ProcessStage.WINDING); // 최초 생성 시 첫 공정부터 시작
        lot.setQty(request.qty());

        Lot saved = lotRepository.save(lot);
        return LotDto.Response.from(saved);
    }

    public List<LotDto.Response> getAll() {
        return lotRepository.findAll().stream()
                .map(LotDto.Response::from)
                .toList();
    }

    public LotDto.Response getByLotNo(String lotNo) {
        Lot lot = findByLotNoOrThrow(lotNo);
        return LotDto.Response.from(lot);
    }

    @Transactional
    public LotDto.Response advanceStage(String lotNo, ProcessStage nextStage) {
        Lot lot = findByLotNoOrThrow(lotNo);
        lot.setStage(nextStage);
        return LotDto.Response.from(lot);
    }

    private Lot findByLotNoOrThrow(String lotNo) {
        return lotRepository.findByLotNo(lotNo)
                .orElseThrow(() -> new EntityNotFoundException("LOT을 찾을 수 없습니다: " + lotNo));
    }
}