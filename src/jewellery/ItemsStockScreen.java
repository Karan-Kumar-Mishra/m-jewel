/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jewellery.helper.RealSettingsHelper;
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
 * @author Sachin
 */
public class ItemsStockScreen extends javax.swing.JFrame {

    /**
     * Creates new form ItemsStockScreen
     */
    Logger logger = Logger.getLogger(ItemsStockScreen.class.getName());
    DefaultTableModel model;

    public ItemsStockScreen() {
        initComponents();
        Color customColor = new Color(0x39, 0x44, 0x4C);
        getContentPane().setBackground(customColor);
        model = (DefaultTableModel) jTable2.getModel();
        fillStockTable("");
        //stocklbl.setBackground(Color.YELLOW);
        customizeTable();
    }

    private void customizeTable() {
        jTable2.getTableHeader().setBackground(Color.BLACK);
        jTable2.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 18));
        jTable2.getTableHeader().setForeground(Color.white);

        ((DefaultTableCellRenderer) jTable2.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER); // header to center

        DefaultTableCellRenderer rendar = new DefaultTableCellRenderer();
        rendar.setHorizontalAlignment(stocklbl.CENTER);
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(rendar);
        }
    }

    private void InsertTableHidingFields(String value) {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "UPDATE hidingFields set tagno='" + value + "'";
            int r = stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void fillStockTable(String selectedItem) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        Double[] op_Qty = {0.0}, purchase_Qty = {0.0}, sale_Qty = {0.0}, cl_Stock = {0.0}, cl_Wt = {0.0}, total_Wt = {0.0};
        try {
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                        DatabaseCredentials.DB_PASSWORD);
            }

            String query = "SELECT DISTINCT(itemname) FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                    + " WHERE itemname LIKE '" + selectedItem + "%';";
            List<List<Object>> items = DBController.getDataFromTable(query);

            items.forEach((item) -> {
                if (item.get(0) != null) {  // Null check
                    String itemname = item.get(0).toString();

                    String openingQtyQuery = "SELECT opqty FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE itemname='" + itemname + "';";

                    // Fetching data from the table
                    List<List<Object>> opening = DBController.getDataFromTable(openingQtyQuery);
                    double temp = 0;

                    if (!opening.isEmpty()) {
                        // Iterate through the result set to sum the quantities
                        for (List<Object> row : opening) {
                            if (row.get(0) != null) {
                                String opqtyStr = row.get(0).toString();
                                try {
                                    // Attempt to parse the value as a Double
                                    temp += Double.parseDouble(opqtyStr);
                                } catch (NumberFormatException e) {
                                    // Handle parsing error if needed
                                }
                            }
                        }
                    }

                    String closingWtQuery = "SELECT netwt FROM sales WHERE itemname='" + itemname + "' ";
                    List<List<Object>> netwet = DBController.getDataFromTable(closingWtQuery);
                    double totalSalesWeight = 0;
                    for (List<Object> list : netwet) {
                        if (list.get(0) != null) {
                            totalSalesWeight += Double.parseDouble(list.get(0).toString());
                        }
                    }

                    double opqty = temp;
                    double weight = totalSalesWeight;
                    String q = "SELECT opqty FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE itemname='" + itemname + "';";

                    List<List<Object>> totalcount = DBController.getDataFromTable(q);
                    totalcount.forEach((itemcount) -> {
                        if (!itemcount.isEmpty() && itemcount.get(0) != null) {  // Null check
                            double notSoldCount = 0;
                            double netRemainingWt = 0;

                            List<List<Object>> notSold = DBController.getDataFromTable("SELECT SUM(CAST(item_sold as Decimal(10,2))), SUM(netwt) FROM "
                                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = '" + itemname + "' AND not opqty='0' ");
                            List<List<Object>> notSold1 = DBController.getDataFromTable("SELECT SUM(CAST(item_sold as Decimal(10,2))), SUM(netwt) FROM "
                                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = '" + itemname + "'  ");

                            for (List<Object> list : notSold) {
                                if (list.get(1) != null) {
                                    netRemainingWt = Double.parseDouble(list.get(1).toString());
                                }
                            }
                            for (List<Object> list : notSold1) {
                                if (list.get(0) != null) {
                                    notSoldCount = Double.parseDouble(list.get(0).toString());
                                }
                            }

                            if (!RealSettingsHelper.gettagNoIsTrue()) {
                                String query12 = "SELECT tagnoItems, qty, netwt FROM purchasehistory WHERE itemname='" + itemname + "'";
                                try (Connection con = DBConnect.connect(); Statement stmt2 = con.createStatement(); ResultSet rt = stmt2.executeQuery(query12)) {

                                    int op = 0;
                                    int nwt = 0;
                                    while (rt.next()) {
                                        op += rt.getInt(2);
                                        nwt += rt.getDouble(3);
                                    }
                                    if (notSoldCount < 0) {
                                        notSoldCount = 0;
                                    }
                                    int soldCount = (int) (opqty + op - notSoldCount);
                                    netRemainingWt += nwt;

                                    // Update totals for labels below the table
                                    op_Qty[0] += opqty;
                                    purchase_Qty[0] += op;
                                    sale_Qty[0] += notSoldCount;
                                    cl_Stock[0] += soldCount;
                                    cl_Wt[0] += (netRemainingWt - weight);
                                    total_Wt[0] += netRemainingWt;

                                    // Add row to the table
                                    model.addRow(new Object[]{
                                        itemname,
                                        (int) opqty, // Cast to int
                                        op, // Already an int
                                        (int) notSoldCount, // Cast to int
                                        soldCount, // Already an int
                                        String.format("%.2f", netRemainingWt), // Keep as double with 2 decimal places
                                        String.format("%.2f", (netRemainingWt - weight)) // Keep as double with 2 decimal places
                                    });
                                } catch (SQLException ex) {
                                    Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                String query12 = "SELECT tagnoItems, qty, netwt FROM purchasehistory WHERE itemname='" + itemname + "'";
                                try (Connection con = DBConnect.connect(); Statement stmt2 = con.createStatement(); ResultSet rt = stmt2.executeQuery(query12)) {

                                    int op = 0;
                                    int nwt = 0;
                                    while (rt.next()) {
                                        op += rt.getInt(2);
                                        nwt += rt.getDouble(3);
                                    }
                                    int soldCount = (int) (opqty + op - notSoldCount);
                                    netRemainingWt += nwt;

                                    // Update totals for labels below the table
                                    op_Qty[0] += opqty;
                                    purchase_Qty[0] += op;
                                    sale_Qty[0] += notSoldCount;
                                    cl_Stock[0] += soldCount;
                                    cl_Wt[0] += (netRemainingWt - weight);
                                    total_Wt[0] += netRemainingWt;

                                    // Add row to the table
                                    model.addRow(new Object[]{
                                        itemname,
                                        (int) opqty, // Cast to int
                                        op, // Already an int
                                        (int) notSoldCount, // Cast to int
                                        soldCount, // Already an int
                                        String.format("%.2f", netRemainingWt), // Keep as double with 2 decimal places
                                        String.format("%.2f", (netRemainingWt - weight)) // Keep as double with 2 decimal places
                                    });
                                } catch (SQLException ex) {
                                    Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        opQtytextField.setText(String.valueOf((int) op_Qty[0].doubleValue())); // Cast to int
        purchaseQtytext.setText(String.valueOf((int) purchase_Qty[0].doubleValue())); // Cast to int
        saleQtytext.setText(String.valueOf((int) sale_Qty[0].doubleValue())); // Cast to int
        clStocktext.setText(String.valueOf((int) cl_Stock[0].doubleValue())); // Cast to int
        clWttext.setText(String.format("%.2f", cl_Wt[0])); // Keep as double with 2 decimal places
        totalWttext.setText(String.format("%.2f", total_Wt[0])); // Keep as double with 2 decimal places
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stocklbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        searchbar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        opQtytextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        purchaseQtytext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        saleQtytext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        clStocktext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        clWttext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totalWttext = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setForeground(new java.awt.Color(51, 51, 51));

        stocklbl.setBackground(new java.awt.Color(51, 51, 51));
        stocklbl.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        stocklbl.setForeground(new java.awt.Color(255, 153, 51));
        stocklbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stocklbl.setText("Stock");
        stocklbl.setMinimumSize(new java.awt.Dimension(28, 16));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Item Name", "Op Qty", "Purchase Qty", "Sale Qty", "Cl Stock", "Total Wt.", "Closing wt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        searchbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbarActionPerformed(evt);
            }
        });
        searchbar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchbarKeyReleased(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 51));
        jLabel1.setText("Search");

        jButton1.setText("Refresh");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setText("Close");
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

        jButton3.setText("Print");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Op Qty:");

        opQtytextField.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Purchase Qty:");

        purchaseQtytext.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        purchaseQtytext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseQtytextActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Sale Qty:");

        saleQtytext.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Cl Stock:");

        clStocktext.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Closing Wt.:");

        clWttext.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Total Wt.:");

        totalWttext.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opQtytextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchaseQtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saleQtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clStocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalWttext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clWttext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 969, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stocklbl, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stocklbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchaseQtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(opQtytextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(saleQtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(clStocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(clWttext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalWttext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        // opens itementryscreen
        if (evt.getClickCount() == 2) {
            DefaultTableModel m = (DefaultTableModel) jTable2.getModel();
            int row = jTable2.getSelectedRow();
            String itemname = m.getValueAt(row, 0).toString();
            ItemEntryScreen ob = new ItemEntryScreen();
            ob.ItemListsRedirect(itemname);
            ob.setVisible(true);
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void searchbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchbarActionPerformed

    private void searchbarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbarKeyReleased
        // TODO add your handling code here:
        if (searchbar.getText() == null) {
            return;
        }
        fillStockTable(searchbar.getText());
    }//GEN-LAST:event_searchbarKeyReleased

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        model = (DefaultTableModel) jTable2.getModel();
        fillStockTable("");
        InsertTableHidingFields("false");
        customizeTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        model = (DefaultTableModel) jTable2.getModel();
        fillStockTable("");
        InsertTableHidingFields("false");
        customizeTable();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2MouseClicked

    private void printx() {
        Connection connection = null;
        try {
            // Establishing the database connection
            connection = DBConnect.connect();
            // Prepare a list to hold report data
            List<Map<String, Object>> itemDataList = new ArrayList<>();
            HashMap<String, Object> parameters = new HashMap<>();

            String query = "SELECT DISTINCT(itemname) FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE;
            List<List<Object>> items = DBController.getDataFromTable(query);

            items.forEach((item) -> {
                if (item.get(0) != null) {  // Null check
                    String itemname = item.get(0).toString();

                    String openingQtyQuery = "SELECT opqty FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE itemname='" + itemname + "';";

                    // Fetching data from the table
                    List<List<Object>> opening = DBController.getDataFromTable(openingQtyQuery);
                    double temp = 0;

                    if (!opening.isEmpty()) {
                        // Iterate through the result set to sum the quantities
                        for (List<Object> row : opening) {
                            if (row.get(0) != null) {
                                String opqtyStr = row.get(0).toString();
                                try {
                                    // Attempt to parse the value as a Double
                                    temp += Double.parseDouble(opqtyStr);
                                } catch (NumberFormatException e) {

                                }
                            }
                        }
                    }

                    String closingWtQuery = "SELECT netwt FROM sales WHERE itemname='" + itemname + "' ";
                    List<List<Object>> netwet = DBController.getDataFromTable(closingWtQuery);
                    double totalSalesWeight = 0;
                    for (List<Object> list : netwet) {
                        if (list.get(0) != null) {
                            totalSalesWeight += Double.parseDouble(list.get(0).toString());
                        }
                    }

                    double opqty = temp;

                    double weight = totalSalesWeight;
                    String q = "SELECT opqty FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE itemname='" + itemname + "';";
                    List<List<Object>> totalcount = DBController.getDataFromTable(q);
                    totalcount.forEach((itemcount) -> {
                        if (!itemcount.isEmpty() && itemcount.get(0) != null) {  // Null check

                            double notSoldCount = 0;
                            double netRemainingWt = 0;

                            List<List<Object>> notSold = DBController.getDataFromTable("SELECT SUM(CAST(item_sold as Decimal(10,2))), SUM(netwt) FROM "
                                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = '" + itemname + "' AND not opqty='0' ");
                            List<List<Object>> notSold1 = DBController.getDataFromTable("SELECT SUM(CAST(item_sold as Decimal(10,2))), SUM(netwt) FROM "
                                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = '" + itemname + "'  ");

                            for (List<Object> list : notSold) {
                                if (list.get(1) != null) {
                                    netRemainingWt = Double.parseDouble(list.get(1).toString());
                                }

                            }
                            for (List<Object> list : notSold1) {

                                if (list.get(0) != null) {
                                    notSoldCount = Double.parseDouble(list.get(0).toString());
                                }
                            }

                            String query12 = "SELECT tagnoItems, qty, netwt FROM purchasehistory WHERE itemname='" + itemname + "'";
                            try (Connection con = DBConnect.connect(); Statement stmt2 = con.createStatement(); ResultSet rt = stmt2.executeQuery(query12)) {

                                double op = 0;
                                int nwt = 0;
                                while (rt.next()) {
                                    op += rt.getInt(2);
                                    nwt += rt.getDouble(3);

                                }
                                Double soldCount = opqty + op - notSoldCount;
                                netRemainingWt += nwt;

                                Map<String, Object> itemParameters = new HashMap<>();
                                itemParameters.put("itemname", itemname);
                                itemParameters.put("opqty", opqty);
                                itemParameters.put("purchaseQty", op);
                                itemParameters.put("soldQty", notSoldCount);
                                itemParameters.put("closingStock", soldCount);
                                itemParameters.put("netRemainingWt", netRemainingWt);
                                itemParameters.put("closingWt", (netRemainingWt - weight));

                                // Add the current item's parameters to the list
                                itemDataList.add(itemParameters);

                            } catch (SQLException ex) {
                                Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                }
            });

            parameters.put("itemDataList", itemDataList);

            File reportFile = new File("jasper_reports" + File.separator + "stock_report.jrxml");
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

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        printx();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void purchaseQtytextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseQtytextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseQtytextActionPerformed

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
            java.util.logging.Logger.getLogger(ItemsStockScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemsStockScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemsStockScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemsStockScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemsStockScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField clStocktext;
    private javax.swing.JTextField clWttext;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField opQtytextField;
    private javax.swing.JTextField purchaseQtytext;
    private javax.swing.JTextField saleQtytext;
    private javax.swing.JTextField searchbar;
    private javax.swing.JLabel stocklbl;
    private javax.swing.JTextField totalWttext;
    // End of variables declaration//GEN-END:variables
}
