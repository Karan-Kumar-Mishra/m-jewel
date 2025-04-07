package jewellery;


import java.awt.EventQueue;
import java.awt.Font;

import java.awt.event.KeyEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.scene.paint.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import jewellery.helper.outstandingAnalysisHelper;

/**
 *
 * @author AR-LABS
 */
public class AccountsListScreen extends javax.swing.JFrame {

    private final List<Object> accountNames = new ArrayList<>();
    private final DefaultTableModel m;

    public AccountsListScreen() {
        Logger.getLogger(AccountsListScreen.class.getName()).log(Level.INFO, "account list screen object initiazed!");

        initComponents();

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 18));
        header.setBackground(java.awt.Color.DARK_GRAY);
        header.setForeground(java.awt.Color.white);
        TableColumnModel tcm = table.getColumnModel();// hiding column
        tcm.getColumn(0).setMaxWidth(0);
//        tcm.getColumn(0).setMinWidth(0);
        tcm.getColumn(0).setWidth(0);
        getAllGroupAndEnterToComboBox();
        m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        refreshTable();
    }
    
    
   
    

    private void delete() {

        DefaultTableModel m = (DefaultTableModel) table.getModel();
        int row = table.getSelectedRow();
        String id = m.getValueAt(row, 0).toString();
        Connection con = DBConnect.connect();
        if (Integer.parseInt(id) == 1) {
            JOptionPane.showMessageDialog(this, "this is Auto-Genrated Account Can't Delete");
        } else {
            DefaultTableModel model2 = (DefaultTableModel) table.getModel();

            int selectedrow = table.getSelectedRow();
            String partyname = model2.getValueAt(selectedrow, 1).toString();
            String payment = "";
            try {
                payment = outstandingAnalysisHelper.fillTableInDateGivenParty(partyname);
            } catch (ParseException ex) {
                Logger.getLogger(AccountsListScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
//            JOptionPane.showMessageDialog(this, payment);
            if (Float.parseFloat(payment.trim()) == 0) {
                String sql = "delete from account where id =" + id + "";
                try {
                    Statement stmt = con.createStatement();
                    int result = stmt.executeUpdate(sql);
                    if (result == 1) {
                        JOptionPane.showMessageDialog(this, "Deleted SuccessFully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Something Went Wrong!!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Clear Balance First And Then Delete");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getAllGroupAndEnterToComboBox() {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select distinct grp from account ";
            ResultSet re = stmt.executeQuery(sql);
            accountGroup.removeAllItems();
            accountGroup.addItem("ALL");
            while (re.next()) {
                accountGroup.addItem(re.getString("grp").trim());
            }
            accountGroup.setSelectedItem("ALL");
            re.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        Refreshbtn = new javax.swing.JButton();
        closebtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        accountGroup = new javax.swing.JComboBox();
        Newbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("List Of Accounts");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));
        jPanel1.setForeground(new java.awt.Color(189, 150, 117));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1264, 661));

        table.setBackground(new java.awt.Color(57, 68, 76));
        table.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        table.setForeground(new java.awt.Color(255, 255, 255));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Account Name", "Group", "Phone No", "State", "Open Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setGridColor(new java.awt.Color(255, 255, 255));
        table.setRowHeight(30);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        Refreshbtn.setText("Refresh");
        Refreshbtn.setMaximumSize(new java.awt.Dimension(111, 57));
        Refreshbtn.setMinimumSize(new java.awt.Dimension(111, 57));
        Refreshbtn.setPreferredSize(new java.awt.Dimension(111, 57));
        Refreshbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshbtnActionPerformed(evt);
            }
        });

        closebtn.setBackground(new java.awt.Color(255, 0, 0));
        closebtn.setText("Close");
        closebtn.setMaximumSize(new java.awt.Dimension(111, 57));
        closebtn.setMinimumSize(new java.awt.Dimension(111, 57));
        closebtn.setPreferredSize(new java.awt.Dimension(111, 57));
        closebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("Search :");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Account");

        accountGroup.setFont(new java.awt.Font("Times New Roman", 1, 14));
        accountGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountGroupActionPerformed(evt);
            }
        });

        Newbtn.setText("New Account");
        Newbtn.setMaximumSize(new java.awt.Dimension(111, 57));
        Newbtn.setMinimumSize(new java.awt.Dimension(111, 57));
        Newbtn.setPreferredSize(new java.awt.Dimension(111, 57));
        Newbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewbtnActionPerformed(evt);
            }
        });

        deletebtn.setBackground(new java.awt.Color(255, 51, 0));
        deletebtn.setText("Delete");
        deletebtn.setMaximumSize(new java.awt.Dimension(111, 57));
        deletebtn.setMinimumSize(new java.awt.Dimension(111, 57));
        deletebtn.setPreferredSize(new java.awt.Dimension(111, 57));
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(accountGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Newbtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Refreshbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Refreshbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Newbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(accountGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1061, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

//    private void populateGroupComboBox() {
//        cmbGroup.removeAllItems();
//        
//        if(!DBController.isDatabaseConnected()) {
//            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
//                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
//        }
//        
//        List<Object> accountGroups = DBController.executeQuery("SELECT account_group FROM " 
//                + DatabaseCredentials.ACCOUNT_GROUP_TABLE);
//        
//        accountGroups.forEach((accountGroup) -> {
//            cmbGroup.addItem(accountGroup.toString());
//        });
//    }
    private void fetchAccountNames() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> account_names = DBController.executeQuery("SELECT accountname FROM account");

        account_names.forEach((accountName) -> {
            accountNames.add(accountName.toString());
        });
    }

    private void populateSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        suggestionsTable.setRowCount(0);

        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),
                (suggestion.get(4) == null) ? "NULL" : suggestion.get(4),
                (suggestion.get(5) == null) ? "NULL" : suggestion.get(5),});
        });
    }
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:

        String value = accountGroup.getSelectedItem().toString().trim();
        fetchAccountNames();
        if (!(accountNames == null || accountNames.isEmpty())) {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    if (value.trim().equalsIgnoreCase("ALL")) {
                        m.setRowCount(0);
                      populateSuggestionsTableFromDatabase(m, "SELECT id,accountname, "
                                    + "grp,mobile1,state,opbal FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                    + " WHERE  accountname LIKE " + "'" + jTextField1.getText() + "%'");
                    } else {
                        populateSuggestionsTableFromDatabase(m, "SELECT id,accountname, "
                                + "grp, mobile1,state,opbal FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE grp='" + value + "' AND accountname LIKE " + "'" + jTextField1.getText() + "%'");
                    }
                    break;
                case KeyEvent.VK_ENTER:

                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        if (value.trim().equalsIgnoreCase("ALL")) {
                            m.setRowCount(0);
                            populateSuggestionsTableFromDatabase(m, "SELECT id,accountname, "
                                    + "grp,mobile1,state,opbal FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                    + " WHERE  accountname LIKE " + "'" + jTextField1.getText() + "%'");
                        } else {
                            populateSuggestionsTableFromDatabase(m, "SELECT id,accountname, "
                                    + "grp,mobile1,state,opbal FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                    + " WHERE grp='" + value + "' AND accountname LIKE " + "'" + jTextField1.getText() + "%'");
                        }
                    });

            }
        }
    }//GEN-LAST:event_jTextField1KeyReleased
    private void refreshTable() {
        m.setRowCount(0);
        try {
            Connection c = DBConnect.connect();
            Statement stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("Select id,accountname,grp,state,mobile1,opbal,dueamt from account;");
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString("accountname");
                String grp = rs.getString("grp");
                String state = rs.getString("state");
                String Contact = rs.getString("mobile1");
                String opbal = rs.getString("opbal");

                m.addRow(new Object[]{id, name, grp, Contact, state, opbal});

            }
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int g = 0; g < table.getColumnCount(); g++) {
                table.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void RefreshbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshbtnActionPerformed
        // TODO add your handling code here:
        refreshTable();
    }//GEN-LAST:event_RefreshbtnActionPerformed

    private void closebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebtnActionPerformed
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

    }//GEN-LAST:event_closebtnActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            DefaultTableModel m = (DefaultTableModel) table.getModel();
            int row = table.getSelectedRow();
            String recp = m.getValueAt(row, 0).toString();
            CreateAccountScreen ob = new CreateAccountScreen(recp);
            ob.bill_history_update(recp);
            ob.setVisible(true);
//            ob.btnUpdateAccount.setVisible(true);
//            dispose();
        }
    }//GEN-LAST:event_tableMouseClicked

    private void accountGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountGroupActionPerformed
        // TODO add your handling code here:
        String value = accountGroup.getSelectedItem().toString();
    }//GEN-LAST:event_accountGroupActionPerformed

    private void NewbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewbtnActionPerformed
        // TODO add your handling code here:
        new CreateAccountScreen().setVisible(true);
    }//GEN-LAST:event_NewbtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        if (table.getSelectionModel().isSelectionEmpty() && table.getSelectedRow() == 1) {
            JOptionPane.showMessageDialog(this, "Please Select Any Company");
        } else {
            int reply = JOptionPane.showConfirmDialog(jPanel1, "Do you want a delete ?", "Select delete", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                delete();
            }

        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Newbtn;
    private javax.swing.JButton Refreshbtn;
    private javax.swing.JComboBox accountGroup;
    private javax.swing.JButton closebtn;
    private javax.swing.JButton deletebtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
