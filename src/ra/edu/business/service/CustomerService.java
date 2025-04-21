package ra.edu.business.service;

import ra.edu.business.dao.CustomerDAO;
import ra.edu.entity.Customer;
import ra.edu.validate.Validator;

import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private Scanner scanner = new Scanner(System.in);

    // Thêm mới khách hàng
    public void addNewCustomer() {
        System.out.print("Nhập tên khách hàng: ");
        String name = scanner.nextLine();

        System.out.print("Nhập số điện thoại: ");
        String phone = scanner.nextLine();

        System.out.print("Nhập email: ");
        String email = scanner.nextLine();

        System.out.print("Nhập địa chỉ: ");
        String address = scanner.nextLine();

        if (Validator.isUniquePhoneNumber(phone, customerDAO) &&
                Validator.isUniqueEmail(email, customerDAO) &&
                Validator.isValidAddress(address)) {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            customerDAO.addCustomer(customer);
        } else {
            System.out.println("Dữ liệu khách hàng không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }


    // Cập nhật khách hàng
    public void updateCustomer() {
        System.out.print("Nhập ID khách hàng cần cập nhật: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Đọc bỏ dòng mới

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            return;
        }

        // Hiển thị thông tin khách hàng hiện tại
        System.out.println("Thông tin khách hàng hiện tại: ");
        List<Customer> customerList = List.of(customer);
        displayAllCustomers(customerList);

        System.out.print("Nhập tên khách hàng mới: ");
        String name = scanner.nextLine();

        System.out.print("Nhập số điện thoại mới: ");
        String phone = scanner.nextLine();

        System.out.print("Nhập email mới: ");
        String email = scanner.nextLine();

        System.out.print("Nhập địa chỉ mới: ");
        String address = scanner.nextLine();

        boolean isValid = true;

        if (!phone.equals(customer.getPhone())) {
            isValid = Validator.isUniquePhoneNumber(phone, customerDAO);
        }

        if (!email.equals(customer.getEmail())) {
            isValid = isValid && Validator.isUniqueEmail(email, customerDAO);
        }

        if (!Validator.isValidAddress(address)) {
            isValid = false;
        }

        if (isValid) {
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);

            if (customerDAO.updateCustomer(customer)) {
                System.out.println("Cập nhật khách hàng thành công!");
            } else {
                System.out.println("Cập nhật khách hàng thất bại.");
            }
        } else {
            System.out.println("Dữ liệu khách hàng không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }

    // Xóa khách hàng
    public void deleteCustomer() {
        System.out.print("Nhập ID khách hàng cần xóa: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        if (customerDAO.getCustomerById(customerId) == null) {
            System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa khách hàng này không? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            if (customerDAO.deleteCustomer(customerId)) {
                System.out.println("Xóa khách hàng thành công!");
            } else {
                System.out.println("Xóa khách hàng thất bại.");
            }
        } else {
            System.out.println("Hủy xóa khách hàng.");
        }
    }

    // Hiển thị danh sách khách hàng
    public void displayAllCustomers(List<Customer> customerList) {
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("Không có khách hàng nào trong danh sách.");
        } else {
            System.out.println("+------------------------------------------------------------+");
            System.out.println("|                      ====== Danh Sách Khách Hàng ======   |");
            System.out.println("+------------+---------------------+---------------------+---------------------+");
            System.out.printf("| %-10s | %-20s | %-25s | %-20s |\n", "ID", "Tên Khách Hàng", "Email", "Địa Chỉ");
            System.out.println("+------------+---------------------+---------------------+---------------------+");

            // Hiển thị thông tin khách hàng
            for (Customer customer : customers) {
                System.out.printf("| %-10d | %-20s | %-25s | %-20s |\n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getAddress());
            }

            System.out.println("+------------+---------------------+---------------------+---------------------+");
        }
    }

}
