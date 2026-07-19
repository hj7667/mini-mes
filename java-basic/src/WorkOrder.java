public class WorkOrder {

    String workOrderNo;
    int quantity;

    // 생성자
    public WorkOrder(String workOrderNo, int quantity) {
        this.workOrderNo = workOrderNo;
        this.quantity = quantity;
    }

    // 메서드
    public void printInfo() {
        System.out.println("생산지시번호 : " + workOrderNo);
        System.out.println("생산수량 : " + quantity);
    }

}
