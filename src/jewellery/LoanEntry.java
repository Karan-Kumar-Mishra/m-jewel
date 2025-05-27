
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import jewellery.GetInterestAmount;

public class LoanEntry extends javax.swing.JPanel {

    private String[] partyNames;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPartyName;
    private javax.swing.JLabel jLabelPartyAddress;
    private javax.swing.JLabel jLabelPartyCity;
    private javax.swing.JLabel jLabelPartyMobile;
    private javax.swing.JLabel jLabelPartyEmail;
    private javax.swing.JLabel jLabelPartyLedgerBalance;
    private javax.swing.JLabel jLabelPartyLastEntry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private DefaultListModel<String> imageListModel;

    public LoanEntry() {
        initComponents();
        addEnterKeyListenerToTextFields();
        addEnterKeyListenerToComboBoxes();
        addFocusListenerToTextFields();

        // Set layout manager for the panel
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1400, 1000)); // Add jPanel5 with constraints
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        DatabaseTableCreator.create();
        int slip_number = TableRowCounter.getRowCount("LOAN_ENTRY");
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField20 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel36 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        jLabelPartyName = new javax.swing.JLabel();
        jLabelPartyAddress = new javax.swing.JLabel();
        jLabelPartyCity = new javax.swing.JLabel();
        jLabelPartyMobile = new javax.swing.JLabel();
        jLabelPartyEmail = new javax.swing.JLabel();
        jLabelPartyLedgerBalance = new javax.swing.JLabel();
        jLabelPartyLastEntry = new javax.swing.JLabel();
        imageListModel = new DefaultListModel<>();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Masters");
        jLabel3.setText("Transaction");
        jLabel4.setText("Reports");
        jLabel5.setText("Stock Reports");
        jLabel6.setText("GST Reports");
        jLabel7.setText("Tools");
        jLabel8.setText("Help");

        jTextField17.setText(LocalDate.now().toString());
        disableAllInputs();

        jTextField18.setText(String.valueOf(slip_number));
        jTextField26.setText(LocalDate.now().toString());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);

        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addContainerGap(686, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)))
        );
        jPanel5.setBackground(new java.awt.Color(57, 68, 76));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Serif", 1, 18));
        jLabel2.setForeground(new java.awt.Color(255, 204, 0));
        jLabel2.setText("LOAN ENTRY");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(57, 68, 76));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setForeground(new java.awt.Color(255, 204, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel9.setForeground(new java.awt.Color(255, 204, 0));
        jLabel9.setText("Loan Details");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 110, 30));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 30));

        jLabel12.setText("Start on Interest Date");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel13.setText("Interest Rate[%]");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 110, -1));

        jLabel14.setText("Weight Type(Gold/Silver)");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 160, 30));

        jLabel15.setText("Purity[%]");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 80, 20));

        jLabel16.setText("Net Weight:");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 160, 20));

        jLabel17.setText("Gold Weight:");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 136, -1, 20));

        jLabel18.setText("Estimated Cost:");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 140, -1));

        jLabel19.setText("Amount Paid:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 160, 20));

        jLabel20.setText("Items Details:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 150, 30));
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 180, 20));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Gold", "Silver"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        partyNames = GetPartyName.get();

        if (partyNames != null && partyNames.length > 0) {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(partyNames));
        } else {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No Parties Available"}));
        }

        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 180, 20));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });
        jTextField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField20ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 180, -1));
        jPanel2.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 180, -1));
        jPanel2.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 180, -1));
        jPanel2.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 180, 70));
        jPanel2.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 180, -1));

        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 180, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 70, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Day", "Month"}));
        jPanel2.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 110, -1));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 360, 380));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(57, 68, 76));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel10.setForeground(new java.awt.Color(255, 204, 0));
        jLabel10.setText("Guarantor Details");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 130, 30));

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 30));
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel22.setText("Items Location:");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 150, 20));

        jLabel23.setText("Guarantor Name:");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel24.setText("Guarantor Address:");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel25.setText("Guarantor Phone:");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, 20));

        jLabel26.setText("Reminders:");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 90, 20));

        jLabel27.setText("Documents:");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 140, 20));

        jLabel28.setText("Notes:");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 140, 30));
        jPanel3.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 180, -1));
        jPanel3.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 180, -1));
        jPanel3.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 180, -1));
        jPanel3.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 180, -1));
        jPanel3.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 180, -1));
        jPanel3.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 180, -1));
        jPanel3.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 180, -1));
        jPanel3.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 180, -1));
        jPanel3.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 180, -1));

        jButton5.setText("Add Item's Images");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 180, -1));

        jButton6.setText("Add Guarantor Image");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt); 
            }
        });
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 180, -1));

        jButton1.setText("Save");
        jButton1.setBackground(Color.blue);
        jButton1.setForeground(Color.white);
        jButton1.setFont(new Font("Serif", Font.PLAIN, 18));
            
            
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 180, -1));

        jPanel5.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 370, 380));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel8.setBackground(new java.awt.Color(57, 68, 76));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setForeground(new java.awt.Color(255, 204, 0));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel11.setForeground(new java.awt.Color(255, 204, 0));
        jLabel11.setText("Party Details");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel11)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 36, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel11)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        jLabel29.setText("Name:");
        jLabel30.setText("Address:");
        jLabel31.setText("City");
        jLabel32.setText("Mobile:");
        jLabel33.setText("Email ID:");
        jLabel34.setText("Ledger Balance:");
        jLabel35.setText("Last Entry:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 110,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabelPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyCity, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyLedgerBalance, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPartyLastEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 59,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 20)
                                .addComponent(jLabel29)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyName))
                                .addGap(0, 5, 10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyAddress))
                                .addGap(0, 5, 10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel31)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyCity))
                                .addGap(0, 5, 10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyMobile))
                                .addGap(0, 5, 10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyEmail))
                                .addGap(0, 5, 10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyLedgerBalance))
                                .addGap(0, 5, 10)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel35)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelPartyLastEntry))
                                .addGap(0, 5, 10)));

        jPanel5.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 160, 240, 380));
        jPanel5.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        jLabel36.setForeground(new java.awt.Color(255, 204, 0));
        jLabel36.setText("Date");
        jPanel5.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 60, 20));

        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 100, -1));

        jLabel37.setForeground(new java.awt.Color(255, 204, 0));
        jLabel37.setText("Slip No");
        jPanel5.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 80, -1));
        jPanel5.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 120, -1));

        jLabel38.setForeground(new java.awt.Color(255, 204, 0));
        jLabel38.setText("Party Name");
        jPanel5.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, -1, -1));

        jPanel5.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 430, -1));

        jLabel39.setForeground(new java.awt.Color(255, 204, 0));
        jLabel39.setText("Remarks");
        jPanel5.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 110, 190, -1));

        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });
        jTextField26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField126ActionPerformed(evt);
            }
        });
        jTextField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField22ActionPerformed(evt);
            }
        });

        jPanel5.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 130, 300, -1));

        jButton2.setText("New Enter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 90, -1));

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 90, -1));

        jButton4.setBackground(new java.awt.Color(255, 0, 0));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton4.setText("Close");
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 90, -1));

        jButton7.setText("Refresh");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 90, -1));

        jButton8.setText("Print");
        jButton8.setSize(40, 10);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               
            }
        });
        jPanel5.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 90, -1));
    }

    private void clearAllTextBox() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField12.setText("");
        jTextField13.setText("");
        jTextField14.setText("");
        jTextField17.setText(LocalDate.now().toString());
        jTextField18.setText(String.valueOf(TableRowCounter.getRowCount("LOAN_ENTRY") + 1));
        jTextField19.setText("");
        jTextField20.setText("");
        jTextField22.setText("");
        jTextField23.setText("");
        jTextField24.setText("");
        jTextField25.setText("");
        jTextField26.setText(LocalDate.now().toString());

        jComboBox1.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
    }

    private void addEnterKeyListenerToTextFields() {
        if (jTextField1 != null) {
            addEnterKeyListener(jTextField1);
        }
        if (jTextField2 != null) {
            addEnterKeyListener(jTextField2);
        }
        if (jTextField6 != null) {
            addEnterKeyListener(jTextField6);
        }
        if (jTextField7 != null) {
            addEnterKeyListener(jTextField7);
        }
        if (jTextField8 != null) {
            addEnterKeyListener(jTextField8);
        }
        if (jTextField9 != null) {
            addEnterKeyListener(jTextField9);
        }
        if (jTextField10 != null) {
            addEnterKeyListener(jTextField10);
        }
        if (jTextField11 != null) {
            addEnterKeyListener(jTextField11);
        }
        if (jTextField12 != null) {
            addEnterKeyListener(jTextField12);
        }
        if (jTextField13 != null) {
            addEnterKeyListener(jTextField13);
        }
        if (jTextField14 != null) {
            addEnterKeyListener(jTextField14);
        }
        if (jTextField17 != null) {
            addEnterKeyListener(jTextField17);
        }
        if (jTextField18 != null) {
            addEnterKeyListener(jTextField18);
        }
        if (jTextField19 != null) {
            addEnterKeyListener(jTextField19);
        }
        if (jTextField20 != null) {
            addEnterKeyListener(jTextField20);
        }
        if (jTextField22 != null) {
            addEnterKeyListener(jTextField22);
        }
        if (jTextField23 != null) {
            addEnterKeyListener(jTextField23);
        }
        if (jTextField24 != null) {
            addEnterKeyListener(jTextField24);
        }
        if (jTextField25 != null) {
            addEnterKeyListener(jTextField25);
        }
        if (jTextField26 != null) {
            addEnterKeyListener(jTextField26);
        }
    }

    private void addEnterKeyListenerToComboBoxes() {
        addEnterKeyListener(jComboBox1);
        addEnterKeyListener(jComboBox2);
        addEnterKeyListener(jComboBox3);
    }

    private void addEnterKeyListener(java.awt.Component component) {
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    component.transferFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void addFocusListenerToTextFields() {
        addFocusListener(jTextField17);
        addFocusListener(jTextField18);
        addFocusListener(jTextField26);
        addFocusListener(jTextField20);
        addFocusListener(jTextField23);
        addFocusListener(jTextField1);
    }

    private void addFocusListener(JTextField textField) {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField == jTextField17) {
                    //    jTextField17.setText(LocalDate.now().toString());
                } else if (textField == jTextField18) {

                } else if (textField == jTextField26) {
                    //  jTextField26.setText(LocalDate.now().toString());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() == jTextField20 || e.getSource() == jTextField23 || e.getSource() == jTextField1) {
                    calculateNetWeight();
                }
            }
        });
    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        Object selectedItem = jComboBox2.getSelectedItem();
        System.out.println("Selected party: " + selectedItem.toString());
        String details[] = getPartyDetailInLoan.get(selectedItem.toString());

        jLabelPartyName.setText(details[0]);
        jLabelPartyAddress.setText(details[1]);
        jLabelPartyCity.setText(details[2]);
        jLabelPartyMobile.setText(details[3]);
        jLabelPartyEmail.setText(details[4]);
    }

    private void calculateNetWeight() {
        if (jTextField20.getText().trim().isEmpty()
                || jTextField23.getText().trim().isEmpty()
                || jTextField1.getText().trim().isEmpty()) {
            return;
        }

        try {
            double goldWeight = Double.parseDouble(jTextField20.getText());
            double purity = Double.parseDouble(jTextField23.getText());
            double interest = Double.parseDouble(jTextField1.getText());
            double netWeight = goldWeight * (purity / 100);
            jTextField22.setText(String.format("%.4f", netWeight));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for gold weight, purity, and interest.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getSelectedPartyName() {
        Object selectedItem = jComboBox2.getSelectedItem();
        return selectedItem.toString();
    }

    public void disableAllInputs() {
        jTextField1.setEnabled(false);
        jTextField2.setEnabled(false);
        jTextField6.setEnabled(false);
        jTextField7.setEnabled(false);
        jTextField8.setEnabled(false);
        jTextField9.setEnabled(false);
        jTextField10.setEnabled(false);
        jTextField11.setEnabled(false);
        jTextField12.setEnabled(false);
        jTextField13.setEnabled(false);
        jTextField14.setEnabled(false);
        jTextField17.setEnabled(false);
        jTextField18.setEnabled(false);
        jTextField19.setEnabled(false);
        jTextField20.setEnabled(false);
        jTextField22.setEnabled(false);
        jTextField23.setEnabled(false);
        jTextField24.setEnabled(false);
        jTextField25.setEnabled(false);
        jTextField26.setEnabled(false);

        jComboBox1.setEnabled(false);
        jComboBox2.setEnabled(false);
        jComboBox3.setEnabled(false);

        jButton1.setEnabled(false);
      ///  jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jButton7.setEnabled(false);
        jButton8.setEnabled(false);
    }

    public void enableAllInputs() {
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);
        jTextField6.setEnabled(true);
        jTextField7.setEnabled(true);
        jTextField8.setEnabled(true);
        jTextField9.setEnabled(true);
        jTextField10.setEnabled(true);
        jTextField11.setEnabled(true);
        jTextField12.setEnabled(true);
        jTextField13.setEnabled(true);
        jTextField14.setEnabled(true);
        jTextField17.setEnabled(true);
        jTextField18.setEnabled(true);
        jTextField19.setEnabled(true);
        jTextField20.setEnabled(true);
        jTextField22.setEnabled(true);
        jTextField23.setEnabled(true);
        jTextField24.setEnabled(true);
        jTextField25.setEnabled(true);
        jTextField26.setEnabled(true);

        jComboBox1.setEnabled(true);
        jComboBox2.setEnabled(true);
        jComboBox3.setEnabled(true);

        jButton1.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
        jButton6.setEnabled(true);
        jButton7.setEnabled(true);
        jButton8.setEnabled(true);
    }

    private void uploadImage() {
        FileDialog fileDialog = new FileDialog((Frame) null, "Select an Image", FileDialog.LOAD);
        fileDialog.setFilenameFilter((dir, name)
                -> name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".png") || name.endsWith(".gif")
        );

        fileDialog.setVisible(true);

        String selectedFile = fileDialog.getFile();
        String selectedDirectory = fileDialog.getDirectory();

        if (selectedFile != null) {
            String filePath = selectedDirectory + selectedFile;

            Object selectedItem = jComboBox2.getSelectedItem();
            File imagesFolder = new File("assets/" + selectedItem.toString());
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            try {
                Path sourcePath = Paths.get(filePath);
                Path destinationPath = Paths.get("assets/" + selectedItem.toString(), "GUARNATOR_PHOTO.png");
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                imageListModel.addElement(destinationPath.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving image: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ImageSelector() {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.dispose();
            }
        });

        DefaultListModel<String> imageListModel = new DefaultListModel<>();
        JList<String> imageList = new JList<>(imageListModel);
        JButton uploadButton = new JButton("Upload Image");
        JButton closeButton = new JButton("Close");
        JLabel imageLabel = new JLabel();
        int[] imageNumber = {0};

        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Image List"));

        JScrollPane listScrollPane = new JScrollPane(imageList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(uploadButton);
        buttonPanel.add(closeButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Selected Image"));
        rightPanel.add(imageLabel, BorderLayout.CENTER);

        frame.getContentPane().setLayout(new GridLayout(1, 2));
        frame.getContentPane().add(leftPanel);
        frame.getContentPane().add(rightPanel);

        uploadButton.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog((Frame) null, "Select an Image", FileDialog.LOAD);
            fileDialog.setFilenameFilter((dir, name)
                    -> name.endsWith(".jpg") || name.endsWith(".jpeg")
                    || name.endsWith(".png") || name.endsWith(".gif")
            );

            fileDialog.setVisible(true);

            String selectedFile = fileDialog.getFile();
            String selectedDirectory = fileDialog.getDirectory();

            if (selectedFile != null) {
                String filePath = selectedDirectory + selectedFile;
                imageListModel.addElement(filePath);
                Object selectedItem = jComboBox2.getSelectedItem();
                File imagesFolder = new File("assets/" + selectedItem.toString() + "/itemsImages");
                if (!imagesFolder.exists()) {
                    imagesFolder.mkdirs();
                }
                try {
                    Path sourcePath = Paths.get(filePath);
                    Path destinationPath = Paths.get("assets/" + selectedItem.toString() + "/itemsImages", "ITEM_PHOTO" + imageNumber[0] + ".png");
                    imageNumber[0]++;
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving image: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        closeButton.addActionListener(e -> frame.dispose());

        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedImagePath = imageList.getSelectedValue();
                if (selectedImagePath != null) {
                    ImageIcon imageIcon = new ImageIcon(selectedImagePath);
                    Image image = imageIcon.getImage();

                    int labelWidth = imageLabel.getWidth();
                    int labelHeight = imageLabel.getHeight();

                    if (labelWidth > 0 && labelHeight > 0) {
                        Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    }
                } else {
                    imageLabel.setIcon(null);
                }
            }
        });

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // java.util.logging.Logger.getLogger(ImageSelectorSingleFunction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        frame.pack();
        frame.setVisible(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        clearAllTextBox();

        jLabelPartyName.setText("");
        jLabelPartyAddress.setText("");
        jLabelPartyCity.setText("");
        jLabelPartyMobile.setText("");
        jLabelPartyEmail.setText("");
        jLabelPartyLedgerBalance.setText("");
        jLabelPartyLastEntry.setText("");

        jTextField17.setText(LocalDate.now().toString());
        jTextField26.setText(LocalDate.now().toString());

        int slip_number = TableRowCounter.getRowCount("LOAN_ENTRY") + 1;
        jTextField18.setText(String.valueOf(slip_number));

        enableAllInputs();

        jTextField17.requestFocus();
    }

    public javax.swing.JPanel getContentPane() {
        return this;
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(true);
    }

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {
        jTextField13.transferFocus();
    }

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField126ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {
        calculateNetWeight();
    }

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {
    }


    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        ImageSelector();
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        uploadImage();
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        partyNames = GetPartyName.get();

        if (partyNames != null && partyNames.length > 0) {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(partyNames));
        } else {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No Parties Available"}));
        }

        clearAllTextBox();

        jLabelPartyName.setText("");
        jLabelPartyAddress.setText("");
        jLabelPartyCity.setText("");
        jLabelPartyMobile.setText("");
        jLabelPartyEmail.setText("");
        jLabelPartyLedgerBalance.setText("");
        jLabelPartyLastEntry.setText("");

        jTextField17.setText(LocalDate.now().toString());
        jTextField26.setText(LocalDate.now().toString());
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JTabbedPane tabbedPane = (javax.swing.JTabbedPane) this.getParent();
        if (tabbedPane != null) {
            tabbedPane.remove(this);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("interest date => " + jTextField1.getText());
        System.out.println("Estimated cost => " + jTextField2.getText());
        System.out.println("items location => " + jTextField6.getText());
        System.out.println("guarantor name => " + jTextField7.getText());
        System.out.println("guarantor location => " + jTextField8.getText());
        System.out.println("guarantor phone no. => " + jTextField9.getText());
        System.out.println("Documents => " + jTextField10.getText());
        System.out.println("Reminders => " + jTextField11.getText());
        System.out.println("Notes => " + jTextField13.getText());
        System.out.println("Date => " + jTextField17.getText());
        System.out.println("slip no => " + jTextField18.getText());
        System.out.println("Top remarks => " + jTextField19.getText());
        System.out.println("Gold weight => " + jTextField20.getText());
        System.out.println("net weight => " + jTextField22.getText());
        System.out.println("purity => " + jTextField23.getText());
        System.out.println("Amount paid => " + jTextField24.getText());
        System.out.println("Item details => " + jTextField25.getText());
        System.out.println("start date => " + jTextField26.getText());
        System.out.println("weight type => " + jComboBox1.getSelectedItem());

        Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, "interest date => " + jTextField1.getText());

        Object selectedItem = jComboBox2.getSelectedItem();

        String entryDate = jTextField17.getText();
        String slipNo = jTextField18.getText().isEmpty() ? " " : jTextField18.getText();
        String partyName = selectedItem == null ? " " : selectedItem.toString();
        String remarks = jTextField19.getText().isEmpty() ? " " : jTextField19.getText();
        String startDate = jTextField26.getText().isEmpty() ? LocalDate.now().toString() : jTextField26.getText();

        String interestDatePercentage = jTextField1.getText().isEmpty() ? "0" : jTextField1.getText();
        String interestType = jComboBox3.getSelectedItem().toString();
        String weightType = jComboBox1.getSelectedItem() == null ? " " : jComboBox1.getSelectedItem().toString();
        String goldWeight = jTextField20.getText().isEmpty() ? "0" : jTextField20.getText();
        String purity = jTextField23.getText().isEmpty() ? "0" : jTextField23.getText();
        String netWeight = jTextField22.getText().isEmpty() ? "0" : jTextField22.getText();
        String estimatedCost = jTextField2.getText().isEmpty() ? "0" : jTextField2.getText();
        String amountPaid = jTextField24.getText().isEmpty() ? "0" : jTextField24.getText();
        String itemDetails = jTextField25.getText().isEmpty() ? " " : jTextField25.getText();
        String guarantorName = jTextField7.getText().isEmpty() ? " " : jTextField7.getText();
        String guarantorAddress = jTextField8.getText().isEmpty() ? " " : jTextField8.getText();
        String guarantorPhone = jTextField9.getText().isEmpty() ? " " : jTextField9.getText();
        String documents = jTextField10.getText().isEmpty() ? " " : jTextField10.getText();

        String reminders = jTextField11.getText().isEmpty() ? " " : jTextField11.getText();
        String notes = jTextField13.getText().isEmpty() ? " " : jTextField13.getText();
        String itemLocation = jTextField6.getText().isEmpty() ? "" : jTextField6.getText();

        InsertLoanDetails.insert(
                entryDate,
                slipNo,
                partyName,
                remarks,
                startDate,
                interestDatePercentage,
                interestType,
                weightType,
                goldWeight,
                purity,
                netWeight,
                estimatedCost,
                amountPaid,
                itemDetails,
                guarantorName,
                guarantorAddress,
                guarantorPhone,
                documents,
                reminders,
                notes,
                itemLocation,
                "0.0"
        );
        GetInterestAmount.updateAllInterestAmounts();
        clearAllTextBox();
        disableAllInputs();
    }

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField118ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
    }
}
