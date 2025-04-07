package jewellery;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

/**
 *
 * @author ASUS
 */
public class GSTReport1B2CScreen extends javax.swing.JFrame {

    private ImageIcon imageIcon;
    
    public GSTReport1B2CScreen() {
        initComponents();
        
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 18));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;

        jPanel1.setSize(w - 280, h - 125);
        
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
    ResultSet rs = stmt.executeQuery("SELECT * FROM sales;");
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
    
    ResultSet rs1 = s.executeQuery("SELECT * FROM purchasehistory;");
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        dateTo = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("GSTR - 1");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 51));
        jLabel3.setText("FROM");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 51));
        jLabel4.setText("TO");

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Sr.No.", "Name", "Invoice No.", "Invoice Dt.", "Value", "POS", "Rate", "Taxable Amt."
        	}
        ) {
        	Class[] columnTypes = new Class[] {
        		Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
        	};
        	public Class getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        	boolean[] columnEditables = new boolean[] {
        		true, true, true, false, true, true, true, true
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jToggleButton1.setIcon(new ImageIcon(GSTReport1B2CScreen.class.getResource("/assets/icons8-ok-48.png"))); // NOI18N
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
        		        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        	}
        });
        btnClose.setText("Close");
        btnClose.setFont(new Font("Times New Roman", Font.BOLD, 14));
        
        JLabel lblBbSaleInvoices = new JLabel();
        lblBbSaleInvoices.setText("B2C Sale Invoices to Taxabale Person");
        lblBbSaleInvoices.setForeground(new Color(255, 153, 51));
        lblBbSaleInvoices.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        
        JButton btnPrint = new JButton();
        btnPrint.setText("PRINT");
        btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnPrint.setBackground(Color.WHITE);
        
        JButton btnExport = new JButton();
        btnExport.setText("EXPORT");
        btnExport.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnExport.setBackground(Color.WHITE);
        
        JLabel TotalBillAmt = new JLabel("Total Bill Amt.");
        TotalBillAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        JLabel TotalTaxableAmt = new JLabel("Total Taxable Amt");
        TotalTaxableAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        JLabel TotalTaxAmt = new JLabel("Total Tax Amt");
        TotalTaxAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        txtTaxableAmt = new JTextField();
        txtTaxableAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtTaxableAmt.setColumns(10);
        
        txtBillAmt = new JTextField();
        txtBillAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtBillAmt.setColumns(10);
        
        txtTaxAmt = new JTextField();
        txtTaxAmt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtTaxAmt.setColumns(10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(23)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblBbSaleInvoices, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE))
        			.addGap(10)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
        				.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
        			.addGap(30)
        			.addComponent(jToggleButton1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(btnPrint, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
        			.addGap(88)
        			.addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
        			.addGap(27))
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1197, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(28)
        			.addComponent(TotalBillAmt, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(txtBillAmt, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(TotalTaxableAmt, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(txtTaxableAmt, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
        			.addGap(43)
        			.addComponent(TotalTaxAmt, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        			.addGap(30)
        			.addComponent(txtTaxAmt, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(232, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        							.addComponent(jLabel3)
        							.addComponent(jLabel2)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        							.addComponent(lblBbSaleInvoices, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        							.addComponent(jLabel4))
        						.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jToggleButton1)
        				.addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnPrint, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 418, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(TotalBillAmt, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txtBillAmt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(TotalTaxableAmt, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txtTaxableAmt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txtTaxAmt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(TotalTaxAmt, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGap(23))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void filter3(Date startDate, Date endDate){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String edate =  sdf.format(endDate);
    Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(edate));
        } catch (ParseException ex) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    c.add(Calendar.DATE, 1);
    endDate = c.getTime();
    
    String sdate = sdf.format(startDate);
    Calendar c1 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(sdate));
        } catch (ParseException ex) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    c1.add(Calendar.DATE,-1);
    startDate = c1.getTime();
    
    
    List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
    filters.add( RowFilter.dateFilter(ComparisonType.AFTER, startDate) );
    filters.add( RowFilter.dateFilter(ComparisonType.BEFORE, endDate) );
   DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
   TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel> (model);
   jTable1.setRowSorter(tr);
    RowFilter<Object, Object> rf = RowFilter.andFilter(filters);
    tr.setRowFilter(rf);
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

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
       filter3(dateFrom.getDate(),dateTo.getDate());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private JButton btnClose;
    public static javax.swing.JTabbedPane tpScreensHolder;
    private JTextField txtTaxableAmt;
    private JTextField txtBillAmt;
    private JTextField txtTaxAmt;
}
