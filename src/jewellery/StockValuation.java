/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import jewellery.helper.RealSettingsHelper;

/**
 *
 * @author pawantiwari
 */
public class StockValuation extends javax.swing.JFrame {

    /**
     * Creates new form StockValuation
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DefaultTableModel stockValuation;
    public StockValuation() {
        initComponents();
        Color customColor = new Color(0x39, 0x44, 0x4C);
        getContentPane().setBackground(customColor);
        
        String formattedDate = dateFormat.format(new Date());
        datetxt.setText(formattedDate);
        
        stockValuation = (DefaultTableModel) jTable1.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        datetxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("STOCK VALUATION");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Show Stock Valuation as on");

        datetxt.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        datetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datetxtActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jButton2.setText("Print");

        jButton3.setBackground(new java.awt.Color(255, 0, 51));
        jButton3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jButton3.setText("Close");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item Name", "Tag No.", "Stock", "Alt. Stock", "Value"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 972, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_datetxtActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
       DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();

    }//GEN-LAST:event_jButton3MouseClicked

    private void populateStockValuationTable(String date){
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
       
        String query1 = "SELECT DISTINCT itemname FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE ;
        
        List<List<Object>> suggestions1 = DBController.getDataFromTable(query1);

        

        suggestions1.forEach((itemname) -> {
            
            String query= "";
            if (!RealSettingsHelper.gettagNoIsTrue()) {
                query ="SELECT tagno, netwt, huid FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE 
                    + " WHERE (itemname = '" + itemname.get(0) + "' AND item_sold = 0 )" 
                     +  " OR (tagno = 'N.A' AND itemname = '" + itemname.get(0) + "' );";

            } else {
                query ="SELECT tagno, netwt, huid FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE 
                    + " WHERE (itemname = '" + itemname.get(0) + "' AND item_sold = 0 )" 
                     +  " OR (tagno = 'N.A' AND itemname = '" + itemname.get(0) + "');";


            }



            List<List<Object>> suggestions = DBController.getDataFromTable(query);
            stockValuation.setRowCount(0);


            if (!RealSettingsHelper.gettagNoIsTrue()) {
                suggestions.forEach((suggestion) -> {
                    if("N.A".equals(suggestion.get(0).toString())){

                        stockValuation.addRow(new Object[]{
                       
                        itemname.get(0),
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        "0",
                        "0",
                        "1538",});

                    }else{
                        stockValuation.addRow(new Object[]{
                        itemname.get(0),
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        "1",
                        "1",
                        "1510",});

                    }
                });


            } else {
                suggestions.forEach((suggestion) -> {

                    if("N.A".equals(suggestion.get(0).toString())){

                        stockValuation.addRow(new Object[]{
                       
                        itemname.get(0),
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        "0",
                        "0",
                        "1538",});

                    }else{
                        stockValuation.addRow(new Object[]{
                        itemname.get(0),
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        "1",
                        "1",
                        "1510",});

                    }
                });

            }
            
        });
        
        
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (!DBController.isDatabaseConnected()) {
                    DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
                }

                populateStockValuationTable(datetxt.getText());
                
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(StockValuation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockValuation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockValuation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockValuation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockValuation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField datetxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
