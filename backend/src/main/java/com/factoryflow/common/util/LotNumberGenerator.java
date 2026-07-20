package com.factoryflow.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LotNumberGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * LOT 번호 생성
     * @param machineCode 설비 코드 (예: M01)
     * @param sequence 그날의 순번 (예: 1 → "001")
     * @return LOT-20260720-M01-001
     */
    public String generate(String machineCode, int sequence) {
        String datePart = LocalDate.now().format(DATE_FORMAT);
        String seqPart = String.format("%03d", sequence);
        return String.format("LOT-%s-%s-%s", datePart, machineCode, seqPart);
    }

    /**
     * 불량 LOT 번호 생성 (원본 LOT 번호에 -ERR 접미사)
     */
    public String generateDefectLotNo(String originalLotNo) {
        return originalLotNo + "-ERR";
    }
}