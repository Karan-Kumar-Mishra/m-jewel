package jewellery;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusListener;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Timer;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import jewellery.helper.outstandingAnalysisHelper;

/**
 *
 * @author AR-LABS
 */
public class OldReceiptScreen extends javax.swing.JFrame {

    private ImageIcon imageIcon;
    private final DefaultTableModel partyNameSuggestionsTableModel;
    private final List<Object> accountNames = new ArrayList<>();
    Connection connect;
    private String reportsavepath;
    private Logger logger = Logger.getLogger(ReceiptScreen.class.getName());
    Vector<String> banks;

    public OldReceiptScreen() throws FileNotFoundException {
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
        txtRemarks.addFocusListener(new customfocusListner());
        setImageOnJLabel(lblFrameImage, AssetsLocations.RECEIPT_IMAGE);

        // resizeFrame();

        setLocationRelativeTo(null);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = d.height;
        int width = d.width;
        height = height - 146;
        width = width - 275;
        fatchPaymentMethods();
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
        pmPartyNameSuggestionsPopup.setLocation(jTextField2.getX() + 6, jTextField2.getY() + 70);
        jPanel1.setSize(width, height);
        deletebutton.setVisible(false);

        java.util.Date date = new java.util.Date();
        jDateChooser1.setDate(date);
        try {
            Connection c = DBConnect.connect();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Receiptno from receipt;");
            int i;
            if (rs.getRow() == 0) {
                i = 1;
                jTextField1.setText("" + i);
            }
            while (rs.next()) {
                i = rs.getInt("Receiptno");

                jTextField1.setText("" + (++i));
                System.out.print(i);

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        refresh();
    }

    class customfocusListner implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            txtRemarks.setText("");
            // Generated from
            // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void focusLost(FocusEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                           // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

    }

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) throws FileNotFoundException {
        System.out.println(
                "*****************************************************************************runnig receipt screen bank name fatching");
        Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null,
                "*****************************************************************************runnig receipt screen bank name fatching");

        fatchPaymentMethods();
    }

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:
        refresh();
    }

    private void fatchPaymentMethods() throws FileNotFoundException {
        System.out.println(
                "*****************************************************************************runnig receipt screen bank name fatching");
        Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null,
                "*****************************************************************************runnig receipt screen bank name fatching");

        try {
            Connection cd = DBConnect.connect();
            Statement stmet;
            stmet = cd.createStatement();

            ResultSet rs1 = stmet.executeQuery("SELECT accountname FROM account WHERE grp='Bank'");
            jComboBox1.removeAllItems();
            banks = new Vector<>();
            banks.add("Cash");
            banks.add("UPI");
            banks.add("Net Banking");
            while (rs1.next()) {
                banks.add(rs1.getString("accountname"));
                // JOptionPane.showMessageDialog(this, rs1.getString("accountname"));
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

        jTextField1.setText(recp);
        jButton4.setText("Update");
        deletebutton.setVisible(true);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM receipt WHERE Receiptno='" + recp + "';");
            while (rs1.next()) {
                jTextField2.setText(rs1.getString("Name"));
                jTextField5.setText(rs1.getString("amtpaid"));
                jTextField4.setText(rs1.getString("discount"));
                jDateChooser1.setDate(rs1.getDate("date"));
                txtRemarks.setText(rs1.getString("remarks"));

            }
            stmt.clearBatch();
            ResultSet rs = stmt
                    .executeQuery("select dueamt from account where accountname = '" + jTextField2.getText() + "';");
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

    // private void resizeFrame() {
    // this.setSize(GLOBAL_VARS.tabbedPaneDimensions.width + 90,
    // GLOBAL_VARS.tabbedPaneDimensions.height);
    // }

    private void setImageOnJLabel(javax.swing.JLabel component,
            String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth(),
                        component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        pnlParent = new javax.swing.JPanel();
        lblFrameImage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        deletebutton = new javax.swing.JButton();

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Party Name", "State", "Due Amt"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblPartyNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartyNameSuggestionsMouseClicked(evt);
            }
        });
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2SuggestionsMouseClicked(evt);
            }
        });

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblFrameImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Receipt");

        jComboBox1.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Card", "UPI", "Net Banking" }));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Payment Mode :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Date :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(12, 12, 12)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jComboBox1,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(14, Short.MAX_VALUE)));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Receipt No.");

        jTextField1.setEditable(false);
        jTextField1.setPreferredSize(new java.awt.Dimension(6, 30));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Account Name");

        jTextField2.setPreferredSize(new java.awt.Dimension(6, 30));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Discount Amt.");

        jTextField4.setPreferredSize(new java.awt.Dimension(6, 30));
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Remarks");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Receipt No.", "Account Name", "Discount Amount", "Paid Amount", "Remarks"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printIcon.png"))); // NOI18N
        jButton7.setText("Print");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        deletebutton.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        deletebutton.setText("Delete Receipt");
        deletebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebuttonActionPerformed(evt);
            }
        });

        jTextField3 = new JTextField();
        jTextField3.setPreferredSize(new Dimension(6, 30));

        JLabel jLabel7 = new JLabel();
        jLabel7.setVisible(false);
        jLabel7.setText("Due  Amt.");
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel7.setFont(new Font("Times New Roman", Font.BOLD, 18));

        JButton btnClose = new JButton();
        btnClose.setForeground(Color.BLACK);
        btnClose.setBackground(Color.RED);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

                clear();
                dispose();
            }
        });
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

                clear();
                dispose();

            }
        });
        btnClose.setText("Close");
        btnClose.setFont(new Font("Times New Roman", Font.BOLD, 14));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(155)
                                .addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 211,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 218,
                                                GroupLayout.PREFERRED_SIZE))
                                .addGap(44)
                                .addComponent(jButton7, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                .addGap(220)
                                .addComponent(deletebutton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 21,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 30,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jButton7)
                                                .addComponent(deletebutton)
                                                .addContainerGap(36, Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 57,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap()))));
        jPanel2.setLayout(jPanel2Layout);

        txtRemarks = new JTextField();
        txtRemarks.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (txtRemarks.getText().trim().isEmpty()) {
                    txtRemarks.setText("paid");
                } // TODO add your handling code her
            }
        });
        txtRemarks.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    jButton4.requestFocusInWindow();
            }
        });
        txtRemarks.setPreferredSize(new Dimension(6, 30));

        jTextField5 = new JTextField();
        jTextField5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    jTextField4.requestFocusInWindow();
            }
        });
        jTextField5.setPreferredSize(new Dimension(6, 30));

        JLabel jLabel10 = new JLabel();
        jLabel10.setText("Amt. Paid");
        jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel10.setFont(new Font("Times New Roman", Font.BOLD, 18));
        jButton4 = new javax.swing.JButton();
        clearbutton = new javax.swing.JButton();

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        // jButton4.setIcon(new
        // javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png")));
        // // NOI18N
        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton4KeyPressed(evt);
            }
        });

        clearbutton.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        // clearbutton.setIcon(new
        // javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png")));
        // // NOI18N
        clearbutton.setText("Clear");
        clearbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearbuttonActionPerformed(evt);
            }
        });
        clearbutton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clearbuttonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlParentLayout = new javax.swing.GroupLayout(pnlParent);
        pnlParentLayout.setHorizontalGroup(
                pnlParentLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(pnlParentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlParentLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlParentLayout.createSequentialGroup()
                                                .addGap(10)
                                                .addGroup(pnlParentLayout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(pnlParentLayout.createSequentialGroup()
                                                                .addComponent(lblFrameImage, GroupLayout.PREFERRED_SIZE,
                                                                        171, Short.MAX_VALUE)
                                                                .addGap(41)
                                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 236,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(41)
                                                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 445,
                                                                        Short.MAX_VALUE))
                                                        .addGroup(pnlParentLayout.createSequentialGroup()
                                                                .addGroup(pnlParentLayout
                                                                        .createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(jTextField1, Alignment.TRAILING,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(jLabel5, Alignment.TRAILING,
                                                                                GroupLayout.DEFAULT_SIZE, 119,
                                                                                Short.MAX_VALUE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(pnlParentLayout
                                                                        .createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(jTextField2,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE,
                                                                                209, Short.MAX_VALUE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(pnlParentLayout
                                                                        .createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(jTextField5,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(jLabel10,
                                                                                GroupLayout.DEFAULT_SIZE, 165,
                                                                                Short.MAX_VALUE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(pnlParentLayout
                                                                        .createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(jTextField4,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(jLabel8, GroupLayout.DEFAULT_SIZE,
                                                                                145, Short.MAX_VALUE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(pnlParentLayout
                                                                        .createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(txtRemarks,
                                                                                GroupLayout.PREFERRED_SIZE, 145,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel9,
                                                                                GroupLayout.PREFERRED_SIZE, 149,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(jButton4)
                                                                .addComponent(clearbutton))))

                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 906,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap()));
        pnlParentLayout.setVerticalGroup(
                pnlParentLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(pnlParentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlParentLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 80,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlParentLayout.createParallelGroup(Alignment.LEADING, false)
                                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblFrameImage, GroupLayout.PREFERRED_SIZE, 79,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .addGap(18)
                                .addGroup(pnlParentLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(pnlParentLayout.createParallelGroup(Alignment.LEADING, false)
                                                .addGroup(pnlParentLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6)
                                                        .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 21,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(jLabel9))
                                                .addGroup(pnlParentLayout.createSequentialGroup()
                                                        .addGap(27)
                                                        .addGroup(pnlParentLayout
                                                                .createParallelGroup(Alignment.BASELINE)
                                                                .addComponent(jTextField1, GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jTextField2, GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE,
                                                                        30, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jTextField4, GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtRemarks, GroupLayout.PREFERRED_SIZE,
                                                                        30, GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jButton4)
                                        .addComponent(clearbutton))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 327, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnlParent.setLayout(pnlParentLayout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2SuggestionsMouseClicked(java.awt.event.MouseEvent evt) {
        updateReceipt();
    }

    private void updateReceipt() {
        Connection c;
        try {
            c = DBConnect.connect();

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select max(Receiptno) from receipt");
            int lastRec = 0;
            while (rs.next()) {
                lastRec = rs.getInt("max(Receiptno)");
            }

            jTextField1.setText(String.valueOf(lastRec + 1));
            c.close();
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        DefaultTableModel model2 = (DefaultTableModel) jTable1.getModel();

        int selectedrow = jTable1.getSelectedRow();
        String recp = model2.getValueAt(selectedrow, 0).toString();
        String partyname = model2.getValueAt(selectedrow, 1).toString();
        jTextField1.setText(recp);
        jTextField2.setText(partyname);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            // ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE
            // accounery(\"SELECT * FROM receipt WHERE Receiptno='\"+recp+\"';\");\n" +
            // " while(rs1.next()){tname='"+partyname+"';");
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
            jButton4.setText("Update");
            deletebutton.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// GEN-LAST:event_jTable1MouseClicked

    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        clear();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed

        int recpno = Integer.parseInt(jTextField1.getText());
        String name = jTextField2.getText();
        double dueamt = Double.parseDouble(jTextField3.getText());
        double dis = Double.parseDouble(jTextField4.getText());
        double amtpaid = Double.parseDouble(jTextField5.getText());
        String remarks = (String) txtRemarks.getText();
        String mop = (String) jComboBox1.getSelectedItem();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(jDateChooser1.getDate());

        double ttlAmount = dueamt - amtpaid;
        String transactionType = "Save";
        if (jButton4.getText().equals("Update")) {
            transactionType = "Update";
        }

        try {

            Connection c = DBConnect.connect();

            Statement s = c.createStatement();

            boolean present = false;
            ResultSet rs = s.executeQuery("select max(Receiptno) from receipt");
            int lastRec = 0;
            while (rs.next()) {
                lastRec = rs.getInt("max(Receiptno)");
            }

            if (transactionType.equals("Update")) {
                s.executeUpdate("update receipt set Name = '" + name + "', date = '" + date + "', discount = " + dis
                        + ", amtpaid = " + String.format("%.2f", amtpaid) + ", remarks = '" + remarks + "', mop = '"
                        + mop + "' "
                        + "where Receiptno = " + recpno + ";");
            } else {
                s.executeUpdate("insert into receipt(Receiptno,Name,date,discount,amtpaid,mop,remarks) values('"
                        + (lastRec + 1) + "','" + name + "','" + date + "','" + dis + "','"
                        + String.format("%.2f", amtpaid) + "','" + mop + "','" + remarks + "');");
            }

            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection c1 = DBConnect.connect();

            Statement s1 = c1.createStatement();

            String q = "update account set dueamt = '" + String.format("%.2f", ttlAmount) + "' WHERE accountname = '"
                    + name + "';";
            s1.executeUpdate(q);

            c1.close();
            refresh();
            clear();
            String buttonText = jButton4.getText();
            jButton4.setText("Save");
            deletebutton.setVisible(false);
            recpno++;
            String showInBox = "Transaction Saved!";
            if (buttonText.equals("Update")) {
                showInBox = "Transaction Updated !";
            }
            jTextField1.setText(Integer.toString(recpno));
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, showInBox);

            // JFrame f1 = new JFrame();
            // int reply = JOptionPane.showConfirmDialog(f1, "Do you want a Print Preview
            // ?", "Select Print", JOptionPane.YES_NO_OPTION);
            // if (reply == JOptionPane.YES_OPTION) {
            // printf();
            // }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        // }

    }// GEN-LAST:event_jButton4ActionPerformed

    private void printx() {
        // JOptionPane.showMessageDialog(this, "running");
        Map<String, Object> parameters = new HashMap<>();
        Connection connection = DBConnect.connect();
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());
            String query = "select * from receipt where date='" + date + "'";
            // JOptionPane.showMessageDialog(this, query);
            InputStream input = new FileInputStream(new File("jasper_reports\\receipt.jrxml"));
            JasperDesign design = JRXmlLoader.load(input);
            JRDesignQuery updateQuary = new JRDesignQuery();
            updateQuary.setText(query);
            design.setQuery(updateQuary);
            String realPath = System.getProperty("user.dir") + File.separator + "jasper_reports" + File.separator
                    + "img" + File.separator;
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

    public void refresh() {

        DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
        m.setRowCount(0);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String curdate = sdf.format(jDateChooser1.getDate());
            // String curdate = ((JTextField)
            // jDateChooser1.getDateEditor().getUiComponent()).getText();
            ResultSet rs = stmt.executeQuery("SELECT * FROM receipt where date = '" + curdate + "';");
            while (rs.next()) {
                String rec = rs.getString("Receiptno");
                String name = rs.getString("Name");
                String Contact = rs.getString("discount");
                String ID = rs.getString("amtpaid");
                String amount = rs.getString("remarks");
                m.addRow(new Object[] { rec, name, Contact, ID, amount });
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

    private void printf() {
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

                pnlParent.print(g2);

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
    }

    void clear() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        txtRemarks.setText("");
        jButton4.setText("Save");
        deletebutton.setVisible(false);
        int lastReceiptNo = 0;
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "select max(ReceiptNo) from receipt;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                lastReceiptNo = rs.getInt("max(ReceiptNo)");
            }
            con.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        jTextField1.setText(String.valueOf(lastReceiptNo + 1));
        jTextField2.requestFocus();
    }

    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            jTextField2.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            jTextField3.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 2).toString());
            pmPartyNameSuggestionsPopup.setVisible(false);
        }
    }// GEN-LAST:event_tblPartyNameSuggestionsMouseClicked

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
                suggestionsTable.addRow(new Object[] {
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                        (suggestion.get(2) == null) ? "NULL"
                                : outstandingAnalysisHelper
                                        .fillTableInDateGivenParty(String.valueOf(suggestion.get(0))), });
            } catch (ParseException ex) {
                Logger.getLogger(ReceiptScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField2KeyReleased
        fetchAccountNames();
        if (!(accountNames == null || accountNames.isEmpty())) {
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    pmPartyNameSuggestionsPopup.setVisible(false);
                    break;
                case KeyEvent.VK_ENTER:
                    jTextField5.requestFocusInWindow();
                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        pmPartyNameSuggestionsPopup.setVisible(true);
                        populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, dueamt FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + jTextField2.getText() + "%'");
                    });
                    break;
            }
        }
    }// GEN-LAST:event_jTextField2KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
        printx();
    }// GEN-LAST:event_jButton7ActionPerformed

    private void deletebuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deletebuttonActionPerformed
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
        }
    }// GEN-LAST:event_deletebuttonActionPerformed

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextField2FocusLost
        pmPartyNameSuggestionsPopup.setVisible(false); // TODO add your handling code here:
    }// GEN-LAST:event_jTextField2FocusLost

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtRemarks.requestFocusInWindow();

        }
    }// GEN-LAST:event_jTextField4KeyPressed

    private void clearbuttonKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_clearbuttonKeyPressed
        if (evt.getKeyCode() == 10) {
            jButton4.doClick();
        }
    }// GEN-LAST:event_clearbuttonKeyPressed

    private void jButton4KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jButton4KeyPressed
        if (evt.getKeyCode() == 10) {
            jButton4.doClick();
        }
    }// GEN-LAST:event_jButton4KeyPressed

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextField4FocusLost
        if (jTextField4.getText().trim().isEmpty()) {
            jTextField4.setText("0");
        } // TODO add your handling code here:
    }// GEN-LAST:event_jTextField4FocusLost

    // Variables declaration - do not modify
    public static javax.swing.JTabbedPane tpScreensHolder;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton clearbutton;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton deletebutton;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblFrameImage;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    public javax.swing.JPanel pnlParent;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblPartyNameSuggestions;
    private JTextField txtRemarks;
    private JTextField jTextField3;
    private JTextField jTextField5;
    // End of variables declaration

}
