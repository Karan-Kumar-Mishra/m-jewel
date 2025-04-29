package jewellery;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableRowCounter {
    
    public static int getRowCount(String tableName) {
        String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + 
                     GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
        String username = "sa";
        String password = "";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int rowCount = 0;
        
        try {
            // 1. Establish connection
            Logger.getLogger(TableRowCounter.class.getName()).log(Level.SEVERE, path);
            conn = DriverManager.getConnection(path, username, password);
            
            // 2. Create statement
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                      ResultSet.CONCUR_READ_ONLY);
            
            // 3. Execute query
            String sql = "SELECT COUNT(*) AS row_count FROM " + tableName;
            rs = stmt.executeQuery(sql);
            
            // 4. Get the row count
            if (rs.next()) {
                rowCount = rs.getInt("row_count");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TableRowCounter.class.getName()).log(Level.SEVERE, null, ex);
            rowCount = -1; // Return -1 to indicate error
        } finally {
            // 5. Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(TableRowCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return rowCount;
    }
    
    // Example usage for your LOAN_ENTRY table
//    public static int getLoanEntryCount() {
//        return getRowCount("LOAN_ENTRY");
//    }
}
