package com.factoryflow.workorder;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNo; // 지시번호 예: WO-20260720-001

    @Column(nullable = false)
    private String productName; // 제품명 예: Motor-A

    @Column(nullable = false)
    private int targetQty; // 목표수량

    @Column(nullable = false)
    private int producedQty = 0; // 생산수량 (기본값 0)

    @Column(nullable = false)
    private int defectQty = 0; // 불량수량 (기본값 0)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderStatus status = WorkOrderStatus.READY;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 진행률 계산 (0~100)
    public double getProgressRate() {
        if (targetQty == 0) return 0;
        return Math.round((double) producedQty / targetQty * 1000) / 10.0;
    }
}