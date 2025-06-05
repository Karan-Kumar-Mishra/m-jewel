package jewellery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class getPartyDetailInLoan {

    public static String[] get(String KeyPartyName) {
        String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
        Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, path);
        String username = "sa";
        String password = "";
        ArrayList<String> partyDetails = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(path, username, password); Statement statement = connection.createStatement()) {

            String selectSQL = "select printname,address,city,mobile1,email from account where grp='Customer' and printname='" + KeyPartyName + "'";

            try (ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("printname");
                    String address = resultSet.getString("address");
                    String city = resultSet.getString("city");
                    String mobile1 = resultSet.getString("mobile1");
                    String email = resultSet.getString("email");

                    // System.out.println(name + " " + address + " " + city + " " + mobile1 + " " + email + " ");
                    partyDetails.add(name);
                    partyDetails.add(address);
                    partyDetails.add(city);
                    partyDetails.add(mobile1);
                    partyDetails.add(email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // It may be helpful to print or log the error details
        }

        // Convert the ArrayList to a String array before returning
        return partyDetails.toArray(new String[0]); // This returns a String[]
    }

}
