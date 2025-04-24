package ra.edu.validate;

import ra.edu.business.dao.CustomerDAO;
import ra.edu.business.dao.ProductDAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";

    // Kiểm tra email
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Kiểm tra sđt
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Kiểm tra ngày tháng
    public static boolean isValidDate(String date) {
        String dateRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Kiểm tra địa chỉ
    public static boolean isValidAddress(String address){
        if(address == null || address.trim().isEmpty()){
            System.out.println(RED + "Địa chỉ không được để trống." + RESET);
            return false;
        }
        if (address.length() < 5 || address.length() > 255){
            System.out.println(RED + "Địa chỉ phải có độ dài từ 5 đến 255 ký tự" + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra sđt duy nhất
    public static boolean isUniquePhoneNumber(String phoneNumber, CustomerDAO customerDAO){
        if(!isValidPhoneNumber(phoneNumber)){
            return false;
        }
        if(customerDAO.isPhoneNumberExists(phoneNumber)){
            System.out.println(RED + "Số điện thoại đã tồn tại." + RESET);
            return false;
        }
        return true;
    }

    //Kiểm tra email duy nhất
    public static boolean isUniqueEmail(String email, CustomerDAO customerDAO){
        if(!isValidEmail(email)){
            return false;
        }
        if(customerDAO.isEmailExists(email)){
            System.out.println(RED + "Email đã tồn tại." + RESET);
            return false;
        }
        return true;
    }
    // Kiểm tra tên sp là duy nhất
    public static boolean isUniqueProductName(String productName, ProductDAO productDAO){
        if(!isValidProductName(productName)){
            return false;
        }
        if(productDAO.isProductNameExists(productName)){
            System.out.println(RED + "Tên sản phẩm đã tồn tại." + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra tên sản phẩm
    public static boolean isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(RED + "Tên sản phẩm không được để trống." + RESET);
            return false;
        }
        if (name.length() < 3 || name.length() > 100) {
            System.out.println(RED + "Tên sản phẩm phải có độ dài từ 3 đến 100 ký tự." + RESET);
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", name)) {
            System.out.println(RED + "Tên sản phẩm không được chứa ký tự đặc biệt." + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra nhãn hàng
    public static boolean isValidBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            System.out.println(RED + "Nhãn hàng không được để trống." + RESET);
            return false;
        }
        if (brand.length() < 3 || brand.length() > 50) {
            System.out.println(RED + "Nhãn hàng phải có độ dài từ 3 đến 50 ký tự." + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra giá
    public static boolean isValidPrice(BigDecimal price) {
        if (price == null) {
            System.out.println(RED + "Giá không được để trống." + RESET);
            return false;
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(RED + "Giá phải là số dương." + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra số lượng tồn kho
    public static boolean isValidStock(int stock) {
        if (stock < 0) {
            System.out.println(RED + "Số lượng tồn kho phải là số nguyên dương." + RESET);
            return false;
        }
        return true;
    }

    // Kiểm tra tổng thể
    public static boolean isValidProduct(String name, String brand, BigDecimal price, int stock) {
        return isValidProductName(name) && isValidBrand(brand) && isValidPrice(price) && isValidStock(stock);
    }

    public static boolean isValidCustomerInput(String name, String phone, String email) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(RED + "Tên khách hàng không được để trống." + RESET);
            return false;
        }

        if ((phone == null || phone.trim().isEmpty()) && (email == null || email.trim().isEmpty())) {
            System.out.println(RED + "Cần nhập ít nhất một trong hai: Số điện thoại hoặc Email." + RESET);
            return false;
        }

        // Nếu có nhập thì phải đúng định dạng
        if (!phone.trim().isEmpty() && !isValidPhoneNumber(phone)) {
            System.out.println(RED + "Số điện thoại không hợp lệ." + RESET);
            return false;
        }

        if (!email.trim().isEmpty() && !isValidEmail(email)) {
            System.out.println(RED + "Email không hợp lệ." + RESET);
            return false;
        }

        return true;
    }

    // Kiểm tra xem có id khách hàng k
    public static boolean isCustomerExists(Connection connection, int customerId) throws SQLException {
        String query = "SELECT 1 FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean exists = rs.next();
                if (!exists) {
                    System.out.println(RED + "Lỗi: Không tồn tại khách hàng với ID = " + customerId  + RESET);
                }
                return exists;
            }
        }
    }

    // Kiểm tra xem có id sản phẩm k
    public static boolean isProductExists(Connection connection, int productId) throws SQLException {
        String query = "SELECT 1 FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean exists = rs.next();
                if (!exists) {
                    System.out.println(RED + "Lỗi: Không tồn tại sản phẩm với ID = " + productId);
                }
                return exists;
            }
        }
    }
}
