package ra.edu.validate;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);
    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";

    public static int getValidInput(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println(RED + "Lựa chọn không hợp lệ. Vui lòng nhập số từ " + min + " đến " + max + "." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Đầu vào không hợp lệ. Vui lòng nhập một số hợp lệ." + RESET);
            }
        }
        return choice;
    }

    public static int getPositiveInt(String prompt) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    break;
                } else {
                    System.out.println(RED + "Vui lòng nhập số nguyên dương." + RESET) ;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Đầu vào không hợp lệ. Vui lòng nhập một số nguyên." + RESET);
            }
        }
        return value;
    }
    // Validate ngày theo yyyy-MM-dd
    public static java.sql.Date getValidDate(String prompt) {
        java.sql.Date date = null;
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine();
                date = java.sql.Date.valueOf(dateStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(RED + "Định dạng ngày không hợp lệ. Vui lòng nhập theo yyyy-mm-dd." + RESET);
            }
        }
        return date;
    }

    // Validate tháng theo yyyy-MM
    public static String getValidMonth(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String monthStr = scanner.nextLine();
                if (monthStr.matches("\\d{4}-\\d{2}")) {
                    return monthStr;
                } else {
                    System.out.println(RED + "Định dạng tháng không hợp lệ. Vui lòng nhập theo yyyy-MM." + RESET);
                }
            } catch (Exception e) {
                System.out.println(RED + "Lỗi nhập tháng. Vui lòng thử lại." + RESET);
            }
        }
    }

    // Validate năm theo yyyy
    public static String getValidYear(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String yearStr = scanner.nextLine();
                if (yearStr.matches("\\d{4}")) {
                    return yearStr;
                } else {
                    System.out.println(RED + "Định dạng năm không hợp lệ. Vui lòng nhập theo yyyy." + RESET);
                }
            } catch (Exception e) {
                System.out.println(RED + "Lỗi nhập năm. Vui lòng thử lại." + RESET);
            }
        }
    }


    // Hàm nhập giá tiền
    public static BigDecimal getValidBigDecimal(String prompt) {
        BigDecimal value;
        while (true) {
            try {
                System.out.print(prompt);
                value = new BigDecimal(scanner.nextLine());
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    break;
                } else {
                    System.out.println(RED + "Giá phải là số dương." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Giá không hợp lệ. Vui lòng nhập lại." + RESET);
            }
        }
        return value;
    }

    // Hàm nhập khoảng giá, tồn kho
    public static double getValidDouble(String prompt) {
        double value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) {
                    break;
                } else {
                    System.out.println(RED + "Giá trị phải không âm." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Đầu vào không hợp lệ. Vui lòng nhập lại." + RESET);
            }
        }
        return value;
    }
}
