package com.factoryflow.workorder;

public enum WorkOrderStatus {
    READY,      // 대기 (생산 시작 전)
    RUNNING,    // 생산 중
    COMPLETED   // 완료
}