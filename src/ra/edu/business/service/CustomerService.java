package ra.edu.business.service;

import ra.edu.business.dao.CustomerDAO;
import ra.edu.entity.Customer;
import ra.edu.validate.InputValidator;
import ra.edu.validate.Validator;

import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private Scanner scanner = new Scanner(System.in);
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";

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

        boolean isValidInput = Validator.isValidCustomerInput(name, phone, email);
        boolean isUniquePhone = phone.trim().isEmpty() || Validator.isUniquePhoneNumber(phone, customerDAO);
        boolean isUniqueEmail = email.trim().isEmpty() || Validator.isUniqueEmail(email, customerDAO);
        boolean isValidAddress = Validator.isValidAddress(address);

        if (isValidInput && isUniquePhone && isUniqueEmail && isValidAddress) {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            customerDAO.addCustomer(customer);
            System.out.println(GREEN + "Thêm khách hàng thành công!" + RESET);
        } else {
            System.out.println(RED + "Dữ liệu khách hàng không hợp lệ. Vui lòng kiểm tra lại." + RESET);
        }
    }

    // Cập nhật khách hàng
    public void updateCustomer() {
        int customerId = InputValidator.getPositiveInt("Nhập ID khách hàng cần cập nhật: ");

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println(RED + "Không tìm thấy khách hàng với ID: " + customerId  + RESET);
            return;
        }

        // Hiển thị thông tin khách hàng hiện tại
        System.out.println("+------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                 ====== Thông Tin Khách Hàng ======                                   |");
        System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");
        System.out.printf("| %-10s | %-20s | %-15s | %-25s | %-18s |\n", "ID", "Tên Khách Hàng", "Số điện thoại", "Email", "Địa Chỉ");
        System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");
        System.out.printf("| %-10d | %-20s | %-15s | %-25s | %-18s |\n",
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress());
        System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");


        System.out.print("Nhập tên khách hàng mới (Enter để giữ nguyên): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            customer.setName(name);
        }

        System.out.print("Nhập số điện thoại mới (Enter để giữ nguyên): ");
        String phone = scanner.nextLine();
        if (!phone.trim().isEmpty()) {
            if (!phone.equals(customer.getPhone()) && !Validator.isUniquePhoneNumber(phone, customerDAO)) {
                System.out.println(RED + "Số điện thoại đã tồn tại. Cập nhật thất bại." + RESET);
                return;
            }
            customer.setPhone(phone);
        }

        System.out.print("Nhập email mới (Enter để giữ nguyên): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) {
            if (!email.equals(customer.getEmail()) && !Validator.isUniqueEmail(email, customerDAO)) {
                System.out.println(RED + "Email đã tồn tại. Cập nhật thất bại." + RESET);
                return;
            }
            customer.setEmail(email);
        }

        System.out.print("Nhập địa chỉ mới (Enter để giữ nguyên): ");
        String address = scanner.nextLine();
        if (!address.trim().isEmpty()) {
            customer.setAddress(address);
        }

        // Kiểm tra dữ liệu trước khi cập nhật
        boolean isValidInput = Validator.isValidCustomerInput(customer.getName(), customer.getPhone(), customer.getEmail());
        boolean isValidAddress = Validator.isValidAddress(customer.getAddress());

        if (isValidInput && isValidAddress) {
            if (customerDAO.updateCustomer(customer)) {
                System.out.println(GREEN + "Cập nhật khách hàng thành công!" + RESET);
            } else {
                System.out.println(RED+ "Cập nhật khách hàng thất bại." + RESET);
            }
        } else {
            System.out.println(RED + "Dữ liệu khách hàng không hợp lệ. Vui lòng kiểm tra lại." + RESET);
        }
    }

    // Xóa khách hàng
    public void deleteCustomer() {
        int customerId = InputValidator.getPositiveInt("Nhập ID khách hàng cần xóa: ");

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null || "inactive".equalsIgnoreCase(customer.getStatus())) {
            System.out.println(RED + "Không tìm thấy khách hàng hoạt động với ID: " + customerId + RESET);
            return;
        }

        // Kiểm tra có hóa đơn hay không
        if (customerDAO.hasInvoices(customerId)) {
            System.out.println(RED + "Không thể xóa khách hàng vì đã có hóa đơn liên quan." + RESET);
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa khách hàng này không? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            if (customerDAO.deleteCustomer(customerId)) {
                System.out.println(GREEN + "Khách hàng đã được xóa!" + RESET);
            } else {
                System.out.println(RED + "Xóa khách hàng thất bại." + RESET);
            }
        } else {
            System.out.println("Đã hủy thao tác.");
        }
    }


    // Hiển thị danh sách khách hàng
    public void displayAllCustomers(List<Customer> customerList) {
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println(RED + "Không có khách hàng nào trong danh sách." + RESET);
        } else {
            System.out.println("+------------------------------------------------------------------------------------------------------+");
            System.out.println("|                                   ====== Danh Sách Khách Hàng ======                                 |");
            System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");
            System.out.printf("| %-10s | %-20s | %-15s | %-25s | %-18s |\n", "ID", "Tên Khách Hàng", "Số điện thoại", "Email", "Địa Chỉ");
            System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");

            for (Customer customer : customers) {
                System.out.printf("| %-10d | %-20s | %-15s | %-25s | %-18s |\n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPhone(),
                        customer.getEmail(),
                        customer.getAddress());
            }

            System.out.println("+------------+---------------------+---------------------+---------------------------------------------+");
        }
    }
}
