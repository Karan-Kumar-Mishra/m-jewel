/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import jewellery.helper.RealSettingsHelper;
import jewellery.InsertLoanEntry;
import jewellery.LoanBook;
import jewellery.LoanReceipt;
import javax.swing.JPanel;

/**
 *
 * @author SACHIN MISHRA
 */
public class DashBoardScreen extends javax.swing.JFrame {

    /**
     * Creates new form DashBoardScreen
     */
    private ImageIcon image;
    private final int buttonImageWidth = 25;
    private int buttonImageHeight = 25;

    public SaleScreen sc;
    public static boolean isCreateCompanyWindowOpen = false;

    private final DebugAndLogsScreen debugAndLogsScreen = new DebugAndLogsScreen();

    public DashBoardScreen() {
        sc = new SaleScreen();

        initComponents();
        // In constructor after initComponents():
        setIconsOnButtons();
        Logger.getLogger(DashBoardScreen.class.getName()).log(Level.INFO, "Dashboard Screen Creation Started!");
        maximizeWindow();
        buttonImageHeight = btndailyUpdate.getHeight() - 20;
        getuser();
        if (RealSettingsHelper.gettagNoIsTrue()) {
            jRadioButtonMenuItem1.setSelected(false);
        } else {
            jRadioButtonMenuItem1.setSelected(false);
        }
        initCloseButtonEventCode();
        versioning v = new versioning();
        String ver = v.version();
        String update = v.lastupdate();
        versioning.setText(ver);
        BildDate.setText(update);
        newtimer();

//        tabbedPane = new JTabbedPane();
//        tabbedPane.setUI(new CustomTabbedPaneUI());
        tabbedPane.addTab("home", new DailyUpdatesScreen().getContentPane());

        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTabs();
            }
        };
        getRootPane().registerKeyboardAction(a, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    private ImageIcon getResizedImageIcon(String resourceLocation, int width, int height) {
        image = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation)).getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return image;
    }

    private void setIconsOnButtons() {
        btndailyUpdate.setIcon(getResizedImageIcon(AssetsLocations.UPDATE_ICON,
                buttonImageWidth, buttonImageHeight));

        btnAccount.setIcon(getResizedImageIcon(AssetsLocations.PROFILE_ICON,
                buttonImageWidth, buttonImageHeight));
        btnReceipt.setIcon(getResizedImageIcon(AssetsLocations.RECEIPT_ICON,
                buttonImageWidth, buttonImageHeight));
        btnItem.setIcon(getResizedImageIcon(AssetsLocations.AMULET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnpurcse.setIcon(getResizedImageIcon(AssetsLocations.COIN_ICON,
                buttonImageWidth, buttonImageHeight));
        btnSale.setIcon(getResizedImageIcon(AssetsLocations.DOLLAR_HAND_ICON,
                buttonImageWidth, buttonImageHeight));

        btnledger.setIcon(getResizedImageIcon(AssetsLocations.DOLLAR_HAND_ICON,
                buttonImageWidth, buttonImageHeight));
        btnpayment.setIcon(getResizedImageIcon(AssetsLocations.MONEY_WALLET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnOutstanding.setIcon(getResizedImageIcon(AssetsLocations.MONEY_WALLET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnSaleRegister.setIcon(getResizedImageIcon(AssetsLocations.REGISTER_ICON,
                buttonImageWidth, buttonImageHeight));
        btnDayBook.setIcon(getResizedImageIcon(AssetsLocations.OPEN_BOOK_ICON,
                buttonImageWidth, buttonImageHeight));
        btnPurchaseRegister.setIcon(getResizedImageIcon(AssetsLocations.RECEIPT_REGISTER_ICON,
                buttonImageWidth, buttonImageHeight));
        stockreport.setIcon(getResizedImageIcon(AssetsLocations.ITEM_STOCK_ICON,
                buttonImageWidth, buttonImageHeight));
        jButton1.setIcon(getResizedImageIcon(AssetsLocations.ITEM_WISE_STOCK_ICON,
                buttonImageWidth, buttonImageHeight));

    }

//    private class CustomTabbedPaneUI extends BasicTabbedPaneUI {
//        @Override
//        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
//            if (isSelected) {
//                g.setColor(Color.GREEN);
//            } else {
//                g.setColor(Color.LIGHT_GRAY); 
//            }
//            g.fillRect(x, y, w, h);
//        }
//    }
    void newtimer() {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (checklicense()) {
//                    disableHomepage();
//                    license();
//                    timer.cancel();
                }
            }
        }, 0, 60000);//wait 0 ms before doing the action and do it evry 60000ms (60second)

    }

    public void disableHomepage() {
        System.out.print("Disabled home");
        jMenu1.setEnabled(false);
        jMenu2.setEnabled(false);
        jMenu3.setEnabled(false);
        jMenu4.setEnabled(false);

        btnAccount.setEnabled(false);
        btnDayBook.setEnabled(false);
        btnItem.setEnabled(false);
        btnOutstanding.setEnabled(false);
        btnpurcse.setEnabled(false);
        btnSale.setEnabled(false);
        btnledger.setEnabled(false);
        btndailyUpdate.setEnabled(false);
        btnReceipt.setEnabled(false);
        btnpayment.setEnabled(false);
        btnSaleRegister.setEnabled(false);
        btnDayBook.setEnabled(false);
        btnPurchaseRegister.setEnabled(false);

    }

    private boolean checklicense() {
        int ctr = 0;
        String s1 = null, s2 = null, id = null, s3 = null, date_till = null;
        try {

            Connection c = DBConnect.connect();
            Statement s = c.createStatement();

            String qry = "SELECT license_id From licence LIMIT 1 ";
            ResultSet rs2 = s.executeQuery(qry);
            if (rs2.next()) {
                ctr++;
            }
            if (ctr == 0) {
                DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String date = dtf.format(now);

                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DATE, 7);
                Date currentDatePlusplan = cal.getTime();
                String active_till = dtf.format(currentDatePlusplan);

                String sql = "INSERT INTO licence VALUES ('default', 'default', 7, 'day','" + date + "','" + active_till + "')";
                s.executeUpdate(sql);
                return false;
            }

            String q = "Select * from licence;";

            ResultSet rs = s.executeQuery(q);
            while (rs.next()) {
                s1 = rs.getString("license_id");
                s2 = rs.getString("license_key");
                s3 = rs.getString("active_till");
                //s3=s3.substring(0, s3.indexOf('.'));
            }
            if (s1.equals("default")) {
                DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                Date date2 = null;

                try {
                    date2 = dtf.parse(s3);
                } catch (ParseException ex) {
                    Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (now.before(date2)) {
                    return false;
                } else {
                    return true;
                }
            }
            Connection c1 = null;
            try {
                c1 = DriverManager.getConnection("jdbc:mysql://69.57.172.244:3306/mindstein_licence",
                        "mindstein_licence", "v9qk@ZYFK$4u");
            } catch (Exception ex) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, "system is offline");

            }
            if (c1 != null) {
                System.out.println("Connection done");
                Statement st = c1.createStatement();
                String q1 = "Select * from license_act_comp_reqs where system_id='" + s1 + "';";
//            JOptionPane.showMessageDialog(this,s1);
                ResultSet rs1 = st.executeQuery(q1);
                while (rs1.next()) {
                    id = rs1.getString("activation_key");
                    date_till = rs1.getString("active_till");
//                date_till = date_till.substring(0, date_till.indexOf('.'));
                }
            } else {
                id = s2;
                date_till = s3;
            }
            s.close();
            c.close();
        } catch (SQLException e) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, "system is offline");

//            
        }
//        JOptionPane.showMessageDialog(this, date_till);
        if (s1 == null || s2 == null || s3 == null) {
            return true;
        }
        DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date date2 = null;

        try {
            date2 = dtf.parse(s3);
        } catch (ParseException ex) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (date_till != null && date_till.equals(s3)) {
            if (s2.equals(id) && now.before(date2)) {
                return false;
            }
        }
        return true;
    }

    public void license() {
        License_form ob = new License_form();
//        pnlLicence.removeAll();
//        pnlLicence.add(ob.jPanel1);
//        tpScreensHolder.setSelectedIndex(12);
    }

    public void license_accepted() {

        License_accepted ob = new License_accepted();
//        pnlLicence.removeAll();
//        pnlLicence.add(ob.jPanel1);
//        tpScreensHolder.setSelectedIndex(11);
    }

    private void maximizeWindow() {
        GraphicsEnvironment env
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(DashBoardScreen.this,
                        "Are you sure you want to close jewelry system", "Close Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    if (DBController.isDatabaseConnected()) {
                        DBController.closeDatabaseConnection();
                    }
                    System.exit(0);
                }
            }
        });
    }

    void getuser() {
        try {
            int userid = GLOBAL_VARS.userid;
            String name = "Welcome, ";
            Connection c = DBConnect.connectCopy();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select name from users where userid='" + userid + "';");
            while (rs.next()) {
                name = name + rs.getString("name");
            }
//            UserName.removeAll();
//            UserName.setText(name);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void ifScreenExistsOrNot() {
        int index = tabbedPane.indexOfTab("Account");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            AccountsListScreen ac = new AccountsListScreen();
            tabbedPane.addTab("Account", ac.getContentPane());
            tabbedPane.setSelectedComponent(ac.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
    }

    public void removeTabs() {
        try {

            if (tabbedPane.getSelectedIndex() >= 1) {
                tabbedPane.remove(tabbedPane.getSelectedComponent());
                System.out.println(tabbedPane.getSelectedIndex());
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        btndailyUpdate = new javax.swing.JButton();
        btnAccount = new javax.swing.JButton();
        btnItem = new javax.swing.JButton();
        btnSale = new javax.swing.JButton();
        btnReceipt = new javax.swing.JButton();
        btnpurcse = new javax.swing.JButton();
        btnpayment = new javax.swing.JButton();
        btnledger = new javax.swing.JButton();
        btnOutstanding = new javax.swing.JButton();
        btnDayBook = new javax.swing.JButton();
        btnSaleRegister = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        versioning = new javax.swing.JLabel();
        BildDate = new javax.swing.JLabel();
        btnPurchaseRegister = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        stockreport = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuitemCompanyProfile = new javax.swing.JMenuItem();
        menuitemUsers = new javax.swing.JMenuItem();
        MenuItemSttings = new javax.swing.JMenuItem();
        MenuItemCloseCompany = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemOutstanding1 = new javax.swing.JMenuItem();
        jMenuItemOutstanding2 = new javax.swing.JMenuItem();
        jMenuItemOutstanding3 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemReceiptHistory = new javax.swing.JMenuItem();
        jMenuItemPaymentHistory = new javax.swing.JMenuItem();
        jMenuItemSaleRegister = new javax.swing.JMenuItem();
        jMenuItemPurchaseRegister = new javax.swing.JMenuItem();
        jMenuItemExchangeReport = new javax.swing.JMenuItem();
        jMenuItemStockReport = new javax.swing.JMenuItem();
        jMenuItemTqagPrinting = new javax.swing.JMenuItem();
        jMenuItemLedger = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        Gst1 = new javax.swing.JMenuItem();
        Gst2 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItemOutstanding = new javax.swing.JMenuItem();
        jMenuItemOutstanding4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(57, 68, 76));

        jPanel1.setBackground(new java.awt.Color(238, 188, 81));

        btndailyUpdate.setText("Daily Updates");
        btndailyUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btndailyUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btndailyUpdate.setIconTextGap(20);
        btndailyUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btndailyUpdateMouseClicked(evt);
            }
        });
        btndailyUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndailyUpdateActionPerformed(evt);
            }
        });

        btnAccount.setText("Account");
        btnAccount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAccount.setIconTextGap(20);
        btnAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountActionPerformed(evt);
            }
        });

        btnItem.setText("Item");
        btnItem.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItem.setIconTextGap(20);
        btnItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemActionPerformed(evt);
            }
        });

        btnSale.setText("Sale");
        btnSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSale.setIconTextGap(20);
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });

        btnReceipt.setText("Receipt");
        btnReceipt.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReceipt.setIconTextGap(20);
        btnReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiptActionPerformed(evt);
            }
        });

        btnpurcse.setText("Purchase");
        btnpurcse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnpurcse.setIconTextGap(20);
        btnpurcse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpurcseActionPerformed(evt);
            }
        });

        btnpayment.setText("Payment");
        btnpayment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnpayment.setIconTextGap(20);
        btnpayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpaymentActionPerformed(evt);
            }
        });

        btnledger.setText("Ledger");
        btnledger.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnledger.setIconTextGap(20);
        btnledger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnledgerActionPerformed(evt);
            }
        });

        btnOutstanding.setText("Outstanding");
        btnOutstanding.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOutstanding.setIconTextGap(20);
        btnOutstanding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutstandingActionPerformed(evt);
            }
        });

        btnDayBook.setText("Day Book");
        btnDayBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDayBook.setIconTextGap(20);
        btnDayBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDayBookActionPerformed(evt);
            }
        });

        btnSaleRegister.setText("Sale Register");
        btnSaleRegister.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSaleRegister.setIconTextGap(20);
        btnSaleRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleRegisterActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel2.setText("SOFTWARE VERSION :");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel3.setText("BUILD DATE :");

        btnPurchaseRegister.setText("Pusrchase Register");
        btnPurchaseRegister.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPurchaseRegister.setIconTextGap(20);
        btnPurchaseRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseRegisterActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(56, 68, 76));
        jLabel4.setText("M-jewel");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jLabel4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel4KeyPressed(evt);
            }
        });

        stockreport.setText("Stock Report");
        stockreport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        stockreport.setIconTextGap(20);
        stockreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockreportActionPerformed(evt);
            }
        });

        jButton1.setText("Item wise stock");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(20);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(versioning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BildDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)))
                .addGap(45, 45, 45))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btndailyUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnItem, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSale, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnpurcse, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnpayment, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnledger, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOutstanding, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDayBook, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSaleRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stockreport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPurchaseRegister, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btndailyUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnItem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSale, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnpurcse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnpayment, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnledger, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOutstanding, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDayBook, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSaleRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPurchaseRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockreport, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(versioning, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BildDate, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.setBackground(new java.awt.Color(255, 0, 0));
        tabbedPane.setForeground(new java.awt.Color(255, 255, 0));

        jMenuBar1.setBackground(new java.awt.Color(238, 188, 81));

        jMenu1.setText("Company");
        jMenu1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        menuitemCompanyProfile.setText("Company Profile");
        menuitemCompanyProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemCompanyProfileActionPerformed(evt);
            }
        });
        jMenu1.add(menuitemCompanyProfile);

        menuitemUsers.setText("Users");
        menuitemUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemUsersActionPerformed(evt);
            }
        });
        jMenu1.add(menuitemUsers);

        MenuItemSttings.setText("Settings");
        MenuItemSttings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemSttingsActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemSttings);

        MenuItemCloseCompany.setText("Close Company");
        MenuItemCloseCompany.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                MenuItemCloseCompanyMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        MenuItemCloseCompany.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuItemCloseCompanyMouseClicked(evt);
            }
        });
        MenuItemCloseCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemCloseCompanyActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemCloseCompany);

        jMenu8.setText("Sale Settings");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("One Item sale");
        jRadioButtonMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButtonMenuItem1MouseClicked(evt);
            }
        });
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu8.add(jRadioButtonMenuItem1);

        jMenu1.add(jMenu8);
        jMenu8.setVisible(false);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Masters");
        jMenu2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        jMenuItemOutstanding1.setText("Daily Sale");
        jMenuItemOutstanding1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOutstanding1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemOutstanding1);

        jMenuItemOutstanding2.setText("Account");
        jMenuItemOutstanding2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOutstanding2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemOutstanding2);

        jMenuItemOutstanding3.setText("Item");
        jMenuItemOutstanding3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOutstanding3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemOutstanding3);

        jMenuBar1.add(jMenu2);

        jMenu9.setText("Enteries");
        jMenu9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem1.setText("Sale");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem1);

        jMenuItem2.setText("Receipt");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem2);

        jMenuItem3.setText("Purchase");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem3);

        jMenuItem4.setText("Payment");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem4);

        jMenuItem6.setText("Bank Entry");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem6);

        jMenuBar1.add(jMenu9);

        jMenu3.setText("Reports");
        jMenu3.setFocusable(false);
        jMenu3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        jMenuItemReceiptHistory.setText("Receipt History");
        jMenuItemReceiptHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReceiptHistoryActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemReceiptHistory);

        jMenuItemPaymentHistory.setText("Payments History");
        jMenuItemPaymentHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPaymentHistoryActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemPaymentHistory);

        jMenuItemSaleRegister.setText("Sale Register");
        jMenuItemSaleRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaleRegisterActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemSaleRegister);

        jMenuItemPurchaseRegister.setText("Purchase Register");
        jMenuItemPurchaseRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPurchaseRegisterActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemPurchaseRegister);

        jMenuItemExchangeReport.setText("Exchange Report");
        jMenuItemExchangeReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExchangeReportActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemExchangeReport);

        jMenuItemStockReport.setText("Stock Report");
        jMenuItemStockReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStockReportActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemStockReport);

        jMenuItemTqagPrinting.setText("Tag Printing");
        jMenuItemTqagPrinting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTqagPrintingActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemTqagPrinting);

        jMenuItemLedger.setText("Ledger");
        jMenuItemLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLedgerActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemLedger);

        jMenu6.setText("GST Repots");

        Gst1.setText("GST 1");
        Gst1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gst1ActionPerformed(evt);
            }
        });
        jMenu6.add(Gst1);

        Gst2.setText("Gst 2");
        Gst2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gst2ActionPerformed(evt);
            }
        });
        jMenu6.add(Gst2);

        jMenu3.add(jMenu6);

        jMenuItem17.setText("Bill History");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuItem18.setText("Payment History");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem18);

        jMenuItem19.setText("Receipt History");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItemOutstanding.setText("Outstanding Analysis");
        jMenuItemOutstanding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOutstandingActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemOutstanding);

        jMenuItemOutstanding4.setText("Day Book");
        jMenuItemOutstanding4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOutstanding4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemOutstanding4);

        jMenuItem5.setText("Item wise Stock");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Utilities");
        jMenu4.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        jMenuItem7.setText("Loan Entry");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem8.setText("Loan Book");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setText("Loan Receipt");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setText("Loan Ledger");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");
        jMenu5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        jMenu7.setText("Licence");

        jMenuItem21.setText("View");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem21);

        jMenuItem22.setText("Re-New");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem22);

        jMenu5.add(jMenu7);

        jMenuItem20.setText("About");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem20);

        jMenuItem23.setText("log");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem23);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1081, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        String str
                = "            Build date: 24-Feb-2022                      \n"
                + "Jewellery is a user-friendly software for managing your    \n"
                + "personal financial accounting needs.Now not much training\n"
                + "and updating is required for maintaining your accounts   \n"
                + "data on your computer. It is a lightweight software that \n"
                + "anyone can use with minimal space.                       \n\n"
                + "   :: Special Thanks to the Jewelry software team::     \n"
                + "                   Development Team                     \n"
                + "                  ______________                       \n"
                + "                      Team Lead :                       \n"
                + "                     Umesh Kumar                        \n\n"
                + "                       Shashank                         \n"
                + "                  _______________                      \n"
                + "                      Developers :                      \n"
                + "                 Anushka Garhwal                       \n"
                + "                   Ubaid Khan                          \n"
                + "                Satyam Kumar Singh                     \n"
                + "                      Sachin Mishra                       \n"
                + "                      Dhawal Verma                   \n"
                + "                          Sachin                              \n"
                + "                    Pawan Tiwari                              \n";
        ImageIcon icon = new ImageIcon(License_form.class.getResource("/assets/jewelry.png"));
        JOptionPane.showMessageDialog(
                null,
                str,
                "About Software", JOptionPane.INFORMATION_MESSAGE,
                icon);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void btndailyUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndailyUpdateActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(0);

    }//GEN-LAST:event_btndailyUpdateActionPerformed

    private void btnAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountActionPerformed

        int index = tabbedPane.indexOfTab("Account");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            AccountsListScreen ac = new AccountsListScreen();
            tabbedPane.addTab("Account", ac.getContentPane());
            tabbedPane.setSelectedComponent(ac.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAccountActionPerformed

    private void btnItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemActionPerformed
        int index = tabbedPane.indexOfTab("Item");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemLists ac = new ItemLists();
            tabbedPane.addTab("Item", ac.jPanel1);
            tabbedPane.setSelectedComponent(ac.jPanel1);
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnItemActionPerformed

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("sale");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            sc = new SaleScreen();
            tabbedPane.addTab("sale", sc.getContentPane());
            tabbedPane.setSelectedComponent(sc.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_btnSaleActionPerformed

    private void btnReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiptActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Receipt");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ReceiptScreen re = new ReceiptScreen();
            tabbedPane.addTab("Receipt", re.getContentPane());
            tabbedPane.setSelectedComponent(re.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_btnReceiptActionPerformed

    private void btnpurcseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpurcseActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Purchase");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            PurchaseScreen pr = new PurchaseScreen();
            tabbedPane.addTab("Purchase", pr.getContentPane());
            tabbedPane.setSelectedComponent(pr.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_btnpurcseActionPerformed

    private void btnpaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpaymentActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Payment");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                PaymentScreen pm = new PaymentScreen();
                tabbedPane.addTab("Payment", pm.getContentPane());
                tabbedPane.setSelectedComponent(pm.getContentPane());
            } catch (Exception e) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, e);

                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_btnpaymentActionPerformed

    private void btnledgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnledgerActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("ledger");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            LeisureTable le = new LeisureTable();
            tabbedPane.addTab("ledger", le.getContentPane());
            tabbedPane.setSelectedComponent(le.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_btnledgerActionPerformed

    private void btnOutstandingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutstandingActionPerformed
        int index = tabbedPane.indexOfTab("Outstanding");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                // TODO add your handling code here:
                OutstandingAnalysis ot = new OutstandingAnalysis();
                tabbedPane.addTab("Outstanding", ot.getContentPane());
                tabbedPane.setSelectedComponent(ot.getContentPane());
            } catch (ParseException ex) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_btnOutstandingActionPerformed

    private void btnDayBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDayBookActionPerformed
        int index = tabbedPane.indexOfTab("Day Book");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                DayBook db = new DayBook();
                tabbedPane.addTab("Day Book", db.jPanel4);
                tabbedPane.setSelectedComponent(db.jPanel4);
            } catch (Exception e) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, e);

                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

// TODO add your handling code here:
    }//GEN-LAST:event_btnDayBookActionPerformed

    private void btnSaleRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleRegisterActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("sALE rEGISTER".toLowerCase());
        if (index == -1) {
            // If it doesn't exist, create a new one and add it

            SaleRegisterScreen re = new SaleRegisterScreen();
            tabbedPane.addTab("sALE rEGISTER".toLowerCase(), re.getContentPane());
            tabbedPane.setSelectedComponent(re.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_btnSaleRegisterActionPerformed

    private void btnPurchaseRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseRegisterActionPerformed
        int index = tabbedPane.indexOfTab("Purchase Register");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it

            PurchaseRegisterScreen pr = new PurchaseRegisterScreen();

            tabbedPane.addTab("Purchase Register", pr.getContentPane());
            tabbedPane.setSelectedComponent(pr.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnPurchaseRegisterActionPerformed

    private void menuitemCompanyProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemCompanyProfileActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Company Profile");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            EditCompanyDetailsScreen s = new EditCompanyDetailsScreen();
            tabbedPane.addTab("Company Profile", s.getContentPane());
            tabbedPane.setSelectedComponent(s.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_menuitemCompanyProfileActionPerformed

    private void menuitemUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemUsersActionPerformed
        int index = tabbedPane.indexOfTab("Users");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            Usercomp cpm = new Usercomp();
            tabbedPane.addTab("Users", cpm.getContentPane());
            tabbedPane.setSelectedComponent(cpm.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_menuitemUsersActionPerformed

    private void MenuItemSttingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemSttingsActionPerformed
        int index = tabbedPane.indexOfTab("Settings");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            Settings_change s = new Settings_change();
            tabbedPane.addTab("Settings", s.getContentPane());
            tabbedPane.setSelectedComponent(s.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuItemSttingsActionPerformed

    private void jMenuItemOutstandingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOutstandingActionPerformed
        int index = tabbedPane.indexOfTab("Outstanding");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                OutstandingAnalysis s = new OutstandingAnalysis();
                tabbedPane.addTab("Outstanding", s.getContentPane());
                tabbedPane.setSelectedComponent(s.getContentPane());        // TODO add your handling code here:
            } catch (ParseException ex) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_jMenuItemOutstandingActionPerformed

    private void jMenuItemReceiptHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReceiptHistoryActionPerformed
        int index = tabbedPane.indexOfTab("Reciept History");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ReceiptHistoryScreen r = new ReceiptHistoryScreen();
            tabbedPane.addTab("Reciept History", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemReceiptHistoryActionPerformed

    private void jMenuItemPaymentHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPaymentHistoryActionPerformed
        int index = tabbedPane.indexOfTab("Payment History");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            PaymentHistory p = new PaymentHistory();
            tabbedPane.addTab("Payment History", p.getContentPane());
            tabbedPane.setSelectedComponent(p.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemPaymentHistoryActionPerformed

    private void jMenuItemSaleRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaleRegisterActionPerformed
        int index = tabbedPane.indexOfTab("Sale Register");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            SaleRegisterScreen r = new SaleRegisterScreen();
            tabbedPane.addTab("Sale Register", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemSaleRegisterActionPerformed

    private void jMenuItemPurchaseRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPurchaseRegisterActionPerformed
        int index = tabbedPane.indexOfTab("Purchase Register");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            PurchaseRegisterScreen p = new PurchaseRegisterScreen();

            tabbedPane.addTab("Purchase Register", p.getContentPane());
            tabbedPane.setSelectedComponent(p.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemPurchaseRegisterActionPerformed

    private void jMenuItemExchangeReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExchangeReportActionPerformed
        int index = tabbedPane.indexOfTab("Exchange Report");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ExchangeReport r = new ExchangeReport();
            tabbedPane.addTab("Exchange Report", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemExchangeReportActionPerformed

    private void jMenuItemStockReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStockReportActionPerformed
        int index = tabbedPane.indexOfTab("Item Stock");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemsStockScreen i = new ItemsStockScreen();

            tabbedPane.addTab("Item Stock", i.getContentPane());
            tabbedPane.setSelectedComponent(i.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemStockReportActionPerformed

    private void jMenuItemTqagPrintingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTqagPrintingActionPerformed
        int index = tabbedPane.indexOfTab("Tag Printing");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it

            TagPrinting t = new TagPrinting();
            tabbedPane.addTab("Tag Printing", t.getContentPane());
            tabbedPane.setSelectedComponent(t.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemTqagPrintingActionPerformed

    private void jMenuItemLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLedgerActionPerformed
        int index = tabbedPane.indexOfTab("ledger");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it

            LeisureTable l = new LeisureTable();
            tabbedPane.addTab("ledger", l.getContentPane());
            tabbedPane.setSelectedComponent(l.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemLedgerActionPerformed

    private void Gst1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gst1ActionPerformed
        int index = tabbedPane.indexOfTab("GST1 B2B");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            GSTR1B2B b = new GSTR1B2B();
            tabbedPane.addTab("GST1 B2B", b.getContentPane());
            tabbedPane.setSelectedComponent(b.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_Gst1ActionPerformed

    private void Gst2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gst2ActionPerformed
        int index = tabbedPane.indexOfTab("GST2 B2B");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            GSTR1B2B2 b = new GSTR1B2B2();

            tabbedPane.addTab("GST2 B2B", b.getContentPane());
            tabbedPane.setSelectedComponent(b.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_Gst2ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        int index = tabbedPane.indexOfTab("Bill History");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            Bill_History b = new Bill_History();
            tabbedPane.addTab("Bill History", b.getContentPane());
            tabbedPane.setSelectedComponent(b.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        int index = tabbedPane.indexOfTab("Payment History");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            PaymentHistory p = new PaymentHistory();
            tabbedPane.addTab("Payment History", p.getContentPane());
            tabbedPane.setSelectedComponent(p.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed

        int index = tabbedPane.indexOfTab("Receipt History");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ReceiptHistoryScreen r = new ReceiptHistoryScreen();
            tabbedPane.addTab("Receipt History", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Licence Form");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            License_accepted a = new License_accepted();
            tabbedPane.addTab("Licence Form", a.getContentPane());
            tabbedPane.setSelectedComponent(a.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("RE New Licence");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            License_form f = new License_form();
            tabbedPane.addTab("RE New Licence", f.getContentPane());
            tabbedPane.setSelectedComponent(f.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        DebugAndLogsScreen logs = new DebugAndLogsScreen();
        logs.setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed
    private void InsertTableHidingFields(String value) {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "UPDATE hidingFields set tagno='" + value + "'";
            int r = stmt.executeUpdate(sql);
            if (r == 1) {
                JOptionPane.showMessageDialog(this, "Change successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Change somethings");
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        // TODO add your handling code here:cf
        if (jRadioButtonMenuItem1.isSelected()) {
            InsertTableHidingFields("true");
        } else {
            InsertTableHidingFields("false");
        }

    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jRadioButtonMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jRadioButtonMenuItem1MouseClicked

    private void jMenuItemOutstanding1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOutstanding1ActionPerformed
        tabbedPane.setSelectedIndex(0);

//        SaleRegisterScreen obj = new SaleRegisterScreen();
//        obj.setVisible(true);
        int index = tabbedPane.indexOfTab("Sale Register");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            SaleRegisterScreen r = new SaleRegisterScreen();
            tabbedPane.addTab("Sale Register", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_jMenuItemOutstanding1ActionPerformed

    private void jMenuItemOutstanding2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOutstanding2ActionPerformed

        int index = tabbedPane.indexOfTab("Account");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            AccountsListScreen ac = new AccountsListScreen();
            tabbedPane.addTab("Account", ac.getContentPane());
            tabbedPane.setSelectedComponent(ac.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemOutstanding2ActionPerformed

    private void jMenuItemOutstanding3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOutstanding3ActionPerformed
        int index = tabbedPane.indexOfTab("Item");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemLists ac = new ItemLists();
            tabbedPane.addTab("Item", ac.jPanel1);
            tabbedPane.setSelectedComponent(ac.jPanel1);
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemOutstanding3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("sale");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            sc = new SaleScreen();
            tabbedPane.addTab("sale", sc.getContentPane());
            tabbedPane.setSelectedComponent(sc.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Receipt");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ReceiptScreen re = new ReceiptScreen();
            tabbedPane.addTab("Receipt", re.getContentPane());
            tabbedPane.setSelectedComponent(re.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        int index = tabbedPane.indexOfTab("Purchase");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            PurchaseScreen pr = new PurchaseScreen();
            tabbedPane.addTab("Purchase", pr.getContentPane());
            tabbedPane.setSelectedComponent(pr.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        int index = tabbedPane.indexOfTab("Payment");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                PaymentScreen pm = new PaymentScreen();
                tabbedPane.addTab("Payment", pm.getContentPane());
                tabbedPane.setSelectedComponent(pm.getContentPane());
            } catch (Exception e) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, e);

                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItemOutstanding4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOutstanding4ActionPerformed
        int index = tabbedPane.indexOfTab("Day Book");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            try {
                DayBook db = new DayBook();
                tabbedPane.addTab("Day Book", db.jPanel4);
                tabbedPane.setSelectedComponent(db.jPanel4);
            } catch (Exception e) {
                Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, e);

                JOptionPane.showMessageDialog(this, e);
            }
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemOutstanding4ActionPerformed

    private void jLabel4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel4KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel4KeyPressed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        DashBoardScreen ds = new DashBoardScreen();
        ds.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void btndailyUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btndailyUpdateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btndailyUpdateMouseClicked

    private void MenuItemCloseCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemCloseCompanyActionPerformed
        // TODO add your handling code here:

        listOfAllCompany obj = new listOfAllCompany();
        obj.setVisible(true);

    }//GEN-LAST:event_MenuItemCloseCompanyActionPerformed

    private void MenuItemCloseCompanyMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_MenuItemCloseCompanyMenuKeyPressed
        // TODO add your handling code here:

//        listOfAllCompany obj = new listOfAllCompany();
//        obj.setVisible(true);
    }//GEN-LAST:event_MenuItemCloseCompanyMenuKeyPressed

    private void MenuItemCloseCompanyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuItemCloseCompanyMouseClicked
        // TODO add your handling code here:
//        listOfAllCompany obj = new listOfAllCompany();
//        obj.setVisible(true);
    }//GEN-LAST:event_MenuItemCloseCompanyMouseClicked

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        int index = tabbedPane.indexOfTab("Item wise Stock");

        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemWiseStock r = new ItemWiseStock();
            tabbedPane.addTab("Item wise Stock", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        {
            int index = tabbedPane.indexOfTab("Bank Group");
            if (index == -1) {
                // If it doesn't exist, create a new one and add it
                BankGroup bk = new BankGroup();
                tabbedPane.addTab("Bank Group", bk.getContentPane());
                tabbedPane.setSelectedComponent(bk.getContentPane());
            } else {
                // If it exists, select the existing tab
                tabbedPane.setSelectedIndex(index);
            }
        }

    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void stockreportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockreportActionPerformed
        int index = tabbedPane.indexOfTab("Item Stock");
        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemsStockScreen i = new ItemsStockScreen();

            tabbedPane.addTab("Item Stock", i.getContentPane());
            tabbedPane.setSelectedComponent(i.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_stockreportActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index = tabbedPane.indexOfTab("Item wise Stock");

        if (index == -1) {
            // If it doesn't exist, create a new one and add it
            ItemWiseStock r = new ItemWiseStock();
            tabbedPane.addTab("Item wise Stock", r.getContentPane());
            tabbedPane.setSelectedComponent(r.getContentPane());
        } else {
            // If it exists, select the existing tab
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            int index = tabbedPane.indexOfTab("Loan Entry");
            if (index == -1) {
                LoanEntry loanEntryPanel = new LoanEntry(); // Create a new instance of LoanEntry
                tabbedPane.addTab("Loan Entry", loanEntryPanel); // Add it to the tabbed pane
                tabbedPane.setSelectedComponent(loanEntryPanel); // Select the newly added tab
            } else {
                tabbedPane.setSelectedIndex(index); // If it exists, select the existing tab
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error creating Loan Entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        try {
            int index = tabbedPane.indexOfTab("Loan Book");
            if (index == -1) {
                LoanBook loanBook = new LoanBook();
                // Ensure the panel is properly initialized
                tabbedPane.addTab("Loan Book", loanBook.getContentPane());
                tabbedPane.setSelectedComponent(loanBook.getContentPane());
            } else {
                tabbedPane.setSelectedIndex(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening Loan Book:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        try {
            int index = tabbedPane.indexOfTab("Loan Receipt");
            if (index == -1) {
                LoanReceipt loan_receipt = new LoanReceipt();
                tabbedPane.addTab("Loan Receipt", loan_receipt.getContentPane());
                tabbedPane.setSelectedComponent(loan_receipt.getContentPane());
            } else {
                tabbedPane.setSelectedIndex(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening Loan Book:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
           try {
            int index = tabbedPane.indexOfTab("Loan Ledger");
            if (index == -1) {
                LoanLedger loan_ledger = new LoanLedger();
                tabbedPane.addTab("Loan Ledger", loan_ledger.getContentPane());
                tabbedPane.setSelectedComponent(loan_ledger.getContentPane());
            } else {
                tabbedPane.setSelectedIndex(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening Loan Book:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

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
            java.util.logging.Logger.getLogger(DashBoardScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoardScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoardScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoardScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DashBoardScreen().setVisible(true);
            }
        });

//        SwingUtilities.invokeLater(new Runnable() {
//        @Override
//            public void run() {
//                new DashBoardScreen().setVisible(true);
//        }
//        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BildDate;
    private javax.swing.JMenuItem Gst1;
    private javax.swing.JMenuItem Gst2;
    private javax.swing.JMenuItem MenuItemCloseCompany;
    private javax.swing.JMenuItem MenuItemSttings;
    private javax.swing.JButton btnAccount;
    private javax.swing.JButton btnDayBook;
    private javax.swing.JButton btnItem;
    private javax.swing.JButton btnOutstanding;
    private javax.swing.JButton btnPurchaseRegister;
    private javax.swing.JButton btnReceipt;
    private javax.swing.JButton btnSale;
    private javax.swing.JButton btnSaleRegister;
    private javax.swing.JButton btndailyUpdate;
    private javax.swing.JButton btnledger;
    private javax.swing.JButton btnpayment;
    private javax.swing.JButton btnpurcse;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemExchangeReport;
    private javax.swing.JMenuItem jMenuItemLedger;
    private javax.swing.JMenuItem jMenuItemOutstanding;
    private javax.swing.JMenuItem jMenuItemOutstanding1;
    private javax.swing.JMenuItem jMenuItemOutstanding2;
    private javax.swing.JMenuItem jMenuItemOutstanding3;
    private javax.swing.JMenuItem jMenuItemOutstanding4;
    private javax.swing.JMenuItem jMenuItemPaymentHistory;
    private javax.swing.JMenuItem jMenuItemPurchaseRegister;
    private javax.swing.JMenuItem jMenuItemReceiptHistory;
    private javax.swing.JMenuItem jMenuItemSaleRegister;
    private javax.swing.JMenuItem jMenuItemStockReport;
    private javax.swing.JMenuItem jMenuItemTqagPrinting;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem menuitemCompanyProfile;
    private javax.swing.JMenuItem menuitemUsers;
    private javax.swing.JButton stockreport;
    public static javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel versioning;
    // End of variables declaration//GEN-END:variables

}
