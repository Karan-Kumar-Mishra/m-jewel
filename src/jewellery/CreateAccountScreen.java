package jewellery;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static javafx.scene.paint.Color.color;
//import static javafx.scene.paint.Color.color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author ASUS
 */
public class CreateAccountScreen extends javax.swing.JFrame {

    private ImageIcon image;
    private List<Object> columnNames = new ArrayList<>();
    private final List<Object> data = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private List<List<Object>> states = new ArrayList<>();
    private Logger logger = Logger.getLogger(CreateAccountScreen.class.getName());
    String temp = "";
    private String recp;
//    public CreateAccountScreen(String temp) {
//        initComponents();
//        //wait
//        btnUpdateAccount.setVisible(false);
//        temp = txtAccountName.getText();
//        if(GLOBAL_VARS.tabbedPaneDimensions != null) {
//            changeDimensions();
//        }
//        
//        setImageOnComponent(lblNewAccountImage, "/assets/newAccountImage.png");
//        
//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//        int h = d.height;
//        int w = d.width;
//        jPanel1.setSize(w - 280, h);
//        
//        fetchStates();
//        
//        populateGroupComboBox();
//    }

    public CreateAccountScreen() {
        initComponents();

        //wait
        btnUpdateAccount.setVisible(false);
        btnSaveAccount.setVisible(true);
        temp = txtAccountName.getText();
        if (GLOBAL_VARS.tabbedPaneDimensions != null) {
            changeDimensions();
        }

        setImageOnComponent(lblNewAccountImage, "/assets/newAccountImage.png");

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;
        jPanel1.setSize(w - 230, h);

        fetchStates();

        populateGroupComboBox();
    }

    CreateAccountScreen(String recp) {
        this.recp = recp;
        initComponents();
        //wait
        btnUpdateAccount.setVisible(false);
        temp = txtAccountName.getText();
        if (GLOBAL_VARS.tabbedPaneDimensions != null) {
            changeDimensions();
        }

        setImageOnComponent(lblNewAccountImage, "/assets/newAccountImage.png");

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;
        jPanel1.setSize(w - 280, h);

        fetchStates();

        populateGroupComboBox();
    }

    String tempid = "";

    public void bill_history_update(String recp) {
        tempid = recp;

        btnUpdateAccount.setVisible(true);
        btnSaveAccount.setVisible(false);
        try {
            Connection c = DBConnect.connect();

            Statement s = c.createStatement();
            String query = "Select * from account where id='" + recp + "';";
            ResultSet rs = s.executeQuery(query);
//               txtAccountName.setText(recp);
//               id=recp;
            while (rs.next()) {

                String name = rs.getString("accountname");
                txtAccountName.setText(name);
                String printname = rs.getString("printname");
                txtPrintName.setText(printname);
                String grp = rs.getString("grp");
                cmbGroup.setSelectedItem(grp);
                String state = rs.getString("state");
                txtState.setText(state);
                String Contact = rs.getString("mobile1");
                txtMobile1.setText(Contact);
                String opbal = rs.getString("opbal");
                double balance = Double.parseDouble(opbal);
                txtOpBalance.setText(String.valueOf(Math.abs(balance)));
                String address = rs.getString("address");
                txtAddress.setText(address);
                String city = rs.getString("city");
                txtCity.setText(city);
                String pincode = rs.getString("pincode");
                txtPincode.setText(pincode);
                String contact2 = rs.getString("mobile2");
                txtMobile2.setText(contact2);
                String email = rs.getString("email");
                txtEmail.setText(email);
                String gstno = rs.getString("gstno");
                txtGSTNo.setText(gstno);
                String adharno = rs.getString("adharno");
                txtAdharCardNo.setText(adharno);
                String vatno = rs.getString("vatno");
                txtVATNo.setText(vatno);
                if (rs.getBoolean("supplier_as_customer")) {
                    supplierCustomerCheckBox.setSelected(true);
                } else {
                    supplierCustomerCheckBox.setSelected(false);
                }
                String panno = rs.getString("panno");
                txtPANNo.setText(panno);
                String statecode = rs.getString("statecode");
                txtStateCode.setText(statecode);
                ifscCode.setText(rs.getString("ifsc"));
                txtAccountNumber.setText(rs.getString("accountnumber"));
                String drcr = rs.getString("dr_cr");
                if (drcr.equals("Dr")) {
                    cmbDrCr.setSelectedIndex(0);
                } else {
                    cmbDrCr.setSelectedIndex(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Bill_History_Update.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeDimensions() {
        setSize(GLOBAL_VARS.tabbedPaneDimensions);
    }

    private void setImageOnComponent(javax.swing.JLabel component, String resourceLocation) {
        image = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth(), component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(image);
    }

    private void populateGroupComboBox() {
        cmbGroup.removeAllItems();

        DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);

        List<Object> accountGroups = DBController.executeQuery("SELECT account_group FROM "
                + DatabaseCredentials.ACCOUNT_GROUP_TABLE);

        accountGroups.forEach((accountGroup) -> {
            cmbGroup.addItem(accountGroup.toString());
        });
        cmbGroup.setSelectedItem("Customer");
    }

    private boolean comboBoxContainsGroup(String group) {
        for (int idx = 0; idx < cmbGroup.getItemCount(); idx++) {
            if (cmbGroup.getItemAt(idx).equals(group)) {
                return true;
            }
        }

        return false;
    }

    private void fetchStates() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
        states = DBController.getDataFromcompanyTable("SELECT * FROM "
                + DatabaseCredentials.STATES_TABLE);
    }

    private void setStateCode(String stateName) {
        states.forEach((state) -> {
            if (state.get(0).toString().equals(stateName)) {
                txtStateCode.setText(state.get(1).toString());
            }
        });
    }

    private boolean fieldsAreValidates() {
        if (UtilityMethods.isTextFieldEmpty(txtAccountName)){
//                || !UtilityMethods.inputOnlyContainsAlphabets(txtAccountName.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter account name");
            return false;
        } else if (UtilityMethods.isTextFieldEmpty(txtPrintName)){
//                || !UtilityMethods.inputOnlyContainsAlphabets(txtPrintName.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter print name");
            return false;
        } 

        return true;
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAccountName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPrintName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbGroup = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtMobile1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtOpBalance = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtStateCode = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtGSTNo = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblNewAccountImage = new javax.swing.JLabel();
        dateAnniversary = new com.toedter.calendar.JDateChooser();
        dateBirthdayOn = new com.toedter.calendar.JDateChooser();
        txtState = new javax.swing.JTextField();
        txtPincode = new javax.swing.JTextField();
        cmbDrCr = new javax.swing.JComboBox<>();
        txtMobile2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnSaveAccount = new javax.swing.JButton();
        btnListOfAccounts = new javax.swing.JButton();
        txtAdharCardNo = new javax.swing.JTextField();
        txtVATNo = new javax.swing.JTextField();
        txtPANNo = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtArea = new javax.swing.JTextField();
        btnUpdateAccount = new javax.swing.JButton();
        supplierCustomerCheckBox = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        ifscCode = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtAccountNumber = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));
        jPanel1.setForeground(new java.awt.Color(255, 153, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1300, 1000));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("<html>Account Name<font color=\"red\">*</font><html>");

        txtAccountName.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        txtAccountName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAccountNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNameFocusLost(evt);
            }
        });
        txtAccountName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccountNameKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("<html>Print Name<font color=\"red\">*</font></html>");

        txtPrintName.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        txtPrintName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrintNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrintNameFocusLost(evt);
            }
        });
        txtPrintName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrintNameKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Group");

        cmbGroup.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cmbGroup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbGroupFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbGroupFocusLost(evt);
            }
        });
        cmbGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbGroupKeyReleased(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plusSignGreen.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/bluePencilImage.png"))); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Op. Bal.");

        txtMobile1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtMobile1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMobile1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobile1FocusLost(evt);
            }
        });
        txtMobile1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMobile1KeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Dr./ Cr.");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Address");

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddressFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAddressFocusLost(evt);
            }
        });
        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setText("Area");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("City");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Pincode");

        txtOpBalance.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtOpBalance.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtOpBalanceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOpBalanceFocusLost(evt);
            }
        });
        txtOpBalance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOpBalanceKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("<html>State</html>");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setText("<html>State Code</html>");

        txtStateCode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtStateCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtStateCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateCodeFocusLost(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 188, 81));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("<html>Mobile 1<font color=\"red\">*</font></html>");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(238, 188, 81));
        jLabel16.setText("Mobile 2");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 188, 81));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Email");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Birthday On");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setText("Anniversary");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("GST No.");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 188, 81));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Adhar Card No");

        txtGSTNo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 188, 81));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("VAT Number");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 188, 81));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("PAN Number");

        dateAnniversary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateAnniversaryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateAnniversaryFocusLost(evt);
            }
        });
        dateAnniversary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateAnniversaryKeyReleased(evt);
            }
        });

        dateBirthdayOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateBirthdayOnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateBirthdayOnFocusLost(evt);
            }
        });
        dateBirthdayOn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateBirthdayOnKeyReleased(evt);
            }
        });

        txtState.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtStateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateFocusLost(evt);
            }
        });
        txtState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtStateMouseClicked(evt);
            }
        });
        txtState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStateActionPerformed(evt);
            }
        });
        txtState.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStateKeyReleased(evt);
            }
        });

        txtPincode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPincode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPincodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPincodeFocusLost(evt);
            }
        });
        txtPincode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPincodeActionPerformed(evt);
            }
        });
        txtPincode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPincodeKeyReleased(evt);
            }
        });

        cmbDrCr.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cmbDrCr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dr", "Cr" }));
        cmbDrCr.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cmbDrCr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbDrCrFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbDrCrFocusLost(evt);
            }
        });
        cmbDrCr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbDrCrKeyReleased(evt);
            }
        });

        txtMobile2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtMobile2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMobile2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobile2FocusLost(evt);
            }
        });
        txtMobile2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMobile2KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("                                     CREATE ACCOUNT");
        jLabel2.setOpaque(true);

        btnSaveAccount.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSaveAccount.setText("Save");
        btnSaveAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveAccountMouseClicked(evt);
            }
        });
        btnSaveAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAccountActionPerformed(evt);
            }
        });
        btnSaveAccount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnSaveAccountKeyReleased(evt);
            }
        });

        btnListOfAccounts.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnListOfAccounts.setText("List of Accounts");
        btnListOfAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListOfAccountsActionPerformed(evt);
            }
        });

        txtAdharCardNo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtAdharCardNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAdharCardNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdharCardNoFocusLost(evt);
            }
        });
        txtAdharCardNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAdharCardNoKeyReleased(evt);
            }
        });

        txtVATNo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        txtPANNo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPANNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPANNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPANNoFocusLost(evt);
            }
        });
        txtPANNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPANNoKeyReleased(evt);
            }
        });

        txtCity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        txtArea.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAreaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAreaFocusLost(evt);
            }
        });
        txtArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAreaKeyReleased(evt);
            }
        });

        btnUpdateAccount.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnUpdateAccount.setText("Update");
        btnUpdateAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateAccountMouseClicked(evt);
            }
        });
        btnUpdateAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAccountActionPerformed(evt);
            }
        });

        supplierCustomerCheckBox.setForeground(new java.awt.Color(238, 188, 81));
        supplierCustomerCheckBox.setText("Use Supplier as Customer Also");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 188, 81));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("IFSC CODE");

        ifscCode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ifscCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ifscCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ifscCodeFocusLost(evt);
            }
        });
        ifscCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ifscCodeActionPerformed(evt);
            }
        });
        ifscCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ifscCodeKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(238, 188, 81));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Account No.");

        txtAccountNumber.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        txtAccountNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccountNumberKeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtVATNo, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(ifscCode, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtPincode, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtMobile1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 67, Short.MAX_VALUE))
                                            .addComponent(txtAddress))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMobile2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtPANNo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel25))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtOpBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbDrCr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 40, Short.MAX_VALUE)))
                                        .addGap(8, 8, 8))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtAccountName, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPrintName, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(cmbGroup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAdd)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnEdit)))
                                        .addGap(70, 70, 70)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(supplierCustomerCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel21)))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAdharCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblNewAccountImage, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtStateCode, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(22, 22, 22))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateBirthdayOn, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19))
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(dateAnniversary, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(342, 342, 342)
                .addComponent(btnUpdateAccount)
                .addGap(18, 18, 18)
                .addComponent(btnListOfAccounts)
                .addGap(18, 18, 18)
                .addComponent(btnSaveAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtAccountName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrintName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmbGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierCustomerCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtAdharCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNewAccountImage, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPANNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOpBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbDrCr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtVATNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ifscCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStateCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPincode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtMobile1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtMobile2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateBirthdayOn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateAnniversary, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnListOfAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSaveAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1060, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtAccountNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNumberKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            ifscCode.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAccountNumberKeyReleased

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        txtAccountNumber.setBackground(Color.white);
    }//GEN-LAST:event_txtAccountNumberFocusLost

    private void txtAccountNumberFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusGained
        txtAccountNumber.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAccountNumberFocusGained

    private void ifscCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ifscCodeKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtOpBalance.requestFocus();

        }
    }//GEN-LAST:event_ifscCodeKeyReleased

    private void ifscCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ifscCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ifscCodeActionPerformed

    private void ifscCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ifscCodeFocusLost
        ifscCode.setBackground(Color.white);
    }//GEN-LAST:event_ifscCodeFocusLost

    private void ifscCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ifscCodeFocusGained
        ifscCode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_ifscCodeFocusGained

    private void btnUpdateAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateAccountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateAccountActionPerformed

    private void btnUpdateAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateAccountMouseClicked
        updateAccountDetails();
    }//GEN-LAST:event_btnUpdateAccountMouseClicked

    private void txtAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtPincode.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAreaKeyReleased

    private void txtAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaFocusLost
        txtArea.setBackground(Color.white);
    }//GEN-LAST:event_txtAreaFocusLost

    private void txtAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaFocusGained
        txtArea.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAreaFocusGained

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtArea.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtCityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusLost
        txtCity.setBackground(Color.white);
    }//GEN-LAST:event_txtCityFocusLost

    private void txtCityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusGained
        txtCity.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtCityFocusGained

    private void txtPANNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPANNoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAccountNumber.requestFocusInWindow();
            
        }
    }//GEN-LAST:event_txtPANNoKeyReleased

    private void txtPANNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPANNoFocusLost
        txtPANNo.setBackground(Color.white);
    }//GEN-LAST:event_txtPANNoFocusLost

    private void txtPANNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPANNoFocusGained
        txtPANNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPANNoFocusGained

    private void txtVATNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVATNoKeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtPANNo.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtVATNoKeyReleased

    private void txtVATNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVATNoFocusLost
        txtVATNo.setBackground(Color.white);
    }//GEN-LAST:event_txtVATNoFocusLost

    private void txtVATNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVATNoFocusGained
        txtVATNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtVATNoFocusGained

    private void txtAdharCardNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdharCardNoKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtVATNo.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAdharCardNoKeyReleased

    private void txtAdharCardNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdharCardNoFocusLost
        txtAdharCardNo.setBackground(Color.white);
    }//GEN-LAST:event_txtAdharCardNoFocusLost

    private void txtAdharCardNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdharCardNoFocusGained
        txtAdharCardNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAdharCardNoFocusGained

    private void btnListOfAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListOfAccountsActionPerformed
        new AccountsListScreen().setVisible(true);
    }//GEN-LAST:event_btnListOfAccountsActionPerformed

    private void btnSaveAccountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveAccountKeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            columnNames.clear();
            data.clear();

            if (fieldsAreValidates()) {
                if (DBController.isDatabaseConnected()) {
                    columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);

                } else {
                    DBController
                    .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME,
                        DatabaseCredentials.DB_PASSWORD);
                    columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);

                }
                //                 Remove the account id as it is auto generated
                columnNames.remove(0);
                columnNames.remove(25);
                data.add(txtAccountName.getText().trim());
                data.add(txtPrintName.getText());
                data.add(cmbGroup.getSelectedItem());

                if (!txtOpBalance.getText().trim().isEmpty()) {
                    double opbalance = Double.parseDouble(txtOpBalance.getText());
                    if (cmbDrCr.getSelectedItem().equals("Cr")) {
                        if (cmbGroup.getSelectedItem().equals("Customer")) {
                            opbalance *= -1;
                        }
                        if (cmbGroup.getSelectedItem().equals("Supplier")) {
                            opbalance *= -1;
                        }
                    }

                    data.add(String.format("%.2f", opbalance));
                } else {
                    data.add("0");
                }
                if (!txtAddress.getText().trim().isEmpty()) {
                    data.add(txtAddress.getText().trim());
                } else {
                    columnNames.remove("address");
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
                if (!txtMobile1.getText().trim().isEmpty()) {
                    data.add(txtMobile1.getText());
                } else {
                    columnNames.remove("mobile1");
                }

                if (!txtMobile2.getText().trim().isEmpty()) {
                    data.add(txtMobile2.getText());
                } else {
                    columnNames.remove("mobile2");
                }

                if (!txtEmail.getText().trim().isEmpty()) {
                    data.add(txtEmail.getText().trim());
                } else {
                    columnNames.remove("email");
                }
                if (UtilityMethods.hasDateBeenPicked(dateBirthdayOn)) {
                    data.add(dateFormat.format(dateBirthdayOn.getDate()));
                } else {
                    columnNames.remove("birthday");
                }

                if (!txtGSTNo.getText().trim().isEmpty()) {
                    data.add(txtGSTNo.getText());
                } else {
                    columnNames.remove("gstno");
                }

                if (!txtAdharCardNo.getText().trim().isEmpty()) {
                    data.add(txtAdharCardNo.getText());
                } else {
                    columnNames.remove("adharno");
                }

                if (!txtVATNo.getText().trim().isEmpty()) {
                    data.add(txtVATNo.getText());
                } else {
                    columnNames.remove("vatno");
                }

                if (!txtPANNo.getText().trim().isEmpty()) {
                    data.add(txtPANNo.getText().trim());
                } else {
                    columnNames.remove("panno");
                }
                data.add(txtState.getText());
                data.add(txtStateCode.getText());

                if (UtilityMethods.hasDateBeenPicked(dateAnniversary)) {
                    data.add(dateFormat.format(dateAnniversary.getDate()));
                } else {
                    columnNames.remove("anniversary");
                }

                data.add(cmbDrCr.getSelectedItem());
                if (supplierCustomerCheckBox.isSelected()) {
                    data.add(1);
                } else {
                    data.add(0);
                }

                if (!txtArea.getText().trim().isEmpty()) {
                    data.add(txtArea.getText().trim());
                } else {
                    columnNames.remove("area");
                }
                data.add("0");
                data.add("0");
                if (!(ifscCode.getText().trim().isEmpty())) {
                    data.add(ifscCode.getText().trim());
                } else {
                    columnNames.remove("ifsc");
                }
                if (!(txtAccountNumber.getText().trim().isEmpty())) {
                    data.add(txtAccountNumber.getText().trim());
                } else {
                    columnNames.remove("accountnumber");
                }

                boolean insertionResult = DBController.insertDataIntoTable(DatabaseCredentials.ACCOUNT_TABLE,
                    columnNames, data);
                if (insertionResult) {
                    JOptionPane.showMessageDialog(this, "Account has been created successfully");

                    emptyText();

                    txtAccountName.requestFocus();
                    // Logger.getLogger(CreateAccountScreen.class.getName());
                }

                txtGSTNo.setBackground(Color.white);

            }
        }
    }//GEN-LAST:event_btnSaveAccountKeyReleased

    private void btnSaveAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAccountActionPerformed
        //        columnNames.clear();
        //        data.clear();
        //
        //        if(fieldsAreValidates()) {
            //            if(DBController.isDatabaseConnected()) {
                //                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);
                //                // Remove the account id as it is auto generated
                ////                columnNames.remove(0);
                //            }
            //            else {
                //                DBController
                //                        .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    //                                DatabaseCredentials.DB_USERNAME,
                    //                                DatabaseCredentials.DB_PASSWORD);
                //                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);
                //
                //                // Remove the account id as it is auto generated
                ////                columnNames.remove(0);
                //            }
            //
            //            data.add(txtAccountName.getText().trim());
            //            data.add(txtPrintName.getText());
            //            data.add(cmbGroup.getSelectedItem());
            //
            //
            //            if(!txtOpBalance.getText().trim().isEmpty()) {
                //                double opbalance = Double.parseDouble(txtOpBalance.getText());
                //                if(cmbDrCr.getSelectedItem().equals("Cr")) {
                    //                    if(cmbGroup.getSelectedItem().equals("Customer"))
                    //                        opbalance *= -1;
                    //                    if(cmbGroup.getSelectedItem().equals("Supplier")) {
                        //                        opbalance *= -1;
                        //                    }
                    //                }
                //
                //                data.add(String.format("%.2f", opbalance));
                //            }
            //            else {
                //                    data.add("0");
                //            }
            //            if(!txtAddress.getText().trim().isEmpty()) {
                //                data.add(txtAddress.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("address");
                //            }
            //
            //            if(!txtCity.getText().trim().isEmpty()) {
                //                data.add(txtCity.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("city");
                //            }
            //
            //
            //
            //            if(!txtPincode.getText().trim().isEmpty()) {
                //                data.add(txtPincode.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("pincode");
                //            }
            //            if(!txtMobile1.getText().trim().isEmpty()) {
                //                data.add(txtMobile1.getText());
                //            }
            //            else {
                //                columnNames.remove("mobile1");
                //            }
            //
            //            if(!txtMobile2.getText().trim().isEmpty()) {
                //                data.add(txtMobile2.getText());
                //            }
            //            else {
                //                columnNames.remove("mobile2");
                //            }
            //
            //
            //            if(!txtEmail.getText().trim().isEmpty()) {
                //                data.add(txtEmail.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("email");
                //            }
            //            if(UtilityMethods.hasDateBeenPicked(dateBirthdayOn)) {
                //                data.add(dateFormat.format(dateBirthdayOn.getDate()));
                //            }
            //            else {
                //                columnNames.remove("birthday");
                //            }
            //
            //            if(!txtGSTNo.getText().trim().isEmpty()) {
                //                data.add(txtGSTNo.getText());
                //            }
            //            else {
                //                columnNames.remove("gstno");
                //            }
            //
            //            if(!txtAdharCardNo.getText().trim().isEmpty()) {
                //                data.add(txtAdharCardNo.getText());
                //            }
            //            else {
                //                columnNames.remove("adharno");
                //            }
            //
            //            if(!txtVATNo.getText().trim().isEmpty()) {
                //                data.add(txtVATNo.getText());
                //            }
            //            else {
                //                columnNames.remove("vatno");
                //            }
            //
            //            if(!txtPANNo.getText().trim().isEmpty()) {
                //                data.add(txtPANNo.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("panno");
                //            }
            //            data.add(txtState.getText());
            //            data.add(txtStateCode.getText());
            //
            //            if(UtilityMethods.hasDateBeenPicked(dateAnniversary)) {
                //                data.add(dateFormat.format(dateAnniversary.getDate()));
                //            }
            //            else {
                //                columnNames.remove("anniversary");
                //            }
            //
            //            data.add(cmbDrCr.getSelectedItem());
            //            if(supplierCustomerCheckBox.isSelected()) {
                //                data.add(1);
                //            } else data.add(0);
            //
            //            if(!txtArea.getText().trim().isEmpty()) {
                //                data.add(txtArea.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("area");
                //            }
            //            data.add("0");
            //            data.add("0");
            //             if(!(ifscCode.getText().trim().isEmpty())) {
                //                data.add(ifscCode.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("ifsc");
                //            }
            //             if(!(txtAccountNumber.getText().trim().isEmpty())) {
                //                data.add(txtAccountNumber.getText().trim());
                //            }
            //            else {
                //                columnNames.remove("accountnumber");
                //            }
            //
            //            boolean insertionResult = DBController.insertDataIntoTable(DatabaseCredentials.ACCOUNT_TABLE,
                //                    columnNames, data);
            //            if(insertionResult) {
                //                JOptionPane.showMessageDialog(this, "Account has been created successfully");
                //                emptyText();
                //            }
            //        }
        //        txtGSTNo.setBackground(Color.white);
        //
    }//GEN-LAST:event_btnSaveAccountActionPerformed

    private void btnSaveAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveAccountMouseClicked
        // TODO add your handling code here:
        columnNames.clear();
        data.clear();

        if (fieldsAreValidates()) {
            if (DBController.isDatabaseConnected()) {
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);
                // Remove the account id as it is auto generated

            } else {
                DBController
                .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);

                // Remove the account id as it is auto generated
            }
            columnNames.remove(0);
            columnNames.remove(25);
            data.add(txtAccountName.getText().trim());
            data.add(txtPrintName.getText());
            data.add(cmbGroup.getSelectedItem());

            if (!txtOpBalance.getText().trim().isEmpty()) {
                double opbalance = Double.parseDouble(txtOpBalance.getText());
                if (cmbDrCr.getSelectedItem().equals("Cr")) {
                    if (cmbGroup.getSelectedItem().equals("Customer")) {
                        opbalance *= -1;
                    }
                    if (cmbGroup.getSelectedItem().equals("Supplier")) {
                        opbalance *= -1;
                    }
                }

                data.add(String.format("%.2f", opbalance));
            } else {
                data.add("0");
            }
            if (!txtAddress.getText().trim().isEmpty()) {
                data.add(txtAddress.getText().trim());
            } else {
                columnNames.remove("address");
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
            if (!txtMobile1.getText().trim().isEmpty()) {
                data.add(txtMobile1.getText());
            } else {
                columnNames.remove("mobile1");
            }

            if (!txtMobile2.getText().trim().isEmpty()) {
                data.add(txtMobile2.getText());
            } else {
                columnNames.remove("mobile2");
            }

            if (!txtEmail.getText().trim().isEmpty()) {
                data.add(txtEmail.getText().trim());
            } else {
                columnNames.remove("email");
            }
            if (UtilityMethods.hasDateBeenPicked(dateBirthdayOn)) {
                data.add(dateFormat.format(dateBirthdayOn.getDate()));
            } else {
                columnNames.remove("birthday");
            }

            if (!txtGSTNo.getText().trim().isEmpty()) {
                data.add(txtGSTNo.getText());
            } else {
                columnNames.remove("gstno");
            }

            if (!txtAdharCardNo.getText().trim().isEmpty()) {
                data.add(txtAdharCardNo.getText());
            } else {
                columnNames.remove("adharno");
            }

            if (!txtVATNo.getText().trim().isEmpty()) {
                data.add(txtVATNo.getText());
            } else {
                columnNames.remove("vatno");
            }

            if (!txtPANNo.getText().trim().isEmpty()) {
                data.add(txtPANNo.getText().trim());
            } else {
                columnNames.remove("panno");
            }
            
            data.add(txtState.getText());
            data.add(txtStateCode.getText());

            if (UtilityMethods.hasDateBeenPicked(dateAnniversary)) {
                data.add(dateFormat.format(dateAnniversary.getDate()));
            } else {
                columnNames.remove("anniversary");
            }

            data.add(cmbDrCr.getSelectedItem());
            if (supplierCustomerCheckBox.isSelected()) {
                data.add(1);
            } else {
                data.add(0);
            }

            if (!txtArea.getText().trim().isEmpty()) {
                data.add(txtArea.getText().trim());
            } else {
                columnNames.remove("area");
            }
            data.add("0");
            data.add("0");
            if (!(ifscCode.getText().trim().isEmpty())) {
                data.add(ifscCode.getText().trim());
            } else {
                columnNames.remove("ifsc");
            }
            if (!(txtAccountNumber.getText().trim().isEmpty())) {
                data.add(txtAccountNumber.getText().trim());
            } else {
                columnNames.remove("accountnumber");
            }

            boolean insertionResult = DBController.insertDataIntoTable(DatabaseCredentials.ACCOUNT_TABLE,
                columnNames, data);
            if (insertionResult) {
                JOptionPane.showMessageDialog(this, "Account has been created successfully");
                emptyText();

            }
        }
        txtGSTNo.setBackground(Color.white);
    }//GEN-LAST:event_btnSaveAccountMouseClicked

    private void txtMobile2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobile2KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtEmail.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtMobile2KeyReleased

    private void txtMobile2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobile2FocusLost
        txtMobile2.setBackground(Color.white);
    }//GEN-LAST:event_txtMobile2FocusLost

    private void txtMobile2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobile2FocusGained
        txtMobile2.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtMobile2FocusGained

    private void cmbDrCrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDrCrKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtAddress.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbDrCrKeyReleased

    private void cmbDrCrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbDrCrFocusLost
        cmbDrCr.setBackground(Color.white);
    }//GEN-LAST:event_cmbDrCrFocusLost

    private void cmbDrCrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbDrCrFocusGained
        cmbDrCr.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbDrCrFocusGained

    private void txtPincodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPincodeKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtState.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtPincodeKeyReleased

    private void txtPincodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPincodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPincodeActionPerformed

    private void txtPincodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPincodeFocusLost
        txtPincode.setBackground(Color.white);
    }//GEN-LAST:event_txtPincodeFocusLost

    private void txtPincodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPincodeFocusGained
        txtPincode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPincodeFocusGained

    private void txtStateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStateKeyReleased
        if (states == null || states.isEmpty()) {
            fetchStates();
        }

        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
            break;
            case java.awt.event.KeyEvent.VK_ENTER:
            txtState.setText(txtState.getText());

            EventQueue.invokeLater(() -> {
                setStateCode(txtState.getText());
            });
            txtMobile1.requestFocusInWindow();
            break;
            default:
            EventQueue.invokeLater(() -> {
                autoComplete(states, txtState.getText(), txtState);
            });
        }
    }//GEN-LAST:event_txtStateKeyReleased

    private void txtStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStateActionPerformed

    private void txtStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStateMouseClicked
        if (evt.getClickCount() >= 1) {
        }
    }//GEN-LAST:event_txtStateMouseClicked

    private void txtStateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusLost
        txtState.setBackground(Color.white);
    }//GEN-LAST:event_txtStateFocusLost

    private void txtStateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusGained
        txtState.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtStateFocusGained

    private void dateBirthdayOnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateBirthdayOnKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            dateAnniversary.requestFocusInWindow();
        }
    }//GEN-LAST:event_dateBirthdayOnKeyReleased

    private void dateBirthdayOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateBirthdayOnFocusLost
        dateBirthdayOn.setBackground(Color.white);
    }//GEN-LAST:event_dateBirthdayOnFocusLost

    private void dateBirthdayOnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateBirthdayOnFocusGained
        dateBirthdayOn.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_dateBirthdayOnFocusGained

    private void dateAnniversaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateAnniversaryKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            btnSaveAccount.requestFocusInWindow();
        }
    }//GEN-LAST:event_dateAnniversaryKeyReleased

    private void dateAnniversaryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateAnniversaryFocusLost
        dateAnniversary.setBackground(Color.white);
    }//GEN-LAST:event_dateAnniversaryFocusLost

    private void dateAnniversaryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateAnniversaryFocusGained
        dateAnniversary.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_dateAnniversaryFocusGained

    private void txtGSTNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTNoKeyReleased
        // TODO add your handling code here:
        int s = txtGSTNo.getText().length();
        String value = txtGSTNo.getText();
        if (s > 15) {
            txtGSTNo.setBackground(Color.red);
        } else if (s < 15) {
            txtGSTNo.setBackground(Color.red);
        } else if (!checker(value)) {
            txtGSTNo.setBackground(Color.red);
        } else {
            txtGSTNo.setBackground(Color.GREEN);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtAdharCardNo.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtGSTNoKeyReleased

    private void txtGSTNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTNoFocusLost

    }//GEN-LAST:event_txtGSTNoFocusLost

    private void txtGSTNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTNoFocusGained
        txtGSTNo.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGSTNoFocusGained

    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            btnSaveAccount.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtEmailKeyReleased

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        txtEmail.setBackground(Color.white);
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        txtEmail.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtStateCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateCodeFocusLost
        txtStateCode.setBackground(Color.white);
    }//GEN-LAST:event_txtStateCodeFocusLost

    private void txtStateCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateCodeFocusGained
        txtStateCode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtStateCodeFocusGained

    private void txtOpBalanceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOpBalanceKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            cmbDrCr.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtOpBalanceKeyReleased

    private void txtOpBalanceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOpBalanceFocusLost
        txtOpBalance.setBackground(Color.white);
    }//GEN-LAST:event_txtOpBalanceFocusLost

    private void txtOpBalanceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOpBalanceFocusGained
        txtOpBalance.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtOpBalanceFocusGained

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtCity.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtAddressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressFocusLost
        txtAddress.setBackground(Color.white);
    }//GEN-LAST:event_txtAddressFocusLost

    private void txtAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddressFocusGained
        txtAddress.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAddressFocusGained

    private void txtMobile1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobile1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtMobile2.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtMobile1KeyReleased

    private void txtMobile1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobile1FocusLost
        txtMobile1.setBackground(Color.white);
    }//GEN-LAST:event_txtMobile1FocusLost

    private void txtMobile1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobile1FocusGained
        txtMobile1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtMobile1FocusGained

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        String groupToUpdate = JOptionPane.showInputDialog(this, "Edit Group",
            cmbGroup.getSelectedItem().toString());

        if (groupToUpdate != null && groupToUpdate.length() > 0) {

            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            List<Object> colNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_GROUP_TABLE);
            List<Object> groupData = new ArrayList<>();
            groupData.add(groupToUpdate);

            boolean groupIsUpdated = DBController.updateTableData(DatabaseCredentials.ACCOUNT_GROUP_TABLE,
                groupData, colNames, "account_group", cmbGroup.getSelectedItem());

            if (groupIsUpdated) {
                JOptionPane.showMessageDialog(this, "Group updated successfully");
            }

        }

        populateGroupComboBox();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String inputtedGroup = JOptionPane.showInputDialog(this, "Add Group");

        if ((inputtedGroup != null && inputtedGroup.length() > 0)
            && !comboBoxContainsGroup(inputtedGroup)) {

            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            List<Object> colNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_GROUP_TABLE);
            List<Object> groupData = new ArrayList<>();
            groupData.add(inputtedGroup);

            boolean groupIsInserted = DBController.insertDataIntoTable(DatabaseCredentials.ACCOUNT_GROUP_TABLE,
                colNames, groupData);

            if (groupIsInserted) {
                JOptionPane.showMessageDialog(this, "Group added successfully");
            }

        }

        populateGroupComboBox();
    }//GEN-LAST:event_btnAddActionPerformed

    private void cmbGroupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGroupKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtGSTNo.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmbGroupKeyReleased

    private void cmbGroupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbGroupFocusLost
        cmbGroup.setBackground(Color.white);
    }//GEN-LAST:event_cmbGroupFocusLost

    private void cmbGroupFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbGroupFocusGained
        cmbGroup.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbGroupFocusGained

    private void txtPrintNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrintNameKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            cmbGroup.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtPrintNameKeyReleased

    private void txtPrintNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrintNameFocusLost
        txtPrintName.setBackground(Color.white);
    }//GEN-LAST:event_txtPrintNameFocusLost

    private void txtPrintNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrintNameFocusGained
        txtPrintName.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtPrintNameFocusGained

    private void txtAccountNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPrintName.setText(txtAccountName.getText().trim());
            txtPrintName.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAccountNameKeyReleased

    private void txtAccountNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNameFocusLost
        txtAccountName.setBackground(Color.white);
    }//GEN-LAST:event_txtAccountNameFocusLost

//    focus color
    private void txtAccountNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNameFocusGained
        txtAccountName.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtAccountNameFocusGained


    private void emptyText() {
        Component[] components = this.jPanel1.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }
    }

    private boolean checker(String value) {
        if (value.length() > 0) {
            if (!((value.charAt(0) >= 'A' && value.charAt(0) <= 'Z')
                    || (value.charAt(0) >= 'a' && value.charAt(0) <= 'z'))) {
                return false;
            }
        }
        if (value.length() > 1) {
            if (!((value.charAt(1) >= 'A' && value.charAt(1) <= 'Z')
                    || (value.charAt(1) >= 'a' && value.charAt(1) <= 'z'))) {
                return false;
            }
        }
        return true;
    }

    private void updateAccountDetails() {
        columnNames.clear();
        data.clear();

        if (fieldsAreValidates()) {
            if (DBController.isDatabaseConnected()) {
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);
                // Remove the account id as it is auto generated
                columnNames.remove(0);
            } else {
                DBController
                        .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                                DatabaseCredentials.DB_USERNAME,
                                DatabaseCredentials.DB_PASSWORD);
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.ACCOUNT_TABLE);

                // Remove the account id as it is auto generated
                columnNames.remove(0);
            }
            String partyname = txtAccountName.getText();

            data.add(txtAccountName.getText().trim());
            data.add(txtPrintName.getText());
            data.add(cmbGroup.getSelectedItem());

            if (!txtOpBalance.getText().trim().isEmpty()) {
                double opbalance = Double.parseDouble(txtOpBalance.getText());
                if (cmbDrCr.getSelectedItem().equals("Cr")) {
                    if (cmbGroup.getSelectedItem().equals("Customer")) {
                        opbalance *= -1;
                    }
                    if (cmbGroup.getSelectedItem().equals("Supplier")) {
                        opbalance *= -1;
                    }
                }
                data.add(String.format("%.2f", opbalance));
            } else {
                columnNames.remove("opbal");
            }
            if (!txtAddress.getText().trim().isEmpty()) {
                data.add(txtAddress.getText().trim());
            } else {
                columnNames.remove("address");
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
            if (!txtMobile1.getText().trim().isEmpty()) {
                data.add(txtMobile1.getText());
            } else {
                columnNames.remove("mobile1");
            }

            if (!txtMobile2.getText().trim().isEmpty()) {
                data.add(txtMobile2.getText());
            } else {
                columnNames.remove("mobile2");
            }

            if (!txtEmail.getText().trim().isEmpty()) {
                data.add(txtEmail.getText().trim());
            } else {
                columnNames.remove("email");
            }
            if (UtilityMethods.hasDateBeenPicked(dateBirthdayOn)) {
                data.add(dateFormat.format(dateBirthdayOn.getDate()));
            } else {
                columnNames.remove("birthday");
            }

            if (!txtGSTNo.getText().trim().isEmpty()) {
                data.add(txtGSTNo.getText());
            } else {
                columnNames.remove("gstno");
            }

            if (!txtAdharCardNo.getText().trim().isEmpty()) {
                data.add(txtAdharCardNo.getText());
            } else {
                columnNames.remove("adharno");
            }

            if (!txtVATNo.getText().trim().isEmpty()) {
                data.add(txtVATNo.getText());
            } else {
                columnNames.remove("vatno");
            }

            if (!txtPANNo.getText().trim().isEmpty()) {
                data.add(txtPANNo.getText().trim());
            } else {
                columnNames.remove("panno");
            }
            
            if (!txtState.getText().trim().isEmpty()) {
                data.add(txtState.getText().trim());
            } else {
                columnNames.remove("state");
            }
            if (!txtStateCode.getText().trim().isEmpty()) {
                data.add(txtStateCode.getText().trim());
            } else {
                columnNames.remove("statecode");
            }
            

            if (UtilityMethods.hasDateBeenPicked(dateAnniversary)) {
                data.add(dateFormat.format(dateAnniversary.getDate()));
            } else {
                columnNames.remove("anniversary");
            }

            data.add(cmbDrCr.getSelectedItem());
            if (supplierCustomerCheckBox.isSelected()) {
                data.add(1);
            } else {
                data.add(1);
            }

            if (!txtArea.getText().trim().isEmpty()) {
                data.add(txtArea.getText().trim());
            } else {
                columnNames.remove("area");
            }
            data.add(0);
            boolean insertionResult = DBController.updateTableData(DatabaseCredentials.ACCOUNT_TABLE,
                    data, columnNames, "id", tempid);

            if (insertionResult) {
                JOptionPane.showMessageDialog(this, "Account has been updated successfully");
                emptyText();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnListOfAccounts;
    private javax.swing.JButton btnSaveAccount;
    public javax.swing.JButton btnUpdateAccount;
    private javax.swing.JComboBox<String> cmbDrCr;
    private javax.swing.JComboBox<String> cmbGroup;
    private com.toedter.calendar.JDateChooser dateAnniversary;
    private com.toedter.calendar.JDateChooser dateBirthdayOn;
    private javax.swing.JTextField ifscCode;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblNewAccountImage;
    private javax.swing.JCheckBox supplierCustomerCheckBox;
    private javax.swing.JTextField txtAccountName;
    private javax.swing.JTextField txtAccountNumber;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAdharCardNo;
    private javax.swing.JTextField txtArea;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGSTNo;
    private javax.swing.JTextField txtMobile1;
    private javax.swing.JTextField txtMobile2;
    private javax.swing.JTextField txtOpBalance;
    private javax.swing.JTextField txtPANNo;
    private javax.swing.JTextField txtPincode;
    private javax.swing.JTextField txtPrintName;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtStateCode;
    private javax.swing.JTextField txtVATNo;
    // End of variables declaration//GEN-END:variables
}
