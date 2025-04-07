/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 *


/**
 *
 * @author Darshana
 */
package jewellery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jewellery.DBConnect;

public class InsertLoanEntry {
    public static void insert(
        String ENTRY_DATE, String SLIP_NO, String PARTY_NAME, String REMARKS

    ) {
        String insertSQL = "INSERT INTO LOAN_ENTRY (ENTRY_DATE, SLIP_NO, PARTY_NAME, REMARKS) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnect.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf("2024-11-23")); // Date
            preparedStatement.setString(2, SLIP_NO); // Slip No
            preparedStatement.setString(3, PARTY_NAME); // Party Name
            preparedStatement.setString(4, REMARKS); // Remarks

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan entry added successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
