
package jewellery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoanEntryDeleter {

    private static final Logger LOGGER = Logger.getLogger(LoanEntryDeleter.class.getName());

    public static boolean deleteLoanByPartyName(String partyName) {
        if (partyName == null || partyName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Party name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (Connection connection = DBConnect.connect()) {
            // First confirm the loan exists
       

            // Proceed with deletion
            String deleteSql = "DELETE FROM LOAN_ENTRY WHERE PARTY_NAME = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, partyName);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
//                    JOptionPane.showMessageDialog(null, 
//                        "Successfully deleted loan for party: " + partyName, 
//                        "Success", 
//                        JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.log(Level.INFO, "Deleted loan entry for party: {0}", partyName);
                    return true;
                } else {
//                    JOptionPane.showMessageDialog(null, 
//                        "No loan was deleted for party: " + partyName, 
//                        "Warning", 
//                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting loan for party: " + partyName, e);
//            JOptionPane.showMessageDialog(null, 
//                "Error deleting loan: " + e.getMessage(), 
//                "Error", 
//                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}