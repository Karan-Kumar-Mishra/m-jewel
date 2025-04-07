package jewellery;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetPartyName {

    public static String[] get() {
        String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, path);     String username = "sa";
        String password = "";
        ArrayList<String> partyNamelist = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(path, username, password);
             Statement statement = connection.createStatement()) {
    
            String selectSQL = "select printname from account where grp='Loans'";
            
            try (ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    partyNamelist.add(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // It may be helpful to print or log the error details
        }
    
        // Convert the ArrayList to a String array before returning
        return partyNamelist.toArray(new String[0]); // This returns a String[]
    }
    
    // public static void main(String[] args) {
    // for (String name : GetPartyName.get()) {
    // System.out.println(name);
    // }
    // }
}
