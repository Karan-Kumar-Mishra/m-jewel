package jewellery;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LastSnoValue {
    
    public static int getLastSnoValue(String tablename) {
        String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + 
                     GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
        String username = "sa";
        String password = "";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int lastSno = 0;
        
        try {
            // 1. Establish connection
            Logger.getLogger(LastSnoValue.class.getName()).log(Level.SEVERE, path);
            conn = DriverManager.getConnection(path, username, password);
            
            // 2. Create statement
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                      ResultSet.CONCUR_READ_ONLY);
            
            // 3. Execute query to get the last sno value
            String sql = "SELECT SLIP_NO FROM "+tablename+" ORDER BY SLIP_NO DESC LIMIT 1";
            rs = stmt.executeQuery(sql);
            
            // 4. Get the last sno value
            if (rs.next()) {
                lastSno = rs.getInt("SLIP_NO");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(LastSnoValue.class.getName()).log(Level.SEVERE, null, ex);
            lastSno = -1; // Return -1 to indicate error
        } finally {
            // 5. Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LastSnoValue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return lastSno;
    }
    
}