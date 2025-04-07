/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author pawantiwari
 */
public class CashBookSummary extends javax.swing.JFrame {

    /**
     * Creates new form CashBookSummary
     */
    private static DefaultTableModel cashBookSummary;
    private static List<List<Object>> selectedDatesData;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public CashBookSummary() {
        initComponents();
        Color customColor = new Color(0x39, 0x44, 0x4C);
        getContentPane().setBackground(customColor);
        
        cashBookSummary = (DefaultTableModel) jTable2.getModel();
    }
    
    
    private void populateCashBookTable(String fromDate, String toDate) {
        cashBookSummary.setRowCount(0);

        try {
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            // Query for receipts
            List<List<Object>> receiptData = DBController.getDataFromTable(
                "SELECT date, SUM(amtpaid) AS receipt_amtpaid " +
                "FROM " + DatabaseCredentials.RECEIPT_TABLE + " " +
                "WHERE sales_Bill = -1 AND date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Query for payments
            List<List<Object>> paymentData = DBController.getDataFromTable(
                "SELECT date, SUM(amtpaid) AS payment_amtpaid " +
                "FROM " + DatabaseCredentials.PAYMENT_TABLE + " " +
                "WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Query for received amount
            List<List<Object>> receivedAmountData = DBController.getDataFromTable(
                "SELECT date, SUM(CAST(receivedamount AS DECIMAL)) AS received_amt " +
                "FROM " + DatabaseCredentials.SALES_TABLE + 
                " WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Map to hold results by date
            Map<String, Double[]> combinedData = new HashMap<>();

            // Process receipt data
            for (List<Object> row : receiptData) {
                String date = row.get(0).toString();
                Double receiptAmt = ((Number) row.get(1)).doubleValue();
                combinedData.put(date, new Double[]{receiptAmt, 0.0, 0.0}); // [receiptAmtpaid, paymentAmtpaid, receivedAmt]
            }

            // Process payment data and combine with receipt data
            for (List<Object> row : paymentData) {
                String date = row.get(0).toString();
                Double paymentAmt = ((Number) row.get(1)).doubleValue();
                combinedData.putIfAbsent(date, new Double[]{0.0, 0.0, 0.0});
                combinedData.get(date)[1] = paymentAmt;
            }

            // Process received amount data and add to receipt data
            for (List<Object> row : receivedAmountData) {
                String date = row.get(0).toString();
                Double receivedAmt = ((Number) row.get(1)).doubleValue();
                combinedData.putIfAbsent(date, new Double[]{0.0, 0.0, 0.0});
                combinedData.get(date)[2] = receivedAmt;
            }

            // Sort dates
            List<String> sortedDates = new ArrayList<>(combinedData.keySet());
            Collections.sort(sortedDates);

            // Track previous closing balance
            double previousClosing = 0.0;
            double finalOpening = 0; // Opening value from the first row only
            double totalReceipt = 0; // Sum of all receipt amounts
            double totalPayment = 0; // Sum of all payment amounts
            
            double finalClosing = 0; // Closing value from the last row

            // Add combined data to the table
            for (String date : sortedDates) {
                Double[] amounts = combinedData.get(date);
                double receiptAmtpaid = amounts[0];
                double paymentAmtpaid = amounts[1];
                double receivedAmt = amounts[2];

                // Add receivedAmt to receiptAmtpaid
                receiptAmtpaid += receivedAmt;

                // Opening is previous day's closing
                double opening = previousClosing;
                double closing = opening + (receiptAmtpaid - paymentAmtpaid);

                // Add row to cashBookSummary
                cashBookSummary.addRow(new Object[]{
                    date,
                    opening,
                    receiptAmtpaid,
                    paymentAmtpaid,
                    closing
                });

                totalReceipt += receiptAmtpaid;
                totalPayment += paymentAmtpaid;
                // Update previous closing for next iteration
                previousClosing = closing;
                finalOpening = opening;
                finalClosing = closing;
            }
            openingTxt.setText(String.valueOf(finalOpening));
            totalReceiptTxt.setText(String.valueOf(totalReceipt));
            totalPaymentTxt.setText(String.valueOf(totalPayment));
            closingTxt.setText(String.valueOf(finalClosing));

        } catch (Exception e) {
            Logger.getLogger(CashBookSummary.class.getName()).log(Level.SEVERE, "Error populating cash book table", e);
        }

    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        openingTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        totalReceiptTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        totalPaymentTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        closingTxt = new javax.swing.JTextField();
        from = new com.toedter.calendar.JDateChooser();
        to = new com.toedter.calendar.JDateChooser();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setBackground(new java.awt.Color(57, 68, 76));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("CASH BOOK SUMMARY");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("From");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("To");

        jButton1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jButton1.setText("OK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jButton2.setText("Print");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 0, 51));
        jButton3.setText("Close");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Opening", "Receipt", "Payment", "Closing"
            }
        ));
        jTable2.setRowHeight(25);
        jScrollPane2.setViewportView(jTable2);
        JTableHeader header = jTable2.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Opening:");

        openingTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openingTxtActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Total Receipt:");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Total Payment:");

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Closing:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1)
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(56, 56, 56)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(openingTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalReceiptTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalPaymentTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closingTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(openingTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(totalReceiptTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalPaymentTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addContainerGap(8, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(closingTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (!DBController.isDatabaseConnected()) {
                    DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
                }

                populateCashBookTable(dateFormat.format(from.getDate()),
                            dateFormat.format(to.getDate()));
                
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();
    }//GEN-LAST:event_jButton3MouseClicked

    private void printx(String fromDate, String toDate) {
        Connection connection = null;
        try {
            
            connection = DBConnect.connect();
            String query= "";
            
            List<Map<String, Object>> itemDataList = new ArrayList<>();
            HashMap<String, Object> parameters = new HashMap<>();
            
            
                            
            
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            // Query for receipts
            List<List<Object>> receiptData = DBController.getDataFromTable(
                "SELECT date, SUM(amtpaid) AS receipt_amtpaid " +
                "FROM " + DatabaseCredentials.RECEIPT_TABLE + " " +
                "WHERE sales_Bill = -1 AND date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Query for payments
            List<List<Object>> paymentData = DBController.getDataFromTable(
                "SELECT date, SUM(amtpaid) AS payment_amtpaid " +
                "FROM " + DatabaseCredentials.PAYMENT_TABLE + " " +
                "WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Query for received amount
            List<List<Object>> receivedAmountData = DBController.getDataFromTable(
                "SELECT date, SUM(CAST(receivedamount AS DECIMAL)) AS received_amt " +
                "FROM " + DatabaseCredentials.SALES_TABLE + 
                " WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
                "GROUP BY date"
            );

            // Map to hold results by date
            Map<String, Double[]> combinedData = new HashMap<>();

            // Process receipt data
            for (List<Object> row : receiptData) {
                String date = row.get(0).toString();
                Double receiptAmt = ((Number) row.get(1)).doubleValue();
                combinedData.put(date, new Double[]{receiptAmt, 0.0, 0.0}); // [receiptAmtpaid, paymentAmtpaid, receivedAmt]
            }

            // Process payment data and combine with receipt data
            for (List<Object> row : paymentData) {
                String date = row.get(0).toString();
                Double paymentAmt = ((Number) row.get(1)).doubleValue();
                combinedData.putIfAbsent(date, new Double[]{0.0, 0.0, 0.0});
                combinedData.get(date)[1] = paymentAmt;
            }

            // Process received amount data and add to receipt data
            for (List<Object> row : receivedAmountData) {
                String date = row.get(0).toString();
                Double receivedAmt = ((Number) row.get(1)).doubleValue();
                combinedData.putIfAbsent(date, new Double[]{0.0, 0.0, 0.0});
                combinedData.get(date)[2] = receivedAmt;
            }

            // Sort dates
            List<String> sortedDates = new ArrayList<>(combinedData.keySet());
            Collections.sort(sortedDates);

            // Track previous closing balance
            double previousClosing = 0.0;
            
            // Add combined data to the table
            for (String date : sortedDates) {
                Double[] amounts = combinedData.get(date);
                double receiptAmtpaid = amounts[0];
                double paymentAmtpaid = amounts[1];
                double receivedAmt = amounts[2];

                // Add receivedAmt to receiptAmtpaid
                receiptAmtpaid += receivedAmt;

                // Opening is previous day's closing
                double opening = previousClosing;
                double closing = opening + (receiptAmtpaid - paymentAmtpaid);

                Map<String, Object> itemParameters = new HashMap<>();
                    itemParameters.put("date", date);
                    itemParameters.put("opening", opening);
                    itemParameters.put("receipt", receiptAmtpaid);
                    itemParameters.put("purchase", paymentAmtpaid);
                    itemParameters.put("closing", closing);
                    

                    itemDataList.add(itemParameters);
                

                // Update previous closing for next iteration
                previousClosing = closing;
            }
            
            
            
            


            
            
            parameters.put("itemDataList", itemDataList);

            File reportFile = new File("jasper_reports" + File.separator + "cashbooksummary.jrxml");
            if (!reportFile.exists()) {
                throw new FileNotFoundException("Report file not found: " + reportFile.getAbsolutePath());
            }

            InputStream input = new FileInputStream(reportFile);
            JasperDesign design = JRXmlLoader.load(input);

            // Set the SQL query in the JasperReport design
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
            design.setQuery(updateQuery);

            // Set the real path for any images used in the report
            String realPath = System.getProperty("user.dir") + File.separator + "jasper_reports" + File.separator + "img" + File.separator;
            parameters.put("imagePath", realPath);

            // Compile the Jasper report
            JasperReport jreport = JasperCompileManager.compileReport(design);

            // Fill the report with data from the query, using the database connection
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemDataList);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, dataSource);

            // Display the report in JasperViewer
            JasperViewer.viewReport(jprint, false);

        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            Logger.getLogger(CashBookSummary.class.getName()).log(Level.SEVERE, null, ex);

        } 
    }


    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        printx(dateFormat.format(from.getDate()),
//                            dateFormat.format(to.getDate()));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        if (!DBController.isDatabaseConnected()) {
                    DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
                }

                populateCashBookTable(dateFormat.format(from.getDate()),
                            dateFormat.format(to.getDate()));
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        printx(dateFormat.format(from.getDate()),
                            dateFormat.format(to.getDate()));
    }//GEN-LAST:event_jButton2MouseClicked

    private void openingTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openingTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_openingTxtActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CashBookSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashBookSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashBookSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashBookSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CashBookSummary().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField closingTxt;
    private com.toedter.calendar.JDateChooser from;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField openingTxt;
    private com.toedter.calendar.JDateChooser to;
    private javax.swing.JTextField totalPaymentTxt;
    private javax.swing.JTextField totalReceiptTxt;
    // End of variables declaration//GEN-END:variables
}
