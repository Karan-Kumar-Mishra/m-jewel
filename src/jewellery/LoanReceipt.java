/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jewellery.GetPartyName;

/**
 *
 * @author 91888
 */
public class LoanReceipt extends javax.swing.JFrame {

    /*
     * Creates new form LoanReceipt
     */
    private DefaultTableModel tableModel;
    private String[][] allLoanData; // To store all loan data for filtering
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String[] partyNames;

    public LoanReceipt() {
        initComponents();
        this.jButton4.setBackground(Color.red);
        this.jLabel1.setFont(new Font(jLabel1.getFont().getName(), Font.BOLD, 16));
        this.getContentPane().setBackground(new java.awt.Color(57, 68, 76));
        partyNames = GetPartyName.get();
        // Initialize the table model with column names
        String[] columnNames = {"Receipt No", "Account Name", "Loan Amount", "Interest Amount", "Remarks"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Cash", "Credit"}));
        if (partyNames != null && partyNames.length > 0) {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(partyNames));
        } else {
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No Parties Available"}));
        }
        jCheckBox2.setBackground(Color.white);

        jLabel6.setForeground(Color.white);

        // Add date change listener to filter data when date changes
        jDateChooser1.addPropertyChangeListener("date", evt -> {
            filterTableByDate();
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

        // Add key listeners for Enter key navigation
        addEnterKeyNavigation();

        // Load data from database
        loadLoanReceiptData();
        clearTextbox();
    }

    private void addEnterKeyNavigation() {
        // Set up Enter key navigation between components
        jDateChooser1.getComponent(0).addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jComboBox2.requestFocus();
                }
            }
        });

        jComboBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextField2.requestFocus();
                }
            }
        });

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jCheckBox1.requestFocus();
                }
            }
        });

        jCheckBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jCheckBox2.requestFocus();
                }
            }
        });

        jCheckBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextField3.requestFocus();
                }
            }
        });

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton1.requestFocus();
                }
            }
        });

        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton1.doClick();
                }
            }
        });

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jComboBox1.requestFocus();
                }
            }
        });

        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton4.requestFocus();
                }
            }
        });

        jButton4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton4.doClick();
                }
            }
        });

        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        showSelectedRowData(selectedRow);
                    }
                }
            }
        });
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

    private void loadLoanReceiptData() {
        // Get data from database using the GetLoanReceiptData class
        allLoanData = GetLoanReceiptData.getinfo();

        // Display all data initially
        displayDataInTable(allLoanData);
        // If there's data, populate the form fields with the first record
        if (allLoanData.length > 0) {
            populateFormFields(allLoanData[0]);
        }
    }

    private void displayDataInTable(String[][] data) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Populate the table with new data
        for (String[] row : data) {
            tableModel.addRow(row);
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
            // javax.swing.JOptionPane.showMessageDialog(this, "No records found for the selected date.");
        }
    }

    void populateFormFields(String[] loanRecord) {
        try {
            // Set form fields based on the loan record
            jTextField1.setText(loanRecord[0]); // SLIP_NO 
            jComboBox2.setSelectedItem(loanRecord[1]); // party Name
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
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("LOAN RECEIPT");

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(281, 281, 281))
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(64, 64, 64)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(70, 70, 70)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox1)
                                        .addComponent(jButton4))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(33, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jCheckBox2)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField2)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Save Successfully ! ");
    }                                        

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

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        try {
            // Debug: Print current values
            System.out.println("jTextField2 value: " + jTextField2.getText());
            System.out.println("jTextField4 value: " + jTextField4.getText());

            // Get and clean input - preserve decimal points
            String amountText = jTextField2.getText().trim().replaceAll("[^0-9.]", "");
            String totalText = jTextField4.getText().trim().replaceAll("[^0-9.]", "");
            JOptionPane.showMessageDialog(this, "value=> " + jTextField2.getText());

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
    private javax.swing.JComboBox<String> jComboBox2;
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
    // End of variables declaration                   
}