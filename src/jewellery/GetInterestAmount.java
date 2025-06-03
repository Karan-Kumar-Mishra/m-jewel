package jewellery;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import jewellery.DBController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetInterestAmount {

    public static BigDecimal RECEIPT_totalLoanAmount = BigDecimal.ZERO;
    public static BigDecimal RECEIPT_totalInterestAmount = BigDecimal.ZERO;

    public static void calculateTotalLoanAndInterest() {
        List<Object> queryResult = DBController.executeQuery(
                "SELECT LOAN_AMOUNT, INTREST_AMOUNT FROM LOAN_RECEIPT ");

        // Each row has 2 columns (LOAN_AMOUNT, INTREST_AMOUNT)
        int columnsPerRow = 2;
        int rowCount = queryResult.size() / columnsPerRow;

        if (rowCount > 0) {
            // Get the last row
            int lastRowIndex = (rowCount - 1) * columnsPerRow;

            // Parse loan amount and interest amount from last row
            RECEIPT_totalLoanAmount = parseBigDecimal(queryResult.get(lastRowIndex));
            RECEIPT_totalInterestAmount = parseBigDecimal(queryResult.get(lastRowIndex + 1));
        } else {
            // No rows found, reset to zero
            RECEIPT_totalLoanAmount = BigDecimal.ZERO;
            RECEIPT_totalInterestAmount = BigDecimal.ZERO;
        }

        BigDecimal grandTotal = RECEIPT_totalLoanAmount.add(RECEIPT_totalInterestAmount);

        // Display results
        String message = String.format(
                "Loan Amount: %s%n"
                + "Interest Amount: %s%n"
                + "Grand Total: %s",
                RECEIPT_totalLoanAmount.toString(),
                RECEIPT_totalInterestAmount.toString(),
                grandTotal.toString());

        // JOptionPane.showMessageDialog(null, message, "Loan Receipt Summary",
        // JOptionPane.INFORMATION_MESSAGE);
    }

    public static void updateAllInterestAmounts() {
        Double totalInterest = 0.0;
        Date startDate = new Date();
        double interestDatePercentage = 0.0;
        String interestType = "month";
        double amountPaid = 0.0;
        double interestAmount = 0.0;
        String partyName = "";

        List<Object> queryResult = DBController.executeQuery(
                "SELECT START_DATE, INTEREST_DATE_PERCENTAGE, INTREST_TYPE, AMOUNT_PAID, INTEREST_AMOUNT, PARTY_NAME FROM LOAN_ENTRY");

        // Each row has 6 columns
        int columnsPerRow = 6;
        int rowCount = queryResult.size() / columnsPerRow;

        for (int i = 0; i < rowCount; i++) {
            int baseIndex = i * columnsPerRow;

            // 1. Parse START_DATE (safe casting)
            startDate = (Date) queryResult.get(baseIndex);

            // 2. Parse numeric fields (handle both String and BigDecimal)
            interestDatePercentage = parseNumber(queryResult.get(baseIndex + 1));
            amountPaid = parseNumber(queryResult.get(baseIndex + 3));
            interestAmount = parseNumber(queryResult.get(baseIndex + 4));

            // 3. Parse String fields
            interestType = (String) queryResult.get(baseIndex + 2);
            partyName = (String) queryResult.get(baseIndex + 5);

            // Calculate days between now and start date
            long rowDays = (System.currentTimeMillis() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            // Calculate interest
            double dailyInterest = (amountPaid * interestDatePercentage / 100) / 30;

            if (interestType.equalsIgnoreCase("Day")) {
                totalInterest += (dailyInterest * rowDays); // Daily interest
            } else {
                long months = rowDays / 30;
                totalInterest += (dailyInterest * 30 * months); // Monthly interest
            }

            calculateTotalLoanAndInterest();
            BigDecimal finalInterestAmount = BigDecimal.ZERO;

            finalInterestAmount = BigDecimal.valueOf(totalInterest).subtract(RECEIPT_totalInterestAmount);

            BigDecimal finalLoanAmount = BigDecimal.valueOf(amountPaid).subtract(RECEIPT_totalLoanAmount);

            if (finalInterestAmount.doubleValue() <= 0) {
                finalLoanAmount = finalLoanAmount.subtract(finalInterestAmount.abs());
                finalInterestAmount = BigDecimal.ZERO;
                LocalDate currentDate = LocalDate.now();

                // Define the desired format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = currentDate.format(formatter);
                DBController.executeQueryUpdate("update  LOAN_ENTRY SET START_DATE ='" + formattedDate
                        + "'  where PARTY_NAME = '" + partyName + "' ;  ");
            }
            DBController.executeQueryUpdate("update  LOAN_ENTRY SET INTEREST_AMOUNT='" + finalInterestAmount
                    + "' ,AMOUNT_PAID='" + finalLoanAmount + "' where PARTY_NAME = '" + partyName + "' ;  ");
            JOptionPane.showMessageDialog(null,
                    "Total Interest: " + finalInterestAmount + " loan amount :" + finalLoanAmount);

        }

    }

    // Helper method to safely parse numbers (String or BigDecimal)
    private static double parseNumber(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException("Cannot parse number from: " + value);
        }
    }

    private static BigDecimal parseBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
    static String sno = "0";
    public static void processAllLoanEntries() {
        List<Object> LOAN_DATA = DBController.executeQuery(
                "SELECT PARTY_NAME,AMOUNT_PAID,INTEREST_AMOUNT,START_DATE,REMARKS FROM LOAN_ENTRY ORDER BY SLIP_NO DESC LIMIT 1;");

        // Initialize with default values in case RECEIPT_DATA is empty
        List<Object> RECEIPT_DATA = DBController.executeQuery(
                "SELECT PARTY_NAME,LOAN_AMOUNT,INTREST_AMOUNT,TRANSACTION_DATE,REMARKS FROM LOAN_RECEIPT ORDER BY RECEIPT_NO DESC LIMIT 1;");

         // Changed to String to match table definition

        // Handle empty LOAN_DATA
        if (LOAN_DATA.isEmpty()) {
            System.out.println("No loan data found");
            return;
        }

        String partyname = (LOAN_DATA.get(0) != null && LOAN_DATA.get(0).toString().length() > 0)
                ? LOAN_DATA.get(0).toString()
                : "Default";

        // for loan - use 0.0 if RECEIPT_DATA is empty
        String DR_AMT1 = LOAN_DATA.get(1) != null ? LOAN_DATA.get(1).toString() : "0.0";
        String CR_AMT1 = (!RECEIPT_DATA.isEmpty() && RECEIPT_DATA.get(1) != null)
                ? RECEIPT_DATA.get(1).toString()
                : "0.0";

        // for interest - use 0.0 if RECEIPT_DATA is empty
        String DR_AMT2 = LOAN_DATA.get(2) != null ? LOAN_DATA.get(2).toString() : "0.0";
        String CR_AMT2 = (!RECEIPT_DATA.isEmpty() && RECEIPT_DATA.get(2) != null)
                ? RECEIPT_DATA.get(2).toString()
                : "0.0";

        String BALANCE1 = String.valueOf(Double.parseDouble(DR_AMT1) - Double.parseDouble(CR_AMT1));
        String BALANCE2 = String.valueOf(Double.parseDouble(DR_AMT2) - Double.parseDouble(CR_AMT2));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String date1Str = "NULL";
        if (LOAN_DATA.get(3) != null) {
            try {
                Date date1 = dateFormat.parse(LOAN_DATA.get(3).toString());
                date1Str = "'" + sqlDateFormat.format(date1) + "'";
            } catch (Exception e) {
                System.out.println("Error parsing loan date: " + e.getMessage());
            }
        }

        String date2Str = "NULL";
        if (!RECEIPT_DATA.isEmpty() && RECEIPT_DATA.get(3) != null) {
            try {
                Date date2 = dateFormat.parse(RECEIPT_DATA.get(3).toString());
                date2Str = "'" + sqlDateFormat.format(date2) + "'";
            } catch (Exception e) {
                System.out.println("Error parsing receipt date: " + e.getMessage());
            }
        }

        String REMARKS1 = LOAN_DATA.get(4) != null ? "'" + LOAN_DATA.get(4).toString().replace("'", "''") + "'" : "''";
        String REMARKS2 = (!RECEIPT_DATA.isEmpty() && RECEIPT_DATA.get(4) != null)
                ? "'" + RECEIPT_DATA.get(4).toString().replace("'", "''") + "'"
                : "''";

        // Properly quote string values in the SQL statement
        String sql = "INSERT INTO LOAN_LEDGER VALUES("
                + "'" + sno + "', '" + partyname.replace("'", "''") + "', " + date1Str + ", " + REMARKS1 + ", "
                + "'" + DR_AMT1 + "', '" + CR_AMT1 + "', '" + BALANCE1 + "', "
                + date2Str + ", " + REMARKS2 + ", "
                + "'" + DR_AMT2 + "', '" + CR_AMT2 + "', '" + BALANCE2 + "')";

        DBController.executeQueryUpdate(sql);
        int a= Integer.parseInt(sno);
        a++;
        sno = a+"";
        
        
    }
}
