package ra.edu.business.service;

import ra.edu.business.dao.InvoiceDAO;
import ra.edu.business.dao.ProductDAO;
import ra.edu.entity.Invoice;
import ra.edu.entity.InvoiceItem;
import ra.edu.validate.InputValidator;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class InvoiceService {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private ProductDAO productDAO = new ProductDAO();
    private Scanner scanner = new Scanner(System.in);

    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";

    // Hiển thị danh sách hóa đơn chi tiết
    public void displayAllInvoices() {
        List<Invoice> invoices = invoiceDAO.getAllInvoicesWithItems();
        if (invoices.isEmpty()) {
            System.out.println(RED + "Không có hóa đơn nào trong danh sách." + RESET);
        } else {
            System.out.println("+================================================================+");
            System.out.println("|                ====== Danh Sách Hóa Đơn Chi Tiết ======        |");
            System.out.println("+================================================================+");

            for (Invoice invoice : invoices) {
                System.out.printf("| Hóa đơn ID: %-50d |\n", invoice.getInvoiceId());
                System.out.println("+------------+---------------------+-----------------------------+");
                System.out.printf("| %-10s | %-19s | %-27s |\n", "ID KH", "Ngày Xuất", "Tổng Tiền ($)");
                System.out.println("+------------+---------------------+-----------------------------+");
                System.out.printf("| %-10d | %-19s | %-27s |\n",
                        invoice.getCustomerId(),
                        invoice.getInvoiceDate(),
                        invoice.getTotalAmount().stripTrailingZeros().toPlainString());

                System.out.println("+------------+------------+------------+-------------------------+");
                System.out.printf("| %-10s | %-10s | %-10s | %-23s |\n", "SP ID", "Số lượng", "Đơn giá", "Thành tiền ($)");
                System.out.println("+------------+------------+------------+-------------------------+");

                for (InvoiceItem item : invoice.getItems()) {
                    BigDecimal thanhTien = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    System.out.printf("| %-10d | %-10d | %-10.2f | %-23s |\n",
                            item.getProductId(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            thanhTien.stripTrailingZeros().toPlainString());
                }

                System.out.println("+------------+------------+------------+-------------------------+\n");
            }
        }
    }



    // Thêm mới hóa đơn với chi tiết
    public void addNewInvoice() {
        int customerId = InputValidator.getPositiveInt("Nhập ID khách hàng: ");

        List<InvoiceItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        while (true) {
            int productId = InputValidator.getPositiveInt("Nhập ID sản phẩm: ");
            int quantity = InputValidator.getPositiveInt("Nhập số lượng: ");

            // Kiểm tra tồn kho
            if (!productDAO.isStockSufficient(productId, quantity)) {
                System.out.println(RED + "Số lượng tồn kho không đủ. Không thể thêm sản phẩm này vào hóa đơn." + RESET);
                continue;
            }

            // Lấy giá sản phẩm từ csdl
            BigDecimal unitPrice = productDAO.getProductPriceById(productId);
            if (unitPrice == null) {
                System.out.println(RED + "Không tìm thấy giá sản phẩm với ID: " + productId + RESET);
                continue;
            }

            InvoiceItem item = new InvoiceItem();
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);  // Đơn giá lấy tự động từ DB
            items.add(item);

            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));

            System.out.print("Bạn có muốn thêm sản phẩm khác không? (y/n): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }

        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setInvoiceDate(new Date());  // Lấy ngày hiện tại
        invoice.setTotalAmount(totalAmount);

        invoiceDAO.addInvoiceWithItems(invoice, items);
        System.out.println(GREEN + "Thêm hóa đơn thành công!" + RESET);
    }



    // Tìm kiếm hóa đơn
    public void searchInvoices() {
        while (true) {
            System.out.println(GREEN + "=== Tìm Kiếm Hóa Đơn ===" + RESET);
            System.out.println(BLUE + "1. Tìm kiếm theo Tên khách hàng" + RESET);
            System.out.println(BLUE + "2. Tìm kiếm theo Ngày xuất hóa đơn" + RESET);
            System.out.println(BLUE + "3. Quay về Menu chính" + RESET);
            System.out.print(YELLOW + "Nhập lựa chọn của bạn: " + RESET);

            int choice = InputValidator.getValidInput(1, 3);

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên khách hàng: ");
                    String customerName = scanner.nextLine();
                    List<Invoice> invoicesByName = invoiceDAO.searchInvoicesByCustomerName(customerName);
                    displayInvoices(invoicesByName);
                    break;
                case 2:
                    Date date = InputValidator.getValidDate("Nhập ngày xuất hóa đơn (yyyy-mm-dd): ");
                    List<Invoice> invoicesByDate = invoiceDAO.searchInvoicesByDate((java.sql.Date) date);
                    displayInvoices(invoicesByDate);
                    break;
                case 3:
                    return;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + RESET);
            }
        }
    }


    // Hiển thị danh sách hóa đơn
    private void displayInvoices(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            System.out.println(RED + "Không tìm thấy hóa đơn nào." + RESET);
        } else {
            System.out.println("+--------------------------------------------------------------------------------------+");
            System.out.println("|                             ====== Danh Sách Hóa Đơn ======                          |");
            System.out.println("+------------+---------------------+---------------------+-----------------------------+");
            System.out.printf("| %-10s | %-19s | %-19s | %-27s |\n", "ID", "ID Khách Hàng", "Ngày Xuất", "Tổng Tiền ($)");
            System.out.println("+------------+---------------------+---------------------+-----------------------------+");

            for (Invoice invoice : invoices) {
                System.out.printf("| %-10d | %-19d | %-19s | %-27.2f |\n",
                        invoice.getInvoiceId(),
                        invoice.getCustomerId(),
                        invoice.getInvoiceDate(),
                        invoice.getTotalAmount());
            }

            System.out.println("+------------+---------------------+---------------------+-----------------------------+");
        }
    }

}