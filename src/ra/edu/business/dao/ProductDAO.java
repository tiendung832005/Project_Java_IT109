package ra.edu.business.dao;

import ra.edu.business.config.DatabaseConfig;
import ra.edu.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Kiểm tra tên sản phẩm duy nhất
    public boolean isProductNameExists(String productName) {
        String query = "SELECT COUNT(*) FROM products WHERE name = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;  // Nếu trả về số > 0, tức là tên đã tồn tại
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra tên sản phẩm: " + e.getMessage());
        }
        return false;
    }

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

    // Lấy id điện thoại
    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try(Connection connection = DatabaseConfig.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBrand(resultSet.getString("brand"));
                product.setPrice(resultSet.getBigDecimal("price"));
                product.setStock(resultSet.getInt("stock"));
                return product;
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi lấy sản phẩm: " + e.getMessage());
        }
        return null;
    }

    // Cập nhật điện thoại
    public boolean updateProduct(Product product){
        String query = "UPDATE products SET name = ?, brand = ?, price = ?, stock = ? WHERE product_id = ?";
        try(Connection connection = DatabaseConfig.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getBrand());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setInt(4, product.getStock());
            preparedStatement.setInt(5, product.getProductId());
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        }catch (SQLException e){
            System.out.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            return false;
        }
    }

    // Xóa điện thoại
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            return false;
        }
    }

    // Tìm kiếm theo nhãn hàng
    public List<Product> searchProductsByBrand(String brandKeyword){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE brand LIKE ?";
        try(Connection connection = DatabaseConfig.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, "%" + brandKeyword + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
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
            System.out.println("Lỗi khi tìm kiếm sản phẩm theo nhãn hàng: " + e.getMessage());
        }
        return products;
    }

    // Tìm kiếm sản phẩm theo khoảng giá
    public List<Product> searchProductsByPriceRange(double minPrice, double maxPrice){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE price BETWEEN ? AND ?";
        try(Connection connection = DatabaseConfig.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setDouble(1, minPrice);
            preparedStatement.setDouble(2, maxPrice);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBrand(resultSet.getString("brand"));
                product.setPrice(resultSet.getBigDecimal("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi tìm kiếm sản phẩm theo khoảng giá: " + e.getMessage());
        }
        return products;
    }

    //Tìm kiếm sản phẩm theo tồn kho
    public List<Product> searchProductsByStockRange(int minStock, int maxStock) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE stock BETWEEN ? AND ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, minStock);
            preparedStatement.setInt(2, maxStock);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBrand(resultSet.getString("brand"));
                product.setPrice(resultSet.getBigDecimal("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm sản phẩm theo tồn kho: " + e.getMessage());
        }
        return products;
    }
}
