package jewellery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//1195, 680

/**
 *
 *
 * @author AR-LABS
 */
public class PaymentScreen extends javax.swing.JFrame {

    private ImageIcon imageIcon;
    private final DefaultTableModel partyNameSuggestionsTableModel;
    private final List<Object> accountNames = new ArrayList<>();
    Connection connect;
    private String reportsavepath;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Logger logger = Logger.getLogger(PaymentScreen.class.getName());
    Vector<String> banks;

    public PaymentScreen() {
        Logger.getLogger(PaymentScreen.class.getName()).log(Level.INFO, "payment screen object initiazed!");

        initComponents();
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        try {
            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();
            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            PAYMENTMODE.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");

            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(banks);
        PAYMENTMODE.setModel(model);
        txtdueamt.setVisible(false);
        jLabel10.setVisible(false);

        setImageOnJLabel(lblFrameImage, AssetsLocations.CASH_IMAGE);

        setImageOnJLabel(lblFrameImage, AssetsLocations.RECEIPT_IMAGE);

        setLocationRelativeTo(null);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = d.height;
        int width = d.width;
        height = height - 146;
        width = width - 275;
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        jPopupMenu1.add(spTblPartyNameSuggestionsContainer);
        jPopupMenu1.setLocation(txtPartyName.getX() + 6, txtPartyName.getY() + 70);
        jPanel1.setSize(width, height);
        java.util.Date date = new java.util.Date();
        jDateChooser1.setDate(date);
        updateButton.setVisible(false);
        deletebutton.setVisible(false);
        try {
            Connection c = DBConnect.connect();
            Statement stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT Receiptno from payments;");
            int i;
            if (rs.getRow() == 0) {
                i = 1;
                txtreceipt.setText("" + i);
            }
            while (rs.next()) {
                i = rs.getInt("Receiptno");

                txtreceipt.setText("" + (++i));
                System.out.print(i);

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        refresh();
    }

//    private void resizeFrame() {
//        this.setSize(GLOBAL_VARS.tabbedPaneDimensions.width + 90,
//                GLOBAL_VARS.tabbedPaneDimensions.height);
//    }
    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:
        refresh();
    }

    private void setImageOnJLabel(javax.swing.JLabel component,
            String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth(),
                        component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        pnlParent = new javax.swing.JPanel();
        lblFrameImage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        PAYMENTMODE = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtreceipt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttotalamt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtdiscountamt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtdueamt = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        deletebutton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlParent.setBackground(new java.awt.Color(57, 68, 76));
        pnlParent.setForeground(new java.awt.Color(189, 150, 117));

        lblFrameImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Payment");

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));

        PAYMENTMODE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PAYMENTMODEFocusGained(evt);
            }
        });
        PAYMENTMODE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PAYMENTMODEActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Payment Mode :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Date :");

        updateButton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        Clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Clear.setText("Clear");
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PAYMENTMODE, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(Clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(PAYMENTMODE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Receipt No.");

        txtreceipt.setEditable(false);
        txtreceipt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtreceiptFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtreceiptFocusLost(evt);
            }
        });
        txtreceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtreceiptActionPerformed(evt);
            }
        });
        txtreceipt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtreceiptKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Party Name");

        txtPartyName.setPreferredSize(new java.awt.Dimension(6, 30));
        txtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusLost(evt);
            }
        });
        txtPartyName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartyNameActionPerformed(evt);
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

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Total Amt.");

        txttotalamt.setPreferredSize(new java.awt.Dimension(6, 30));
        txttotalamt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txttotalamtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txttotalamtFocusLost(evt);
            }
        });
        txttotalamt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttotalamtKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Discount Amt.");

        txtdiscountamt.setPreferredSize(new java.awt.Dimension(6, 30));
        txtdiscountamt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtdiscountamtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtdiscountamtFocusLost(evt);
            }
        });
        txtdiscountamt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtdiscountamtKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Remarks");

        jTable1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Receipt No.", "Account Name", "Total Amount", "Discount Amount", "Payment Mode", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        jPanel2.setBackground(new java.awt.Color(56, 68, 76));
        jPanel2.setPreferredSize(new java.awt.Dimension(1009, 70));

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton4.setText("Print");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Due Amount :");

        txtdueamt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdueamtActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        jButton7.setText("Save");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jButton7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton7KeyPressed(evt);
            }
        });

        deletebutton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        deletebutton.setText("Delete Receipt");
        deletebutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deletebuttonMouseClicked(evt);
            }
        });
        deletebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(deletebutton)
                .addGap(54, 54, 54)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtdueamt, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtdueamt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setText("Close");
        jButton2.setPreferredSize(new java.awt.Dimension(70, 22));
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

        javax.swing.GroupLayout pnlParentLayout = new javax.swing.GroupLayout(pnlParent);
        pnlParent.setLayout(pnlParentLayout);
        pnlParentLayout.setHorizontalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1158, Short.MAX_VALUE)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlParentLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                .addGap(98, 98, 98))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlParentLayout.createSequentialGroup()
                                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlParentLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel2))
                                    .addGroup(pnlParentLayout.createSequentialGroup()
                                        .addComponent(txtreceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addComponent(txtPartyName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlParentLayout.createSequentialGroup()
                                .addComponent(txttotalamt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(205, 205, 205))
                            .addGroup(pnlParentLayout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)))
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(190, 190, 190))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlParentLayout.createSequentialGroup()
                .addGap(0, 426, Short.MAX_VALUE)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtdiscountamt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(107, 107, 107)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
            .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlParentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblFrameImage, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(122, Short.MAX_VALUE)))
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlParentLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6))
                    .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel9))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdiscountamt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttotalamt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtreceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlParentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblFrameImage, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(90, 90, 90)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(68, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlParent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
                    (suggestion.get(2) == null) ? "NULL" : suggestion.get(2)});

            } catch (Exception ex) {
                Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void refresh() {
        DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
        m.setRowCount(0);
        try {

            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todaydate = sdf.format(jDateChooser1.getDate());
//        JOptionPane.showMessageDialog(this, todaydate);
//            String todaydate =((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
            ResultSet rs = stmt.executeQuery("SELECT * FROM payments where date = '" + todaydate + "';");
            while (rs.next()) {
                String rec = rs.getString("Receiptno");
                String name = rs.getString("Name");
                String ID = rs.getString("amtpaid");
                String Contact = rs.getString("discount");
                String mop = rs.getString("mop");
                String amount = rs.getString("remarks");
                m.addRow(new Object[]{rec, name, ID, Contact, mop, amount});
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
    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            txtdueamt.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 2).toString());
            jPopupMenu1.setVisible(false);
        }
    }//GEN-LAST:event_tblPartyNameSuggestionsMouseClicked
    int selectedrow = 0;
    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased
        // TODO add your handling code here:
        fetchAccountNames();
        if (!(accountNames == null || accountNames.isEmpty())) {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    jPopupMenu1.setVisible(false);
                    break;
                case KeyEvent.VK_ENTER:
                    txttotalamt.requestFocusInWindow();
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
                default:
                    EventQueue.invokeLater(() -> {
                        jPopupMenu1.setVisible(true);
                        populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, dueamt FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'");

                    });
                    break;
            }
        }
    }//GEN-LAST:event_txtPartyNameKeyReleased

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusLost
        txtPartyName.setBackground(Color.white);
        jPopupMenu1.setVisible(false);
    }//GEN-LAST:event_txtPartyNameFocusLost
    void clear() {
        txtPartyName.setText("");
        txttotalamt.setText("");
        txtdiscountamt.setText("");
        txttotalamt.setText("");
        jTextField1.setText("");
        int lastReceiptNo = 0;
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "SELECT MAX(Receiptno) FROM payments;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                lastReceiptNo = rs.getInt("MAX(Receiptno)");
            }
            con.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        txtreceipt.setText(String.valueOf(lastReceiptNo + 1));
        txtdueamt.setText("");
        txtPartyName.requestFocus();
    }
    private void txtdiscountamtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiscountamtFocusLost
        txtdiscountamt.setBackground(Color.white);
        if (txtdiscountamt.getText().trim().isEmpty()) {
            txtdiscountamt.setText("0");

        }
    }//GEN-LAST:event_txtdiscountamtFocusLost

    private void txtdiscountamtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdiscountamtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField1.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtdiscountamtKeyReleased

    private double getDueAmountByName(String partyname) {
        try {
            String dueAmountQuery = "select dueamt from account where accountname = '" + partyname + "';";
            Connection c = DBConnect.connect();

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(dueAmountQuery);
            double currentDueAmount = 0;
            while (rs.next()) {
                currentDueAmount = rs.getDouble("dueamt");
            }
            c.close();
            s.close();
            JOptionPane.showMessageDialog(this, "try to getting the due amount=> " + currentDueAmount);
            return currentDueAmount;
        } catch (SQLException e) {
            Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0.0;
    }

    private void saveButtonClicked() {
        try {
            int recpno = Integer.parseInt(txtreceipt.getText());
            String name = txtPartyName.getText();
            double dueamt = Double.parseDouble(txtdueamt.getText());

            double dis;
            if (!txtdiscountamt.getText().trim().isEmpty()) {
                dis = Double.parseDouble(txtdiscountamt.getText());
            } else {
                dis = 0;
            }
            double amtpaid = Double.parseDouble(txttotalamt.getText());
            String remarks = (String) jTextField1.getText();
            String mop = (String) PAYMENTMODE.getSelectedItem();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());

            double ttlAmount = dueamt + amtpaid;
            JOptionPane.showMessageDialog(this, "dueamt=> " + dueamt + " amtpaid=> " + amtpaid + "final amount=> " + ttlAmount);
//        if (ttlAmount < 0.0) {
//            JFrame f = new JFrame();
//            JOptionPane.showMessageDialog(f,"Amount Exceeded!! \n Please enter Amount less than Due Amount.");
//            ttlAmount = 0.0;
//        }
            int result = 0;
            try {
                Connection c = DBConnect.connect();

                Statement s = c.createStatement();
                result = s.executeUpdate("insert into payments(Receiptno,Name,date,discount,amtpaid,mop,remarks) values('" + recpno + "','" + name + "','" + date + "','" + dis + "','" + String.format("%.2f", amtpaid) + "','" + mop + "','" + remarks + "');");

                c.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e);
            }
            try {
                Connection c1 = DBConnect.connect();
                Statement s1 = c1.createStatement();
                ttlAmount = ttlAmount;

                String q = "update account set dueamt = '" + String.format("%.2f", ttlAmount) + "' WHERE accountname = '" + name + "';";
                result = s1.executeUpdate(q);

                c1.close();
                refresh();
                clear();
                recpno++;
                txtreceipt.setText(Integer.toString(recpno));
                if (result == 1) {
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Transaction Saved!");
                } else {
                    System.out.println("error");
                }
                JFrame f1 = new JFrame();
                int reply = JOptionPane.showConfirmDialog(f1, "Do you want a Print Preview ?", "Select Print", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    int bill_no = recpno - 1;
                    printx(bill_no);
                }

            } catch (SQLException ex) {
                Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void fillPageFields(String receiptNo, String partyname, String totalamt, String discount, String remarks, Date date, String mop) {
        txtreceipt.setText(receiptNo);
        txtPartyName.setText(partyname);
        jTextField1.setText(remarks);
        txttotalamt.setText(totalamt);
        txtdiscountamt.setText(discount);
        jDateChooser1.setDate(date);
        PAYMENTMODE.setSelectedItem(mop);

        double dueamt = getDueAmountByName(partyname);
        txtdueamt.setText(String.valueOf(dueamt));
    }

    protected void paymentHistoryRedirectFill(int selectedReceiptNumber) {
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "select * from payments where Receiptno = " + selectedReceiptNumber + ";";
            ResultSet rs = st.executeQuery(query);

            String partyname = "", totalamt = "", discount = "", remarks = "", mop = "";
            Date date = null;
            while (rs.next()) {
                partyname = rs.getString("Name");
                totalamt = String.valueOf(rs.getDouble("amtpaid"));
                discount = String.valueOf(rs.getDouble("discount"));
                remarks = rs.getString("remarks");
                mop = rs.getString("mop");
                date = rs.getDate("date");
            }

            fillPageFields(String.valueOf(selectedReceiptNumber), partyname, totalamt, discount, remarks, date, mop);
            updateButton.setVisible(true);
            deletebutton.setVisible(true);
            con.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void updateReceiptData() {
        int receiptNo = Integer.parseInt(txtreceipt.getText());
        String partyname = txtPartyName.getText();
        double amount = Double.parseDouble(txttotalamt.getText());
        double discount = Double.parseDouble(txtdiscountamt.getText());
        String date = dateFormat.format(jDateChooser1.getDate());
        String paymentMode = (String) PAYMENTMODE.getSelectedItem();
        String remarks = jTextField1.getText();

        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();

            ResultSet rs1 = st.executeQuery("select amtpaid from payments where Receiptno = " + receiptNo + ";");
            double previousReceiptAmount = 0.0;
            while (rs1.next()) {
                previousReceiptAmount = rs1.getDouble("amtpaid");
            }
            st.clearBatch();

            String query = "update payments set "
                    + "date = '" + date + "',"
                    + "discount = " + discount + ","
                    + "amtpaid = " + amount + ","
                    + "mop = '" + paymentMode + "',"
                    + "remarks = '" + remarks + "'"
                    + " where Receiptno = " + receiptNo + ";";
            st.executeUpdate(query);
            st.clearBatch();
            String dueAmountQuery = "select dueamt from account where accountname = '" + partyname + "';";
            ResultSet rs = st.executeQuery(dueAmountQuery);
            double currentDueAmount = 0;
            while (rs.next()) {
                currentDueAmount = rs.getDouble("dueamt");
            }

            double newDueAmount = currentDueAmount + previousReceiptAmount - amount;
            st.clearBatch();
            String dueAmountUpdateQuery = "update account set dueamt = " + newDueAmount + " where accountname = '" + partyname + "';";
            st.executeUpdate(dueAmountUpdateQuery);

            con.close();
            st.close();

            updateButton.setVisible(false);
            deletebutton.setVisible(false);
            refresh();
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model2 = (DefaultTableModel) jTable1.getModel();

        int selectedrow = jTable1.getSelectedRow();
        String remarks = model2.getValueAt(selectedrow, 5).toString();
        String amount = model2.getValueAt(selectedrow, 2).toString();
        String discount = model2.getValueAt(selectedrow, 3).toString();
        String recp = model2.getValueAt(selectedrow, 0).toString();
        String partyname
                = model2.getValueAt(selectedrow, 1).toString();
        PAYMENTMODE.setSelectedItem(model2.getValueAt(selectedrow, 4).toString());
        txtreceipt.setText(recp);
        txtPartyName.setText(partyname);
        jTextField1.setText(remarks);
        txttotalamt.setText(amount);
        txtdiscountamt.setText(discount);

        updateButton.setVisible(true);
        deletebutton.setVisible(true);

        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
//           ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE accounery(\"SELECT * FROM payments WHERE Receiptno='\"+recp+\"';\");\n" +
//"                while(rs1.next()){tname='"+partyname+"';");
            ResultSet rs = stmt.executeQuery("select * from account where accountname = '" + partyname + "';");
            while (rs.next()) {
                txtdueamt.setText(rs.getString("dueamt"));
            }
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM payments WHERE Receiptno='" + recp + "';");
            while (rs1.next()) {
                txttotalamt.setText(rs1.getString("amtpaid"));
                txtdiscountamt.setText(rs1.getString("discount"));
                jDateChooser1.setDate(rs1.getDate("date"));
            }
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void printx(int recpno) {
//        JOptionPane.showMessageDialog(this, "running");
        Map<String, Object> parameters = new HashMap<>();
        Connection connection = DBConnect.connect();
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());
            String query = "select * from payments where date='" + date + "' AND Receiptno='" + recpno + "'";
//            JOptionPane.showMessageDialog(this, query);
            InputStream input = new FileInputStream(new File("jasper_reports" + File.separator + "payments.jrxml"));
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
    private void txttotalamtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttotalamtKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtdiscountamt.requestFocusInWindow();
        }
    }//GEN-LAST:event_txttotalamtKeyReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            saveButtonClicked();
        }

    }//GEN-LAST:event_jTextField1KeyPressed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        // TODO add your handling code here:

        clear();
        updateButton.setVisible(false);
        deletebutton.setVisible(false);
    }//GEN-LAST:event_ClearActionPerformed

    private void PAYMENTMODEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PAYMENTMODEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PAYMENTMODEActionPerformed

    private void PAYMENTMODEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PAYMENTMODEFocusGained
        // TODO add your handling code here:
        try {

            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();
            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            PAYMENTMODE.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");

            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
//                JOptionPane.showMessageDialog(this, rs1.getString("accountname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(banks);
        PAYMENTMODE.setModel(model);
    }//GEN-LAST:event_PAYMENTMODEFocusGained

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusGained
        txtPartyName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
    }//GEN-LAST:event_txtPartyNameFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:

        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());
        clear();
        dispose();
    }//GEN-LAST:event_jButton2MouseClicked

    private void txtreceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtreceiptActionPerformed


    }//GEN-LAST:event_txtreceiptActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        // TODO add your handling code here:
        updateReceiptData();
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deletebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deletebuttonActionPerformed

    private void deletebuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deletebuttonMouseClicked
        // TODO add your handling code here:

        int receiptNo = Integer.parseInt(txtreceipt.getText());
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "delete from payments where Receiptno = " + receiptNo + ";";
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
        }
    }//GEN-LAST:event_deletebuttonMouseClicked

    private void jButton7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton7KeyPressed
        if (evt.getKeyCode() == 10) {
            jButton4.doClick();
        }
    }//GEN-LAST:event_jButton7KeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveButtonClicked();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtdueamtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdueamtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdueamtActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int recpno = Integer.parseInt(txtreceipt.getText());
        printx(recpno);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtreceiptFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtreceiptFocusGained
        txtreceipt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtreceiptFocusGained

    private void txtreceiptFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtreceiptFocusLost
        txtreceipt.setBackground(Color.white);
    }//GEN-LAST:event_txtreceiptFocusLost

    private void txttotalamtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txttotalamtFocusGained
        txttotalamt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txttotalamtFocusGained

    private void txttotalamtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txttotalamtFocusLost
        txttotalamt.setBackground(Color.white);
    }//GEN-LAST:event_txttotalamtFocusLost

    private void txtdiscountamtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiscountamtFocusGained
        txtdiscountamt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtdiscountamtFocusGained

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        jTextField1.setBackground(Color.white);
    }//GEN-LAST:event_jTextField1FocusLost

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
            txtdueamt.setText(String.valueOf(dueAmount));
            JOptionPane.showMessageDialog(this, "due amount is => " + dueAmount);
        }
    }//GEN-LAST:event_txtPartyNameKeyPressed

    private void txtreceiptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtreceiptKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPartyName.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtreceiptKeyReleased

    private void txtPartyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Clear;
    private javax.swing.JComboBox<String> PAYMENTMODE;
    private javax.swing.JButton deletebutton;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblFrameImage;
    public javax.swing.JPanel pnlParent;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtdiscountamt;
    private javax.swing.JTextField txtdueamt;
    private javax.swing.JTextField txtreceipt;
    private javax.swing.JTextField txttotalamt;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
