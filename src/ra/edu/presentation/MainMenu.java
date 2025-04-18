package ra.edu.presentation;

import ra.edu.business.service.productService;
import ra.edu.validate.InputValidator;

public class MainMenu {
    private static productService productService = new productService();

    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";

    public static void main(String[] args) {
        showMainMenu();
    }

    static void showMainMenu() {
        while (true) {
            System.out.println(GREEN + "============ MENU CHÍNH ============" + RESET);
            System.out.println(BLUE + "1. Quản lý Sản phẩm" + RESET);
            System.out.println(BLUE + "2. Quản lý Khách hàng" + RESET);
            System.out.println(BLUE + "3. Quản lý Hóa đơn" + RESET);
            System.out.println(BLUE + "4. Thoát" + RESET);
            System.out.println(GREEN + "====================================" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

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
                    System.out.println(RED + "Thoát chương trình" + RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }

    private static void showProductMenu() {
        while (true) {
            System.out.println(GREEN + "=== Quản Lý Sản Phẩm ===" + RESET);
            System.out.println(BLUE + "1. Thêm Sản phẩm" + RESET);
            System.out.println(BLUE + "2. Cập nhật Sản phẩm" + RESET);
            System.out.println(BLUE + "3. Xóa Sản phẩm" + RESET);
            System.out.println(BLUE + "4. Xem danh sách Sản phẩm" + RESET);
            System.out.println(BLUE + "5. Tìm kiếm Sản phẩm" + RESET);
            System.out.println(BLUE + "6. Quay về Menu chính" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

            int choice = InputValidator.getValidInput(1, 6);

            switch (choice) {
                case 1:
                    productService.addNewProduct();
                    break;
                case 2:
                    productService.updateProduct();
                    break;
                case 3:
                    productService.deleteProduct();
                    break;
                case 4:
                    productService.displayAllProducts();
                    break;
                case 5:
                    showSearchMenu();
                    break;
                case 6:
                    return;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }

    private static void showSearchMenu() {
        while (true) {
            System.out.println(GREEN + "=== Tìm kiếm Sản phẩm ===" + RESET);
            System.out.println(BLUE + "1. Tìm kiếm theo tên nhãn hàng" + RESET);
            System.out.println(BLUE + "2. Tìm kiếm theo giá" + RESET);
            System.out.println(BLUE + "3. Tìm kiếm theo tồn kho" + RESET);
            System.out.println(BLUE + "4. Quay về Menu sản phẩm" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

            int choice = InputValidator.getValidInput(1, 4);

            switch (choice) {
                case 1:
                    productService.searchProductsByBrand();
                    break;
                case 2:
                    productService.searchProductsByPriceRange();
                    break;
                case 3:
                    productService.searchProductsByStockRange();
                    break;
                case 4:
                    return;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }

    private static void showCustomerMenu() {
        while (true) {
            System.out.println(GREEN + "=== Quản Lý Khách Hàng ===" + RESET);
            System.out.println(BLUE + "1. Thêm Khách hàng" + RESET);
            System.out.println(BLUE + "2. Cập nhật Khách hàng" + RESET);
            System.out.println(BLUE + "3. Xóa Khách hàng" + RESET);
            System.out.println(BLUE + "4. Xem danh sách Khách hàng" + RESET);
            System.out.println(BLUE + "5. Quay về Menu chính" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

            int choice = InputValidator.getValidInput(1, 5);

            switch (choice) {
                case 1:
                    // Thêm khách hàng
                    break;
                case 2:
                    // Cập nhật khách hàng
                    break;
                case 3:
                    // Xóa khách hàng
                    break;
                case 4:
                    // Xem danh sách khách hàng
                    break;
                case 5:
                    return;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }

    private static void showInvoiceMenu() {
        while (true) {
            System.out.println(GREEN + "=== Quản Lý Hóa Đơn ===" + RESET);
            System.out.println(BLUE + "1. Thêm Hóa đơn" + RESET);
            System.out.println(BLUE + "2. Xem danh sách Hóa đơn" + RESET);
            System.out.println(BLUE + "3. Tìm kiếm Hóa đơn" + RESET);
            System.out.println(BLUE + "4. Quay về Menu chính" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

            int choice = InputValidator.getValidInput(1, 4);

            switch (choice) {
                case 1:
                    // Thêm hóa đơn
                    break;
                case 2:
                    // Xem danh sách hóa đơn
                    break;
                case 3:
                    // Tìm kiếm hóa đơn
                    break;
                case 4:
                    return;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }
}
