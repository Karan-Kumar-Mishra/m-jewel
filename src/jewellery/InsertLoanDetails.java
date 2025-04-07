package jewellery;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import jewellery.DBConnect;

public class InsertLoanDetails {

    public static void insert(String ENTRY_DATE, String SLIP_NO, String PARTY_NAME,
            String REMARKS, String START_DATE, String INTEREST_DATE_PERCENTAGE,
            String WEIGHT_TYPE, String GOLD_WEIGHT,
            String PURITY, String NET_WEIGHT, String ESTIMATED_COST, String AMOUNT_PAID,
            String ITEM_DETAILS, String GUARNATOR_NAME, String GUARNATOR_ADDRESS,
            String DOCUMENTS, String REMINDERS, String NOTES, String ITEM_LOCATION) {

        String insertSQL = """
            INSERT INTO LOAN_ENTRY ( 
                ENTRY_DATE, SLIP_NO, PARTY_NAME,
                REMARKS, START_DATE, INTEREST_DATE_PERCENTAGE,
                WEIGHT_TYPE, GOLD_WEIGHT,
                PURITY, NET_WEIGHT, ESTIMATED_COST, AMOUNT_PAID,
                ITEM_DETAILS, GUARNATOR_NAME, GUARNATOR_ADDRESS,
                DOCUMENTS, REMINDERS, NOTES, ITEM_LOCATION) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DBConnect.connect(); 
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Convert LocalDate to java.sql.Date
            Date entryDate = Date.valueOf(LocalDate.now());
            Date startDate = Date.valueOf(LocalDate.now());

            // Set parameters for the prepared statement
            preparedStatement.setDate(1, entryDate); // ENTRY_DATE
            preparedStatement.setString(2, SLIP_NO); // SLIP_NO
            preparedStatement.setString(3, PARTY_NAME); // PARTY_NAME
            preparedStatement.setString(4, REMARKS); // REMARKS
            preparedStatement.setDate(5, startDate); // START_DATE
            preparedStatement.setDouble(6, Double.parseDouble(INTEREST_DATE_PERCENTAGE)); // INTEREST_DATE_PERCENTAGE
            preparedStatement.setString(7, WEIGHT_TYPE); // WEIGHT_TYPE
            preparedStatement.setDouble(8, Double.parseDouble(GOLD_WEIGHT)); // GOLD_WEIGHT
            preparedStatement.setDouble(9, Double.parseDouble(PURITY)); // PURITY
            preparedStatement.setDouble(10, Double.parseDouble(NET_WEIGHT)); // NET_WEIGHT
            preparedStatement.setDouble(11, Double.parseDouble(ESTIMATED_COST)); // ESTIMATED_COST
            preparedStatement.setDouble(12, Double.parseDouble(AMOUNT_PAID)); // AMOUNT_PAID
            preparedStatement.setString(13, ITEM_DETAILS); // ITEM_DETAILS
            preparedStatement.setString(14, GUARNATOR_NAME); // GUARNATOR_NAME
            preparedStatement.setString(15, GUARNATOR_ADDRESS); // GUARNATOR_ADDRESS
            preparedStatement.setString(16, DOCUMENTS); // DOCUMENTS
            preparedStatement.setString(17, REMINDERS); // REMINDERS
            preparedStatement.setString(18, NOTES); // NOTES
            preparedStatement.setString(19, ITEM_LOCATION); // ITEM_LOCATION

            // Execute the insert statement
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan details added successfully!");
                JOptionPane.showMessageDialog(null, "Loan details added successfully! in loan details table");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Invalid date or number format: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding loan details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}