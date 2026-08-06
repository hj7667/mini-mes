package com.factoryflow.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// @Configuration: "이 클래스는 설정 파일이야"라고 Spring한테 알려주는 표시
@Configuration
// @EnableWebSocket: "이 프로젝트는 WebSocket 기능을 쓸 거야"라고 선언
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // 실제로 실시간 데이터를 만들어서 보내는 담당자(핸들러)를 여기서 가져다 씀
    private final ProductionSimulatorHandler productionSimulatorHandler;

    // 생성자: Spring이 이 클래스를 만들 때, ProductionSimulatorHandler도 같이 자동으로 넣어줌
    public WebSocketConfig(ProductionSimulatorHandler productionSimulatorHandler) {
        this.productionSimulatorHandler = productionSimulatorHandler;
    }

    // WebSocket 주소(경로)를 등록하는 메서드
    // "누군가 이 경로로 접속하면, 누가 그 연결을 담당할지" 정하는 곳
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(productionSimulatorHandler, "/ws/production")
                // 프론트엔드(localhost:3000)가 다른 포트(8080)로 접속하는 거라
                // 이 허용 설정이 없으면 브라우저가 보안상 연결을 막아버림
                .setAllowedOrigins("*");
    }
}