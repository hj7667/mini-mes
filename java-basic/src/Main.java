import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<WorkOrder> orders = new ArrayList<>();

        orders.add(new WorkOrder("WO-001", 500));
        orders.add(new WorkOrder("WO-002", 300));
        orders.add(new WorkOrder("WO-003", 1000));

        for (WorkOrder order : orders) {
            order.printInfo();
            System.out.println();
        }
    }

}