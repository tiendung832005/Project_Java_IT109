package ra.edu.business.service;

import ra.edu.business.dao.ReportDAO;
import ra.edu.validate.InputValidator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;

public class ReportService {

    private ReportDAO reportDAO = new ReportDAO();
    private Scanner scanner = new Scanner(System.in);
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";

    // Hiển thị tổng doanh thu theo khoảng ngày
    public void displayRevenueBetweenDates() {
        Date startDate = InputValidator.getValidDate("Nhập ngày bắt đầu (yyyy-mm-dd): ");
        Date endDate = InputValidator.getValidDate("Nhập ngày kết thúc (yyyy-mm-dd): ");

        BigDecimal revenue = reportDAO.calculateRevenueBetweenDates(startDate, endDate);
        System.out.printf(GREEN + "Tổng doanh thu từ %s đến %s: %s $\n",
                startDate, endDate, revenue.stripTrailingZeros().toPlainString() + RESET);
    }

    // Hiển thị tổng doanh thu theo khoảng tháng
    public void displayRevenueBetweenMonths() {
        String startMonthStr = InputValidator.getValidMonth("Nhập tháng bắt đầu (yyyy-MM): ");
        String endMonthStr = InputValidator.getValidMonth("Nhập tháng kết thúc (yyyy-MM): ");

        BigDecimal revenue = reportDAO.calculateRevenueBetweenMonths(startMonthStr, endMonthStr);
        System.out.printf(GREEN + "Tổng doanh thu từ %s đến %s: %s $\n",
                startMonthStr, endMonthStr, revenue.stripTrailingZeros().toPlainString() + RESET);
    }

    // Hiển thị tổng doanh thu theo khoảng năm
    public void displayRevenueBetweenYears() {
        String startYearStr = InputValidator.getValidYear("Nhập năm bắt đầu (yyyy): ");
        String endYearStr = InputValidator.getValidYear("Nhập năm kết thúc (yyyy): ");

        BigDecimal revenue = reportDAO.calculateRevenueBetweenYears(startYearStr, endYearStr);
        System.out.printf(GREEN + "Tổng doanh thu từ %s đến %s: %s $\n",
                startYearStr, endYearStr, revenue.stripTrailingZeros().toPlainString() + RESET);
    }

}
