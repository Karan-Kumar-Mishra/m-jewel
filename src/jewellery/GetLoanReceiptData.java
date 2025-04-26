package jewellery;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetLoanReceiptData {
    public static String[][] getinfo() {
        String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + 
                     GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
        String username = "sa";
        String password = "";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String[][] result = null;
        
        try {
            // 1. Establish connection
            Logger.getLogger(GetLoanReceiptData.class.getName()).log(Level.SEVERE, path);
            conn = DriverManager.getConnection(path, username, password);
            
            // 2. Create statement
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                      ResultSet.CONCUR_READ_ONLY);
            
            // 3. Execute query for specific columns
            String sql = "SELECT SLIP_NO, PARTY_NAME, AMOUNT_PAID, INTEREST_DATE_PERCENTAGE, REMARKS,START_DATE  FROM LOAN_ENTRY";
            rs = stmt.executeQuery(sql);
            
            // 4. Get row count (we know column count is 5)
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            
            // 5. Initialize result array (6 columns)
            result = new String[rowCount][6];
            
            // 6. Populate result array
            int rowIndex = 0;
            while (rs.next()) {
                result[rowIndex][0] = rs.getString("SLIP_NO") != null ? rs.getString("SLIP_NO") : "";
                result[rowIndex][1] = rs.getString("PARTY_NAME") != null ? rs.getString("PARTY_NAME") : "";
                result[rowIndex][2] = rs.getString("AMOUNT_PAID") != null ? rs.getString("AMOUNT_PAID") : "";
                result[rowIndex][3] = rs.getString("INTEREST_DATE_PERCENTAGE") != null ? rs.getString("INTEREST_DATE_PERCENTAGE") : "";
                result[rowIndex][4] = rs.getString("REMARKS") != null ? rs.getString("REMARKS") : "";
                result[rowIndex][5] = rs.getString("START_DATE") != null ? rs.getString("START_DATE") : "";

                rowIndex++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(GetLoanReceiptData.class.getName()).log(Level.SEVERE, null, ex);
            result = new String[0][6]; // Return empty array with 5 columns on error
        } finally {
            // 7. Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(GetLoanReceiptData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
} 