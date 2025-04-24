package ra.edu.business.dao;

import ra.edu.business.config.DatabaseConfig;
import ra.edu.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    //Kiểm tra sđt tồn tại
    public boolean isPhoneNumberExists(String phoneNumber){
        String query = "SELECT COUNT(*) FROM customers WHERE phone = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi kiểm tra số điện thoại: " + e.getMessage());
        }
            return false;
    }

    // Kiểm tra email đã tồn tại
    public boolean isEmailExists(String email){
        String query = "SELECT COUNT(*) FROM customers WHERE email = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi kiểm tra email: " + e.getMessage());
        }
        return false;
    }

    // Thêm khách hàng
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhone());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.executeUpdate();
            System.out.println("Thêm khách hàng thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    // Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getInt("customer_id"));
                customer.setName(resultSet.getString("name"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setEmail(resultSet.getString("email"));
                customer.setAddress(resultSet.getString("address"));
                customer.setStatus(resultSet.getString("status"));
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
        }
        return null;
    }

    // Cập nhật khách hàng
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET name = ?, phone = ?, email = ?, address = ? WHERE customer_id = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhone());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setInt(5, customer.getCustomerId());
            int rowUpdated = preparedStatement.executeUpdate();
            return rowUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra trc khi xóa xem có hóa đơn k
    public boolean hasInvoices(int customerId) {
        String query = "SELECT COUNT(*) FROM invoices WHERE customer_id = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra hóa đơn: " + e.getMessage());
        }
        return false;
    }

    // Xóa khách hàng
    public boolean deleteCustomer(int customerId) {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            int rowDeleted = preparedStatement.executeUpdate();
            return rowDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa khách hàng: " + e.getMessage());
            return false;
        }
    }

    // Lấy danh sách khách hàng
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE status = 'active'";
        try (Connection connection = DatabaseConfig.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getInt("customer_id"));
                customer.setName(resultSet.getString("name"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setEmail(resultSet.getString("email"));
                customer.setAddress(resultSet.getString("address"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return customers;
    }
}
