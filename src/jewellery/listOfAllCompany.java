package jewellery;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class listOfAllCompany extends javax.swing.JFrame {

    private final DefaultTableModel tableModel;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private ImageIcon image;
    private ImageIcon imageIcon;
    private List<List<Object>> companiesData = new ArrayList<>();
    private static int selectedRow = -1;
    private static int selectedColumn = -1;
    private final JFileChooser fileChooser = new JFileChooser();
    private String selectedFile = "";

    public static String selectedCompany = "";

    public listOfAllCompany() {
        initComponents();

        
        
        

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpg");
        this.setIconImage(icon);

        tableModel = (DefaultTableModel) tblCompaniesList.getModel();

        initCloseButtonEventCode();
        lblCurrentDate.setText("DATE: " + getCurrentDate("yyyy-MM-dd"));
        centerTableCells();

        setImageOnButton(AssetsLocations.CIRCULAR_ARROW, btnRefresh);

        SwingUtilities.invokeLater(() -> {
            try {
                fetchCompaniesDataFromDatabase();
            } catch (ParseException ex) {
                Logger.getLogger(listOfAllCompany.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        setupCustomEnterKeyEventOnTable();
    }
    static Date dateFY;
    private static final SimpleDateFormat DATEFORMAT
            = new SimpleDateFormat("yyyy-MM-dd");

    private void fetchCompaniesDataFromDatabase() throws ParseException {
        // System.out.println("hello");
            int ctr = 0;
            companiesData = DBController.getDataFromcompanyTable("SELECT distinct companyname FROM company");
             Connection con = null;

            try {
                Class.forName("org.h2.Driver");
                String path = "jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
                Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, path);

                con = DriverManager.getConnection("jdbc:h2:./company/" + GLOBAL_VARS.SELECTED_COMPANY + "/" + GLOBAL_VARS.SELECTED_COMPANY_FYYEAR + "_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE",
                         "sa", "");
                

            } catch (Exception e) {
                System.out.println("inter.DBConnect.connect()");
                Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(null, e);
            }          
            

            companiesData.forEach((companies) -> {
                //ctr++;
                tableModel.addRow(new Object[]{
                   "",
                    companies.get(0), // Company name
""
                });
            });
            // dateFY = DATEFORMAT.parse( companiesData.get(0).get(2).toString());
            // System.out.println(dateFY);
        

    }

    private void setupCustomEnterKeyEventOnTable() {
        String solve = "solve";

        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        tblCompaniesList.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKeyStroke, solve);
        tblCompaniesList.getActionMap().put(solve, new EnterAction());
    }

    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(listOfAllCompany.this,
                        "Are you sure you want to close this window?", "Close Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    
                    listOfAllCompany.this.dispose();
                    System.exit(0);
                }
            }
        });
    }

    private String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();

        return dateTimeFormatter.format(localDateTime);
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblCompaniesList
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setImageOnButton(String resourceLocation, JButton button) {
        image = new ImageIcon(getClass().getResource(resourceLocation));
        imageIcon = new ImageIcon(image
                .getImage()
                .getScaledInstance(button.getWidth() - 5,
                        button.getHeight() - 5, Image.SCALE_SMOOTH));
        button.setIcon(imageIcon);
    }

    private String selectFile() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            GLOBAL_VARS.SELECTED_COMPANY = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 1).toString();
            EventQueue.invokeLater(() -> {
                new SelectAndCreateCompanyScreen().setVisible(true);
            });
            listOfAllCompany.this.dispose();
        }

    }

    class GradientColor extends javax.swing.JPanel {

        @Override
        protected void paintComponent(Graphics grphcs) {
            super.paintComponent(grphcs);
            Graphics2D g2d = (Graphics2D) grphcs;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int width = getWidth();
            int height = getHeight();

            Color color1 = new Color(236, 161, 40);
            Color color2 = new Color(112, 111, 109);

            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new GradientColor();
        lblCurrentDate = new javax.swing.JLabel();
        btnAddNewCompany = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompaniesList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("View and Create Company");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        lblCurrentDate.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblCurrentDate.setForeground(new java.awt.Color(255, 255, 255));
        lblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentDate.setText("Current Date");

        btnAddNewCompany.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnAddNewCompany.setText("Add A Company");
        btnAddNewCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewCompanyActionPerformed(evt);
            }
        });

        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        tblCompaniesList.setFont(new java.awt.Font("Bookman Old Style", 1, 16)); // NOI18N
        tblCompaniesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Company Name", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompaniesList.setRowHeight(25);
        tblCompaniesList.getTableHeader().setReorderingAllowed(false);
        tblCompaniesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCompaniesListMouseClicked(evt);
            }
        });
        tblCompaniesList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCompaniesListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompaniesList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(436, 436, 436)
                .addComponent(lblCurrentDate, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddNewCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(297, 297, 297)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCurrentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnAddNewCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblCompaniesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCompaniesListMouseClicked
        selectedRow = tblCompaniesList.getSelectedRow();
        selectedColumn = tblCompaniesList.getSelectedColumn();

        // If the mouse is double clicked on any row, only then proceed
        if (evt.getClickCount() == 2 && tblCompaniesList.getSelectedRow() != -1) {
            GLOBAL_VARS.SELECTED_COMPANY = tblCompaniesList
                    .getValueAt(tblCompaniesList.getSelectedRow(), 1).toString();
            EventQueue.invokeLater(() -> {
                new SelectAndCreateCompanyScreen().setVisible(true);
            });
            this.dispose();
        } else {
            selectedRow = tblCompaniesList.getSelectedRow();
            selectedColumn = tblCompaniesList.getSelectedColumn();
        }
    }//GEN-LAST:event_tblCompaniesListMouseClicked

    private void btnAddNewCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewCompanyActionPerformed
        new CreateNewCompanyScreen().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAddNewCompanyActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        tableModel.setRowCount(0);
        tblCompaniesList.repaint();

        SwingUtilities.invokeLater(() -> {
            try {
                fetchCompaniesDataFromDatabase();
            } catch (ParseException ex) {
                Logger.getLogger(listOfAllCompany.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void tblCompaniesListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCompaniesListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedRow = tblCompaniesList.getSelectedRow();
        }
    }//GEN-LAST:event_tblCompaniesListKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewCompany;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCurrentDate;
    private javax.swing.JTable tblCompaniesList;
    // End of variables declaration//GEN-END:variables
}
