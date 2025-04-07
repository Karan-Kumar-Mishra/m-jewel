package jewellery;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import static jewellery.DBConnect.connect;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ASUS
 */
public class SaleRegisterScreen extends javax.swing.JFrame {

    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private static String currentDate;
    private static List<List<Object>> selectedDatesData;
    private static DefaultTableModel salesListTableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
    private Logger logger = Logger.getLogger(SaleRegisterScreen.class.getName());

    public SaleRegisterScreen() {
        initComponents();
        System.out.println("Hello");
        salesListTableModel = (DefaultTableModel) tblSalesList.getModel();
        centerTableCells();
        currentDate = getCurrentDate("yyyy-MM-dd");

        SwingUtilities.invokeLater(() -> {
            populateSalesListTable();
        });

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;
        jSplitPane2.setSize(w - 280, h - 125);
        dateFrom.setDate(new Date());
        dateTo.setDate(new Date());
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblSalesList
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();

        return dateTimeFormatter.format(localDateTime);
    }

    @SuppressWarnings("empty-statement")
    public void populateSalesListTable(String fromDate, String toDate) {
        if (selectedDatesData != null && !selectedDatesData.isEmpty()) {
            selectedDatesData.clear();
        }

        salesListTableModel.setRowCount(0);

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
        

        selectedDatesData = DBController.getDataFromTable("SELECT date,"
                + "bill, partyname, netwt, diamondwt,"
                + " taxable_amount,labour,Discount,qty,gstamt,netamount FROM "
                + DatabaseCredentials.SALES_TABLE
                + " WHERE date BETWEEN " + "'" + fromDate + "'" + " AND "
                + "'" + toDate + "'");
        
        for (int j = 0; j < selectedDatesData.size(); j++) {
            double labour_amt_discount = 0.0;
            Double netwt = 0.0,
                    diamondwt = 0.0,
                    gst_amount = 0.0,
                    netamount = 0.0;
            double taxableamount = 0,
                    labour = 0;
            int qty = 0;
            Integer bill = (Integer) selectedDatesData.get(j).get(1);

            int i = j;
            while (i < selectedDatesData.size()
                    && Objects.equals(bill, (Integer) selectedDatesData.get(i).get(1))) {

                if (selectedDatesData.get(i).get(3) != null) {
                    netwt += (Double) selectedDatesData.get(i).get(3);
                }

                if (selectedDatesData.get(i).get(4) != null) {
                    diamondwt += (Double) selectedDatesData.get(i).get(4);
                }

                if (selectedDatesData.get(i).get(7) != null) {
                    labour_amt_discount += (Double) selectedDatesData.get(i).get(7);
                }

                if (selectedDatesData.get(i).get(9) != null) {
                    gst_amount += (Double) selectedDatesData.get(i).get(9);
                }

                if (selectedDatesData.get(i).get(10) != null) {
                    netamount += (Double) selectedDatesData.get(i).get(10);
                }

                if (selectedDatesData.get(i).get(5) != null) {
                    taxableamount += Double.valueOf(selectedDatesData.get(i).get(5).toString());
                }

                if (selectedDatesData.get(i).get(6) != null) {
                    labour += Double.parseDouble(selectedDatesData.get(i).get(6).toString());
                }

                if (selectedDatesData.get(i).get(8) != null) {
                    try {
                        // Ensure qty is not null and contains only numeric characters
                        if (qty ==0) {
                            qty = 0;  // Default to 0 if qty is null or empty
                        }

                        // Retrieve and validate the value from selectedDatesData
                        String dataValue = (String) selectedDatesData.get(i).get(8);
                        if (dataValue == null || dataValue.trim().isEmpty()) {
                            dataValue = "0";  // Default to 0 if dataValue is null or empty
                        }

                        // Perform the integer parsing and addition
                        int qtyValue = qty;  // Convert qty to integer
                        int dataValueInt = Integer.parseInt(dataValue);  // Convert dataValue to integer

                        // Add the values and store the result back in qty
                        qty = (qtyValue + dataValueInt);

                    } catch (NumberFormatException e) {
                       
                        qty = 0;  // You can choose to assign a default value or handle it differently
                    }
                }

                i++;
            }
              String statement = "SELECT total FROM `exchange` WHERE bill = " + String.valueOf(bill) + ";";
            Connection con = connect();
            String total = "0";
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(statement);
                while (rs.next()) {

                    total = rs.getString("total");

                }

                con.close();
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ExChange.class.getName()).log(Level.SEVERE, "Failed", ex);
            }
                salesListTableModel.addRow(new Object[]{
                    selectedDatesData.get(j).get(0),
                    selectedDatesData.get(j).get(1),
                    selectedDatesData.get(j).get(2),
                    netwt,
                    diamondwt,
                    String.format("%.2f", taxableamount),
                    labour_amt_discount,
                    (int)qty,
                    gst_amount,
                    (netamount-Double.parseDouble(total))
                });

                j = i - 1;
        }
        System.out.println("167");
     //   fillgrandtotal();
     
        System.out.println("169");
    }

    public static void populateSalesListTable() {
        salesListTableModel.setRowCount(0);

        List<List<Object>> salesItems;

        if (DBController.isDatabaseConnected()) {

            selectedDatesData = DBController.getDataFromTable("SELECT date,"
                    + "bill, partyname,netwt, diamondwt,"
                    + " taxable_amount,labour,labour_amt_discount,qty,gstamt,netamount FROM "
                    + DatabaseCredentials.SALES_BILL_TABLE
                    + " WHERE date=" + "'" + currentDate + "'");

            for (int j = 0; j < selectedDatesData.size(); j++) {
                Float labour_amt_discount = 0.0f;
                Double netwt = 0.0,
                        diamondwt = 0.0,
                        gst_amount = 0.0,
                        netamount = 0.0;
                Long taxableamount = 0L,
                        labour = 0L;
                int qty = 0;
                Integer bill = (Integer) selectedDatesData.get(j).get(1);

                int i = j;
                while (i < selectedDatesData.size()
                        && Objects.equals(bill, (Integer) selectedDatesData.get(i).get(1))) {

                    if (selectedDatesData.get(i).get(3) != null) {
                        netwt += (Double) selectedDatesData.get(i).get(3);
                    }

                    if (selectedDatesData.get(i).get(4) != null) {
                        diamondwt += (Double) selectedDatesData.get(i).get(4);
                    }

                    if (selectedDatesData.get(i).get(7) != null) {
                        labour_amt_discount += (Float) selectedDatesData.get(i).get(7);
                    }

                    if (selectedDatesData.get(i).get(9) != null) {
                        gst_amount += (Double) selectedDatesData.get(i).get(9);
                    }

                    if (selectedDatesData.get(i).get(10) != null) {
                        netamount += (Double) selectedDatesData.get(i).get(10);
                    }

                    if (selectedDatesData.get(i).get(5) != null) {
                        taxableamount += (Long) selectedDatesData.get(i).get(5);
                    }

                    if (selectedDatesData.get(i).get(6) != null) {
                        labour += (Long) selectedDatesData.get(i).get(6);
                    }

                    if (selectedDatesData.get(i).get(8) != null) {
                        qty = (qty + Integer.parseInt((String) selectedDatesData.get(i).get(8)));
                    }

                    i++;
                }
                salesListTableModel.addRow(new Object[]{
                    selectedDatesData.get(j).get(0),
                    selectedDatesData.get(j).get(1),
                    selectedDatesData.get(j).get(2),
                    netwt,
                    diamondwt,
                    taxableamount,
                    labour,
                    labour_amt_discount,
                    qty,
                    gst_amount,
                    netamount
                });

                j = i - 1;
            }
        } else {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            selectedDatesData = DBController.getDataFromTable("SELECT date,"
                    + "bill, partyname, netwt, diamondwt,"
                    + " taxable_amount,labour,labour_amt_discount,qty,gstamt,netamount FROM "
                    + DatabaseCredentials.SALES_BILL_TABLE
                    + " WHERE date=" + "'" + currentDate + "'");

            for (int j = 0; j < selectedDatesData.size(); j++) {
                Float labour_amt_discount = 0.0f;
                Double netwt = 0.0,
                        diamondwt = 0.0,
                        gst_amount = 0.0,
                        netamount = 0.0;
                Long taxableamount = 0L,
                        labour = 0L;
                int qty = 0;
                Integer bill = (Integer) selectedDatesData.get(j).get(1);

                int i = j;
                while (i < selectedDatesData.size()
                        && bill == (Integer) selectedDatesData.get(i).get(1)) {

                    if (selectedDatesData.get(i).get(3) != null) {
                        netwt += (Double) selectedDatesData.get(i).get(3);
                    }

                    if (selectedDatesData.get(i).get(4) != null) {
                        diamondwt += (Double) selectedDatesData.get(i).get(4);
                    }

                    if (selectedDatesData.get(i).get(7) != null) {
                        labour_amt_discount += (Float) selectedDatesData.get(i).get(7);
                    }

                    if (selectedDatesData.get(i).get(9) != null) {
                        gst_amount += (Double) selectedDatesData.get(i).get(9);
                    }

                    if (selectedDatesData.get(i).get(10) != null) {
                        netamount += (Double) selectedDatesData.get(i).get(10);
                    }

                    if (selectedDatesData.get(i).get(5) != null) {
                        taxableamount += (Long) selectedDatesData.get(i).get(5);
                    }

                    if (selectedDatesData.get(i).get(6) != null) {
                        labour += (Long) selectedDatesData.get(i).get(6);
                    }

                    if (selectedDatesData.get(i).get(8) != null) {
                        qty = ((qty) + Integer.parseInt((String) selectedDatesData.get(i).get(8)));
                    }

                    i++;
                }
                salesListTableModel.addRow(new Object[]{
                    selectedDatesData.get(j).get(0),
                    selectedDatesData.get(j).get(1),
                    selectedDatesData.get(j).get(2),
                    netwt,
                    diamondwt,
                    taxableamount,
                    labour,
                    labour_amt_discount,
                    (int)qty,
                    gst_amount,
                    netamount
                });

                j = i - 1;
            }

        }
        System.out.println("any1");
      //  fillgrandtotal();
        System.out.println("any");
    }

    public static void fillgrandtotal() {
        double netwt = 0.0, totalDis = 0.0, netAmt = 0, BasicAmt = 0, taxAmt = 0, billAmt = 0, lbrAmt = 0;
        int txtqty = 0;
        int j = tblSalesList.getRowCount();
        txtRecords.setText(Integer.toString(j));
        txtBillNo.setText(Integer.toString(j));
        //txtNetWt.setText(Integer.toString(salesListTableModel.getRowCount()));
        
        for (int i = 0; i < j; i++) {
            netAmt += Double.parseDouble(tblSalesList.getValueAt(i, 9).toString());
//            lbrAmt += 
            totalDis += Double.parseDouble(tblSalesList.getValueAt(i, 6).toString());
            BasicAmt += Double.parseDouble(tblSalesList.getValueAt(i, 5).toString());
            taxAmt += Double.parseDouble(tblSalesList.getValueAt(i, 8).toString());
            billAmt = lbrAmt + BasicAmt;
            txtqty += Double.parseDouble(tblSalesList.getValueAt(i, 7).toString());
            netwt += Double.parseDouble(tblSalesList.getValueAt(i, 3).toString());
        }
      // column numbering issue
        txtNetWt.setText(Integer.toString(j));
       // txtNetWt.setText("8.0");
        txtDiscount.setText(Double.toString(totalDis));
        txtTotalQty.setText(Integer.toString(txtqty));
        txtGrossWt.setText(Double.toString(netwt));
        txtBillAmt.setText(Double.toString(billAmt));
        txtBasicAmt.setText(Double.toString(BasicAmt));
        txtTaxableAmt.setText(Double.toString(BasicAmt));
        txtTaxAmt.setText(String.format("%.2f", taxAmt));
        txtNetAmt.setText(String.format("%.2f", netAmt));
        System.out.println(txtqty);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Exportbtn = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        closebtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSalesList = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtBillNo = new javax.swing.JTextField();
        txtRecords = new javax.swing.JTextField();
        txtTotalQty = new javax.swing.JTextField();
        txtGrossWt = new javax.swing.JTextField();
        txtNetWt = new javax.swing.JTextField();
        txtBillAmt = new javax.swing.JTextField();
        txtBasicAmt = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtTaxableAmt = new javax.swing.JTextField();
        txtTaxAmt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtNetAmt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setDividerSize(0);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("SALE REGISTER");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Exportbtn.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Exportbtn.setText("Export");
        Exportbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportbtnActionPerformed(evt);
            }
        });

        btnOk.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("FROM");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("TO");

        closebtn.setBackground(new java.awt.Color(255, 0, 0));
        closebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        closebtn.setText("Close");
        closebtn.setMaximumSize(new java.awt.Dimension(111, 57));
        closebtn.setMinimumSize(new java.awt.Dimension(111, 57));
        closebtn.setPreferredSize(new java.awt.Dimension(70, 23));
        closebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(Exportbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Exportbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(57, 68, 76));

        tblSalesList.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblSalesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Bill No.", "Name", "Net Wt", "Dimond Wt", "Basic AMT.", "Discount", "Quantity", "GST", "Net Amt."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSalesList.setMaximumSize(new java.awt.Dimension(2147483647, 500));
        tblSalesList.setRowHeight(26);
        tblSalesList.getTableHeader().setReorderingAllowed(false);
        tblSalesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalesListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSalesList);
        JTableHeader header = tblSalesList.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("No of Bills");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Records");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setText("Tot Qty.");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Gross Wt.");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setText("Net Wt.");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setText("Tot Bill Amt.");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setText("Basic Amt.");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setText("Discount");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setText("Taxable Amt.");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("Tax Amt.");

        txtBillNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillNoActionPerformed(evt);
            }
        });

        txtRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRecordsActionPerformed(evt);
            }
        });

        txtTotalQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalQtyActionPerformed(evt);
            }
        });

        txtNetWt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNetWtActionPerformed(evt);
            }
        });

        txtBillAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillAmtActionPerformed(evt);
            }
        });

        txtBasicAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBasicAmtActionPerformed(evt);
            }
        });

        txtDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountActionPerformed(evt);
            }
        });

        txtTaxAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTaxAmtActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setText("Net Amt.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalQty, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtGrossWt, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNetWt, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBillAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(txtBasicAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12)
                            .addComponent(txtTaxableAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(txtTaxAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtNetAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBillNo)
                    .addComponent(txtRecords)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotalQty)
                        .addComponent(txtGrossWt)
                        .addComponent(txtNetWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBillAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBasicAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTaxableAmt)
                        .addComponent(txtTaxAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNetAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public String paths = "";
    private void ExportbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportbtnActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(this);
//        File saveFile = JFileChooser.getSelectedFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String date = dateFormat.format(new Date());

        try {
            File f = fileChooser.getSelectedFile();
            String path = f.getAbsolutePath();
            path = path + "_" + date + ".xlsx";
            paths = path;
        } catch (Exception e) {
            System.out.println(e);
        }
        exportToExcel();
    }//GEN-LAST:event_ExportbtnActionPerformed

    public void exportToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();
        DefaultTableModel model = (DefaultTableModel) tblSalesList.getModel();

        TreeMap<String, Object[]> map = new TreeMap<>();

        map.put("0", new Object[]{model.getColumnName(0), model.getColumnName(1), model.getColumnName(2), model.getColumnName(3),
            model.getColumnName(4), model.getColumnName(5), model.getColumnName(6), model.getColumnName(7), model.getColumnName(8),
            model.getColumnName(9), model.getColumnName(10)});

        for (int i = 1; i < model.getRowCount(); i++) {
            map.put(Integer.toString(i), new Object[]{model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2),
                model.getValueAt(i, 3), model.getValueAt(i, 4), model.getValueAt(i, 5), model.getValueAt(i, 6), model.getValueAt(i, 7),
                model.getValueAt(i, 8), model.getValueAt(i, 9), model.getValueAt(i, 10),});
        }

        Set<String> id = map.keySet();

        XSSFRow fRow;

        int rowId = 0;
        for (String key : id) {
            fRow = ws.createRow(rowId++);
            Object[] value = map.get(key);
            int cellId = 0;

            for (Object object : value) {
                XSSFCell cell = fRow.createCell(cellId++);
                cell.setCellValue(object.toString());
            }

            try ( FileOutputStream fos = new FileOutputStream(new File(paths));) {

                wb.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "file exported succesfully:" + paths);

    }
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        System.out.println("works");
        try{
        if (UtilityMethods.hasDateBeenPicked(dateFrom)
                && UtilityMethods.hasDateBeenPicked(dateTo)) {

            populateSalesListTable(dateFormat.format(dateFrom.getDate()),
                    dateFormat.format(dateTo.getDate()));
            System.out.println("779");
        }
        System.out.println("781");
        fillgrandtotal();
        System.out.println("782");
        }catch(Exception e){
            Logger.getLogger(SaleRegisterScreen.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void closebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebtnActionPerformed
        // TODO add your handling code here:
                DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        dispose();
    }//GEN-LAST:event_closebtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Data");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                pf.setOrientation(PageFormat.LANDSCAPE);
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.55, 1.0);

                jPanel2.print(g2);

                return Printable.PAGE_EXISTS;

            }
        });
        boolean ok = job.printDialog();
        if (ok) {
            try {

                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtTaxAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTaxAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxAmtActionPerformed

    private void txtBillAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBillAmtActionPerformed

    private void txtNetWtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNetWtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNetWtActionPerformed

    private void txtTotalQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalQtyActionPerformed

    private void txtRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRecordsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRecordsActionPerformed

    private void tblSalesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesListMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            DefaultTableModel m = (DefaultTableModel) tblSalesList.getModel();
            int row = tblSalesList.getSelectedRow();
            String recp = m.getValueAt(row, 1).toString();
            LoginPageRedesigned.staticdashboared.sc = new SaleScreen();

            try {
                LoginPageRedesigned.staticdashboared.sc.saleRegisterRedirect(Integer.parseInt(recp));
            } catch (Exception ex) {
                Logger.getLogger(SaleRegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            LoginPageRedesigned.staticdashboared.sc.setVisible(true);
            LoginPageRedesigned.staticdashboared.sc.closebtn.setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_tblSalesListMouseClicked

    private void txtBasicAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBasicAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBasicAmtActionPerformed

    private void txtDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountActionPerformed

    private void txtBillNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBillNoActionPerformed
 private final  List<Object> columnNames = new ArrayList<>();
    private final  List<Object> data = new ArrayList<>();
    private void updatedueamountminus(String partyname ,String netamt){
      data.clear();
        columnNames.clear();
//        JOptionPane.showMessageDialog(this,outstandingAmt);
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
        }
//        String partyname = (String) soldItemsForCurrentBill.get(soldItemsForCurrentBill.size() - 1).get("partyname");
        String query = "SELECT dueamt FROM " + DatabaseCredentials.ACCOUNT_TABLE
                + " WHERE accountname = '" + partyname + "';";
        List<List<Object>> res = DBController.getDataFromTable(query);
        double dueamount = 0;
        for (List<Object> list : res) {
            for (Object l : list) {
                dueamount += Double.parseDouble(l.toString());
            }
        }

        dueamount -= Float.parseFloat(netamt);

        data.clear();
        columnNames.clear();
        columnNames.add("dueamt");
        data.add(dueamount);
        DBController.updateTableData(DatabaseCredentials.ACCOUNT_TABLE, data, columnNames,
                "accountname", partyname);
 }
    ArrayList<String> itemname = new ArrayList<>();
    ArrayList<String> tagno=new ArrayList<>();
    private void deleteBill(int billnumber ,String netamount) {

        Connection con;
        try {
            con =DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select itemname,tagno,terms,partyname from sales where bill=" + billnumber + "";
            ResultSet re = stmt.executeQuery(sql);
            tagno.removeAll(tagno);
            String term="Cash";
            String parrty="test";
            while (re.next()) {
                itemname.add(re.getString("itemname"));
                tagno.add(re.getString("tagno"));
                term=re.getString("terms");
                parrty=re.getString("partyname");
//                 JOptionPane.showMessageDialog(this, itemname);
            }
            if(term.trim().equalsIgnoreCase("Credit")){
                updatedueamountminus(parrty,netamount);
            }
            con.close();
            re.close();
            RegainItemPeice();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        try {
            con = DBConnect.connect();
            Statement st = con.createStatement();
            st.execute("delete from sales where bill=" + billnumber + ";");
            JOptionPane.showMessageDialog(this, "Bill Deleted Successfully!");
            populateSalesListTable(dateFormat.format(dateFrom.getDate()),
                    dateFormat.format(dateTo.getDate()));
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(SaleRegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void RegainItemPeice() {
        Connection con;
//        int solditem = 0;
//        DefaultTableModel tmodel = (DefaultTableModel) tblSalesList.getModel();
//        try {
//            con = DriverManager.getConnection(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
//                    DatabaseCredentials.DB_PASSWORD);
//            Statement stmt = con.createStatement();
//            String sql = "select * from entryitem where itemname='"+ itemname+"' and tagno='"+tagno+"'";
//            ResultSet re = stmt.executeQuery(sql);
//            while (re.next()) {
//                solditem =re.getInt("item_sold");
////                JOptionPane.showMessageDialog(this, solditem);
//            }
//            con.close();
//            re.close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, e);
//        }
        try {
            con = DBConnect.connect();
            Statement st = con.createStatement();
            for(int i=0;i<tagno.size();i++){
              int result=st.executeUpdate("update entryitem set item_sold = 0 where itemname='"+ itemname.get(i) +"' and tagno='"+tagno.get(i)+"'");  
//              JOptionPane.showMessageDialog(this, "deleting tag "+tagno.get(i)+" with result "+result);
            }
           
//         Logger.getLogger(SaleRegisterScreen.class.getName()).log(Level.SEVERE, null, (byte)(solditem - Integer.parseInt(tmodel.getValueAt(tblSalesList.getSelectedRow(), 7).toString())));
        
        } catch (SQLException ex) {
            Logger.getLogger(SaleRegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Exportbtn;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton closebtn;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSplitPane jSplitPane2;
    private static javax.swing.JTable tblSalesList;
    private static javax.swing.JTextField txtBasicAmt;
    private static javax.swing.JTextField txtBillAmt;
    private static javax.swing.JTextField txtBillNo;
    private static javax.swing.JTextField txtDiscount;
    private static javax.swing.JTextField txtGrossWt;
    private static javax.swing.JTextField txtNetAmt;
    private static javax.swing.JTextField txtNetWt;
    private static javax.swing.JTextField txtRecords;
    private static javax.swing.JTextField txtTaxAmt;
    private static javax.swing.JTextField txtTaxableAmt;
    private static javax.swing.JTextField txtTotalQty;
    // End of variables declaration//GEN-END:variables
}
