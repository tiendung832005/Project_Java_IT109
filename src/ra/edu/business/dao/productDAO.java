package ra.edu.business.dao;

import ra.edu.business.config.DatabaseConfig;
import ra.edu.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productDAO {
    //Hiển thị ds sản phẩm
    public List<Product> getallProducts(){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try(Connection connection = DatabaseConfig.openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){

            while(resultSet.next()){
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBrand(resultSet.getString("brand"));
                product.setPrice(resultSet.getBigDecimal("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        return products;
    }

    //Thêm điện thoại
    public void addProduct(Product product){
        String query =  "INSERT INTO products (name, brand, price, stock) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getBrand());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setInt(4, product.getStock());
            preparedStatement.executeUpdate();

            System.out.println("Thêm mới sản phẩm thành công");
        }catch (SQLException e){
            System.out.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }
}
