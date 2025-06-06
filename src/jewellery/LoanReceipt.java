/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.event.KeyEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import jewellery.GetPartyName;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.JTextComponent;
import jewellery.GetLoanData;
import jewellery.helper.outstandingAnalysisHelper;
import jewellery.DBController;
import jewellery.DatabaseTableCreator;
import jewellery.TableRowCounter;
import java.text.SimpleDateFormat;
import jewellery.GetInterestAmount;

/**
 *
 * @author 91888
 */
public class LoanReceipt extends javax.swing.JFrame {

    /*
     * Creates new form LoanReceipt
     */
    int selectedrow = 0;
    Vector<String> banks;
    private DefaultTableModel tableModel;
    private final List<Object> accountNames = new ArrayList<>();
    private String[][] allLoanData; // To store all loan data for filtering
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String[] partyNames;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    private final DefaultTableModel partyNameSuggestionsTableModel;

    public LoanReceipt() {
        DatabaseTableCreator.create();
        // GetInterestAmount.processAllLoanEntries();
        initComponents();
        try {
            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();
            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            jComboBox1.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");

            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.jButton4.setBackground(Color.red);
        this.jLabel1.setFont(new Font(jLabel1.getFont().getName(), Font.BOLD, 16));
        this.getContentPane().setBackground(new java.awt.Color(57, 68, 76));
        partyNames = GetPartyName.get();
        // Initialize the table model with column names
        String[] columnNames = { "Receipt No", "Account Name", "Loan Amount", "Interest Amount", "Remarks" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setupEnterKeyNavigation();
        jTable1.setModel(tableModel);
        jDateChooser1.setDate(new Date());
        this.jLabel1.setForeground(Color.white);
        this.jLabel2.setForeground(Color.white);
        this.jLabel3.setForeground(Color.white);
        this.jLabel4.setForeground(Color.white);
        this.jLabel5.setForeground(Color.white);
        this.jLabel6.setForeground(Color.white);
        this.jLabel7.setForeground(Color.white);
        this.jLabel8.setForeground(Color.white);
        this.jLabel9.setForeground(Color.white);
        jCheckBox1.setBackground(Color.white);
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();

        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PAYMENTMODEFocusGained(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PAYMENTMODEActionPerformed(evt);
            }
        });
        jLabel6.setForeground(Color.white);

        // Add date change listener to filter data when date changes
        jDateChooser1.addPropertyChangeListener("date", evt -> {
            filterTableByDate();
        });
        jDateChooser1.setSize(5, 5);
        txtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusLost(evt);
            }
        });
        txtPartyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyReleased(evt);
            }
        });

        // Add selection listener to the table
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        showSelectedRowData(selectedRow);
                    }
                }
            }
        });
        tblPartyNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = tblPartyNameSuggestions.getSelectedRow();
                    if (selectedRow >= 0) {
                        txtPartyName.setText(tblPartyNameSuggestions.getValueAt(selectedRow, 0).toString().trim());
                        jPopupMenu1.setVisible(false);
                        txtPartyName.requestFocus();
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
                        txtPartyName.setText(tblPartyNameSuggestions.getValueAt(selectedRow, 0).toString().trim());
                        jPopupMenu1.setVisible(false);
                        txtPartyName.requestFocus();
                    }
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jPopupMenu1.setVisible(false);
                    txtPartyName.requestFocus();
                }
            }
        });

        // Add key listeners for Enter key navigation
        // Load data from database
        clearTextbox();
        jTextField1.setText(String.valueOf(TableRowCounter.getRowCount("LOAN_RECEIPT") + 1));
        loadLoanReceiptData();
    }

    private void PAYMENTMODEFocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        try {

            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();
            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            jComboBox1.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");

            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
                // JOptionPane.showMessageDialog(this, rs1.getString("accountname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(banks);
        jComboBox1.setModel(model);
    }

    private void PAYMENTMODEActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

    }

    // Add this method to your LoanReceipt class
    private void setupEnterKeyNavigation() {
        // Create a list of components in the desired navigation order
        java.util.List<Component> navigationOrder = new ArrayList<>();
        navigationOrder.add(jDateChooser1.getDateEditor().getUiComponent());
        navigationOrder.add(jTextField1); // Receipt No
        navigationOrder.add(jComboBox1); // Payment Mode
        navigationOrder.add(txtPartyName); // Account Name
        navigationOrder.add(jTextField2); // Amount
        navigationOrder.add(jCheckBox1); // Loan Amt checkbox
        navigationOrder.add(jCheckBox2); // Int Amt checkbox
        navigationOrder.add(jTextField3); // Remarks
        navigationOrder.add(jButton1); // Save button
        navigationOrder.add(jButton2); // Delete button
        navigationOrder.add(jButton3); // Print button
        navigationOrder.add(jButton4); // Close button

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

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {
        txtPartyName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
    }

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {
        txtPartyName.setBackground(Color.white);
        jPopupMenu1.setVisible(false);
    }

    private void loadLoanReceiptData() {
        try {
            // Connect to database if not already connected
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            // Query to get all loan receipt data
            String query = "SELECT RECEIPT_NO, PARTY_NAME, LOAN_AMOUNT, INTREST_AMOUNT, REMARKS, TRANSACTION_DATE "
                    + "FROM LOAN_RECEIPT;";

            // Execute query using getDataFromTableforcompany()
            List<List<Object>> results = DBController.getDataFromTable(query);

            // Check if results are null (error occurred)
            if (results == null) {
                JOptionPane.showMessageDialog(this,
                        "Error loading loan receipt data: No data returned",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convert results to 2D array for easier handling
            allLoanData = new String[results.size()][6]; // 6 columns in our query

            for (int i = 0; i < results.size(); i++) {
                List<Object> row = results.get(i);
                for (int j = 0; j < row.size(); j++) {
                    allLoanData[i][j] = (row.get(j) == null) ? "" : row.get(j).toString();
                }
            }

            // Display data in table
            displayDataInTable(allLoanData);

            // Filter by current date
            // filterTableByDate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading loan receipt data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
                    txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                case KeyEvent.VK_UP:
                    tblPartyNameSuggestions.requestFocus();

                    if (tblPartyNameSuggestions.getSelectedRow() > 0) {
                        tblPartyNameSuggestions.setRowSelectionInterval(tblPartyNameSuggestions.getSelectedRow() - 1,
                                tblPartyNameSuggestions.getSelectedRow() - 1);
                    }

                    txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        jPopupMenu1.setVisible(true);
                        populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel,
                                "SELECT PARTY_NAME, AMOUNT_PAID, ITEM_DETAILS FROM LOAN_ENTRY"
                                        + " WHERE PARTY_NAME LIKE " + "'" + txtPartyName.getText() + "%'");
                    });
                    break;
            }
        }
    }

    private void showSelectedRowData(int rowIndex) {
        // Temporarily remove the date change listener
        java.beans.PropertyChangeListener[] listeners = jDateChooser1.getPropertyChangeListeners("date");
        for (java.beans.PropertyChangeListener listener : listeners) {
            jDateChooser1.removePropertyChangeListener("date", listener);
        }

        // Get the data from the selected row
        String receiptNo = (String) jTable1.getValueAt(rowIndex, 0);
        String accountName = (String) jTable1.getValueAt(rowIndex, 1);
        String loanAmount = (String) jTable1.getValueAt(rowIndex, 2);
        String interestAmount = (String) jTable1.getValueAt(rowIndex, 3);
        String remarks = (String) jTable1.getValueAt(rowIndex, 4);

        // Find the complete record in allLoanData
        for (String[] loanRecord : allLoanData) {
            if (loanRecord[0].equals(receiptNo)) {
                // Populate the form fields with the complete record
                populateFormFields(loanRecord);
                break;
            }
        }

        // Restore the date change listener
        for (java.beans.PropertyChangeListener listener : listeners) {
            jDateChooser1.addPropertyChangeListener("date", listener);
        }
    }

    private void filterTableByDate() {
        Date selectedDate = jDateChooser1.getDate();
        if (selectedDate == null || allLoanData == null) {
            return;
        }

        String selectedDateStr = dateFormat.format(selectedDate);
        List<String[]> filteredData = new ArrayList<>();

        for (String[] row : allLoanData) {
            // Assuming the date is in the 6th column (index 5) - adjust if needed
            if (row.length > 5 && row[5] != null) {
                // Compare formatted date strings
                if (row[5].equals(selectedDateStr)) {
                    filteredData.add(row);
                }
            }
        }

        // Convert to array and display
        String[][] filteredArray = filteredData.toArray(new String[0][0]);
        displayDataInTable(filteredArray);

        // Optional: Show message if no records found
        if (filteredArray.length == 0) {
            // javax.swing.JOptionPane.showMessageDialog(this, "No records found for the
            // selected date.");
        }
    }

    void populateFormFields(String[] loanRecord) {
        try {
            // Set form fields based on the loan record
            jTextField1.setText(loanRecord[0]); // SLIP_NO
            txtPartyName.setText(loanRecord[1]); // party Name
            jTextField2.setText(loanRecord[2]); // Loan Amount
            jTextField3.setText(loanRecord[4]); // Remarks
            jTextField4.setText(loanRecord[2]); // Total Amount

            jDateChooser1.setDate(new Date());

            // Set checkboxes based on the type of amount
            jCheckBox1.setSelected(true); // Assuming it's always a loan amount
            jCheckBox2.setSelected(loanRecord[3] != null && !loanRecord[3].isEmpty() && !loanRecord[3].equals("0"));

        } catch (Exception e) {
            System.err.println("Error populating form fields: " + e.getMessage());
        }
    }

    private void clearTextbox() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        txtPartyName.setText("");
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        banks = new Vector<>();
        banks.add("Cash");
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jDateChooser1.setDate(new Date());
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();

        // Set up the table model for suggestions
        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] { "Party Name", "item", "Amount" }) {
            boolean[] canEdit = new boolean[] { false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblPartyNameSuggestions.setRowHeight(20);
        tblPartyNameSuggestions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        jPopupMenu1.setLocation(txtPartyName.getX() + 6, txtPartyName.getY() + 250);

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("LOAN RECEIPT");

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(banks);
        jComboBox1.setModel(model);

        jTextField2.setText("jTextField2");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Loan Amt.");

        jCheckBox2.setText("Int Amt.");

        jTextField3.setText("jTextField3");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        jTextField4.setText("jTextField4");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel2.setText("TOTAL AMOUNT");

        jButton2.setText("Delete");

        jButton3.setText("Print");

        jButton4.setText("Close");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Amount");

        jLabel4.setText("Adjustment");

        jLabel5.setText("Remarks");

        jLabel6.setText("Account name");

        jLabel7.setText("Date");

        jLabel8.setText(" Receipt No.");

        jLabel9.setText("Payment mode");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 169,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(281, 281, 281))
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtPartyName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(13, 13, 13)
                                                                .addComponent(jLabel6,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 181,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel3,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(70, 70, 70)
                                                                .addComponent(jLabel4,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(64, 64, 64)
                                                                                .addComponent(jTextField3,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        113,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(70, 70, 70)
                                                                                .addComponent(jLabel5,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        91,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jTextField2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jCheckBox1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jCheckBox2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(13, 13, 13)
                                                                .addComponent(jLabel7,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jDateChooser1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTextField1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                143,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel8,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                115,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel9,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                137,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jComboBox1,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        257,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jButton4,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        84,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                .addContainerGap(151, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel8,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel9,
                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel5)
                                                .addGap(30, 30, 30))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jDateChooser1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                20,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jTextField1,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jComboBox1)
                                                                                .addComponent(jButton4))))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(33, Short.MAX_VALUE)
                                                                .addComponent(jLabel1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)))
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel4)
                                                                        .addComponent(jLabel3)
                                                                        .addComponent(jLabel6))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(txtPartyName,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jCheckBox1)
                                                                                .addComponent(jCheckBox2)
                                                                                .addComponent(jTextField3,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        26,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jTextField2)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(22, 22, 22)
                                                                .addComponent(jButton1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 26,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(13, 13, 13)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13,
                                        Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap()));

        pack();
    }// </editor-fold>

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        dispose();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jCheckBox1.isSelected() && jCheckBox2.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please select one option in loan adjustment not both!");
            return;
        }
        if (!jCheckBox1.isSelected() && !jCheckBox2.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please select one option in loan adjustment !");
            return;
        }
        try {
            // 1. Get all data from form fields
            String receiptNo = jTextField1.getText().trim();
            String partyName = txtPartyName.getText().trim();
            String loanAmountStr = jCheckBox1.isSelected() ? jTextField2.getText().trim() : "0";
            String interestAmountStr = jCheckBox2.isSelected() ? jTextField2.getText().trim() : "0";
            String remarks = jTextField3.getText().trim();
            String totalAmountStr = jTextField4.getText().trim();
            Date transactionDate = jDateChooser1.getDate();
            String paymentMode = jComboBox1.getSelectedItem().toString();
            boolean isLoanAmount = jCheckBox1.isSelected();
            boolean isInterestAmount = jCheckBox2.isSelected();

            // 2. Validate required fields
            if (partyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Account Name", "Error", JOptionPane.ERROR_MESSAGE);
                txtPartyName.requestFocus();
                return;
            }

            if (loanAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Amount", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField2.requestFocus();
                return;
            }

            if (transactionDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid date", "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooser1.requestFocus();
                return;
            }

            // 3. Parse numeric values with proper error handling
            double loanAmount = 0;
            double interestAmount = 0;
            double totalAmount = 0;

            try {
                loanAmount = Double.parseDouble(loanAmountStr);
                if (!totalAmountStr.isEmpty()) {
                    totalAmount = Double.parseDouble(totalAmountStr);
                }

                if (isInterestAmount) {
                    interestAmount = Double.parseDouble(interestAmountStr);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for amounts",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Prepare data for display or database operation
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(transactionDate);

            if (1 == 1) {
                // 6. Add data to the table
                String[] newRow = {
                        receiptNo.isEmpty() ? "[New Entry]" : receiptNo,
                        partyName,
                        String.valueOf(loanAmount),
                        String.valueOf(interestAmount),
                        remarks
                };
                tableModel.addRow(newRow); // Directly add to table model

                // Prepare data for database insertion
                List<Object> columnNames = new ArrayList<>();
                columnNames.add("RECEIPT_NO");
                columnNames.add("PARTY_NAME");
                columnNames.add("LOAN_AMOUNT");
                columnNames.add("INTREST_AMOUNT");
                columnNames.add("REMARKS");
                columnNames.add("TRANSACTION_DATE");
                columnNames.add("PAYMENT_MODE");

                List<Object> dataValues = new ArrayList<>();
                dataValues.add(receiptNo.isEmpty() ? null : receiptNo);
                dataValues.add(partyName);
                dataValues.add(Double.parseDouble(loanAmountStr));
                dataValues.add(Double.parseDouble(interestAmountStr));
                dataValues.add(remarks);
                dataValues.add(formattedDate);
                dataValues.add(paymentMode);

                boolean insertSuccess = DBController.insertDataIntoTable("LOAN_RECEIPT", columnNames, dataValues);

                // Get loan details for interest calculation
                List<Object> result = DBController.executeQuery(
                        "SELECT AMOUNT_PAID ,INTEREST_DATE_PERCENTAGE,INTREST_TYPE,START_DATE FROM LOAN_ENTRY WHERE PARTY_NAME='"
                                + partyName + "'");

                double amountPaid = 0.0;
                double INTEREST_DATE_PERCENTAGE = 0.0;
                String INTREST_TYPE = "";
                String dateString = "";

                if (!result.isEmpty()) {
                    try {
                        Object value = result.get(0);
                        Object value2 = result.get(1);
                        Object value3 = result.get(2);
                        Object value4 = result.get(3);

                        if (value instanceof Number) {
                            amountPaid = ((Number) value).doubleValue();
                            INTEREST_DATE_PERCENTAGE = ((Number) value2).doubleValue();
                            INTREST_TYPE = value3.toString();
                            dateString = value4.toString();
                        } else if (value != null) {
                            amountPaid = Double.parseDouble(value.toString());
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Error converting AMOUNT_PAID to number: " + e.getMessage(),
                                "Data Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    GetInterestAmount.updateAllInterestAmounts(partyName);
                }

                // Calculate interest
                double dailyInterest = (amountPaid * INTEREST_DATE_PERCENTAGE / 100) / 30;
                double totalInterest;
                long rowDays = 0;

                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat1.parse(dateString);
                java.sql.Date startDate = new java.sql.Date(parsedDate.getTime());
                rowDays = (System.currentTimeMillis() - startDate.getTime()) / (1000 * 60 * 60 * 24);

                if (INTREST_TYPE.equals("Day")) {
                    totalInterest = (dailyInterest * (rowDays) / 30) * rowDays;
                } else {
                    long month = rowDays / 30;
                    totalInterest = (dailyInterest * (rowDays)) * month;
                }

                // Update loan amount if checkbox is selected
                if (jCheckBox1.isSelected()) {
                    amountPaid = amountPaid - Double.parseDouble(loanAmountStr);
                    // DBController.executeQueryUpdate("UPDATE LOAN_ENTRY SET AMOUNT_PAID='" +
                    // amountPaid + "' WHERE PARTY_NAME ='" + partyName + "';");
                }
                if (jCheckBox2.isSelected()) {
                    // PENDING FOR INTEREST AMOUNT
                    // DBController.executeQueryUpdate("UPDATE LOAN_ENTRY SET INTEREST_AMOUNT='" +
                    // interestAmountStr + "' WHERE PARTY_NAME ='" + partyName + "';");
                }

                // Show success message only once
                if (insertSuccess) {
                    clearTextbox();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to save data to database",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        jTextField1.setText(String.valueOf(TableRowCounter.getRowCount("LOAN_RECEIPT") + 1));
        loadLoanReceiptData();
        GetInterestAmount.processAllLoanEntries();
    }

    private void displayDataInTable(String[][] data) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Ensure data exists
        if (data == null || data.length == 0) {
            return;
        }

        // Add all rows that have enough columns
        for (String[] row : data) {
            // Create a new row with 5 columns, filling with empty strings if needed
            Object[] tableRow = new Object[5];
            for (int i = 0; i < 5; i++) {
                tableRow[i] = (i < row.length && row[i] != null) ? row[i] : "";
            }
            tableModel.addRow(tableRow);
        }

        // Force table to update its view
        tableModel.fireTableDataChanged();
        jTable1.revalidate();
        jTable1.repaint();
    }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Debug: Print current values
            System.out.println("jTextField2 value: " + jTextField2.getText());
            System.out.println("jTextField4 value: " + jTextField4.getText());

            // Get and clean input - preserve decimal points
            String amountText = jTextField2.getText().trim().replaceAll("[^0-9.]", "");
            String totalText = jTextField4.getText().trim().replaceAll("[^0-9.]", "");

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (totalText.isEmpty()) {
                totalText = "0"; // Default to 0 if empty
            }

            // Parse values as doubles to handle decimals
            double amount = Double.parseDouble(amountText);
            double totalAmount = Double.parseDouble(totalText);

            // Calculate and update
            double newAmount = totalAmount - amount;

            // Format the result to show 2 decimal places
            jTextField4.setText(String.format("%.2f", newAmount));

            // Debug confirmation
            System.out.println("Calculation: " + totalAmount + " - " + amount + " = " + newAmount);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format!\nPlease enter a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Debug the exact error
        }
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField txtPartyName;

    // End of variables declaration
}
