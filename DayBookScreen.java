package jewellery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import static jewellery.SaleRegisterScreen.populateSalesListTable;
import static kabitalib.util.Utilitas.dateFormat;

/**
 *
 * @author ASUS
 */
public class DayBookScreen extends javax.swing.JFrame {

    private ImageIcon imageIcon;
    private static String currentDate;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private static DefaultTableModel m ;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static List<List<Object>> selectedDatesData;
    
    public DayBookScreen() {
        initComponents();
        
        m = (DefaultTableModel) jTable1.getModel();
        currentDate = getCurrentDate("yyyy-MM-dd");
        SwingUtilities.invokeLater(() -> {
            filter();
        });
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 18));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;

        jPanel1.setSize(w - 280, h - 125);
    }

    private void setImageOnJLabel(javax.swing.JLabel component
            , String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth() - 5, 
                        component.getHeight() - 5, Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        dateTo = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/DayBookImage.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("DAY BOOK");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 51));
        jLabel3.setText("FROM");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 51));
        jLabel4.setText("TO");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No.", "Party Name", "Item Name", "HUID", "Net Amount", "Date", "Remark"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        jToggleButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        
        
        btnClose = new JButton();
        btnClose.setBackground(new Color(204, 0, 0));
        btnClose.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		DashBoardScreen.tpScreensHolder.setSelectedIndex(0);
        	}
        });
        btnClose.setText("Close");
        btnClose.setFont(new Font("Times New Roman", Font.BOLD, 14));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(jLabel2)
        					.addGap(52)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
        						.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
        					.addGap(20)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
        						.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
        					.addGap(47)
        					.addComponent(jToggleButton1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
        					.addGap(127)
        					.addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
        					.addGap(0, 0, Short.MAX_VALUE))
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE))
        			.addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        					.addComponent(jLabel2))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        					.addGap(33))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel3)
        						.addComponent(jLabel4))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        						.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jToggleButton1))
        			.addGap(18)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        			.addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void filter3(String toDate, String fromDate){
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    String edate =  sdf.format(endDate);
//    Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(edate));
//        } catch (ParseException ex) {
//            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    c.add(Calendar.DATE, 1);
//    endDate = c.getTime();
//    
//    String sdate = sdf.format(startDate);
//    Calendar c1 = Calendar.getInstance();
//        try {
//            c1.setTime(sdf.parse(sdate));
//        } catch (ParseException ex) {
//            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    c1.add(Calendar.DATE,-1);
//    startDate = c1.getTime();
//    
//    
//    List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
//    filters.add( RowFilter.dateFilter(ComparisonType.AFTER, startDate) );
//    filters.add( RowFilter.dateFilter(ComparisonType.BEFORE, endDate) );
//   DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
//   TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel> (model);
//   jTable1.setRowSorter(tr);
//    RowFilter<Object, Object> rf = RowFilter.andFilter(filters);
//    tr.setRowFilter(rf);
    
    
    String status1="Sales entry";
    if(selectedDatesData != null && selectedDatesData.size() > 0) {
            selectedDatesData.clear();
        }
        
        m.setRowCount(0);
        
        if(!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
        
        selectedDatesData = DBController.getDataFromTable("SELECT partyname,"
                + "itemname, huid,net_amount, date FROM"
                + DatabaseCredentials.SALES_TABLE
                + " WHERE date BETWEEN " + "'" + fromDate + "'" + " AND " 
                + "'" + toDate + "'");
        int i=0;
        selectedDatesData.forEach((item) -> {
            m.addRow(new Object[]{
//                    i++,
                    item.get(0), // date
                    item.get(1), // billno
                    item.get(2), // partyname
//                    item.get(3), // itemname
//                    item.get(4), // huid
                    item.get(3), // netWt
                    item.get(4), // diamondWt
                    status1
                    
            });
            
        });
    
   }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        DefaultTableModel m = (DefaultTableModel)jTable1.getModel();
        int row = jTable1.getSelectedRow();
        String recp = m.getValueAt(row, 0).toString();
        Bill_History_Update ob=new Bill_History_Update();
        ob.bill_history_update(Integer.parseInt(recp));
        ob.setVisible(true);
        dispose();
    }//GEN-LAST:event_jTable1MouseClicked

    private String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();
        
        return dateTimeFormatter.format(localDateTime);
    }
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
//       filter3(dateFrom.getDate(),dateTo.getDate());
        
        if (DBController.isDatabaseConnected()) {
            if(UtilityMethods.hasDateBeenPicked(dateFrom) 
                    && UtilityMethods.hasDateBeenPicked(dateTo)) {
                
                System.out.println(dateFormat.format(dateFrom.getDate()));
                System.out.println(dateFormat.format(dateTo.getDate()));
                
                filter3(dateFormat.format(dateFrom.getDate()), 
                        dateFormat.format(dateTo.getDate()));
            }
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    private void filter()
    {
        DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
        m.setRowCount(0);
int i=0;
String status1="Sales entry";
String status2="Purchase entry";

try{
    Connection con = DBConnect.connect();
    Statement stmt = con.createStatement();
    Connection c = DBConnect.connect();
    Statement s = c.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM sales where date=''"+currentDate+"");
    while(rs.next()){
        String name = rs.getString("partyname");
        String itemname = rs.getString("itemname");
        String huid=rs.getString("huid");
        String amount = rs.getString("netamount");
         Date date = rs.getDate("date");
         
        m.addRow(new Object[]{++i,name,itemname,huid,amount,date,status1});
    
    }
    con.close();
    stmt.close();
    rs.close();
    
    ResultSet rs1 = s.executeQuery("SELECT * FROM purchasehistory where date = '"+currentDate+"';");
    while(rs1.next()){
        String name = rs1.getString("partyname");
        String itemname = rs1.getString("itemname");
        String huid=rs1.getString("huid");
        String amount = rs1.getString("net_amount");
        Date date = rs1.getDate("date");
        m.addRow(new Object[]{++i,name,itemname,huid,amount,date,status2});
    
    }
     DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for(int g=0;g<jTable1.getColumnCount();g++)
            {jTable1.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);}
   c.close();
    s.close();
    rs1.close();
}

catch(SQLException e){
    e.printStackTrace();
    }
//String status1="Sales entry";
//String status2="Purchase entry";
//
//
//    m.setRowCount(0);
//        
//        List<List<Object>> salesItems;
//        
//        if(DBController.isDatabaseConnected()) {
//            int i=0;
//            salesItems = DBController.getDataFromTable("SELECT partyname,"
//                + "itemname, huid,net_amount, date FROM"
//                + DatabaseCredentials.SALES_TABLE
//                + " WHERE date=" + "'" + currentDate + "'");
//            
//            salesItems.forEach((item) -> {
//                m.addRow(new Object[]{
////                    ++i, // date
//                    item.get(0), // billno
//                    item.get(1), // partyname
////                    item.get(3), // itemname
////                    item.get(4), // huid
//                    item.get(2), // netWt
//                    item.get(3), // diamondWt
//                    item.get(4), // BasicAmt
//                    status1
//                  
//                });
//            });
//        }
//        else {
//            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
//                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
//            salesItems = DBController.getDataFromTable("SELECT partyname,"
//                + "itemname, huid,net_amount, date FROM"
//                + DatabaseCredentials.SALES_TABLE
//                + " WHERE date=" + "'" + currentDate + "'");
//            
//            salesItems.forEach((item) -> {
//                m.addRow(new Object[]{
//                    item.get(0), // billno
//                    item.get(1), // partyname
////                    item.get(3), // itemname
////                    item.get(4), // huid
//                    item.get(2), // netWt
//                    item.get(3), // diamondWt
//                    item.get(4), // BasicAmt
//                    status1
//                });
//            });
//            
//        }
//}
    }
    // Variables declaration - do not modify                     
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private JButton btnClose;
    public static javax.swing.JTabbedPane tpScreensHolder;
}
