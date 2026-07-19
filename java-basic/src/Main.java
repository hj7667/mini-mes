

public class Main {

    public static void main(String[] args) {

        WorkOrder order1 = new WorkOrder();

        order1.workOrderNo = "WO-001";
        order1.quantity = 500;

        System.out.println(order1.workOrderNo);
        System.out.println(order1.quantity);
        System.out.println(order1.completed);

        WorkOrder order2 = new WorkOrder();

        order1.workOrderNo = "WO-002";
        order1.quantity = 300;

    }
}