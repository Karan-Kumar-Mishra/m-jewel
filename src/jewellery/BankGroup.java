/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
//import jewellery.AccountsListScreen.setCursorForAllComponents

/**
 *
 * @author shrey
 */
public class BankGroup extends javax.swing.JFrame {

    /**
     * Creates new form BankGroup
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Vector<String> v = null;
    private final DefaultTableModel partyNameSuggestionsTableModel;

    public BankGroup() {
        initComponents();
        initC();
        delete.setVisible(false);
        FatchTableData();
        date.setDate(new Date());
        TableColumnModel tcm = table.getColumnModel();// hiding column
        tcm.getColumn(6).setMaxWidth(0);
        tcm.getColumn(6).setMinWidth(0);
        tcm.getColumn(6).setWidth(0);

        v = new Vector<>();
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
        pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 200, txtPartyName.getY() + 150);
        try {
            Connection cd = DBConnect.connect();
            Statement s = cd.createStatement();
            ResultSet rst = s.executeQuery("SELECT accountname FROM `account` WHERE `grp` = 'Bank';");
            while (rst.next()) {
                v.add(rst.getString("accountname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>(v);
        jComboBox1.setModel(m);

    }
    String recp = "";

    public void fillByRedirect(String recp) {
        this.recp = recp;
//        jTextField1.setText(recp);
        jButton3.setText("Update");
        delete.setVisible(true);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM bankledger WHERE rno='" + recp + "';");
            while (rs1.next()) {
                txtPartyName.setText(rs1.getString("name"));
                jTextField1.setText(rs1.getString("amt"));
                jComboBox1.setSelectedItem(rs1.getString("bankname"));
                date.setDate(rs1.getDate("date"));
                if (rs1.getString("type").equalsIgnoreCase("deposit")) {
                    entry.setSelectedIndex(1);
                } else {
                    entry.setSelectedIndex(0);
                }

                jTextField2.setText(rs1.getString("remarks"));

            }
            stmt.clearBatch();

            con.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populatenameSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
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
                (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),});
        });

    }

    void initC() {
        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Party Name", "State", "GRP"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblPartyNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartyNameSuggestionsMouseClicked(evt);
            }
        });

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);

        setResizable(false);

    }

    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            pmPartyNameSuggestionsPopup.setVisible(false);
        }
    }

    private void FatchTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select * from bankledger where sales_Bill = -1 ";
            ResultSet re = stmt.executeQuery(sql);
            double totalcr = 0.0;
            double totaldr = 0.0;
            while (re.next()) {
                String name = re.getString("name");
                String Bank = re.getString("bankname");
                String date = re.getString("date");
                String amt = re.getString("amt");
                String remark = re.getString("remarks");
                String type = re.getString("type");
                String id=re.getString("rno");
                String drAmt = null, crAmt = null;

                if (type.equals("deposit")) {
                    drAmt = amt;
                    totaldr = totaldr + Double.parseDouble(amt);
                    System.out.println("dr" + totaldr);

                } else {
                    crAmt = amt;
                    totalcr += Double.parseDouble(amt);
                    System.out.println("cr" + totalcr);
                }
                String[] data = {date, name, Bank, drAmt, crAmt, remark,id};
                model.addRow(data);
                totaldebitfield.setText("");
                totalcreditfield.setText("");
                totaldebitfield.setText(Double.toString(totaldr));
                totalcreditfield.setText(Double.toString(totalcr));
            }
            re.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, e);
        }

    }
//    private void totalDrCr(){
//        for(int i=0;i<table.getRowCount();i++){
//            if()
//        }
//    }

    private void ClearAllData() {
        txtPartyName.setText("");
        jTextField1.setText("");
        jTextField2.setText("");
    }

    private void save() {
        Connection c;
        Statement s = null;
        String query;
        ResultSet rs;
        System.out.println("enering to the save");
        try {
            c = DBConnect.connect();
            s = c.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!jButton3.getText().trim().equalsIgnoreCase("Update")) {
            if (entry.getSelectedIndex() == 1) {
                try {
                    query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`) VALUES ('" + txtPartyName.getText() + "','" + v.get(jComboBox1.getSelectedIndex()) + "', '" + dateFormat.format(date.getDate()) + "', '" + jTextField1.getText() + "', '" + jTextField2.getText() + "', 'deposit');";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    s.clearBatch();
                    query = "UPDATE account SET dueamt = dueamt-" + jTextField1.getText() + " WHERE `accountname` ='" + txtPartyName.getText() + "';";
                    s.executeUpdate(query);
                    s.clearBatch();
                    query = "UPDATE account SET amt = amt+" + jTextField1.getText() + " WHERE `accountname` ='" + v.get(jComboBox1.getSelectedIndex()) + "';";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(this, "Transaction Successful", "Message", HEIGHT);
                ClearAllData();
                FatchTableData();
            }

            double amt = 0;

            if (entry.getSelectedIndex() == 0 || entry.getSelectedIndex() == 2) {
                try {
                    rs = s.executeQuery("SELECT amt FROM `account` WHERE accountname = '" + v.get(jComboBox1.getSelectedIndex()) + "';");

                    while (rs.next()) {
                        amt = rs.getDouble("amt");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
//            if(amt >Double.parseDouble(jTextField1.getText())){
                try {
                    query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`) VALUES ( '" + txtPartyName.getText() + "','" + v.get(jComboBox1.getSelectedIndex()) + "', '" + dateFormat.format(date.getDate()) + "', '" + jTextField1.getText() + "', '', 'withdraw');";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    s.clearBatch();
                    query = "UPDATE account SET dueamt = dueamt+" + jTextField1.getText() + " WHERE `accountname` ='" + txtPartyName.getText() + "';";
                    s.executeUpdate(query);
                    s.clearBatch();
                    query = "UPDATE account SET amt = amt-" + jTextField1.getText() + " WHERE `accountname` ='" + v.get(jComboBox1.getSelectedIndex()) + "';";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(this, "Transaction Successful", "Message", HEIGHT);
                ClearAllData();
              
            }
        } else {
            if (entry.getSelectedIndex() == 1) {
                try {
                    query = "update bankledger set name='" + txtPartyName.getText().trim() + "', bankname='" + v.get(jComboBox1.getSelectedIndex()) + "', date='" + dateFormat.format(date.getDate()) + "', amt= '" + jTextField1.getText() + "', remarks='" + jTextField2.getText() + "', type='deposit' where rno=" + this.recp.trim() + "";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(this, "Transaction Successful", "Message", HEIGHT);
                ClearAllData();
            }

//        double amt = 0;
            if (entry.getSelectedIndex() == 0 || entry.getSelectedIndex() == 2) {

//            if(amt >Double.parseDouble(jTextField1.getText())){
                try {
                    query = "update bankledger set name='" + txtPartyName.getText().trim() + "', bankname='" + v.get(jComboBox1.getSelectedIndex()) + "', date='" + dateFormat.format(date.getDate()) + "', amt= '" + jTextField1.getText() + "', remarks='" + jTextField2.getText() + "',type='withdraw' where rno=" + this.recp.trim() + "";
                    s.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(this, "Transaction Successful", "Message", HEIGHT);
                ClearAllData();

            }
        }
          FatchTableData();
        this.recp = "-2";
        jButton3.setText("Save");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        closebutton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        totalcreditlabel = new javax.swing.JLabel();
        totalcreditfield = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totaldebitfield = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        entry = new javax.swing.JComboBox<>();
        delete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("BANK ENTRY");

        closebutton.setBackground(new java.awt.Color(255, 0, 0));
        closebutton.setText("Close");
        closebutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closebuttonMouseClicked(evt);
            }
        });
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Name", "Bank Name", "Dr Amount", "Cr Amount", "Remarks", "id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setColumnSelectionAllowed(true);
        table.setRowHeight(24);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        totalcreditlabel.setForeground(new java.awt.Color(238, 188, 81));
        totalcreditlabel.setText("Credit");

        totalcreditfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcreditfieldActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Debit");

        txtPartyName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPartyName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartyNameActionPerformed(evt);
            }
        });
        txtPartyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyReleased(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Account Name");

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Amount");

        jButton3.setText("Save");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton3KeyPressed(evt);
            }
        });

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setText("Remarks");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(totalcreditlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalcreditfield, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totaldebitfield, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1023, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totaldebitfield, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalcreditfield, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalcreditlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox1FocusGained(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Date : ");

        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        date.setDateFormatString("yyyy-MM-dd");
        date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Bank :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Entry :");
        jLabel3.setPreferredSize(new java.awt.Dimension(40, 17));

        entry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Deposit Cash Into Bank", "Withdraw Cash From Bank", "Online Transfer" }));
        entry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                entryFocusGained(evt);
            }
        });
        entry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entryActionPerformed(evt);
            }
        });
        entry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                entryKeyPressed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(255, 0, 0));
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(entry, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 87, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(entry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closebuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closebuttonMouseClicked
        // TODO add your handling code here:

        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        dispose();
    }//GEN-LAST:event_closebuttonMouseClicked

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_closebuttonActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
DefaultTableModel model2 = (DefaultTableModel) table.getModel();
if(jButton3.getText().trim().equalsIgnoreCase("Save")){
        int selectedrow = table.getSelectedRow();
        String recp = model2.getValueAt(selectedrow, 6).toString();
                BankGroup bg = null;
                try {
                    bg = new BankGroup();
                } catch (Exception ex) {
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                bg.setVisible(true);
                bg.fillByRedirect(recp);
}

    }//GEN-LAST:event_tableMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void txtPartyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameActionPerformed

    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased
        if (entry.getSelectedIndex() == 2) {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    pmPartyNameSuggestionsPopup.setVisible(false);
                    break;
                default:
                    EventQueue.invokeLater(() -> {

                        pmPartyNameSuggestionsPopup.setVisible(true);
//                        JOptionPane.showMessageDialog(this, "SELECT accountname, "
//                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
//                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'"
//                                + "AND grp = 'Bank' ;");
                        populatenameSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'"
                                + "AND grp = 'Bank' ;");
                    });

                    break;
            }
        } else {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    pmPartyNameSuggestionsPopup.setVisible(false);
                    break;
                default:
                    EventQueue.invokeLater(() -> {

                        pmPartyNameSuggestionsPopup.setVisible(true);

                        populatenameSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'"
                                + "AND grp = 'Customer' OR grp = 'Cash' ;");
                    });

                    break;
            }


    }//GEN-LAST:event_txtPartyNameKeyReleased
    }
    private void totalcreditfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcreditfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcreditfieldActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void entryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entryActionPerformed

    private void dateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jComboBox1.requestFocus();
        }
    }//GEN-LAST:event_dateKeyPressed

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            entry.requestFocus();
        }
    }//GEN-LAST:event_jComboBox1KeyPressed

    private void entryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entryKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPartyName.requestFocus();
        }
    }//GEN-LAST:event_entryKeyPressed

    private void txtPartyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField1.requestFocus();
        }
    }//GEN-LAST:event_txtPartyNameKeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField2.requestFocus();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton3.requestFocus();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1FocusGained
        // TODO add your handling code here:
        jComboBox1.showPopup();
    }//GEN-LAST:event_jComboBox1FocusGained

    private void entryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entryFocusGained
        // TODO add your handling code here:
        jComboBox1.showPopup();
    }//GEN-LAST:event_entryFocusGained

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
        // TODO add your handling code here:
        save();

    }//GEN-LAST:event_jButton3KeyPressed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        save();
    }//GEN-LAST:event_jButton3MouseClicked

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "delete from bankledger where rno=" + this.recp.trim() + "";
            int result = stmt.executeUpdate(sql);
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "deleted");
                ClearAllData();
            } else {
                JOptionPane.showMessageDialog(this, "something went wrong");
            }
        } catch (Exception e) {
            Logger.getLogger(BankGroup.class.getName()).log(Level.SEVERE, null, e);
        }


    }//GEN-LAST:event_deleteActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new CreateAccountScreen().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(BankGroup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BankGroup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BankGroup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BankGroup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BankGroup().setVisible(true);
               
                
            }
        });
    }
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closebutton;
    private com.toedter.calendar.JDateChooser date;
    private javax.swing.JButton delete;
    private javax.swing.JComboBox<String> entry;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalcreditfield;
    private javax.swing.JLabel totalcreditlabel;
    private javax.swing.JTextField totaldebitfield;
    private javax.swing.JTextField txtPartyName;
    // End of variables declaration//GEN-END:variables
}
