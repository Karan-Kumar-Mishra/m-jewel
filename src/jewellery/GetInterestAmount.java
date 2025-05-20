package jewellery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class GetInterestAmount {

    public static double loanAmt;
    public static double totalInterest;

    public static double getTotalInterest(String partyName) {
        try {
            // Query the database for all receipts of the given party
            String query = "SELECT INTREST_AMOUNT FROM LOAN_RECEIPT WHERE PARTY_NAME = '" + partyName + "'";
            List<List<Object>> result = DBController.getDataFromTable(query);

            // Check if data was found
            if (result == null || result.isEmpty()) {
                return 0.0; // No receipts found for the party
            }

            // Sum the interest amounts
            double totalInterest = 0.0;
            for (List<Object> row : result) {
                // INTREST_AMOUNT is the first column in the query result (index 0)
                if (row.get(0) != null) {
                    totalInterest += Double.parseDouble(row.get(0).toString());
                }
            }

            return totalInterest;

        } catch (Exception e) {
            System.err.println("Error calculating total interest for party " + partyName + ": " + e.getMessage());
            return 0.0;
        }
    }

    public static double getInterestAmount(String partyName) {
        try {
            // Query the database for loan data of the given party
            String query = "SELECT START_DATE, INTEREST_DATE_PERCENTAGE , INTREST_TYPE , AMOUNT_PAID "
                    + "FROM LOAN_ENTRY WHERE PARTY_NAME  = '" + partyName + "'";
            List<List<Object>> result = DBController.getDataFromTable(query);

            // Check if data was found
            if (result == null || result.isEmpty()) {
                throw new Exception("Party name not found: " + partyName);
            }

            // Get the first matching record
            List<Object> rowData = result.get(0);

            // Extract required fields
            String startDateStr = rowData.get(0) != null ? rowData.get(0).toString() : "";
            double intAmt = rowData.get(1) != null ? Double.parseDouble(rowData.get(1).toString()) : 0.00;
            String interestType = rowData.get(2) != null ? rowData.get(2).toString() : "Month";
            loanAmt = rowData.get(3) != null ? Double.parseDouble(rowData.get(3).toString()) : 0.00;

            // Parse start date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);

            // Current date (May 19, 2025)
            Date currentDate = new Date();

            // Calculate days
            long rowDays = (currentDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            // Calculate interest
            double dailyInterest = (loanAmt * intAmt / 100) / 30;

            if (interestType.equals("Day")) {
                totalInterest = (dailyInterest * (rowDays) / 30) * rowDays; // Daily

            } else {
                long month = rowDays / 30;
                totalInterest = (dailyInterest * rowDays) * month; // Monthly
            }

            totalInterest = totalInterest - getTotalInterest(partyName);

            double updated_amount = 0.0;
            if (totalInterest <= 0) {
                // now substract forn loan amount
                updated_amount = loanAmt - Math.abs(totalInterest);
                DBController.executeQueryUpdate("UPDATE LOAN_ENTRY set AMOUNT_PAID=" + updated_amount + " where PARTY_NAME='" + partyName + "';");
                totalInterest = 0;
            }
            DBController.executeQueryUpdate("UPDATE LOAN_ENTRY set INTEREST_AMOUNT=" + totalInterest + " where PARTY_NAME='" + partyName + "';");

            return totalInterest;

        } catch (Exception e) {
            System.err.println("Error calculating interest for party " + partyName + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, "error amount is=> " + totalInterest);
            return 0.0;
        }
    }

}
