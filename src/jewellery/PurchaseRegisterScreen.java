package jewellery;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ASUS
 */
public class PurchaseRegisterScreen extends javax.swing.JFrame {

    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private static String currentDate;
    private static DefaultTableModel purchaseHistoryListTableModel;
    private static List<List<Object>> selectedDatesData, selectedDataDetails;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PurchaseRegisterScreen() {
        initComponents();

        purchaseHistoryListTableModel = (DefaultTableModel) tblPurchaseHistoryData.getModel();
        centerTableCells();
        currentDate = getCurrentDate("yyyy-MM-dd");

//        populatePurchasesHistoryListTable();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        int height = d.height;
        int width = d.width;

        jSplitPane2.setSize(width - 280, height - 125);
        purchaseHistoryListTableModel.setRowCount(0);
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblPurchaseHistoryData
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();

        return dateTimeFormatter.format(localDateTime);
    }

    public void populatePurchaseHistoryTable(String fromDate, String toDate) {
        if (selectedDatesData != null && selectedDatesData.size() > 0) {
            selectedDatesData.clear();
        }

        purchaseHistoryListTableModel.setRowCount(0);

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        selectedDatesData = DBController.getDataFromTable("SELECT DISTINCT(bill) FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                + " WHERE date BETWEEN " + "'" + fromDate + "'" + " AND "
                + "'" + toDate + "'");

        selectedDatesData.forEach((item) -> {
            Integer bill = Integer.valueOf(item.get(0).toString());
            selectedDataDetails = DBController.getDataFromTable("SELECT date,"
                    + " partyname, sum(qty), sum(Cast(grosswt as decimal(10,2))), sum(Cast(beedswt as decimal (10,2))),"
                    + " sum(cast(bankamt as decimal(10,2))), sum(CAST(net_amount AS DECIMAL(10,2))) FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                    + " WHERE bill = " + bill
                    + "GROUP BY date, partyname");

            selectedDataDetails.forEach(data -> {
//                Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE,null,selectedDataDetails);
                purchaseHistoryListTableModel.addRow(new Object[]{
                    data.get(0), // date
                    bill,
                    data.get(1), // partyname
                    data.get(2), // qty
                    data.get(3), // GrossWt
                    data.get(4), // BeedsWt
                    data.get(5), // BasicAmt
                    data.get(6) // NetAmount
                });

            });
        });
        try {
            double totalamt = 0.0, totaldis = 0.0, totalgwt = 0.0, totalbwt = 0.0;
            int totalqty = 0;
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "select sum(CAST(net_amount AS DECIMAL(10,2))) as sum, sum(discountamount), sum(beedswt), sum(grosswt), sum(qty)"
                    + " from " + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " where date between "
                    + "'" + fromDate + "' and '" + toDate + "';";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (null != rs.getObject("sum")) {
                    totalamt += rs.getDouble("sum");
                }
                if (null != rs.getObject("sum(discountamount)")) {
                    totaldis += rs.getDouble("sum(discountamount)");
                }
                if (null != rs.getObject("sum(grosswt)")) {
                    totalgwt += rs.getDouble("sum(grosswt)");
                }
                if (null != rs.getObject("sum(beedswt)")) {
                    totalbwt += rs.getDouble("sum(beedswt)");
                }
                if (null != rs.getObject("sum(qty)")) {
                    totalqty += rs.getInt("sum(qty)");
                }
            }
            con.close();
            st.close();
            rs.close();

            totalgwtfield.setText(String.valueOf(totalgwt));
            totalbwtfield.setText(String.valueOf(totalbwt));
            totaldisfield.setText(String.valueOf(totaldis));
            totalqtytxtfield.setText(String.valueOf(totalqty));
            netamtfield.setText(String.valueOf(totalamt));
        } catch (SQLException e) {
            Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void populatePurchasesHistoryListTable() {

        if (selectedDatesData != null && selectedDatesData.size() > 0) {
            selectedDatesData.clear();
        }

        purchaseHistoryListTableModel.setRowCount(0);

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        selectedDatesData = DBController.getDataFromTable("SELECT DISTINCT(bill) FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE);

        selectedDatesData.forEach((item) -> {
            Integer bill = Integer.valueOf(item.get(0).toString());
            selectedDataDetails = DBController.getDataFromTable("SELECT date,"
                    + " partyname, sum(qty), sum(grosswt), sum(beedswt),"
                    + " sum(bankamt), sum(CAST(net_amount as DECIMAL(10,2))) as nt FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE);

            selectedDataDetails.forEach(data -> {
                purchaseHistoryListTableModel.addRow(new Object[]{
                    data.get(0), // date
                    bill,
                    data.get(1), // partyname
                    data.get(2), // qty
                    data.get(3), // GrossWt
                    data.get(4), // BeedsWt
                    data.get(5), // BasicAmt
                    data.get(6) // NetAmount
                });

            });
        });
        try {
            double totalamt = 0.0, totaldis = 0.0, totalgwt = 0.0, totalbwt = 0.0;
            int totalqty = 0;
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "select sum(CAST(net_amount as DECIMAL(10,2))) as nt, sum(discountamount), sum(beedswt), sum(grosswt), sum(qty)"
                    + " from " + DatabaseCredentials.PURCHASE_HISTORY_TABLE;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (null != rs.getObject("nt")) {
                    totalamt += rs.getDouble("nt");
                }
                if (null != rs.getObject("sum(discountamount)")) {
                    totaldis += rs.getDouble("sum(discountamount)");
                }
                if (null != rs.getObject("sum(grosswt)")) {
                    totalgwt += rs.getDouble("sum(grosswt)");
                }
                if (null != rs.getObject("sum(beedswt)")) {
                    totalbwt += rs.getDouble("sum(beedswt)");
                }
                if (null != rs.getObject("sum(qty)")) {
                    totalqty += rs.getInt("sum(qty)");
                }
            }
            con.close();
            st.close();
            rs.close();

            totalgwtfield.setText(String.valueOf(totalgwt));
            totalbwtfield.setText(String.valueOf(totalbwt));
            totaldisfield.setText(String.valueOf(totaldis));
            totalqtytxtfield.setText(String.valueOf(totalqty));
            netamtfield.setText(String.valueOf(totalamt));
        } catch (SQLException e) {
            Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE, null, e);
        }

//        purchaseHistoryListTableModel.setRowCount(0);
//
//        List<List<Object>> purchaseItems;
//
//        if (DBController.isDatabaseConnected()) {
//            purchaseItems = DBController.getDataFromTable("SELECT date,"
//                    + "bill, partyname, itemname, qty, grosswt, beedswt, price,"
//                    + " bankamt, discountpercent, net_amount FROM "
//                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE
//                    + " WHERE date = " + "'" + currentDate + "'");
//
//            purchaseItems.forEach((data) -> {
//                purchaseHistoryListTableModel.addRow(new Object[]{
//                    data.get(0), // date
//                    data.get(1), // billno
//                    data.get(2), // partyname
//                    data.get(3), // itemname
//                    data.get(4), // qty
//                    data.get(5), // GrossWt
//                    data.get(6), // BeedsWt
//                    data.get(7), // rate
//                    data.get(8), // BasicAmt
//                    data.get(9), // Discount
//                    data.get(10) // NetAmount
//                });
//            });
//        } else {
//            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
//                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
//
//            purchaseItems = DBController.getDataFromTable("SELECT date,"
//                    + "bill, partyname, itemname, qty, grosswt, beedswt, price,"
//                    + " bankamt, discountpercent, net_amount FROM "
//                    + DatabaseCredentials.PURCHASE_BILL_TABLE
//                    + " WHERE date = " + "'" + currentDate + "'");
//
//            purchaseItems.forEach((data) -> {
//                purchaseHistoryListTableModel.addRow(new Object[]{
//                    data.get(0), // date
//                    data.get(1), // billno
//                    data.get(2), // partyname
//                    data.get(3), // itemname
//                    data.get(4), // qty
//                    data.get(5), // GrossWt
//                    data.get(6), // BeedsWt
//                    data.get(7), // rate
//                    data.get(8), // BasicAmt
//                    data.get(9), // Discount
//                    data.get(10) // NetAmount
//                });
//            });
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchaseHistoryData = new javax.swing.JTable();
        totaldisfield = new javax.swing.JTextField();
        totaldislabel = new javax.swing.JLabel();
        totalqtytxtfield = new javax.swing.JTextField();
        netamtfield = new javax.swing.JTextField();
        totalqtylabel = new javax.swing.JLabel();
        netamtlabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        totalgwtfield = new javax.swing.JTextField();
        totalbwtfield = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Purchase Register Screen");

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setDividerSize(0);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("PURCHASE REGISTER");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Export");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setText("Close");
        jButton3.setPreferredSize(new java.awt.Dimension(70, 23));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(75, 75, 75)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(213, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(57, 68, 76));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N

        tblPurchaseHistoryData.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblPurchaseHistoryData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Bill No.", "Party Name", "Qty.", "G.Wt", "B. Wt.", "Basic AMT.", "Net Amt."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPurchaseHistoryData.setMaximumSize(new java.awt.Dimension(2147483647, 500));
        tblPurchaseHistoryData.setRowHeight(26);
        tblPurchaseHistoryData.getTableHeader().setReorderingAllowed(false);
        tblPurchaseHistoryData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchaseHistoryDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPurchaseHistoryData);
        JTableHeader header = tblPurchaseHistoryData.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 990, 410));

        totaldisfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaldisfieldActionPerformed(evt);
            }
        });
        jPanel2.add(totaldisfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 470, 102, -1));

        totaldislabel.setForeground(new java.awt.Color(238, 188, 81));
        totaldislabel.setText("Total Discount");
        jPanel2.add(totaldislabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 470, 100, 27));
        jPanel2.add(totalqtytxtfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 470, 116, -1));

        netamtfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netamtfieldActionPerformed(evt);
            }
        });
        jPanel2.add(netamtfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 470, 97, -1));

        totalqtylabel.setForeground(new java.awt.Color(238, 188, 81));
        totalqtylabel.setText("Total Qty");
        jPanel2.add(totalqtylabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 470, 70, 27));

        netamtlabel.setForeground(new java.awt.Color(238, 188, 81));
        netamtlabel.setText("Net Amount");
        jPanel2.add(netamtlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 470, 90, 27));

        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Total B.Wt");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 70, 27));

        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Total G.Wt");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 470, 80, 27));
        jPanel2.add(totalgwtfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 470, 104, -1));
        jPanel2.add(totalbwtfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, 94, -1));

        jSplitPane2.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        if (UtilityMethods.hasDateBeenPicked(dateFrom)
                && UtilityMethods.hasDateBeenPicked(dateTo)) {

            populatePurchaseHistoryTable(dateFormat.format(dateFrom.getDate()),
                    dateFormat.format(dateTo.getDate()));

        }
    }//GEN-LAST:event_btnOkActionPerformed
    public String paths = "";
     private void printx() {
//        JOptionPane.showMessageDialog(this, "running");
        Map<String, Object> parameters = new HashMap<>();
        Connection connection = DBConnect.connect();
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(dateFrom.getDate());
             String date2 = sdf.format(dateTo.getDate());
            String query = "select * from purchasehistory where date between '" + date + "' and '"+date2+"'";
//            JOptionPane.showMessageDialog(this, query);
            InputStream input = new FileInputStream(new File("jasper_reports" + File.separator + "purchasehistory.jrxml"));
            JasperDesign design = JRXmlLoader.load(input);
            JRDesignQuery updateQuary = new JRDesignQuery();
            updateQuary.setText(query);
            design.setQuery(updateQuary);
            String realPath = System.getProperty("user.dir") + File.separator + "jasper_reports" + File.separator + "img" + File.separator;
            parameters.put("imagePath", realPath);
            JasperReport jreport = JasperCompileManager.compileReport(design);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, connection);
            JasperViewer.viewReport(jprint, false);
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "running");
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      printx();
    }//GEN-LAST:event_jButton1ActionPerformed
    public void exportToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();
        DefaultTableModel model = (DefaultTableModel) tblPurchaseHistoryData.getModel();

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
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
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
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:

        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        dispose();
    }//GEN-LAST:event_jButton3MouseClicked

    private void netamtfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netamtfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_netamtfieldActionPerformed

    private void totaldisfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totaldisfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totaldisfieldActionPerformed

    private void tblPurchaseHistoryDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseHistoryDataMouseClicked
        // TODO add your handling code here:
        try{
            if (evt.getClickCount() == 2) {
                DefaultTableModel m = (DefaultTableModel) tblPurchaseHistoryData.getModel();
                int row = tblPurchaseHistoryData.getSelectedRow();
                String recp = m.getValueAt(row, 1).toString();
                PurchaseScreen sc = new PurchaseScreen();

                sc.sale_Bill(Integer.parseInt(recp));
                sc.setVisible(true);

            }
        }catch(Exception e){
            Logger.getLogger(e.toString());
        }
    }//GEN-LAST:event_tblPurchaseHistoryDataMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
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
    private void deleteBill(int billnumber) {
        Connection con;
        try {
            con = DBConnect.connect();
            Statement st = con.createStatement();
            st.execute("delete from purchasehistory where bill='" + billnumber + "';");
            JOptionPane.showMessageDialog(this, "Bill deleted Successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextField netamtfield;
    private javax.swing.JLabel netamtlabel;
    private javax.swing.JTable tblPurchaseHistoryData;
    private javax.swing.JTextField totalbwtfield;
    private javax.swing.JTextField totaldisfield;
    private javax.swing.JLabel totaldislabel;
    private javax.swing.JTextField totalgwtfield;
    private javax.swing.JLabel totalqtylabel;
    private javax.swing.JTextField totalqtytxtfield;
    // End of variables declaration//GEN-END:variables
}
