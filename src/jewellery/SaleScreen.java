package jewellery;

import com.mycompany.whatsupblasterjar.Whatsupblaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import static jewellery.DBConnect.connect;
import static jewellery.SelectAndCreateCompanyScreen.dateFY;
import jewellery.helper.RealSettingsHelper;
import jewellery.helper.numberToWord;
import jewellery.helper.outstandingAnalysisHelper;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
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
 * @author ASUS
 */
public class SaleScreen extends javax.swing.JFrame {

    ExChange ex = new ExChange();
    public cashAndBankPayments CashAndBankPaymentsSales = new cashAndBankPayments();
    String tagno = null;
    private List<Object> columnNames = new ArrayList<>();
    private final List<Object> data = new ArrayList<>();
    private final List<Object> accountNames = new ArrayList<>();
    private List<Object> partyGSTAndBalance = new ArrayList<>();
    private static final List<Object> ITEM_NAMES = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public DefaultTableModel salesListTableModel;
    private ImageIcon imageIcon;
//    private final PurchaseItemsDetailDialog purchaseItemsDetailsDialog = new PurchaseItemsDetailDialog(this, false, 0);
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private final DefaultTableModel itemNameSuggestionsTableModel;
    private final DefaultTableModel itemNameSuggestionsDetailsTableModel;
    private final DefaultTableModel partyNameSuggestionsTableModel;
    private List<Object> fieldsData;
    private int selectedRow = -1;
    private int id, billToBePrinted;

    private List<Object> states;
    private Logger logger = Logger.getLogger(SaleScreen.class.getName());
    private Connection connect;
    List<Map<String, Object>> soldItemsForCurrentBill;
    public static int billno;

    public SaleScreen() {
        Logger.getLogger(SaleScreen.class.getName()).log(Level.INFO, "sale screen object initiazed!");

        initComponents();

        initPopups();
//        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        whatsuppanel.setVisible(false);
        jPanel2.setVisible(false);
        save.setEnabled(false);
        jButton4.setEnabled(false);
        txtQty.setText("1");
        Logger.getLogger(SaleScreen.class.getName()).info("*********************************************************running sales");
        System.out.println("*********************************************************running sales");
        ex.setVisible(false);
        salesListTableModel = (DefaultTableModel) tblPurchasesList.getModel();
        itemNameSuggestionsTableModel = (DefaultTableModel) tblItemNameSuggestions.getModel();
        itemNameSuggestionsDetailsTableModel = (DefaultTableModel) tblItemNameSuggestionsDetails.getModel();
        partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
        JTableHeader header = tblPurchasesList.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 18));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int h = d.height;
        int w = d.width;
        pnlRootContainer.setSize(w - 280, h - 100);
        soldItemsForCurrentBill = new ArrayList<>();
        centerTableCells();

        populateSalesListTable();
        String itemgrp;
        setDateOnJCalender("yyyy-MM-dd");

//        setImageOnJLabel(lblDeleteButton, AssetsLocations.TRASH_CAN_ICON);
//        setImageOnJLabel(lblPrintButton, AssetsLocations.PRINT_ICON);
//        setImageOnJLabel(lblClearFields, AssetsLocations.CLEANING_BROOM_ICON);
        pmItemNameSuggestionsPopup.add(spTblItemNameSuggestionsContainer);
        pmItemNameSuggestionsPopup.setLocation(txtItemName.getX() + 200, txtItemName.getY() + 150);
        pmItemNameSuggestionsDetailsPopup.add(spTblItemNameSuggestionsDetailsContainer);
        pmItemNameSuggestionsDetailsPopup.setLocation(txtItemName.getX() + 200, txtItemName.getY() + 150);

        pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
        pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 200, txtPartyName.getY() + 150);

        txtBill.setText(String.valueOf(getBillNofromSales()));

        fetchAccountNames("Cash");
        fetchItemNames();

    }

    private void fillFieldOnClick() {
        int selectedRow = tblPurchasesList.getSelectedRow();

    }

    private void fillBillDataInoSoldItemForCurrentBillList(int billNumber) throws SQLException {

        Connection con = DBConnect.connect();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + DatabaseCredentials.SALES_TABLE + " WHERE bill = " + billNumber);
        String terms = "";
        while (rs.next()) {
            terms = rs.getString("terms");
            Map<String, Object> newEntry = new HashMap<>();
            newEntry.put("id", rs.getInt("id"));
            newEntry.put("itemname", rs.getString("itemname"));
            newEntry.put("huid", rs.getString("huid"));
            newEntry.put("grosswt", rs.getDouble("grosswt"));
            newEntry.put("beedswt", rs.getDouble("beedswt"));
            newEntry.put("netwt", rs.getDouble("netwt"));
            newEntry.put("diamondwt", rs.getDouble("diamondwt"));
            newEntry.put("diamondrate", rs.getDouble("diamondrate"));
            newEntry.put("itemdescription", rs.getString("itemdescription"));
            newEntry.put("qty", rs.getString("qty"));
            newEntry.put("rate", rs.getDouble("rate"));

            newEntry.put("per", rs.getString("lbr_per"));
            newEntry.put("labour", String.format("%.2f", rs.getDouble("labour")));
            newEntry.put("labouramtdiscount", String.format("%.2f", rs.getDouble("labour_amt_discount")));
            newEntry.put("extrachange", rs.getString("extrachange"));
            newEntry.put("basicamount", String.format("%.2f", rs.getDouble("bankamt")));
            newEntry.put("taxableamount", rs.getDouble("taxable_amount"));
            newEntry.put("gstpercent", rs.getDouble("gstpercent"));
            newEntry.put("gstamount", String.format("%.2f", rs.getDouble("gstamt")));
            newEntry.put("netamount", String.format("%.2f", rs.getDouble("netamount")));
            newEntry.put("date", rs.getDate("date"));
            newEntry.put("terms", rs.getString("terms").trim());
            newEntry.put("billnumber", rs.getInt("bill"));
            newEntry.put("partyname", rs.getString("partyname"));
            newEntry.put("gst", rs.getString("gst"));
            newEntry.put("previousbalance", rs.getString("previous_balance"));
            newEntry.put("discount", rs.getString("Discount"));
            newEntry.put("tagno", rs.getString("tagno"));
            soldItemsForCurrentBill.add(newEntry);
        }
        if ("Credit".equals(terms.trim())) {
            cmbTerms.setSelectedIndex(1);
        }
        if ("Cash".equals(terms.trim())) {
            cmbTerms.setSelectedIndex(0);
        }
    }

    public void saleRegisterRedirect(int bill) throws SQLException, FileNotFoundException {
        save.setText(("Update"));
//ExChange ex=new ExChange();
//ex.fetchData(bill);
        save.setEnabled(true);
        jButton4.setEnabled(true);
        soldItemsForCurrentBill.clear();
        billToBePrinted = bill;
        ex.fetchData(billToBePrinted);
        String statement = "SELECT total FROM `exchange` WHERE bill = " + String.valueOf(bill) + ";";
        Connection con = connect();
        double total = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            while (rs.next()) {

                total += rs.getDouble("total");

            }
            double roundedTotal = Math.round(total);

            txtexchange.setText(Double.toString(roundedTotal));

            con.close();
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ExChange.class.getName()).log(Level.SEVERE, "Failed", ex);
        }

        CashAndBankPaymentsSales.fatchData(billToBePrinted);
        fillBillDataInoSoldItemForCurrentBillList(bill);
        fillPartyDetails(bill);
        txtBill.setText(String.valueOf(bill));
        sale_Bill(bill);

        double outstandingamt = Double.parseDouble(txtgrandtotal.getText()) - Double.parseDouble(txtreceive.getText());
        double tot = 0;
        if (txtexchange.getText().trim().isEmpty()) {
            tot = outstandingamt - 0;
        } else {
            tot = outstandingamt - Double.parseDouble(txtexchange.getText().trim());
        }
        if (tot < 0) {
            txtoutstanding.setText(Double.toString(0));
        } else {
            txtoutstanding.setText(Double.toString(tot));
        }

    }

    private void fillPartyDetails(int bill) {
        DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                DatabaseCredentials.DB_PASSWORD);

        List<List<Object>> res = DBController.getDataFromTable("SELECT partyname, receivedamount FROM " + DatabaseCredentials.SALES_TABLE
                + " WHERE bill=" + bill);

        String partyname = "";
        double recamt = 0;
        for (List<Object> list : res) {
            partyname = list.get(0).toString();
            recamt = Double.parseDouble(list.get(1).toString());
        }
        txtreceive.setText(String.format("%.3f", recamt - Double.parseDouble(txtexchange.getText().trim())));

        txtPartyName.setText(partyname);
        setPartyDetails(partyname);
    }

    public void sale_Bill(int bill) throws SQLException {
        salesListTableModel.setRowCount(0);

        List<List<Object>> salesItems;

        if (DBController.isDatabaseConnected()) {
            salesItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, netwt, qty,Discount, taxable_amount, gstpercent,gstamt, netamount FROM "
                    + DatabaseCredentials.SALES_TABLE + " WHERE bill= " + "'" + bill + "'");

            salesItems.forEach((item) -> {
                salesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // taxableAmount
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // gst
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gstamt
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8),
                    (item.get(9) == null || item.get(9).toString().trim().isEmpty()) ? "NULL" : item.get(9),// netamount
                });
            });
        } else {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);

            salesItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, netwt, qty,Discount, taxable_amount, gstpercent,gstamt, netamount FROM "
                    + DatabaseCredentials.SALES_TABLE + " WHERE bill= " + "'" + bill + "'");

            salesItems.forEach((item) -> {
                salesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // taxableAmount
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // gst
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gstamt
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8),
                    (item.get(9) == null || item.get(9).toString().trim().isEmpty()) ? "NULL" : item.get(9),// netamount
                });
            });
        }
        fillgrandtotal();
    }

    int getBillNofromSales() {
        int sno = 0;
        try {
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select bill from sales");
            while (rs.next()) {
                sno = rs.getInt("bill");
            }
            sno = sno + 1;
            String q1 = "Update salebillnocounter set billno='" + sno + "'";
            s.executeUpdate(q1);
        } catch (SQLException ex) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sno;
    }

    private void fetchAccountNames(String accountType) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> account_names = DBController.executeQuery("SELECT accountname FROM "
                + DatabaseCredentials.ACCOUNT_TABLE);

        account_names.forEach((accountName) -> {
            accountNames.add(accountName.toString());
        });
    }

    public static void fetchItemNames() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> item_names = DBController.executeQuery("SELECT itemname FROM "
                + DatabaseCredentials.ENTRY_ITEM_TABLE);

        item_names.forEach((item) -> {
            ITEM_NAMES.add(item.toString());
        });
    }

    private void setFocus(KeyEvent event, JComponent component) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            component.requestFocusInWindow();
        }
    }

    private void setDateOnJCalender(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();
        try {
            datePurchaseDate.setDate(dateFormat.parse(dateTimeFormatter.format(localDateTime)));
            datePurchaseDate.setSelectableDateRange(dateFY, null);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void setImageOnJLabel(javax.swing.JLabel component, String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth() - 5,
                        component.getHeight() - 5, Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    private void autoComplete(List<Object> autoCompletionSource,
            String textToAutoComplete, JTextField txtField) {
        String complete = "";
        int start = textToAutoComplete.length();
        int last = textToAutoComplete.length();

        for (int idx = 0; idx < autoCompletionSource.size(); idx++) {
            if (autoCompletionSource.get(idx).toString().startsWith(textToAutoComplete)) {
                complete = autoCompletionSource.get(idx).toString();
                last = complete.length();
                break;
            }
        }
        if (last > start) {
            txtField.setText(complete);
            txtField.setCaretPosition(last);
            txtField.moveCaretPosition(start);
        }
    }

    private List setItemRate(String itemName) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
        List<Object> itemRate = null;
        List<Object> itemGroup = DBController.executeQuery("SELECT itemgroup FROM "
                + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + itemName + "'");
        System.out.print(DatabaseCredentials.ENTRY_ITEM_TABLE);
        if (itemGroup != null && (!itemGroup.isEmpty())) {
            System.out.print("Entered if");

            switch (itemGroup.get(0).toString()) {
                case "Gold 16 Carat":

                    itemRate = DBController.executeQuery("SELECT gold16 FROM "
                            + DatabaseCredentials.DAILY_UPDATE_TABLE
                            + " WHERE date = " + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
                    break;
                case "Gold 18 Carat":
                    itemRate = DBController.executeQuery("SELECT gold18 FROM "
                            + DatabaseCredentials.DAILY_UPDATE_TABLE
                            + " WHERE date = " + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
                    break;
                case "Gold 22 Carat":
                    itemRate = DBController.executeQuery("SELECT gold22 FROM "
                            + DatabaseCredentials.DAILY_UPDATE_TABLE
                            + " WHERE date = " + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
                    break;
                case "Gold 24 Carat":
                    itemRate = DBController.executeQuery("SELECT gold24 FROM "
                            + DatabaseCredentials.DAILY_UPDATE_TABLE
                            + " WHERE date = " + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
                    break;
                case "Silver":
                    itemRate = DBController.executeQuery("SELECT silver FROM "
                            + DatabaseCredentials.DAILY_UPDATE_TABLE
                            + " WHERE date = " + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'");
                    break;
                default:
                    break;
            }

            if (itemRate != null && (!itemRate.isEmpty())) {
                txtRate.setText(itemRate.get(0).toString());
            }
        }
        return itemRate;
    }

    void getHuid(String name, String tagno, String itemgrp) {
        String huid = null;
        String grosswt = null;
        String beedswt = null;
        String netwt = null;
        String diamondwt = null;
        String carat = null;
        String gst = null;
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }
//        JOptionPane.showMessageDialog(this, "testsachin item name "+name);
//        JOptionPane.showMessageDialog(this, "testsachin tag no name "+tagno);
        List<Object> itemGroup = DBController.executeQuery("SELECT grosswt,huid,beedswt,netwt,diamondwt,carats,taxslab FROM "
                + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + name + "' AND tagno = '" + tagno + "' AND itemgroup = '" + itemgrp + "';");
//           for(Object values:itemGroup){
//              JOptionPane.showMessageDialog(this, "testsachin tag no name "+values); 
//           }
        try {
            if (itemGroup.get(1) != null) {
                huid = ("".equals(itemGroup.get(1).toString())) ? "" : itemGroup.get(1).toString();
            }

            grosswt = itemGroup.get(0).toString();
            if (itemGroup.get(2) == null) {
                beedswt = "0.0";
            } else {
                beedswt = itemGroup.get(2).toString();
            }

            netwt = itemGroup.get(3).toString();

            if (itemGroup.get(4) == null) {
                diamondwt = "0.0";
            } else {
                diamondwt = itemGroup.get(4).toString();
            }

            if (itemGroup.get(5) == null) {
                carat = "0.0";
            } else {
                carat = itemGroup.get(5).toString();
            }

            if (itemGroup.get(6) == null) {
                gst = "0.0";
            } else {
                gst = itemGroup.get(6).toString();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "testpawan " + e);

        }

//              JOptionPane.showMessageDialog(this, "testsachin huid name "+huid); 
//              JOptionPane.showMessageDialog(this, "testsachin gro name "+grosswt); 
//              JOptionPane.showMessageDialog(this, "testsachin beed name "+beedswt); 
//              JOptionPane.showMessageDialog(this, "testsachin net wt name "+netwt); 
//              JOptionPane.showMessageDialog(this, "testsachin diamond name "+diamondwt); 
//              JOptionPane.showMessageDialog(this, "testsachin carat name "+carat); 
//              JOptionPane.showMessageDialog(this, "testsachin gst name "+gst); 
        if (RealSettingsHelper.gettagNoIsTrue()) {
            if ("N.A".equals(tagno)) {
                if (jComboBox1.getSelectedItem().equals("No GST")) {

                    txtGSTPercent.setText("0");

                } else {
                    txtGSTPercent.setText(gst);
                }
                txthuid.requestFocus();

            } else {
                if (huid == null) {
                    txthuid.setText("");
                } else {
                    txthuid.setText(huid);
                }

                txtGrossWt.setText(grosswt);

                txtBeedsWt.setText(beedswt);

                txtNetWt.setText(netwt);

                if (jComboBox1.getSelectedItem().equals("No GST")) {

                    txtGSTPercent.setText("0");

                } else {
                    txtGSTPercent.setText(gst);
                }

                txtDiamondWt.setText(diamondwt);
                txtItemDescription.requestFocus();
            }
        } else {
            if ("N.A".equals(tagno)) {
                if (jComboBox1.getSelectedItem().equals("No GST")) {

                    txtGSTPercent.setText("0");

                } else {
                    txtGSTPercent.setText(gst);
                }
                txthuid.requestFocus();

            } else {
                if (huid == null) {
                    txthuid.setText("");
                } else {
                    txthuid.setText(huid);
                }

                txtGrossWt.setText(grosswt);

                txtBeedsWt.setText(beedswt);

                txtNetWt.setText(netwt);
                if (jComboBox1.getSelectedItem().equals("No GST")) {

                    txtGSTPercent.setText("0");

                } else {
                    txtGSTPercent.setText(gst);
                }
                txtDiamondWt.setText(diamondwt);
                txtItemDescription.requestFocus();

            }
        }
        txtDiamondRate.setText(carat);

        if (jComboBox1.getSelectedItem().equals("No GST")) {

            txtGSTPercent.setText("0");

        } else {
            txtGSTPercent.setText(gst.replaceAll("%", ""));
        }

//    JOptionPane.showMessageDialog(this, "testsachin ending of enter items"); 
    }

    void getGST(String name) {
        String huid = null;
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> itemGroup = DBController.executeQuery("SELECT taxslab FROM "
                + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + name + "'");

        if (itemGroup != null && (!itemGroup.isEmpty())) {

            List<Object> itemRate = null;

            huid = itemGroup.get(0).toString();
        }
        int sepPos = huid.lastIndexOf("%");

        txtGSTPercent.setText(huid.substring(0, sepPos));
    }

    private boolean fieldsAreValidated() {
        if (UtilityMethods.isTextFieldEmpty(txtPartyName)) {
//                || !UtilityMethods.inputOnlyContainsAlphabets(txtPartyName.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter the party name correctly");
            return false;
        } else if (UtilityMethods.isTextFieldEmpty(txtItemName)) {
//                || !UtilityMethods.inputContainsAlphabetsAndNumbers(txtItemName.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter the item name correctly");
            return false;
        }

        return true;
    }

    private void setBg() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setBackground(Color.white);
            }
        }

    }

    private void clearTextFields() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }

    }

    private void clearTextFieldsEnter() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                if (component == txtPartyName) {
                    continue;
                }
                if (component == txtBill) {
                    continue;
                }
                if (component == txtreceive) {
                    continue;
                }
                if (component == txtoutstanding) {
                    continue;
                }
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }

    }

    private void clearLabels() {
        lblGST.setText("");
        lblPreviousBalance.setText("");
        lblState.setText("");
        lblAddress.setText("");
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblPurchasesList
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setComponentsEnabledOrDisabled() {
        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setEnabled(!textComponent.isEnabled());
            }
        }
    }

    private void populateSalesListTable() {
        if (!txtBill.getText().trim().isEmpty()) {
            salesListTableModel.setRowCount(0);

            List<List<Object>> salesItems;

            if (DBController.isDatabaseConnected()) {
                salesItems = DBController.getDataFromTable("SELECT id,"
                        + "itemname, huid, netwt, qty,Discount, taxable_amount, gstpercent,gstamt, netamount FROM "
                        + DatabaseCredentials.SALES_TABLE + " WHERE date = "
                        + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'" + " AND bill= " + "'" + txtBill.getText().trim() + "'");

                salesItems.forEach((item) -> {
                    salesListTableModel.addRow(new Object[]{
                        (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                        (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                        (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                        (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                        (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), //discount
                        (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableAmount
                        (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gst
                        (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // gstamt
                        (item.get(9) == null || item.get(9).toString().trim().isEmpty()) ? "NULL" : item.get(9),// netamount
                    });
                });
            } else {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);

                salesItems = DBController.getDataFromTable("SELECT id,"
                        + "itemname, huid, netwt, qty,Discount, taxable_amount, gstpercent,gstamt, netamount FROM "
                        + DatabaseCredentials.SALES_TABLE + " WHERE date = "
                        + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'" + " AND bill= " + "'" + txtBill.getText().trim() + "'");

                salesItems.forEach((item) -> {
                    salesListTableModel.addRow(new Object[]{
                        (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                        (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                        (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                        (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                        (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), //discount
                        (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableAmount
                        (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gst
                        (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // gstamt
                        (item.get(9) == null || item.get(9).toString().trim().isEmpty()) ? "NULL" : item.get(9),// netamount
                    });
                });
            }
            fillgrandtotal();
        }
    }

    private void verifyNetWeightCalculationFields() {
        double grossWt;
        double beedsWt;

        if (txtGrossWt.getText().trim().isEmpty() && txtBeedsWt.getText().trim().isEmpty()) {
            txtGrossWt.setText("1");
            txtBeedsWt.setText("0");

            grossWt = Double.valueOf(txtGrossWt.getText().trim());
            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));

        } else if (txtGrossWt.getText().trim().isEmpty()) {
            txtGrossWt.setText("1");

            grossWt = Double.valueOf(txtGrossWt.getText().trim());
            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
        } else if (txtBeedsWt.getText().trim().isEmpty()) {
            txtBeedsWt.setText("0");

            grossWt = Double.valueOf(txtGrossWt.getText().trim());
            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
        } else {
            grossWt = Double.valueOf(txtGrossWt.getText().trim());
            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
        }
    }

    private void calculateNetAmount() {
        if (!(txtTaxableAmt.getText().trim().isEmpty() || txtGSTPercent.getText().trim().isEmpty())) {
            double taxableAmount = Double.parseDouble(txtTaxableAmt.getText());
            float gstPercent = Float.parseFloat(txtGSTPercent.getText());

            double gstamount = (taxableAmount
                    * (UtilityMethods.numberToDecimalPoint(gstPercent)));
            txtGSTAmt.setText(String.format("%.3f", gstamount));
            txtNetAmt.setText(String.format("%.3f", taxableAmount + (taxableAmount
                    * (UtilityMethods.numberToDecimalPoint(gstPercent)))));
        }
    }

    private double getlabourcharge() {
        String per = cmbPer.getSelectedItem().toString();
        double labour = 0;
        double labourper = Double.parseDouble(txtExtraCharge1.getText());
        double netWeight = Double.parseDouble(txtNetWt.getText());
        double rate = Double.parseDouble(txtRate.getText());
        if (per.equals("Gram")) {
            labour = netWeight * labourper;

        } else if (per.equals("Piece")) {
            labour = labourper;
        } else {
            labour = (netWeight * rate) * (labourper / 100.0);
        }
        return labour;
    }

    private void calculateBasicAmount() {
        String per = cmbPer.getSelectedItem().toString();
        double labour = 100;
        if (!(txtNetWt.getText().trim().isEmpty() || txtDiamondWt.getText().trim().isEmpty()
                || txtDiamondRate.getText().trim().isEmpty() || txtExtraCharge.getText().trim().isEmpty())) {
            double labourper = Double.parseDouble(txtExtraCharge1.getText());
            double netWeight = Double.parseDouble(txtNetWt.getText());
            double rate = Double.parseDouble(txtRate.getText());
            if (per.equals("Gram")) {
                labour = netWeight * labourper;
            } else if (per.equals("Piece")) {
                labour = labourper;
            } else {
                labour = (netWeight * rate) * (labourper / 100.0);
            }
//            double labourdis = Double.parseDouble(txtExtraCharge2.getText());
            double diamondWt = Double.parseDouble(txtDiamondWt.getText());
            double diamondRate = Double.parseDouble(txtDiamondRate.getText());
            double extraCharge = Double.parseDouble(txtExtraCharge.getText());

            double basicAmount = (netWeight * rate) + (diamondWt * diamondRate) + extraCharge + (labour);
//            if(!(txtDiscount.getText().trim().isEmpty())){
//               basicAmount-=Double.parseDouble(txtDiscount.getText());
//            }

            txtBasicAmt.setText(String.format("%.3f", basicAmount));

        }
    }

    private void setPartyDetails(String partyName) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        partyGSTAndBalance = DBController.executeQuery("SELECT gstno, opbal, address, state, statecode FROM "
                + DatabaseCredentials.ACCOUNT_TABLE + " WHERE accountname = '" + partyName + "'");

        if (partyGSTAndBalance.get(0) != null) {
            lblGST.setText(partyGSTAndBalance.get(0).toString());
        } else {
            lblGST.setText("Not Specified");
        }

        if (partyGSTAndBalance.get(1) != null) {
            try {
//                JOptionPane.showMessageDialog(this,outstandingAnalysisHelper.fillTableInDateGivenParty(partyName) );
                lblPreviousBalance.setText(outstandingAnalysisHelper.fillTableInDateGivenParty(partyName));
            } catch (ParseException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            lblPreviousBalance.setText("0.0");
        }

        if (partyGSTAndBalance.get(2) != null) {
            lblAddress.setText(partyGSTAndBalance.get(2).toString());
        } else {
            lblAddress.setText("---");
        }

        if (partyGSTAndBalance.get(3) != null) {
            lblState.setText(partyGSTAndBalance.get(3).toString() + "-" + partyGSTAndBalance.get(4).toString());
        } else {
            lblState.setText("---");
        }
    }

    private void tblItemNameSuggestionsDetailsKeyPressed(java.awt.event.KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                txtItemName.setText(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                setItemRate(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                tagno = tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 0).toString();
                getHuid(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 0).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 2).toString()
                );
//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			txtQty.setText("1");
                pmItemNameSuggestionsDetailsPopup.setVisible(false);
                break;
            case KeyEvent.VK_DOWN:
                tblItemNameSuggestionsDetails.requestFocus();
                if (selectedrow == 0) {
                    tblItemNameSuggestionsDetails.setRowSelectionInterval(0, 0);
                    selectedrow++;
                } else {
                    if (tblItemNameSuggestionsDetails.getSelectedRow() < tblItemNameSuggestionsDetails.getRowCount() - 1) {
                        tblItemNameSuggestionsDetails.setRowSelectionInterval(tblItemNameSuggestionsDetails.getSelectedRow() + 1, tblItemNameSuggestionsDetails.getSelectedRow() + 1);
                    }
                }
                txtItemName.setText(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                setItemRate(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                tagno = tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 0).toString();
                getHuid(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 0).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 2).toString());
//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			txtQty.setText("1");
                pmItemNameSuggestionsDetailsPopup.setVisible(false);
                break;
            case KeyEvent.VK_UP:
                tblItemNameSuggestionsDetails.requestFocus();

                if (tblItemNameSuggestionsDetails.getSelectedRow() > 0) {
                    tblItemNameSuggestionsDetails.setRowSelectionInterval(tblItemNameSuggestionsDetails.getSelectedRow() - 1, tblItemNameSuggestionsDetails.getSelectedRow() - 1);
                }

                txtItemName.setText(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                setItemRate(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString());
                tagno = tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 0).toString();
                getHuid(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                        .getSelectedRow(), 1).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 0).toString(),
                        tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 2).toString());
//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			txtQty.setText("1");
                pmItemNameSuggestionsDetailsPopup.setVisible(false);
                break;
        }

    }

    private void tblItemNameSuggestionsDetailsKeyReleased(java.awt.event.KeyEvent evt) {

    }

    private void initPopups() {
        pmItemNameSuggestionsDetailsPopup = new javax.swing.JPopupMenu();
        spTblItemNameSuggestionsDetailsContainer = new javax.swing.JScrollPane();
        tblItemNameSuggestionsDetails = new javax.swing.JTable();

        pmItemNameSuggestionsDetailsPopup.setMinimumSize(new java.awt.Dimension(200, 200));

        tblItemNameSuggestionsDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Tag No.", "Item Name", "Item Group", "Net.W", "Huid"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblItemNameSuggestionsDetails.setOpaque(false);
        tblItemNameSuggestionsDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemNameSuggestionsDetailsMouseClicked(evt);
            }
        });
//        tblItemNameSuggestionsDetails.addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                tblItemNameSuggestionsDetailsKeyPressed(evt);
//            }
//
//            public void keyReleased(java.awt.event.KeyEvent evt) {
//                tblItemNameSuggestionsDetailsKeyReleased(evt);
//            }
//        });

        spTblItemNameSuggestionsDetailsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsDetailsContainerFocusLost(evt);
            }
        });
        spTblItemNameSuggestionsDetailsContainer.setViewportView(tblItemNameSuggestionsDetails);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmItemNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblItemNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblItemNameSuggestions = new javax.swing.JTable();
        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        pnlRootContainer = new javax.swing.JPanel();
        whatsuppanel = new javax.swing.JPanel();
        type1 = new javax.swing.JCheckBox();
        type2 = new javax.swing.JCheckBox();
        type3 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        phnno = new javax.swing.JTextField();
        Send = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        txtBeedsWt = new javax.swing.JTextField();
        txtNetWt = new javax.swing.JTextField();
        txtDiamondWt = new javax.swing.JTextField();
        txtDiamondRate = new javax.swing.JTextField();
        txtGrossWt = new javax.swing.JTextField();
        txtItemDescription = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtRate = new javax.swing.JTextField();
        txtExtraCharge = new javax.swing.JTextField();
        txtBasicAmt = new javax.swing.JTextField();
        cmbPer = new javax.swing.JComboBox<>();
        jLabel55 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtBill = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        datePurchaseDate = new com.toedter.calendar.JDateChooser();
        cmbTerms = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtTaxableAmt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtGSTPercent = new javax.swing.JTextField();
        txtNetAmt = new javax.swing.JTextField();
        lblAddPartyName = new javax.swing.JLabel();
        lblEditPartyName = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        lblPreviousBalance = new javax.swing.JLabel();
        lblGST = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtExtraCharge1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtExtraCharge2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txthuid = new javax.swing.JTextField();
        save = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtcount = new javax.swing.JTextField();
        txtgrandtotal = new javax.swing.JTextField();
        txtnetwt = new javax.swing.JTextField();
        txtGSTAmt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtdiscount = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtreceive = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtoutstanding = new javax.swing.JTextField();
        closebtn = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        txtexchange = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        exbtn = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        paymentbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchasesList = new javax.swing.JTable();
        jLabel44 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();

        pmItemNameSuggestionsPopup.setMinimumSize(new java.awt.Dimension(200, 200));

        spTblItemNameSuggestionsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsContainerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsContainerFocusLost(evt);
            }
        });

        tblItemNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Item Group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemNameSuggestions.setOpaque(false);
        tblItemNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemNameSuggestionsMouseClicked(evt);
            }
        });
        spTblItemNameSuggestionsContainer.setViewportView(tblItemNameSuggestions);

        pmPartyNameSuggestionsPopup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pmPartyNameSuggestionsPopupFocusLost(evt);
            }
        });

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Party Name", "State", "GRP"
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
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlRootContainer.setBackground(new java.awt.Color(57, 68, 76));
        pnlRootContainer.setForeground(new java.awt.Color(189, 150, 117));
        pnlRootContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pnlRootContainerFocusLost(evt);
            }
        });
        pnlRootContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlRootContainerMouseClicked(evt);
            }
        });
        pnlRootContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        whatsuppanel.setBackground(new java.awt.Color(153, 255, 153));
        whatsuppanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        whatsuppanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        type1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        type1.setText("Type 1");
        type1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                type1MouseClicked(evt);
            }
        });
        type1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                type1ActionPerformed(evt);
            }
        });
        whatsuppanel.add(type1, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 43, -1, -1));

        type2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        type2.setText("Type 2");
        type2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                type2MouseClicked(evt);
            }
        });
        whatsuppanel.add(type2, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 43, -1, -1));

        type3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        type3.setText("Type 3");
        type3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                type3MouseClicked(evt);
            }
        });
        whatsuppanel.add(type3, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 43, -1, -1));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel5.setText("Phone no");
        whatsuppanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 79, 105, -1));

        phnno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                phnnoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                phnnoFocusLost(evt);
            }
        });
        whatsuppanel.add(phnno, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 75, 278, -1));

        Send.setBackground(new java.awt.Color(0, 102, 51));
        Send.setText("Send");
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });
        whatsuppanel.add(Send, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 176, 104, -1));

        jLabel10.setBackground(new java.awt.Color(0, 102, 51));
        jLabel10.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 51));
        jLabel10.setText("Send Whatsup");
        whatsuppanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 137, -1));

        jLabel37.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel37.setText("Message");
        whatsuppanel.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 118, 105, -1));

        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        whatsuppanel.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 114, 278, -1));

        jLabel40.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel40.setText("Update System Name");
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });
        whatsuppanel.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, -1, -1));

        jLabel42.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(204, 0, 0));
        jLabel42.setText("X");
        jLabel42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel42MouseClicked(evt);
            }
        });
        whatsuppanel.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 20, -1));

        jLabel43.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(204, 0, 51));
        jLabel43.setText("Do Not Close Browser");
        whatsuppanel.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 220, -1));

        pnlRootContainer.add(whatsuppanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 190, 450, 240));

        txtItemName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemNameFocusLost(evt);
            }
        });
        txtItemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemNameActionPerformed(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 160, 25));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("<html>Item Name<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        txtQty.setEditable(true);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        txtQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtyActionPerformed(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 170, 65, 25));

        txtBeedsWt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBeedsWtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBeedsWtFocusLost(evt);
            }
        });
        txtBeedsWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBeedsWtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtBeedsWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 75, 25));

        txtNetWt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNetWtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetWtFocusLost(evt);
            }
        });
        txtNetWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetWtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtNetWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 50, 25));

        txtDiamondWt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiamondWtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondWtFocusLost(evt);
            }
        });
        txtDiamondWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondWtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtDiamondWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 170, 82, 25));

        txtDiamondRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiamondRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondRateFocusLost(evt);
            }
        });
        txtDiamondRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiamondRateActionPerformed(evt);
            }
        });
        txtDiamondRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondRateKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtDiamondRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 170, 135, 25));

        txtGrossWt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGrossWtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGrossWtFocusLost(evt);
            }
        });
        txtGrossWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGrossWtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtGrossWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 74, 25));

        txtItemDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemDescriptionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemDescriptionFocusLost(evt);
            }
        });
        txtItemDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemDescriptionActionPerformed(evt);
            }
        });
        txtItemDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemDescriptionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtItemDescriptionKeyTyped(evt);
            }
        });
        pnlRootContainer.add(txtItemDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 170, 120, 25));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 188, 81));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("<html>Qty.<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 150, 56, -1));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(238, 188, 81));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Beeds Wt.");
        pnlRootContainer.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 150, 75, -1));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 188, 81));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Net Wt.");
        pnlRootContainer.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Diamond Wt.");
        pnlRootContainer.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 150, -1, -1));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Diamond Rate / Carat");
        pnlRootContainer.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, -1, -1));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 188, 81));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("<html>Gross Wt.<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 74, -1));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 188, 81));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Item Descriptions");
        pnlRootContainer.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 150, 120, -1));

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(238, 188, 81));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("<html>Per<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 43, -1));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(238, 188, 81));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Rate");
        pnlRootContainer.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(238, 188, 81));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Extra charge");
        pnlRootContainer.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, -1, -1));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(238, 188, 81));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Discount");
        pnlRootContainer.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 200, 76, -1));

        txtRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRateFocusLost(evt);
            }
        });
        txtRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRateKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 90, 25));

        txtExtraCharge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtExtraChargeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExtraChargeFocusLost(evt);
            }
        });
        txtExtraCharge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExtraChargeKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtExtraCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, 70, 25));

        txtBasicAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBasicAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBasicAmtFocusLost(evt);
            }
        });
        txtBasicAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBasicAmtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtBasicAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, 80, 25));

        cmbPer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gram", "Piece", "Percentage %" }));
        cmbPer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbPerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbPerFocusLost(evt);
            }
        });
        cmbPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbPerKeyReleased(evt);
            }
        });
        pnlRootContainer.add(cmbPer, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 66, 25));

        jLabel55.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(238, 188, 81));
        jLabel55.setText("<html>Party Name<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

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
        pnlRootContainer.add(txtPartyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 213, 25));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("<html>Bill<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, -1, 20));

        txtBill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBillFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillFocusLost(evt);
            }
        });
        txtBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillActionPerformed(evt);
            }
        });
        txtBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBillKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 90, 25));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("GST No.");
        pnlRootContainer.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 60, -1, 20));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Previous Balance");
        pnlRootContainer.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, 20));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SALE");
        pnlRootContainer.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 10, 218, 66));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("<html>DATE<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 60, 20));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("<html>TERMS<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 70, 20));
        pnlRootContainer.add(datePurchaseDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 44, 159, 25));

        cmbTerms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit" }));
        cmbTerms.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTermsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbTermsFocusLost(evt);
            }
        });
        cmbTerms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbTermsMouseClicked(evt);
            }
        });
        cmbTerms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTermsActionPerformed(evt);
            }
        });
        cmbTerms.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbTermsPropertyChange(evt);
            }
        });
        cmbTerms.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbTermsKeyReleased(evt);
            }
        });
        pnlRootContainer.add(cmbTerms, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 44, 159, 25));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Taxable Amt");
        pnlRootContainer.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 200, -1, -1));

        txtTaxableAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTaxableAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTaxableAmtFocusLost(evt);
            }
        });
        txtTaxableAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTaxableAmtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTaxableAmtKeyTyped(evt);
            }
        });
        pnlRootContainer.add(txtTaxableAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 220, 100, 25));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("GST [%]");
        pnlRootContainer.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 200, -1, -1));

        txtGSTPercent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGSTPercentFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTPercentFocusLost(evt);
            }
        });
        txtGSTPercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGSTPercentKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtGSTPercent, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 220, 44, 25));

        txtNetAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNetAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetAmtFocusLost(evt);
            }
        });
        txtNetAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNetAmtActionPerformed(evt);
            }
        });
        txtNetAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetAmtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtNetAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 220, 90, 25));
        pnlRootContainer.add(lblAddPartyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(382, 124, 38, 35));

        lblEditPartyName.setForeground(new java.awt.Color(255, 255, 255));
        pnlRootContainer.add(lblEditPartyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 124, 38, 35));

        jLabel56.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(238, 188, 81));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("State");
        pnlRootContainer.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 20));

        lblPreviousBalance.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPreviousBalance.setForeground(new java.awt.Color(255, 255, 255));
        lblPreviousBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlRootContainer.add(lblPreviousBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 176, 22));

        lblGST.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblGST.setForeground(new java.awt.Color(255, 255, 255));
        lblGST.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlRootContainer.add(lblGST, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 60, 170, 30));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(238, 188, 81));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Address");
        pnlRootContainer.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 120, -1, 20));

        lblAddress.setForeground(new java.awt.Color(255, 255, 255));
        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlRootContainer.add(lblAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 110, 110, 30));

        lblState.setForeground(new java.awt.Color(255, 255, 255));
        lblState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlRootContainer.add(lblState, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 210, 20));

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(238, 188, 81));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("<html>Labour<font color=\"red\">*</font></html>");
        pnlRootContainer.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 101, -1));

        txtExtraCharge1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtExtraCharge1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExtraCharge1FocusLost(evt);
            }
        });
        txtExtraCharge1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExtraCharge1KeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtExtraCharge1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 101, 25));

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(238, 188, 81));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Labour Amt");
        pnlRootContainer.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, -1, -1));

        txtExtraCharge2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtExtraCharge2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExtraCharge2FocusLost(evt);
            }
        });
        txtExtraCharge2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExtraCharge2KeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtExtraCharge2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, 78, 25));

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(238, 188, 81));
        jLabel27.setText("HUID");
        pnlRootContainer.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 60, -1));

        txthuid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txthuidFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txthuidFocusLost(evt);
            }
        });
        txthuid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthuidActionPerformed(evt);
            }
        });
        txthuid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txthuidKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txthuidKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txthuid, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 108, 25));

        save.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        save.setForeground(new java.awt.Color(238, 188, 81));
        save.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        save.setText("Save");
        save.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveMouseEntered(evt);
            }
        });
        pnlRootContainer.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 520, 191, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setText("Count :");
        pnlRootContainer.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("Net Weight :");
        pnlRootContainer.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 560, -1, 20));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(238, 188, 81));
        jLabel20.setText("Grand Total :");
        pnlRootContainer.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 520, -1, 20));

        txtcount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtcount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtcountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtcountFocusLost(evt);
            }
        });
        pnlRootContainer.add(txtcount, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, 70, 23));

        txtgrandtotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtgrandtotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtgrandtotalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgrandtotalFocusLost(evt);
            }
        });
        txtgrandtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtgrandtotalActionPerformed(evt);
            }
        });
        txtgrandtotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtgrandtotalKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtgrandtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 520, 144, 23));

        txtnetwt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtnetwt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnetwtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtnetwtFocusLost(evt);
            }
        });
        pnlRootContainer.add(txtnetwt, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 560, 143, 23));

        txtGSTAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGSTAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTAmtFocusLost(evt);
            }
        });
        txtGSTAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGSTAmtKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtGSTAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 220, 92, 25));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("GST Amt");
        pnlRootContainer.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 200, 65, -1));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 188, 81));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Net Amt.");
        pnlRootContainer.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 200, 65, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 188, 81));
        jLabel24.setText("Discount :");
        pnlRootContainer.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, -1, 20));

        txtdiscount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtdiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtdiscountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtdiscountFocusLost(evt);
            }
        });
        txtdiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtdiscountKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtdiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 520, 71, 23));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(238, 188, 81));
        jLabel25.setText("Received :");
        pnlRootContainer.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 520, -1, 20));

        txtreceive.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtreceive.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtreceiveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtreceiveFocusLost(evt);
            }
        });
        txtreceive.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtreceiveKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtreceive, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 90, 23));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(238, 188, 81));
        jLabel34.setText("Outstanding :");
        pnlRootContainer.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 520, 90, 20));

        txtoutstanding.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtoutstanding.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtoutstandingFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtoutstandingFocusLost(evt);
            }
        });
        txtoutstanding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtoutstandingActionPerformed(evt);
            }
        });
        pnlRootContainer.add(txtoutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 520, 98, 23));

        closebtn.setBackground(new java.awt.Color(255, 0, 0));
        closebtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        closebtn.setText("Close");
        closebtn.setMaximumSize(new java.awt.Dimension(111, 57));
        closebtn.setMinimumSize(new java.awt.Dimension(111, 57));
        closebtn.setPreferredSize(new java.awt.Dimension(111, 57));
        closebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebtnActionPerformed(evt);
            }
        });
        pnlRootContainer.add(closebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 70, 31));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plusSignGreen.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        pnlRootContainer.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 34, 30));

        txtexchange.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtexchange.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtexchangeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtexchangeFocusLost(evt);
            }
        });
        txtexchange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtexchangeActionPerformed(evt);
            }
        });
        txtexchange.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtexchangeKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtexchange, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 560, 90, 23));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(238, 188, 81));
        jLabel35.setText("Exchange :");
        pnlRootContainer.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 560, -1, 20));

        exbtn.setText("Exchange");
        exbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exbtnMouseEntered(evt);
            }
        });
        exbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exbtnActionPerformed(evt);
            }
        });
        pnlRootContainer.add(exbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, -1, -1));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(238, 188, 81));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Basic Amt");
        pnlRootContainer.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, 76, -1));

        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountFocusLost(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, 80, 25));

        paymentbtn.setText("Payment");
        paymentbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentbtnMouseClicked(evt);
            }
        });
        pnlRootContainer.add(paymentbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 560, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 153, 51));
        jButton1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton1.setText("WhatsApp");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlRootContainer.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 100, 100, 40));

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setText("Delete");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        pnlRootContainer.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, -1, 31));

        jButton4.setText("Print");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        pnlRootContainer.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 70, 31));

        jButton5.setText("Refresh");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        pnlRootContainer.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, -1, 31));

        jPanel2.setBackground(new java.awt.Color(255, 153, 153));
        jPanel2.setAlignmentX(0);
        jPanel2.setAlignmentY(0);

        jLabel38.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel38.setText("Enter System Name");

        jButton2.setBackground(new java.awt.Color(0, 153, 51));
        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel39.setText("Go To : C:\\Users\\ and select user name");

        jLabel41.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 0, 0));
        jLabel41.setText("X");
        jLabel41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel41MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel41))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel38)
                        .addGap(27, 27, 27)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel39))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlRootContainer.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, -1, 110));

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tblPurchasesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "HUID", "Net Wt.", "Qty.", "Discount", "Taxable Amt.", "GST [%]", "GST Amount", "Net Amt."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPurchasesList.setRowHeight(36);
        tblPurchasesList.getTableHeader().setReorderingAllowed(false);
        tblPurchasesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchasesListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPurchasesList);

        pnlRootContainer.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 1020, 250));

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(238, 188, 81));
        jLabel44.setText("GST Type:");
        pnlRootContainer.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, -1, 20));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GST", "No GST" }));
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBox1FocusLost(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox1KeyReleased(evt);
            }
        });
        pnlRootContainer.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, -1, 25));

        jButton6.setBackground(new java.awt.Color(0, 153, 51));
        jButton6.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton6.setText("Scan WhatsApp");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jButton6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton6KeyPressed(evt);
            }
        });
        pnlRootContainer.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 100, 110, 40));

        getContentPane().add(pnlRootContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1050, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void fillgrandtotal() {
        double netwt = 0.0, total = 0.0;
        txtcount.setText(Integer.toString(salesListTableModel.getRowCount()));

        for (int i = 0; i < tblPurchasesList.getRowCount(); i++) {
            total += Double.parseDouble(tblPurchasesList.getValueAt(i, 9).toString());
            netwt += Double.parseDouble(tblPurchasesList.getValueAt(i, 3).toString());
//       JOptionPane.showMessageDialog(this, total);
        }

        long total_value = Math.round(total);
        txtnetwt.setText(String.format("%.3f", netwt));

        //   total -= Double.parseDouble(txtexchange.getText());
        txtgrandtotal.setText(Long.toString(total_value));

        if (cmbTerms.getSelectedItem().toString().equals("Cash")) {
            txtreceive.setText(Long.toString(total_value));

        }
        if (cmbTerms.getSelectedItem().toString().equals("Credit")) {
            txtreceive.setText(Long.toString(total_value));

        }

    }

    public static int getSaleBillNo(String partyName) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> partyBillNo = DBController.executeQuery("SELECT bill FROM "
                + DatabaseCredentials.SALES_TABLE + " WHERE partyName = "
                + "'" + partyName + "'");

        if (partyBillNo != null && partyBillNo.size() > 0) {
            return Integer.parseInt(partyBillNo.get(0).toString());
        }

        return 0;
    }

    void addtodueamt() {
        double dueamt = 0;
        try {
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            Connection c1 = DBConnect.connect();
            Statement s1 = c1.createStatement();
            ResultSet rs = s.executeQuery("select dueamt from account where accountname='" + txtPartyName.getText().trim() + "'");
            while (rs.next()) {
                dueamt = rs.getDouble("dueamt");
            }
            dueamt = dueamt + Double.parseDouble(txtNetAmt.getText().trim());

            s1.executeUpdate("Update account set dueamt='" + dueamt + "' where accountname='" + txtPartyName.getText().trim() + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        logger.info(String.valueOf(suggestions.size()));
        suggestionsTable.setRowCount(0);

        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),});
        });
        spTblItemNameSuggestionsContainer.setVisible(true);

        fillgrandtotal();

    }

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
        fillgrandtotal();

    }

    private int getBillNo() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> billNo = DBController.executeQuery("SELECT billno FROM "
                + DatabaseCredentials.SALE_BILL_NO_COUNTER_TABLE);
        if (billNo.get(0) != null) {
            return Integer.parseInt(billNo.get(0).toString());
        }

        return 0;

    }

    private List<String> getPartyDetails(String partyname) {
        // name, address, state, mobile, gstin
        List<String> result = new ArrayList<>();
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "SELECT * FROM " + DatabaseCredentials.ACCOUNT_TABLE + " WHERE accountname = '" + partyname + "'";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                if (!(rs.getString("grp").trim().equals("Cash"))) {
                    result.add(partyname);
                    result.add(rs.getObject("address") != null ? rs.getString("address") : "");
                    result.add(rs.getObject("state") != null ? rs.getString("state") : "");
                    result.add(rs.getObject("statecode") != null ? rs.getString("statecode") : "");
                    result.add(rs.getObject("mobile1") != null ? String.valueOf(rs.getLong("mobile1")) : "");
                    result.add(rs.getObject("gstno") != null ? rs.getString("gstno") : "");
                } else {
                    try {
                        Statement stmt = con.createStatement();
                        String sql = "select * from cashpurchasedetails order by id desc";
                        List<List<Object>> companyState = DBController.getDataFromTableforcompany("SELECT state, state_code FROM company "
                                + "WHERE companyname = '"
                                + GLOBAL_VARS.SELECTED_COMPANY + "';");

                        ResultSet res = stmt.executeQuery(sql);
                        while (res.next()) {
//                         result.add(partyname);
//                result.add(res.getObject("name") != null ? res.getString("name") : "");
//                result.add(res.getObject("address") != null ? res.getString("address") : "");
                            result.add(res.getObject("name") != null ? res.getString("name") : "");
                            result.add(res.getObject("address") != null ? res.getString("address") : "");
                            result.add((String) companyState.get(0).get(0));
                            result.add((String) companyState.get(0).get(1));
                            result.add(res.getObject("contact_no") != null ? res.getString("contact_no") : "");
                            result.add("");
//                result.add(res.getObject("contact_no") != null ? String.valueOf(res.getLong("contact_no")) : "");
//                result.add(res.getObject("gstno") != null ? res.getString("gstno") : ""); 
                        }
                        stmt.close();
                        res.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    private ArrayList<String> getBankPaymentAndCash(int billnumber) {
        ArrayList<String> cashAndBankData = new ArrayList<>();
        Connection con = DBConnect.connect();
        String cashamt = "";
        String card1amt = "", card1tran = "", card11bank = "", card12amt = "", card12tran = "", card12bank = "", ewallateamt = "", ewallatetran = "", ewallatbank = "", chamt = "", chtran = "", chbank = "", chbankname = "", chno = "", chDate = "";
        try {
            Statement stmt = con.createStatement();
            String sqlForReceipt = "select amtpaid from receipt where sales_Bill='" + billnumber + "'";
            ResultSet re = stmt.executeQuery(sqlForReceipt);
            while (re.next()) {
                cashamt = re.getString(1);
            }

            re.close();
            String sqlForBankGrp = "select * from bankledger where sales_Bill='" + billnumber + "'";
            re = stmt.executeQuery(sqlForBankGrp);
            while (re.next()) {

                String text = re.getString("remarks");
                if (!text.trim().isEmpty()) {
                    String[] allData = text.split(" ");
                    String areaToAdd = allData[0];
                    if (areaToAdd.trim().equals("1") && allData.length >= 2) {
                        card1amt = re.getString("amt");
                        card1tran = allData[1];
                        card11bank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("2") && allData.length >= 2) {
                        card12amt = re.getString("amt");
                        card12tran = allData[1];
                        card12bank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("3") && allData.length >= 2) {
                        ewallateamt = re.getString("amt");
                        ewallatetran = allData[1];
                        ewallatbank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("4") && allData.length >= 4) {
                        chamt = re.getString("amt");
                        chno = allData[1];
                        chbankname = allData[2];
                        chDate = allData[3];
                        chbank = re.getString("bankname");
                    }

                }

            }
            cashAndBankData.add(cashamt);
            cashAndBankData.add(card1amt);
            cashAndBankData.add(card1tran);
            cashAndBankData.add(card11bank);
            cashAndBankData.add(card12amt);
            cashAndBankData.add(card12tran);
            cashAndBankData.add(card12bank);
            cashAndBankData.add(ewallateamt);
            cashAndBankData.add(ewallatetran);
            cashAndBankData.add(ewallatbank);
            cashAndBankData.add(chamt);
            cashAndBankData.add(chno);
            cashAndBankData.add(chbankname);
            cashAndBankData.add(chDate);
            cashAndBankData.add(chbank);
            re.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }

        return cashAndBankData;
    }

    private void printSaleBillJasperReport(int billnumber, String Type) throws FileNotFoundException {
        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "jasper_reports" + File.separator;
//        JOptionPane.showMessageDialog(this, "system path is " + System.getProperty("user.dir"));
//        JOptionPane.showMessageDialog(this, "path is " + path);
        if ("Type 1".equals(Type)) {
            path += "SaleBill_Report.jrxml";
        } else if ("Type 2".equals(Type)) {
            path += "SaleBill_Report1.jrxml";
        } else {
            path += "SaleBill_Report2.jrxml";
        }

        List<SaleJasperContentDetails> listItems = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        String realPath = System.getProperty("user.dir") + File.separator + "jasper_reports" + File.separator + "img" + File.separator;
        ArrayList<String> datas = getBankPaymentAndCash(billnumber);
        parameters.put("imagePath", realPath);
        parameters.put("PaymentCash", datas.get(0));
        parameters.put("PaymentCard1amt", datas.get(1));
        parameters.put("Paymentcard1tran", datas.get(2));
        parameters.put("Paymentcard11bank", datas.get(3));
        parameters.put("Paymentcard12amt", datas.get(4));
        parameters.put("Paymentcard12tran", datas.get(5));
        parameters.put("Paymentcard12bank", datas.get(6));
        parameters.put("Paymentewallateamt", datas.get(7));
        parameters.put("Paymentewallatetran", datas.get(8));
        parameters.put("Paymentewallatbank", datas.get(9));
        parameters.put("Paymentchamt", datas.get(10));
        parameters.put("Paymentchno", datas.get(11));
        parameters.put("Paymentchbankname", datas.get(12));
        parameters.put("PaymentchDate", datas.get(13));
        parameters.put("Paymentchbank", datas.get(14));

//         JOptionPane.showMessageDialog(this, " image path is "+realPath);
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();

            String query = "SELECT partyname FROM " + DatabaseCredentials.SALES_TABLE + " WHERE bill = " + billnumber;
            ResultSet rs = st.executeQuery(query);

            String partyname = "", grp = "", billNo = String.valueOf(billnumber), date = "";
            double taxableAmt = 0.0, taxAmt = 0.0, amtBeforeTax = 0.0, amtAfterTax = 0.0;

            while (rs.next()) {
                partyname = rs.getString("partyname");

            }
            st.clearBatch();

            List<String> partyDetails = getPartyDetails(partyname);
//            JOptionPane.showMessageDialog(this, partyDetails);

            parameters.put("partyname", partyDetails.get(0));
            parameters.put("address", partyDetails.get(1));
            parameters.put("state", partyDetails.get(2));
            parameters.put("statecode", partyDetails.get(3));

            parameters.put("mobile", partyDetails.get(4));
            parameters.put("gstin", partyDetails.get(5));
            if ("Type 3".equals(Type)) {
                query = "SELECT * FROM `cashpurchasedetails` WHERE bill_no = " + String.valueOf(billnumber) + ";";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    parameters.put("partyname", rs.getString("name"));
                    parameters.put("address", rs.getString("address"));
                    parameters.put("mobile", rs.getString("contact_no"));
                }
            }
            st.clearBatch();
            query = "SELECT * FROM " + DatabaseCredentials.SALES_TABLE + " WHERE bill = " + Integer.parseInt(billNo);
            rs = st.executeQuery(query);
            Double discount1 = 0.0;
            Double BasicAmount = 0.0;

            while (rs.next()) {

                date = String.valueOf(rs.getDate("date"));
                taxableAmt += rs.getDouble("taxable_amount");
                taxAmt += rs.getDouble("gstamt");
                discount1 = rs.getDouble("Discount");
                BasicAmount += rs.getDouble("bankamt");
                String makingper = String.valueOf(rs.getFloat("labour"));
                String makingprice = String.format("%.2f", rs.getFloat("labour_amt_discount"));
                String lbr_per = rs.getString("lbr_per");

                if ("Percentage %".equals(lbr_per)) {
                    lbr_per = "%";
                } else if ("Gram".equals(lbr_per)) {
                    lbr_per = "Gm";
                } else if ("Piece".equals(lbr_per)) {
                    lbr_per = "Pcs";
                }

                Statement hsnst = con.createStatement();
                String itemname = rs.getString("itemname");
                ResultSet hsn = hsnst.executeQuery("select hsncode from entryitem where itemname = '" + itemname + "';");
                String hsncode = "";
                while (hsn.next()) {
                    if (hsn.getString("hsncode") != null) {
                        hsncode = hsn.getString("hsncode");
                    }
                }
                hsnst.close();
                DecimalFormat df = new DecimalFormat("0.#"); // This will remove the decimal point if it's a whole number
                String result = df.format(rs.getFloat("gstpercent"));

                SaleJasperContentDetails obj = new SaleJasperContentDetails(
                        rs.getObject("itemname") != null ? rs.getString("itemname") : "", //itemdescription
                        hsncode, // hsn
                        rs.getString("qty"), // pcs
                        "", // marka
                        rs.getString("huid"), // huid
                        String.valueOf(rs.getDouble("grosswt")), // gross wt
                        String.valueOf(rs.getDouble("netwt")), // net wt
                        String.valueOf(rs.getDouble("diamondwt")), // diamond wt    
                        String.valueOf(rs.getDouble("rate")), //rate
                        makingper, // making percentage
                        String.valueOf(rs.getDouble("netamount")),// net amount
                        String.valueOf(rs.getString("tagno")),//tagno
                        String.valueOf(result + "%"),
                        String.valueOf(rs.getDouble("taxable_amount")),
                        makingprice,
                        lbr_per
                );

                listItems.add(obj);

            }
            parameters.put("discount", String.valueOf(discount1));
            parameters.put("basic-amount", String.valueOf(BasicAmount));
            for (int i = 0; i < listItems.size(); i++) {
                st.clearBatch();

                ResultSet rs1;
                if (listItems.get(i).getgst().equals("0%")) {
                    rs1 = st.executeQuery("select hsncode, itemgroup from entryitem where tagno='"
                            + listItems.get(i).getTagno() + "' And itemname = '" + listItems.get(i).getItemdesc() + "'");

                } else {
                    rs1 = st.executeQuery("select hsncode, itemgroup from entryitem where tagno='"
                            + listItems.get(i).getTagno() + "' And taxslab = '" + listItems.get(i).getgst() + "' And itemname = '" + listItems.get(i).getItemdesc() + "'");

                }

                String hsncode = "", itemgroup = "";
                while (rs1.next()) {
                    if (rs1.getString("hsncode") != null) {
                        hsncode = rs1.getString("hsncode");
                    }
                    if (rs1.getString("itemgroup") != null) {
                        itemgroup = rs1.getString("itemgroup");
                    }
                }

                listItems.get(i).setHsn(hsncode);
                listItems.get(i).setMarka(itemgroup);
            }

            String selectedCompanyGST = "";
            st.clearBatch();
            Connection con2 = DBConnect.connectCopy();
            Statement sytm = con2.createStatement();
            rs = sytm.executeQuery("SELECT gstno FROM " + DatabaseCredentials.COMPANY_TABLE + " WHERE companyname = '" + GLOBAL_VARS.SELECTED_COMPANY + "';");

            while (rs.next()) {
                selectedCompanyGST = rs.getString("gstno");
            }

            List<List<Object>> companyState = DBController.getDataFromTableforcompany("SELECT state_code FROM company "
                    + "WHERE companyname = '"
                    + GLOBAL_VARS.SELECTED_COMPANY + "';");
            String companyStateCode;
            companyStateCode = (String) companyState.get(0).get(0);

            double igst = 0.0, cgst = 0.0, sgst = 0.0;
            List<List<Object>> partyState = DBController.getDataFromTable("SELECT statecode FROM " + DatabaseCredentials.ACCOUNT_TABLE
                    + " WHERE accountname = '"
                    + partyname + "';");

            double gst_amt = taxAmt;
            DecimalFormat df = new DecimalFormat("0.00");
            String partyStateCode = "";
            if (!partyState.isEmpty()) {
                partyStateCode = (String) partyState.get(0).get(0);
            }

            String figst = "", fcgst = "", fsgst = "";
            if (partyStateCode.equals("")) {
                // got no record of it's state code
            } else if (partyStateCode.equals(companyStateCode) || partyname.equalsIgnoreCase("cash")) {
                cgst = gst_amt / 2.0;
                sgst = gst_amt / 2.0;
                fcgst = String.valueOf(df.format(cgst));
                fsgst = String.valueOf(df.format(sgst));
                figst = "";
            } else if (!partyStateCode.equals(companyStateCode)) {
                igst = gst_amt;
                fcgst = "";
                fsgst = "";
                figst = String.valueOf(df.format(igst));
            }

            String statement = "SELECT total FROM `exchange` WHERE bill = " + String.valueOf(billNo) + ";";
            Connection con3 = connect();
            double totalexc = 0;
            try {
                Statement stmt = con3.createStatement();
                ResultSet rs3 = stmt.executeQuery(statement);
                while (rs3.next()) {

                    totalexc += rs3.getDouble("total");

                }
                con3.close();
                rs3.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ExChange.class.getName()).log(Level.SEVERE, "Failed", ex);
            }

            double tot = 0;
            if (totalexc == 0) {
                tot = 0;
            } else {
                tot = totalexc;
            }

            amtBeforeTax = taxableAmt;
            amtAfterTax = amtBeforeTax + taxAmt;

            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(listItems);
            parameters.put("sale-parameter", datasource);
            parameters.put("date", date);
            parameters.put("bill-no", billNo);
            Long wordnumber = (Long) Math.round(amtAfterTax);
//            JOptionPane.showMessageDialog(this, numberToWord.convertNumberToWord((wordnumber)));
            parameters.put("numberInWord", numberToWord.convertNumberToWord((wordnumber)));

            parameters.put("tax-amount", taxAmt);
            parameters.put("cgst", fcgst);
            parameters.put("sgst", fsgst);
            parameters.put("igst", figst);
            parameters.put("totalexc", tot);
            parameters.put("taxable-amount", String.valueOf(String.format("%.2f", taxableAmt)));
            parameters.put("amount-before-tax", String.valueOf(String.format("%.2f", amtBeforeTax)));
            parameters.put("amount-after-tax", amtAfterTax);
            parameters.put("amountafterexc", amtAfterTax - tot);
            parameters.put("company-gstin", selectedCompanyGST);
            parameters.put("receivable_amt", String.valueOf(String.format("%.2f", amtAfterTax)));
            if ("Type 2".equals(Type)) {
                rs = st.executeQuery("SELECT * FROM `exchange` ORDER BY id DESC LIMIT 1");
                String id = null, name = null, grosswt = null, fine = null, netwt = null, total = null;
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, "Exchanged");
                List<ExchangeJasperContentDetails> listItems1 = new ArrayList<>();
                while (rs.next()) {
                    id = rs.getString("id");
                    name = rs.getString("ItemName");
                    grosswt = rs.getString("grosswt");
                    fine = rs.getString("fine");
                    netwt = rs.getString("netwt");
                    total = rs.getString("total");
//                  parameters.put("item-name", rs.getString("Item Name"));
//                  parameters.put("grosswt", rs.getString("grosswt"));
//                  parameters.put("fine", rs.getString("fine"));
//                  parameters.put("netwt", rs.getString("netwt"));
//                  parameters.put("total", rs.getString("total"));
                }
                ExchangeJasperContentDetails obje = new ExchangeJasperContentDetails(name, grosswt, fine, netwt, total);
                listItems1.add(obje);
                JRBeanCollectionDataSource datasource1 = new JRBeanCollectionDataSource(listItems1);
                parameters.put("exchange-parameter", datasource1);
                System.out.print("hello");
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);
        }

        try {

            JasperReport report = JasperCompileManager.compileReport(path);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(print, false);
            String path2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "jasper_reports" + File.separator + "jasperpdf" + File.separator;

            JasperExportManager.exportReportToPdfFile(print, path2 + txtBill.getText().trim() + Type + ".pdf");
//            WindowListener[] wl = viewer.getWindowListeners();
//            for (WindowListener wl1 : wl) {
//                viewer.removeWindowListener(wl1);
//            }
//            viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            viewer.setVisible(true);
//            JasperExportManager.exportReportToPdfFile(print, "C:\\Users\\Sachin\\Desktop\\Sale_Bill.pdf");
        } catch (Exception e) {
            Logger.getLogger(LeisureTable.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void printjrxml() {

        connect = DBConnect.connect();
        InputStream jasper1 = null;
        String u = txtBill.getText().trim();
        String user = System.getProperty("user.dir") + "/jasper_reports/SaleBill.jrxml";

        File f = new File(user);

        try {

            jasper1 = new FileInputStream(f);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        String partyName = txtPartyName.getText();
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(jasper1);

            String query1 = "select itemname,huid,netwt,bankamt,labour,netamount from sales where bill = '" + u + "' and date = '" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "';";
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(query1);
            jasperDesign.setQuery(newQuery);

            Map<String, Object> hm = new HashMap<>();
            System.out.print(u);

            hm.put("customer_name", txtPartyName.getText());
            hm.put("bill_no", u);
            hm.put("state", lblState.getText());
            hm.put("total", txtgrandtotal.getText());
            hm.put("date", dateFormat.format(datePurchaseDate.getDate()));
            File file1 = null;
            String icon = System.getProperty("user.dir");
            file1 = new File(icon);
            hm.put("path", file1.getPath());

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, connect);
            String reportsavepath = System.getProperty("user.dir") + "/SalesBills/" + "Sale_" + u + ".pdf";

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportsavepath);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void tblItemNameSuggestionsKeyReleased(java.awt.event.KeyEvent evt) {
        int curselectedRow = tblItemNameSuggestions.getSelectedRow();
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            // down arrow
            if (curselectedRow + 1 < tblItemNameSuggestions.getRowCount()) {
                tblItemNameSuggestions.requestFocus();
                tblItemNameSuggestions.changeSelection(curselectedRow + 1, 0, false, false);
            }
        } else if (evt.getKeyCode() == 38) {
            if (curselectedRow - 1 > 0) {
                tblItemNameSuggestions.requestFocus();
                tblItemNameSuggestions.changeSelection(curselectedRow - 1, 0, false, false);
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txtItemName.requestFocusInWindow();
        }
    }

    private void filltblItemNameSuggestionDetails(String itemname, String itemgrp) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        String query = "";
        if (!RealSettingsHelper.gettagNoIsTrue()) {
            query = "SELECT tagno, itemgroup, netwt, huid FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                    + " WHERE (itemname = '" + itemname + "' AND item_sold = 0 AND itemgroup = '" + itemgrp + "')"
                    + " OR (tagno = 'N.A' AND itemname = '" + itemname + "' AND itemgroup = '" + itemgrp + "');";

        } else {
            query = "SELECT tagno, itemgroup, netwt, huid FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                    + " WHERE (itemname = '" + itemname + "' AND item_sold = 0 AND itemgroup = '" + itemgrp + "')"
                    + " OR (tagno = 'N.A' AND itemname = '" + itemname + "' AND itemgroup = '" + itemgrp + "');";

        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);
        itemNameSuggestionsDetailsTableModel.setRowCount(0);

        if (!RealSettingsHelper.gettagNoIsTrue()) {
            suggestions.forEach((suggestion) -> {

                if ("N.A".equals(suggestion.get(0).toString())) {

                    itemNameSuggestionsDetailsTableModel.addRow(new Object[]{
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        itemname,
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                        "0",
                        (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),});

                } else {
                    itemNameSuggestionsDetailsTableModel.addRow(new Object[]{
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        itemname,
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                        (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                        (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),});

                }
            });

        } else {
            suggestions.forEach((suggestion) -> {

                if ("N.A".equals(suggestion.get(0).toString())) {

                    itemNameSuggestionsDetailsTableModel.addRow(new Object[]{
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        itemname,
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                        "0",
                        (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),});

                } else {
                    itemNameSuggestionsDetailsTableModel.addRow(new Object[]{
                        (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                        itemname,
                        (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                        (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                        (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),});

                }
            });

        }

        fillgrandtotal();
    }

    private void tblItemNameSuggestionsDetailsMouseClicked(java.awt.event.MouseEvent evt) {
        //  if (evt.getClickCount() > 1 && evt.getClickCount() <= 2)
        if (evt.getClickCount() > 0) {
            txtItemName.setText(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                    .getSelectedRow(), 1).toString());
            setItemRate(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                    .getSelectedRow(), 1).toString());
            tagno = tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                    .getSelectedRow(), 0).toString();
            getHuid(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                    .getSelectedRow(), 1).toString(),
                    tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                            .getSelectedRow(), 0).toString(),
                    tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                            .getSelectedRow(), 2).toString());
//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			
            pmItemNameSuggestionsDetailsPopup.setVisible(false);

        }
    }

	private void tblItemNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemNameSuggestionsMouseClicked
            //   if (evt.getClickCount() > 1 && evt.getClickCount() <= 2)
            if (evt.getClickCount() > 0) {
                String itemName = tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 0).toString();
                String itemgrp = tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 1).toString();
                txtItemName.setText(itemName);
                pmItemNameSuggestionsPopup.setVisible(false);
                pmItemNameSuggestionsDetailsPopup.setVisible(true);
                filltblItemNameSuggestionDetails(itemName, itemgrp);
            }
	}//GEN-LAST:event_tblItemNameSuggestionsMouseClicked

	private void spTblItemNameSuggestionsContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_spTblItemNameSuggestionsContainerFocusGained
            JOptionPane.showMessageDialog(this, "Item name suggestions table 'focus gained'");
	}//GEN-LAST:event_spTblItemNameSuggestionsContainerFocusGained

	private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
            if (evt.getClickCount() > 0) {
                setPartyDetails(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                pmPartyNameSuggestionsPopup.setVisible(false);
            }
	}//GEN-LAST:event_tblPartyNameSuggestionsMouseClicked

    private void closebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebtnActionPerformed
        // TODO add your handling code here:
        clearTextFields();
        clearLabels();
        soldItemsForCurrentBill.clear();
        txtBill.setText(String.valueOf(getBillNofromSales()));
        salesListTableModel.setRowCount(0);
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

    }//GEN-LAST:event_closebtnActionPerformed

    private void txtreceiveKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtreceiveKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            double grand = Double.parseDouble(txtgrandtotal.getText());
            double receive = Double.parseDouble(txtreceive.getText());
            grand = grand - receive;
            txtoutstanding.setText(String.format("%.3f", grand));
            txtoutstanding.requestFocusInWindow();
            setBg();
            txtoutstanding.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtreceiveKeyReleased

    private void txtdiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdiscountKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtdiscount.getText().trim().isEmpty()) {
                txtdiscount.setText("0");
                txtgrandtotal.requestFocusInWindow();
                setBg();
                txtgrandtotal.setBackground(Color.LIGHT_GRAY);
            } else {
                double grand = Double.parseDouble(txtgrandtotal.getText());
                double discount = Double.parseDouble(txtdiscount.getText());
                grand = grand - discount;
                txtgrandtotal.setText(Double.toString(grand));
                txtgrandtotal.requestFocusInWindow();
                setBg();
                txtgrandtotal.setBackground(Color.LIGHT_GRAY);
            }
        }
    }//GEN-LAST:event_txtdiscountKeyReleased

    private void txtGSTAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTAmtKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTAmtKeyReleased

    private void txtGSTAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTAmtFocusGained
        txtGSTAmt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGSTAmtFocusGained

    private void txtgrandtotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgrandtotalKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtreceive.requestFocusInWindow();
            setBg();
            txtreceive.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtgrandtotalKeyReleased

    private void txtgrandtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtgrandtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtgrandtotalActionPerformed
    private void insertIntoCashAndBankPayments() {
        try {
            if (cmbTerms.getSelectedItem().toString().equalsIgnoreCase("Cash")) {
                if (!CashAndBankPaymentsSales.isVisible()) {

                    if (!(CashAndBankPaymentsSales.getCardAmount1() == null)) {
                        if (!(CashAndBankPaymentsSales.getCardAmount1().trim().isEmpty())) {
                            try {
                                Connection c = DBConnect.connect();
                                Statement s = c.createStatement();

                                String query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`,sales_Bill) VALUES ( '" + txtPartyName.getText() + "','" + CashAndBankPaymentsSales.getCardBank1() + "', '" + dateFormat.format(datePurchaseDate.getDate()) + "', '" + CashAndBankPaymentsSales.getCardAmount1() + "', "
                                        + "'1 " + CashAndBankPaymentsSales.getCard1Transectionno() + " sales Direct Payment', 'deposit','" + txtBill.getText().trim() + "');";
                                s.executeUpdate(query);
                                c.close();
                                s.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                    if (!(CashAndBankPaymentsSales.getCashAmount() == null)) {
                        if (!CashAndBankPaymentsSales.getCashAmount().trim().isEmpty()) {
                            String name = txtPartyName.getText();
                            double dis = 0;
                            double amtpaid = Double.parseDouble(CashAndBankPaymentsSales.getCashAmount().trim());
                            String remarks = "sales Direct Payment";
                            String mop = "Cash";

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sdf.format(datePurchaseDate.getDate());

                            try {

                                Connection cmm = DBConnect.connect();
                                Statement stmt = cmm.createStatement();
                                String query = "insert into receipt(Receiptno,Name,date,discount,amtpaid,mop,remarks,sales_Bill) values(-1,'" + name + "','" + date + "','" + dis + "','" + String.format("%.2f", amtpaid) + "','" + mop + "','" + remarks + "','" + txtBill.getText().trim() + "')";
//                                JOptionPane.showMessageDialog(this, query);
                                stmt.executeUpdate(query);

                                cmm.close();
                                stmt.close();
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(this, e);
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

                            }
                        }
                    }
                    if (!(CashAndBankPaymentsSales.getCardAmount2() == null)) {
                        if (!(CashAndBankPaymentsSales.getCardAmount2().trim().isEmpty())) {
                            try {
                                Connection c = DBConnect.connect();
                                Statement s = c.createStatement();

                                String query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`,sales_Bill) VALUES ( '" + txtPartyName.getText() + "','" + CashAndBankPaymentsSales.getCardBank2() + "', '" + dateFormat.format(datePurchaseDate.getDate()) + "', '" + CashAndBankPaymentsSales.getCardAmount2() + "', "
                                        + "'2 " + CashAndBankPaymentsSales.getCard2Transectionno() + " sales Direct Payment', 'deposit','" + txtBill.getText().trim() + "');";
                                s.executeUpdate(query);
                                c.close();
                                s.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                    if (!(CashAndBankPaymentsSales.geteWallteAmount() == null)) {
                        if (!(CashAndBankPaymentsSales.geteWallteAmount().trim().isEmpty())) {
                            try {
                                Connection c = DBConnect.connect();
                                Statement s = c.createStatement();

                                String query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`,sales_Bill) VALUES ( '" + txtPartyName.getText() + "','" + CashAndBankPaymentsSales.getEwalletBank() + "', '" + dateFormat.format(datePurchaseDate.getDate()) + "', '" + CashAndBankPaymentsSales.geteWallteAmount() + "', "
                                        + "'3 " + CashAndBankPaymentsSales.getEwalletTransectionno() + " sales Direct Payment', 'deposit','" + txtBill.getText().trim() + "');";
                                s.executeUpdate(query);
                                c.close();
                                s.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                    if (!(CashAndBankPaymentsSales.getChequeAmount() == null)) {
                        if (!(CashAndBankPaymentsSales.getChequeAmount().trim().isEmpty())) {
                            try {
                                Connection c = DBConnect.connect();
                                Statement s = c.createStatement();

                                String query = "INSERT INTO `bankledger` ( `name`, `bankname`, `date`, `amt`, `remarks`, `type`,sales_Bill  ) VALUES ( '" + txtPartyName.getText() + "','" + CashAndBankPaymentsSales.getChequeBank() + "', '" + dateFormat.format(datePurchaseDate.getDate()) + "', '" + CashAndBankPaymentsSales.getChequeAmount() + "', "
                                        + "'4 " + CashAndBankPaymentsSales.getChequno() + " " + CashAndBankPaymentsSales.getChequeTransectionno() + " " + CashAndBankPaymentsSales.getChequeDate() + " sales Direct Payment', 'deposit','" + txtBill.getText().trim() + "');";
                                s.executeUpdate(query);
                                c.close();
                                s.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "iosert casgh   " + e);
        }
    }

    private void insertDataFromPurchaseTableToDatabase() throws FileNotFoundException {
        for (Map<String, Object> item : soldItemsForCurrentBill) {
            data.clear();
            columnNames.clear();
            if (DBController.isDatabaseConnected()) {
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);
                // Remove the id as it is auto generated
                columnNames.remove(0);
            } else {
                DBController
                        .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                                DatabaseCredentials.DB_USERNAME,
                                DatabaseCredentials.DB_PASSWORD);
                columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);
                // Remove the id as it is auto generated
                columnNames.remove(0);
            }

            data.add(dateFormat.format(item.get("date")));
            data.add(cmbTerms.getSelectedItem().toString().trim());
            if (txtPartyName.getText().trim().isEmpty()) {
                data.add(item.get("partyname"));
            } else {
                data.add(txtPartyName.getText().trim());
            }

            data.add(item.get("billnumber"));
            data.add(item.get("gst"));
            data.add(item.get("previousbalance"));
            data.add(item.get("itemname"));
            data.add(item.get("huid"));
            if (item.get("qty") != null) {
                data.add(item.get("qty"));
            } else {
                columnNames.remove("qty");
            }

            if (item.get("address") != null) {
                data.add(item.get("address"));
            } else {
                columnNames.remove("address");
            }

            if (item.get("beedswt") != null) {
                data.add(item.get("beedswt"));
            } else {
                columnNames.remove("beedswt");
            }

            if (item.get("netwt") != null) {
                data.add(item.get("netwt"));
            } else {
                columnNames.remove("netwt");
            }

            if (item.get("diamondwt") != null) {
                data.add(item.get("diamondwt"));
            } else {
                columnNames.remove("diamondwt");
            }

            if (item.get("diamondrate") != null) {
                data.add(item.get("diamondrate"));
            } else {
                columnNames.remove("diamondrate");
            }

            if (item.get("grosswt") != null) {
                data.add(item.get("grosswt"));
            } else {
                columnNames.remove("grosswt");
            }

            if (item.get("itemdescription") != null) {
                data.add(item.get("itemdescription"));
            } else {
                columnNames.remove("itemdescription");
            }

            data.add(item.get("per"));

            if (item.get("extrachange") != null) {
                data.add(item.get("extrachange"));
            } else {
                columnNames.remove("extrachange");
            }

            if (item.get("basicamount") != null) {
                data.add(item.get("basicamount"));
            } else {
                columnNames.remove("bankamt");
            }

            if (item.get("netamount") != null) {
                data.add(item.get("netamount"));
            } else {
                columnNames.remove("netamount");
            }

            if (txtreceive.getText() != null) {
                data.add(txtreceive.getText());
            } else {
                columnNames.remove("receivedamount");
            }

            if (item.get("rate") != null) {
                data.add(item.get("rate"));
            } else {
                columnNames.remove("rate");
            }

            if (item.get("taxableamount") != null) {
                Float f = Float.valueOf(item.get("taxableamount").toString());
                data.add(f);
            } else {
                columnNames.remove("taxable_amount");
            }

            if (item.get("gstpercent") != null) {
                data.add(item.get("gstpercent"));
            } else {
                columnNames.remove("gstpercent");
            }
            if (item.get("gstamount") != null) {
                data.add(item.get("gstamount"));
            } else {
                columnNames.remove("gstamt");
            }

            if (item.get("labour") != null) {
                data.add(item.get("labour"));
            } else {
                columnNames.remove("labour");
            }
            if (item.get("labouramtdiscount") != null) {
                data.add(item.get("labouramtdiscount"));
            } else {
                columnNames.remove("labour_amt_discount");
            }
            if (item.get("discount") != null) {
                data.add(item.get("discount"));
            } else {
                columnNames.remove("Discount");
            }
            if (item.get("tagno") != null) {
                data.add(item.get("tagno"));
            } else {
                columnNames.remove("tagno");
            }

            Integer dataId = (Integer) item.get("id");
//            JOptionPane.showMessageDialog(this, !LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.isVisible());

            if (dataId != null) {
                DBController.updateTableData(DatabaseCredentials.SALES_TABLE, data, columnNames, "id", dataId);

                CashAndBankPaymentsSales.deleteMethod(txtBill.getText().trim());
//                    JOptionPane.showMessageDialog(this, LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.getCashAmount() + !LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.isDisplayable());
                insertIntoCashAndBankPayments();

            } else {
                try {
                    DBController.insertDataIntoTable(DatabaseCredentials.SALES_TABLE,
                            columnNames, data);
                    insertIntoCashAndBankPayments();
//                    JOptionPane.showMessageDialog(this, LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.isDisplayable());

                } catch (Exception e) {
                    Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

                    JOptionPane.showMessageDialog(this, e);

                }
            }
        }

        if (save.getText().equals("Update")) {
            JOptionPane.showMessageDialog(this, "Successfully Updated!");
        } else {
            JOptionPane.showMessageDialog(this, "Items Sale Successfully!");
        }
    }

    private void updateBillNumberField() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
        }
        List<List<Object>> result = DBController.getDataFromTable("SELECT billno FROM " + DatabaseCredentials.SALE_BILL_NO_COUNTER_TABLE
                + " WHERE id = 1");

        result.forEach((item) -> {
            txtBill.setText(item.get(0).toString());
        });

    }

    private void updateDueAmtFromOutstanding(double outstandingAmt) {
        data.clear();
        columnNames.clear();
//        JOptionPane.showMessageDialog(this,outstandingAmt);
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
        }
        String partyname = (String) soldItemsForCurrentBill.get(soldItemsForCurrentBill.size() - 1).get("partyname");
        String query = "SELECT dueamt FROM " + DatabaseCredentials.ACCOUNT_TABLE
                + " WHERE accountname = '" + partyname + "';";
        List<List<Object>> res = DBController.getDataFromTable(query);
        double dueamount = 0;
        for (List<Object> list : res) {
            for (Object l : list) {
                dueamount += Double.parseDouble(l.toString());
            }
        }

        dueamount += outstandingAmt;

        data.clear();
        columnNames.clear();
        columnNames.add("dueamt");
        data.add(dueamount);
        DBController.updateTableData(DatabaseCredentials.ACCOUNT_TABLE, data, columnNames,
                "accountname", partyname);
    }

    private void markItemsSold() {
        for (Map<String, Object> item : soldItemsForCurrentBill) {
            String tag = item.get("tagno").toString();
            String itemname = item.get("itemname").toString();
            int qty = Integer.parseInt(item.get("qty").toString());

            Connection con;
            List<List<Object>> notSold = DBController.getDataFromTable("SELECT SUM(CAST (item_sold as Integer)), SUM(netwt) FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE tagno ='" + tagno + "' ");
            int notsoldcount = 0;
            for (List<Object> list : notSold) {

                if (list.get(0) != null) {

                    notsoldcount = Integer.parseInt(list.get(0).toString());
                }
            }
            try {
                JOptionPane.showMessageDialog(this, qty);
                con = DBConnect.connect();
                Statement st = con.createStatement();
                String query = "UPDATE " + DatabaseCredentials.ENTRY_ITEM_TABLE + " SET item_sold = " + ((int)(qty)) + " "
                        + " WHERE itemname = '" + itemname + "';";
                st.execute(query);

            } catch (SQLException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void generateNewReceiptForSale(double recievedAmount) throws FileNotFoundException {

        try {
            Connection con = DBConnect.connect();

            Statement stmt2 = con.createStatement();
            stmt2.execute("delete from receipt where sales_Bill=" + txtBill.getText() + " and remarks='Paid'");
            con.close();
            stmt2.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "sale receipt " + e);
        }
        if (recievedAmount == 0) {
            return;
        }
        data.clear();
        columnNames.clear();
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
        }
        columnNames = DBController.getTableColumnNames(DatabaseCredentials.RECEIPT_TABLE);
        // id
        columnNames.remove(0);
        Map<String, Object> billData = soldItemsForCurrentBill.get(soldItemsForCurrentBill.size() - 1);
        data.add("-1");
        data.add(billData.get("partyname"));
        data.add(billData.get("date"));

        if (txtDiscount.getText().trim() != null && !"".equals(txtDiscount.getText())) {
            data.add(txtDiscount.getText());
        } else {
            data.add(0);
        }
//data.add(tagno);
        data.add(recievedAmount);
        data.add("Cash");
        data.add("Paid");
        data.add(txtBill.getText().trim());
        DBController.insertDataIntoTable(DatabaseCredentials.RECEIPT_TABLE, columnNames, data);
    }

    private void clearPartyFields() {
        lblAddress.setText("");
        lblGST.setText("");
        lblPreviousBalance.setText("");
        lblState.setText("");
        jComboBox1.setSelectedItem("GST");
        cmbTerms.setSelectedItem("Cash");
        save.setEnabled(false);
        jButton4.setEnabled(true);

    }

    private void txthuidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthuidActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txthuidActionPerformed

    private void txtExtraCharge2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraCharge2KeyReleased
        setFocus(evt, txtExtraCharge);
    }//GEN-LAST:event_txtExtraCharge2KeyReleased

    private void txtExtraCharge2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge2FocusLost
        txtExtraCharge2.setBackground(Color.white);
        if (txtExtraCharge2.getText().trim().isEmpty()) {
            txtExtraCharge2.setText("0");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtExtraCharge2FocusLost

    private void txtExtraCharge1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraCharge1KeyReleased
        setFocus(evt, cmbPer);
    }//GEN-LAST:event_txtExtraCharge1KeyReleased

    private void txtExtraCharge1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge1FocusLost
        txtExtraCharge.setBackground(Color.white);
        if (txtExtraCharge1.getText().trim().isEmpty()) {
            txtExtraCharge1.setText("0");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtExtraCharge1FocusLost

    private void savePurchaseListTableDataToDatabase() throws FileNotFoundException {
        if (DBController.isDatabaseConnected()) {
            columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);

            // Remove the id as it is auto generated
            columnNames.remove(0);
        } else {
            DBController
                    .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME,
                            DatabaseCredentials.DB_PASSWORD);
            columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);

            // Remove the id as it is auto generated
            columnNames.remove(0);
        }

        data.add(dateFormat.format(datePurchaseDate.getDate()));
        data.add(cmbTerms.getSelectedItem());
        data.add(txtPartyName.getText().trim());

        data.add(txtBill.getText().trim());

        //                if(!txtBill.getText().trim().isEmpty()) {
        //                    data.add(txtBill.getText().trim());
        //                }
        //                else {
        //                    columnNames.remove("bill");
        //                }
        data.add(lblGST.getText());

        data.add(lblPreviousBalance.getText());
        data.add(txtItemName.getText().trim());
        data.add(txthuid.getText().trim());
        if (!txtQty.getText().trim().isEmpty()) {
            data.add(txtQty.getText().trim());
        } else {
            columnNames.remove("qty");
        }

        if (!lblAddress.getText().isEmpty()) {
            data.add(lblAddress.getText());
        } else {
            columnNames.remove("address");
        }

        if (!txtBeedsWt.getText().trim().isEmpty()) {
            data.add(txtBeedsWt.getText().trim());
        } else {
            columnNames.remove("beedswt");
        }

        if (!txtNetWt.getText().trim().isEmpty()) {
            data.add(txtNetWt.getText().trim());
        } else {
            columnNames.remove("netwt");
        }

        if (!txtDiamondWt.getText().trim().isEmpty()) {
            data.add(txtDiamondWt.getText().trim());
        } else {
            columnNames.remove("diamondwt");
        }

        if (!txtDiamondRate.getText().trim().isEmpty()) {
            data.add(txtDiamondRate.getText().trim());
        } else {
            columnNames.remove("diamondrate");
        }

        if (!txtGrossWt.getText().trim().isEmpty()) {
            data.add(txtGrossWt.getText().trim());
        } else {
            columnNames.remove("grosswt");
        }

        if (!txtItemDescription.getText().trim().isEmpty()) {
            data.add(txtItemDescription.getText().trim());
        } else {
            columnNames.remove("itemdescription");
        }

        data.add(cmbPer.getSelectedItem());

        if (!txtExtraCharge.getText().trim().isEmpty()) {
            data.add(txtExtraCharge.getText().trim());
        } else {
            columnNames.remove("extrachange");
        }

        if (!txtBasicAmt.getText().trim().isEmpty()) {
            data.add(txtBasicAmt.getText().trim());
        } else {
            columnNames.remove("bankamt");
        }

        if (!txtNetAmt.getText().trim().isEmpty()) {
            data.add(txtNetAmt.getText().trim());
        } else {
            columnNames.remove("netamount");
        }

        if (!txtRate.getText().trim().isEmpty()) {
            data.add(txtRate.getText().trim());
        } else {
            columnNames.remove("rate");
        }

        if (!txtTaxableAmt.getText().trim().isEmpty()) {
            data.add(txtTaxableAmt.getText().trim());
        } else {
            columnNames.remove("taxable_amount");
        }

        if (!txtGSTPercent.getText().trim().isEmpty()) {
            data.add(txtGSTPercent.getText().trim());
        } else {
            columnNames.remove("gstpercent");
        }
        if (!txtGSTAmt.getText().trim().isEmpty()) {
            data.add(txtGSTAmt.getText().trim());
        } else {
            columnNames.remove("gstamt");
        }

        if (!txtExtraCharge1.getText().trim().isEmpty()) {
            data.add(txtExtraCharge1.getText().trim());
        } else {
            columnNames.remove("labour");
        }
        if (!txtExtraCharge2.getText().trim().isEmpty()) {
            data.add(txtExtraCharge2.getText().trim());
        } else {
            columnNames.remove("labour_amt_discount");
        }

        if (!DBController.updateTableData(DatabaseCredentials.SALES_TABLE,
                data, columnNames, "id", id)) {

            DBController.insertDataIntoTable(DatabaseCredentials.SALES_TABLE,
                    columnNames, data);
        }
        setBg();
        populateSalesListTable();
        fillgrandtotal();
        addtodueamt();
        txtItemName.requestFocus();
    }

    private void fillNewEntryIntotblPurchasesList() {
        salesListTableModel.setRowCount(0);
        for (Map<String, Object> list : soldItemsForCurrentBill) {
            this.salesListTableModel.addRow(new Object[]{
                list.get("billnumber"),
                list.get("itemname"),
                list.get("huid"),
                list.get("netwt"),
                list.get("qty"),
                list.get("discount"),
                list.get("taxableamount"),
                list.get("gstpercent"),
                list.get("gstamount"),
                list.get("netamount")
            });
        }

    }

    private void updateRequestForItemInDatabase(int entryid, Map<String, Object> item) throws SQLException, FileNotFoundException {
        data.clear();
        columnNames.clear();
        if (DBController.isDatabaseConnected()) {
            columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);
            // Remove the id as it is auto generated
            columnNames.remove(0);
        } else {
            DBController
                    .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME,
                            DatabaseCredentials.DB_PASSWORD);
            columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);
            // Remove the id as it is auto generated
            columnNames.remove(0);
        }

        data.add(dateFormat.format(item.get("date")));
        data.add(item.get("terms"));
        data.add(item.get("partyname"));
        data.add(item.get("billnumber"));
        data.add(item.get("gst"));
        data.add(item.get("previousbalance"));
        data.add(item.get("itemname"));
        data.add(item.get("huid"));
        if (item.get("qty") != null) {
            data.add(item.get("qty"));
        } else {
            columnNames.remove("qty");
        }

        if (item.get("address") != null) {
            data.add(item.get("address"));
        } else {
            columnNames.remove("address");
        }

        if (item.get("beedswt") != null) {
            data.add(item.get("beedswt"));
        } else {
            columnNames.remove("beedswt");
        }

        if (item.get("netwt") != null) {
            data.add(item.get("netwt"));
        } else {
            columnNames.remove("netwt");
        }

        if (item.get("diamondwt") != null) {
            data.add(item.get("diamondwt"));
        } else {
            columnNames.remove("diamondwt");
        }

        if (item.get("diamondrate") != null) {
            data.add(item.get("diamondrate"));
        } else {
            columnNames.remove("diamondrate");
        }

        if (item.get("grosswt") != null) {
            data.add(item.get("grosswt"));
        } else {
            columnNames.remove("grosswt");
        }

        if (item.get("itemdescription") != null) {
            data.add(item.get("itemdescription"));
        } else {
            columnNames.remove("itemdescription");
        }

        data.add(item.get("per"));

        if (item.get("extrachange") != null) {
            data.add(item.get("extrachange"));
        } else {
            columnNames.remove("extrachange");
        }

        if (item.get("basicamount") != null) {
            data.add(item.get("basicamount"));
        } else {
            columnNames.remove("bankamt");
        }

        if (item.get("netamount") != null) {
            data.add(item.get("netamount"));
        } else {
            columnNames.remove("netamount");
        }

        if (txtreceive.getText() != null) {
            data.add(txtreceive.getText());
        } else {
            columnNames.remove("receivedamount");
        }

        if (item.get("rate") != null) {
            data.add(item.get("rate"));
        } else {
            columnNames.remove("rate");
        }

        if (item.get("taxableamount") != null) {
            data.add(item.get("taxableamount"));
        } else {
            columnNames.remove("taxable_amount");
        }

        if (item.get("gstpercent") != null) {
            data.add(item.get("gstpercent"));
        } else {
            columnNames.remove("gstpercent");
        }
        if (item.get("gstamount") != null) {
            data.add(item.get("gstamount"));
        } else {
            columnNames.remove("gstamt");
        }

        if (item.get("labour") != null) {
            data.add(item.get("labour"));
        } else {
            columnNames.remove("labour");
        }
        if (item.get("labouramtdiscount") != null) {
            data.add(item.get("labouramtdiscount"));
        } else {
            columnNames.remove("labour_amt_discount");
        }
        if (item.get("discount") != null) {
            data.add(item.get("discount"));
        } else {
            columnNames.remove("Discount");
        }
        if (item.get("tagno") != null) {
            data.add(item.get("tagno"));
        } else {
            columnNames.remove("tagno");
        }

        DBController.updateTableData(DatabaseCredentials.SALES_TABLE,
                data, columnNames, "id", entryid);

        // fill back the updated table
        saleRegisterRedirect(Integer.parseInt(txtBill.getText().trim()));
    }

    private void txtNetAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetAmtKeyReleased
        // TODO add your handling code here:columnNames.clear();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//JOptionPane.showMessageDialog(this,fieldsAreValidated());
            try {
                if (fieldsAreValidated()) {

                    Map<String, Object> newEntry = new HashMap<>();
                    newEntry.put("itemname", txtItemName.getText());
                    newEntry.put("huid", txthuid.getText());
                    newEntry.put("grosswt", txtGrossWt.getText());
                    newEntry.put("beedswt", txtBeedsWt.getText());
                    newEntry.put("netwt", txtNetWt.getText());
                    newEntry.put("diamondwt", txtDiamondWt.getText());
                    newEntry.put("diamondrate", txtDiamondRate.getText());
                    newEntry.put("itemdescription", txtItemDescription.getText());
                    newEntry.put("qty", txtQty.getText());
                    newEntry.put("rate", txtRate.getText());
                    newEntry.put("per", cmbPer.getSelectedItem());
                    newEntry.put("labour", String.format("%.2f", Double.valueOf(txtExtraCharge1.getText())));
                    newEntry.put("labouramtdiscount", String.format("%.2f", Double.valueOf(txtExtraCharge2.getText())));
                    newEntry.put("extrachange", String.format("%.2f", Double.valueOf(txtExtraCharge.getText())));
                    newEntry.put("basicamount", String.format("%.2f", Double.valueOf(txtBasicAmt.getText())));
                    newEntry.put("taxableamount", String.format("%.2f", Double.valueOf(txtTaxableAmt.getText())));
                    newEntry.put("gstpercent", txtGSTPercent.getText());
                    newEntry.put("gstamount", String.format("%.2f", Double.valueOf(txtGSTAmt.getText())));
                    newEntry.put("netamount", String.format("%.2f", Double.valueOf(txtNetAmt.getText())));
                    newEntry.put("date", datePurchaseDate.getDate());
                    newEntry.put("terms", cmbTerms.getSelectedItem());
                    newEntry.put("billnumber", txtBill.getText().trim());
                    newEntry.put("partyname", txtPartyName.getText());
                    newEntry.put("gst", lblGST.getText());
                    newEntry.put("previousbalance", String.format("%.2f", Double.valueOf(lblPreviousBalance.getText())));
                    newEntry.put("discount", txtDiscount.getText());
                    newEntry.put("tagno", tagno);

                    if (tblPurchasesList.getSelectedRow() != -1) {
                        // request is for updating a entry
                        int row = tblPurchasesList.getSelectedRow();
                        Integer entryId = (Integer) tblPurchasesList.getValueAt(row, 0);
                        if (!"".equals(entryId) && entryId != null) {
                            try {
                                // update to database and reload
                                // it is coming from saleRegister
                                soldItemsForCurrentBill.set(row, newEntry);
                                updateRequestForItemInDatabase(entryId, soldItemsForCurrentBill.get(row));
                            } catch (SQLException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            // update to soldItemsForCurrentBill list
                            soldItemsForCurrentBill.set(row, newEntry);
                            fillNewEntryIntotblPurchasesList();
                        }
                    } else {
                        soldItemsForCurrentBill.add(newEntry);
                        fillNewEntryIntotblPurchasesList();
                    }
                }
                clearTextFieldsEnter();
                fillgrandtotal();
                setFocus(evt, txtItemName);
                txtItemName.setBackground(Color.LIGHT_GRAY);
                save.setEnabled(true);

            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
                if (e.toString().trim().equals("java.lang.NumberFormatException: empty String")) {
                    JOptionPane.showMessageDialog(this, "Enter Rate And Basic Amount");
                    txtRate.requestFocus();
                }
            }
        }


    }//GEN-LAST:event_txtNetAmtKeyReleased

    private void txtNetAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusGained
        txtNetAmt.setBackground(new Color(245, 230, 66));
        calculateNetAmount();
    }//GEN-LAST:event_txtNetAmtFocusGained

    private void txtGSTPercentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPercentKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtNetAmt);
            setBg();
            txtNetAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtGSTPercentKeyReleased

    private void txtTaxableAmtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxableAmtKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxableAmtKeyTyped

    private void txtTaxableAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxableAmtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtGSTPercent);
            setBg();
            txtGSTPercent.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtTaxableAmtKeyReleased

    private void txtTaxableAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxableAmtFocusGained
        txtTaxableAmt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtTaxableAmtFocusGained

    private void printbuttonclicked() {
        String typeToBePrinted = "";
        try {
            String[] options = {"Type 1", "Type 2", "Type 3"};
            typeToBePrinted = (String) JOptionPane.showInputDialog(null, "Select One:", "Print", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (typeToBePrinted != null) {
                if (Arrays.asList(options).contains(typeToBePrinted)) {
                    printSaleBillJasperReport(billToBePrinted, typeToBePrinted);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid selection.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No option selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            JOptionPane.showMessageDialog(this, typeToBePrinted + "this is printbuttonclicked");
            JOptionPane.showMessageDialog(null, "Select one bill to print.");

            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void markItemUnsold(String id) throws SQLException {
        Connection con = DBConnect.connect();
        Statement st = con.createStatement();
        String Query = "select tagno,qty from sales where id=" + id + "";
        ResultSet re = st.executeQuery(Query);
        String tagno = null;
        while (re.next()) {
            tagno = re.getString("tagno");
            updateEntryItem(tagno, re.getString(2));
        }
//        st.execute("update entryitem set item_sold=0 where id ='" + id + "';");
    }

    private void updateEntryItem(String tagno, String qty) {
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            List<List<Object>> notSold = DBController.getDataFromTable("SELECT SUM(CAST (item_sold as Decimal(10,2))), SUM(netwt) FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE tagno ='" + tagno + "' AND "
                    + " not tagno='N.A'");
            double notsoldcount = 0;

            for (List<Object> list : notSold) {

                if (list.get(0) != null) {
                    notsoldcount = Double.parseDouble(list.get(0).toString());
                }
            }

            st.execute("update entryitem set item_sold=" + (notsoldcount - Integer.parseInt(qty)) + " where tagno ='" + tagno + "'");
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }

    }
    private void cmbTermsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTermsKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtPartyName);
            setBg();
            txtPartyName.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_cmbTermsKeyReleased

    private void cmbTermsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTermsActionPerformed

        if (cmbTerms.getSelectedItem().equals("Cash")) {
            fetchAccountNames("Cash");
            String grandTotalText = txtgrandtotal.getText().trim();
            double grandTotal;
            if (!grandTotalText.isEmpty()) {
                grandTotal = Double.parseDouble(grandTotalText);
                // Proceed with grandTotal
            } else {
                grandTotal = 0;

            }

            String txtexchangeText = txtexchange.getText();
            double exchange = 0;
            if (!grandTotalText.isEmpty()) {
                exchange = Double.parseDouble(txtexchangeText);
                // Proceed with grandTotal
            } else {
                exchange = 0;

            }

            double receive = grandTotal - exchange;

            paymentbtn.setEnabled(true);

            txtreceive.setText(String.format("%.3f", receive));

            //  txtPartyName.setText("Cash");
        } else if (cmbTerms.getSelectedItem().equals("Credit")) {
            fetchAccountNames("Credit");
            paymentbtn.setEnabled(false);
            String grandTotalText = txtgrandtotal.getText().trim();
            double grandTotal;
            if (!grandTotalText.isEmpty()) {
                grandTotal = Double.parseDouble(grandTotalText);
                // Proceed with grandTotal
            } else {
                grandTotal = 0;

            }
            String txtexchangeText = txtexchange.getText();
            double exchange = 0;
            if (!grandTotalText.isEmpty()) {
                exchange = Double.parseDouble(txtexchangeText);
                // Proceed with grandTotal
            } else {
                exchange = 0;

            }
            double receive = grandTotal - exchange;
            txtreceive.setText(String.format("%.3f", receive));
            //   txtPartyName.setText("Credit");
        }
        if (txtPartyName.getText().trim().equalsIgnoreCase("Cash")) {
//            cmbTerms.removeAllItems();
            cmbTerms.setSelectedItem("Cash");
        } else {
//            cmbTerms.removeAllItems();
//            cmbTerms.addItem("Cash");
//            cmbTerms.addItem("Credit");
        }
    }//GEN-LAST:event_cmbTermsActionPerformed

    private void txtBillKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            setFocus(evt, txtItemName);
            jComboBox1.requestFocus();

            setBg();

        }
    }//GEN-LAST:event_txtBillKeyReleased
    int selectedrow = 0;
    CashUserDetails obj = new CashUserDetails();
    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased

//        if (!(accountNames == null || accountNames.isEmpty())) {
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
                txtBill.requestFocusInWindow();
                setBg();
                txtBill.setBackground(Color.LIGHT_GRAY);
                if ("Update".equals(save.getText())) {
                    if ("Cash".equalsIgnoreCase(txtPartyName.getText())) {

                        obj.setVisible(true);

                    }
                    try {
                        obj.fetchData(Integer.parseInt(txtBill.getText().trim()));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    obj.setPass(1, Integer.parseInt(txtBill.getText().trim()));
                }
                if ("Cash".equalsIgnoreCase(txtPartyName.getText()) && "Save".equals(save.getText())) {
                    obj = new CashUserDetails();
                    obj.setVisible(true);
                    obj.setPass(0, Integer.parseInt(txtBill.getText().trim()));
                    //        new CashPurchaseDialog(this, true).setVisible(true);
                } else {
//                    obj.setVisible(true);
//                    obj.setPass(1,Integer.parseInt(txtBill.getText()));
                }
                setPartyDetails(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                pmPartyNameSuggestionsPopup.setVisible(false);
                break;
            default:
                EventQueue.invokeLater(() -> {
                    if (cmbTerms.getSelectedItem().toString().trim().equals("Cash")) {
                        pmPartyNameSuggestionsPopup.setVisible(true);
                        populatenameSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'"
                                + " AND ((grp = 'Customer' OR grp = 'Cash') OR (grp = 'Supplier' AND supplier_as_customer = TRUE));");

                    } else {
                        pmPartyNameSuggestionsPopup.setVisible(true);
                        populatenameSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                                + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                                + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'"
                                + " AND grp = 'Customer' OR (grp = 'Supplier' AND supplier_as_customer = TRUE);");

                    }
                });
                break;
        }
//        }
    }//GEN-LAST:event_txtPartyNameKeyReleased

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusLost
        txtPartyName.setBackground(Color.white);
        try {
            if (txtPartyName.getText().trim().isEmpty()) {

            } else {
                pmPartyNameSuggestionsPopup.setVisible(false);
                lblPreviousBalance.setText(
                        outstandingAnalysisHelper.fillTableInDateGivenParty(
                                (txtPartyName.getText() == null ? "" : txtPartyName.getText())
                        ) == null ? "" : String.format("%.4f", Double.parseDouble(
                        outstandingAnalysisHelper.fillTableInDateGivenParty(
                                (txtPartyName.getText() == null ? "" : txtPartyName.getText())
                        )
                ))
                );
                //        int x=getSaleBillNo(txtPartyName.getText());
                //        if(x==0){
                //            x=getBillNo();
                //        }
                //        System.out.print(x);
                if (save.getText().trim().equalsIgnoreCase("Save")) {
                    txtBill.setText(Integer.toString(getBillNofromSales()));
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtPartyNameFocusLost

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusGained
        txtPartyName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
    }//GEN-LAST:event_txtPartyNameFocusGained

    private void tblPurchasesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchasesListMouseClicked
        selectedRow = tblPurchasesList.getSelectedRow();

        if (DBController.isDatabaseConnected()) {
            //clearTextFields();

            fieldsData = DBController.executeQuery("SELECT * FROM "
                    + DatabaseCredentials.SALES_TABLE + " WHERE id = '" + tblPurchasesList.getValueAt(selectedRow, 0) + "'");

            id = Integer.valueOf(fieldsData.remove(0).toString());

            try {
                datePurchaseDate.setDate(dateFormat.parse(fieldsData.get(0).toString()));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }

            cmbTerms.setSelectedItem(fieldsData.get(1).toString());
            txtPartyName.setText(fieldsData.get(2).toString());

            if (fieldsData.get(3) != null) {
                txtBill.setText(fieldsData.get(3).toString());
            }

            if (fieldsData.get(4) != null) {
                lblGST.setText(fieldsData.get(4).toString());
            }

            lblPreviousBalance.setText(fieldsData.get(5).toString());
            txtItemName.setText(fieldsData.get(6).toString());
            txthuid.setText(fieldsData.get(7).toString());
            if (fieldsData.get(8) != null) {
                JOptionPane.showMessageDialog(this, fieldsData.get(8).toString());
                txtQty.setText(fieldsData.get(8).toString());
            }

            if (fieldsData.get(9) != null) {
                lblAddress.setText(fieldsData.get(9).toString());
            } else {
                lblAddress.setText("---");
            }

            if (fieldsData.get(10) != null) {
                txtBeedsWt.setText(fieldsData.get(10).toString());
            }

            if (fieldsData.get(11) != null) {
                txtNetWt.setText(fieldsData.get(11).toString());
            }

            if (fieldsData.get(12) != null) {
                txtDiamondWt.setText(fieldsData.get(12).toString());
            }

            if (fieldsData.get(13) != null) {
                txtDiamondRate.setText(fieldsData.get(13).toString());
            }

            if (fieldsData.get(14) != null) {
                txtGrossWt.setText(fieldsData.get(14).toString());
            }

            if (fieldsData.get(15) != null) {
                txtItemDescription.setText(fieldsData.get(15).toString());
            }

            cmbPer.setSelectedItem(fieldsData.get(16).toString());

            if (fieldsData.get(17) != null) {
                txtExtraCharge.setText(fieldsData.get(17).toString());
            }

            if (fieldsData.get(18) != null) {
                txtBasicAmt.setText(fieldsData.get(18).toString());
            }

            if (fieldsData.get(19) != null) {
                txtNetAmt.setText(fieldsData.get(19).toString());
            }

            if (fieldsData.get(20) != null) {
                txtRate.setText(fieldsData.get(20).toString());
            }
            if (fieldsData.get(21) != null) {
                txtRate.setText(fieldsData.get(21).toString());
            }

            if (fieldsData.get(22) != null) {
                txtTaxableAmt.setText(fieldsData.get(22).toString());
            }
            if (fieldsData.get(23) != null) {
                txtGSTPercent.setText(fieldsData.get(23).toString());
            }
            if (fieldsData.get(24) != null) {
                txtGSTAmt.setText(fieldsData.get(24).toString());
            }
            if (fieldsData.get(25) != null) {
                txtExtraCharge1.setText(fieldsData.get(25).toString());
            }
            if (fieldsData.get(26) != null) {
                txtExtraCharge2.setText(fieldsData.get(26).toString());
            }
            if (fieldsData.get(27) != null) {
                txtDiscount.setText(fieldsData.get(27).toString());
            }
            if (fieldsData.get(28) != null) {
                tagno = fieldsData.get(28).toString();
            }

            setPartyDetails(fieldsData.get(2).toString());
        }
    }//GEN-LAST:event_tblPurchasesListMouseClicked

    private void cmbPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPerKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtExtraCharge2);
            setBg();
            double labour = getlabourcharge();
            txtExtraCharge2.setText(String.format("%.2f", labour));

            txtExtraCharge2.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_cmbPerKeyReleased

    private void txtBasicAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicAmtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiscount);
            setBg();
            txtDiscount.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtBasicAmtKeyReleased

    private void txtBasicAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusLost
        txtBasicAmt.setBackground(Color.white);
        txtTaxableAmt.setText(txtBasicAmt.getText());
    }//GEN-LAST:event_txtBasicAmtFocusLost

    private void txtBasicAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusGained
        txtBasicAmt.setBackground(new Color(245, 230, 66));
        calculateBasicAmount();
    }//GEN-LAST:event_txtBasicAmtFocusGained

    private void txtExtraChargeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraChargeKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtBasicAmt);
            setBg();
            txtBasicAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtExtraChargeKeyReleased

    private void txtExtraChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraChargeFocusLost
        txtExtraCharge.setBackground(Color.white);
        if (txtExtraCharge.getText().trim().isEmpty()) {
            txtExtraCharge.setText("0");
        }
    }//GEN-LAST:event_txtExtraChargeFocusLost

    private void txtRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtExtraCharge1);
            setBg();
            txtExtraCharge1.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtRateKeyReleased

    private void txtItemDescriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemDescriptionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemDescriptionKeyTyped

    private void txtItemDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemDescriptionKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtQty);
            setBg();
            txtQty.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtItemDescriptionKeyReleased

    private void txtGrossWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossWtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtBeedsWt);
            setBg();
            txtBeedsWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtGrossWtKeyReleased

    private void txtDiamondRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondRateKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtItemDescription);
            setBg();
            txtItemDescription.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiamondRateKeyReleased

    private void txtDiamondRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiamondRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiamondRateActionPerformed

    private void txtDiamondRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateFocusLost
        txtDiamondRate.setBackground(Color.white);
        if (txtDiamondRate.getText().trim().isEmpty()) {
            txtDiamondRate.setText("0");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiamondRateFocusLost

    private void txtDiamondWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondWtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiamondRate);
            setBg();
            txtDiamondRate.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiamondWtKeyReleased

    private void txtDiamondWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtFocusLost
        txtDiamondWt.setBackground(Color.white);
        if (txtDiamondWt.getText().trim().isEmpty()) {
            txtDiamondWt.setText("0");
        }
    }//GEN-LAST:event_txtDiamondWtFocusLost

    private void txtNetWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetWtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiamondWt);
            setBg();
            txtDiamondWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtNetWtKeyReleased

    private void txtBeedsWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBeedsWtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            verifyNetWeightCalculationFields();
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                setFocus(evt, txtNetWt);
                setBg();
                txtNetWt.setBackground(Color.LIGHT_GRAY);
            }
        }

    }//GEN-LAST:event_txtBeedsWtKeyReleased

    private void txtBeedsWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeedsWtFocusLost
        txtBeedsWt.setBackground(Color.white);
        if (txtBeedsWt.getText().trim().isEmpty()) {
            txtBeedsWt.setText("0");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtBeedsWtFocusLost

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtRate);
            setBg();
            txtRate.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtQtyKeyReleased
    String scannedTagNo = "";
    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyActionPerformed
    private void showAllItemDataWithTheHelpOfTagNo() {
//        txtItemName.getText().toString().trim().replace("+", "");
        if (originalItemName.trim().contains("+")) {
            scannedTagNo += txtItemName.getText();
            txtItemName.setText(scannedTagNo.toString().trim().replace("+", ""));
            String sql = "SELECT  itemname,huid,grosswt,beedswt,netwt,diamondwt,carats,polishpercent,taxslab FROM entryitem WHERE UPPER(tagno) LIKE UPPER('%" + scannedTagNo.toString().trim().replace("+", "") + "%')";

            try {
                Connection con = DBConnect.connect();
                Statement stmt = con.createStatement();
                ResultSet re = stmt.executeQuery(sql);
                txtQty.setText("1");
                while (re.next()) {
                    txtItemName.setText(re.getString(1));
                    txthuid.setText(re.getString(2));
                    txtGrossWt.requestFocus();
                    txtGrossWt.setText(re.getString(3));
                    txtBeedsWt.requestFocus();
                    txtBeedsWt.setText(re.getString(4));
                    txtNetWt.requestFocus();
                    txtNetWt.setText(re.getString(5));
                    txtDiamondWt.requestFocus();
                    txtDiamondWt.setText(re.getString(6));
                    txtDiamondRate.requestFocus();
                    txtDiamondRate.setText(re.getString(7));
                    txtExtraCharge1.requestFocus();
                    txtExtraCharge1.setText(re.getString(8));
                    txtGSTPercent.setText(re.getString(9).replace("%", ""));
                    txtRate.requestFocus();
                    txtExtraCharge1.requestFocus();
                    cmbPer.requestFocus();
                    txtExtraCharge2.requestFocus();
                    txtExtraCharge.requestFocus();
                    txtBasicAmt.requestFocus();
                    txtDiscount.requestFocus();
                    txtTaxableAmt.requestFocus();
                    txtGSTPercent.requestFocus();
                    txtGSTAmt.requestFocus();
                    txtNetAmt.requestFocus();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
        scannedTagNo = "";

//        JOptionPane.showMessageDialog(this, txtItemName.getText().toString().trim().replace("+", ""));
    }
    String originalItemName = "";
    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased
        originalItemName += txtItemName.getText().trim();
        if (txtItemName.getText().trim().toString().contains("+")) {

        } else {
            if (bool) {
                pmItemNameSuggestionsDetailsPopup.setVisible(false);
                pmItemNameSuggestionsPopup.setVisible(true);
            } else {
                pmItemNameSuggestionsDetailsPopup.setVisible(true);
                pmItemNameSuggestionsPopup.setVisible(false);
            }
            switch (evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    pmItemNameSuggestionsPopup.setVisible(false);
                    break;
                case KeyEvent.VK_ENTER:
                    if (bool) {
                        txtItemName.setText(txtItemName.getText());

                        EventQueue.invokeLater(() -> {
                            if (!txtItemName.getText().trim().isEmpty()) {
                                setItemRate(txtItemName.getText().trim());
//                            getHuid(txtItemName.getText().trim());
                                String itemName = tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 0).toString();
                                String itemgrp = tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 1).toString();
                                txtItemName.setText(itemName);
                                pmItemNameSuggestionsPopup.setVisible(false);
                                pmItemNameSuggestionsDetailsPopup.setVisible(true);
                                filltblItemNameSuggestionDetails(itemName, itemgrp);
                                tblItemNameSuggestionsDetails.requestFocus();
                                txtQty.setText("1");

                            } else {
                                JOptionPane.showMessageDialog(null, "Item name cannot be empty");
                                setBg();
                                txtItemName.requestFocusInWindow();

                                txtItemName.setBackground(Color.LIGHT_GRAY);

                            }

                        });

                        if (!txtQty.getText().trim().isEmpty()) {
//                        purchaseItemsDetailsDialog.setTableRowCount(Integer.parseInt(txtQty.getText().trim()));
//                        purchaseItemsDetailsDialog.setVisible(true);
//
//                        purchaseItemsDetailsDialog.populateItemsDetailsTableSale("SELECT huid, "
//                                + "grosswt, beedswt,netwt,diamondwt FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
//                                + " WHERE itemname LIKE " + "'" + txtItemName.getText() + "'");
                        }
                        setBg();
                        bool = false;
                        selectedrow = 0;
                    } else {
                        txtItemName.setText(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 1).toString());
                        setItemRate(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 1).toString());
                        tagno = tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 0).toString();
                        getHuid(tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                .getSelectedRow(), 1).toString(),
                                tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                        .getSelectedRow(), 0).toString(),
                                tblItemNameSuggestionsDetails.getValueAt(tblItemNameSuggestionsDetails
                                        .getSelectedRow(), 2).toString());
//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			txtQty.setText("1");
                        pmItemNameSuggestionsDetailsPopup.setVisible(false);
                        setFocus(evt, txthuid);
                        setBg();
                        txthuid.setBackground(Color.LIGHT_GRAY);
                        bool = true;
                        selectedrow = 0;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (bool) {
                        tblItemNameSuggestions.requestFocus();
                        if (selectedrow == 0) {
                            tblItemNameSuggestions.setRowSelectionInterval(0, 0);
                            selectedrow++;
                        } else {
                            if (tblItemNameSuggestions.getSelectedRow() < tblItemNameSuggestions.getRowCount() - 1) {
                                tblItemNameSuggestions.setRowSelectionInterval(tblItemNameSuggestions.getSelectedRow() + 1, tblItemNameSuggestions.getSelectedRow() + 1);
                            }
                        }
                        txtItemName.setText(tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 0).toString().trim());
                    } else {
                        tblItemNameSuggestionsDetails.requestFocus();
                        if (selectedrow == 0) {
                            tblItemNameSuggestionsDetails.setRowSelectionInterval(0, 0);
                            selectedrow++;
                        } else {
                            if (tblItemNameSuggestionsDetails.getSelectedRow() < tblItemNameSuggestionsDetails.getRowCount() - 1) {
                                tblItemNameSuggestionsDetails.setRowSelectionInterval(tblItemNameSuggestionsDetails.getSelectedRow() + 1, tblItemNameSuggestionsDetails.getSelectedRow() + 1);
                            }
                        }

//                txtGSTPercent.setText(itemNameSuggestionsTableModel
//                        .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
//			txtQty.setText("1");
//                    pmItemNameSuggestionsDetailsPopup.setVisible(false);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (bool) {
                        tblItemNameSuggestions.requestFocus();

                        if (tblItemNameSuggestions.getSelectedRow() > 0) {
                            tblItemNameSuggestions.setRowSelectionInterval(tblItemNameSuggestions.getSelectedRow() - 1, tblItemNameSuggestions.getSelectedRow() - 1);
                        }

                        txtItemName.setText(tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 0).toString().trim());
                    } else {
                        tblItemNameSuggestionsDetails.requestFocus();

                        if (tblItemNameSuggestionsDetails.getSelectedRow() > 0) {
                            tblItemNameSuggestionsDetails.setRowSelectionInterval(tblItemNameSuggestionsDetails.getSelectedRow() - 1, tblItemNameSuggestionsDetails.getSelectedRow() - 1);
                        }
                    }
                    break;

                case KeyEvent.VK_ESCAPE:
                    txtItemName.requestFocusInWindow();
                    break;

//            case KeyEvent.VK_DOWN:
                ////                pmItemNameSuggestionsPopup.requestFocusInWindow();
//               
////                if(pmItemNameSuggestionsPopup.getComponentCount()>0){
////                    Component com=pmItemNameSuggestionsPopup.getComponent(0);
//////                    JOptionPane.showMessageDialog(this, com.getClass().getSimpleName());
////                    if(com instanceof JScrollPane){
////                       com.setBackground(Color.red);
////                       com.sets
////                    }
////                }
//                
//               if(tblItemNameSuggestions.getRowCount()>0){
//                   SwingUtilities.invokeLater(()->{
//                       MenuSelectionManager.defaultManager().clearSelectedPath();
//                       MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{pmItemNameSuggestionsPopup,spTblItemNameSuggestionsContainer});
//                   tblItemNameSuggestions.requestFocusInWindow();
//                   
//                   
//                   });
//               }
//                
////                JOptionPane.showMessageDialog(this, "vkdown");
//                break;
            default:
                    EventQueue.invokeLater(() -> {
                        if (bool) {
                            pmItemNameSuggestionsPopup.setVisible(true);
                            populateSuggestionsTableFromDatabase(itemNameSuggestionsTableModel, "SELECT DISTINCT itemname, "
                                    + "itemgroup FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                                    + " WHERE itemname LIKE " + "'" + txtItemName.getText() + "%'");
                        } //                        autoComplete(ITEM_NAMES, txtItemName.getText(), txtItemName);
                        else {
                            pmItemNameSuggestionsDetailsPopup.setVisible(true);
                        }
                    });
                    break;
            }
        }
    }//GEN-LAST:event_txtItemNameKeyReleased

    private void txtItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNameActionPerformed

    private void txtItemNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusLost
        txtItemName.setBackground(Color.white);
        pmItemNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsDetailsPopup.setVisible(false);
        showAllItemDataWithTheHelpOfTagNo();
    }//GEN-LAST:event_txtItemNameFocusLost

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        CreateAccountScreen sc = new CreateAccountScreen();
        sc.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void txthuidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthuidKeyReleased
//ds        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtGrossWt);
            setBg();
            txtGrossWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txthuidKeyReleased

    private void txtItemDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemDescriptionActionPerformed

    private void spTblItemNameSuggestionsContainerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_spTblItemNameSuggestionsContainerFocusLost
        // TODO add your handling code here:
        spTblItemNameSuggestionsContainer.setVisible(false);
    }//GEN-LAST:event_spTblItemNameSuggestionsContainerFocusLost

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        pmItemNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsDetailsPopup.setVisible(false);
    }//GEN-LAST:event_formMouseClicked

    private void pnlRootContainerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pnlRootContainerFocusLost
        // TODO add your handling code here:
        pmItemNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsDetailsPopup.setVisible(false);
    }//GEN-LAST:event_pnlRootContainerFocusLost

    private void pnlRootContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlRootContainerMouseClicked
        // TODO add your handling code here:
        pmItemNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsDetailsPopup.setVisible(false);
    }//GEN-LAST:event_pnlRootContainerMouseClicked

    private void pmPartyNameSuggestionsPopupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pmPartyNameSuggestionsPopupFocusLost
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }//GEN-LAST:event_pmPartyNameSuggestionsPopupFocusLost

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        pmItemNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsDetailsPopup.setVisible(false);
        pmPartyNameSuggestionsPopup.setVisible(false);
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void txtExtraCharge2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge2FocusGained
        txtExtraCharge2.setBackground(new Color(245, 230, 66));
        double labour = getlabourcharge();
        txtExtraCharge2.setText(String.format("%.2f", labour));
    }//GEN-LAST:event_txtExtraCharge2FocusGained

    private void exbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exbtnActionPerformed
//        billno = getBillNo();
        billno = Integer.parseInt(txtBill.getText().trim());

        ex.setVisible(true);
        //  System.out.println("h");
//             if(ex.isVisible()==false){
//               txtexchange.setText(String.valueOf(exchngamt))  ;
//             }            
// TODO add your handling code here:
    }//GEN-LAST:event_exbtnActionPerformed

    private void txtoutstandingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtoutstandingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtoutstandingActionPerformed

    private void txtNetAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNetAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNetAmtActionPerformed

    private void txtexchangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtexchangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtexchangeActionPerformed

    private void txtexchangeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtexchangeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int t = Integer.parseInt(txtgrandtotal.getText());
            int y = Integer.parseInt(txtexchange.getText());
            txtoutstanding.setText(String.valueOf(t - y));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtexchangeKeyReleased

    private void txtBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBillActionPerformed

    private void txtPartyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameActionPerformed

    private void txtDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusLost
        txtDiscount.setBackground(Color.white);

        if (txtDiscount.getText().trim().isEmpty()) {
            txtDiscount.setText("0");
        }
        double taxableAmount = Double.parseDouble(txtBasicAmt.getText()) - Double.parseDouble(txtDiscount.getText());
        txtTaxableAmt.setText(String.format("%.3f", taxableAmount));

    }//GEN-LAST:event_txtDiscountFocusLost

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtTaxableAmt);
            setBg();
            txtTaxableAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        txtQty.setBackground(Color.white);
        if (RealSettingsHelper.gettagNoIsTrue()) {
            txtQty.setText("1");
        }
    }//GEN-LAST:event_txtQtyFocusLost

    private void txtBillFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillFocusLost
        txtBill.setBackground(Color.white);
        if (save.getText().trim().equalsIgnoreCase("Save")) {
            try {
                Connection con = DBConnect.connect();
                Statement stmt = con.createStatement();
                String sql = "select bill from sales";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getString("bill").equals(txtBill.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Bill No Already Used ..Please use Another Bill");
                        txtBill.requestFocus();
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }//GEN-LAST:event_txtBillFocusLost

    private void txtBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtBillKeyPressed
    boolean bool = true;
    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed


    }//GEN-LAST:event_txtItemNameKeyPressed

    private void paymentbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentbtnMouseClicked
        // TODO add your handling code here:
        CashAndBankPaymentsSales.fatchData(billToBePrinted);

//        CashAndBankPaymentsSales.setChequeAmount("");
//        CashAndBankPaymentsSales.setCardAmount1("");
//        CashAndBankPaymentsSales.setCardAmount2("");
//        CashAndBankPaymentsSales.seteWallteAmount("");
//        CashAndBankPaymentsSales.setCashAmount("");
//        CashAndBankPaymentsSales.setCard1Transectionno("");
//        CashAndBankPaymentsSales.setCard2Transectionno("");
//        CashAndBankPaymentsSales.setEwalletTransectionno("");
//        CashAndBankPaymentsSales.setChequeTransectionno("");
//        CashAndBankPaymentsSales.setBankName("");
//        CashAndBankPaymentsSales.setChequno("");
//        CashAndBankPaymentsSales.setBalanceamount("");
//        CashAndBankPaymentsSales.fatchData(billToBePrinted);
        CashAndBankPaymentsSales.setBalanceamount(String.valueOf((int) Double.parseDouble(txtgrandtotal.getText().trim())));
        CashAndBankPaymentsSales.setVisible(true);
    }//GEN-LAST:event_paymentbtnMouseClicked

    private void exbtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exbtnMouseEntered
        // TODO add your handling code here:
        try {
//             JOptionPane.showMessageDialog(this,LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_exbtnMouseEntered

    private void cmbTermsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbTermsPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbTermsPropertyChange

    private void cmbTermsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTermsFocusLost
        cmbTerms.setBackground(Color.white);
        if (txtPartyName.getText().trim().equalsIgnoreCase("Cash")) {
//            cmbTerms.removeAllItems();
            cmbTerms.setSelectedItem("Cash");
        } else if (txtPartyName.getText().trim().equalsIgnoreCase("Credit")) {
            cmbTerms.setSelectedItem("Credit");
        }
    }//GEN-LAST:event_cmbTermsFocusLost

    private void cmbTermsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbTermsMouseClicked
        // TODO add your handling code here:
        if (txtPartyName.getText().trim().equalsIgnoreCase("Cash")) {
//            cmbTerms.removeAllItems();
            cmbTerms.setSelectedItem("Cash");
        } else if (txtPartyName.getText().trim().equalsIgnoreCase("Credit")) {
            cmbTerms.setSelectedItem("Credit");
        }
    }//GEN-LAST:event_cmbTermsMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select * from Systemname limit 1";
            ResultSet re = stmt.executeQuery(sql);
            if (!re.next()) {
                jPanel2.setVisible(true);
            } else {
                whatsuppanel.setVisible(true);

            }
            con.close();
            stmt.close();
            re.close();

        } catch (Exception e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

        }


    }//GEN-LAST:event_jButton1ActionPerformed
    String Systemname = null;
    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
        // TODO add your handling code here:

        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select * from Systemname limit 1";
            ResultSet re = stmt.executeQuery(sql);
            while (re.next()) {
                Systemname = re.getString(2);
            }
            con.close();
            stmt.close();
            re.close();

        } catch (Exception e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

        }
        String path2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "jasper_reports" + File.separator + "jasperpdf" + File.separator;
        try {
            if (phnno.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Phone No");
            } else if (!type1.isSelected() && !type2.isSelected() && !type3.isSelected()) {
                JOptionPane.showMessageDialog(this, "Select Bill Type");
            } else {
                Thread th = new Thread(() -> {
                    Whatsupblaster wa = new Whatsupblaster();
                    List<String> phoneNo = new ArrayList<>();
                    String message = "Sale Report";
                    if (!jTextField2.getText().trim().isEmpty()) {
                        message = jTextField2.getText().trim();
                    }

                    String[] numbers = phnno.getText().trim().split("\\,");
                    for (int i = 0; i < numbers.length; i++) {
                        phoneNo.add(numbers[i]);
                    }
//                    phoneNo.add();

                    int i = wa.sendDocAndMessage(Systemname.trim(), path2 + txtBill.getText().trim() + Type + ".pdf", phoneNo, message);
                    JOptionPane.showMessageDialog(this, "send Message " + i + " Outoff " + phoneNo.size());
                    whatsuppanel.setVisible(false);

                });
                th.start();

            }

        } catch (Exception e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

            e.printStackTrace();
        }
    }//GEN-LAST:event_SendActionPerformed

    private void type1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_type1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_type1ActionPerformed
    String Type = "";
    private void type2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_type2MouseClicked

        if (type2.isSelected()) {
            type1.setSelected(false);
            type3.setSelected(false);
            Type = "Type 2";
            try {
                printSaleBillJasperReport(Integer.parseInt(txtBill.getText().trim()), "Type 2");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_type2MouseClicked

    private void type3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_type3MouseClicked
        if (type3.isSelected()) {
            try {
                type1.setSelected(false);
                type2.setSelected(false);
                Type = "Type 3";
                printSaleBillJasperReport(Integer.parseInt(txtBill.getText().trim()), "Type 3");
            } // TODO add your handling code here:
            catch (FileNotFoundException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_type3MouseClicked

    private void type1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_type1MouseClicked
        if (type1.isSelected()) {
            try {
                type2.setSelected(false);
                type3.setSelected(false);
                Type = "Type 1";
                printSaleBillJasperReport(Integer.parseInt(txtBill.getText().trim()), "Type 1");
            } // TODO add your handling code here:
            catch (FileNotFoundException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_type1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            if (jTextField1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter System name");
            } else {
                String sql = null;
                Connection con = DBConnect.connect();
                Statement stmt = con.createStatement();
                if (jButton2.getText().trim().equalsIgnoreCase("Update")) {
                    sql = "update  Systemname set Systemname='" + jTextField1.getText().trim() + "'";

                } else {
                    sql = "insert into Systemname (Systemname) values ('" + jTextField1.getText().trim() + "')";

                }
                stmt.executeUpdate(sql);
                stmt.close();
                con.close();
                jTextField1.setText("");
            }
        } // TODO add your handling code here:
        catch (Exception ex) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel41MouseClicked
        // TODO add your handling code here:
        jPanel2.setVisible(false);
    }//GEN-LAST:event_jLabel41MouseClicked

    private void jLabel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel42MouseClicked
        whatsuppanel.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel42MouseClicked

    private void jLabel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel40MouseClicked
        // TODO add your handling code here:
        jPanel2.setVisible(true);
        jButton2.setText("Update");
    }//GEN-LAST:event_jLabel40MouseClicked

    private void txtItemNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusGained
        txtItemName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
        bool = true;
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNameFocusGained

    private void txthuidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthuidKeyPressed


    }//GEN-LAST:event_txthuidKeyPressed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        if (selectedRow != -1) {

            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            if (JOptionPane.showConfirmDialog(SaleScreen.this,
                    "Are you sure you want delete the selected record", "Delete Record",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String id = tblPurchasesList.getValueAt(selectedRow, 0).toString();
                try {
                    markItemUnsold(id);
                } catch (SQLException ex) {
                    Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                }

                boolean rowDeleted
                        = DBController.removeDataFromTable(DatabaseCredentials.SALES_TABLE,
                                "id", tblPurchasesList.getValueAt(selectedRow, 0));

                if (rowDeleted) {
                    try {
                        Connection con = DBConnect.connect();
                        Statement stmt = con.createStatement();
                        String sql = "select id from sales where bill='" + txtBill.getText().trim() + "'";
                        ResultSet re = stmt.executeQuery(sql);
                        if (!re.next()) {
                            String sql2 = "delete from exchange where bill='" + txtBill.getText().trim() + "'";
                            stmt.execute(sql2);
                            stmt.close();
                            re.close();
                            con.close();
                        }
                    } catch (Exception e) {

                    }

                    CashAndBankPaymentsSales.deleteMethod(txtBill.getText());

                    populateSalesListTable();

                    fillgrandtotal();
                    JOptionPane.showMessageDialog(this, "Record deleted successfully");
                }

            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        printbuttonclicked();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        clearTextFields();
        clearLabels();
        soldItemsForCurrentBill.clear();
        salesListTableModel.setRowCount(0);
        txtBill.setText(String.valueOf(getBillNofromSales()));
        salesListTableModel.setRowCount(0);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    private void txtBillFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillFocusGained
        txtBill.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtBillFocusGained

    private void cmbTermsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTermsFocusGained
        cmbTerms.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbTermsFocusGained

    private void txthuidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthuidFocusGained
        txthuid.setBackground(new Color(245, 230, 66));
        if (!txtItemName.getText().trim().isEmpty()) {

            Object itemRate = setItemRate(txtItemName.getText().trim());

            if ((itemRate == null)) {
//                txtNetAmt.setText("");
                try {
                    if (originalItemName.trim().contains("+")) {
//                        txtRate.setText("0");

                        JOptionPane.showMessageDialog(this, "Please Go To Home Page And Add Today Price");
                    }
                } catch (Exception e) {
                    Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

                }
            }
            originalItemName = "";
        }

    }//GEN-LAST:event_txthuidFocusGained

    private void txthuidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthuidFocusLost
        txthuid.setBackground(Color.white);
    }//GEN-LAST:event_txthuidFocusLost

    private void txtGrossWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGrossWtFocusGained
        txtGrossWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGrossWtFocusGained

    private void txtGrossWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGrossWtFocusLost
        txtGrossWt.setBackground(Color.white);
    }//GEN-LAST:event_txtGrossWtFocusLost

    private void txtBeedsWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeedsWtFocusGained
        txtBeedsWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtBeedsWtFocusGained

    private void txtNetWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWtFocusGained
        txtNetWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtNetWtFocusGained

    private void txtNetWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWtFocusLost
        txtNetWt.setBackground(Color.white);
    }//GEN-LAST:event_txtNetWtFocusLost

    private void txtDiamondWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtFocusGained
        txtDiamondWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiamondWtFocusGained

    private void txtDiamondRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateFocusGained
        txtDiamondRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiamondRateFocusGained

    private void txtItemDescriptionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemDescriptionFocusGained
        txtItemDescription.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtItemDescriptionFocusGained

    private void txtItemDescriptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemDescriptionFocusLost
        txtItemDescription.setBackground(Color.white);
    }//GEN-LAST:event_txtItemDescriptionFocusLost

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        txtQty.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusGained
        txtRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtRateFocusGained

    private void txtRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusLost
        txtRate.setBackground(Color.white);
    }//GEN-LAST:event_txtRateFocusLost

    private void txtExtraCharge1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge1FocusGained
        txtExtraCharge1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtExtraCharge1FocusGained

    private void cmbPerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbPerFocusGained
        cmbPer.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbPerFocusGained

    private void cmbPerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbPerFocusLost
        cmbPer.setBackground(Color.white);
    }//GEN-LAST:event_cmbPerFocusLost

    private void txtExtraChargeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraChargeFocusGained
        txtExtraCharge.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtExtraChargeFocusGained

    private void txtDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusGained
        txtDiscount.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiscountFocusGained

    private void txtTaxableAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxableAmtFocusLost
        txtTaxableAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtTaxableAmtFocusLost

    private void txtGSTPercentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTPercentFocusGained
        txtGSTPercent.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGSTPercentFocusGained

    private void txtGSTPercentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTPercentFocusLost
        txtGSTPercent.setBackground(Color.white);
    }//GEN-LAST:event_txtGSTPercentFocusLost

    private void txtGSTAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTAmtFocusLost
        txtGSTAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtGSTAmtFocusLost

    private void txtNetAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusLost
        txtNetAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtNetAmtFocusLost

    private void txtdiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiscountFocusGained
        txtdiscount.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtdiscountFocusGained

    private void txtdiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiscountFocusLost
        txtdiscount.setBackground(Color.white);
    }//GEN-LAST:event_txtdiscountFocusLost

    private void txtgrandtotalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgrandtotalFocusGained
        txtgrandtotal.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtgrandtotalFocusGained

    private void txtgrandtotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgrandtotalFocusLost
        txtgrandtotal.setBackground(Color.white);
    }//GEN-LAST:event_txtgrandtotalFocusLost

    private void txtreceiveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtreceiveFocusGained
        txtreceive.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtreceiveFocusGained

    private void txtreceiveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtreceiveFocusLost
        txtreceive.setBackground(Color.white);
    }//GEN-LAST:event_txtreceiveFocusLost

    private void txtoutstandingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtoutstandingFocusGained
        txtoutstanding.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtoutstandingFocusGained

    private void txtoutstandingFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtoutstandingFocusLost
        txtoutstanding.setBackground(Color.white);
    }//GEN-LAST:event_txtoutstandingFocusLost

    private void txtcountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcountFocusGained
        txtcount.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtcountFocusGained

    private void txtcountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcountFocusLost
        txtcount.setBackground(Color.white);
    }//GEN-LAST:event_txtcountFocusLost

    private void txtnetwtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnetwtFocusGained
        txtnetwt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtnetwtFocusGained

    private void txtnetwtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnetwtFocusLost
        txtnetwt.setBackground(Color.white);
    }//GEN-LAST:event_txtnetwtFocusLost

    private void txtexchangeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtexchangeFocusGained
        txtexchange.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtexchangeFocusGained

    private void txtexchangeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtexchangeFocusLost
        txtexchange.setBackground(Color.white);
    }//GEN-LAST:event_txtexchangeFocusLost

    private void phnnoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phnnoFocusGained
        phnno.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_phnnoFocusGained

    private void phnnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phnnoFocusLost
        phnno.setBackground(Color.white);
    }//GEN-LAST:event_phnnoFocusLost

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        jTextField2.setBackground(Color.white);
    }//GEN-LAST:event_jTextField2FocusLost

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1FocusGained
        jComboBox1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jComboBox1FocusGained

    private void jComboBox1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1FocusLost
        jComboBox1.setBackground(Color.white);
    }//GEN-LAST:event_jComboBox1FocusLost

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedItem().equals("No GST")) {

            txtGSTPercent.setText("0");

        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtItemName);
            setBg();

        }

    }//GEN-LAST:event_jComboBox1KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6KeyPressed

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select * from Systemname limit 1";
            ResultSet re = stmt.executeQuery(sql);
            while (re.next()) {
                Systemname = re.getString(2);
            }
            con.close();
            stmt.close();
            re.close();

        } catch (Exception e) {
            Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, e);

        }
        Thread th = new Thread(() -> {
            Whatsupblaster wa = new Whatsupblaster();

            //wa.openQRCODE(Systemname.trim());
        });
        th.start();
    }//GEN-LAST:event_jButton6MouseClicked

    private void saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseEntered

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        try {
            if (txtPartyName.getText() == null || "".equals(txtPartyName.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Invalid Sale Entry !");
                return;
            }

            if (cmbTerms.getSelectedItem().equals("Cash") && (!txtoutstanding.getText().trim().equals("") && !txtoutstanding.getText().trim().equals(txtgrandtotal.getText().trim())
                    && !txtreceive.getText().trim().equals("0.00") && !txtreceive.getText().trim().equals(""))) {
                JOptionPane.showMessageDialog(this, "Please select Credit Option to use Received and Credit Amount.");
                return;
            }

            if (txtreceive.getText() == null || "".equals(txtreceive.getText())) {
                txtreceive.setText("0");
            }
            if (txtoutstanding.getText() == null || "".equals(txtoutstanding.getText())) {
                if (cmbTerms.getSelectedItem().equals("Cash")) {
                    txtoutstanding.setText("0");
                } else {
                    txtoutstanding.setText(String.valueOf(Double.parseDouble(txtgrandtotal.getText())
                            - Double.parseDouble(txtreceive.getText())));
                }

            }
            // if(txtecxchange.getText() != null)
            //            if (cmbTerms.getSelectedItem().equals("Cash")) {
            ////            txtoutstanding.setText(String.valueOf(Double.parseDouble(txtgrandtotal.getText())
                ////                    - Double.parseDouble(txtreceive.getText())));
        //                txtreceive.setText("0");
        //            }
        // -Double.parseDouble(txtexchange.getText())
        billToBePrinted = Integer.parseInt(txtBill.getText().trim());

            try {
                insertDataFromPurchaseTableToDatabase();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            markItemsSold();
            if (save.getText().trim().equals("Update")) {
                try {
                    generateNewReceiptForSale(Double.parseDouble(txtreceive.getText()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (cmbTerms.getSelectedItem().equals("Credit")) {
                    try {
                        generateNewReceiptForSale(Double.parseDouble(txtreceive.getText()));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (cmbTerms.getSelectedItem().equals("Credit")) {
                // update due amount in account table
                updateDueAmtFromOutstanding(Double.parseDouble(txtoutstanding.getText()));

                Connection con = null;
                con = connect();
                String statement = "UPDATE `sales` SET `terms` = '" + cmbTerms.getSelectedItem() + "' WHERE bill =" + txtBill.getText().trim();
                try {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(statement);
                } catch (SQLException ex) {
                    Logger.getLogger(ExChange.class.getName()).log(Level.SEVERE, null, ex);
                }
                // write query here
            }
            //        CashUserDetails obj = null;
            //        if ("Cash".equalsIgnoreCase(txtPartyName.getText())){
            //         obj  = new CashUserDetails();
            //
            //        }
            //        JDialog d = new JDialog(obj,"Input",true);
            //        d.add(obj);
            //        d.setVisible(true);
            if (!"".equals(txtexchange.getText())) {
                if (save.getText().equals("Save")) {
                    try {
                        ex.fillData(billToBePrinted);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        ex.updateData(billToBePrinted);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SaleScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            int reply = JOptionPane.showConfirmDialog(pnlRootContainer, "Do you want a Print Preview ?", "Select Print", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                printbuttonclicked();
            }

            CashAndBankPaymentsSales.setChequeAmount("");
            CashAndBankPaymentsSales.setCardAmount1("");
            CashAndBankPaymentsSales.setCardAmount2("");
            CashAndBankPaymentsSales.seteWallteAmount("");
            CashAndBankPaymentsSales.setCashAmount("");
            CashAndBankPaymentsSales.setCard1Transectionno("");
            CashAndBankPaymentsSales.setCard2Transectionno("");
            CashAndBankPaymentsSales.setEwalletTransectionno("");
            CashAndBankPaymentsSales.setChequeTransectionno("");
            CashAndBankPaymentsSales.setBankName("");
            CashAndBankPaymentsSales.setChequno("");
            CashAndBankPaymentsSales.setBalanceamount("");
            soldItemsForCurrentBill.clear();
            salesListTableModel.setRowCount(0);
            clearTextFields();
            clearPartyFields();

            txtPartyName.requestFocusInWindow();
            txtPartyName.setBackground(Color.LIGHT_GRAY);
            id = -97108105;
            updateBillNumberField();
            if (obj != null) {
                obj.clear();
            }
            ex.clear();

        } catch (Exception e) {
            Logger.getLogger(ExChange.class.getName()).log(Level.SEVERE, null, e);

            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_saveMouseClicked

    private void spTblItemNameSuggestionsDetailsContainerFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        spTblItemNameSuggestionsDetailsContainer.setVisible(false);
    }

    private javax.swing.JTable tblItemNameSuggestionsDetails;
    private javax.swing.JScrollPane spTblItemNameSuggestionsDetailsContainer;
    private javax.swing.JPopupMenu pmItemNameSuggestionsDetailsPopup;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Send;
    private javax.swing.JButton btnAdd;
    public javax.swing.JButton closebtn;
    private javax.swing.JComboBox<String> cmbPer;
    private javax.swing.JComboBox<String> cmbTerms;
    private com.toedter.calendar.JDateChooser datePurchaseDate;
    private javax.swing.JButton exbtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    public javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblAddPartyName;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblEditPartyName;
    private javax.swing.JLabel lblGST;
    private javax.swing.JLabel lblPreviousBalance;
    private javax.swing.JLabel lblState;
    private javax.swing.JButton paymentbtn;
    private javax.swing.JTextField phnno;
    private javax.swing.JPopupMenu pmItemNameSuggestionsPopup;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    public javax.swing.JPanel pnlRootContainer;
    private javax.swing.JLabel save;
    private javax.swing.JScrollPane spTblItemNameSuggestionsContainer;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    private javax.swing.JTable tblItemNameSuggestions;
    private javax.swing.JTable tblPartyNameSuggestions;
    private javax.swing.JTable tblPurchasesList;
    private javax.swing.JTextField txtBasicAmt;
    private javax.swing.JTextField txtBeedsWt;
    private javax.swing.JTextField txtBill;
    private javax.swing.JTextField txtDiamondRate;
    private javax.swing.JTextField txtDiamondWt;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtExtraCharge;
    private javax.swing.JTextField txtExtraCharge1;
    private javax.swing.JTextField txtExtraCharge2;
    private javax.swing.JTextField txtGSTAmt;
    private javax.swing.JTextField txtGSTPercent;
    private javax.swing.JTextField txtGrossWt;
    private javax.swing.JTextField txtItemDescription;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtNetAmt;
    private javax.swing.JTextField txtNetWt;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtTaxableAmt;
    private javax.swing.JTextField txtcount;
    private javax.swing.JTextField txtdiscount;
    public static javax.swing.JTextField txtexchange;
    private javax.swing.JTextField txtgrandtotal;
    private javax.swing.JTextField txthuid;
    private javax.swing.JTextField txtnetwt;
    private javax.swing.JTextField txtoutstanding;
    public static javax.swing.JTextField txtreceive;
    private javax.swing.JCheckBox type1;
    private javax.swing.JCheckBox type2;
    private javax.swing.JCheckBox type3;
    private javax.swing.JPanel whatsuppanel;
    // End of variables declaration//GEN-END:variables
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SaleScreen().setVisible(true);
            }
        });
    }
}
