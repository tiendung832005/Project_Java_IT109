package ra.edu.business.service;

import ra.edu.business.dao.productDAO;
import ra.edu.entity.Product;
import ra.edu.validate.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class productService {
    private productDAO productDAO = new productDAO();
    private Scanner scanner = new Scanner(System.in);

    // ds tất cả sản phẩm
    public void displayAllProducts() {
        List<Product> products = productDAO.getallProducts();
        if (products.isEmpty()) {
            System.out.println("Không có sản phẩm nào trong danh sách.");
        } else {
            System.out.println("                ====== Danh Sách Sản Phẩm ======");
            System.out.printf("%-10s %-20s %-15s %-10s %-10s%n", "ID", "Tên Sản Phẩm", "Nhãn Hàng", "Giá", "Tồn Kho");
            System.out.println("-------------------------------------------------------------------");
            for (Product product : products) {
                System.out.printf("%-10d %-20s %-15s %-10.2f %-10d%n",
                        product.getProductId(),
                        product.getName(),
                        product.getBrand(),
                        product.getPrice(),
                        product.getStock());
            }
        }
    }

    //Thêm điện thoại
    public void addNewProduct() {
        System.out.print("Nhập tên sản phẩm: ");
        String name = scanner.nextLine();

        System.out.print("Nhập nhãn hiệu: ");
        String brand = scanner.nextLine();

        System.out.print("Nhập giá: ");
        BigDecimal price = scanner.nextBigDecimal();

        System.out.print("Nhập số lượng tồn kho: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        if (Validator.isValidProduct(name, brand, price, stock)) {
            Product product = new Product();
            product.setName(name);
            product.setBrand(brand);
            product.setPrice(price);
            product.setStock(stock);

            productDAO.addProduct(product);
        } else {
            System.out.println("Dữ liệu sản phẩm không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }

    // cập nhật sản phẩm
    public void updateProduct() {
        System.out.print("Nhập ID sản phẩm cần cập nhật: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
            return;
        }

        System.out.println("Thông tin sản phẩm hiện tại: " + product);

        System.out.print("Nhập tên sản phẩm mới: ");
        String name = scanner.nextLine();

        System.out.print("Nhập nhãn hiệu mới: ");
        String brand = scanner.nextLine();

        System.out.print("Nhập giá mới: ");
        BigDecimal price = scanner.nextBigDecimal();

        System.out.print("Nhập số lượng tồn kho mới: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        // validate
        if (Validator.isValidProduct(name, brand, price, stock)) {
            product.setName(name);
            product.setBrand(brand);
            product.setPrice(price);
            product.setStock(stock);

            if (productDAO.updateProduct(product)) {
                System.out.println("Cập nhật sản phẩm thành công");
            } else {
                System.out.println("Cập nhật sản phẩm thất bại");
            }
        } else {
            System.out.println("Dữ liệu sản phẩm không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }

    // Xóa sản phẩm
    public void deleteProduct() {
        System.out.print("Nhập ID sản phẩm cần xóa: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        if (productDAO.getProductById(productId) == null) {
            System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa sản phẩm này không? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            if (productDAO.deleteProduct(productId)) {
                System.out.println("Xóa sản phẩm thành công");
            } else {
                System.out.println("Xóa sản phẩm thất bại");
            }
        } else {
            System.out.println("Hủy xóa sản phẩm");
        }
    }

    //Tìm kiếm theo nhanx hàng
    public void searchProductsByBrand() {
        System.out.print("Nhập từ khóa nhãn hàng: ");
        String brandKeyword = scanner.nextLine();
        List<Product> products = productDAO.searchProductsByBrand(brandKeyword);
        displayProducts(products);
    }

    // Tìm kiếm sản phẩm theo khoảng giá
    public void searchProductsByPriceRange() {
        System.out.print("Nhập giá tối thiểu: ");
        double minPrice = scanner.nextDouble();
        System.out.print("Nhập giá tối đa: ");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();
        List<Product> products = productDAO.searchProductsByPriceRange(minPrice, maxPrice);
        displayProducts(products);
    }

    // Tìm kiếm sản phẩm theo tồn kho
    public void searchProductsByStockRange() {
        System.out.print("Nhập số lượng tồn kho tối thiểu: ");
        int minStock = scanner.nextInt();
        System.out.print("Nhập số lượng tồn kho tối đa: ");
        int maxStock = scanner.nextInt();
        scanner.nextLine();
        List<Product> products = productDAO.searchProductsByStockRange(minStock, maxStock);
        displayProducts(products);
    }

    // Hiển thị danh sách sản phẩm
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào.");
        } else {
            System.out.println("=== Danh Sách Sản Phẩm ===");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }


}
