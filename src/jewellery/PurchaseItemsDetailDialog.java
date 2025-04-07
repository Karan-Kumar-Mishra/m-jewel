package jewellery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AR-LABS
 */
public class PurchaseItemsDetailDialog extends javax.swing.JDialog {

    private static final int MAX_ROWS_LIMIT = 1000;
    private DefaultTableModel purchaseItemsDetailsTableModel;
    private int selectedRow;
    private int selectedCol;

    private int totalNetWt = 0;
    private int totalDiamondWt = 0;
    private int totalBeedsWt = 0;
    private int GrossWt = 0;
    private List<Object> purchaseItemsDetails;
    PurchaseScreen purchaseScreen;

    public PurchaseItemsDetailDialog(PurchaseScreen purchaseScreen, boolean modal, int tableRowCount) {
        initComponents();
        addEscapeListener(PurchaseItemsDetailDialog.this);
        setLocationRelativeTo(DashBoardScreen.tabbedPane);
        this.purchaseScreen = purchaseScreen;

        purchaseItemsDetailsTableModel = (DefaultTableModel) tblPurchaseItemsDetails.getModel();

        centerTableCells();

        PurchaseItemsDetailDialog.this.setTableRowCount(tableRowCount);

        PurchaseItemsDetailDialog.this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                PurchaseItemsDetailDialog.this.dispose();
            }
        });

    }

    public PurchaseItemsDetailDialog(PurchaseScreen purchaseScreen, boolean modal, int tableRowCount, List<List<String>> data) {
        initComponents();
        addEscapeListener(PurchaseItemsDetailDialog.this);
        setLocationRelativeTo(DashBoardScreen.tabbedPane);
        this.purchaseScreen = purchaseScreen;

        purchaseItemsDetailsTableModel = (DefaultTableModel) tblPurchaseItemsDetails.getModel();

        centerTableCells();

        PurchaseItemsDetailDialog.this.setTableRowCount(tableRowCount);

        PurchaseItemsDetailDialog.this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                PurchaseItemsDetailDialog.this.dispose();
            }
        });
        for (int i = 0; i < data.size(); i++) {
            purchaseItemsDetailsTableModel.setRowCount(0);
            Object[] tabledata = {data.get(i).get(0), data.get(i).get(1), data.get(i).get(2), data.get(i).get(3), data.get(i).get(4)};
            purchaseItemsDetailsTableModel.addRow(tabledata);

        }

    }

    public PurchaseItemsDetailDialog(PurchaseScreen purchaseScreen, boolean modal, int tableRowCount, List<List<String>> data, String add) {
        initComponents();
        addEscapeListener(PurchaseItemsDetailDialog.this);
        setLocationRelativeTo(DashBoardScreen.tabbedPane);
        this.purchaseScreen = purchaseScreen;

        purchaseItemsDetailsTableModel = (DefaultTableModel) tblPurchaseItemsDetails.getModel();

        centerTableCells();

        PurchaseItemsDetailDialog.this.setTableRowCount(tableRowCount);

        PurchaseItemsDetailDialog.this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                PurchaseItemsDetailDialog.this.dispose();
            }
        });
          purchaseItemsDetailsTableModel.setRowCount(0);
        for (int i = 0; i < data.size(); i++) {
            
          
            Object[] tabledata = {data.get(i).get(0), data.get(i).get(1), data.get(i).get(2), data.get(i).get(3), data.get(i).get(4)};
            purchaseItemsDetailsTableModel.addRow(tabledata);

        }
        btnSubmit.doClick();

    }

    public void populateItemsDetailsTable(String query) {

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        purchaseItemsDetailsTableModel.setRowCount(0);

        suggestions.forEach((purchaseItemsDetails) -> {
            purchaseItemsDetailsTableModel.addRow(new Object[]{
                (purchaseItemsDetails.get(0) == null) ? 0 : purchaseItemsDetails.get(0),
                (purchaseItemsDetails.get(1) == null) ? 0 : purchaseItemsDetails.get(1),
                (purchaseItemsDetails.get(2) == null) ? 0 : purchaseItemsDetails.get(2),
                (purchaseItemsDetails.get(3) == null) ? 0 : purchaseItemsDetails.get(3),
                (purchaseItemsDetails.get(4) == null) ? 0 : purchaseItemsDetails.get(4),});
        });

//         txtTotalNetWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,3)));
//         txtTotalDiamondWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,4)));
    }

    public void populateItemsDetailsTableSale(String query) {

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        purchaseItemsDetailsTableModel.setRowCount(0);

        suggestions.forEach((purchaseItemsDetails) -> {
            purchaseItemsDetailsTableModel.addRow(new Object[]{
                (purchaseItemsDetails.get(0) == null) ? 0 : purchaseItemsDetails.get(0),
                (purchaseItemsDetails.get(1) == null) ? 0 : purchaseItemsDetails.get(1),
                (purchaseItemsDetails.get(2) == null) ? 0 : purchaseItemsDetails.get(2),
                (purchaseItemsDetails.get(3) == null) ? 0 : purchaseItemsDetails.get(3),
                (purchaseItemsDetails.get(4) == null) ? 0 : purchaseItemsDetails.get(4),});
        });
//         txtGrossWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,1)));
//         txtBeedsWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,2)));
//         txtTotalNetWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,3)));
//         txtTotalDiamondWt.setText(String.valueOf(purchaseItemsDetailsTableModel.getValueAt(0,4)));
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblPurchaseItemsDetails
                .getDefaultRenderer(Object.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = (ActionEvent e) -> {
            dialog.dispose();
        };

        dialog.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_FOCUSED);
    }

    public void setTableRowCount(int nRows) {
        if (nRows > 0 && nRows < MAX_ROWS_LIMIT) {
            purchaseItemsDetailsTableModel.setRowCount(nRows);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRootContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchaseItemsDetails = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        pnlRootContainer.setBackground(new java.awt.Color(240, 168, 78));
        pnlRootContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblPurchaseItemsDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "HUID", "Gross Wt.", "Beeds Wt.", "Net Wt.", "Diamond Wt."
            }
        ));
        tblPurchaseItemsDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPurchaseItemsDetailsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblPurchaseItemsDetails);

        jPanel1.setOpaque(false);

        btnSubmit.setText("Submit");
        btnSubmit.setPreferredSize(new java.awt.Dimension(150, 35));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlRootContainerLayout = new javax.swing.GroupLayout(pnlRootContainer);
        pnlRootContainer.setLayout(pnlRootContainerLayout);
        pnlRootContainerLayout.setHorizontalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlRootContainerLayout.setVerticalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {
    	 PurchaseItemsDetailDialog.this.dispose();//GEN-FIRST:event_btnSubmitActionPerformed
        int rowCount = tblPurchaseItemsDetails.getRowCount();
        int colCount = tblPurchaseItemsDetails.getColumnCount();

        List<List<String>> itemData = new ArrayList<>();
        for (int i = 0; i < tblPurchaseItemsDetails.getRowCount(); i++) {
            String huid = "0", grosswt = "0", beedwt = "0", netwt = "0", diawt = "0";
            if (tblPurchaseItemsDetails.getValueAt(i, 0) != null) {
                huid = tblPurchaseItemsDetails.getValueAt(i, 0).toString();
            }
            if (tblPurchaseItemsDetails.getValueAt(i, 1) != null) {
                grosswt = tblPurchaseItemsDetails.getValueAt(i, 1).toString();
            }
            if (tblPurchaseItemsDetails.getValueAt(i, 2) != null) {
                beedwt = tblPurchaseItemsDetails.getValueAt(i, 2).toString();
            }
            if (tblPurchaseItemsDetails.getValueAt(i, 3) != null) {
                netwt = tblPurchaseItemsDetails.getValueAt(i, 3).toString();
            }
            if (tblPurchaseItemsDetails.getValueAt(i, 4) != null) {
                diawt = tblPurchaseItemsDetails.getValueAt(i, 4).toString();
            }

            itemData.add(Arrays.asList(
                    huid,
                    grosswt,
                    beedwt,
                    netwt,
                    diawt
            ));
        }

        purchaseScreen.PurchaseItemsDetailDialogSubmitClicked(itemData);

//        if(purchaseItemsDetails != null && purchaseItemsDetails.size() > 0) {
//            purchaseItemsDetails.clear();
//        }
//        
//        for(int row = 0; row < rowCount; row++) {
//            List<Object> tblRow = new ArrayList<>();
//            
//            for(int col = 0; col < colCount; col++) {
//                if(tblPurchaseItemsDetails.getValueAt(row, col) != null) {
//                    tblRow.add(tblPurchaseItemsDetails.getValueAt(row, col));
//                }
//                else {
//                    tblRow.add(0);
//                }
//            }
//            
//            GLOBAL_VARS.purchaseItems.add(tblRow);
//        }
//        
//        GLOBAL_VARS.purchaseItems.forEach((purchaseItem) -> {
//            purchaseItem.forEach((detail) -> {
//                System.out.print(detail + " ");
//            });
//            
//            System.out.println();
//        });
//        txtGrossWt.setText(String.valueOf(GrossWt));
//        txtBeedsWt.setText(String.valueOf(totalBeedsWt));
//        txtTotalNetWt.setText(String.valueOf(totalNetWt));
//        txtTotalDiamondWt.setText(String.valueOf(totalDiamondWt));
//        PurchaseItemsDetailDialog.this.dispose();
//
//        PurchaseScreen.setTotalNetWtAndDiamondWt(txtTotalNetWt.getText(), txtTotalDiamondWt.getText());
        //PurchaseItemsDetailDialog.setVisible(false);
        // PurchaseItemsDetailDialog.this.dispose();

    }//GEN-LAST:event_btnSubmitActionPerformed

    private void tblPurchaseItemsDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPurchaseItemsDetailsKeyReleased

        if (tblPurchaseItemsDetails.getSelectedColumn() == 3) {
            int row = tblPurchaseItemsDetails.getSelectedRow();
            double beedswt = 0;
            if (tblPurchaseItemsDetails.getValueAt(row, 2) != null && !tblPurchaseItemsDetails.getValueAt(row, 2).equals("")) {
                beedswt = Double.parseDouble(tblPurchaseItemsDetails.getValueAt(row, 2).toString());
            }

            Double netwt = Double.parseDouble(tblPurchaseItemsDetails.getValueAt(row, 1).toString())
                    - beedswt;
            tblPurchaseItemsDetails.setValueAt(String.format("%.2f", netwt), row, 3);
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER && tblPurchaseItemsDetails.getSelectedColumn() == 4) {
            int row = tblPurchaseItemsDetails.getSelectedRow();
            tblPurchaseItemsDetails.changeSelection(row, 0, false, false);
        }

//        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            int selectedCol = tblPurchaseItemsDetails.getSelectedColumn();
//            int selectedRow = tblPurchaseItemsDetails.getSelectedRow();
//            Logger.getLogger(dashboard.class.getName()).info(String.valueOf(selectedRow) + " " + String.valueOf(selectedCol));
//            if(selectedCol == 4 && selectedRow != tblPurchaseItemsDetails.getRowCount()) {
//                tblPurchaseItemsDetails.changeSelection(selectedRow, 0, false, false);
//            } else {
//                tblPurchaseItemsDetails.editCellAt(selectedRow, selectedCol);
//                
//            }
//        }
//        if(tblPurchaseItemsDetails.getValueAt(selectedRow, 1) != null
//                && tblPurchaseItemsDetails.getValueAt(selectedRow, 2) != null) {
//            
//            double netWt = Double.valueOf(tblPurchaseItemsDetails.getValueAt(selectedRow, 1).toString())
//                    - Double.valueOf(tblPurchaseItemsDetails.getValueAt(selectedRow, 2).toString());
//            
//            tblPurchaseItemsDetails.setValueAt(netWt, selectedRow, 3);
//            
//        }
//        else if(tblPurchaseItemsDetails.getValueAt(selectedRow, 1) != null
//                && tblPurchaseItemsDetails.getValueAt(selectedRow, 2) == null) {
//            
//            tblPurchaseItemsDetails.setValueAt(tblPurchaseItemsDetails
//                    .getValueAt(selectedRow, 1), selectedRow, 3);
//            
//        }
//        
//        totalNetWt = 0;
//        totalDiamondWt = 0;
//        totalBeedsWt = 0;
//        GrossWt = 0;
//        
//        for(int row = 0; row < tblPurchaseItemsDetails.getRowCount(); row++) {
//            if(tblPurchaseItemsDetails.getValueAt(row, 3) != null) {
//                totalNetWt += Double.valueOf(tblPurchaseItemsDetails.getValueAt(row, 3).toString());
//            }
//        }
//        
//        for(int row = 0; row < tblPurchaseItemsDetails.getRowCount(); row++) {
//            if(tblPurchaseItemsDetails.getValueAt(row, 2) != null) {
//                totalBeedsWt += Double.valueOf(tblPurchaseItemsDetails.getValueAt(row, 2).toString());
//            }
//        }
//        for(int row = 0; row < tblPurchaseItemsDetails.getRowCount(); row++) {
//            if(tblPurchaseItemsDetails.getValueAt(row, 1) != null) {
//                GrossWt += Double.valueOf(tblPurchaseItemsDetails.getValueAt(row, 1).toString());
//            }
//        }
//        
//        for(int row = 0; row < tblPurchaseItemsDetails.getRowCount(); row++) {
//            if(tblPurchaseItemsDetails.getValueAt(row, 4) != null) {
//                totalDiamondWt += Double.valueOf(tblPurchaseItemsDetails.getValueAt(row, 4).toString());
//            }
//        }
//        txtGrossWt.setText(String.valueOf(GrossWt));
//        txtBeedsWt.setText(String.valueOf(totalBeedsWt));
//        txtTotalNetWt.setText(String.valueOf(totalNetWt));
//        txtTotalDiamondWt.setText(String.valueOf(totalDiamondWt));
//        
//        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
//            selectedRow = tblPurchaseItemsDetails.getSelectedRow();
//        }
//        
//        if(evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_RIGHT) {
//            selectedCol = tblPurchaseItemsDetails.getSelectedColumn();
//        }
    }//GEN-LAST:event_tblPurchaseItemsDetailsKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlRootContainer;
    private javax.swing.JTable tblPurchaseItemsDetails;
    // End of variables declaration//GEN-END:variables
}
