# 🏭 FactoryFlow 
> 진행중 20260720 1차
> 산업용 모터 제조 공정을 시뮬레이션하는 MES(제조실행시스템)
> 단순 CRUD가 아닌, **공정 데이터 시뮬레이션**과 **LOT 추적성(Traceability)** 구현에 집중한 프로젝트입니다.

<!-- 데모 GIF/영상 삽입 위치 -->
<!-- ![demo](./docs/demo.gif) -->
<p align="center">
  <img src="./docs/demo.gif" width="800" alt="FactoryFlow 데모" />
</p>

<p align="center">
  <a href="#-데모-바로가기">데모 바로가기</a> ·
  <a href="#-핵심-기능">핵심 기능</a> ·
  <a href="#-기술-스택">기술 스택</a> ·
  <a href="#-아키텍처">아키텍처</a>
</p>

---

## 📌 프로젝트 소개

**FactoryFlow**는 산업용 모터 조립 공정(`생산지시 → 권선 → 조립 → 성능검사 → 포장/출하`)을 가상으로 재현한 MES입니다.

핀테크·이커머스 도메인에서 흔히 쓰이는 CRUD 위주의 포트폴리오와 다르게, **제조업 특유의 데이터 정합성 문제**를 실제로 풀어보는 데 목적을 뒀습니다.

- 불량 발생 시 LOT을 실시간으로 분할하고, 트랜잭션 하나로 재고까지 자동 반영
- 특정 LOT 번호 하나로 전체 생산 이력을 역추적하는 Time Machine 기능
- WebSocket으로 설비 가동 상황을 실시간 시뮬레이션

## 🎬 데모 바로가기

| 화면 | 설명 | 링크 |
|---|---|---|
| 작업자 키오스크 | 현장 실적 등록 + 설비 시뮬레이터 | [바로가기](#) |
| 관리자 대시보드 | 공정별 가동 현황 요약 | [바로가기](#) |
| LOT 역추적 | Time Machine 타임라인 뷰어 | [바로가기](#) |

## ⭐ 핵심 기능

### 1. 품질검사 & LOT Split (단일 트랜잭션)

성능검사에서 불량이 나오면, 기존 LOT에서 불량 수량을 차감하고 `-ERR` 접미사가 붙은 불량 전용 LOT을 자동 생성합니다. 양품은 완제품 창고로, 불량품은 격리 창고로 동시에 입고 처리되며, 이 모든 과정은 하나의 트랜잭션으로 묶여 있어 중간에 실패하면 전체가 롤백됩니다.

### 2. FIFO 출하 자동화

출하 요청이 들어오면 동일 제품군 중 생성일자가 가장 빠른 LOT부터 순차적으로 차감합니다. 한 LOT으로 부족하면 다음 LOT까지 자동으로 이어서 계산합니다.

### 3. LOT 역추적 (Time Machine)

LOT 번호 하나로 권선 → 조립 → 성능검사 → 포장 각 공정의 통과 시각과 담당 작업자 이력을 타임라인으로 조회할 수 있습니다.

### 4. 실시간 생산 시뮬레이션

작업자가 [생산 시작] 버튼을 누르면 WebSocket을 통해 5초마다 생산량이 1씩 증가하고, 설비 RPM/온도 데이터가 랜덤하게 변동하며 실제 현장처럼 표출됩니다.

## 🛠 기술 스택

**Frontend**
| 구분 | 기술 |
|---|---|
| Framework | Next.js (App Router) |
| Styling | Tailwind CSS, shadcn/ui |
| 상태관리 | React Query |
| 아이콘 | Lucide React |

**Backend**
| 구분 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| ORM | Spring Data JPA (Hibernate) |
| 검증 | Spring Validation |
| 실시간 통신 | WebSocket |
| 기타 | Lombok |

**Database**
| 구분 | 기술 |
|---|---|
| DBMS | PostgreSQL |

**Dev Tools**
Gradle · Git/GitHub · VS Code · Postman

## 🗂 아키텍처
com.factoryflow
│
├── FactoryflowApplication.java      → 메인 실행 클래스 (@SpringBootApplication)
│
├── workorder/                      [생산지시 도메인]
│   ├── WorkOrder.java              → 생산지시 엔티티. 목표수량/생산수량/불량수량/상태를 갖는 DB 테이블 매핑 클래스
│   ├── WorkOrderStatus.java        → READY/RUNNING/COMPLETED 상태값 enum
│   ├── WorkOrderRepository.java    → DB 접근 담당. JPA가 CRUD 메서드 자동 생성해줌
│   ├── WorkOrderDto.java           → 요청/응답 DTO (CreateRequest, UpdateRequest, Response record)
│   ├── WorkOrderService.java       → 실제 비즈니스 로직 (등록/수정/상태전환/진행률 계산 등)
│   └── WorkOrderController.java    → 프론트가 호출할 REST API 엔드포인트 (/api/work-orders)
│
├── lot/                            [LOT 관리 도메인]
│   ├── Lot.java                    → 엔티티
│   ├── ProcessStage.java           → enum (WINDING/ASSEMBLY/INSPECTION/PACKAGING/SHIPPED)
│   ├── LotStatus.java              → enum (AVAILABLE/DEFECT/SHIPPED)
│   ├── LotRepository.java          → DB 접근
│   ├── LotDto.java                 → 요청/응답 DTO
│   ├── LotService.java             → 비즈니스 로직
│   └── LotController.java          → REST API
│
├── inventory/                      [재고 도메인]
│   ├── Inventory.java              → 재고 엔티티. 어느 LOT이 어느 창고에 얼마나 있는지 기록
│   ├── WarehouseType.java          → FINISHED_GOODS/QUARANTINE 창고 종류 enum
│   ├── InventoryRepository.java    → 재고 DB 접근 담당 (FIFO용 커스텀 쿼리 포함)
│   ├── InventoryDto.java           → 응답 DTO (Response record만 있음, CreateRequest 없음)
│   ├── InventoryService.java       → 조회 + stockIn() 헬퍼 (Quality 서비스가 내부적으로 호출)
│   └── InventoryController.java    → REST API (GET만 있음)
│
├── quality/                        [품질검사 도메인 — 킬러 기능 ① ✅ 테스트 완료]
│   ├── DefectReason.java           → 불량 사유 enum (절연저항 불량, 베어링 소음 등)
│   ├── QualityDto.java             → InspectRequest, Response record (LotSplitResult 역할 통합)
│   ├── QualityInspectionService.java → 양품/불량 판정 + LOT Split + 재고 자동 분리를 트랜잭션으로 처리
│   └── QualityController.java      → REST API (/api/quality/inspect)
│
├── shipment/                       [출하 도메인 — 킬러 기능 ② ✅ 테스트 완료]
│   ├── ShipmentDto.java            → Request, LotDeduction, Response record (ShipmentResult 역할 통합)
│   ├── FifoShipmentService.java    → 선입선출로 여러 LOT에 걸쳐 출하 처리
│   └── ShipmentController.java     → REST API (/api/shipments)
│
├── websocket/                      [실시간 시뮬레이션 — ⛔ 미완성, 빈 파일]
│   ├── ProductionSimulatorHandler.java
│   └── WebSocketConfig.java
│
├── dashboard/                      [대시보드 집계 — ⛔ 미완성, 빈 파일]
│   ├── DashboardService.java
│   └── DashboardController.java
│
└── common/                         [공통 모듈 ✅ 테스트 완료]
    ├── exception/
    │   ├── GlobalExceptionHandler.java     → 모든 컨트롤러 예외를 한 곳에서 처리 (@RestControllerAdvice)
    │   ├── EntityNotFoundException.java    → 커스텀 예외 (LOT/WorkOrder 못 찾았을 때) → 404
    │   ├── InsufficientStockException.java → 재고 부족 예외 → 422
    │   ├── InvalidRequestException.java    → 잘못된 요청 예외 → 400
    │   └── ErrorResponse.java              → 에러 응답 통일 포맷 (record)
    └── util/
        └── LotNumberGenerator.java → LOT 번호 생성 규칙(LOT-YYYYMMDD-M01-001) 담당 유틸

## 📋 핵심 비즈니스 로직

<details>
<summary>품질검사 트랜잭션 코드 보기</summary>

```java
@Transactional
public LotSplitResult inspect(Long lotId, int defectQty, String defectReason) {
    // 1. 기존 LOT 수량 차감 (양품만 남김)
    // 2. 불량 발생 시 신규 LOT 생성 (-ERR)
    // 3. 불량 재고 → 격리창고 입고
    // 4. 양품 재고 → 완제품 창고 입고
    // 5. WorkOrder 불량 수량 누적
    // → 중간 실패 시 전체 롤백
}
```

</details>

## 🚀 실행 방법

### Backend

```bash
cd backend
./gradlew bootRun
```

`src/main/resources/application.properties`에 PostgreSQL 연결 정보 설정 필요:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/factoryflow
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```



## 📅 개발 히스토리

| 일차 | 작업 내용 |
|---|---|
| 1~2일 차 | DB 스키마 정의, WorkOrder/LOT/Inventory 트랜잭션 API 구현 |
| 3~4일 차 | 작업자 터치 UI, WebSocket 기반 생산 시뮬레이터 |
| 5~6일 차 | 관리자 대시보드, LOT 역추적(Time Machine) 타임라인 |
| 7일 차 | 배포 및 데모 영상 촬영 |

## 📝 트러블슈팅

<!-- 개발하면서 겪은 문제 + 해결 과정을 여기에 기록하세요.
     예: "LOT Split 시 재고 반영이 늦게 되는 문제 → @Transactional 범위 재조정으로 해결" -->

## 👤 개발자


- 이메일: [chohj.biz@gmail.com]