package jewellery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ccs
 */
public class DBConnect {

    public static Connection connect() {
        Connection con = null;

        try {
            Class.forName("org.h2.Driver");
            String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, path);

            con = DriverManager.getConnection("jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE",
                     "sa", "");
            System.out.println("Connection done");

        } catch (Exception e) {
            System.out.println("inter.DBConnect.connect()");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }

//     public static Connection connect() {
//        Connection con = null;
//        try {
////            Class.forName("org.sqlite.JDBC");
//            String companyname = "";
//            String fyStart = "";
//
//            if (createCompany.companyname != null && createCompany.FYstart != null) {
//                companyname = createCompany.companyname;
//                fyStart = createCompany.FYstart;
//                System.out.println(" start f1");
//            }else if(createCompany.companyname != null && createCompany.FYstart1 != null){
//               companyname = createCompany.companyname;
//                fyStart = createCompany.FYstart1;
//                System.out.println(" start f2");
//            }
//            Connection comcon = connectCopy();
//            Statement stmt = comcon.createStatement();
//            if (listAndSelectCompany.selectedcompany != null && listAndSelectCompany.selectedcompany != "") {
//                String sql = "select cname,fystart from company where id =" + listAndSelectCompany.selectedcompany + "";
//
//                ResultSet re = stmt.executeQuery(sql);
//                while (re.next()) {
//                    companyname = re.getString(1);
//                    fyStart = re.getString(2);
////                    JOptionPane.showMessageDialog(null,"company "+companyname);
//                }
//                re.close();
//            }
//            comcon.close();
//            stmt.close();
//
//            Class.forName("org.h2.Driver").newInstance();
////            con=DriverManager.getConnection("jdbc:sqlite:cold_db.db");
////JOptionPane.showMessageDialog(null,companyname );
//            String databaseref = "jdbc:h2:./company/" + companyname.trim() + "/" + fyStart + "_db";
////            JOptionPane.showMessageDialog(null, databaseref);
//            con = DriverManager.getConnection(databaseref, "sa", "");
//            System.out.print("Connection done "+databaseref);
//
//        } catch (Exception e) {
//            System.out.println("inter.DBConnect.connect()");
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return con;
//    }
    public static Connection maxDatabaseConnection(String compnayname, String fystart) {
        Connection con = null;
        try {

            Class.forName("org.h2.Driver");

            String databaseref = "jdbc:h2:./company/" + compnayname.trim() + "/" + fystart + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";

            con = DriverManager.getConnection(databaseref, "sa", "");
            System.out.print("Connection done");

        } catch (Exception e) {
            System.out.println("inter.DBConnect.connect()");
            JOptionPane.showMessageDialog(null, e);
        }
        return con;

    }

    public static Connection connectCopy() {
        Connection con = null;
        try {
            Class.forName("org.h2.Driver");
//            con=DriverManager.getConnection("jdbc:sqlite:cold_db.db");
            con = DriverManager.getConnection("jdbc:h2:./company/company_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE", "sa", "");
            System.out.println("Connection done");

        } catch (Exception e) {

            System.out.println("inter.DBConnect.connect()");
            JOptionPane.showMessageDialog(null, "Already Running");
        }
         StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Print relevant information
        System.out.println("Stack trace of method calls:");
        for (StackTraceElement element : stackTrace) {
            System.out.println("Class: " + element.getClassName() + ", Method: " + element.getMethodName());
        }
        
        return con;
    }
}
