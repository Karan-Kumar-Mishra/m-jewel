package jewellery;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SelectAndCreateCompanyScreen extends javax.swing.JFrame {

    private final DefaultTableModel tableModel;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private ImageIcon image;
    private ImageIcon imageIcon;
    private List<List<Object>> companiesData = new ArrayList<>();
    private static int selectedRow = -1;
    private static int selectedColumn = -1;
    private final JFileChooser fileChooser = new JFileChooser();
    private String selectedFile = "";

    public static String selectedCompany = "";

    public SelectAndCreateCompanyScreen() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpg");
        this.setIconImage(icon);

        tableModel = (DefaultTableModel) tblCompaniesList.getModel();

        initCloseButtonEventCode();
        lblCurrentDate.setText("DATE: " + getCurrentDate("yyyy-MM-dd"));
        centerTableCells();

        setImageOnButton(AssetsLocations.FOLDER_IMAGE, btnLoadFile);
        setImageOnButton(AssetsLocations.PENCIL_IMAGE, btnEdit);
        setImageOnButton(AssetsLocations.CIRCULAR_ARROW, btnRefresh);

        SwingUtilities.invokeLater(() -> {
            try {
                fetchCompaniesDataFromDatabase();
            } catch (ParseException ex) {
                Logger.getLogger(SelectAndCreateCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        setupCustomEnterKeyEventOnTable();
    }
    static Date dateFY;
    private static final SimpleDateFormat DATEFORMAT
            = new SimpleDateFormat("yyyy-MM-dd");

    private void fetchCompaniesDataFromDatabase() throws ParseException {
        // System.out.println("hello");
        int ctr = 0;

        companiesData = DBController.getDataFromcompanyTable("SELECT companyname, "
                + "city, startfinancialyear FROM company where companyname='" + GLOBAL_VARS.SELECTED_COMPANY + "'");

        companiesData.forEach((companies) -> {
            //ctr++;
            tableModel.addRow(new Object[]{
                companies.get(0), // Company name
                companies.get(1), // Company city,
                companies.get(2) // Company FY from
            });
        });
        // dateFY = DATEFORMAT.parse( companiesData.get(0).get(2).toString());
        // System.out.println(dateFY);

        btnAddNewCompany.setVisible(true);
//            jLabel1.setVisible(false);

    }

    private void setupCustomEnterKeyEventOnTable() {
        String solve = "solve";

        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        tblCompaniesList.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKeyStroke, solve);
        tblCompaniesList.getActionMap().put(solve, new EnterAction());
    }

    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(SelectAndCreateCompanyScreen.this,
                        "Are you sure you want to close this window?", "Close Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    SelectAndCreateCompanyScreen.this.dispose();
                    System.exit(0);
                }
            }
        });
    }

    private String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();

        return dateTimeFormatter.format(localDateTime);
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblCompaniesList
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setImageOnButton(String resourceLocation, JButton button) {
        image = new ImageIcon(getClass().getResource(resourceLocation));
        imageIcon = new ImageIcon(image
                .getImage()
                .getScaledInstance(button.getWidth() - 5,
                        button.getHeight() - 5, Image.SCALE_SMOOTH));
        button.setIcon(imageIcon);
    }

    private String selectFile() {
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Set to select directories only
    fileChooser.setAcceptAllFileFilterUsed(false); // Optional: Hide the "All files" filter

    int returnVal = fileChooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
        return fileChooser.getSelectedFile().getAbsolutePath();
    }
    return null;
}


    class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            GLOBAL_VARS.SELECTED_COMPANY = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 0).toString();
            GLOBAL_VARS.SELECTED_COMPANY = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 2).toString();
            EventQueue.invokeLater(() -> {
                createTableAccount();
                createTableExchange();
                createTableLicence();
                createTableOutstandinganalysis();
                createTablePayment();
                createTablecashpurchasedetails();
                createTabledailyupdate();
                createTableentryitem();
                createTableitemslist();
                createTableledger();
                createTablepurchaseHistory();
                createTablepurchasebill();
                createTablereceipt();
                createTablesalebillnocounter();
                createTablesales();
                createTablesalesbill();
                createTableHidingFields();
                createTabletagcounter();
                createTabletagprinting();
                createTableAccountGroup();
                createTableBankledger();
                createTableBillCounter();
                createTableCash();
                insertAccountGroup();
                InsertTableHidingFields();
                insertBillCount();
                insertsalebillnocounter();
                inserttagcounter();
                insertInCash();
                new LoginPageRedesigned().setVisible(true);

            });
            SelectAndCreateCompanyScreen.this.dispose();
        }

    }

    class GradientColor extends javax.swing.JPanel {

        @Override
        protected void paintComponent(Graphics grphcs) {
            super.paintComponent(grphcs);
            Graphics2D g2d = (Graphics2D) grphcs;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int width = getWidth();
            int height = getHeight();

            Color color1 = new Color(236, 161, 40);
            Color color2 = new Color(112, 111, 109);

            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }

    }

    private static void inserttagcounter() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select tag_counter_id from tagcounter";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                            INSERT INTO `tagcounter` (`tag_counter_id`, `tag_counter`) VALUES
                            (1, 1)""";

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

    private void insertInCash() {
        System.out.println("auto Fill Cash Account If Not Exist");
         
        try {
            int result = 0;
            List<List<Object>> companyState = DBController.getDataFromTableforcompany("SELECT state FROM company "
                + "WHERE companyname = '"
                + GLOBAL_VARS.SELECTED_COMPANY + "';");
            List<List<Object>> companyStatecode = DBController.getDataFromTableforcompany("SELECT state_code FROM company "
                + "WHERE companyname = '"
                + GLOBAL_VARS.SELECTED_COMPANY + "';");
            
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();

            String query = "select accountname from account";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {
            String companyStateCode;
            companyStateCode = (String) companyStatecode.get(0).get(0);
            String companyStatename;
            companyStatename = (String) companyState.get(0).get(0);
            
                String q2 = "insert into account"
                        + "(accountname,printname,grp,state,statecode,dr_cr,dueamt,amt) values "
                        + "('Cash','Cash','Cash','" + companyStatename + "','" + companyStateCode + "','0',0,0),"
                        + "('Bank Account','Bank Account','Current Assets','India','0','0',0,0),"
                        + "('Bank O/D Account','Bank O/D Account','Loans (Liability)','India','0','0',0,0),"
                        + "('Capital Account','Capital Account','','India','0','0',0,0),"
                        + "('Cash In Hand','Cash In Hand','Current Assets','India','0','0',0,0),"
                        + "('Current Assets','Current Assets','','India','0','0',0,0),"
                        + "('Current Liabilities','Current Liabilities','','India','0','0',0,0),"
                        + "('Duties & Texes','Duties & Texes','Current Liabilities','India','0','0',0,0),"
                        + "('Expanses (Direct/Mfg)','Expanses (Direct/Mfg)','Revenue Accounts','India','0','0',0,0),"
                        + "('Expenses (Indirect/Admn)','Expenses (Indirect/Admn)','Revenue Accounts','India','0','0',0,0),"
                        + "('Fixed Assets','Fixed Assets','','India','0','0',0,0),"
                        + "('Income (Direct/Opr)','Income (Direct/Opr)','Revenue Accounts','India','0','0',0,0),"
                        + "('Income (Indirect)','Income (Indirect)','Revenue Accounts','India','0','0',0,0),"
                        + "('Investments','Investments','','India','0','0',0,0),"
                        + "('Loans & Advances (Asset)','Loans & Advances (Asset)','Current Assets','India','0','0',0,0),"
                        + "('Labour Charges','Labour Charges','','India','0','0',0,0),"
                        + "('Pre-Operative Expenses','Pre-Operative Expenses','','India','0','0',0,0),"
                        + "('Profit & Loss','Profit & Loss','','India','0','0',0,0),"
                        + "('Provision/Expenses Payable','Provision/Expenses Payable','Current Liabilities','India','0','0',0,0),"
                        + "('Purchase','Purchase','Revenue Accounts','India','0','0',0,0),"
                        + "('Reserves & Surplus','Reserves & Surplus','Capital Account','India','0','0',0,0),"
                        + "('Revenue Accounts','Revenue Accounts','','India','0','0',0,0),"
                        + "('Sale','Sale','Revenue Accounts','India','0','0',0,0),"
                        + "('Secured Loans','Secured Loans','Loans (Liability)','India','0','0',0,0),"
                        + "('Securities & Deposits (Asset)','Securities & Deposits (Asset)','Current Assets','India','0','0',0,0),"
                        + "('Stock-in-hand','Stock-in-hand','Current Assets','India','0','0',0,0),"
                        + "('SU/Sundry Creditor','SU/Sundry Creditor','Current Liabilities','India','0','0',0,0),"
                        + "('CU/Sundry Debitor','CU/Sundry Debitor','','India','0','0',0,0),"
                        + "('Suspence Account','Suspence Account','Current Assets','India','0','0',0,0),"
                        + "('Unsecured Loans','Unsecured Loans','Loans (Liability)','India','0','0',0,0)";

                if (result == 0) {
                    stmt.executeUpdate(q2);
                }
            }
            Checkstmt.close();
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
             e.printStackTrace(); // Prints the full stack trace for debugging
             JOptionPane.showMessageDialog(null, "Error while filling Account: " + e.getMessage());
        }
    }

    private void createTableAccount() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS account (id int NOT NULL AUTO_INCREMENT PRIMARY KEY ,accountname varchar(50)  NOT NULL,printname varchar(50)  NOT NULL,grp varchar(20)  NOT NULL,opbal float ,address varchar(50)  ,city varchar(50)  ,pincode varchar(20)  ,mobile1 varchar(100) ,mobile2 varchar(11) ,email varchar(50)  ,birthday date ,gstno varchar(15) ,adharno varchar(12) ,vatno varchar(11) ,panno varchar(50) ,state varchar(50)  NOT NULL,statecode varchar(5)  NOT NULL,anniversary date ,dr_cr varchar(30)  NOT NULL,supplier_as_customer bit DEFAULT 0,area varchar(30)  ,dueamt double NOT NULL DEFAULT 0.0,amt double NOT NULL DEFAULT 0.0,ifsc varchar(50) ,accountnumber varchar(100),ClosingBalance varchar(250) )";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableAccountGroup() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `accountgroup` (
                           `account_group` varchar(70) PRIMARY KEY NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableBankledger() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `bankledger` (
                           `rno` int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                           `name` varchar(30) NOT NULL,
                           `bankname` varchar(20) NOT NULL,
                           `date` date NOT NULL,
                           `amt` double NOT NULL,
                           `remarks` varchar(100) NOT NULL,
                           `type` varchar(20) NOT NULL,
                         `sales_Bill` varchar(255) NOT NULL DEFAULT '-1'
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableBillCounter() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `billnocounter` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `billno` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableSystemName() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `Systemname` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `Systemname` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableCash() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `cash` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `bill` int NOT NULL,
                           `date` date NOT NULL,
                           `name` varchar(50) NOT NULL,
                           `address` varchar(100) NOT NULL,
                           `phone` varchar(10) NOT NULL,
                           `itemname` varchar(50) NOT NULL,
                           `huid` varchar(20) NOT NULL,
                           `netwt` double NOT NULL,
                           `bankamt` double NOT NULL,
                           `labour` double NOT NULL,
                           `netamount` double NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablecashpurchasedetails() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `cashpurchasedetails` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `name` varchar(100)  NULL,
                           `address` varchar(100)  NULL,
                           `adhar_no` varchar(30)  NULL,
                           `pan_no` varchar(30)  NULL,
                           `contact_no` varchar(20)  NULL,
                           `bill_no` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTabledailyupdate() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `dailyupdate` (
                           `date` date NOT NULL,
                           `gold16` int NOT NULL,
                           `gold18` varchar(100) NOT NULL,
                           `gold22` varchar(100) NOT NULL,
                           `gold24` varchar(100) NOT NULL,
                           `silver` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableentryitem() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `entryitem` (
                           `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                           `itemname` varchar(50)  NOT NULL,
                           `itemprefix` varchar(50)  ,
                           `itemgroup` varchar(50)  NOT NULL,
                           `taxslab` varchar(50)  NOT NULL,
                           `hsncode` varchar(11) ,
                           `shortname` varchar(50)  ,
                           `tagno` varchar(30) ,
                           `huid` varchar(40) ,
                           `grosswt` float ,
                           `beedswt` float ,
                           `netwt` float ,
                           `diamondwt` float ,
                           `carats` float ,
                           `polishpercent` float ,
                           `opqty` varchar(100) ,
                           `op_qty_type` varchar(7) ,
                           `itemimage` varchar(260) ,
                           `item_sold` int DEFAULT 0
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableExchange() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                        CREATE TABLE IF NOT EXISTS `exchange` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `ItemName` varchar(100) NOT NULL,
                           `grosswt` float NOT NULL,
                           `fine` float NOT NULL,
                           `netwt` float NOT NULL,
                           `rate` float NOT NULL,
                           `total` float NOT NULL,
                           `bill` int NOT NULL,
                           `material` varchar(50) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableitemslist() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `itemslist` (
                           `itemname` varchar(50)  NOT NULL,
                           `grp` varchar(50)  NOT NULL,
                           `hsncode` int NOT NULL,
                           `taxslab` varchar(50)  NOT NULL,
                           `count` int NOT NULL,
                           `opstockqty` float NOT NULL,
                           `opstockwt` float NOT NULL,
                           `opstockvalue` varchar(50)  NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableledger() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                        CREATE TABLE IF NOT EXISTS `ledger` (
                           `accountid` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `city` varchar(30)  NOT NULL,
                           `closingbalance` float NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableLicence() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                       CREATE TABLE IF NOT EXISTS `licence` (
                           `license_id` varchar(20) NOT NULL,
                           `license_key` varchar(50) NOT NULL,
                           `plan` int NOT NULL,
                           `period` varchar(255) NOT NULL,
                           `active_from` timestamp NULL ,
                           `active_till` timestamp NULL 
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableOutstandinganalysis() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `outstandinganalysis` (
                           `accounthead` varchar(50)  NOT NULL,
                           `city` varchar(50)  NOT NULL,
                           `mobile` int NOT NULL,
                           `balancevalue` float NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablePayment() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `payments` (
                           `Receiptno` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `Name` varchar(30) NOT NULL,
                           `date` date NOT NULL,
                           `discount` double NOT NULL,
                           `amtpaid` double NOT NULL,
                           `mop` varchar(20) NOT NULL,
                           `remarks` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablepurchasebill() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `purchasebill` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `date` date NOT NULL,
                           `bill` varchar(70) ,
                           `partyname` varchar(50)  NOT NULL,
                           `itemname` varchar(100)  NOT NULL,
                           `qty` int,
                           `grosswt` double ,
                           `beedswt` double ,
                           `price` varchar(100) ,
                           `bankamt` double ,
                           `discountpercent` double ,
                           `net_amount` varchar(100) 
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablepurchaseHistory() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `purchasehistory` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `date` date NOT NULL,
                           `terms` varchar(50)  NOT NULL,
                           `partyname` varchar(50)  NOT NULL,
                           `bill` varchar(70) ,
                           `gst` varchar(100) ,
                           `balance` varchar(20) ,
                           `itemname` varchar(100)  NOT NULL,
                           `qty` int ,
                           `beedswt` double ,
                           `netwt` double ,
                           `diamondwt` double ,
                           `diamondrate` double ,
                           `grosswt` double ,
                           `itemdescription` varchar(250)  ,
                           `price` varchar(100) ,
                           `taxable_amt` int,
                           `gst_percent` float ,
                           `net_amount` varchar(100) ,
                           `extrachange` double ,
                           `bankamt` double ,
                           `discountpercent` double ,
                           `discountamount` double ,
                           `huid` varchar(100) NOT NULL,
                           `tagnoItems` varchar(255) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablereceipt() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `receipt` (id int not null primary key AUTO_INCREMENT,
                           `Receiptno` int NOT NULL  default -1,
                           `Name` varchar(30) NOT NULL,
                           `date` date NOT NULL,
                           `discount` double NOT NULL,
                           `amtpaid` double NOT NULL,
                           `mop` varchar(20) NOT NULL,
                           `remarks` varchar(100) NOT NULL,
                         `sales_Bill` varchar(255) NOT NULL DEFAULT '-1'
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablesalebillnocounter() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `salebillnocounter` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `billno` int NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablesales() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `sales` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `date` date NOT NULL,
                           `terms` varchar(50) NOT NULL,
                           `partyname` varchar(50)  NOT NULL,
                           `bill` int,
                           `gst` varchar(100) NOT NULL,
                           `previous_balance` varchar(100) NOT NULL,
                           `itemname` varchar(100) NOT NULL,
                           `huid` varchar(50) NOT NULL,
                           `qty` varchar(100) ,
                           `address` varchar(255) ,
                           `beedswt` double NOT NULL,
                           `netwt` double ,
                           `diamondwt` double ,
                           `diamondrate` double ,
                           `grosswt` double ,
                           `itemdescription` varchar(250) ,
                           `lbr_per` varchar(50)  ,
                           `extrachange` varchar(255) ,
                           `bankamt` double ,
                           `netamount` double ,
                           `receivedamount` double ,
                           `rate` varchar(100) ,
                           `taxable_amount` varchar(100) NOT NULL,
                           `gstpercent` float NOT NULL,
                           `gstamt` double NOT NULL,
                           `labour` varchar(100) NOT NULL,
                           `labour_amt_discount` float NOT NULL,
                           `Discount` float ,
                           `tagno` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTablesalesbill() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `salesbill` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `date` date NOT NULL,
                           `partyname` varchar(50)  NOT NULL,
                           `bill` int ,
                           `gst` varchar(100) NOT NULL,
                           `qty` varchar(100) ,
                           `netwt` double ,
                           `diamondwt` double ,
                           `diamondrate` double ,
                           `netamount` double ,
                           `taxable_amount` varchar(100) NOT NULL,
                           `gstamt` double NOT NULL,
                           `labour` varchar(100) NOT NULL,
                           `labour_amt_discount` float NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableHidingFields() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                        CREATE TABLE IF NOT EXISTS `hidingFields` (
                           `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `tagno` varchar(10) NOT NULL Default 'false'
                          ) """;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void InsertTableHidingFields() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();

            String query = "select id from hidingFields";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = "insert into hidingFields(tagno) values('True') ";

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

    private void createTabletagcounter() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                       CREATE TABLE IF NOT EXISTS `tagcounter` (
                           `tag_counter_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `tag_counter` varchar(100) NOT NULL
                         )""";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTabletagprinting() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = """
                         CREATE TABLE IF NOT EXISTS `tagprinting` (
                           `itemid` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           `item_prefix` varchar(70) ,
                           `tagno` varchar(100) ,
                           `huid` varchar(100) NOT NULL
                         ) """;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static void insertAccountGroup() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select account_group from accountgroup";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                            INSERT INTO `accountgroup` (`account_group`) VALUES
                            ('Bank'),
                            ('Customer'),
                            ('Supplier')
                            ,('Bank O/D Account')
                            ,('Capital Account')
                            ,('Cash In Hand')
                            ,('Current Assets')
                            ,('Current Liabilities')
                            ,('Duties & Texes')
                            ,('Expanses (Direct/Mfg)')
                            ,('Expenses (Indirect/Admn)')
                            ,('Fixed Assets')
                            ,('Income (Direct/Opr)')
                            ,('Income (Indirect)')
                            ,('Investments')
                            ,('Loans & Advances (Asset)')
                            ,('Loans')
                            ,('Labour Charges')
                            ,('Pre-Operative Expenses')
                            ,('Profit & Loss')
                            ,('Provision/Expenses Payable')
                            ,('Purchase')
                            ,('Reserves & Surplus')
                            ,('Revenue Accounts')
                            ,('Sale')
                            ,('Secured Loans')
                            ,('Securities & Deposits (Asset)')
                            ,('Stock-in-hand')
                            ,('SU/Sundry Creditor')
                            ,('CU/Sundry Debitor')
                            ,('Suspence Account')
                            ,('Unsecured Loans')""";

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

    private static void insertBillCount() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select id from billnocounter";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                           INSERT INTO `billnocounter` (`id`, `billno`) VALUES
                            (1, 1) """;

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

    private static void insertsalebillnocounter() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Statement Checkstmt = con.createStatement();
            String query = "select id from salebillnocounter";
            ResultSet re = Checkstmt.executeQuery(query);
            if (!re.next()) {

                String q2 = """
                            INSERT INTO `salebillnocounter` (`id`, `billno`) VALUES
                            (1, 1); """;

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

    private static void updatetable() {
        
        Connection con = DBConnect.connect();
        
        String q3 = "UPDATE account SET state = ?, statecode = ? WHERE accountname = ?";

        try {
            List<List<Object>> companyState = DBController.getDataFromTableforcompany("SELECT state FROM company "
                + "WHERE companyname = '"
                + GLOBAL_VARS.SELECTED_COMPANY + "';");
            List<List<Object>> companyStatecode = DBController.getDataFromTableforcompany("SELECT state_code FROM company "
                + "WHERE companyname = '"
                + GLOBAL_VARS.SELECTED_COMPANY + "';");
            
            
            String companyStateCode;
            companyStateCode = (String) companyStatecode.get(0).get(0);
            String companyStatename;
            companyStatename = (String) companyState.get(0).get(0);
            
            
            PreparedStatement pstmt = con.prepareStatement(q3);

            // Setting the parameters for the UPDATE query
            pstmt.setString(1, companyStatename);   // Set state to '0'
            pstmt.setString(2, companyStateCode);   // Set statecode to '0'
            pstmt.setString(3, "Cash"); // Update where accountname = 'Cash'

            // Execute the update statement
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsAffected);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for debugging
        }


    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new GradientColor();
        lblCurrentDate = new javax.swing.JLabel();
        btnAddNewCompany = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtFilePath = new javax.swing.JTextField();
        btnLoadFile = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompaniesList = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("View and Create Company");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        lblCurrentDate.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblCurrentDate.setForeground(new java.awt.Color(255, 255, 255));
        lblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentDate.setText("Current Date");

        btnAddNewCompany.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnAddNewCompany.setText("Split Company");
        btnAddNewCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewCompanyActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("File Path:");

        btnLoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadFileActionPerformed(evt);
            }
        });

        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        tblCompaniesList.setFont(new java.awt.Font("Bookman Old Style", 1, 16)); // NOI18N
        tblCompaniesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Company Name", "City", "FY From"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompaniesList.setRowHeight(25);
        tblCompaniesList.getTableHeader().setReorderingAllowed(false);
        tblCompaniesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCompaniesListMouseClicked(evt);
            }
        });
        tblCompaniesList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCompaniesListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompaniesList);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/jewelry (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(309, 309, 309)
                .addComponent(lblCurrentDate, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLoadFile, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddNewCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCurrentDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(btnAddNewCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFilePath, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblCompaniesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCompaniesListMouseClicked
        selectedRow = tblCompaniesList.getSelectedRow();
        selectedColumn = tblCompaniesList.getSelectedColumn();

        // If the mouse is double clicked on any row, only then proceed
        if (evt.getClickCount() == 2 && tblCompaniesList.getSelectedRow() != -1) {
            GLOBAL_VARS.SELECTED_COMPANY = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 0).toString();
            GLOBAL_VARS.SELECTED_COMPANY_FYYEAR = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 2).toString();
            EventQueue.invokeLater(() -> {
                createTableSystemName();
                createTableAccount();
                createTableExchange();
                createTableLicence();
                createTableOutstandinganalysis();
                createTablePayment();
                createTablecashpurchasedetails();
                createTabledailyupdate();
                createTableentryitem();
                createTableitemslist();
                createTableledger();
                createTablepurchaseHistory();
                createTablepurchasebill();
                createTablereceipt();
                createTablesalebillnocounter();
                createTablesales();
                createTablesalesbill();
                createTableHidingFields();
                createTabletagcounter();
                createTabletagprinting();
                createTableAccountGroup();
                createTableBankledger();
                createTableBillCounter();
                createTableCash();
                insertAccountGroup();
                InsertTableHidingFields();
                insertBillCount();
                insertsalebillnocounter();
                inserttagcounter();
                insertInCash();
                updatetable();
                
                new LoginPageRedesigned().setVisible(true);

            });
            this.dispose();
        } else {
            selectedRow = tblCompaniesList.getSelectedRow();
            selectedColumn = tblCompaniesList.getSelectedColumn();
        }
    }//GEN-LAST:event_tblCompaniesListMouseClicked

    private void btnAddNewCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewCompanyActionPerformed
        new CreateNewCompanyScreen().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAddNewCompanyActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        tableModel.setRowCount(0);
        tblCompaniesList.repaint();

        SwingUtilities.invokeLater(() -> {
            try {
                fetchCompaniesDataFromDatabase();
            } catch (ParseException ex) {
                Logger.getLogger(SelectAndCreateCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (selectedRow != -1) {
            GLOBAL_VARS.SELECTED_COMPANY
                    = tblCompaniesList.getValueAt(selectedRow, 0).toString();
            GLOBAL_VARS.SELECTED_COMPANY_FYYEAR
                    = tblCompaniesList.getValueAt(selectedRow, 2).toString();

            new EditCompanyDetailsScreen().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a company to edit");
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnLoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadFileActionPerformed
        selectedFile = selectFile();
        txtFilePath.setText(selectedFile);
    }//GEN-LAST:event_btnLoadFileActionPerformed

    private void tblCompaniesListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCompaniesListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedRow = tblCompaniesList.getSelectedRow();
        }
    }//GEN-LAST:event_tblCompaniesListKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewCompany;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLoadFile;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCurrentDate;
    private javax.swing.JTable tblCompaniesList;
    private javax.swing.JTextField txtFilePath;
    // End of variables declaration//GEN-END:variables
}
