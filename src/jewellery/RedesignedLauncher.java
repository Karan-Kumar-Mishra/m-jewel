package jewellery;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

//import testgroup.LOGOINIT;
/**
 *
 * @author AR-LABS
 */
public class RedesignedLauncher extends javax.swing.JFrame {

    private ImageIcon bgImage;
    private Timer timer;
    private int pbValue = 0;
    private int progressTextsIndex = 0;

    private String[] progressTexts = {
        "Loading account records...",
        "Loading daily update records...",
        "Loading day book records...",
        "Loading company info records...",
        "Loading entry items...",
        "Loading outstanding analysis records...",
        "Loading purchase history...",
        "Loading sales records...",
        "Loading sale register records.....",
        "Loading items list..."
    };

    private static final Color LIGHT_ORANGE = new Color(242, 176, 59);

    public RedesignedLauncher() throws SQLException, FileNotFoundException {
        String imagepath = System.getProperty("user.dir") + "/Images/" + "launchericon.jpeg";
        Image icon = Toolkit.getDefaultToolkit().getImage(imagepath);
        // Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpg");  
        this.setIconImage(icon);

        /**
         * Creating Database jewelry_db if not exists and executing
         * 'jewelry_db.sql' file
         */
        initComponents();

        lblProgressText.setForeground(Color.YELLOW);
        pbLoadingProgress.setForeground(LIGHT_ORANGE);

        timer = new Timer(50, (e) -> {

            if (pbValue > 100) {
                timer.stop();

                SwingUtilities.invokeLater(() -> {
                    new listOfAllCompany().setVisible(true);
                });

                this.dispose();
            }

            if (pbValue == 0) {

                createTableCompany();

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 1) {

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 2) {

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 3) {

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 4) {
                createTablesettings();

                createTablestates();

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 5) {

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 6) {

                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 7) {
                try {

                    insertsettings();
                    loadBackgroundImage();

                    lblProgressText.setText(progressTexts[progressTextsIndex]);
                    progressTextsIndex += 1;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(RedesignedLauncher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pbValue == 8) {

                insertstates();
                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }
            if (pbValue == 9) {
                createTableusers();
                lblProgressText.setText(progressTexts[progressTextsIndex]);
                progressTextsIndex += 1;
            }

            pbLoadingProgress.setValue(pbValue);

            pbValue += 1;

        });

        timer.start();

    }

    private void createTableusers() {
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `users` (
                           `userid` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `name` varchar(100) NOT NULL,
                           `type` varchar(50) NOT NULL,
                           `username` varchar(50) NOT NULL,
                           `password` varchar(50) NOT NULL
                         ) """;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableCompany() {
        try {
            Connection con = DBConnect.connectCopy();
            if(con == null){
                 System.exit(0);
            }
            Statement stmt = con.createStatement();
            
            String sql = """
                         CREATE TABLE IF NOT EXISTS `company` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `username` varchar(100) NOT NULL,
                           `password` varchar(100) NOT NULL,
                           `companyname` varchar(100)  NOT NULL,
                           `address1` varchar(100)  ,
                           `address2` varchar(100)  ,
                           `city` varchar(20)  ,
                           `pincode` varchar(20)  ,
                           `state` varchar(20)  NOT NULL,
                           `state_code` varchar(20) NOT NULL,
                           `district` varchar(20)  ,
                           `email` varchar(70) ,
                           `website` varchar(70) ,
                           `phone` varchar(100) ,
                           `gstno` varchar(70) ,
                           `vatno` varchar(100) ,
                           `startfinancialyear` date NOT NULL,
                           `endfinancialyear` date NOT NULL,
                           `dlno1` int ,
                           `dlno2` int ,
                           `dealsin` varchar(50)  ,
                           `taxsystem` varchar(50)  
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            
            System.out.println(e);
            
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablesettings() {
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            String sql = """
                        CREATE TABLE IF NOT EXISTS `settings` (
                           `slno` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `Title` varchar(100) NOT NULL,
                           `file_name` varchar(100) NOT NULL
                         ) """;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablestates() {
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `states` (
                           `state_name` varchar(100) NOT NULL,
                           `state_code` int NOT NULL PRIMARY KEY
                         ) """;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static void insertsettings() {
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select slno from settings";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                           INSERT INTO `settings` (`slno`, `Title`, `file_name`) VALUES
                            (1, 'launcher', 'launchericon.jpg'),
                            (2, 'Login', 'loginicon.jpg'),
                            (3, 'Title', 'Jewelry Software Dashboard') """;

                stmt.executeUpdate(q2);

            }
            Checkstmt.close();
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static void insertstates() {
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select state_name from states";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                            INSERT INTO `states` (`state_name`, `state_code`) VALUES
                            ('Jammu and Kashmir', 1),
                            ('Himachal Pradesh', 2),
                            ('PUNJAB', 3),
                            ('CHANDIGARH', 4),
                            ('UTTARAKHAND', 5),
                            ('HARYANA', 6),
                            ('DELHI', 7),
                            ('RAJASTHAN', 8),
                            ('UTTAR PRADESH', 9),
                            ('BIHAR', 10),
                            ('SIKKIM', 11),
                            ('ARUNACHAL PRADESH', 12),
                            ('NAGALAND', 13),
                            ('MANIPUR', 14),
                            ('MIZORAM', 15),
                            ('TRIPURA', 16),
                            ('MEGHALAYA', 17),
                            ('ASSAM', 18),
                            ('WEST BENGAL', 19),
                            ('JHARKHAND', 20),
                            ('ODISHA', 21),
                            ('CHATTISGARH', 22),
                            ('MADHYA PRADESH', 23),
                            ('GUJARAT', 24),
                            ('DADRA AND NAGAR HAVELI AND DAMAN AND DIU (NEWLY MERGED UT)', 26),
                            ('MAHARASHTRA', 27),
                            ('ANDHRA PRADESH', 28),
                            ('KARNATAKA', 29),
                            ('GOA', 30),
                            ('LAKSHADWEEP', 31),
                            ('KERALA', 32),
                            ('TAMIL NADU', 33),
                            ('PUDUCHERRY', 34),
                            ('ANDAMAN AND NICOBAR ISLANDS', 35),
                            ('TELANGANA', 36),
                            ('ANDHRA PRADESH', 37),
                            ('LADAKH', 38) """;

                stmt.executeUpdate(q2);

            }
            Checkstmt.close();
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void loadBackgroundImage() throws FileNotFoundException {

        String loadingimage = null;
        URL f = null;
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DBConnect.connectCopy();  // DriverManager.getConnection("jdbc:mysql://localhost:3306/jewelry_db","root","");
            Statement s = c.createStatement();
            ResultSet rs1 = s.executeQuery("Select * from settings where slno=1;");
            while (rs1.next()) {
                String title = rs1.getString("file_name");
                loadingimage = System.getProperty("user.dir") + "/Images/" + "launchericon.jpg";
                Logger.getLogger(RedesignedLauncher.class.getName()).log(Level.INFO, System.getProperty("user.dir") + "/Images/" + "launchericon.jpg");

                //  loadingimage = "C:\\Program Files\\Jewelry Setup\\Images\\" + title;
                //  f = new File(loadingimage).toURI().toURL();
                File imagefile = new File(loadingimage);
                f = imagefile.toURI().toURL();
            }
        } catch (SQLException | MalformedURLException ex) {
            Logger.getLogger(RedesignedLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }

        Logger.getLogger(RedesignedLauncher.class.getName()).info(System.getProperty("user.dir") + "/Images/" + "launchericon.jpg");
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(f).getImage().getScaledInstance(lblBackgroundImage.getWidth(),
                lblBackgroundImage.getHeight(), Image.SCALE_SMOOTH));
        lblBackgroundImage.setIcon(imageIcon);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pbLoadingProgress = new javax.swing.JProgressBar();
        lblProgressText = new javax.swing.JLabel();
        lblBackgroundImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(pbLoadingProgress, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 630, 10));

        lblProgressText.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        lblProgressText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblProgressText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 610, 40));

        lblBackgroundImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblBackgroundImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 630, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblBackgroundImage;
    private javax.swing.JLabel lblProgressText;
    private javax.swing.JProgressBar pbLoadingProgress;
    // End of variables declaration//GEN-END:variables
}
