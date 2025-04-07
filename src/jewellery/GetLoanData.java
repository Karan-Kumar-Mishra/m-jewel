package jewellery;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetLoanData {
    public static String[][] get() {
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
            Logger.getLogger(GetLoanData.class.getName()).log(Level.SEVERE, path);
            conn = DriverManager.getConnection(path, username, password);
            
            // 2. Create statement
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                      ResultSet.CONCUR_READ_ONLY);
            
            // 3. Execute query
            String sql = "SELECT * FROM LOAN_ENTRY";
            rs = stmt.executeQuery(sql);
            
            // 4. Get row and column count
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 5. Initialize result array
            result = new String[rowCount][columnCount];
            
            // 6. Populate result array
            int rowIndex = 0;
            while (rs.next()) {
                for (int col = 0; col < columnCount; col++) {
                    result[rowIndex][col] = rs.getString(col + 1);
                    if (result[rowIndex][col] == null) {
                        result[rowIndex][col] = ""; // Replace null with empty string
                    }
                }
                rowIndex++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(GetLoanData.class.getName()).log(Level.SEVERE, null, ex);
            result = new String[0][0]; // Return empty array on error
        } finally {
            // 7. Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(GetLoanData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
}