/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jewellery.DBController;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 *
 * @author 91888
 */
public class LoanLedger extends javax.swing.JFrame {

    /**
     * Creates new form LoanLedger
     */
    int selectedrow = 0;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    private javax.swing.JPopupMenu jPopupMenu1;
    private DefaultTableModel partyNameSuggestionsTableModel;
    private final List<Object> accountNames = new ArrayList<>();

    public LoanLedger() {
        initComponents();
        initCustomComponents();
        addEvents();
        setupEnterKeyNavigation();
        this.getContentPane().setBackground(new java.awt.Color(57, 68, 76));
        jLabel1.setForeground(Color.white);
        jLabel2.setForeground(Color.white);
        jLabel3.setForeground(Color.white);
        jLabel4.setForeground(Color.white);
        jLabel5.setForeground(Color.white);
        jLabel6.setForeground(Color.white);
        jLabel7.setForeground(Color.white);
        jButton3.setBackground(Color.red);
        jLabel1.setFont(new Font(jLabel1.getFont().getName(), Font.BOLD, 16));

    }

    private void setupEnterKeyNavigation() {
        // Create a list of components in the desired navigation order
        java.util.List<Component> navigationOrder = new ArrayList<>();
        navigationOrder.add(jTextField7); // Account Name
        navigationOrder.add(jDateChooser1.getDateEditor().getUiComponent());
        navigationOrder.add(jDateChooser2.getDateEditor().getUiComponent());
        // navigationOrder.add(jTextField1); // Receipt No

        // navigationOrder.add(jTextField2); // Amount
        navigationOrder.add(jTextField3); // Remarks
        navigationOrder.add(jButton1); // Save button
        navigationOrder.add(jButton2); // Delete button
        navigationOrder.add(jButton3); // Print button

        // Add KeyListener to each component
        for (Component comp : navigationOrder) {
            comp.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        int currentIndex = navigationOrder.indexOf(e.getComponent());
                        if (currentIndex < navigationOrder.size() - 1) {
                            navigationOrder.get(currentIndex + 1).requestFocusInWindow();
                        } else {
                            // If we're at the last component, loop back to the first
                            navigationOrder.get(0).requestFocusInWindow();
                        }

                        // Special case for checkboxes - toggle their state on ENTER
                        if (e.getComponent() instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) e.getComponent();
                            checkBox.setSelected(!checkBox.isSelected());
                        }

                        // Special case for buttons - trigger their action
                        if (e.getComponent() instanceof JButton) {
                            ((JButton) e.getComponent()).doClick();
                        }
                    }
                }
            });
        }
    }

    public void initCustomComponents() {
        this.jTextField7.setText("");
        this.jTextField6.setText("");
        this.jTextField1.setText("");
        this.jTextField2.setText("");
        this.jTextField3.setText("");
        this.jTextField4.setText("");
        this.jTextField5.setText("");

        tblPartyNameSuggestions = new javax.swing.JTable();
        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] { "Party Name", "item", "Amount" }) {
            boolean[] canEdit = new boolean[] { false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();

        // Initialize popup menu and suggestion table
        jPopupMenu1 = new javax.swing.JPopupMenu();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        tblPartyNameSuggestions.setRowHeight(20);
        tblPartyNameSuggestions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        jPopupMenu1.setLocation(jTextField7.getX() + 6, jTextField7.getY() + 70);

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Updated table structure for Loan and Interest sections
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "Date", "Remarks", "Dr. Amt", "Cr. Amt", "Balance", // Loan Section
                        "Date", "Remarks", "Dr. Amt", "Cr. Amt", "Balance" // Interest Section
                }));
    }

    public void addEvents() {

        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusLost(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyReleased(evt);
            }
        });
        tblPartyNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = tblPartyNameSuggestions.getSelectedRow();
                    if (selectedRow >= 0) {
                        jTextField7.setText(tblPartyNameSuggestions.getValueAt(selectedRow, 0).toString().trim());
                        jPopupMenu1.setVisible(false);
                        jTextField7.requestFocus();
                    }
                }
            }
        });
        tblPartyNameSuggestions.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = tblPartyNameSuggestions.getSelectedRow();
                    if (selectedRow >= 0) {
                        jTextField7.setText(tblPartyNameSuggestions.getValueAt(selectedRow, 0).toString().trim());
                        jPopupMenu1.setVisible(false);
                        jTextField7.requestFocus();
                    }
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jPopupMenu1.setVisible(false);
                    jTextField7.requestFocus();
                }
            }
        });
    }

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {
        jTextField7.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
    }

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {
        jTextField7.setBackground(Color.white);
        jPopupMenu1.setVisible(false);
    }

    private void fetchAccountNames() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> account_names = DBController.executeQuery("SELECT PARTY_NAME FROM LOAN_ENTRY");

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
            try {
                // Fix: Use the correct column index for loan amount (assuming it's index 2)
                String loanAmount = (suggestion.get(2) == null) ? "0" : suggestion.get(2).toString();

                suggestionsTable.addRow(new Object[] {
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0).toString(),
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1).toString(),
                        loanAmount // Directly use the loan amount from the query result
                });
            } catch (Exception ex) {
                Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {

        // TODO add your handling code here:
        fetchAccountNames();
        if (!(accountNames == null || accountNames.isEmpty())) {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    jPopupMenu1.setVisible(false);
                    break;
                case KeyEvent.VK_ENTER:
                    // txttotalamt.requestFocusInWindow();
                    TableModel model = tblPartyNameSuggestions.getModel();
                    JOptionPane.showMessageDialog(null, "" + model.getValueAt(1, 1));

                    break;
                case KeyEvent.VK_DOWN:
                    tblPartyNameSuggestions.requestFocus();
                    if (selectedrow == 0) {
                        tblPartyNameSuggestions.setRowSelectionInterval(0, 0);
                        selectedrow++;
                    } else {
                        if (tblPartyNameSuggestions.getSelectedRow() < tblPartyNameSuggestions.getRowCount() - 1) {
                            tblPartyNameSuggestions.setRowSelectionInterval(
                                    tblPartyNameSuggestions.getSelectedRow() + 1,
                                    tblPartyNameSuggestions.getSelectedRow() + 1);
                        }
                    }
                    jTextField7.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                case KeyEvent.VK_UP:
                    tblPartyNameSuggestions.requestFocus();

                    if (tblPartyNameSuggestions.getSelectedRow() > 0) {
                        tblPartyNameSuggestions.setRowSelectionInterval(tblPartyNameSuggestions.getSelectedRow() - 1,
                                tblPartyNameSuggestions.getSelectedRow() - 1);
                    }

                    jTextField7.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        jPopupMenu1.setVisible(true);
                        populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel,
                                "SELECT PARTY_NAME, ITEM_DETAILS, AMOUNT_PAID FROM LOAN_ENTRY ");
                    });
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("LOAN LEDGER");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField2");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jTextField5.setText("jTextField5");

        jTextField6.setText("jTextField6");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel2.setText("Total Dr.");

        jLabel3.setText("Total Cr.");

        jLabel4.setText("Balance");

        jLabel5.setText("Total Dr.");

        jLabel6.setText("Total Cr.");

        jLabel7.setText("Balance");

        jTextField7.setText("jTextField7");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTextField7))
                                .addGap(32, 32, 32)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(78, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 1,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 98,
                                                Short.MAX_VALUE))
                                .addGap(21, 21, 21)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jDateChooser1,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jDateChooser2,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1)
                                                        .addComponent(jButton2)
                                                        .addComponent(jButton3)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(14, 14, 14)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private double getInterestAmt(String partyname) {
        String credit_amount_query = "select SUM(LOAN_AMOUNT + INTREST_AMOUNT) as TOTAL_AMOUNT from LOAN_RECEIPT where PARTY_NAME='"
                + partyname + "' ";
        double totalcreditAmount = 0.0;
        try {
            totalcreditAmount = ((Number) DBController.getDataFromTable(credit_amount_query).get(0).get(0))
                    .doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalcreditAmount;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Validate dates
            if (jDateChooser1.getDate() == null || jDateChooser2.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Use SQL-compatible format
            String startDate = sdf.format(jDateChooser1.getDate());
            String endDate = sdf.format(jDateChooser2.getDate());

            // Get party name
            String partyName = jTextField7.getText().trim();
            if (partyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a party name.");
                return;
            }

            // Ensure database connection
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            // Get connection from DBConnect
            Connection conn = DBConnect.connect();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Failed to establish database connection.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Loan entry query
            String loanEntryQuery = "SELECT ENTRY_DATE, AMOUNT_PAID, INTEREST_AMOUNT, REMARKS " +
                    "FROM LOAN_ENTRY WHERE PARTY_NAME = ? AND ENTRY_DATE BETWEEN ? AND ?";
            List<List<Object>> loanEntries = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(loanEntryQuery)) {
                pstmt.setString(1, partyName);
                pstmt.setString(2, startDate);
                pstmt.setString(3, endDate);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    row.add(rs.getString("ENTRY_DATE"));
                    row.add(rs.getDouble("AMOUNT_PAID"));
                    row.add(rs.getDouble("INTEREST_AMOUNT"));
                    row.add(rs.getString("REMARKS"));
                    loanEntries.add(row);
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoanLedger.class.getName()).log(Level.SEVERE, "Error fetching loan entries", ex);
                JOptionPane.showMessageDialog(this, "Error fetching loan entries: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                conn.close();
                return;
            }

            // Loan receipt query
            String loanReceiptQuery = "SELECT TRANSACTION_DATE, LOAN_AMOUNT, INTREST_AMOUNT, REMARKS " +
                    "FROM LOAN_RECEIPT WHERE PARTY_NAME = ? AND TRANSACTION_DATE BETWEEN ? AND ?";
            List<List<Object>> loanReceipts = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(loanReceiptQuery)) {
                pstmt.setString(1, partyName);
                pstmt.setString(2, startDate);
                pstmt.setString(3, endDate);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    row.add(rs.getString("TRANSACTION_DATE"));
                    row.add(rs.getDouble("LOAN_AMOUNT"));
                    row.add(rs.getDouble("INTREST_AMOUNT"));
                    row.add(rs.getString("REMARKS"));
                    loanReceipts.add(row);
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoanLedger.class.getName()).log(Level.SEVERE, "Error fetching loan receipts", ex);
                JOptionPane.showMessageDialog(this, "Error fetching loan receipts: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                conn.close();
                return;
            }

            // Close connection
            conn.close();

            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0); // Clear existing data

            // Initialize totals
            double totalLoanDr = 0.0;
            double totalLoanCr = 0.0;
            double totalInterestDr = 0.0;
            double totalInterestCr = 0.0;
            double loanBalance = 0.0;
            double interestBalance = 0.0;

            // Process Loan Entries (Dr. for Loan and Interest)
            for (List<Object> row : loanEntries) {
                String date = row.get(0) != null ? row.get(0).toString() : "";
                String remarks = row.get(3) != null ? row.get(3).toString() : "";
                double loanDr = row.get(1) != null ? Double.parseDouble(row.get(1).toString()) : 0.0;
                double interestDr = row.get(2) != null ? Double.parseDouble(row.get(2).toString()) : 0.0;

                loanBalance += loanDr;
                interestBalance += interestDr;

                totalLoanDr += loanDr;
                totalInterestDr += interestDr;

                tableModel.addRow(new Object[] {
                        date, remarks, String.format("%.2f", loanDr), "0.00", String.format("%.2f", loanBalance), // Loan
                                                                                                                  // Section
                        date, remarks, String.format("%.2f", interestDr), "0.00", String.format("%.2f", interestBalance) // Interest
                                                                                                                         // Section
                });
            }

            // Process Loan Receipts (Cr. for Loan and Interest)
            for (List<Object> row : loanReceipts) {
                String date = row.get(0) != null ? row.get(0).toString() : "";
                String remarks = row.get(3) != null ? row.get(3).toString() : "";
                double loanCr = row.get(1) != null ? Double.parseDouble(row.get(1).toString()) : 0.0;
                double interestCr = row.get(2) != null ? Double.parseDouble(row.get(2).toString()) : 0.0;

                loanBalance -= loanCr;
                interestBalance -= interestCr;

                totalLoanCr += loanCr;
                totalInterestCr += interestCr;

                tableModel.addRow(new Object[] {
                        date, remarks, "0.00", String.format("%.2f", loanCr), String.format("%.2f", loanBalance), // Loan
                                                                                                                  // Section
                        date, remarks, "0.00", String.format("%.2f", interestCr), String.format("%.2f", interestBalance) // Interest
                                                                                                                         // Section
                });
            }

            // Update summary fields
            jTextField1.setText(String.format("%.2f", totalLoanDr)); // Total Loan Dr.
            jTextField2.setText(String.format("%.2f", totalLoanCr)); // Total Loan Cr.
            jTextField3.setText(String.format("%.2f", loanBalance)); // Loan Balance
            jTextField6.setText(String.format("%.2f", totalInterestDr)); // Total Interest Dr.
            jTextField5.setText(String.format("%.2f", totalInterestCr)); // Total Interest Cr.
            jTextField4.setText(String.format("%.2f", interestBalance)); // Interest Balance

            JOptionPane.showMessageDialog(this, "Data loaded successfully!");

        } catch (Exception ex) {
            Logger.getLogger(LoanLedger.class.getName()).log(Level.SEVERE, "Error in jButton1ActionPerformed", ex);
            JOptionPane.showMessageDialog(this, "Error fetching data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField6ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
