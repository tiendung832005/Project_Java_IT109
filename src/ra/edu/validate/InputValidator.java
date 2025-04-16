package ra.edu.validate;

import java.util.Scanner;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    public static int getValidInput(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số từ " + min + " đến " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số hợp lệ.");
            }
        }
        return choice;
    }
}
