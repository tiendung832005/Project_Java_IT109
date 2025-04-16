package ra.edu.business.service;

import ra.edu.business.dao.productDAO;
import ra.edu.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class productService {
    private productDAO productDAO = new productDAO();
    private Scanner scanner = new Scanner(System.in);

    //Hiển thị ds sản phẩm
    public void displayProduct() {
        List<Product> products = productDAO.getallProducts();
        System.out.println("====== QUẢN LÝ SẢN PHẨM ======");
        for (Product product : products){
            System.out.println(product);
        }
    }

    //Thêm điện thoại
    public void addNewProduct(){
        System.out.println("Nhập tên sản phẩm: ");
        String name = scanner.nextLine();

        System.out.println("Nhập thương hiệu: ");
        String brand = scanner.nextLine();

        System.out.println("Nhập giá: ");
        BigDecimal price = scanner.nextBigDecimal();

        System.out.println("Nhập số lượng tồn kho: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(price);
        product.setStock(stock);

        productDAO.addProduct(product);
    }
}
