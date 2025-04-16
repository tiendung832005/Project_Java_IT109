package ra.edu.presentation;

import ra.edu.business.service.productService;
import ra.edu.validate.InputValidator;

public class MainMenu {
    private static productService productService = new productService();
    public static void main(String[] args) {
        showMainMenu();
    }

    static void showMainMenu() {
        while (true) {
            System.out.println("============ MENU CHÍNH ============");
            System.out.println("1. Quản lý Sản phẩm");
            System.out.println("2. Quản lý Khách hàng");
            System.out.println("3. Quản lý Hóa đơn");
            System.out.println("4. Thoát");
            System.out.println("====================================");
            System.out.print("Nhập lựa chọn của bạn: ");

            int choice = InputValidator.getValidInput(1, 4);

            switch (choice) {
                case 1:
                    showProductMenu();
                    break;
                case 2:
                    showCustomerMenu();
                    break;
                case 3:
                    showInvoiceMenu();
                    break;
                case 4:
                    System.out.println("Thoát chương trình");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static void showProductMenu() {
        while (true) {
            System.out.println("=== Quản Lý Sản Phẩm ===");
            System.out.println("1. Thêm Sản phẩm");
            System.out.println("2. Cập nhật Sản phẩm");
            System.out.println("3. Xóa Sản phẩm");
            System.out.println("4. Xem danh sách Sản phẩm");
            System.out.println("5. Tìm kiếm Sản phẩm");
            System.out.println("6. Quay về Menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");

            int choice = InputValidator.getValidInput(1, 6);

            switch (choice) {
                case 1:
                    productService.addNewProduct();
                    break;
                case 2:
                    // Logic cập nhật sản phẩm
                    break;
                case 3:
                    // Logic xóa sản phẩm
                    break;
                case 4:
                    productService.displayProduct();
                    break;
                case 5:
                    // Logic tìm kiếm sản phẩm
                    break;
                case 6:
                    return; // Quay về menu chính
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static void showCustomerMenu() {
        while (true) {
            System.out.println("=== Quản Lý Khách Hàng ===");
            System.out.println("1. Thêm Khách hàng");
            System.out.println("2. Cập nhật Khách hàng");
            System.out.println("3. Xóa Khách hàng");
            System.out.println("4. Xem danh sách Khách hàng");
            System.out.println("5. Quay về Menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");

            int choice = InputValidator.getValidInput(1, 5);

            switch (choice) {
                case 1:
                    // Logic thêm khách hàng
                    break;
                case 2:
                    // Logic cập nhật khách hàng
                    break;
                case 3:
                    // Logic xóa khách hàng
                    break;
                case 4:
                    // Logic xem danh sách khách hàng
                    break;
                case 5:
                    return; // Quay về menu chính
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static void showInvoiceMenu() {
        while (true) {
            System.out.println("=== Quản Lý Hóa Đơn ===");
            System.out.println("1. Thêm Hóa đơn");
            System.out.println("2. Xem danh sách Hóa đơn");
            System.out.println("3. Tìm kiếm Hóa đơn");
            System.out.println("4. Quay về Menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");

            int choice = InputValidator.getValidInput(1, 4);

            switch (choice) {
                case 1:
                    // Logic thêm hóa đơn
                    break;
                case 2:
                    // Logic xem danh sách hóa đơn
                    break;
                case 3:
                    // Logic tìm kiếm hóa đơn
                    break;
                case 4:
                    return; // Quay về menu chính
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
}