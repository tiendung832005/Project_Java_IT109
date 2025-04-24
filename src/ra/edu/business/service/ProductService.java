package ra.edu.business.service;

import ra.edu.business.dao.ProductDAO;
import ra.edu.entity.Product;
import ra.edu.validate.InputValidator;
import ra.edu.validate.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private Scanner scanner = new Scanner(System.in);
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";

    // ds tất cả sản phẩm
    public void displayAllProducts() {
        List<Product> products = productDAO.getallProducts();
        displayProducts(products);
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
            System.out.println(RED + "Dữ liệu sản phẩm không hợp lệ. Vui lòng kiểm tra lại." + RESET);
        }
    }

    //cập nhật dt
    public void updateProduct() {
        int productId = InputValidator.getPositiveInt("Nhập ID sản phẩm cần cập nhật: ");

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            System.out.println(RED + "Không tìm thấy sản phẩm với ID: " + productId + RESET);
            return;
        }

        // Hiển thị thông tin sản phẩm hiện tại theo bảng
        System.out.println("+------------------------------------------------------------------------------------+");
        System.out.println("|                             ====== Thông Tin Sản Phẩm ======                      |");
        System.out.println("+------------+-------------------------+------------------+------------+------------+");
        System.out.printf("| %-10s | %-23s | %-16s | %-10s | %-10s |\n", "ID", "Tên Sản Phẩm", "Nhãn Hiệu", "Giá", "Tồn Kho");
        System.out.println("+------------+-------------------------+------------------+------------+------------+");
        System.out.printf("| %-10d | %-23s | %-16s | %-10.2f | %-10d |\n",
                product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getStock());
        System.out.println("+------------+-------------------------+------------------+------------+------------+");

        // Nhập từng trường hoặc giữ nguyên nếu bỏ trống
        System.out.print("Nhập tên sản phẩm mới (Enter để giữ nguyên): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            if (!name.equals(product.getName()) && productDAO.isProductNameExists(name)) {
                System.out.println(RED + "Tên sản phẩm này đã tồn tại. Vui lòng chọn tên khác." + RESET);
                return;
            }
            product.setName(name);
        }

        System.out.print("Nhập nhãn hiệu mới (Enter để giữ nguyên): ");
        String brand = scanner.nextLine();
        if (!brand.trim().isEmpty()) {
            product.setBrand(brand);
        }

        System.out.print("Nhập giá mới (Enter để giữ nguyên): ");
        String priceInput = scanner.nextLine();
        if (!priceInput.trim().isEmpty()) {
            try {
                BigDecimal price = new BigDecimal(priceInput);
                product.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Giá không hợp lệ. Vui lòng thử lại." + RESET);
                return;
            }
        }

        System.out.print("Nhập số lượng tồn kho mới (Enter để giữ nguyên): ");
        String stockInput = scanner.nextLine();
        if (!stockInput.trim().isEmpty()) {
            try {
                int stock = Integer.parseInt(stockInput);
                product.setStock(stock);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Số lượng tồn kho không hợp lệ. Vui lòng thử lại." + RESET);
                return;
            }
        }

        // Validate dữ liệu sau cùng
        if (Validator.isValidProduct(product.getName(), product.getBrand(), product.getPrice(), product.getStock())) {
            if (productDAO.updateProduct(product)) {
                System.out.println(GREEN + "Cập nhật sản phẩm thành công" + RESET);
            } else {
                System.out.println(RED + "Cập nhật sản phẩm thất bại" + RESET);
            }
        } else {
            System.out.println(RED + "Dữ liệu sản phẩm không hợp lệ. Vui lòng kiểm tra lại." + RESET);
        }
    }


    // Xóa sản phẩm
    public void deleteProduct() {
        int productId = InputValidator.getPositiveInt("Nhập ID sản phẩm cần xóa: ");

        if (productDAO.getProductById(productId) == null) {
            System.out.println(RED + "Không tìm thấy sản phẩm với ID: " + productId);
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa sản phẩm này không? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            if (productDAO.deleteProduct(productId)) {
                System.out.println(GREEN + "Xóa sản phẩm thành công" + RESET);
            } else {
                System.out.println(RED + "Xóa sản phẩm thất bại" + RESET);
            }
        } else {
            System.out.println("Hủy xóa sản phẩm");
        }
    }

    //Tìm kiếm theo tên
    public void searchProductsByName() {
        System.out.print("Nhập tên sản phẩm: ");
        String brandKeyword = scanner.nextLine();
        List<Product> products = productDAO.searchProductsByName(brandKeyword);
        displayProducts(products);
    }

    // Tìm kiếm sản phẩm theo khoảng giá
    public void searchProductsByPriceRange() {
        double minPrice = InputValidator.getValidDouble("Nhập giá tối thiểu: ");
        double maxPrice = InputValidator.getValidDouble("Nhập giá tối đa: ");

        List<Product> products = productDAO.searchProductsByPriceRange(minPrice, maxPrice);
        displayProducts(products);
    }

    // Tìm kiếm sản phẩm theo tồn kho
    public void searchProductsByStockRange() {
        int minStock = InputValidator.getPositiveInt("Nhập số lượng tồn kho tối thiểu: ");
        int maxStock = InputValidator.getPositiveInt("Nhập số lượng tồn kho tối đa: ");

        List<Product> products = productDAO.searchProductsByStockRange(minStock, maxStock);
        displayProducts(products);
    }

    // Hiển thị danh sách sản phẩm
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(RED + "Không tìm thấy sản phẩm nào." + RESET);
        } else {
            // Tiêu đề bảng
            System.out.println("+----------------------------------------------------------------------------+");
            System.out.println("|                        ====== Danh Sách Sản Phẩm ======                    |");
            System.out.println("+------------+--------------------+----------------+------------+------------+");
            System.out.printf("| %-10s | %-18s | %-14s | %-10s | %-10s |\n", "ID", "Tên Sản Phẩm", "Nhãn Hàng", "Giá", "Tồn Kho");
            System.out.println("+------------+--------------------+----------------+------------+------------+");

            // Hiển thị thông tin sản phẩm
            for (Product product : products) {
                BigDecimal price = product.getPrice();
                // Kiểm tra xem giá có phần thập phân không
                if (price.stripTrailingZeros().scale() <= 0) {
                    System.out.printf("| %-10d | %-18s | %-14s | %-10d | %-10d |\n",
                            product.getProductId(),
                            product.getName(),
                            product.getBrand(),
                            price.intValue(),
                            product.getStock());
                } else {
                    System.out.printf("| %-10d | %-18s | %-14s | %-10.2f | %-10d |\n",
                            product.getProductId(),
                            product.getName(),
                            product.getBrand(),
                            product.getPrice(),
                            product.getStock());
                }
            }

            System.out.println("+------------+--------------------+----------------+------------+------------+");
        }
    }


}
