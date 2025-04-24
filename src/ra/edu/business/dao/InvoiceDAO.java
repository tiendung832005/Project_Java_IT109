package ra.edu.business.dao;

import ra.edu.business.config.DatabaseConfig;
import ra.edu.entity.Invoice;
import ra.edu.entity.InvoiceItem;
import ra.edu.validate.Validator;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    // Lấy ds hóa đơn
    public List<Invoice> getAllInvoices(){
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM invoices";
        try (Connection connection = DatabaseConfig.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(resultSet.getInt("invoice_id"));
                invoice.setCustomerId(resultSet.getInt("customer_id"));
                invoice.setInvoiceDate(resultSet.getDate("invoice_date"));
                invoice.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return invoices;
    }


    //Thêm hóa đơn
    public void addInvoiceWithItems(Invoice invoice, List<InvoiceItem> items) {
        String invoiceQuery = "INSERT INTO invoices (customer_id, invoice_date, total_amount) VALUES (?, ?, ?)";
        String itemQuery = "INSERT INTO invoice_items (invoice_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String priceQuery = "SELECT price FROM products WHERE product_id = ?";

        try (Connection connection = DatabaseConfig.openConnection()) {
            // Kiểm tra id khách hàng
            if (!Validator.isCustomerExists(connection, invoice.getCustomerId())) {
                System.out.println("Không thể thêm hóa đơn vì khách hàng không tồn tại.");
                return;
            }

            // Kiểm tra id sản phẩm và cập nhật đơn giá từ DB
            for (InvoiceItem item : items) {
                if (!Validator.isProductExists(connection, item.getProductId())) {
                    System.out.println("Không thể thêm hóa đơn vì sản phẩm không tồn tại: ID = " + item.getProductId());
                    return;
                }
                try (PreparedStatement priceStmt = connection.prepareStatement(priceQuery)) {
                    priceStmt.setInt(1, item.getProductId());
                    ResultSet rs = priceStmt.executeQuery();
                    if (rs.next()) {
                        item.setUnitPrice(rs.getBigDecimal("price")); // Gán giá từ DB
                    } else {
                        System.out.println("Không tìm thấy giá sản phẩm ID = " + item.getProductId());
                        return;
                    }
                }
            }

            connection.setAutoCommit(false);

            try (PreparedStatement invoiceStmt = connection.prepareStatement(invoiceQuery, Statement.RETURN_GENERATED_KEYS)) {
                invoiceStmt.setInt(1, invoice.getCustomerId());
                invoiceStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));  // Ngày hiện tại
                invoiceStmt.setBigDecimal(3, invoice.getTotalAmount());
                invoiceStmt.executeUpdate();

                ResultSet generatedKeys = invoiceStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    invoice.setInvoiceId(generatedKeys.getInt(1));
                }

                try (PreparedStatement itemStmt = connection.prepareStatement(itemQuery)) {
                    for (InvoiceItem item : items) {
                        itemStmt.setInt(1, invoice.getInvoiceId());
                        itemStmt.setInt(2, item.getProductId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setBigDecimal(4, item.getUnitPrice());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }

                connection.commit();
                System.out.println("Thêm hóa đơn và các mục hóa đơn thành công");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối đến csdl: " + e.getMessage());
        }
    }


    // Lấy danh sách hóa đơn chi tiết
    public List<Invoice> getAllInvoicesWithItems() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.*, ii.product_id, ii.quantity, ii.unit_price FROM invoices i " +
                "JOIN invoice_items ii ON i.invoice_id = ii.invoice_id";

        try (Connection connection = DatabaseConfig.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int invoiceId = resultSet.getInt("invoice_id");
                Invoice invoice = findInvoiceById(invoices, invoiceId);
                if (invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceId(invoiceId);
                    invoice.setCustomerId(resultSet.getInt("customer_id"));
                    invoice.setInvoiceDate(resultSet.getDate("invoice_date"));
                    invoice.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                    invoices.add(invoice);
                }

                InvoiceItem item = new InvoiceItem();
                item.setProductId(resultSet.getInt("product_id"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setUnitPrice(resultSet.getBigDecimal("unit_price"));
                invoice.addItem(item);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return invoices;
    }

    private Invoice findInvoiceById(List<Invoice> invoices, int invoiceId) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceId() == invoiceId) {
                return invoice;
            }
        }
        return null;
    }

    // Tìm kiếm hóa đơn theo tên khách hàng
    public List<Invoice> searchInvoicesByCustomerName(String customerName) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.* FROM invoices i JOIN customers c ON i.customer_id = c.customer_id WHERE c.name LIKE ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + customerName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(resultSet.getInt("invoice_id"));
                invoice.setCustomerId(resultSet.getInt("customer_id"));
                invoice.setInvoiceDate(resultSet.getDate("invoice_date"));
                invoice.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm hóa đơn theo tên khách hàng: " + e.getMessage());
        }
        return invoices;
    }

    // Tìm kiếm hóa đơn theo ngày
    public List<Invoice> searchInvoicesByDate(Date date) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM invoices WHERE invoice_date = ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(resultSet.getInt("invoice_id"));
                invoice.setCustomerId(resultSet.getInt("customer_id"));
                invoice.setInvoiceDate(resultSet.getDate("invoice_date"));
                invoice.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm hóa đơn theo ngày: " + e.getMessage());
        }
        return invoices;
    }
}

