package com.factoryflow.lot;

public enum LotStatus {
    AVAILABLE,  // 정상 (다음 공정 진행 가능)
    DEFECT,     // 불량 (격리 상태)
    SHIPPED     // 출하 완료
}