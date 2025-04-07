package jewellery;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import javax.swing.JSeparator;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JMenu;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class DashBoardScreen extends javax.swing.JFrame {
       
    public  cashAndBankPayments CashAndBankPaymentsSales;
    private ImageIcon image;
    private final int buttonImageWidth = 25;
    private  int buttonImageHeight=25;
  
    public SaleScreen sc;
    public static boolean isCreateCompanyWindowOpen = false;
 
    private final DebugAndLogsScreen debugAndLogsScreen = new DebugAndLogsScreen();

    public DashBoardScreen(){
        try{
         CashAndBankPaymentsSales= new cashAndBankPayments();    
        Logger.getLogger(DashBoardScreen.class.getName()).log(Level.INFO, "Dashboard Screen Creation Started!");

        initComponents();

        setTitle();
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpg");
        this.setIconImage(icon);

        Logger.getLogger(DashBoardScreen.class.getName()).log(Level.INFO, "Dashboard Components Initialized!");

        buttonImageHeight = btnDailyUpdates.getHeight() - 20;

        GLOBAL_VARS.IS_SCREEN_INSIDE_TABBED_PANE = true;

        maximizeWindow();
        getuser();
        initCloseButtonEventCode();

        GLOBAL_VARS.tabbedPaneDimensions = tpScreensHolder.getSize();

        pnlDailyUpdates.add(new DailyUpdatesScreen().getContentPane());
        pnlReceipt.add(new ReceiptScreen().getContentPane());
        pnlItemEntry.add(new ItemLists().jPanel1);
        pnlPurchase.add(new PurchaseScreen().pnlRootContainer);
        
        pnlSale.add(new SaleScreen().pnlRootContainer);
//        pnlSale1.add(new SaleScreenNoGST().getContentPane());
        pnlCreateAccount.add(new AccountsListScreen().jPanel1);
        pnlPayment.add(new PaymentScreen().getContentPane());
        pnlSaleRegister.add(new SaleRegisterScreen().jSplitPane2);
        pnlDayBook.add(new DayBook().jPanel4);
        pnlPurchaseRegister.add(new PurchaseRegisterScreen().jSplitPane2);
        pnlReceiptHistory.add(new ReceiptHistoryScreen().jPanel4);
        pnlPaymentHistory.add(new PaymentHistory().jPanel4);

        Logger.getLogger(DashBoardScreen2.class.getName()).log(Level.INFO, "Dashboard TabbedPane Panels Set!");

        setIconsOnButtons();

        Logger.getLogger(DashBoardScreen.class.getName()).log(Level.INFO, "Dashboard Buttons Icons Set!");

        jMenuItem1.setVisible(false);
        jMenuItem2.setVisible(false);
        jMenuItem55.setVisible(false);
        jMenuItem47.setVisible(false);
        jMenuItem44.setVisible(false);
        jMenuItem43.setVisible(false);
        jMenuItem42.setVisible(false);
        versioning v = new versioning();
        String ver = v.version();
        String update = v.lastupdate();
        jLabel4.setText(ver);
        jLabel5.setText(update);
//        if (checklicense()) {
//            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//            Runnable task = new Runnable() {
//                public void run() {
//                    disableHomepage();
//                    license();
//
//                }
//            };
//
//            int delay = 60;
//            scheduler.schedule(task, delay, TimeUnit.SECONDS);
//            scheduler.shutdown();
//
//        } else {
        newtimer();
        }catch(Exception e){
                  Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null,e );
        }
//        }
    }

    void newtimer() {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (checklicense()) {
                    disableHomepage();
                    license();
                    timer.cancel();
                }
            }
        }, 0, 60000);//wait 0 ms before doing the action and do it evry 60000ms (60second)

    }

    public void disableHomepage() {
        System.out.print("Disabled home");
        jMenu1.setEnabled(false);
        jMenu2.setEnabled(false);
        jMenu3.setEnabled(false);
        jMenu5.setEnabled(false);
        tpScreensHolder.setEnabled(false);
        btnDailyUpdates.setEnabled(false);
        btnCreateAccount.setEnabled(false);
        btnReceipt.setEnabled(false);
        btnItemEntry.setEnabled(false);
        btnPurchase.setEnabled(false);
        btnSale.setEnabled(false);
        btnSale1.setEnabled(false);
        btnPayment.setEnabled(false);
        btnPaymentHistory.setEnabled(false);
        btnSaleRegister.setEnabled(false);
        btnDayBook.setEnabled(false);
        btnPurchaseRegister.setEnabled(false);

    }

    void setTitle() {
        try {
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("Select * from settings where slno=3;");
            while (rs.next()) {
                String title = rs.getString("file_name");
                this.setTitle(title);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setIconsOnButtons() {
        btnDailyUpdates.setIcon(getResizedImageIcon(AssetsLocations.UPDATE_ICON,
                buttonImageWidth, buttonImageHeight));
        btnCreateAccount.setIcon(getResizedImageIcon(AssetsLocations.PROFILE_ICON,
                buttonImageWidth, buttonImageHeight));
        btnReceipt.setIcon(getResizedImageIcon(AssetsLocations.RECEIPT_ICON,
                buttonImageWidth, buttonImageHeight));
        btnItemEntry.setIcon(getResizedImageIcon(AssetsLocations.AMULET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnPurchase.setIcon(getResizedImageIcon(AssetsLocations.COIN_ICON,
                buttonImageWidth, buttonImageHeight));
        btnSale.setIcon(getResizedImageIcon(AssetsLocations.DOLLAR_HAND_ICON,
                buttonImageWidth, buttonImageHeight));
        btnSale1.setIcon(getResizedImageIcon(AssetsLocations.DOLLAR_HAND_ICON,
                buttonImageWidth, buttonImageHeight));
        btnPayment.setIcon(getResizedImageIcon(AssetsLocations.MONEY_WALLET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnPaymentHistory.setIcon(getResizedImageIcon(AssetsLocations.MONEY_WALLET_ICON,
                buttonImageWidth, buttonImageHeight));
        btnSaleRegister.setIcon(getResizedImageIcon(AssetsLocations.REGISTER_ICON,
                buttonImageWidth, buttonImageHeight));
        btnDayBook.setIcon(getResizedImageIcon(AssetsLocations.OPEN_BOOK_ICON,
                buttonImageWidth, buttonImageHeight));
        btnPurchaseRegister.setIcon(getResizedImageIcon(AssetsLocations.RECEIPT_REGISTER_ICON,
                buttonImageWidth, buttonImageHeight));
    }

    private ImageIcon getResizedImageIcon(String resourceLocation, int width, int height) {
        image = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation)).getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return image;
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
            Connection c =DBConnect.connect();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select name from users where userid='" + userid + "';");
            while (rs.next()) {
                name = name + rs.getString("name");
            }
            jLabel6.setText(name);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        tpScreensHolder = new javax.swing.JTabbedPane();
        pnlDailyUpdates = new javax.swing.JPanel();
        pnlCreateAccount = new javax.swing.JPanel();
        pnlReceipt = new javax.swing.JPanel();
        pnlReceiptHistory = new javax.swing.JPanel();
        pnlItemEntry = new javax.swing.JPanel();
        pnlPurchase = new javax.swing.JPanel();
        pnlSale = new javax.swing.JPanel();
//        pnlSale1 = new javax.swing.JPanel();
        pnlPayment = new javax.swing.JPanel();
        pnlPaymentHistory = new javax.swing.JPanel();
        pnlSaleRegister = new javax.swing.JPanel();
        pnlDayBook = new javax.swing.JPanel();
        pnlPurchaseRegister = new javax.swing.JPanel();
        pnlLicence = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnCreateAccount = new javax.swing.JButton();
        btnDailyUpdates = new javax.swing.JButton();
        btnReceipt = new javax.swing.JButton();
        btnItemEntry = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnSale = new javax.swing.JButton();
        btnSale1 = new javax.swing.JButton();
        btnPayment = new javax.swing.JButton();
        btnPaymentHistory = new javax.swing.JButton();
        btnSaleRegister = new javax.swing.JButton();
        btnDayBook = new javax.swing.JButton();
        btnPurchaseRegister = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuItemCreateCompany = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        users = new javax.swing.JMenuItem();
        menuItemCreateCompany1 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItemReceiptHistory = new javax.swing.JMenuItem();
        jMenuItemPaymentHistory = new javax.swing.JMenuItem();
        jMenuItemSaleRegister = new javax.swing.JMenuItem();
        jMenuItemPurchaseRegister = new javax.swing.JMenuItem();
        jMenuItemStockReport = new javax.swing.JMenuItem();
        jMenuExchaneReportkReport=new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem99 = new javax.swing.JMenuItem();
        jMenuItem100 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem90 = new javax.swing.JMenuItem();
        jMenuItem89 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem54 = new javax.swing.JMenuItem();
        About = new javax.swing.JMenuItem();

//        Frame.setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1280, 720));

        tpScreensHolder.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tpScreensHolder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tpScreensHolder.setMaximumSize(new java.awt.Dimension(3840, 2160));
        tpScreensHolder.setMinimumSize(new java.awt.Dimension(1001, 654));
        tpScreensHolder.setPreferredSize(new java.awt.Dimension(1001, 654));
        tpScreensHolder.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpScreensHolderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlDailyUpdatesLayout = new javax.swing.GroupLayout(pnlDailyUpdates);
        pnlDailyUpdates.setLayout(pnlDailyUpdatesLayout);
        pnlDailyUpdatesLayout.setHorizontalGroup(
                pnlDailyUpdatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlDailyUpdatesLayout.setVerticalGroup(
                pnlDailyUpdatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Daily Updates", pnlDailyUpdates);

        javax.swing.GroupLayout pnlCreateAccountLayout = new javax.swing.GroupLayout(pnlCreateAccount);
        pnlCreateAccount.setLayout(pnlCreateAccountLayout);
        pnlCreateAccountLayout.setHorizontalGroup(
                pnlCreateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlCreateAccountLayout.setVerticalGroup(
                pnlCreateAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Account", pnlCreateAccount);

        javax.swing.GroupLayout pnlReceiptLayout = new javax.swing.GroupLayout(pnlReceipt);
        pnlReceipt.setLayout(pnlReceiptLayout);
        pnlReceiptLayout.setHorizontalGroup(
                pnlReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlReceiptLayout.setVerticalGroup(
                pnlReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Receipt", pnlReceipt);

        javax.swing.GroupLayout pnlItemEntryLayout = new javax.swing.GroupLayout(pnlItemEntry);
        pnlItemEntry.setLayout(pnlItemEntryLayout);
        pnlItemEntryLayout.setHorizontalGroup(
                pnlItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlItemEntryLayout.setVerticalGroup(
                pnlItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Item Entry", pnlItemEntry);

        javax.swing.GroupLayout pnlPurchaseLayout = new javax.swing.GroupLayout(pnlPurchase);
        pnlPurchase.setLayout(pnlPurchaseLayout);
        pnlPurchaseLayout.setHorizontalGroup(
                pnlPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlPurchaseLayout.setVerticalGroup(
                pnlPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Purchase", pnlPurchase);

        javax.swing.GroupLayout pnlSaleLayout = new javax.swing.GroupLayout(pnlSale);
        pnlSale.setLayout(pnlSaleLayout);
        pnlSaleLayout.setHorizontalGroup(
                pnlSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlSaleLayout.setVerticalGroup(
                pnlSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Sale", pnlSale);

        javax.swing.GroupLayout pnlReceiptHistoryLayout = new javax.swing.GroupLayout(pnlReceiptHistory);
        pnlReceiptHistory.setLayout(pnlReceiptHistoryLayout);
        pnlReceiptHistoryLayout.setHorizontalGroup(
                pnlReceiptHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlReceiptHistoryLayout.setVerticalGroup(
                pnlReceiptHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("ReceiptHistory", pnlReceiptHistory);

//        javax.swing.GroupLayout pnlSale1Layout = new javax.swing.GroupLayout(pnlSale1);
//        pnlSale1.setLayout(pnlSale1Layout);
//        pnlSale1Layout.setHorizontalGroup(
//            pnlSale1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 768, Short.MAX_VALUE)
//        );
//        pnlSale1Layout.setVerticalGroup(
//            pnlSale1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 697, Short.MAX_VALUE)
//        );
//
//        tpScreensHolder.addTab("Non Gst Sale", pnlSale1);
        javax.swing.GroupLayout pnlPaymentLayout = new javax.swing.GroupLayout(pnlPayment);
        pnlPayment.setLayout(pnlPaymentLayout);
        pnlPaymentLayout.setHorizontalGroup(
                pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlPaymentLayout.setVerticalGroup(
                pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Payment", pnlPayment);

        javax.swing.GroupLayout pnlPaymentHistoryLayout = new javax.swing.GroupLayout(pnlPaymentHistory);
        pnlPaymentHistory.setLayout(pnlPaymentHistoryLayout);
        pnlPaymentHistoryLayout.setHorizontalGroup(
                pnlPaymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlPaymentHistoryLayout.setVerticalGroup(
                pnlPaymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("PaymentHistory", pnlPaymentHistory);

        javax.swing.GroupLayout pnlSaleRegisterLayout = new javax.swing.GroupLayout(pnlSaleRegister);
        pnlSaleRegister.setLayout(pnlSaleRegisterLayout);
        pnlSaleRegisterLayout.setHorizontalGroup(
                pnlSaleRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlSaleRegisterLayout.setVerticalGroup(
                pnlSaleRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Sale Register", pnlSaleRegister);

        javax.swing.GroupLayout pnlDayBookLayout = new javax.swing.GroupLayout(pnlDayBook);
        pnlDayBook.setLayout(pnlDayBookLayout);
        pnlDayBookLayout.setHorizontalGroup(
                pnlDayBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlDayBookLayout.setVerticalGroup(
                pnlDayBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Day Book", pnlDayBook);

        javax.swing.GroupLayout pnlPurchaseRegisterLayout = new javax.swing.GroupLayout(pnlPurchaseRegister);
        pnlPurchaseRegister.setLayout(pnlPurchaseRegisterLayout);
        pnlPurchaseRegisterLayout.setHorizontalGroup(
                pnlPurchaseRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlPurchaseRegisterLayout.setVerticalGroup(
                pnlPurchaseRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Purchase Reg", pnlPurchaseRegister);

        javax.swing.GroupLayout pnlLicenceLayout = new javax.swing.GroupLayout(pnlLicence);
        pnlLicence.setLayout(pnlLicenceLayout);
        pnlLicenceLayout.setHorizontalGroup(
                pnlLicenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 768, Short.MAX_VALUE)
        );
        pnlLicenceLayout.setVerticalGroup(
                pnlLicenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 697, Short.MAX_VALUE)
        );

        tpScreensHolder.addTab("Licence", pnlLicence);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MENU");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Software Version: ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Build Date: ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        btnCreateAccount.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnCreateAccount.setText("Account");
        btnCreateAccount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCreateAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateAccountActionPerformed(evt);
            }
        });

        btnDailyUpdates.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnDailyUpdates.setText("Daily Updates");
        btnDailyUpdates.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDailyUpdates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyUpdatesActionPerformed(evt);
            }
        });

        btnReceipt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnReceipt.setText("Receipt");
        btnReceipt.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiptActionPerformed(evt);
            }
        });

        btnItemEntry.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnItemEntry.setText("Item");
        btnItemEntry.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemEntryActionPerformed(evt);
            }
        });

        btnPurchase.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnPurchase.setText("Purchase");
        btnPurchase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });

        btnSale.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSale.setText("Sale");
        btnSale.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });

        btnSale1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSale1.setText("Ledger");
        btnSale1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSale1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSale1ActionPerformed(evt);
            }
        });

        btnPayment.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnPayment.setText("Payment");
        btnPayment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnPaymentHistory.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnPaymentHistory.setText("Outstanding");
        btnPaymentHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPaymentHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentHistoryActionPerformed(evt);
            }
        });

        btnSaleRegister.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnSaleRegister.setText("Sale Register");
        btnSaleRegister.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSaleRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleRegisterActionPerformed(evt);
            }
        });

        btnDayBook.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnDayBook.setText("Day Book");
        btnDayBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDayBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDayBookActionPerformed(evt);
            }
        });

        btnPurchaseRegister.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnPurchaseRegister.setText("Purchase Register");
        btnPurchaseRegister.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPurchaseRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseRegisterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 0));

        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuBar1.setMaximumSize(new java.awt.Dimension(30, 30));
        jMenuBar1.setMinimumSize(new java.awt.Dimension(30, 30));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(30, 30));

        jMenu1.setBackground(new java.awt.Color(204, 255, 204));
        jMenu1.setText("Company");
        jMenu1.setBorderPainted(true);
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu1.setIconTextGap(15);

        menuItemCreateCompany.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuItemCreateCompany.setText("Company Profile");
        menuItemCreateCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCreateCompanyActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemCreateCompany);
        jMenu1.add(jSeparator5);

        users.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        users.setText("Users");
        users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersActionPerformed(evt);
            }
        });
        jMenu1.add(users);

        menuItemCreateCompany1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuItemCreateCompany1.setText("Settings");
        menuItemCreateCompany1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCreateCompany1ActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemCreateCompany1);
        jMenu1.add(jSeparator4);

        jMenuItem9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem9.setText("Close Company");
        jMenu1.add(jMenuItem9);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Masters");
        jMenu2.setBorderPainted(true);
        jMenu2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu2.setIconTextGap(15);

        jMenuItem13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem13.setText("Outstanding Analysis");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);
        jMenu2.add(jSeparator6);

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem1.setText("Purchase Setting");
        jMenu2.add(jMenuItem1);
        jMenu2.add(jSeparator7);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem2.setText("Accounts List");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Reports");
        jMenu3.setBorderPainted(true);
        jMenu3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu3.setIconTextGap(15);

        jMenuItemReceiptHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItemReceiptHistory.setText("Receipt History");
        jMenuItemReceiptHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReceiptHistoryActionPerformed(evt);
            }

            private void jMenuItemReceiptHistoryActionPerformed(ActionEvent evt) {
                ReceiptHistoryScreen obj = new ReceiptHistoryScreen();
                obj.setVisible(true);
            }
        });
        jMenuItemPaymentHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItemPaymentHistory.setText("Payment History");
        jMenuItemPaymentHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPaymentHistoryActionPerformed(evt);
            }

            private void jMenuItemPaymentHistoryActionPerformed(ActionEvent evt) {
                PaymentHistory obj = new PaymentHistory();
                obj.setVisible(true);
            }
        });
        jMenuItemSaleRegister.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItemSaleRegister.setText("Sale Register");
        jMenuItemSaleRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaleRegisterActionPerformed(evt);
            }

            private void jMenuItemSaleRegisterActionPerformed(ActionEvent evt) {
                SaleRegisterScreen obj = new SaleRegisterScreen();
                System.out.println("hiudcui");
                obj.setVisible(true);
            }
        });
        jMenuItemPurchaseRegister.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItemPurchaseRegister.setText("PurchaseRegister");
        jMenuItemPurchaseRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPurchaseRegisterActionPerformed(evt);
            }

            private void jMenuItemPurchaseRegisterActionPerformed(ActionEvent evt) {
                PurchaseRegisterScreen obj = new PurchaseRegisterScreen();
                obj.setVisible(true);
            }
        });
        jMenuItemStockReport.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItemStockReport.setText("Stock Report");
        jMenuItemStockReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStockReportActionPerformed(evt);
            }

            private void jMenuItemStockReportActionPerformed(ActionEvent evt) {
                ItemsStockScreen obj = new ItemsStockScreen();
                obj.setVisible(true);
            }
        });
//        )))))))))))))))))))))))))))))))))
        jMenuExchaneReportkReport.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuExchaneReportkReport.setText("Exchange Report");
        jMenuExchaneReportkReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExchaneReportkReportActionPerformed(evt);
            }

            private void jMenuExchaneReportkReportActionPerformed(ActionEvent evt) {
                ExchangeReport obj = new ExchangeReport();
                obj.setVisible(true);
            }

        });

        jMenu3.add(jMenuItemReceiptHistory);
        jMenu3.add(jSeparator8);
        jMenu3.add(jMenuItemPaymentHistory);
        jMenu3.add(jSeparator8);
        jMenu3.add(jMenuItemSaleRegister);
        jMenu3.add(jSeparator8);
        jMenu3.add(jMenuItemPurchaseRegister);
        jMenu3.add(jSeparator8);
        jMenu3.add(jMenuExchaneReportkReport);
        jMenu3.add(jSeparator8);
        jMenu3.add(jMenuItemStockReport);
        jMenu3.add(jSeparator8);

        jMenuItem14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem14.setText("Tag Printing");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem14);
        jMenu3.add(jSeparator8);

        jMenuItem15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem15.setText("Ledger Table");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuItem16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem16.setText("Bank Group");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem16);

        separator = new JSeparator();
        jMenu3.add(separator);
        jMenuBar1.add(jMenu3);
        mnNewMenu = new JMenu("GST Report");
        mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jMenu3.add(mnNewMenu);

        jMenuItem99.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem99.setText("GST 1");
        jMenuItem99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem99ActionPerformed(evt);
            }
        });

        jMenuItem100.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem100.setText("GST 2");
        jMenuItem100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem100ActionPerformed(evt);
            }
        });

        mnNewMenu.add(jMenuItem99);
        mnNewMenu.add(jSeparator8);
        mnNewMenu.add(jMenuItem100);
//        mnNewMenu.add(jSeparator8);

        jMenu5.setText("Utilities");
        jMenu5.setBorderPainted(true);
        jMenu5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu5.setIconTextGap(15);

        jMenuItem45.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem45.setText("Bill History");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });

        jMenuItem90.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem90.setText("Payment History");
        jMenuItem90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem90ActionPerformed(evt);
            }
        });

        jMenuItem89.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem89.setText("Receipt History");
        jMenuItem89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem89ActionPerformed(evt);
            }
        });

        jMenu5.add(jMenuItem45);
        jMenu5.add(jMenuItem89);
        jMenu5.add(jMenuItem90);

        jMenuItem42.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem42.setText("Report Building Tools");
        jMenu5.add(jMenuItem42);

        jMenuItem43.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem43.setText("Additional Fields");
        jMenu5.add(jMenuItem43);

        jMenuItem44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem44.setText("Calculator");
        jMenu5.add(jMenuItem44);

        jMenuItem47.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem47.setText("Calender");
        jMenu5.add(jMenuItem47);

        jMenuItem55.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem55.setText("Logs");
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem55);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Help");
        jMenu6.setBorderPainted(true);
        jMenu6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu6.setIconTextGap(15);

        jMenuItem54.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem54.setText("License Register");
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem54);

        About.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        jMenu6.add(About);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tpScreensHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 777, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnReceipt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnCreateAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btnDayBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnDailyUpdates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnItemEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnSale1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnPaymentHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnSaleRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnPurchaseRegister, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDailyUpdates, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCreateAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnItemEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSale, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSale1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPaymentHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDayBook, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSaleRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPurchaseRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 59, Short.MAX_VALUE))
                        .addComponent(tpScreensHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>

    private void menuItemCreateCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCreateCompanyActionPerformed
        SwingUtilities.invokeLater(() -> {
            new EditCompanyDetailsScreen().setVisible(true);
        });
    }//GEN-LAST:event_menuItemCreateCompanyActionPerformed

    private void btnDailyUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyUpdatesActionPerformed
        pnlDailyUpdates.removeAll();
        pnlDailyUpdates.add(new DailyUpdatesScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(0);
    }//GEN-LAST:event_btnDailyUpdatesActionPerformed

    private void btnCreateAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateAccountActionPerformed
       pnlCreateAccount.removeAll();
        pnlCreateAccount.add(new AccountsListScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(1);
    }//GEN-LAST:event_btnCreateAccountActionPerformed

    private void btnReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiptActionPerformed
        pnlReceipt.removeAll();
        try {
            pnlReceipt.add(new ReceiptScreen().getContentPane());
        } catch (Exception ex) {
            Logger.getLogger(DashBoardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpScreensHolder.setSelectedIndex(2);
    }//GEN-LAST:event_btnReceiptActionPerformed

    private void btnItemEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemEntryActionPerformed
        pnlItemEntry.removeAll();
        pnlItemEntry.add(new ItemLists().getContentPane());
        tpScreensHolder.setSelectedIndex(3);
    }//GEN-LAST:event_btnItemEntryActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
       pnlPurchase.removeAll();
        pnlPurchase.add(new PurchaseScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(4);
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
      pnlSale.removeAll();
        pnlSale.add(new SaleScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(5);
    }//GEN-LAST:event_btnSaleActionPerformed

    private void btnSaleRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleRegisterActionPerformed
      pnlSaleRegister.removeAll();
        pnlSaleRegister.add(new SaleRegisterScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(9);
    }//GEN-LAST:event_btnSaleRegisterActionPerformed

    private void btnDayBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDayBookActionPerformed
       pnlDayBook.removeAll();
        pnlDayBook.add(new DayBook().getContentPane());
        tpScreensHolder.setSelectedIndex(10);
    }//GEN-LAST:event_btnDayBookActionPerformed

    private void btnPurchaseRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseRegisterActionPerformed
       pnlPurchaseRegister.removeAll();
        pnlPurchaseRegister.add(new PurchaseRegisterScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(11);
    }//GEN-LAST:event_btnPurchaseRegisterActionPerformed

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
       pnlPayment.removeAll();
        pnlPayment.add(new PaymentScreen().getContentPane());
        tpScreensHolder.setSelectedIndex(7);
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnPaymentHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentHistoryActionPerformed
       
        try{
            OutstandingAnalysis obj = new OutstandingAnalysis();
            obj.setVisible(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e);
        }
        
    }//GEN-LAST:event_btnPaymentHistoryActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        EventQueue.invokeLater(() -> {
            UtilityMethods.showReport("/reports/AccountsList.jrxml",
                    "SELECT accountname, state, statecode FROM " + DatabaseCredentials.ACCOUNT_TABLE,
                    DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        });
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void tpScreensHolderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tpScreensHolderStateChanged
//        if(purchaseScreen.pnlRootContainer.isDisplayable()) {
//            PurchaseScreen.purchasesListTableModel.setRowCount(0);
//        }
    }//GEN-LAST:event_tpScreensHolderStateChanged

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        debugAndLogsScreen.setVisible(true);
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersActionPerformed
        Usercomp ob = new Usercomp();
        ob.setVisible(true);
    }//GEN-LAST:event_usersActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        Bill_History f = new Bill_History();
        f.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItem89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem89ActionPerformed
        ReceiptHistory f = new ReceiptHistory();
        f.setVisible(true);        // TODO add your handling code here:
    	    }//GEN-LAST:event_jMenuItem89ActionPerformed

    private void jMenuItem90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem90ActionPerformed
        PaymentHistory f = new PaymentHistory();
        f.setVisible(true);        // TODO add your handling code here:
    	    }//GEN-LAST:event_jMenuItem90ActionPerformed

    private void btnSale1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSale1ActionPerformed
        LeisureTable obj = new LeisureTable();
        obj.setVisible(true);
    }//GEN-LAST:event_btnSale1ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        TagPrinting ob = new TagPrinting();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem100ActionPerformed(java.awt.event.ActionEvent evt) {
        GSTR1B2B2 ob = new GSTR1B2B2();
        ob.setVisible(true);
    }

    private void jMenuItem99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem99ActionPerformed
        GSTR1B2B ob = new GSTR1B2B();
        ob.setVisible(true);
     }//GEN-LAST:event_jMenuItem99ActionPerformed
    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
       try{
            OutstandingAnalysis obj = new OutstandingAnalysis();
            obj.setVisible(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        LeisureTable ob = new LeisureTable();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {
        BankGroup ob = new BankGroup();
        ob.setVisible(true);
    }

    private boolean checklicense() {
        int ctr = 0;
        String s1 = null, s2 = null, id = null, s3 = null, date_till = null;
        try {

            Connection c =DBConnect.connect();
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
            Connection c1 = DriverManager.getConnection("jdbc:mysql://103.180.121.89:3306/mindstein_licence",
                    "mindstein_licence", "v9qk@ZYFK$4u");
            System.out.println("Connection done");
            Statement st = c1.createStatement();
            String q1 = "Select * from license_act_comp_reqs where system_id='" + s1 + "';";
            ResultSet rs1 = st.executeQuery(q1);
            while (rs1.next()) {
                id = rs1.getString("activation_key");
                date_till = rs1.getString("active_till");
                date_till = date_till.substring(0, date_till.indexOf('.'));
            }

            s.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        pnlLicence.removeAll();
        pnlLicence.add(ob.jPanel1);
        tpScreensHolder.setSelectedIndex(12);
    }

    public void license_accepted() {

        License_accepted ob = new License_accepted();
        pnlLicence.removeAll();
        pnlLicence.add(ob.jPanel1);
        tpScreensHolder.setSelectedIndex(11);
    }
    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        if (checklicense()) {
            license();
        } else {
            license_accepted();
        }
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        String str
                = "            Build date: 24-Feb-2022                      \n"
                + "Jewellery is a user-friendly software for managing your    \n"
                + "personal financial accounting needs.Now not much training\n"
                + "and updating is required for maintaining your accounts   \n"
                + "data on your computer. It is a lightweight software that \n"
                + "anyone can use with minimal space.                       \n\n"
                + "   :: Special Thanks to the Jewelry software team::     \n"
                + "                   Development Team                     \n"
                //                + "                  ______________                       \n"
                //                + "                      Team Lead :                       \n"
                ////                + "                   Umesh Kumar                        \n\n"
                //                + "                       Shashank                         \n"
                + "                  _______________                      \n"
                + "                      Developers :                      \n"
                //                + "                 Anushka Garhwal                       \n"
                //                + "                   Ubaid Khan                          \n"
                //                + "                Satyam Kumar Singh                     \n"
                + "                    Dhawal Verma                        \n"
                + "                   Sachin Poonia                        \n"
                + "                                                        \n";
        ImageIcon icon = new ImageIcon(License_form.class.getResource("/assets/jewelry.png"));
        JOptionPane.showMessageDialog(
                null,
                str,
                "About Software", JOptionPane.INFORMATION_MESSAGE,
                icon);
    }//GEN-LAST:event_AboutActionPerformed

    private void menuItemCreateCompany1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCreateCompany1ActionPerformed
        Settings_change ob = new Settings_change();
        ob.setVisible(true);
    }//GEN-LAST:event_menuItemCreateCompany1ActionPerformed

    // Variables declaration - do not modify                     
    private javax.swing.JMenuItem About;
    private javax.swing.JButton btnCreateAccount;
    private javax.swing.JButton btnDailyUpdates;
    private javax.swing.JButton btnDayBook;
    private javax.swing.JButton btnItemEntry;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPaymentHistory;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnPurchaseRegister;
    private javax.swing.JButton btnReceipt;
    private javax.swing.JButton btnSale;
    private javax.swing.JButton btnSale1;
    private javax.swing.JButton btnSaleRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItemStockReport;
    private javax.swing.JMenuItem jMenuExchaneReportkReport;
    private javax.swing.JMenuItem jMenuItemPurchaseRegister;
    private javax.swing.JMenuItem jMenuItemSaleRegister;
    private javax.swing.JMenuItem jMenuItemPaymentHistory;
    private javax.swing.JMenuItem jMenuItemReceiptHistory;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem99;
    private javax.swing.JMenuItem jMenuItem100;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem90;
    private javax.swing.JMenuItem jMenuItem89;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JMenuItem menuItemCreateCompany;
    private javax.swing.JMenuItem menuItemCreateCompany1;
    private javax.swing.JPanel pnlCreateAccount;
    private javax.swing.JPanel pnlDailyUpdates;
    private javax.swing.JPanel pnlDayBook;
    private javax.swing.JPanel pnlItemEntry;
    private javax.swing.JPanel pnlLicence;
    private javax.swing.JPanel pnlPayment;
    private javax.swing.JPanel pnlPurchase;
    private javax.swing.JPanel pnlPurchaseRegister;
    private javax.swing.JPanel pnlReceiptHistory;
    private javax.swing.JPanel pnlPaymentHistory;
    private javax.swing.JPanel pnlReceipt;
    private javax.swing.JPanel pnlSale;
//    private javax.swing.JPanel pnlSale1;
    private javax.swing.JPanel pnlSaleRegister;
    public static javax.swing.JTabbedPane tpScreensHolder;
    private javax.swing.JMenuItem users;
    private JSeparator separator;
    private JMenuItem gst1b2b;
    private JMenu mnNewMenu;
    private JMenuItem mntmNewMenuItem;
    // End of variables declaration  
}
