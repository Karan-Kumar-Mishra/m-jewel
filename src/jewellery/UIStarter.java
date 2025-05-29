package jewellery;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import jewellery.DatabaseTableCreator;

public class UIStarter extends javax.swing.JFrame {
 

 
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        
        SwingUtilities.invokeLater(() -> {

        }
        );

        //new DashBoardScreen().setVisible(true);
        //new PurchaseScreen().setVisible(true);
        //// new DayBookScreen.setVisible(true);
        new RedesignedLauncher().setVisible(true);
        DatabaseTableCreator.create();

    }
    
    void setImage() {
        String path = System.getProperty("user.dir") + "/Images/" + "launchericon.jpeg";
//        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpeg"); 
Image icon = Toolkit.getDefaultToolkit().getImage(path);
        this.setIconImage(icon);
    }

}
