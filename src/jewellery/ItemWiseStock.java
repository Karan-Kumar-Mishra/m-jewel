/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
 * @author ansar
 */
public class ItemWiseStock extends javax.swing.JFrame {
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DefaultTableModel model;
    private static List<List<Object>> selectedDatesData, selectedDataDetails;
    private static List<List<Object>> selectedDatesData1;


    
   // private JTable jTable2;
   // private JComboBox<String> jComboBox1;
    
   

    public ItemWiseStock() {
        initComponents();
        updateCombo();
        ShowDataInTable(null, null, null); // Show all data initially

        
    }
    

    private void customizeTable() {
        jTable2.getTableHeader().setBackground(Color.BLACK);
        jTable2.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 18));
        jTable2.getTableHeader().setForeground(Color.WHITE);
        ((DefaultTableCellRenderer) jTable2.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER); // header to center

        DefaultTableCellRenderer rendar = new DefaultTableCellRenderer();
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(rendar);
        }
    }
    
    private void updateCombo() {
        String sql = "SELECT itemname FROM entryitem";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnect.connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            jComboBox1.removeAllItems();
            
            
            Set<String> itemNames = new HashSet<>(); // Use a Set to ensure uniqueness
        while (rs.next()) {
            itemNames.add(rs.getString("itemname"));
        }

        for (String itemName : itemNames) {
            jComboBox1.addItem(itemName);
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

     private void ShowDataInTable(String selectedItem, Date fromDate, Date toDate) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        try {
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                        DatabaseCredentials.DB_PASSWORD);
            }

            String query;
            
            
                query = "SELECT DISTINCT(itemname) FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                        + " WHERE itemname LIKE '" + selectedItem + "%'";
            

            List<List<Object>> items = DBController.getDataFromTable(query);
            for (List<Object> item : items) {
                if (item.get(0) != null) {
                    String itemname = item.get(0).toString();
                    fillStockTable(itemname, model, fromDate, toDate);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ItemWiseStock.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void fillStockTable(String itemname, DefaultTableModel model, Date fromDate, Date toDate) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
       
        
        String query= "";
        try {
            String from = fromDate != null ? dateFormat.format(fromDate) : "1970-01-01";
            String to = toDate != null ? dateFormat.format(toDate) : getCurrentDate("yyyy-MM-dd");
            selectedDatesData = DBController.getDataFromTable("SELECT DISTINCT(bill) FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                + " WHERE date BETWEEN " + "'" + from + "'" + " AND "
                + "'" + to + "'");
            
           

            selectedDatesData.forEach((item) -> {
                Integer bill = Integer.valueOf(item.get(0).toString());
                selectedDataDetails = DBController.getDataFromTable("SELECT date,"
                        + " itemname, sum(qty), sum(Cast(grosswt as decimal(10,2)))"
                        + " FROM "
                        + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                        + " WHERE bill = '" + bill+"' And itemname='"+itemname+"'"
                        + "GROUP BY date, itemname");

                selectedDataDetails.forEach((data) -> {
    //                Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE,null,selectedDataDetails);
                    model.addRow(new Object[]{
                        data.get(0), // date
                        "Purchase",
                        bill,
                        data.get(1), // itemname
                        data.get(2), // qty purchase
                        "0", // qty sale
                        data.get(3), //  wt. in
                        "0", // wt. out

                    });

                });
            });
            
             selectedDatesData1 = DBController.getDataFromTable("SELECT date,"
                + "bill, itemname, grosswt,"
                + " qty FROM "
                + DatabaseCredentials.SALES_TABLE
                + " WHERE itemname='"+itemname+"' AND date BETWEEN " + "'" + from + "'" + " AND "
                + "'" + to + "'");
            
            selectedDatesData1.forEach((data) ->{
                model.addRow(new Object[]{
                    data.get(0), // date
                    "Sale",
                    data.get(1),
                    data.get(2), // itemname
                    "0", // qty purchase
                    data.get(4), // qty sale
                    "0", //  wt. in
                    data.get(3), // wt. out

                });
                
            });
            
           
        } catch (Exception e) {
            Logger.getLogger(ItemWiseStock.class.getName()).log(Level.SEVERE, null, e);
        }
        
        double totalgwtp = 0.0;    //total gross wt purchasse
        int totalqtyp = 0;          //total qt purchase
        double totalgwts = 0.0;     //total gross wt sale
        int totalqtys = 0;          //total qt sale
        int countbill=0;
        
        try {
            String from = fromDate != null ? dateFormat.format(fromDate) : "1970-01-01";
            String to = toDate != null ? dateFormat.format(toDate) : getCurrentDate("yyyy-MM-dd");
            
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query1 = "select sum(grosswt), sum(qty), "
                    + " from " + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " where itemname='"+itemname+"' AND date between "
                    + "'" + from + "' and '" + to + "';";
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                
                if (null != rs.getObject("sum(grosswt)")) {
                    totalgwtp += rs.getDouble("sum(grosswt)");
                }
               
                if (null != rs.getObject("sum(qty)")) {
                    totalqtyp += rs.getInt("sum(qty)");
                }
            }
            
            
            String query2 = "SELECT SUM(CAST(grosswt AS DECIMAL(10,2))) as grosswt, SUM(CAST(qty AS DECIMAL(10,2))) as qty " +
                "FROM " + DatabaseCredentials.SALES_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";
            


            
            ResultSet rs1 = st.executeQuery(query2);
            while (rs1.next()) {
                
                if (null != rs1.getObject("grosswt")) {
                    totalgwts += rs1.getDouble("grosswt");
                }
               
                if (null != rs1.getObject("qty")) {
                    totalqtys += rs1.getInt("qty");
                }
            }
            int totalCountSales = 0;
            int totalCountPurchases = 0;
            
            String query3 = "SELECT COUNT(DISTINCT(bill)) " +
                "FROM " + DatabaseCredentials.SALES_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";
            
            ResultSet rs3 = st.executeQuery(query3); 
            if (rs3.next()) {
                totalCountSales = rs3.getInt(1); // Get count from query3
            }
            String query4 = "SELECT COUNT(DISTINCT(bill)) " +
                "FROM " + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";
            ResultSet rs4 = st.executeQuery(query4); 
            if (rs4.next()) {
                totalCountPurchases = rs4.getInt(1); // Get count from query3
            }
            countbill=totalCountSales+totalCountPurchases;
            con.close();
            st.close();
            rs.close();
            rs1.close();
            rs3.close();
            rs4.close();
            
            
        } catch (SQLException e) {
            Logger.getLogger(PurchaseRegisterScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        totalBilltxt.setText(String.valueOf(countbill));
        tQtyPrchaseTxt.setText(String.valueOf(totalqtyp));
        tQtySaleText.setText(String.valueOf(totalqtys));
        tWtInTxt.setText(String.valueOf(totalgwtp));
        tWtOutTxt.setText(String.valueOf(totalgwts));
        
        countbill=0;

    }


    private String getCurrentDate(String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.now();
        return dateTimeFormatter.format(localDateTime);
    }
    
    
    private void printx(String itemname, Date fromDate, Date toDate) {
        Connection connection = null;
        try {
            
            connection = DBConnect.connect();
            String query= "";
            
            List<Map<String, Object>> itemDataList = new ArrayList<>();
            HashMap<String, Object> parameters = new HashMap<>();
            
            String from = fromDate != null ? dateFormat.format(fromDate) : "1970-01-01";
            String to = toDate != null ? dateFormat.format(toDate) : getCurrentDate("yyyy-MM-dd");
            selectedDatesData = DBController.getDataFromTable("SELECT DISTINCT(bill) FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                + " WHERE date BETWEEN " + "'" + from + "'" + " AND "
                + "'" + to + "'");
            
           

            selectedDatesData.forEach((item) -> {
                Integer bill = Integer.valueOf(item.get(0).toString());
                selectedDataDetails = DBController.getDataFromTable("SELECT date,"
                        + " itemname, sum(qty), sum(Cast(grosswt as decimal(10,2)))"
                        + " FROM "
                        + DatabaseCredentials.PURCHASE_HISTORY_TABLE
                        + " WHERE bill = '" + bill+"' And itemname='"+itemname+"'"
                        + "GROUP BY date, itemname");

                selectedDataDetails.forEach((data) -> {
                    Map<String, Object> itemParameters = new HashMap<>();
                    itemParameters.put("date", dateFormat.format(data.get(0)));
                    itemParameters.put("type", "Purchase");
                    itemParameters.put("bill", bill);
                    itemParameters.put("itemname", data.get(1));
                    itemParameters.put("qtyPurchase", data.get(2).toString());
                    itemParameters.put("qtySale", "0");
                    itemParameters.put("wtin", data.get(3).toString());
                    itemParameters.put("wtout", "0");

                    itemDataList.add(itemParameters);
                   

                });
            });
            
             selectedDatesData1 = DBController.getDataFromTable("SELECT date,"
                + "bill, itemname, grosswt,"
                + " qty FROM "
                + DatabaseCredentials.SALES_TABLE
                + " WHERE itemname='"+itemname+"' AND date BETWEEN " + "'" + from + "'" + " AND "
                + "'" + to + "'");
            
            selectedDatesData1.forEach((data) ->{
                Map<String, Object> itemParameters = new HashMap<>();
                itemParameters.put("date", dateFormat.format(data.get(0)));
                itemParameters.put("type", "Sale");
                itemParameters.put("bill", data.get(1));
                itemParameters.put("itemname", data.get(2));
                itemParameters.put("qtyPurchase", "0");
                itemParameters.put("qtySale", data.get(4).toString());
                itemParameters.put("wtin", "0");
                itemParameters.put("wtout", data.get(3).toString());
                itemDataList.add(itemParameters);
            });
            
            
            double totalgwtp = 0.0;    //total gross wt purchasse
            int totalqtyp = 0;          //total qt purchase
            double totalgwts = 0.0;     //total gross wt sale
            int totalqtys = 0;          //total qt sale
            int countbill=0;

            

            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query1 = "select sum(grosswt), sum(qty), "
                    + " from " + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " where itemname='"+itemname+"' AND date between "
                    + "'" + from + "' and '" + to + "';";
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {

                if (null != rs.getObject("sum(grosswt)")) {
                    totalgwtp += rs.getDouble("sum(grosswt)");
                }

                if (null != rs.getObject("sum(qty)")) {
                    totalqtyp += rs.getInt("sum(qty)");
                }
            }


            String query2 = "SELECT SUM(CAST(grosswt AS DECIMAL(10,2))) as grosswt, SUM(CAST(qty AS DECIMAL(10,2))) as qty " +
                "FROM " + DatabaseCredentials.SALES_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";




            ResultSet rs1 = st.executeQuery(query2);
            while (rs1.next()) {

                if (null != rs1.getObject("grosswt")) {
                    totalgwts += rs1.getDouble("grosswt");
                }

                if (null != rs1.getObject("qty")) {
                    totalqtys += rs1.getInt("qty");
                }
            }
            int totalCountSales = 0;
            int totalCountPurchases = 0;

            String query3 = "SELECT COUNT(DISTINCT(bill)) " +
                "FROM " + DatabaseCredentials.SALES_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";

            ResultSet rs3 = st.executeQuery(query3); 
            if (rs3.next()) {
                totalCountSales = rs3.getInt(1); // Get count from query3
            }
            String query4 = "SELECT COUNT(DISTINCT(bill)) " +
                "FROM " + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " " +
                "WHERE itemname = '" + itemname + "' AND date BETWEEN '" + from + "' AND '" + to + "'";
            ResultSet rs4 = st.executeQuery(query4); 
            if (rs4.next()) {
                totalCountPurchases = rs4.getInt(1); // Get count from query3
            }
            countbill=totalCountSales+totalCountPurchases;
            con.close();
            st.close();
            rs.close();
            rs1.close();
            rs3.close();
            rs4.close();
           
            parameters.put("tbill", countbill);
            parameters.put("tqp", totalqtyp);
            parameters.put("tqs", totalqtys);
            parameters.put("tgwi", totalgwtp);
            parameters.put("tgwo", totalgwts);
            
            
            


            
            
            parameters.put("itemDataList", itemDataList);

            File reportFile = new File("jasper_reports" + File.separator + "itemwise_stock_report.jrxml");
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

        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            Logger.getLogger(ItemsStockScreen.class.getName()).log(Level.SEVERE, null, ex);

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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        from = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        to = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        totalBilltxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tQtyPrchaseTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tQtySaleText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tWtInTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tWtOutTxt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(57, 68, 76));

        jScrollPane2.setBackground(new java.awt.Color(153, 153, 153));
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Type", "Bill No.", "Item Name", "Purchase Qty", "Sale Qty", "Wt. IN", "Wt. OUt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
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
        JTableHeader header = jTable2.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));

        jPanel2.setBackground(new java.awt.Color(57, 68, 76));
        jPanel2.setForeground(new java.awt.Color(238, 188, 81));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Item Wise");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("Stock");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        from.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fromFocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("From");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("To");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Ok");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton2.setText("Close");
        jButton2.setPreferredSize(new java.awt.Dimension(70, 23));
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

        jButton3.setBackground(new java.awt.Color(0, 255, 6));
        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton3.setText("Refresh");
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

        jButton4.setText("Print");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jButton1)
                        .addGap(30, 30, 30)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jButton3)
                        .addGap(41, 41, 41)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(178, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(24, 24, 24))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))))))
        );

        totalBilltxt.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        totalBilltxt.setSize(new java.awt.Dimension(70, 30));
        totalBilltxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBilltxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("Total Bill:");
        jLabel4.setSize(new java.awt.Dimension(42, 25));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setText("Total Qty Purchase:");

        tQtyPrchaseTxt.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tQtyPrchaseTxt.setSize(new java.awt.Dimension(70, 30));
        tQtyPrchaseTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tQtyPrchaseTxtActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel6.setText("Total Qty Sale:");

        tQtySaleText.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tQtySaleText.setSize(new java.awt.Dimension(70, 30));
        tQtySaleText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tQtySaleTextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel8.setText("Total Wt. In:");

        tWtInTxt.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tWtInTxt.setSize(new java.awt.Dimension(70, 30));
        tWtInTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tWtInTxtActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel9.setText("Total Wt. Out:");

        tWtOutTxt.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tWtOutTxt.setSize(new java.awt.Dimension(70, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalBilltxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tQtyPrchaseTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tQtySaleText, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tWtInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tWtOutTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 21, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalBilltxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(tQtyPrchaseTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tQtySaleText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tWtInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(tWtOutTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

    }//GEN-LAST:event_jTable2MouseClicked

    private void fromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fromFocusLost

    }//GEN-LAST:event_fromFocusLost

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String selectedItem = (String) jComboBox1.getSelectedItem();

        Date fromDate = from.getDate();
        Date toDate = to.getDate();
        ShowDataInTable(selectedItem, fromDate, toDate);
        
        
        
        
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
       // dispose();
    }//GEN-LAST:event_jButton2MouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
    
        
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
//    model = (DefaultTableModel) jTable2.getModel();
//        
//        customizeTable();
//    
        
    }//GEN-LAST:event_jButton3MouseClicked

    private void totalBilltxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBilltxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBilltxtActionPerformed

    private void tQtyPrchaseTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tQtyPrchaseTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tQtyPrchaseTxtActionPerformed

    private void tQtySaleTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tQtySaleTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tQtySaleTextActionPerformed

    private void tWtInTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tWtInTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tWtInTxtActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String selectedItem = (String) jComboBox1.getSelectedItem();
                
        Date fromDate = from.getDate();
        Date toDate = to.getDate();
        printx(selectedItem, fromDate, toDate);
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        String selectedItem = (String) jComboBox1.getSelectedItem();
                
        Date fromDate = from.getDate();
        Date toDate = to.getDate();
        ShowDataInTable(selectedItem, fromDate, toDate);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        String selectedItem = (String) jComboBox1.getSelectedItem();
                
        Date fromDate = from.getDate();
        Date toDate = to.getDate();
        printx(selectedItem, fromDate, toDate);
    }//GEN-LAST:event_jButton4MouseClicked


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
            java.util.logging.Logger.getLogger(ItemWiseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemWiseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemWiseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemWiseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemWiseStock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser from;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField tQtyPrchaseTxt;
    private javax.swing.JTextField tQtySaleText;
    private javax.swing.JTextField tWtInTxt;
    private javax.swing.JTextField tWtOutTxt;
    private com.toedter.calendar.JDateChooser to;
    private javax.swing.JTextField totalBilltxt;
    // End of variables declaration//GEN-END:variables
}
