package jewellery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import jewellery.helper.outstandingAnalysisHelper;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author SACHIN MISHRA
 */
public class ReceiptScreen extends javax.swing.JFrame {

    /**
     * Creates new form ReceiptScreen
     */
    private ImageIcon imageIcon;
    private final DefaultTableModel partyNameSuggestionsTableModel;
    private final List<Object> accountNames = new ArrayList<>();
    Connection connect;
    private String reportsavepath;
    private Logger logger = Logger.getLogger(ReceiptScreen.class.getName());
    Vector<String> banks;

    public ReceiptScreen() {
        Logger.getLogger(ReceiptScreen.class.getName()).log(Level.INFO, "reciept screen object initiazed!");
        initComponents();
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                try {
                    jComboBox1FocusGained(evt);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        txtRemarks.addFocusListener(new ReceiptScreen.customfocusListner());
        setImageOnJLabel(lblFrameImage, AssetsLocations.RECEIPT_IMAGE);

//        resizeFrame();
        setLocationRelativeTo(null);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = d.height;
        int width = d.width;
        height = height - 146;
        width = width - 275;
        try {
            fatchPaymentMethods();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
        pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 6, txtPartyName.getY() + 70);
        jPanel1.setSize(width, height);
        deletebutton.setVisible(false);

        java.util.Date date = new java.util.Date();
        jDateChooser1.setDate(date);
        try {
            Connection c = DBConnect.connect();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(Receiptno) AS maxReceiptno FROM receipt;");

            int receiptNo = 1; // Default to 1

            if (rs.next()) {
                receiptNo = rs.getInt("maxReceiptno") + 1;
            }
            if(receiptNo<=0)
            {
                receiptNo=1;
            }
            jTextField1.setText(String.valueOf(receiptNo));
            jTextField1.setEditable(false);

            rs.close();
            stmt.close();
            c.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        refresh();
    }

    class customfocusListner implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            txtRemarks.setText("");
            // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void focusLost(FocusEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

    }

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) throws FileNotFoundException {
        System.out.println("*****************************************************************************runnig receipt screen bank name fatching");
        Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, "*****************************************************************************runnig receipt screen bank name fatching");

        fatchPaymentMethods();
    }

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:
        refresh();
    }

    private void fatchPaymentMethods() throws FileNotFoundException {
        System.out.println("*****************************************************************************runnig receipt screen bank name fatching");
        Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, "*****************************************************************************runnig receipt screen bank name fatching");

        try {
            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();

            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            jComboBox1.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");
            //banks.add("bank group");
            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
//                JOptionPane.showMessageDialog(this, rs1.getString("accountname"));
            }
            rs1.close();
            cd.close();
            stmet.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(banks);
        jComboBox1.setModel(model);
        jTextField3.setVisible(false);
    }

    public void fillByRedirect(String recp) {

        if (Integer.parseInt(recp) <= 0) {
            recp = (Integer.parseInt(recp) + 1) + "";
        }
        jTextField1.setText(recp);
        savebtn.setText("Update");
        deletebutton.setVisible(true);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM receipt WHERE Receiptno='" + recp + "';");
            while (rs1.next()) {
                txtPartyName.setText(rs1.getString("Name"));
                jTextField5.setText(rs1.getString("amtpaid"));
                jTextField4.setText(rs1.getString("discount"));
                jDateChooser1.setDate(rs1.getDate("date"));
                txtRemarks.setText(rs1.getString("remarks"));
                jComboBox1.setSelectedItem(rs1.getString("mop"));

            }
            stmt.clearBatch();
            ResultSet rs = stmt.executeQuery("select dueamt from account where accountname = '" + txtPartyName.getText() + "';");
            while (rs.next()) {
                jTextField3.setText(rs.getString("dueamt"));
            }

            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private void resizeFrame() {
//        this.setSize(GLOBAL_VARS.tabbedPaneDimensions.width + 90,
//                GLOBAL_VARS.tabbedPaneDimensions.height);
//    }
    private void setImageOnJLabel(javax.swing.JLabel component,
            String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth(),
                        component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    private void updateReceipt() {
        Connection c;
        try {
            c = DBConnect.connect();

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select max(Receiptno) from receipt");
            int lastRec = 1;
            while (rs.next()) {
                lastRec = rs.getInt("max(Receiptno)");
            }
            lastRec = lastRec + 1;
            if (lastRec <= 0) {
                lastRec = 1;
            }
            jTextField1.setText("" + lastRec);
            c.close();
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void printx() {
//        JOptionPane.showMessageDialog(this, "running");

        Connection connection = DBConnect.connect();
        try {
            String bill_no = jTextField1.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());
            String query = "select * from receipt where date='" + date + "' AND Receiptno = '" + bill_no + "' ";
//            JOptionPane.showMessageDialog(this, query);
            InputStream input = new FileInputStream(new File("jasper_reports" + File.separator + "receipt.jrxml"));
            JasperDesign design = JRXmlLoader.load(input);
            JRDesignQuery updateQuary = new JRDesignQuery();
            updateQuary.setText(query);
            design.setQuery(updateQuary);
            String realPath = System.getProperty("user.dir") + File.separator + "jasper_reports" + File.separator + "img" + File.separator;
            HashMap map = new HashMap();
            map.put("imagePath", realPath);
//            JOptionPane.showMessageDialog(this, "image path" + realPath);
            JasperReport jreport = JasperCompileManager.compileReport(design);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, map, connection);
            JasperViewer.viewReport(jprint, false);
            connection.close();
        } catch (Exception ex) {
//            DebugAndLogsScreen d=new  DebugAndLogsScreen();
            System.out.println(ex);
//            for(StackTraceElement e: ex.getStackTrace()){
//              JOptionPane.showMessageDialog(this, "running12 " + e);   
//            }

            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {

        DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
        m.setRowCount(0);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String curdate = sdf.format(jDateChooser1.getDate());
//            String curdate =  ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
            ResultSet rs = stmt.executeQuery("SELECT * FROM receipt where   sales_Bill = -1 and date = '" + curdate + "';");
            while (rs.next()) {
                String rec = rs.getString("Receiptno");
                String name = rs.getString("Name");
                String Contact = rs.getString("discount");
                String ID = rs.getString("amtpaid");
                String amount = rs.getString("remarks");
                m.addRow(new Object[]{rec, name, Contact, ID, amount});
            }
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int g = 0; g < jTable1.getColumnCount(); g++) {
                jTable1.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void clear() {
        txtPartyName.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        txtRemarks.setText("");
        savebtn.setText("Save");
        deletebutton.setVisible(false);
        int lastReceiptNo = 1;
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "select max(Receiptno) from receipt;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                lastReceiptNo = rs.getInt(1);
            }
            con.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        lastReceiptNo = lastReceiptNo + 1;
        if (lastReceiptNo <= 0) {
            lastReceiptNo = 1;
        }
        jTextField1.setText("" + lastReceiptNo);
        txtPartyName.requestFocus();
    }

    private void fetchAccountNames() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> account_names = DBController.executeQuery("SELECT accountname FROM account");

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
                suggestionsTable.addRow(new Object[]{
                    (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                    (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                    (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),});
            } catch (Exception ex) {

                Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
        pnlParent = new javax.swing.JPanel();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        lblFrameImage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        clear = new javax.swing.JButton();
        txtRemarks = new javax.swing.JTextField();
        savebtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        btnClose = new javax.swing.JButton();
        deletebutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlParent.setBackground(new java.awt.Color(57, 68, 76));
        pnlParent.setForeground(new java.awt.Color(189, 150, 117));
        pnlParent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Party Name", "State", "Due Amt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPartyNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartyNameSuggestionsMouseClicked(evt);
            }
        });
        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);

        pnlParent.add(spTblPartyNameSuggestionsContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 440, 270));

        lblFrameImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlParent.add(lblFrameImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 6, 24, 79));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Receipt");
        pnlParent.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 6, 211, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 183, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlParent.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(952, 6, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Receipt No.");
        pnlParent.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, -1));

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        pnlParent.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 107, 20));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Party Name");
        pnlParent.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 110, -1));

        txtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusLost(evt);
            }
        });
        txtPartyName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPartyNameMouseClicked(evt);
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
        pnlParent.add(txtPartyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 170, 20));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Amt. Paid");
        pnlParent.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 90, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Discount Amt.");
        pnlParent.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 145, -1));

        jTextField4.setPreferredSize(new java.awt.Dimension(6, 30));
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
        });
        pnlParent.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, 145, 20));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Remarks");
        pnlParent.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, 90, -1));

        jScrollPane1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Receipt No.", "Account Name", "Discount Amount", "Paid Amount", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        pnlParent.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 196, 980, 340));

        jPanel2.setBackground(new java.awt.Color(57, 68, 76));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Due.Amt");

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printIcon.png"))); // NOI18N
        jButton7.setText("Print");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        clear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clearKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlParent.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 540, 870, 60));

        txtRemarks.setPreferredSize(new java.awt.Dimension(6, 30));
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRemarksFocusLost(evt);
            }
        });
        txtRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRemarksKeyPressed(evt);
            }
        });
        pnlParent.add(txtRemarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 160, 149, 20));

        savebtn.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        savebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        savebtn.setText("Save");
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });
        savebtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                savebtnKeyPressed(evt);
            }
        });
        pnlParent.add(savebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 550, 120, 50));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Payment Mode :");
        pnlParent.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 62, -1, -1));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Date :");
        pnlParent.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 14, -1, -1));

        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });
        pnlParent.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 127, 20));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Card" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        pnlParent.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(746, 60, 150, -1));
        pnlParent.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(746, 14, 150, -1));

        btnClose.setBackground(new java.awt.Color(255, 0, 0));
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(70, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlParent.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 10, 70, 31));

        deletebutton.setBackground(new java.awt.Color(255, 0, 0));
        deletebutton.setText("Delete");
        deletebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebuttonActionPerformed(evt);
            }
        });
        pnlParent.add(deletebutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 60, 70, 31));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlParent, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlParent, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusLost
        txtPartyName.setBackground(Color.white);
        pmPartyNameSuggestionsPopup.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameFocusLost

    int selectedrow = 0;
    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased
        try {
            fetchAccountNames();
            if (!(accountNames == null || accountNames.isEmpty())) {
                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_BACK_SPACE:
                        pmPartyNameSuggestionsPopup.setVisible(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        tblPartyNameSuggestions.requestFocus();
                        if (selectedrow == 0) {
                            tblPartyNameSuggestions.setRowSelectionInterval(0, 0);
                            selectedrow++;
                        } else {
                            if (tblPartyNameSuggestions.getSelectedRow() < tblPartyNameSuggestions.getRowCount() - 1) {
                                tblPartyNameSuggestions.setRowSelectionInterval(tblPartyNameSuggestions.getSelectedRow() + 1, tblPartyNameSuggestions.getSelectedRow() + 1);
                            }
                        }
                        txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0).toString().trim());

                        break;
                    case KeyEvent.VK_UP:
                        tblPartyNameSuggestions.requestFocus();

                        if (tblPartyNameSuggestions.getSelectedRow() > 0) {
                            tblPartyNameSuggestions.setRowSelectionInterval(tblPartyNameSuggestions.getSelectedRow() - 1, tblPartyNameSuggestions.getSelectedRow() - 1);
                        }

                        txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0).toString().trim());

                        break;
                    case KeyEvent.VK_ENTER:
                        jTextField5.requestFocusInWindow();
                        break;
                    default:
                        EventQueue.invokeLater(() -> {
                            pmPartyNameSuggestionsPopup.setVisible(true);
                            populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                    + "state, dueamt FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                    + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'");
                        });
                        break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_txtPartyNameKeyReleased

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        jTextField4.setBackground(Color.white);
        if (jTextField4.getText().trim().isEmpty()) {
            jTextField4.setText("0");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtRemarks.requestFocusInWindow();

        }
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        DefaultTableModel model2 = (DefaultTableModel) jTable1.getModel();

        int selectedrow = jTable1.getSelectedRow();
        String recp = model2.getValueAt(selectedrow, 0).toString();
        String partyname
                = model2.getValueAt(selectedrow, 1).toString();
        if (Integer.parseInt(recp) <= 0) {
            recp = (Integer.parseInt(recp) + 1) + "";
        }
        jTextField1.setText(recp);
        txtPartyName.setText(partyname);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            //                ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE accounery(\"SELECT * FROM receipt WHERE Receiptno='\"+recp+\"';\");\n" +
            //"                while(rs1.next()){tname='"+partyname+"';");
            ResultSet rs = stmt.executeQuery("select dueamt from account where accountname = '" + partyname + "';");
            while (rs.next()) {
                jTextField3.setText(rs.getString("dueamt"));
            }
            stmt.clearBatch();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM receipt WHERE Receiptno='" + recp + "';");
            while (rs1.next()) {
                jTextField5.setText(rs1.getString("amtpaid"));
                jTextField4.setText(rs1.getString("discount"));
                jDateChooser1.setDate(rs1.getDate("date"));
                txtRemarks.setText(rs1.getString("remarks"));
            }
            con.close();
            stmt.close();
            rs.close();
            savebtn.setText("Update");
            deletebutton.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        try {
            int recpno = Integer.parseInt(jTextField1.getText());
            if(recpno<=0)
            {
                recpno=1;
            }
            String name = txtPartyName.getText();
            double dueamt = Double.parseDouble(jTextField3.getText());

            double dis;
            if (!jTextField4.getText().trim().isEmpty()) {
                dis = Double.parseDouble(jTextField4.getText());
            } else {
                dis = 0;
            }
            double amtpaid = Double.parseDouble(jTextField5.getText());
            String remarks = (String) txtRemarks.getText();
            String mop = (String) jComboBox1.getSelectedItem();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());
              JOptionPane.showMessageDialog(this, "dueamt=> "+dueamt+" amtpaid=> "+amtpaid);
            double ttlAmount = dueamt -(- amtpaid);
            
            
            String transactionType = "Save";
            if (savebtn.getText().equals("Update")) {
                transactionType = "Update";
            }

            try {

                Connection c = DBConnect.connect();

                Statement s = c.createStatement();

                boolean present = false;
                ResultSet rs = s.executeQuery("select max(Receiptno) from receipt");
                int lastRec = 1;
                while (rs.next()) {
                    lastRec = rs.getInt("max(Receiptno)");
                }
                if(lastRec<=0)
                {
//                    lastRec=1;
                }
                if (transactionType.equals("Update")) {
                    s.executeUpdate("update receipt set Name = '" + name + "', date = '" + date + "', discount = " + dis
                            + ", amtpaid = " + String.format("%.2f", amtpaid) + ", remarks = '" + remarks + "', mop = '" + mop + "' "
                            + "where Receiptno = " + recpno + ";");
                } else {
                    s.executeUpdate("insert into receipt(Receiptno,Name,date,discount,amtpaid,mop,remarks) values('" + (lastRec + 1) + "','" + name + "','" + date + "','" + dis + "','" + String.format("%.2f", amtpaid) + "','" + mop + "','" + remarks + "');");
                }

                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Connection c1 = DBConnect.connect();

                Statement s1 = c1.createStatement();

                String q = "update account set dueamt = '" + String.format("%.2f", ttlAmount) + "' WHERE accountname = '" + name + "';";
                s1.executeUpdate(q);

                c1.close();

                clear();
                String buttonText = savebtn.getText();
                savebtn.setText("Save");
                deletebutton.setVisible(false);
                recpno = recpno + 1;
                String showInBox = "Transaction Saved!";
                if (buttonText.equals("Update")) {
                    showInBox = "Transaction Updated !";
                }
                if (recpno <= 0) {
                    recpno = (recpno + 1);
                }
                jTextField1.setText(Integer.toString(recpno));
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, showInBox);

                //            JFrame f1 = new JFrame();
                //            int reply = JOptionPane.showConfirmDialog(f1, "Do you want a Print Preview ?", "Select Print", JOptionPane.YES_NO_OPTION);
                //            if (reply == JOptionPane.YES_OPTION) {
                //            }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            refresh();
        } catch (Exception e) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
        //    	}
    }//GEN-LAST:event_savebtnActionPerformed

    private void savebtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_savebtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            savebtn.doClick();
        }
    }//GEN-LAST:event_savebtnKeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        printx();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField4.requestFocus();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        jTextField5.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField5FocusGained

    private void txtRemarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusLost
        txtRemarks.setBackground(Color.white);
    }//GEN-LAST:event_txtRemarksFocusLost

    private void txtRemarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            savebtn.requestFocus();
        }
    }//GEN-LAST:event_txtRemarksKeyPressed

    private void txtPartyNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPartyNameMouseClicked
        // TODO add your handling code here:
        updateReceipt();
    }//GEN-LAST:event_txtPartyNameMouseClicked

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        clear();        // TODO add your handling code here:
    }//GEN-LAST:event_clearActionPerformed

    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            jTextField3.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 2).toString());
            pmPartyNameSuggestionsPopup.setVisible(false);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tblPartyNameSuggestionsMouseClicked

    private void deletebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebuttonActionPerformed
        int receiptNo = Integer.parseInt(jTextField1.getText());
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "delete from receipt where Receiptno = " + receiptNo + ";";
            st.execute(query);
            con.close();
            st.close();
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "Receipt Deleted !");
            deletebutton.setVisible(false);
            refresh();
            clear();

        } catch (SQLException e) {
            Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_deletebuttonActionPerformed

    private void clearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clearKeyPressed
        if (evt.getKeyCode() == 10) {
            savebtn.doClick();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_clearKeyPressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusGained
        txtPartyName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameFocusGained

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        jTextField1.setBackground(Color.white);
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        jTextField5.setBackground(Color.white);
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        jTextField4.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField4FocusGained

    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        txtRemarks.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtRemarksFocusGained

    private void txtPartyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            String name = txtPartyName.getText();
            double dueAmount = 0.0;
            try {
                Connection c = DBConnect.connect();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT dueamt FROM account WHERE accountname = '" + name + "';");
                if (rs.next()) {
                    dueAmount = rs.getDouble("dueamt");
                }
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            jTextField3.setText(String.valueOf(dueAmount));

        }
    }//GEN-LAST:event_txtPartyNameKeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPartyName.requestFocusInWindow();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

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
            java.util.logging.Logger.getLogger(ReceiptScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReceiptScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReceiptScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceiptScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceiptScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton clear;
    private javax.swing.JButton deletebutton;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel lblFrameImage;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    public javax.swing.JPanel pnlParent;
    private javax.swing.JButton savebtn;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
