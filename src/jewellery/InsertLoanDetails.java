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
            String INTREST_TYPE, String WEIGHT_TYPE, String GOLD_WEIGHT,
            String PURITY, String NET_WEIGHT, String ESTIMATED_COST, String AMOUNT_PAID,
            String ITEM_DETAILS, String GUARNATOR_NAME, String GUARNATOR_ADDRESS,
            String GUARANTOR_PHONE,
            String DOCUMENTS, String REMINDERS, String NOTES, String ITEM_LOCATION,
            String INTEREST_AMOUNT) {

        String insertSQL = """
    INSERT INTO LOAN_ENTRY ( 
        ENTRY_DATE, SLIP_NO, PARTY_NAME,
        REMARKS, START_DATE, INTEREST_DATE_PERCENTAGE, INTREST_TYPE,
        WEIGHT_TYPE, GOLD_WEIGHT,
        PURITY, NET_WEIGHT, ESTIMATED_COST, AMOUNT_PAID,
        ITEM_DETAILS, GUARNATOR_NAME, GUARNATOR_ADDRESS,
        GUARANTOR_PHONE,
        DOCUMENTS, REMINDERS, NOTES, ITEM_LOCATION,INTEREST_AMOUNT) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)
""";

        try (Connection connection = DBConnect.connect(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Convert LocalDate to java.sql.Date
            Date entryDate = Date.valueOf(ENTRY_DATE);
            Date startDate = Date.valueOf(START_DATE);

            // Set parameters for the prepared statement
            preparedStatement.setDate(1, entryDate); // ENTRY_DATE
            preparedStatement.setString(2, SLIP_NO); // SLIP_NO
            preparedStatement.setString(3, PARTY_NAME); // PARTY_NAME
            preparedStatement.setString(4, REMARKS); // REMARKS
            preparedStatement.setDate(5, startDate); // START_DATE
            preparedStatement.setDouble(6, Double.parseDouble(INTEREST_DATE_PERCENTAGE)); // INTEREST_DATE_PERCENTAGE
            preparedStatement.setString(7, INTREST_TYPE);
            preparedStatement.setString(8, WEIGHT_TYPE); // WEIGHT_TYPE
            preparedStatement.setDouble(9, Double.parseDouble(GOLD_WEIGHT)); // GOLD_WEIGHT
            preparedStatement.setDouble(10, Double.parseDouble(PURITY)); // PURITY
            preparedStatement.setDouble(11, Double.parseDouble(NET_WEIGHT)); // NET_WEIGHT
            preparedStatement.setDouble(12, Double.parseDouble(ESTIMATED_COST)); // ESTIMATED_COST
            preparedStatement.setDouble(13, Double.parseDouble(AMOUNT_PAID)); // AMOUNT_PAID
            preparedStatement.setString(14, ITEM_DETAILS); // ITEM_DETAILS
            preparedStatement.setString(15, GUARNATOR_NAME); // GUARNATOR_NAME
            preparedStatement.setString(16, GUARNATOR_ADDRESS); // GUARNATOR_ADDRESS
            preparedStatement.setString(17, GUARANTOR_PHONE); // GUARNATOR_PHON
            preparedStatement.setString(18, DOCUMENTS); // DOCUMENTS
            preparedStatement.setString(19, REMINDERS); // REMINDERS
            preparedStatement.setString(20, NOTES); // NOTES
            preparedStatement.setString(21, ITEM_LOCATION); // ITEM_LOCATION
            preparedStatement.setString(22, INTEREST_AMOUNT); //INTEREST_AMOUNT
            // Execute the insert statement
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan details added successfully!");
                JOptionPane.showMessageDialog(null, "Loan details added successfully! in loan details table");
            } else {
                JOptionPane.showMessageDialog(null, "Error while insert the data ");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error while insert the data " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
