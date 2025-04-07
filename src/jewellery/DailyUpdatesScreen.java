package jewellery;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author AR-LABS
 */
public class DailyUpdatesScreen extends javax.swing.JFrame {

    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final String tableName = DatabaseCredentials.DAILY_UPDATE_TABLE;
    private List<Object> columnNames = new ArrayList<>();
    private List<Object> data = new ArrayList<>();
    private final String[] buttonTexts = {"Confirm Today's Rate", "Update"};
    
    public DailyUpdatesScreen() {
                Logger.getLogger(DailyUpdatesScreen.class.getName()).log(Level.INFO, "daily object initiazed!");

        initComponents();
        
        setDateOnJCalender("yyyy-MM-dd");
        
        dateTodaysRateDate.setEnabled(false);
        
        setFieldsIfDailyRateExists();
    }
    
    private void setFocus(KeyEvent event, JComponent component) {
        if(event.getKeyCode() == KeyEvent.VK_ENTER) {
            component.requestFocusInWindow();
        }
    }
    
    private void setFieldsIfDailyRateExists() {
        if (dailyRateExistsInDatabase()) {
            btnConfirmTodaysRate.setText(buttonTexts[1]);
            populateFields();

            txt16CaratRate.setEditable(false);
            txt18CaratRate.setEditable(false);
            txt22CaratRate.setEditable(false);
            txt24CaratRate.setEditable(false);
            txtSilverRate.setEditable(false);
        }
    }

    private boolean dailyRateExistsInDatabase() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> record = DBController.executeQuery("SELECT * FROM "
                + DatabaseCredentials.DAILY_UPDATE_TABLE + " WHERE date="
                + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");

        // If we got something, that means the daily update
        // for current date is already in the database
        return record != null && record.size() > 0;
    }
    
    private void populateFields() {
        if(!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
        
        if(data != null) {
            data.clear();
        }
        
        data = DBController.executeQuery("SELECT * FROM " + 
                DatabaseCredentials.DAILY_UPDATE_TABLE + " WHERE date=" + 
                "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
        
        txt16CaratRate.setText(data.get(1).toString());
        txt18CaratRate.setText(data.get(2).toString());
        txt22CaratRate.setText(data.get(3).toString());
        txt24CaratRate.setText(data.get(4).toString());
        txtSilverRate.setText(data.get(5).toString());
    }
    
    private void setDateOnJCalender(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();
         
        try {
            dateTodaysRateDate.setDate(dateFormat.parse(dateTimeFormatter.format(localDateTime)));
        } catch (ParseException ex) {
                                                Logger.getLogger(DailyUpdatesScreen.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private boolean fieldsAreValidated() {
        if(dateTodaysRateDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date");
            return false;
        }
//        else if(txt16CaratRate.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter the price of 16 carat gold");
//            return false;
//        }
//        else if(txt18CaratRate.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter the price of 18 carat gold");
//            return false;
//        }
//        else if(txt22CaratRate.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter the price of 22 carat gold");
//            return false;
//        }
//        else if(txt24CaratRate.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter the price of 24 carat gold");
//            return false;
//        }
//        else if(txtSilverRate.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter the price of silver");
//            return false;
//        }
        else if(!UtilityMethods.inputOnlyContainsNumbers(txt16CaratRate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter the rate correctly for 16 carat gold");
            return false;
        }
        else if(!UtilityMethods.inputOnlyContainsNumbers(txt18CaratRate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter the rate correctly for 18 carat gold");
            return false;
        }
        else if(!UtilityMethods.inputOnlyContainsNumbers(txt22CaratRate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter the rate correctly for 22 carat gold");
            return false;
        }
        else if(!UtilityMethods.inputOnlyContainsNumbers(txt24CaratRate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter the rate correctly for 24 carat gold");
            return false;
        }
        else if(!UtilityMethods.inputOnlyContainsNumbers(txtSilverRate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter the silver rate correctly");
            return false;
        }
        
        return true;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTopScreenRegion = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblFrameImage = new javax.swing.JLabel();
        pnlMiddleScreenRegion = new javax.swing.JPanel();
        pnlCurrentDateHolder = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        dateTodaysRateDate = new com.toedter.calendar.JDateChooser();
        pnlGoldRateInfoHolder = new javax.swing.JPanel();
        lblGoldRateImageLeft = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblGoldRateImageRight = new javax.swing.JLabel();
        lblGoldImage = new javax.swing.JLabel();
        pnlGoldRateInputHolder = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt16CaratRate = new javax.swing.JTextField();
        txt18CaratRate = new javax.swing.JTextField();
        txt22CaratRate = new javax.swing.JTextField();
        txt24CaratRate = new javax.swing.JTextField();
        pnlSilverRateInfoHolder = new javax.swing.JPanel();
        lblSilverRateImageLeft = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblSilverRateImageRight = new javax.swing.JLabel();
        lblSilverImage = new javax.swing.JLabel();
        pnlSilverRateInputHolder = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtSilverRate = new javax.swing.JTextField();
        pnlConfirmationButtonHolder = new javax.swing.JPanel();
        btnConfirmTodaysRate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Daily Updates");
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        pnlTopScreenRegion.setBackground(new java.awt.Color(48, 68, 76));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("Enter today's rate");
        pnlTopScreenRegion.add(jLabel1);

        getContentPane().add(pnlTopScreenRegion, java.awt.BorderLayout.PAGE_START);

        lblFrameImage.setBackground(new java.awt.Color(57, 68, 76));
        lblFrameImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFrameImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/professionalManImage.png"))); // NOI18N
        lblFrameImage.setPreferredSize(new java.awt.Dimension(200, 300));
        getContentPane().add(lblFrameImage, java.awt.BorderLayout.LINE_START);

        pnlMiddleScreenRegion.setLayout(new javax.swing.BoxLayout(pnlMiddleScreenRegion, javax.swing.BoxLayout.PAGE_AXIS));

        pnlCurrentDateHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 25));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 25)); // NOI18N
        jLabel2.setText("Today's Date :");
        pnlCurrentDateHolder.add(jLabel2);

        dateTodaysRateDate.setPreferredSize(new java.awt.Dimension(150, 30));
        pnlCurrentDateHolder.add(dateTodaysRateDate);

        pnlMiddleScreenRegion.add(pnlCurrentDateHolder);

        pnlGoldRateInfoHolder.setPreferredSize(new java.awt.Dimension(200, 93));
        pnlGoldRateInfoHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 0));

        lblGoldRateImageLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/lineLeftImage.png"))); // NOI18N
        lblGoldRateImageLeft.setPreferredSize(new java.awt.Dimension(97, 50));
        pnlGoldRateInfoHolder.add(lblGoldRateImageLeft);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 25)); // NOI18N
        jLabel3.setText("Today's Gold Rate / Gram");
        pnlGoldRateInfoHolder.add(jLabel3);

        lblGoldRateImageRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/lineRightImage.png"))); // NOI18N
        pnlGoldRateInfoHolder.add(lblGoldRateImageRight);

        lblGoldImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/goldBricksImage.png"))); // NOI18N
        pnlGoldRateInfoHolder.add(lblGoldImage);

        pnlMiddleScreenRegion.add(pnlGoldRateInfoHolder);

        java.awt.GridBagLayout pnlGoldRateInputHolderLayout = new java.awt.GridBagLayout();
        pnlGoldRateInputHolderLayout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0};
        pnlGoldRateInputHolderLayout.rowHeights = new int[] {0, 5, 0};
        pnlGoldRateInputHolder.setLayout(pnlGoldRateInputHolderLayout);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("16 Crt.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlGoldRateInputHolder.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("18 Crt.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        pnlGoldRateInputHolder.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("22 Crt.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        pnlGoldRateInputHolder.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("24 Crt.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        pnlGoldRateInputHolder.add(jLabel7, gridBagConstraints);

        txt16CaratRate.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt16CaratRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt16CaratRate.setPreferredSize(new java.awt.Dimension(120, 25));
        txt16CaratRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt16CaratRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt16CaratRateFocusLost(evt);
            }
        });
        txt16CaratRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt16CaratRateKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlGoldRateInputHolder.add(txt16CaratRate, gridBagConstraints);

        txt18CaratRate.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt18CaratRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt18CaratRate.setPreferredSize(new java.awt.Dimension(120, 25));
        txt18CaratRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt18CaratRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt18CaratRateFocusLost(evt);
            }
        });
        txt18CaratRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt18CaratRateKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        pnlGoldRateInputHolder.add(txt18CaratRate, gridBagConstraints);

        txt22CaratRate.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt22CaratRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt22CaratRate.setPreferredSize(new java.awt.Dimension(120, 25));
        txt22CaratRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt22CaratRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt22CaratRateFocusLost(evt);
            }
        });
        txt22CaratRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt22CaratRateKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        pnlGoldRateInputHolder.add(txt22CaratRate, gridBagConstraints);

        txt24CaratRate.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt24CaratRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt24CaratRate.setPreferredSize(new java.awt.Dimension(120, 25));
        txt24CaratRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt24CaratRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt24CaratRateFocusLost(evt);
            }
        });
        txt24CaratRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt24CaratRateKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        pnlGoldRateInputHolder.add(txt24CaratRate, gridBagConstraints);

        pnlMiddleScreenRegion.add(pnlGoldRateInputHolder);

        pnlSilverRateInfoHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 0));

        lblSilverRateImageLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/lineLeftImage.png"))); // NOI18N
        lblSilverRateImageLeft.setPreferredSize(new java.awt.Dimension(97, 50));
        pnlSilverRateInfoHolder.add(lblSilverRateImageLeft);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 25)); // NOI18N
        jLabel9.setText("Today's Silver Rate / Gram");
        pnlSilverRateInfoHolder.add(jLabel9);

        lblSilverRateImageRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/lineRightImage.png"))); // NOI18N
        lblSilverRateImageRight.setPreferredSize(new java.awt.Dimension(95, 50));
        pnlSilverRateInfoHolder.add(lblSilverRateImageRight);

        lblSilverImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/silverBrickImage.png"))); // NOI18N
        pnlSilverRateInfoHolder.add(lblSilverImage);

        pnlMiddleScreenRegion.add(pnlSilverRateInfoHolder);

        pnlSilverRateInputHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 25));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel8.setText("Silver Rate :");
        pnlSilverRateInputHolder.add(jLabel8);

        txtSilverRate.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txtSilverRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSilverRate.setPreferredSize(new java.awt.Dimension(120, 25));
        txtSilverRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSilverRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSilverRateFocusLost(evt);
            }
        });
        txtSilverRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSilverRateKeyReleased(evt);
            }
        });
        pnlSilverRateInputHolder.add(txtSilverRate);

        pnlMiddleScreenRegion.add(pnlSilverRateInputHolder);

        pnlConfirmationButtonHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 25));

        btnConfirmTodaysRate.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnConfirmTodaysRate.setText("Confirm Today's Rate");
        btnConfirmTodaysRate.setPreferredSize(new java.awt.Dimension(200, 30));
        btnConfirmTodaysRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnConfirmTodaysRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnConfirmTodaysRateFocusLost(evt);
            }
        });
        btnConfirmTodaysRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmTodaysRateActionPerformed(evt);
            }
        });
        btnConfirmTodaysRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnConfirmTodaysRateKeyReleased(evt);
            }
        });
        pnlConfirmationButtonHolder.add(btnConfirmTodaysRate);

        pnlMiddleScreenRegion.add(pnlConfirmationButtonHolder);

        getContentPane().add(pnlMiddleScreenRegion, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmTodaysRate() throws FileNotFoundException { 
        // buttonsTexts[0] is "Confirm Today's Rate"
        if (btnConfirmTodaysRate.getText().equals(buttonTexts[0])) {

            if (fieldsAreValidated()) {

                if (!DBController.isDatabaseConnected()) {
                    DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
                }
                
                columnNames.clear();
                data.clear();
                
                data.add(new SimpleDateFormat("yyyy-MM-dd").format(dateTodaysRateDate.getDate()));
               if(txt16CaratRate.getText().trim().isEmpty()){
                  data.add("0");  
               }else{
                     data.add(txt16CaratRate.getText().trim());
               }
               if(txt18CaratRate.getText().trim().isEmpty()){
                  data.add("0");  
               }else{
                   data.add(txt18CaratRate.getText().trim());
               }
               if(txt22CaratRate.getText().trim().isEmpty()){
                  data.add("0");  
               }else{
                   data.add(txt22CaratRate.getText().trim()); 
               }
               if(txt24CaratRate.getText().trim().isEmpty()){
                  data.add("0");  
               }else{
                  data.add(txt24CaratRate.getText().trim());  
               }
               if(txtSilverRate.getText().trim().isEmpty()){
                  data.add("0");  
               }else{
                  data.add(txtSilverRate.getText().trim()); 
               }
              
                
               
               
                

                columnNames = DBController.getTableColumnNames(DatabaseCredentials.DAILY_UPDATE_TABLE);

                if (!DBController.updateTableData(DatabaseCredentials.DAILY_UPDATE_TABLE,
                        data, columnNames, "date", UtilityMethods.getCurrentDate("yyyy-MM-dd"))) {

                    if(DBController.insertDataIntoTable(tableName, columnNames, data)) {
                        JOptionPane.showMessageDialog(this, "Daily Updated has been added successfully");
                    }

                }

                setFieldsIfDailyRateExists();
                ItemEntryScreen.populateItemGroupComboBox();
            }

        }
        else if(btnConfirmTodaysRate.getText().equals(buttonTexts[1])) {
            txt16CaratRate.setEditable(true);
            txt18CaratRate.setEditable(true);
            txt22CaratRate.setEditable(true);
            txt24CaratRate.setEditable(true);
            txtSilverRate.setEditable(true);
            
            btnConfirmTodaysRate.setText(buttonTexts[0]);
        }
    }
    
    private void btnConfirmTodaysRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmTodaysRateActionPerformed
        try {
            confirmTodaysRate();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DailyUpdatesScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConfirmTodaysRateActionPerformed

    private void btnConfirmTodaysRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnConfirmTodaysRateKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                confirmTodaysRate();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DailyUpdatesScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnConfirmTodaysRateKeyReleased

    private void txt16CaratRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt16CaratRateKeyReleased
        setFocus(evt, txt18CaratRate);
    }//GEN-LAST:event_txt16CaratRateKeyReleased

    private void txt18CaratRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt18CaratRateKeyReleased
        setFocus(evt, txt22CaratRate);
    }//GEN-LAST:event_txt18CaratRateKeyReleased

    private void txt22CaratRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt22CaratRateKeyReleased
        setFocus(evt, txt24CaratRate);
    }//GEN-LAST:event_txt22CaratRateKeyReleased

    private void txt24CaratRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt24CaratRateKeyReleased
        setFocus(evt, txtSilverRate);
    }//GEN-LAST:event_txt24CaratRateKeyReleased

    private void txtSilverRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSilverRateKeyReleased
        setFocus(evt, btnConfirmTodaysRate);
    }//GEN-LAST:event_txtSilverRateKeyReleased

    private void txt16CaratRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt16CaratRateFocusGained
        txt16CaratRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txt16CaratRateFocusGained

    private void txt16CaratRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt16CaratRateFocusLost
        txt16CaratRate.setBackground(Color.white);
    }//GEN-LAST:event_txt16CaratRateFocusLost

    private void txt18CaratRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt18CaratRateFocusGained
        txt18CaratRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txt18CaratRateFocusGained

    private void txt18CaratRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt18CaratRateFocusLost
        txt18CaratRate.setBackground(Color.white);
    }//GEN-LAST:event_txt18CaratRateFocusLost

    private void txt22CaratRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt22CaratRateFocusGained
        txt22CaratRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txt22CaratRateFocusGained

    private void txt22CaratRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt22CaratRateFocusLost
        txt22CaratRate.setBackground(Color.white);
    }//GEN-LAST:event_txt22CaratRateFocusLost

    private void txt24CaratRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt24CaratRateFocusGained
        txt24CaratRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txt24CaratRateFocusGained

    private void txt24CaratRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt24CaratRateFocusLost
        txt24CaratRate.setBackground(Color.white);
    }//GEN-LAST:event_txt24CaratRateFocusLost

    private void txtSilverRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSilverRateFocusGained
        txtSilverRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtSilverRateFocusGained

    private void txtSilverRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSilverRateFocusLost
        txtSilverRate.setBackground(Color.white);
    }//GEN-LAST:event_txtSilverRateFocusLost

    private void btnConfirmTodaysRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnConfirmTodaysRateFocusGained
        btnConfirmTodaysRate.setBackground(new Color(245, 230, 66));    }//GEN-LAST:event_btnConfirmTodaysRateFocusGained

    private void btnConfirmTodaysRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnConfirmTodaysRateFocusLost
        btnConfirmTodaysRate.setBackground(Color.white);
    }//GEN-LAST:event_btnConfirmTodaysRateFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmTodaysRate;
    private com.toedter.calendar.JDateChooser dateTodaysRateDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblFrameImage;
    private javax.swing.JLabel lblGoldImage;
    private javax.swing.JLabel lblGoldRateImageLeft;
    private javax.swing.JLabel lblGoldRateImageRight;
    private javax.swing.JLabel lblSilverImage;
    private javax.swing.JLabel lblSilverRateImageLeft;
    private javax.swing.JLabel lblSilverRateImageRight;
    private javax.swing.JPanel pnlConfirmationButtonHolder;
    private javax.swing.JPanel pnlCurrentDateHolder;
    private javax.swing.JPanel pnlGoldRateInfoHolder;
    private javax.swing.JPanel pnlGoldRateInputHolder;
    private javax.swing.JPanel pnlMiddleScreenRegion;
    private javax.swing.JPanel pnlSilverRateInfoHolder;
    private javax.swing.JPanel pnlSilverRateInputHolder;
    private javax.swing.JPanel pnlTopScreenRegion;
    private javax.swing.JTextField txt16CaratRate;
    private javax.swing.JTextField txt18CaratRate;
    private javax.swing.JTextField txt22CaratRate;
    private javax.swing.JTextField txt24CaratRate;
    private javax.swing.JTextField txtSilverRate;
    // End of variables declaration//GEN-END:variables
}
