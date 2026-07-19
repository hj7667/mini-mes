public class Main {

    public static void printLot(String workOrderNo, int quantity) {

        System.out.println("LOT 번호  : " + workOrderNo);
        System.out.println("수량 : " + quantity);

    }


/*
LOT 번호 : LOT-001
수량 : 500

LOT 번호 : LOT-002
수량 : 300

LOT 번호 : LOT-003
수량 : 150
 */

    public static void main(String[] args) {

          printLot("LOT-001", 500);
          printLot("LOT-002", 300);
          printLot("LOT-003", 150);

    }

}