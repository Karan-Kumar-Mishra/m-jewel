/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import jewellery.helper.tableObject;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author Sachin
 */
public class DayBook extends javax.swing.JFrame {

    /**
     * Creates new form DayBook
     */
    private List<tableObject> initialTableData;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DayBook() {
        initComponents();
        ((DefaultTableModel) daybooktable.getModel()).setRowCount(0);
        setDefaultDate();
        searchbutton.requestFocus();
    }

    private void setDefaultDate() {
        fromdate.setDate(new Date());
        todate.setDate(new Date());
    }

    private void fillDayBookTableForDate(Date from, Date to) {
        initialTableData = new ArrayList<>();
        String startDate = dateFormat.format(DateUtils.addDays(from, -1));
        String endDate = dateFormat.format(DateUtils.addDays(to, +1));
        try {
            DefaultTableModel m = (DefaultTableModel) daybooktable.getModel();
            m.setRowCount(0);

            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select distinct(bill) from sales where date between '" + startDate + "' and '"
                    + endDate + "'");

            while (rs.next()) {
                int bill = rs.getInt("Bill");
                Statement salestmt = con.createStatement();
                ResultSet saleRs = salestmt.executeQuery("select partyname, sum(netamount),sum(gstamt), date,terms from sales where bill =" + bill);
                Date date = new Date();
                String remark = "", name = "";
                double amount = 0;
                double gstamt = 0;
                String terms = "";
                while (saleRs.next()) {
                    date = saleRs.getDate("date");
                    remark = "Sale Bill No." + String.valueOf(bill);
                    name = saleRs.getString("partyname");
                    amount = saleRs.getDouble("sum(netamount)");
                    gstamt = saleRs.getDouble("sum(gstamt)");
                    terms = saleRs.getString("terms");

                }
                salestmt.close();
                saleRs.close();
                if (terms.trim().equalsIgnoreCase("Cash")) {
                    tableObject tableObj = new tableObject(date, "Cash", amount, 0.0, 0, remark, "Sale");
                    tableObject tableObj1 = new tableObject(date, "Sale", 0.0, amount - gstamt, 1, remark, " ");
                    tableObject tableObj2 = new tableObject(date, "CGST", 0.0, gstamt / 2, 1, remark, " ");
                    tableObject tableObj3 = new tableObject(date, "SGST", 0.0, gstamt / 2, 1, remark, " ");

                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                    initialTableData.add(tableObj2);
                    initialTableData.add(tableObj3);
                } else {
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Sale");
                    tableObject tableObj1 = new tableObject(date, "Sale", 0.0, amount - gstamt, 1, remark, " ");
                    tableObject tableObj2 = new tableObject(date, "CGST", 0.0, gstamt / 2, 1, remark, " ");
                    tableObject tableObj3 = new tableObject(date, "SGST", 0.0, gstamt / 2, 1, remark, " ");

                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                    initialTableData.add(tableObj2);
                    initialTableData.add(tableObj3);
                }
            }

            stmt.clearBatch();

            ResultSet rs1 = stmt.executeQuery("select * from receipt where not Receiptno='-1' and  date between '" + startDate + "' and '"
                    + endDate + "';");
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");
                String mop = rs1.getString("mop");
                String remark = "Rcpt. No." + String.valueOf(rs1.getInt("Receiptno")) + ", " + mop;
                tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Receipt");
                tableObject tableObj1 = new tableObject(date, mop, 0.0, amount, 1, remark, " ");
                initialTableData.add(tableObj);
                initialTableData.add(tableObj1);
            }

            stmt.clearBatch();

            rs1 = stmt.executeQuery("select * from payments where date between '" + startDate + "' and '"
                    + endDate + "';");
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");
                String mop = rs1.getString("mop");
                String remark = "Pymt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + mop;
                tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Payment");
                tableObject tableObj1 = new tableObject(date, mop, amount, 0.0, 0, remark, "");
                initialTableData.add(tableObj);
                initialTableData.add(tableObj1);
            }

            stmt.clearBatch();
            rs1 = stmt.executeQuery("select distinct(bill) from purchasehistory where date between '" + startDate + "' and '"
                    + endDate + "';");
            while (rs1.next()) {
                String bill = String.valueOf(rs1.getString("bill"));
                Statement stitem = con.createStatement();
                ResultSet rst = stitem.executeQuery("select partyname,terms, sum(CAST(net_amount as DECIMAL(10,2))) as d,sum(CAST(taxable_amt as DECIMAL(10,2))) as txamt, date from purchasehistory where bill ='" + bill + "';");
                String name = "", remark = "";
                double amount = 0;
                double gstamt = 0;
                String terms = "";
                Date date = new Date();
                while (rst.next()) {
                    name = rst.getString("partyname");
                    amount = rst.getDouble("d");
                    date = rst.getDate("date");
                    terms = rst.getString("terms");
                    remark = "Pur. Bill. No." + bill;
                    gstamt = rst.getDouble("d") - rst.getDouble("txamt");
                }
                if (terms.trim().equalsIgnoreCase("Cash")) {
                    tableObject tableObj = new tableObject(date, "Cash", 0.0, amount, 1, remark, "Purchase");
                    tableObject tableObj1 = new tableObject(date, "Purchase", amount - gstamt, 0.0, 0, remark, " ");
                    tableObject tableObj2 = new tableObject(date, "CGST", gstamt / 2, 0.0, 0, remark, " ");
                    tableObject tableObj3 = new tableObject(date, "SGST", gstamt / 2, 0.0, 0, remark, " ");
                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                    initialTableData.add(tableObj2);
                    initialTableData.add(tableObj3);
                } else {
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Purchase");
                    tableObject tableObj1 = new tableObject(date, "Purchase", amount - gstamt, 0.0, 0, remark, " ");
                    tableObject tableObj2 = new tableObject(date, "CGST", gstamt / 2, 0.0, 0, remark, " ");
                    tableObject tableObj3 = new tableObject(date, "SGST", gstamt / 2, 0.0, 0, remark, " ");
                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                    initialTableData.add(tableObj2);
                    initialTableData.add(tableObj3);
                }
                stitem.close();
                rst.close();
            }
            stmt.clearBatch();
            rs1 = stmt.executeQuery("select distinct(rno) from bankledger where sales_Bill='-1' and date between '" + startDate + "' and '"
                    + endDate + "';");
            while (rs1.next()) {
                String bill = String.valueOf(rs1.getString("rno"));
                Statement stitem = con.createStatement();
                ResultSet rst = stitem.executeQuery("select bankname, sum(amt),type, date from bankledger where rno ='" + bill + "';");
                String name = "", remark = "";
                double amount = 0;
                Date date = new Date();
                boolean bool = false;
                while (rst.next()) {
                    name = rst.getString("bankname");
//                    if(rst.getDouble("sum(net_amount)")

                    if (rst.getString("type").trim().equals("deposit")) {
                        bool = true;
                    }
                    amount = rst.getDouble("sum(amt)");
                    date = rst.getDate("date");
                    remark = "bank. Bill. No." + bill;
                }
                if (bool) {
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Bank Group");
                    tableObject tableObj1 = new tableObject(date, name, amount, 0.0, 0, remark, " ");

                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                } else {
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Bank Group");
                    tableObject tableObj1 = new tableObject(date, name, 0.0, amount, 1, remark, " ");

                    initialTableData.add(tableObj);
                    initialTableData.add(tableObj1);
                }
                stitem.close();
                rst.close();
            }
            con.close();
            stmt.close();
            rs1.close();

//            Collections.sort(initialTableData, (tableObject p1, tableObject p2) -> {
//                boolean res = p1.date.before(p2.date);
//                if (res) {
//                    return -1;
//                } else {
//                    return 1;
//                }
//            });
            Collections.sort(initialTableData, new Comparator<tableObject>() {
                @Override
                public int compare(tableObject p1, tableObject p2) {
                    if (p1.date.before(p2.date)) {
                        return -1;
                    } else if (p1.date.after(p2.date)) {
                        return 1;
                    } else {
                        return 0; // dates are equal
                    }
                }
            });

            double credit = 0.0, debit = 0.0;
            for (tableObject i : initialTableData) {
                credit += i.credit;
                debit += i.debit;

                String dateStr = "";
                if (i.type.equals("Sale") || i.type.equals("Purchase")) {
                    dateStr = dateFormat.format(i.date);
                }

                if (i.creditOrDebit == 0) { // credit
                    m.addRow(new Object[]{dateStr, i.type, i.name, i.remark, String.format("%.2f", i.credit), ""});
                } else {
                    m.addRow(new Object[]{dateStr, i.type, i.name, i.remark, "", String.format("%.2f", i.debit)});
                }
            }

//            double credit = 0.0, debit = 0.0;
//            for (tableObject i : initialTableData) {
//                credit += i.credit;
//                debit += i.debit;
//
//                if (i.creditOrDebit == 0) // credit
//                {
//                    m.addRow(new Object[]{i.date, i.type, i.name, i.remark, String.format("%.2f", i.credit), ""});
//                } else {
//                    m.addRow(new Object[]{i.date, i.type, i.name, i.remark, "", String.format("%.2f", i.debit)});
//                }
//            }
            totalCreditField.setText(String.format("%.2f", credit));
            totalDebitField.setText(String.format("%.2f", debit));

            initialTableData.clear();

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.LEFT);
            for (int g = 0; g < daybooktable.getColumnCount(); g++) {
                daybooktable.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }

        } catch (SQLException e) {
            Logger.getLogger(DayBook.class.getName()).log(Level.SEVERE, null, e);

            e.printStackTrace();
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

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        todate = new com.toedter.calendar.JDateChooser();
        fromdate = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        searchbutton = new javax.swing.JButton();
        closebutton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        daybooktable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        totalCreditField = new javax.swing.JTextField();
        totalDebitField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(57, 68, 76));

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));
        jPanel1.setPreferredSize(new java.awt.Dimension(1243, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("From : ");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("To :");

        searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchbutton.setText("Search");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });

        closebutton.setBackground(new java.awt.Color(255, 0, 0));
        closebutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        closebutton.setForeground(new java.awt.Color(51, 51, 0));
        closebutton.setText("Close");
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(57, 68, 76));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Day Book");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdate, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(229, 229, 229))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(todate, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbutton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(57, 68, 76));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1143, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N

        daybooktable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Type", "Account", "Remarks", "Debit", "Credit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        daybooktable.setRowHeight(25);
        JTableHeader header = daybooktable.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        daybooktable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                daybooktableMouseClicked(evt);
            }
        });
        daybooktable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                daybooktableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(daybooktable);

        jPanel3.setBackground(new java.awt.Color(57, 68, 76));
        jPanel3.setMinimumSize(new java.awt.Dimension(540, 26));
        jPanel3.setPreferredSize(new java.awt.Dimension(540, 26));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(238, 188, 81));
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Total Credit : ");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 30));

        totalCreditField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalCreditFieldActionPerformed(evt);
            }
        });
        jPanel3.add(totalCreditField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 140, 30));
        jPanel3.add(totalDebitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 140, 30));

        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Total Debit : ");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 120, 30));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void totalCreditFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalCreditFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalCreditFieldActionPerformed

    private void daybooktableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_daybooktableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {

            int selectedRow = daybooktable.getSelectedRow();
            String type = daybooktable.getValueAt(selectedRow, 1).toString();
            if (type.equals("Receipt")) {
                String value = daybooktable.getValueAt(selectedRow, 3).toString();
                int end = 9;
                while (end < value.length()) {
                    if (value.charAt(end) == ',') {
                        break;
                    }
                    end++;
                }
                String selectedReceiptNumber = value.substring(9, end);
                ReceiptScreen rc = null;
                try {
                    rc = new ReceiptScreen();
                } catch (Exception ex) {
                    Logger.getLogger(DayBook.class.getName()).log(Level.SEVERE, null, ex);
                }
                rc.setVisible(true);
                rc.fillByRedirect(selectedReceiptNumber);
            } else if (type.equals("Payment")) {
                String value = daybooktable.getValueAt(selectedRow, 3).toString();
                int end = 9;
                while (end < value.length()) {
                    if (value.charAt(end) == ',') {
                        break;
                    }
                    end++;
                }
                int selectedReceiptNumber = Integer.parseInt(value.substring(9, end));
                PaymentScreen paymentScreen = new PaymentScreen();
                paymentScreen.setVisible(true);
                paymentScreen.paymentHistoryRedirectFill(selectedReceiptNumber);
            } else if (type.equals("Sale")) {
                String recp = daybooktable.getValueAt(selectedRow, 3).toString();
                String billNo = recp.substring(13, recp.length());
                SaleScreen sc = new SaleScreen();

                try {
                    sc.saleRegisterRedirect(Integer.parseInt(billNo));
                } catch (SQLException ex) {
                    Logger.getLogger(DayBook.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DayBook.class.getName()).log(Level.SEVERE, null, ex);
                }
                sc.setVisible(true);
                sc.closebtn.setVisible(false);
            } else if (type.equals("Purchase")) {
                String recp = daybooktable.getValueAt(selectedRow, 3).toString();
                String billNo = recp.substring(14, recp.length());
                PurchaseScreen sc = new PurchaseScreen();
                sc.sale_Bill(Integer.parseInt(billNo));
                sc.setVisible(true);
            } else if (type.equalsIgnoreCase("Bank Group")) {
                String recp = daybooktable.getValueAt(selectedRow, 3).toString();
                String billNo = recp.substring(15, recp.length());
                BankGroup bg = null;
                try {
                    bg = new BankGroup();
                } catch (Exception ex) {
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                bg.setVisible(true);
                bg.fillByRedirect(billNo);
            }
        }
    }//GEN-LAST:event_daybooktableMouseClicked

    private void daybooktableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_daybooktableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_daybooktableKeyPressed

    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed
        // TODO add your handling code here:
        try {
            fillDayBookTableForDate(fromdate.getDate(), todate.getDate());
        } catch (Exception e) {
            Logger.getLogger(DayBook.class.getName()).log(Level.SEVERE, null, e);

            JOptionPane.showMessageDialog(this, e);
        }

    }//GEN-LAST:event_searchbuttonActionPerformed

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebuttonActionPerformed
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        dispose();
    }//GEN-LAST:event_closebuttonActionPerformed

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
            java.util.logging.Logger.getLogger(DayBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DayBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DayBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DayBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DayBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closebutton;
    private javax.swing.JTable daybooktable;
    private com.toedter.calendar.JDateChooser fromdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchbutton;
    private com.toedter.calendar.JDateChooser todate;
    private javax.swing.JTextField totalCreditField;
    private javax.swing.JTextField totalDebitField;
    // End of variables declaration//GEN-END:variables
}

//class tableObject {
//
//    public Date date;
//    public String name;
//    public double credit;
//    public double debit;
//    public int creditOrDebit;  // 0 is credit, 1 is debit
//    public String remark;
//    public String type;
//
//    public tableObject(Date date, String name, double credit, double debit, int creditOrDebit, String remark, String type) {
//        this.date = date;
//        this.name = name;
//        this.credit = credit;
//        this.debit = debit;
//        this.creditOrDebit = creditOrDebit;
//        this.remark = remark;
//        this.type = type;
//    }
//}
