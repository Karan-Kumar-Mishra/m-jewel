package jewellery;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.Date;
import jewellery.helper.dateformatehelper;
import java.sql.Connection;
import java.sql.Statement;
import jewellery.helper.outstandingAnalysisHelper;

public class CreateNewCompanyScreen extends javax.swing.JFrame {

    private ImageIcon image;

    private final String tableName = DatabaseCredentials.COMPANY_TABLE;
    private List<Object> columnNames = new ArrayList<>();
    private final List<Object> data = new ArrayList<>();
    private List<List<Object>> states = new ArrayList<>();

    private SimpleDateFormat dateFormat;

    private LocalDate fyStartDate;
    private LocalDate fyEndDate;

    public CreateNewCompanyScreen() {
        initComponents();
        if (GLOBAL_VARS.SELECTED_COMPANY != null  ) {
            if(!GLOBAL_VARS.SELECTED_COMPANY.trim().isEmpty()){
                 txtCompanyName.setText(GLOBAL_VARS.SELECTED_COMPANY);
        }  
            }
         
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpeg");
        this.setIconImage(icon);

        setImageOnComponent(lblBulbImage, "/assets/lightBulbImage.png");

        if (GLOBAL_VARS.IS_SCREEN_INSIDE_TABBED_PANE) {
            btnClose.setEnabled(false);
        }

        initCloseButtonEventCode();

        setDateOnJCalenders("yyyy-MM-dd");

        fetchStates();
    }

    public static void main(String[] args) {
        CreateNewCompanyScreen nc = new CreateNewCompanyScreen();

    }

    private void setFocus(KeyEvent event, JComponent component) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            component.requestFocusInWindow();
        }
    }

    private void emptyTextFields() {
        Component[] components = this.pnlParent.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }
    }

    private void fetchStates() {

        states = DBController.getDataFromcompanyTable("SELECT * FROM "
                + DatabaseCredentials.STATES_TABLE);
    }

    public void autoComplete(List<List<Object>> autoCompletionSource,
            String textToAutoComplete, JTextField txtField) {
        String complete = "";
        int start = textToAutoComplete.length();
        int last = textToAutoComplete.length();

        for (int idx = 0; idx < autoCompletionSource.size(); idx++) {

            if (autoCompletionSource.get(idx).get(0).toString().toUpperCase().startsWith(textToAutoComplete.toUpperCase())) {
                complete = autoCompletionSource.get(idx).get(0).toString().toUpperCase();
                last = complete.length();
                break;
            }
        }
        if (last > start) {
            txtField.setText(complete);
            txtField.setCaretPosition(last);
            txtField.moveCaretPosition(start);
        }
    }

    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(CreateNewCompanyScreen.this,
                        "Are you sure you want to exit the create company screen?", "Close Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    DashBoardScreen.isCreateCompanyWindowOpen = false;
                    CreateNewCompanyScreen.this.dispose();
                }
            }
        });
    }

    private void setDateOnJCalenders(String pattern) {
        dateFormat = new SimpleDateFormat(pattern);

        fyStartDate = LocalDate.now();

        try {

            String startYear = String.valueOf(fyStartDate.getYear() + "-"
                    + "4" + "-" + "1");
            dateFYStart.setDate(dateFormat.parse(startYear));

            fyEndDate = fyStartDate.plusMonths(12);

            String endYear = String.valueOf(fyEndDate.getYear() + "-"
                    + "3" + "-" + "31");
            dateFYClose.setDate(dateFormat.parse(endYear));

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void setImageOnComponent(javax.swing.JLabel component, String resourceLocation) {
        image = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        component.setIcon(image);
    }

    private boolean validateFields() {
        if (UtilityMethods.isTextFieldEmpty(txtCompanyName)) {
            JOptionPane.showMessageDialog(this, "Please enter your company name");
            return false;
        } else if (UtilityMethods.isTextFieldEmpty(txtState)) {
            JOptionPane.showMessageDialog(this, "Please enter your state");
            return false;
        } else if (!UtilityMethods.hasDateBeenPicked(dateFYStart)) {
            JOptionPane.showMessageDialog(this, "Please select financial year start date");
            return false;
        } else if (!UtilityMethods.hasDateBeenPicked(dateFYClose)) {
            JOptionPane.showMessageDialog(this, "Please select financial year end date");
            return false;
        }

        return true;
    }

    private void createTableAccount(String companyname, String fy) {
        try {
            Connection con = DBConnect.maxDatabaseConnection(companyname, fy);
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS account (id int NOT NULL AUTO_INCREMENT PRIMARY KEY ,accountname varchar(50)  NOT NULL,printname varchar(50)  NOT NULL,grp varchar(20)  NOT NULL,opbal float ,address varchar(50)  ,city varchar(50)  ,pincode varchar(20)  ,mobile1 varchar(100) ,mobile2 varchar(11) ,email varchar(50)  ,birthday varchar(30) ,gstno varchar(15) ,adharno varchar(11) ,vatno varchar(11) ,panno varchar(50) ,state varchar(30)  NOT NULL,statecode varchar(5)  NOT NULL,anniversary varchar(30) ,dr_cr varchar(30)  NOT NULL,supplier_as_customer bit DEFAULT 0,area varchar(30)  ,dueamt double NOT NULL DEFAULT 0.0,amt double NOT NULL DEFAULT 0.0,ifsc varchar(50) ,accountnumber varchar(100),ClosingBalance varchar(250) )";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void createTableentryitem(String companyname, String fy) {
        try {
            Connection con = DBConnect.maxDatabaseConnection(companyname, fy);
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

    private void insertPreviousClosingBal() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select accountname,id from account";
            ResultSet re = stmt.executeQuery(sql);
            while (re.next()) {
                String balance = outstandingAnalysisHelper.fillTableInDateGivenParty(re.getString(1));
                String sql2 = "update account set ClosingBalance='" + balance + "' where id='" + re.getString(2) + "' ";
                Statement stmt2 = con.createStatement();
                stmt2.execute(sql2);
                stmt2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void insertAccountintonewDataBase(String companyname, String fy) {

        System.out.println("auto Fill Cash Account If Not Exist");
        try {

            Connection con = DBConnect.connect();
            Connection con2 = DBConnect.maxDatabaseConnection(companyname, fy);
            Statement stmt = con2.createStatement();
            Statement Checkstmt = con.createStatement();

            String query = "select * from account ";
            ResultSet re = Checkstmt.executeQuery(query);
            while (re.next()) {

                String q2 = "insert into account(accountname,printname,grp,opbal,address,city,pincode,mobile1,mobile2,email,birthday,gstno,adharno,vatno,panno,state,statecode,anniversary,dr_cr,supplier_as_customer,area,dueamt,amt,ifsc,accountnumber) values('" + (re.getString(2)==null?"":re.getString(2)) + "','" + (re.getString(3)==null?"":re.getString(3)) + "','" + (re.getString(4)==null?"":re.getString(4)) + "','" + (re.getString(27)==null?"":re.getString(27)) + "','" +(re.getString(6)==null?"":re.getString(6)) + "','" + (re.getString(7)==null?"":re.getString(7)) + "','" + (re.getString(8)==null?"":re.getString(8)) + "','" + (re.getString(9)==null?"":re.getString(9)) + "','" + (re.getString(10)==null?"":re.getString(10)) + "','" + (re.getString(11)==null?"":re.getString(11)) + "','" + (re.getString(12)==null?"":re.getString(12)) + "','" + (re.getString(13)==null?"":re.getString(13)) + "','" + (re.getString(14)==null?"":re.getString(14)) + "','" + (re.getString(15)==null?"":re.getString(15)) + "','" + (re.getString(16)==null?"":re.getString(16)) + "','" + (re.getString(17)==null?"":re.getString(17)) + "','" + (re.getString(18)==null?"":re.getString(18)) + "','" + (re.getString(19)==null?"":re.getString(19)) + "','" + (re.getString(20)==null?"":re.getString(20)) + "','" + (re.getString(21)==null?"":re.getString(21)) + "','" +(re.getString(22)==null?"":re.getString(22)) + "','" + (re.getString(23)==null?"":re.getString(23)) + "','" + (re.getString(24)==null?"":re.getString(24)) + "','" + (re.getString(25)==null?"":re.getString(25)) + "','" + (re.getString(26)==null?"":re.getString(26)) + "') ";
                 System.out.println("sql is "+q2);
                stmt.executeUpdate(q2);
               
            }
            Checkstmt.close();
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println( "filling Account" +e);
            JOptionPane.showMessageDialog(null, "filling Account" + e);
        }

    }

    private void insertItemintonewDataBase(String companyname, String fy) {
        try {

            Connection con = DBConnect.connect();
            Connection con2 = DBConnect.maxDatabaseConnection(companyname, fy);
            Statement stmt = con2.createStatement();
            Statement Checkstmt = con.createStatement();

            String query = "select * from entryitem ";
            ResultSet re = Checkstmt.executeQuery(query);
            while (re.next()) {

                String q2 = "insert into entryitem(itemname,itemprefix,itemgroup,taxslab,hsncode,shortname,tagno,huid,grosswt,beedswt,netwt,diamondwt,carats,polishpercent,opqty,op_qty_type,itemimage,item_sold) values('" + (re.getString(2)==null?"":re.getString(2)) + "','" + (re.getString(3)==null?"":re.getString(3)) + "','" + (re.getString(4)==null?"":re.getString(4)) + "','" + (re.getString(5)==null?"":re.getString(5)) + "','" + (re.getString(6)==null?"":re.getString(6)) + "','" + (re.getString(7)==null?"":re.getString(7)) + "','" + (re.getString(8)==null?"":re.getString(8)) + "','" + (re.getString(9)==null?"":re.getString(9)) + "','" + (re.getString(10)==null?"":re.getString(10)) + "'," + (re.getString(11)==null?0.0:re.getFloat(11)) + "," + (re.getString(12)==null?0.0:re.getFloat(12)) + "," + (re.getString(13)==null?0.0:re.getFloat(13)) + "," + (re.getString(14)==null?0.0:re.getFloat(14))+ "," + (re.getString(15)==null?0.0:re.getFloat(15)) + ",'" + (re.getString(16)==null?"":re.getString(16)) + "','" + (re.getString(17)==null?"":re.getString(17)) + "','" + (re.getString(18)==null?"":re.getString(18)) + "'," + re.getInt(19)+ ") ";
                 System.out.println(q2);
                stmt.executeUpdate(q2);
               
            }
            Checkstmt.close();
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("filling item "+e);
            JOptionPane.showMessageDialog(null, "filling item" + e);
        }

    }

    private void closeCompany() {
        try {
            Connection con = DBConnect.connectCopy();
            String sql = "select max(id),companyname,startfinancialyear from company where companyname='" + txtCompanyName.getText().trim() + "'";
            Statement stmt = con.createStatement();
            ResultSet re = stmt.executeQuery(sql);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fystart = String.valueOf(dateFormat.format(dateFYStart.getDate()));

            while (re.next()) {
                if (re.getString(1) != null) {
//                    JOptionPane.showMessageDialog(this,re.getString(1) );
                    GLOBAL_VARS.SELECTED_COMPANY = re.getString(2);
                    GLOBAL_VARS.SELECTED_COMPANY_FYYEAR = re.getString(3);
                    createTableAccount(txtCompanyName.getText().trim(), fystart);
                    createTableentryitem(txtCompanyName.getText().trim(), fystart);
                    insertPreviousClosingBal();
                    insertAccountintonewDataBase(txtCompanyName.getText().trim(), fystart);
                    insertItemintonewDataBase(txtCompanyName.getText().trim(), fystart);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCompany() throws FileNotFoundException {
        columnNames.clear();
        data.clear();

        if (validateFields()) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            columnNames = DBController.getTableColumnNamescompany(DatabaseCredentials.COMPANY_TABLE);
            


            // Remove id from the column names, since it is auto generated
            columnNames.remove(0);

            data.add((txtUsername.getText().trim().isEmpty()) ? "1"
                    : txtUsername.getText().trim());
            data.add((txtPassword.getText().trim().isEmpty()) ? "1"
                    : txtPassword.getText().trim());

            data.add(txtCompanyName.getText().trim());

            if (!txtAddressLine1.getText().trim().isEmpty()) {
                data.add(txtAddressLine1.getText().trim());
            } else {
                columnNames.remove("address1");
            }

            if (!txtAddressLine2.getText().trim().isEmpty()) {
                data.add(txtAddressLine2.getText().trim());
            } else {
                columnNames.remove("address2");
            }

            if (!txtCity.getText().trim().isEmpty()) {
                data.add(txtCity.getText().trim());
            } else {
                columnNames.remove("city");
            }

            if (!txtPincode.getText().trim().isEmpty()) {
                data.add(txtPincode.getText().trim());
            } else {
                columnNames.remove("pincode");
            }

            data.add(txtState.getText().trim());
            data.add(txtStateCode.getText().trim());

            if (!txtDistrict.getText().trim().isEmpty()) {
                data.add(txtDistrict.getText().trim());
            } else {
                columnNames.remove("district");
            }

            if (!txtEmail.getText().trim().isEmpty()) {
                data.add(txtEmail.getText().trim());
            } else {
                columnNames.remove("email");
            }

            if (!txtWebsite.getText().trim().isEmpty()) {
                data.add(txtWebsite.getText().trim());
            } else {
                columnNames.remove("website");
            }

            if (!txtPhone.getText().trim().isEmpty()) {
                data.add(txtPhone.getText().trim());
            } else {
                columnNames.remove("phone");
            }

//            if(!txtEmail.getText().trim().isEmpty()) {
//                data.add(txtEmail.getText().trim());
//            }
//            else {
//                columnNames.remove("website");
//            }
            if (!txtGSTNo.getText().trim().isEmpty()) {
                data.add(txtGSTNo.getText().trim());
            } else {
                columnNames.remove("gstno");
            }

            if (!txtVATNo.getText().trim().isEmpty()) {
                data.add(txtVATNo.getText().trim());
            } else {
                columnNames.remove("vatno");
            }

            data.add(dateFormat.format(dateFYStart.getDate()));
            data.add(dateFormat.format(dateFYClose.getDate()));

            if (!txtDLNo1.getText().trim().isEmpty()) {
                data.add(txtDLNo1.getText().trim());
            } else {
                columnNames.remove("dlno1");
            }

            if (!txtDLNo2.getText().trim().isEmpty()) {
                data.add(txtDLNo2.getText().trim());
            } else {
                columnNames.remove("dlno2");
            }

            if (!txtDealsIn.getText().trim().isEmpty()) {
                data.add(txtDealsIn.getText().trim());
            } else {
                columnNames.remove("dealsin");
            }

            if (!txtTaxSystem.getText().trim().isEmpty()) {
                data.add(txtTaxSystem.getText().trim());
            } else {
                columnNames.remove("taxsystem");
            }

            //data.add(cmbCountrySid.getSelectedItem().toString());
              closeCompany();
            boolean companyIsInserted = DBController.insertDataIntoCompanyTable(tableName,
                    columnNames, data);

            if (companyIsInserted) {
                EventQueue.invokeLater(() -> {
                    emptyTextFields();
                });

                JOptionPane.showMessageDialog(this, "New Company created successfully!!!");
                
                listOfAllCompany lc = new listOfAllCompany();
                lc.setVisible(true);
                dispose();
            
//                closeCompany();
           
            }

            insertintousers();
        }
    }

    void insertintousers() {

        try {
            java.sql.Connection con = DBConnect.connectCopy();
            java.sql.Statement stmt = con.createStatement();
            System.out.print("Entered users \n");
            String u = (txtUsername.getText().trim().isEmpty()) ? "1" : txtUsername.getText().trim();
            String p = (txtPassword.getText().trim().isEmpty()) ? "1" : txtPassword.getText().trim();
            String q1 = "Insert into users(name,type,username,password) values('" + txtCompanyName.getText() + "','" + "Supervisor" + "','" + u + "','" + p + "')";
            stmt.executeUpdate(q1);
            System.out.print("Inserted into users \n");
        } catch (SQLException ex) {
            Logger.getLogger(CreateNewCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void transferDataFromPreviousDataToPresentData() {
        try {
            String sql = "select max(id) ,companyname,startfinancialyear from company where companyname='" + txtCompanyName.getText().trim() + "'";
            java.sql.Connection con = DBConnect.connectCopy();
            java.sql.Statement stmt = con.createStatement();
            String fystart = ((JTextField) dateFYStart.getDateEditor().getUiComponent()).getText().trim();
            Date date = dateformatehelper.it.parse(fystart);
            fystart = dateformatehelper.ot.format(date);
            ResultSet re = stmt.executeQuery(sql);
            if (re.next()) {
                if (re.getString(1) != null) {
                    GLOBAL_VARS.SELECTED_COMPANY = re.getString(2);
                    GLOBAL_VARS.SELECTED_COMPANY_FYYEAR = re.getString(3);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlParent = new javax.swing.JPanel();
        pnlTopScreenRegion = new javax.swing.JPanel();
        lblBulbImage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlCompanyNameHolder = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCompanyName = new javax.swing.JTextField();
        pnlBasicDetailsHolder = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAddressLine1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAddressLine2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPincode = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtStateCode = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtDistrict = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtWebsite = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        pnlFinancialYearRangeHolder = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        dateFYStart = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        dateFYClose = new com.toedter.calendar.JDateChooser();
        pnlRegistrationDetailsHolder = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtGSTNo = new javax.swing.JTextField();
        txtVATNo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        pnlLicensingDetailsHolder = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtDLNo1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDLNo2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDealsIn = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTaxSystem = new javax.swing.JTextField();
        pnlSuperUserDetailsHolder = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        pnlButtonsHolder = new javax.swing.JPanel();
        btnCreateCompany = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Create New Company");
        setResizable(false);

        pnlParent.setBackground(new java.awt.Color(57, 68, 76));

        pnlTopScreenRegion.setBackground(new java.awt.Color(57, 68, 76));
        pnlTopScreenRegion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));
        pnlTopScreenRegion.add(lblBulbImage);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Create a new organization");
        pnlTopScreenRegion.add(jLabel2);

        pnlCompanyNameHolder.setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("<html>Name of the Company / Firm / Organization:<font color=\"red\">*</font></html>");
        pnlCompanyNameHolder.add(jLabel1);

        txtCompanyName.setPreferredSize(new java.awt.Dimension(350, 25));
        txtCompanyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCompanyNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCompanyNameFocusLost(evt);
            }
        });
        txtCompanyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCompanyNameKeyReleased(evt);
            }
        });
        pnlCompanyNameHolder.add(txtCompanyName);

        pnlBasicDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlBasicDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Basic Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Address Line 1 :");

        txtAddressLine1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddressLine1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAddressLine1FocusLost(evt);
            }
        });
        txtAddressLine1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressLine1KeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Address Line 2 :");

        txtAddressLine2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddressLine2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAddressLine2FocusLost(evt);
            }
        });
        txtAddressLine2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressLine2KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("City :");

        txtCity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCityFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCityFocusLost(evt);
            }
        });
        txtCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCityKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Pincode :");

        txtPincode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPincodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPincodeFocusLost(evt);
            }
        });
        txtPincode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPincodeKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setText("<html>State:<font color=\"red\">*</font></html>");

        txtState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtStateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateFocusLost(evt);
            }
        });
        txtState.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStateKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setText("State Code :");

        txtStateCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtStateCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateCodeFocusLost(evt);
            }
        });
        txtStateCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStateCodeKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setText("District :");

        txtDistrict.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDistrictFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDistrictFocusLost(evt);
            }
        });
        txtDistrict.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDistrictKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setText("Email :");

        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 188, 81));
        jLabel23.setText("Website :");

        txtWebsite.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWebsiteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWebsiteFocusLost(evt);
            }
        });
        txtWebsite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWebsiteKeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 188, 81));
        jLabel24.setText("Phone :");

        txtPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPhoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhoneFocusLost(evt);
            }
        });
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPhoneKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlBasicDetailsHolderLayout = new javax.swing.GroupLayout(pnlBasicDetailsHolder);
        pnlBasicDetailsHolder.setLayout(pnlBasicDetailsHolderLayout);
        pnlBasicDetailsHolderLayout.setHorizontalGroup(
            pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddressLine1))
                    .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddressLine2))
                    .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBasicDetailsHolderLayout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBasicDetailsHolderLayout.createSequentialGroup()
                                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel11))
                                        .addGap(15, 15, 15))))
                            .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                                .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)))
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPincode)
                            .addComponent(txtPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(txtEmail)
                            .addComponent(txtStateCode))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        pnlBasicDetailsHolderLayout.setVerticalGroup(
            pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAddressLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAddressLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(txtPincode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtStateCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pnlFinancialYearRangeHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlFinancialYearRangeHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Financial Year Range", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setText("Financial Year Starting Date :");

        dateFYStart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateFYStartKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("Financial Year Ending Date :");

        dateFYClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateFYCloseKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlFinancialYearRangeHolderLayout = new javax.swing.GroupLayout(pnlFinancialYearRangeHolder);
        pnlFinancialYearRangeHolder.setLayout(pnlFinancialYearRangeHolderLayout);
        pnlFinancialYearRangeHolderLayout.setHorizontalGroup(
            pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFinancialYearRangeHolderLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(5, 5, 5)
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFYClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateFYStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFinancialYearRangeHolderLayout.setVerticalGroup(
            pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFinancialYearRangeHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateFYStart, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateFYClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlRegistrationDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlRegistrationDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registration Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setText("GST No :");

        txtGSTNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGSTNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTNoFocusLost(evt);
            }
        });
        txtGSTNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGSTNoKeyReleased(evt);
            }
        });

        txtVATNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVATNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVATNoFocusLost(evt);
            }
        });
        txtVATNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVATNoKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 188, 81));
        jLabel15.setText("VAT No :");

        javax.swing.GroupLayout pnlRegistrationDetailsHolderLayout = new javax.swing.GroupLayout(pnlRegistrationDetailsHolder);
        pnlRegistrationDetailsHolder.setLayout(pnlRegistrationDetailsHolderLayout);
        pnlRegistrationDetailsHolderLayout.setHorizontalGroup(
            pnlRegistrationDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistrationDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(txtVATNo)
                .addContainerGap())
        );
        pnlRegistrationDetailsHolderLayout.setVerticalGroup(
            pnlRegistrationDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistrationDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRegistrationDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtVATNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlLicensingDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlLicensingDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Licensing", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(238, 188, 81));
        jLabel16.setText("DL No 1 :");

        txtDLNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDLNo1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDLNo1FocusLost(evt);
            }
        });
        txtDLNo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDLNo1KeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 188, 81));
        jLabel17.setText("DL No 2 :");

        txtDLNo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDLNo2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDLNo2FocusLost(evt);
            }
        });
        txtDLNo2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDLNo2KeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setText("Deals In :");

        txtDealsIn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDealsInFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDealsInFocusLost(evt);
            }
        });
        txtDealsIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDealsInKeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setText("Tax System :");

        txtTaxSystem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTaxSystemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTaxSystemFocusLost(evt);
            }
        });
        txtTaxSystem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTaxSystemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlLicensingDetailsHolderLayout = new javax.swing.GroupLayout(pnlLicensingDetailsHolder);
        pnlLicensingDetailsHolder.setLayout(pnlLicensingDetailsHolderLayout);
        pnlLicensingDetailsHolderLayout.setHorizontalGroup(
            pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLicensingDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addGap(9, 9, 9)
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDLNo1)
                    .addComponent(txtDealsIn))
                .addGap(23, 23, 23)
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDLNo2)
                    .addComponent(txtTaxSystem))
                .addContainerGap())
        );
        pnlLicensingDetailsHolderLayout.setVerticalGroup(
            pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLicensingDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtDLNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtDLNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLicensingDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtDealsIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtTaxSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSuperUserDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlSuperUserDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Super User Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(238, 188, 81));
        jLabel20.setText("Username :");

        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 188, 81));
        jLabel21.setText("Password :");

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 188, 81));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("By Default Username = 1 and Password = 1 is given");

        javax.swing.GroupLayout pnlSuperUserDetailsHolderLayout = new javax.swing.GroupLayout(pnlSuperUserDetailsHolder);
        pnlSuperUserDetailsHolder.setLayout(pnlSuperUserDetailsHolderLayout);
        pnlSuperUserDetailsHolderLayout.setHorizontalGroup(
            pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperUserDetailsHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuperUserDetailsHolderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPassword)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        pnlSuperUserDetailsHolderLayout.setVerticalGroup(
            pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperUserDetailsHolderLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSuperUserDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlButtonsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlButtonsHolder.setLayout(new java.awt.GridBagLayout());

        btnCreateCompany.setBackground(new java.awt.Color(0, 255, 0));
        btnCreateCompany.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnCreateCompany.setText("Create Company");
        btnCreateCompany.setPreferredSize(new java.awt.Dimension(200, 35));
        btnCreateCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateCompanyActionPerformed(evt);
            }
        });
        btnCreateCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnCreateCompanyKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 14, 15, 14);
        pnlButtonsHolder.add(btnCreateCompany, gridBagConstraints);

        btnClose.setBackground(new java.awt.Color(255, 0, 0));
        btnClose.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(200, 35));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 14, 15, 14);
        pnlButtonsHolder.add(btnClose, gridBagConstraints);

        javax.swing.GroupLayout pnlParentLayout = new javax.swing.GroupLayout(pnlParent);
        pnlParent.setLayout(pnlParentLayout);
        pnlParentLayout.setHorizontalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addComponent(pnlCompanyNameHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlParentLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlLicensingDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlBasicDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlFinancialYearRangeHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlSuperUserDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlRegistrationDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlButtonsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(134, 134, 134))))
            .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlParentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlTopScreenRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlParentLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(pnlCompanyNameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addComponent(pnlFinancialYearRangeHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlRegistrationDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlSuperUserDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addComponent(pnlBasicDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3)))
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addComponent(pnlLicensingDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4))
                    .addComponent(pnlButtonsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(108, 108, 108))
            .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlParentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlTopScreenRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(673, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateCompanyActionPerformed
        try {
            createCompany();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateNewCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCreateCompanyActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    
        new listOfAllCompany().setVisible(true);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtStateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStateKeyReleased
        if (states != null && states.isEmpty()) {
            fetchStates();
        }

        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
                break;
            case java.awt.event.KeyEvent.VK_ENTER:
                txtState.setText(txtState.getText());

                List<Object> stateCode = DBController.executeQueryCompany("SELECT state_code FROM "
                        + DatabaseCredentials.STATES_TABLE + " WHERE state_name = "
                        + "'" + txtState.getText().trim() + "'");

                if (stateCode.get(0) != null) {
                    txtStateCode.setText(stateCode.get(0).toString());
                }

                txtStateCode.requestFocusInWindow();
                break;
            default:
                EventQueue.invokeLater(() -> {
                    autoComplete(states, txtState.getText(), txtState);
                });
        }
    }//GEN-LAST:event_txtStateKeyReleased

    private void txtCompanyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompanyNameKeyReleased
        setFocus(evt, txtAddressLine1);
    }//GEN-LAST:event_txtCompanyNameKeyReleased

    private void txtAddressLine1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressLine1KeyReleased
        setFocus(evt, txtAddressLine2);
    }//GEN-LAST:event_txtAddressLine1KeyReleased

    private void dateFYStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateFYStartKeyReleased
        setFocus(evt, dateFYClose);
    }//GEN-LAST:event_dateFYStartKeyReleased

    private void txtAddressLine2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressLine2KeyReleased
        setFocus(evt, txtCity);
    }//GEN-LAST:event_txtAddressLine2KeyReleased

    private void dateFYCloseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateFYCloseKeyReleased
        setFocus(evt, txtGSTNo);
    }//GEN-LAST:event_dateFYCloseKeyReleased

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        setFocus(evt, txtPincode);
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtPincodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPincodeKeyReleased
        setFocus(evt, txtState);
    }//GEN-LAST:event_txtPincodeKeyReleased

    private void txtStateCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStateCodeKeyReleased
        setFocus(evt, txtDistrict);
    }//GEN-LAST:event_txtStateCodeKeyReleased

    private void txtDistrictKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDistrictKeyReleased
        setFocus(evt, txtEmail);
    }//GEN-LAST:event_txtDistrictKeyReleased

    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
        setFocus(evt, txtWebsite);
    }//GEN-LAST:event_txtEmailKeyReleased

    private void txtWebsiteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWebsiteKeyReleased
        setFocus(evt, txtPhone);
    }//GEN-LAST:event_txtWebsiteKeyReleased

    private void txtPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyReleased
        setFocus(evt, txtDLNo1);
    }//GEN-LAST:event_txtPhoneKeyReleased

    private void txtDLNo1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDLNo1KeyReleased
        setFocus(evt, txtDLNo2);
    }//GEN-LAST:event_txtDLNo1KeyReleased

    private void txtDLNo2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDLNo2KeyReleased
        setFocus(evt, txtDealsIn);
    }//GEN-LAST:event_txtDLNo2KeyReleased

    private void txtDealsInKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDealsInKeyReleased
        setFocus(evt, txtTaxSystem);
    }//GEN-LAST:event_txtDealsInKeyReleased

    private void txtTaxSystemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxSystemKeyReleased
        setFocus(evt, dateFYStart);
    }//GEN-LAST:event_txtTaxSystemKeyReleased

    private void txtGSTNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTNoKeyReleased
        setFocus(evt, txtVATNo);
    }//GEN-LAST:event_txtGSTNoKeyReleased

    private void txtVATNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVATNoKeyReleased
        setFocus(evt, txtUsername);
    }//GEN-LAST:event_txtVATNoKeyReleased

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        setFocus(evt, txtPassword);
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        setFocus(evt, btnCreateCompany);
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void btnCreateCompanyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCreateCompanyKeyReleased
        try {
            createCompany();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateNewCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCreateCompanyKeyReleased

    private void txtCompanyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCompanyNameFocusGained
        txtCompanyName.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtCompanyNameFocusGained

    private void txtCompanyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCompanyNameFocusLost
        txtCompanyName.setBackground(Color.white);
    }//GEN-LAST:event_txtCompanyNameFocusLost

    private void txtAddressLine1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressLine1FocusGained
        txtAddressLine1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAddressLine1FocusGained

    private void txtAddressLine1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressLine1FocusLost
        txtAddressLine1.setBackground(Color.white);
    }//GEN-LAST:event_txtAddressLine1FocusLost

    private void txtAddressLine2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressLine2FocusGained
        txtAddressLine2.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAddressLine2FocusGained

    private void txtAddressLine2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressLine2FocusLost
        txtAddressLine2.setBackground(Color.white);
    }//GEN-LAST:event_txtAddressLine2FocusLost

    private void txtCityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusGained
        txtCity.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtCityFocusGained

    private void txtCityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusLost
        txtCity.setBackground(Color.white);
    }//GEN-LAST:event_txtCityFocusLost

    private void txtPincodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPincodeFocusGained
        txtPincode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPincodeFocusGained

    private void txtPincodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPincodeFocusLost
        txtPincode.setBackground(Color.white);
    }//GEN-LAST:event_txtPincodeFocusLost

    private void txtStateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusGained
        txtState.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtStateFocusGained

    private void txtStateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusLost
        txtState.setBackground(Color.white);
    }//GEN-LAST:event_txtStateFocusLost

    private void txtDistrictFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDistrictFocusGained
        txtDistrict.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDistrictFocusGained

    private void txtDistrictFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDistrictFocusLost
        txtDistrict.setBackground(Color.white);
    }//GEN-LAST:event_txtDistrictFocusLost

    private void txtStateCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateCodeFocusGained
        txtStateCode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtStateCodeFocusGained

    private void txtStateCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateCodeFocusLost
        txtStateCode.setBackground(Color.white);
    }//GEN-LAST:event_txtStateCodeFocusLost

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        txtEmail.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        txtEmail.setBackground(Color.white);
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtWebsiteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWebsiteFocusGained
        txtWebsite.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtWebsiteFocusGained

    private void txtWebsiteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWebsiteFocusLost
        txtWebsite.setBackground(Color.white);
    }//GEN-LAST:event_txtWebsiteFocusLost

    private void txtPhoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneFocusGained
        txtPhone.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPhoneFocusGained

    private void txtPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneFocusLost
        txtPhone.setBackground(Color.white);
    }//GEN-LAST:event_txtPhoneFocusLost

    private void txtDLNo1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDLNo1FocusGained
        txtDLNo1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDLNo1FocusGained

    private void txtDLNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDLNo1FocusLost
        txtDLNo1.setBackground(Color.white);
    }//GEN-LAST:event_txtDLNo1FocusLost

    private void txtDLNo2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDLNo2FocusGained
        txtDLNo2.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDLNo2FocusGained

    private void txtDLNo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDLNo2FocusLost
        txtDLNo2.setBackground(Color.white);
    }//GEN-LAST:event_txtDLNo2FocusLost

    private void txtDealsInFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDealsInFocusGained
        txtDealsIn.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDealsInFocusGained

    private void txtDealsInFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDealsInFocusLost
        txtDealsIn.setBackground(Color.white);
    }//GEN-LAST:event_txtDealsInFocusLost

    private void txtTaxSystemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxSystemFocusGained
        txtTaxSystem.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtTaxSystemFocusGained

    private void txtTaxSystemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxSystemFocusLost
        txtTaxSystem.setBackground(Color.white);
    }//GEN-LAST:event_txtTaxSystemFocusLost

    private void txtGSTNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTNoFocusGained
        txtGSTNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGSTNoFocusGained

    private void txtGSTNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTNoFocusLost
        txtGSTNo.setBackground(Color.white);
    }//GEN-LAST:event_txtGSTNoFocusLost

    private void txtVATNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVATNoFocusGained
        txtVATNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtVATNoFocusGained

    private void txtVATNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVATNoFocusLost
        txtVATNo.setBackground(Color.white);
    }//GEN-LAST:event_txtVATNoFocusLost

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        txtUsername.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        txtUsername.setBackground(Color.white);
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        txtPassword.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        txtPassword.setBackground(Color.white);
    }//GEN-LAST:event_txtPasswordFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCreateCompany;
    private com.toedter.calendar.JDateChooser dateFYClose;
    private com.toedter.calendar.JDateChooser dateFYStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblBulbImage;
    private javax.swing.JPanel pnlBasicDetailsHolder;
    private javax.swing.JPanel pnlButtonsHolder;
    private javax.swing.JPanel pnlCompanyNameHolder;
    private javax.swing.JPanel pnlFinancialYearRangeHolder;
    private javax.swing.JPanel pnlLicensingDetailsHolder;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlRegistrationDetailsHolder;
    private javax.swing.JPanel pnlSuperUserDetailsHolder;
    private javax.swing.JPanel pnlTopScreenRegion;
    private javax.swing.JTextField txtAddressLine1;
    private javax.swing.JTextField txtAddressLine2;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCompanyName;
    private javax.swing.JTextField txtDLNo1;
    private javax.swing.JTextField txtDLNo2;
    private javax.swing.JTextField txtDealsIn;
    private javax.swing.JTextField txtDistrict;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGSTNo;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPincode;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtStateCode;
    private javax.swing.JTextField txtTaxSystem;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtVATNo;
    private javax.swing.JTextField txtWebsite;
    // End of variables declaration//GEN-END:variables
}
