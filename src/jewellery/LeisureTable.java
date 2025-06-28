/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jewellery.helper.ledgerClassForPrint;
import jewellery.helper.outstandingAnalysisHelper;
import jewellery.helper.tableObject;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author hp
 */
public class LeisureTable extends javax.swing.JFrame {

    /**
     * Creates new form Bill_History
     */
    private static Logger logger = Logger.getLogger(LeisureTable.class.getName());
    private final List<Object> accountNames = new ArrayList<>();
    private static List<tableObject> initialTableData = new ArrayList<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    DefaultTableModel m;
    static String status1 = "Credit Entry";
    static String status2 = "Debit Entry";
    private final DefaultTableModel partyNameSuggestionsTableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LeisureTable() {
        initComponents();
        initC();
        m = (DefaultTableModel) jTable1.getModel();
        m.setRowCount(0);
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
        pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 200, txtPartyName.getY() + 150);
    }

    private String getGroupName(String partyname) {
        String grp = null;
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select distinct grp from account where accountname='" + partyname + "'";
            ResultSet re = stmt.executeQuery(sql);
            while (re.next()) {
                grp = re.getString("grp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grp.trim();
    }

    public void OutStandingRedirect(String partyname) throws ParseException {
        txtPartyName.setText(partyname);
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "select startfinancialyear from " + DatabaseCredentials.COMPANY_TABLE + " where companyname = '"
                    + GLOBAL_VARS.SELECTED_COMPANY + "';");
            LocalDate localDate = LocalDate.now();
            Date todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(localDate.toString());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(localDate.toString());
            while (rs.next()) {
                if (rs.getObject("startfinancialyear") != null) {
                    date = rs.getDate("startfinancialyear");
                }
            }

            jDateChooser1.setDate(date);
            jDateChooser2.setDate(todayDate);
            fillTableInDateGivenParty(jDateChooser1.getDate(),
                    jDateChooser2.getDate(), txtPartyName.getText());
            con.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    void initC() {
        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Party Name", "State", "GRP"
                }) {
            boolean[] canEdit = new boolean[]{
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

        spTblPartyNameSuggestionsContainer.setViewportView(tblPartyNameSuggestions);

        setResizable(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        totalcreditlabel = new javax.swing.JLabel();
        totalcreditfield = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totaldebitfield = new javax.swing.JTextField();
        totalbalance = new javax.swing.JLabel();
        totalbalancefield = new javax.swing.JTextField();
        OpBal = new javax.swing.JTextField();
        totalcreditlabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        closebutton = new javax.swing.JButton();
        printbutton = new javax.swing.JButton();
        exportbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ledger Table");
        setBackground(new java.awt.Color(57, 68, 76));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(57, 68, 76));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Date", "Type", "Party Name", "Remark", "Credit", "Debit", "Balance"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setRowHeight(25);
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel()
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("Date From:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("Party Name :");

        txtPartyName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyReleased(evt);
            }
        });

        jDateChooser1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        jDateChooser2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jDateChooser2MousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jDateChooser2MouseReleased(evt);
            }
        });
        jDateChooser2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser2KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Date To:");

        totalcreditlabel.setBackground(new java.awt.Color(57, 68, 76));
        totalcreditlabel.setForeground(new java.awt.Color(238, 188, 81));
        totalcreditlabel.setText("Op.Bal :");

        totalcreditfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcreditfieldActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Debit :");

        totaldebitfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaldebitfieldActionPerformed(evt);
            }
        });

        totalbalance.setForeground(new java.awt.Color(238, 188, 81));
        totalbalance.setText("Balance :");

        totalbalancefield.setToolTipText("");
        totalbalancefield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalbalancefieldActionPerformed(evt);
            }
        });

        OpBal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpBalActionPerformed(evt);
            }
        });

        totalcreditlabel1.setForeground(new java.awt.Color(238, 188, 81));
        totalcreditlabel1.setText("Credit :");

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        jLabel6.setText("SEARCH");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jLabel6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLabel6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtPartyName)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 160,
                                                Short.MAX_VALUE))
                                .addGap(30, 30, 30)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(totalcreditlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpBal, javax.swing.GroupLayout.PREFERRED_SIZE, 188,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(totalcreditlabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalcreditfield, javax.swing.GroupLayout.PREFERRED_SIZE, 173,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totaldebitfield, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(totalbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 63,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalbalancefield, javax.swing.GroupLayout.PREFERRED_SIZE, 145,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(174, 174, 174))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1058,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtPartyName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jDateChooser1,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jDateChooser2,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totalcreditfield, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totalcreditlabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(OpBal, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totalcreditlabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totaldebitfield, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(totalbalancefield, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(totalbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(14, 14, 14)));

        jLabel1.setBackground(new java.awt.Color(57, 68, 76));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("Ledger Table");

        closebutton.setBackground(new java.awt.Color(255, 0, 0));
        closebutton.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        closebutton.setText("Close");
        closebutton.setPreferredSize(new java.awt.Dimension(70, 24));
        closebutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closebuttonMouseClicked(evt);
            }
        });
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        printbutton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        printbutton.setText("Print");
        printbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printbuttonMouseClicked(evt);
            }
        });

        exportbutton.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        exportbutton.setText("Export");
        exportbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportbuttonMouseClicked(evt);
            }
        });
        exportbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1079,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jLabel1)
                                .addGap(135, 135, 135)
                                .addComponent(printbutton)
                                .addGap(37, 37, 37)
                                .addComponent(exportbutton)
                                .addGap(168, 168, 168)
                                .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 70,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(exportbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void populatenameSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        suggestionsTable.setRowCount(0);

        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),});
        });

    }

    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            pmPartyNameSuggestionsPopup.setVisible(false);
        }
    }

    public void filter1(String Query) {

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(rowSorter);

        if (Query.length() != 0) {
            rowSorter.setRowFilter(RowFilter.regexFilter(Query));
        }
    }

    public void filter2(String Query) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        jTable1.setRowSorter(tr);

        tr.setRowFilter(RowFilter.regexFilter("(?i)" + Query, 0));
    }

    private void fetchAccountNames(String accountType) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> account_names = DBController.executeQuery("SELECT accountname FROM "
                + DatabaseCredentials.ACCOUNT_TABLE + " WHERE dr_cr = " + "'" + accountType + "'");

        account_names.forEach((accountName) -> {
            accountNames.add(accountName.toString());
        });
    }

    public void filter3(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String edate = sdf.format(endDate);
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
        c1.add(Calendar.DATE, -1);
        startDate = c1.getTime();

        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        filters.add(RowFilter.dateFilter(ComparisonType.AFTER, startDate));
        filters.add(RowFilter.dateFilter(ComparisonType.BEFORE, endDate));

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        jTable1.setRowSorter(tr);
        RowFilter<Object, Object> rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
            int row = jTable1.getSelectedRow();
            String type = jTable1.getValueAt(row, 1).toString();
            if (type.equals("Sale")) {
                String recp = m.getValueAt(row, 3).toString();
                String billNo = recp.substring(8, recp.indexOf(","));
                // JOptionPane.showMessageDialog(this, billNo);
                SaleScreen sc = new SaleScreen();

                try {
                    sc.saleRegisterRedirect(Integer.parseInt(billNo));
                } catch (SQLException ex) {
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                sc.setVisible(true);
                sc.closebtn.setVisible(false);
            } else if (type.equals("Payment")) {
                String value = m.getValueAt(row, 3).toString();
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
            } else if (type.trim().equalsIgnoreCase("withdraw") || type.trim().equalsIgnoreCase("deposit")) {
                String value = m.getValueAt(row, 3).toString();
                int end = 9;
                while (end < value.length()) {
                    if (value.charAt(end) == ',') {
                        break;
                    }
                    end++;
                }
                String selectedReceiptNumber = value.substring(9, end);
                BankGroup bg = null;
                try {
                    bg = new BankGroup();
                } catch (Exception ex) {
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                bg.setVisible(true);
                bg.fillByRedirect(selectedReceiptNumber);

            } else if (type.trim().equals("Purchase")) {
                String value = m.getValueAt(row, 3).toString();
                String recp = value.substring(9, value.length());
                // JOptionPane.showMessageDialog(this,recp);
                PurchaseScreen sc = new PurchaseScreen();
                sc.sale_Bill(Integer.parseInt(recp));
                sc.setVisible(true);

                // nothing write
            } else { // receipt
                String value = m.getValueAt(row, 3).toString();
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
                    Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                rc.setVisible(true);
                rc.fillByRedirect(selectedReceiptNumber);
            }
        }
    }// GEN-LAST:event_jTable1MouseClicked

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

    int selectedrow = 0;

    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtPartyNameKeyReleased
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
                            tblPartyNameSuggestions.setRowSelectionInterval(
                                    tblPartyNameSuggestions.getSelectedRow() + 1,
                                    tblPartyNameSuggestions.getSelectedRow() + 1);
                        }
                    }
                    txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                case KeyEvent.VK_UP:
                    tblPartyNameSuggestions.requestFocus();

                    if (tblPartyNameSuggestions.getSelectedRow() > 0) {
                        tblPartyNameSuggestions.setRowSelectionInterval(tblPartyNameSuggestions.getSelectedRow() - 1,
                                tblPartyNameSuggestions.getSelectedRow() - 1);
                    }

                    txtPartyName.setText(tblPartyNameSuggestions.getValueAt(tblPartyNameSuggestions.getSelectedRow(), 0)
                            .toString().trim());

                    break;
                case KeyEvent.VK_ENTER:
                    pmPartyNameSuggestionsPopup.setVisible(false);
                    break;
                default:
                    EventQueue.invokeLater(() -> {
                        pmPartyNameSuggestionsPopup.setVisible(true);
                        populatenameSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'");
                    });
                    break;
            }
        }
    }// GEN-LAST:event_txtPartyNameKeyReleased

    private void populateSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
        String status1 = "Credit";
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        suggestionsTable.setRowCount(0);

        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                0,
                status1, // (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),
            // (suggestion.get(3) == null) ? "NULL" : suggestion.get(4),
            });
        });
    }

    private void populateSuggestionsTable1FromDatabase(DefaultTableModel suggestionsTable, String query) {
        String status2 = "Debit";
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        // suggestionsTable.setRowCount(0);
        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                0,
                (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                status2, // (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),
            // (suggestion.get(3) == null) ? "NULL" : suggestion.get(4),
            });
        });
    }

    private void jDateChooser2KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jDateChooser2KeyReleased

    }// GEN-LAST:event_jDateChooser2KeyReleased

    private void jDateChooser2MousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jDateChooser2MousePressed

    }// GEN-LAST:event_jDateChooser2MousePressed

    private void jDateChooser2MouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jDateChooser2MouseReleased

    }// GEN-LAST:event_jDateChooser2MouseReleased

    private void fillTableInDateGivenParty(Date start, Date to, String party) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        start = formatter.parse(formatter.format(start));
        to = formatter.parse(formatter.format(to));

        try {

            m.setRowCount(0);
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            String query = "select distinct(bill) from sales where partyname = '" + party + "'";
            if (!party.equals("Cash")) {
                query += " ";
            }
            if (party.equals("Cash")) {
                query = "select distinct(bill) from sales where terms='Cash'";
            }

            ResultSet rs = stmt.executeQuery(query);
            DecimalFormat df = new DecimalFormat("0.00");
            while (rs.next()) {
                Statement st = con.createStatement();
                int billno = rs.getInt("bill");
                query = "select sum(netamount),date,partyname,receivedamount,terms from sales where bill=" + billno;
                // query = "select sum(sales.netamount),sales.date,sales.partyname from sales
                // join receipt on sales.bill=receipt.sales_Bill where not receipt.sales_Bill="
                // + billno;

                ResultSet rest = st.executeQuery(query);
                Date date = new Date();
                String partyName = "";
                double exchangeAmt = 0;
                double netamount = 0;
                double receivedamt = 0;
                String terms = "";
                while (rest.next()) {
                    date = rest.getDate("date");
                    netamount = rest.getDouble("sum(netamount)");
                    partyName = rest.getString("partyname");
                    receivedamt = rest.getDouble("receivedamount");
                    terms = rest.getString("terms");

                    // date = rest.getDate("sales.date");
                    // netamount = rest.getDouble("sum(sales.netamount)");
                    // partyName=rest.getString("sales.partyname");
                }
                rest.close();
                st.close();
                String q = "select total from exchange where bill='" + billno + "'";
                Statement stmtexchange = con.createStatement();
                ResultSet rex = stmtexchange.executeQuery(q);
                while (rex.next()) {
                    exchangeAmt = rex.getDouble(1);
                }
                rex.close();
                stmtexchange.close();
                String remark = "Bill No." + billno;
                if (party.equals("Cash")) {
                    Statement stmtt = con.createStatement();
                    Statement stmttt = con.createStatement();
                    ResultSet r = stmtt.executeQuery("select sales_Bill from receipt where sales_Bill=" + billno + "");
                    ResultSet r2 = stmttt
                            .executeQuery("select sales_Bill from bankledger where sales_Bill=" + billno + "");
                    if (!r.next() && !r2.next()) {
                        // JOptionPane.showMessageDialog(this, "running sales1");
                        if (netamount != 0) {
                            // JOptionPane.showMessageDialog(this, remark);

                            tableObject tableObj = new tableObject(date, partyName, 0.0,
                                    (int) netamount, 1, remark + "," + netamount, "Sale");
                            initialTableData.add(tableObj);
                        }
                    }
                    r.close();
                    stmtt.close();
                } else {

                    if (netamount != 0) {
                        // JOptionPane.showMessageDialog(this, remark);
                        if (terms.trim().equalsIgnoreCase("Cash")) {
                            Statement stmtt = con.createStatement();
                            Statement stmttt = con.createStatement();
                            ResultSet r = stmtt.executeQuery(
                                    "select sales_Bill,amtpaid from receipt where sales_Bill='" + billno + "'");
                            ResultSet r2 = stmttt
                                    .executeQuery("select sales_Bill,SUM(amt) as am from bankledger where sales_Bill='"
                                            + billno + "' group by sales_Bill");
                            double money1 = 0;
                            double money2 = 0.0;
                            while (r.next()) {
                                money1 = r.getDouble(2);
                            }
                            while (r2.next()) {
                                money2 = r2.getDouble(2);
                            }
                            r2.close();
                            r.close();
                            stmtt.close();
                            stmttt.close();
                            tableObject tableObj = new tableObject(date, partyName, 0.0, netamount, 1,
                                    remark + ", " + (netamount), "Sale");
                            //  initialTableData.add(tableObj);
                            if (money1 != 0 && money2 != 0) {
                                tableObject tableObj1 = new tableObject(date, partyName, 0.0,
                                        Double.parseDouble(df.format((int) netamount - money1 - money2 - exchangeAmt)),
                                        1, remark + "," + netamount, "Sale ");
                                initialTableData.add(tableObj1);
                            }
                        } else {
                            tableObject tableObj = new tableObject(date, partyName, 0.0,
                                    netamount, 1, remark + "," + netamount, "Sale");
                            initialTableData.add(tableObj);
                        }

                    }
                }

            }
            con.close();
            stmt.close();
            rs.close();

            query = "select * from receipt where Name = '" + party + "' and not Receiptno='-1';";
            if (party.equals("Cash")) {
                query = "select * from receipt ";
            }
            if (getGroupName(party).equals("Bank")) {
                query = "select * from receipt where mop='" + party + "' ";
            }
            ResultSet rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");
                String mop = rs1.getString("mop");
                int salesbill = rs1.getInt("sales_Bill");
                String remark = null;
                if (!getGroupName(mop).equals("Bank") && party.equals("Cash")) {
                    if (salesbill > -1) {
                        remark = "Bill No." + String.valueOf(salesbill);
                        // hjg
                        // remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " +
                        // rs1.getString("mop");
                        tableObject tableObj1 = new tableObject(date, name, 0, outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)), 1,
                                remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)),
                                "Sale ");
                        initialTableData.add(tableObj1);
                        // tableObject tableObj = new tableObject(date, name, 0, amount, 1,
                        //         remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)),
                        //         "Sale ch7");
                        //  initialTableData.add(tableObj);
                    } else {
                        remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                        if (name.equals("Cash")) {
                            tableObject tableObj = new tableObject(date, name, 0.0, amount, 0, remark, "Receipt");
                            initialTableData.add(tableObj);
                        } else {
                            tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Receipt");
                            initialTableData.add(tableObj);
                        }

                    }
                } else if (!party.equals("Cash") && getGroupName(party).equals("Bank")) {
                    remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Receipt");
                    initialTableData.add(tableObj);
                } else {

                    remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Receipt");
                    initialTableData.add(tableObj);

                }
            }

            c.close();
            s.close();
            rs1.close();

            query = "select * from bankledger where name = '" + party + "';";

            c = DBConnect.connect();
            s = c.createStatement();

            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("name");
                double amount = rs1.getDouble("amt");
                Date date = rs1.getDate("date");
                String remark = "Rcpt. No." + String.valueOf(rs1.getInt("rno")) + ", ";
                int salesbill = rs1.getInt("sales_Bill");
                String type = rs1.getString("type");
                tableObject tableObj;
                if (salesbill == -1) {
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "withdraw");
                        initialTableData.add(tableObj);
                    } else {
                        tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "deposit");
                        initialTableData.add(tableObj);
                    }
                }

            }

            s.clearBatch();

            query = "select * from bankledger where bankname = '" + party + "' order by sales_Bill;";
            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("bankname");
                double amount = rs1.getDouble("amt");
                Date date = rs1.getDate("date");
                String remark = "Rcpt. No." + String.valueOf(rs1.getInt("rno")) + ", ";
                int salesbill = rs1.getInt("sales_Bill");
                String type = rs1.getString("type");
                tableObject tableObj;
                if (salesbill > -1) {
                    remark = "Bill No. " + String.valueOf(salesbill);
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, 0.0, amount, 1,
                                remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)),
                                "Sale");

                    } else {
                        tableObj = new tableObject(date, name, amount, 0.0, 0,
                                remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)),
                                "Sale");
                    }
                } else {
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Deposit");

                    } else {
                        tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "withdraw");
                    }
                }
                initialTableData.add(tableObj);
            }

            c.close();
            s.close();
            rs1.close();

            c = DBConnect.connect();
            s = c.createStatement();

            query = "select * from payments where  Name = '" + party + "' or mop='" + party + "';";
            if (party.equals("Cash")) {
                query = "select * from payments where mop = 'Cash';";
            }
            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");

                String remark = null;
                remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                // tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark,
                // "Payment");
                // if (getGroupName(party).equals("Bank") ||
                // getGroupName(party).equals("Customer")) {
                ////                   JOptionPane.showMessageDialog(this, party+ "  running bank");
                // tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark,
                // "Payment");
                // initialTableData.add(tableObj);
                // } else {
                if (name.equals("Cash")) {
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 1, remark, "Payment");
                    initialTableData.add(tableObj);
                } else {
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Payment");
                    initialTableData.add(tableObj);
                }

                // }
            }

            c.close();
            s.close();
            rs1.close();

            c = DBConnect.connect();
            s = c.createStatement();

            query = "select distinct(bill) from purchasehistory where partyname = '" + party + "'";
            if (!party.equals("Cash")) {
                query = "select distinct(bill) from purchasehistory where partyname = '" + party + "'";
                query += " and  terms = 'Credit'";
                // JOptionPane.showMessageDialog(this, query);

            } else {
                query = "select distinct(bill) from purchasehistory where ";
                // notjing write previous coder
                query += "  terms = 'Cash'";
            }

            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                Statement st = c.createStatement();
                int billno = rs1.getInt("bill");
                query = "select sum(Cast(net_amount as decimal(10,2))) as nt,date from purchasehistory where bill='"
                        + billno + "'";

                ResultSet rest = st.executeQuery(query);
                Date date = new Date();
                double netamount = 0;
                while (rest.next()) {
                    date = rest.getDate("date");
                    netamount = rest.getDouble("nt");
                }
                rest.close();
                st.close();

                String remark = "Bill. No." + billno;
                tableObject tableObj = new tableObject(date, party, netamount, 0.0, 0, remark, "Purchase");
                initialTableData.add(tableObj);
            }

            c.close();
            s.close();
            rs1.close();

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

            double balance = 0.0;
            double credit = 0.0, debit = 0.0;
            Connection newCon = DBConnect.connect();
            Statement st = newCon.createStatement();
            ResultSet res = st.executeQuery("SELECT opbal FROM account where accountname = '" + party + "';");
            while (res.next()) {
                if (res.getObject("opbal") != null) {
                    balance += res.getDouble("opbal");
                }
            }
            OpBal.setText(String.valueOf(balance));
            newCon.close();
            st.close();
            res.close();

            for (tableObject i : initialTableData) {

                balance += i.debit - i.credit;
                if ((i.date.compareTo(start) >= 0 || i.date.after(start))
                        && (i.date.compareTo(to) <= 0)) {
                    credit += i.credit;
                    debit += i.debit;
                    if (i.creditOrDebit == 0) // credit
                    {
                        if (balance <= 0) {
                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, (int) i.debit,
                                String.format("%.2f", balance) + " Cr"});
                        } else {
                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, (int) i.debit,
                                String.format("%.2f", balance) + " Dr"});
                        }

                    } else {
                        if (balance <= 0) {
                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, (int) i.debit,
                                String.format("%.2f", balance) + " Cr"});
                        } else {
                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, (int) i.debit,
                                String.format("%.2f", balance) + " Dr"});
                        }
                    }
                }
            }
            if (balance < 0) {
                totalbalancefield.setText(String.format("%.2f", -balance) + " Cr");
            } else {
                totalbalancefield.setText(String.format("%.2f", balance) + " Dr");
            }
            if (balance - (debit - credit) < 0) {
                OpBal.setText(String.format("%.2f", -(balance - (debit - credit))) + " Cr");
            } else {
                OpBal.setText(String.format("%.2f", balance - (debit - credit)) + " Dr");
            }

            totalcreditfield.setText(String.format("%.2f", credit));
            totaldebitfield.setText(String.format("%.2f", debit));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.LEFT);
            for (int g = 0; g < jTable1.getColumnCount(); g++) {
                jTable1.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }

        } catch (SQLException e) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }

    }

    private List<ledgerClassForPrint> fatchdataFromGivenDateForJasperReport() {
        double balance = 0;
        List<ledgerClassForPrint> data = new ArrayList<>();
        for (tableObject i : initialTableData) {

            balance += i.debit - i.credit;
            if ((i.date.compareTo(jDateChooser1.getDate()) >= 0 || i.date.after(jDateChooser1.getDate()))
                    && (i.date.compareTo(jDateChooser2.getDate()) <= 0)) {
                if (i.creditOrDebit == 0) // credit
                {
                    if (balance <= 0) {
                        data.add(new ledgerClassForPrint(jDateChooser1.getDate(), jDateChooser2.getDate(), i.name,
                                i.remark, String.format("%.2f", -balance) + " Cr", i.date, i.credit, i.debit, i.type));
                        // m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit,
                        // String.format("%.2f", -balance) + " Cr"});
                    } else {
                        data.add(new ledgerClassForPrint(jDateChooser1.getDate(), jDateChooser2.getDate(), i.name,
                                i.remark, String.format("%.2f", balance) + " Dr", i.date, i.credit, i.debit, i.type));
                        // m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit,
                        // String.format("%.2f", balance) + " Dr"});
                    }

                } else {
                    if (balance <= 0) {
                        data.add(new ledgerClassForPrint(jDateChooser1.getDate(), jDateChooser2.getDate(), i.name,
                                i.remark, String.format("%.2f", -balance) + " Cr", i.date, i.credit, i.debit, i.type));
                        // m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit,
                        // String.format("%.2f", -balance) + " Cr"});
                    } else {
                        data.add(new ledgerClassForPrint(jDateChooser1.getDate(), jDateChooser2.getDate(), i.name,
                                i.remark, String.format("%.2f", balance) + " Dr", i.date, i.credit, i.debit, i.type));
                        // m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit,
                        // String.format("%.2f", balance) + " Dr"});
                    }
                }
            }
        }
        return data;
    }

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_closebuttonActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_closebuttonActionPerformed

    private void exportbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportbuttonActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_exportbuttonActionPerformed

    @SuppressWarnings("unchecked")
    private void jasperPrint() throws FileNotFoundException {

        try {

            List<ledgerClassForPrint> partydatasource = fatchdataFromGivenDateForJasperReport();
            InputStream input2 = new FileInputStream(
                    new File("src" + File.separator + "jasper_reports" + File.separator + "ledger.jrxml"));

            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(partydatasource);
            HashMap map = new HashMap();
            map.put("name", "testyiong");
            JasperReport jreport = JasperCompileManager.compileReport(input2);
            @SuppressWarnings("unchecked")
            JasperPrint jprint = JasperFillManager.fillReport(jreport, map, datasource);
            JasperViewer.viewReport(jprint, false);

        } catch (Exception e) {
            Logger.getLogger(LeisureTable.class
                    .getName()).log(Level.SEVERE, null, e);

        }
    }

    private void printbuttonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_printbuttonMouseClicked
        try {
            // TODO add your handling code here:

            jasperPrint();

            // PrinterJob job = PrinterJob.getPrinterJob();
            // job.setJobName("Print Data");
            //
            // job.setPrintable(new Printable() {
            // public int print(Graphics pg, PageFormat pf, int pageNum) {
            // pf.setOrientation(PageFormat.LANDSCAPE);
            // if (pageNum > 0) {
            // return Printable.NO_SUCH_PAGE;
            // }
            //
            // Graphics2D g2 = (Graphics2D) pg;
            // g2.translate(pf.getImageableX(), pf.getImageableY());
            // g2.scale(0.55, 1.0);
            //
            // jPanel1.print(g2);
            //
            // return Printable.PAGE_EXISTS;
            //
            // }
            // });
            // boolean ok = job.printDialog();
            // if (ok) {
            // try {
            // job.print();
         ////                logger.info("reached");
            // } catch (PrinterException ex) {
            // ex.printStackTrace();
            // }
            // }


} catch (FileNotFoundException ex) {
            Logger.getLogger(LeisureTable.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }// GEN-LAST:event_printbuttonMouseClicked

    public void exportToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        TreeMap<String, Object[]> map = new TreeMap<>();

        map.put("0",
                new Object[]{model.getColumnName(0), model.getColumnName(1), model.getColumnName(2),
                    model.getColumnName(3),
                    model.getColumnName(4), model.getColumnName(5)});

        for (int i = 1; i < model.getRowCount(); i++) {
            map.put(Integer.toString(i),
                    new Object[]{model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2),
                        model.getValueAt(i, 3), model.getValueAt(i, 4), model.getValueAt(i, 5),
                        model.getValueAt(i, 6),});
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

            try (FileOutputStream fos = new FileOutputStream(new File(paths));) {

                wb.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "file exported succesfully:" + paths);
    }

    String paths = "";

    private void exportbuttonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_exportbuttonMouseClicked
        // TODO add your handling code here:

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(this);
        // File saveFile = JFileChooser.getSelectedFile();
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

    }// GEN-LAST:event_exportbuttonMouseClicked

    private void closebuttonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_closebuttonMouseClicked
        // TODO add your handling code here:

        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

        dispose();

    }// GEN-LAST:event_closebuttonMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }// GEN-LAST:event_jPanel1MouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }// GEN-LAST:event_formMouseClicked

    private void formFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_formFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_formFocusLost

    private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }// GEN-LAST:event_formWindowClosed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }// GEN-LAST:event_jScrollPane1MouseClicked

    private void totalcreditfieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_totalcreditfieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_totalcreditfieldActionPerformed

    private void txtPartyNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtPartyNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtPartyNameActionPerformed

    private void totalbalancefieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_totalbalancefieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_totalbalancefieldActionPerformed

    private void totaldebitfieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_totaldebitfieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_totaldebitfieldActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        initialTableData.clear();
        try {
            fillTableInDateGivenParty(jDateChooser1.getDate(),
                    jDateChooser2.getDate(), txtPartyName.getText());
        } catch (ParseException ex) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// GEN-LAST:event_jLabel6MouseClicked

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtPartyNameFocusGained
        selectedrow = 0; // TODO add your handling code here:
    }// GEN-LAST:event_txtPartyNameFocusGained

    private void OpBalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_OpBalActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_OpBalActionPerformed

    private void jLabel6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jLabel6ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel6ActionPerformed

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtPartyNameFocusLost
        pmPartyNameSuggestionsPopup.setVisible(false);
    }// GEN-LAST:event_txtPartyNameFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LeisureTable.class
                    .getName()).log(java.util.logging.Level.SEVERE, null,
                            ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeisureTable.class
                    .getName()).log(java.util.logging.Level.SEVERE, null,
                            ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeisureTable.class
                    .getName()).log(java.util.logging.Level.SEVERE, null,
                            ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeisureTable.class
                    .getName()).log(java.util.logging.Level.SEVERE, null,
                            ex);
        }
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LeisureTable().setVisible(true);
            }
        });
    }

    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    private javax.swing.JTable tblPartyNameSuggestions;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField OpBal;
    private javax.swing.JButton closebutton;
    private javax.swing.JButton exportbutton;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton printbutton;
    private javax.swing.JLabel totalbalance;
    private javax.swing.JTextField totalbalancefield;
    private javax.swing.JTextField totalcreditfield;
    private javax.swing.JLabel totalcreditlabel;
    private javax.swing.JLabel totalcreditlabel1;
    private javax.swing.JTextField totaldebitfield;
    private javax.swing.JTextField txtPartyName;
    // End of variables declaration//GEN-END:variables
}
