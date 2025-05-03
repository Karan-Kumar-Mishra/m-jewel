package jewellery;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DatabaseTableCreator {

    public static void create() {
        try (Connection connection = DBConnect.connect(); Statement statement = connection.createStatement()) {

            // SQL to create LOAN_ENTRY table
            String createLoanEntryTable = """
                CREATE TABLE IF NOT EXISTS LOAN_ENTRY (
                    ENTRY_DATE DATE ,
                    SLIP_NO VARCHAR(50) ,
                    PARTY_NAME VARCHAR(100) ,
                    REMARKS VARCHAR(255),
                    START_DATE DATE,
                    INTEREST_DATE_PERCENTAGE DECIMAL(5, 2),
                    INTREST_TYPE VARCHAR(50),
                    WEIGHT_TYPE VARCHAR(50),
                    GOLD_WEIGHT DECIMAL(10, 3),
                    PURITY DECIMAL(5, 2),
                    NET_WEIGHT DECIMAL(10, 3),
                    ESTIMATED_COST DECIMAL(15, 2),
                    AMOUNT_PAID DECIMAL(15, 2),
                    ITEM_DETAILS VARCHAR(255),
                    GUARNATOR_NAME VARCHAR(50),
                    GUARNATOR_ADDRESS VARCHAR(50),
                    GUARANTOR_PHONE VARCHAR(50),
                    DOCUMENTS VARCHAR(50),
                    REMINDERS VARCHAR(50),
                    NOTES VARCHAR(40),
                    ITEM_LOCATION VARCHAR(40)
                )
            """;

            statement.execute(createLoanEntryTable);

            Logger.getLogger(DatabaseTableCreator.class.getName()).log(Level.INFO, "LOAN_ENTRY table created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(DatabaseTableCreator.class.getName()).log(Level.SEVERE, "LOAN_ENTRY table is not created successfully.", e);
        }
    }
}
