package ra.edu.business.dao;

import ra.edu.business.config.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ReportDAO {

    // Tính tổng doanh thu từ ngày nào đến ngày nào
    public BigDecimal calculateRevenueBetweenDates(Date startDate, Date endDate) {
        String query = "SELECT SUM(total_amount) FROM invoices WHERE invoice_date BETWEEN ? AND ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tính doanh thu theo khoảng ngày: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // Tính tổng doanh thu từ tháng nào đến tháng nào
    public BigDecimal calculateRevenueBetweenMonths(String startMonth, String endMonth) {
        String query = "SELECT SUM(total_amount) FROM invoices WHERE DATE_FORMAT(invoice_date, '%Y-%m') BETWEEN ? AND ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startMonth);
            preparedStatement.setString(2, endMonth);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tính doanh thu theo khoảng tháng: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // Tính tổng doanh thu từ năm nào đến năm nào
    public BigDecimal calculateRevenueBetweenYears(String startYear, String endYear) {
        String query = "SELECT SUM(total_amount) FROM invoices WHERE YEAR(invoice_date) BETWEEN ? AND ?";
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startYear);
            preparedStatement.setString(2, endYear);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tính doanh thu theo khoảng năm: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
