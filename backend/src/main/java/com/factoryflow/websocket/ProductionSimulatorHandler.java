package com.factoryflow.websocket;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import tools.jackson.databind.ObjectMapper;

// @Component: "이 클래스는 Spring이 관리하는 부품(Bean)이야"라는 표시
// 이게 있어야 WebSocketConfig에서 자동으로 갖다 쓸 수 있음
@Component
public class ProductionSimulatorHandler extends TextWebSocketHandler {

    // 자바 객체를 JSON 문자열로 바꿔주는 도구
    // (예: {"producedQty": 5} 이런 텍스트로 변환해줌)
    private final ObjectMapper objectMapper = new ObjectMapper();

    // RPM, 온도를 랜덤으로 흔들리게 하기 위한 난수 생성기
    private final Random random = new Random();

    // 현재 접속 중인 모든 클라이언트(브라우저 창)를 담아두는 목록
    // CopyOnWriteArraySet: 여러 명이 동시에 접속/해제해도 안전하게 처리해주는 특수 Set
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    // 지금까지 생산된 수량 (서버가 켜져있는 동안 계속 누적됨)
    private int producedQty = 0;

    // 생성자: 이 클래스가 만들어지는 순간, "5초마다 반복 실행"을 예약해둠
    public ProductionSimulatorHandler() {
        // 정해진 시간마다 작업을 반복 실행해주는 자바 표준 도구
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // broadcastProductionData 메서드를, 5초 뒤에 처음 실행하고
        // 그 이후로도 계속 5초 간격으로 반복 실행하라는 예약
        scheduler.scheduleAtFixedRate(this::broadcastProductionData, 5, 5, TimeUnit.SECONDS);
    }

    // 새로운 클라이언트(브라우저)가 WebSocket에 접속했을 때 자동으로 호출되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 접속한 클라이언트를 목록에 추가 (앞으로 이 사람한테도 데이터 쏴줘야 하니까)
        sessions.add(session);
    }

    // 클라이언트가 연결을 끊었을 때 자동으로 호출되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        // 나간 클라이언트를 목록에서 제거 (더 이상 데이터 안 보내도 되니까)
        sessions.remove(session);
    }

    // 5초마다 실제로 실행되는 핵심 로직
    private void broadcastProductionData() {
        // 생산량 1 증가 (실제 공장에서 모터 하나 더 만들어졌다고 가정)
        producedQty += 1;

        // RPM 값을 2400~2600 사이에서 랜덤하게 생성 (설비가 살짝씩 흔들리는 걸 흉내)
        int rpm = 2400 + random.nextInt(200);

        // 온도 값을 38~45도 사이에서 랜덤하게 생성
        int temp = 38 + random.nextInt(8);

        // 프론트로 보낼 데이터를 하나의 묶음(Map)으로 정리
        Map<String, Object> data = Map.of(
                "producedQty", producedQty,
                "rpm", rpm,
                "temperature", temp
        );

        try {
            // Map 객체를 JSON 문자열로 변환 (프론트가 이해할 수 있는 형태로)
            String json = objectMapper.writeValueAsString(data);

            // 현재 접속 중인 모든 클라이언트에게 똑같은 데이터를 동시에 전송 (방송, broadcast)
            for (WebSocketSession session : sessions) {
                // 혹시 연결이 끊긴 세션한테 잘못 보내는 걸 방지하기 위한 확인
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            // JSON 변환이나 전송 중 문제가 생기면, 서버가 죽지 않고 에러만 출력하도록 처리
            e.printStackTrace();
        }
    }
}