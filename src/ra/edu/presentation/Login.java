package ra.edu.presentation;

import ra.edu.business.dao.UserDAO;

import java.util.Scanner;

public class Login {
    private static UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        showMenu();
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("======== HỆ THỐNG QUẢN LÝ CỬA HÀNG ========");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Thoát");
            System.out.println("===========================================");
            System.out.print("Nhập lựa chọn: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (authenticate()) {
                        MainMenu.showMainMenu();
                    }
                    break;
                case 2:
                    System.out.println("Thoát chương trình. Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static boolean authenticate() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Nhập tên đăng nhập: ");
            String username = scanner.nextLine();

            System.out.print("Nhập mật khẩu: ");
            String password = scanner.nextLine();

            if (userDAO.validateLogin(username, password)) {
                System.out.println("Đăng nhập thành công!");
                return true;
            } else {
                System.out.println("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
            }
        }
    }
}