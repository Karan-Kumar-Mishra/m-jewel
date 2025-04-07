package jewellery;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import static jewellery.SelectAndCreateCompanyScreen.dateFY;

/**
 *
 * @author AR-LABS
 */
public class EditCompanyDetailsScreen extends javax.swing.JFrame {

    private List<Object> fieldsDataHolder = new ArrayList<>();
    private final List<Object> updationDataHolder = new ArrayList<>();
    private List<Object> columnsNamesHolder = new ArrayList<>();
    private List<List<Object>> states = new ArrayList<>();
    private static final SimpleDateFormat DATEFORMAT = 
            new SimpleDateFormat("yyyy-MM-dd");
    private int id;
    private ImageIcon image;
    
    public EditCompanyDetailsScreen() {
        initComponents();
        
        initCloseButtonEventCode();
        
        setImageOnComponent(lblBulbImage, AssetsLocations.LIGHT_BULB_IMAGE);
        
        populateDataFields();
        
        fetchStates();
    }

    private void emptyTextFields() {
        Component[] components = this.pnlParent.getComponents();
        
        for(Component component : components) {
            if(component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }
    }
    
    private void fetchStates() {
       
        states = DBController.getDataFromcompanyTable("SELECT * FROM " + 
                DatabaseCredentials.STATES_TABLE);
    }

    public void autoComplete(List<List<Object>> autoCompletionSource, 
            String textToAutoComplete, JTextField txtField) {
        String complete = "";
        int start = textToAutoComplete.length();
        int last = textToAutoComplete.length();
        
        for (int idx = 0; idx < autoCompletionSource.size(); idx++) {
            
            if (autoCompletionSource.get(idx).get(0).toString().toUpperCase()
                    .startsWith(textToAutoComplete.toUpperCase())) {
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
    
    private void setImageOnComponent(javax.swing.JLabel component, String resourceLocation) {
        image = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        component.setIcon(image);
    }
    
    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(EditCompanyDetailsScreen.this,
                        "Are you sure you want to exit the editing screen?", "Close Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    EditCompanyDetailsScreen.this.dispose();
                    
                }
            }
        });
    }
    
    private boolean validateFields() {
        if(UtilityMethods.isTextFieldEmpty(txtCompanyName)) {
            JOptionPane.showMessageDialog(this, "Please enter your company name");
            return false;
        }
        else if(UtilityMethods.isTextFieldEmpty(txtState)) {
            JOptionPane.showMessageDialog(this, "Please enter your state");
            return false;
        }
        else if(UtilityMethods.isTextFieldEmpty(txtStateCode)) {
            JOptionPane.showMessageDialog(this, "Please enter state code");
            return false;
        }
        else if(!UtilityMethods.hasDateBeenPicked(dateFYStart)) {
            JOptionPane.showMessageDialog(this, "Please select financial year start date");
            return false;
        }
        else if(!UtilityMethods.hasDateBeenPicked(dateFYClose)) {
            JOptionPane.showMessageDialog(this, "Please select financial year end date");
            return false;
        }
        return true;
    }
    
    private void clearUpdationDataHolder() {
        updationDataHolder.clear();
    }
    
    private void clearFieldsDataHolder() {
        fieldsDataHolder.clear();
    }
    
    private void populateDataFields() {
        clearFieldsDataHolder();
        
            fieldsDataHolder = DBController.executeQueryCompany("SELECT * FROM " 
                    + DatabaseCredentials.COMPANY_TABLE 
                    + " WHERE companyname = " + "'" + GLOBAL_VARS.SELECTED_COMPANY + "' and startfinancialyear='"+GLOBAL_VARS.SELECTED_COMPANY_FYYEAR+"' ");
            
            id = Integer.parseInt(fieldsDataHolder.get(0).toString());
            
            
            if(fieldsDataHolder.get(1) != null) {
                txtUsername.setText(fieldsDataHolder.get(1).toString());
            }
            
            if(fieldsDataHolder.get(2) != null) {
                txtPassword.setText(fieldsDataHolder.get(2).toString());
            }
            
            if(fieldsDataHolder.get(3) != null) {
                txtCompanyName.setText(fieldsDataHolder.get(3).toString());
            }
            
            if(fieldsDataHolder.get(4) != null) {
                txtAddressLine1.setText(fieldsDataHolder.get(4).toString());
            }
            
            if(fieldsDataHolder.get(5) != null) {
                txtAddressLine2.setText(fieldsDataHolder.get(5).toString());
            }
            
            if(fieldsDataHolder.get(6) != null) {
                txtCity.setText(fieldsDataHolder.get(6).toString());
            }
            
            if(fieldsDataHolder.get(7) != null) {
                txtPincode.setText(fieldsDataHolder.get(7).toString());
            }
            
            if(fieldsDataHolder.get(8) != null) {
                txtState.setText(fieldsDataHolder.get(8).toString());
            }
            
            if(fieldsDataHolder.get(9) != null) {
                txtStateCode.setText(fieldsDataHolder.get(9).toString());
            }
            
            if(fieldsDataHolder.get(10) != null) {
                txtDistrict.setText(fieldsDataHolder.get(10).toString());
            }
            
            if(fieldsDataHolder.get(11) != null) {
                txtEmail.setText(fieldsDataHolder.get(11).toString());
            }
            
            if(fieldsDataHolder.get(12) != null) {
                txtWebsite.setText(fieldsDataHolder.get(12).toString());
            }
            
            if(fieldsDataHolder.get(13) != null) {
                txtPhone.setText(fieldsDataHolder.get(13).toString());
            }
            
            if(fieldsDataHolder.get(14) != null) {
                txtGSTNo.setText(fieldsDataHolder.get(14).toString());
            }
            
            if(fieldsDataHolder.get(15) != null) {
                txtVATNo.setText(fieldsDataHolder.get(15).toString());
            }

            try {
                dateFYStart.setDate(DATEFORMAT.parse(fieldsDataHolder.get(16).toString()));
                dateFYClose.setSelectableDateRange(DATEFORMAT.parse(fieldsDataHolder.get(16).toString()), null);
                dateFYClose.setDate(DATEFORMAT.parse(fieldsDataHolder.get(17).toString()));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            
            if(fieldsDataHolder.get(18) != null) {
                txtDLNo1.setText(fieldsDataHolder.get(18).toString());
            }
            
            if(fieldsDataHolder.get(19) != null) {
                txtDLNo2.setText(fieldsDataHolder.get(19).toString());
            }
            
            if(fieldsDataHolder.get(20) != null) {
                txtDealsIn.setText(fieldsDataHolder.get(20).toString());
            }
            
            if(fieldsDataHolder.get(21) != null) {
                txtTaxSystem.setText(fieldsDataHolder.get(21).toString());
            }

            //cmbCountrySid.setSelectedItem(fieldsDataHolder.get(22));            
        
    }
    
    private void updateData() throws FileNotFoundException {
        clearUpdationDataHolder();
    
            columnsNamesHolder.clear();
            columnsNamesHolder = DBController.getTableColumnNamescompany(DatabaseCredentials.COMPANY_TABLE);
            columnsNamesHolder.remove(0);

            updationDataHolder.add((txtUsername.getText()
                    .trim().isEmpty()) ? "1" : txtUsername.getText());
            updationDataHolder.add((txtPassword.getText()
                    .trim().isEmpty()) ? "1" : txtPassword.getText());
            
            updationDataHolder.add(txtCompanyName.getText());
            
            if(!txtAddressLine1.getText().trim().isEmpty()) {
                updationDataHolder.add(txtAddressLine1.getText());
            }
            else {
                columnsNamesHolder.remove("address1");
            }
            
            if(!txtAddressLine2.getText().trim().isEmpty()) {
                updationDataHolder.add(txtAddressLine2.getText());
            }
            else {
                columnsNamesHolder.remove("address2");
            }
            
            if(!txtCity.getText().trim().isEmpty()) {
                updationDataHolder.add(txtCity.getText());
            }
            else {
                columnsNamesHolder.remove("city");
            }
            
            if(!txtPincode.getText().trim().isEmpty()) {
                updationDataHolder.add(txtPincode.getText());
            }
            else {
                columnsNamesHolder.remove("pincode");
            }
            
            if(!txtState.getText().trim().isEmpty()) {
                updationDataHolder.add(txtState.getText());
            }
            else {
                columnsNamesHolder.remove("state");
            }
            
            if(!txtStateCode.getText().trim().isEmpty()) {
                updationDataHolder.add(txtStateCode.getText());
            }
            else {
                columnsNamesHolder.remove("statecode");
            }
            
            if(!txtDistrict.getText().trim().isEmpty()) {
                updationDataHolder.add(txtDistrict.getText());
            }
            else {
                columnsNamesHolder.remove("district");
            }
            
            if(!txtEmail.getText().trim().isEmpty()) {
                updationDataHolder.add(txtEmail.getText());
            }
            else {
                columnsNamesHolder.remove("email");
            }
            
            if(!txtWebsite.getText().trim().isEmpty()) {
                updationDataHolder.add(txtWebsite.getText());
            }
            else {
                columnsNamesHolder.remove("website");
            }
            
            if(!txtPhone.getText().trim().isEmpty()) {
                updationDataHolder.add(txtPhone.getText());
            }
            else {
                columnsNamesHolder.remove("phone");
            }
            
            if(!txtGSTNo.getText().trim().isEmpty()) {
                updationDataHolder.add(txtGSTNo.getText());
            }
            else {
                columnsNamesHolder.remove("gstno");
            }
            
            if(!txtVATNo.getText().trim().isEmpty()) {
                updationDataHolder.add(txtVATNo.getText());
            }
            else {
                columnsNamesHolder.remove("vatno");
            }

            updationDataHolder.add(dateFYStart.getDate());
            updationDataHolder.add(dateFYClose.getDate());
            dateFY = dateFYClose.getDate();
            
            if(!txtDLNo1.getText().trim().isEmpty()) {
                updationDataHolder.add(txtDLNo1.getText());
            }
            else {
                columnsNamesHolder.remove("dlno1");
            }
            
            if(!txtDLNo2.getText().trim().isEmpty()) {
                updationDataHolder.add(txtDLNo2.getText());
            }
            else {
                columnsNamesHolder.remove("dlno2");
            }
            
            if(!txtDealsIn.getText().trim().isEmpty()) {
                updationDataHolder.add(txtDealsIn.getText());
            }
            else {
                columnsNamesHolder.remove("dealsin");
            }
            
            if(!txtTaxSystem.getText().trim().isEmpty()) {
                updationDataHolder.add(txtTaxSystem.getText());
            }
            else {
                columnsNamesHolder.remove("taxsystem");
            }
            
            boolean updationSuccessfull = DBController.updateTableDataCompany(DatabaseCredentials.COMPANY_TABLE, 
                    updationDataHolder, columnsNamesHolder, "id", id);
            
            if(updationSuccessfull) {
                updateusers();
                JOptionPane.showMessageDialog(this, "Company data updated successfully");
                //emptyTextFields();
            }
            else {
                JOptionPane.showMessageDialog(this, "There was an error in "
                        + "updating the company data");
            }
        
    }
    private void updateusers(){
        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            String q1="Update users Set username='"+txtUsername.getText()+"', password='"+txtPassword.getText()+"', name='"+txtCompanyName.getText()+"' where userid=1";
            stmt.executeUpdate(q1);
            } catch (SQLException ex) {
                Logger.getLogger(EditCompanyDetailsScreen.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Company data notupdated successfully");
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
        btnUpdateCompany = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Edit and Organization");
        setResizable(false);

        pnlParent.setBackground(new java.awt.Color(57, 68, 76));

        pnlTopScreenRegion.setBackground(new java.awt.Color(57, 68, 76));
        pnlTopScreenRegion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));
        pnlTopScreenRegion.add(lblBulbImage);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Edit an Organization");
        pnlTopScreenRegion.add(jLabel2);

        pnlCompanyNameHolder.setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("<html>Name of the Company / Firm / Organization:<font color=\"red\">*</font></html>");
        pnlCompanyNameHolder.add(jLabel1);

        txtCompanyName.setPreferredSize(new java.awt.Dimension(200, 25));
        pnlCompanyNameHolder.add(txtCompanyName);

        pnlBasicDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlBasicDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Basic Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N
        pnlBasicDetailsHolder.setForeground(new java.awt.Color(238, 188, 81));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Address Line 1 :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Address Line 2 :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("City :");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Pincode :");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setText("<html>State :<font color=\"red\">*</font></html>");

        txtState.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStateKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setText("State Code:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setText("District :");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setText("Email :");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 188, 81));
        jLabel23.setText("Website :");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 188, 81));
        jLabel24.setText("Phone :");

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
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPincode, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(pnlBasicDetailsHolderLayout.createSequentialGroup()
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStateCode)
                            .addComponent(txtEmail)
                            .addComponent(txtPhone))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBasicDetailsHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtPincode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
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
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlFinancialYearRangeHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlFinancialYearRangeHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Financial Year Range", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setText("Financial Year Starting Date :");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("Financial Year Ending Date :");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateFYStart, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFinancialYearRangeHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateFYClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlRegistrationDetailsHolder.setBackground(new java.awt.Color(57, 68, 76));
        pnlRegistrationDetailsHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registration Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 20), new java.awt.Color(238, 188, 81))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setText("GST No :");

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

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 188, 81));
        jLabel17.setText("DL No 2 :");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setText("Deals In :");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setText("Tax System :");

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

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 188, 81));
        jLabel21.setText("Password :");

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
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
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

        btnUpdateCompany.setBackground(new java.awt.Color(0, 255, 0));
        btnUpdateCompany.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnUpdateCompany.setText("Update Company");
        btnUpdateCompany.setPreferredSize(new java.awt.Dimension(200, 35));
        btnUpdateCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateCompanyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 14, 15, 14);
        pnlButtonsHolder.add(btnUpdateCompany, gridBagConstraints);

        btnClose.setBackground(new java.awt.Color(255, 0, 0));
        btnClose.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(200, 35));
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
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
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlLicensingDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlBasicDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlFinancialYearRangeHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlSuperUserDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlRegistrationDetailsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlParentLayout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(pnlButtonsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlTopScreenRegion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlParentLayout.createSequentialGroup()
                .addComponent(pnlTopScreenRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlCompanyNameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(pnlFinancialYearRangeHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlRegistrationDetailsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlSuperUserDetailsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(pnlBasicDetailsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlLicensingDetailsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlButtonsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(109, 109, 109))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
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

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
       
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnUpdateCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCompanyActionPerformed
        if(validateFields()) {
            try {
                updateData();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EditCompanyDetailsScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnUpdateCompanyActionPerformed
 
    private void txtStateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStateKeyReleased
        if(states != null && states.isEmpty()) {
            fetchStates();
        }

        switch(evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
                break;
            
            case java.awt.event.KeyEvent.VK_ENTER:
                txtState.setText(txtState.getText());
            
                List<Object> stateCode = DBController.executeQueryCompany("SELECT state_code FROM " 
                        + DatabaseCredentials.STATES_TABLE + " WHERE state_name = " 
                        + "'" + txtState.getText().trim() + "'");
                
                if(stateCode.get(0) != null) {
                    txtStateCode.setText(stateCode.get(0).toString());
                }
                
                txtStateCode.requestFocusInWindow();
                
                break;
            
            default:
                EventQueue.invokeLater(() -> {
                    autoComplete(states, txtState.getText(), txtState);
                });
                break;
        }
    }//GEN-LAST:event_txtStateKeyReleased

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnUpdateCompany;
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
