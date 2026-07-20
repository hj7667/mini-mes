package com.factoryflow.workorder;

import com.factoryflow.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    @Transactional
    public WorkOrderDto.Response create(WorkOrderDto.CreateRequest request) {
        if (workOrderRepository.existsByOrderNo(request.orderNo())) {
            throw new IllegalArgumentException("이미 존재하는 지시번호입니다: " + request.orderNo());
        }

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNo(request.orderNo());
        workOrder.setProductName(request.productName());
        workOrder.setTargetQty(request.targetQty());

        WorkOrder saved = workOrderRepository.save(workOrder);
        return WorkOrderDto.Response.from(saved);
    }

    public List<WorkOrderDto.Response> getAll() {
        return workOrderRepository.findAll().stream()
                .map(WorkOrderDto.Response::from)
                .toList();
    }

    public WorkOrderDto.Response getById(Long id) {
        WorkOrder workOrder = findByIdOrThrow(id);
        return WorkOrderDto.Response.from(workOrder);
    }

    @Transactional
    public WorkOrderDto.Response update(Long id, WorkOrderDto.UpdateRequest request) {
        WorkOrder workOrder = findByIdOrThrow(id);
        workOrder.setProductName(request.productName());
        workOrder.setTargetQty(request.targetQty());
        return WorkOrderDto.Response.from(workOrder);
        // JPA 영속성 컨텍스트 덕분에 save() 호출 안 해도 트랜잭션 종료 시 자동 반영(dirty checking)
    }

    @Transactional
    public void delete(Long id) {
        WorkOrder workOrder = findByIdOrThrow(id);
        workOrderRepository.delete(workOrder);
    }

    @Transactional
    public WorkOrderDto.Response start(Long id) {
        WorkOrder workOrder = findByIdOrThrow(id);
        if (workOrder.getStatus() != WorkOrderStatus.READY) {
            throw new IllegalStateException("READY 상태에서만 시작할 수 있습니다.");
        }
        workOrder.setStatus(WorkOrderStatus.RUNNING);
        workOrder.setStartedAt(LocalDateTime.now());
        return WorkOrderDto.Response.from(workOrder);
    }

    @Transactional
    public WorkOrderDto.Response complete(Long id) {
        WorkOrder workOrder = findByIdOrThrow(id);
        if (workOrder.getStatus() != WorkOrderStatus.RUNNING) {
            throw new IllegalStateException("RUNNING 상태에서만 종료할 수 있습니다.");
        }
        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        workOrder.setCompletedAt(LocalDateTime.now());
        return WorkOrderDto.Response.from(workOrder);
    }

    // 생산 시뮬레이션에서 생산량 1씩 증가시킬 때 사용 (websocket에서 호출)
    @Transactional
    public void incrementProducedQty(Long id) {
        WorkOrder workOrder = findByIdOrThrow(id);
        workOrder.setProducedQty(workOrder.getProducedQty() + 1);
        if (workOrder.getProducedQty() >= workOrder.getTargetQty()) {
            complete(id);
        }
    }

    private WorkOrder findByIdOrThrow(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("생산지시를 찾을 수 없습니다: " + id));
    }
}