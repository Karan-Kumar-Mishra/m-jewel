package jewellery;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableCellRenderer;
import jewellery.helper.RealSettingsHelper;
import jewellery.helper.outstandingAnalysisHelper;

/**
 *
 * @author AR-LABS
 */
public class PurchaseScreen extends javax.swing.JFrame {

    int billid = -1;
    String tagNoOfSelectedId = null;
    List<List<String>> itemData = new ArrayList<>();
    boolean bool = false;
    private List<Object> columnNames = new ArrayList<>();
    private final List<Object> data = new ArrayList<>();
    private final List<Object> accountNames = new ArrayList<>();
    private List<Object> partyGSTAndBalance = new ArrayList<>();
    private static final List<Object> ITEM_NAMES = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DefaultTableModel itemNameSuggestionsTableModel = null;
    private DefaultTableModel partyNameSuggestionsTableModel = null;
    private DefaultTableModel itemNameDetailsTableModel = null;
    private DefaultTableModel tagnoTableModel = null;
    private static Logger logger = Logger.getLogger(PurchaseScreen.class.getName());
    private ImageIcon imageIcon;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;
    private List<Object> fieldsData;
    private int selectedRow = -1;
    private int id;
    private final PurchaseItemsDetailDialog purchaseItemsDetailsDialog = new PurchaseItemsDetailDialog(this, false, 0);
    public String itemprefixString = null;
    public DefaultTableModel purchasesListTableModel;
    Connection connect;
    private String reportsavepath;
    private List<List<List<String>>> FeildFilledRedirectDetails;
    public List<Map<String, Object>> currentBillItems;
    private double grossweight, netweight, totaltaxableamount, totalAmount;
    private String itemgrp;
    private JLabel jtitle = new JLabel("          Select Tag No.          ");
    
    
    private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        

    public PurchaseScreen() {
        Logger.getLogger(PurchaseScreen.class.getName()).log(Level.INFO, "purchase screen object initiazed!");
        try {
            initComponents();
            initPopups();
            cmbTerms.setSelectedItem("Credit");
            purchasesListTableModel = (DefaultTableModel) table.getModel();
            itemNameSuggestionsTableModel = (DefaultTableModel) tblItemNameSuggestions.getModel();
            partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
            itemNameDetailsTableModel = (DefaultTableModel) itemNameDetailsTable.getModel();
            tagnoTableModel = (DefaultTableModel) tagnoTable.getModel();

            FeildFilledRedirectDetails = new ArrayList<>();
            currentBillItems = new ArrayList<>();

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Dialog", Font.BOLD, 18));
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int h = d.height;
            int w = d.width;

            pnlRootContainer.setSize(w - 280, h - 100);

            centerTableCells();

            populatePurchasesListTable();

            setDateOnJCalender("yyyy-MM-dd");
//            setImageOnJLabel(lblDeleteButton, AssetsLocations.TRASH_CAN_ICON);
//            setImageOnJLabel(lblAttachButton, AssetsLocations.ATTACHMENT_ICON);
//            setImageOnJLabel(lblPrintButton, AssetsLocations.PRINT_ICON);
//            setImageOnJLabel(lblClearFields, AssetsLocations.CLEANING_BROOM_ICON);

            fetchAccountNames("Cash");
            fetchItemNames();

            pmItemNameSuggestionsPopup.add(spTblItemNameSuggestionsContainer);
            pmItemNameSuggestionsPopup.setLocation(txtItemName.getX() + 200, txtItemName.getY() + 150);
            itemNameDetailsPopup.add(itemNameDetailsContainer);
            itemNameDetailsPopup.setLocation(txtItemName.getX() + 200, txtItemName.getY() + 150);
            jtitle.setForeground(Color.RED);
            jtitle.setHorizontalAlignment(JLabel.CENTER);
            jtitle.setOpaque(true);
            panel.add(jtitle);
            tagNoPopup.add(panel);
            jtitle.setBackground(Color.ORANGE);
            panel.setBackground(Color.ORANGE);
            jtitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
            tagNoPopup.add(tagnoContainer);
            tagNoPopup.setBackground(Color.ORANGE);

            tagNoPopup.setLocation(txtQty.getX() + 6, txtItemName.getY() + 150);

            pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
            pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 200, txtPartyName.getY() + 150);

            updateBillNo();
            purchasesListTableModel.setRowCount(0);
            grossweight = 0.0;
            netweight = 0.0;
            totaltaxableamount = 0.0;
            totalAmount = 0.0;
            addComponentListeners(this.getContentPane().getComponents());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void updateBillNo() {
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(cast(bill as signed))  from purchasehistory;");
            int lastBill = 0;
            while (rs.next()) {
                lastBill = rs.getInt(1);
            }
            jTextField1.setText(String.valueOf(lastBill + 1));
            con.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void updateallthefieldsinarray() {
        columnNames.clear();
        data.clear();
        if (bool) {

            Map<String, Object> currentBill = new HashMap<>();
            for (int i = 0; i < Integer.parseInt(txtQty.getText()); i++) {
                currentBill.put("itemdescription", txtItemDescription.getText()); // description
                currentBill.put("date", datePurchaseDate.getDate()); // date
                currentBill.put("cmbterms", cmbTerms.getSelectedItem().toString());
                currentBill.put("partyname", txtPartyName.getText());
                currentBill.put("bill", jTextField1.getText());
                currentBill.put("totaldiamondweight", txtDiamondWt.getText());
                currentBill.put("totalnetwt", txtNetWt.getText());
                currentBill.put("itemname", txtItemName.getText());
                currentBill.put("gst", lblGST.getText());
                currentBill.put("discountamount", txtDiscountAmt.getText());
                currentBill.put("discountpercent", txtDiscountPercent.getText());
                currentBill.put("basicamount", txtBasicAmt.getText());
                currentBill.put("extracharge", txtExtraCharge.getText());
                currentBill.put("netamount", txtNetAmt.getText());
                currentBill.put("gstpercent", txtGSTPercent.getText());
                currentBill.put("taxableamount", txtTaxableAmt.getText());
                currentBill.put("rate", txtRate.getText());
                currentBill.put("diamondrate", txtDiamondRate.getText());
                currentBill.put("qty", txtQty.getText());
                currentBill.put("itemgroup", itemgrp);
                
            }
//                JOptionPane.showMessageDialog(this,currentBill);
            currentBillItems.removeAll(currentBillItems);
            currentBillItems.add(currentBill);
            fillTableWithAllEntries();
            clearnontotal();
            txtItemName.requestFocus();

        } else {
            if (fieldsAreValidated()) {
                Map<String, Object> currentBill = new HashMap<>();
                for (int i = 0; i < Integer.parseInt(txtQty.getText()); i++) {
                    currentBill.put("itemdescription", txtItemDescription.getText()); // description
                    currentBill.put("date", datePurchaseDate.getDate()); // date
                    currentBill.put("cmbterms", cmbTerms.getSelectedItem().toString());
                    currentBill.put("partyname", txtPartyName.getText());
                    currentBill.put("bill", jTextField1.getText());
                    currentBill.put("totaldiamondweight", txtDiamondWt.getText());
                    currentBill.put("totalnetwt", txtNetWt.getText());
                    currentBill.put("itemname", txtItemName.getText());
                    currentBill.put("gst", lblGST.getText());
                    currentBill.put("discountamount", txtDiscountAmt.getText());
                    currentBill.put("discountpercent", txtDiscountPercent.getText());
                    currentBill.put("basicamount", txtBasicAmt.getText());
                    currentBill.put("extracharge", txtExtraCharge.getText());
                    currentBill.put("netamount", txtNetAmt.getText());
                    currentBill.put("gstpercent", txtGSTPercent.getText());
                    currentBill.put("taxableamount", txtTaxableAmt.getText());
                    currentBill.put("rate", txtRate.getText());
                    currentBill.put("diamondrate", txtDiamondRate.getText());
                    currentBill.put("qty", txtQty.getText());
                    currentBill.put("itemgroup", itemgrp);
                }
//                JOptionPane.showMessageDialog(this,currentBill);
                currentBillItems.removeAll(currentBillItems);
                currentBillItems.add(currentBill);
                fillTableWithAllEntries();
                clearnontotal();
                txtItemName.requestFocus();
            }
        }
    }

    private void addComponentListeners(Component[] components) {
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        updateallthefieldsinarray();
                    }
                });
            } else if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                comboBox.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("selectedItem")) {
                            updateallthefieldsinarray();
                        }
                    }
                });
            }
            // Add other cases for handling other types of components if needed
        }
    }

    private void fillTableWithAllEntries() {
        try {
            double taxableamt = 0.0, netamt = 0.0, netwt = 0.0, grosswt = 0.0, diowt = 0.0;
//        int count = FeildFilledRedirectDetails.get(FeildFilledRedirectDetails.size() - 1).size();
            List<List<String>> lastItemDetails = FeildFilledRedirectDetails.get(FeildFilledRedirectDetails.size() - 1);
            Map<String, Object> lastBillItems = currentBillItems.get(currentBillItems.size() - 1);

            taxableamt = Double.parseDouble(lastBillItems.get("taxableamount").toString());
            netamt = Double.parseDouble(lastBillItems.get("netamount").toString());

            for (int i = 0; i < lastItemDetails.size(); i++) {
                netwt += Double.parseDouble(lastItemDetails.get(i).get(3));
                grosswt += Double.parseDouble(lastItemDetails.get(i).get(1));
                diowt += Double.parseDouble(lastItemDetails.get(i).get(4));

                double netwtcontribution = Double.parseDouble(lastItemDetails.get(i).get(3));
                double diawtcontribution = Double.parseDouble(lastItemDetails.get(i).get(4));
                double totalnetamountfromdiamond = Double.parseDouble(lastBillItems.get("diamondrate").toString()) * diawtcontribution;
                double totalnetamountfromnetwt = Double.parseDouble(lastBillItems.get("rate").toString()) * netwtcontribution;
                double gstpercent = Double.parseDouble(lastBillItems.get("gstpercent").toString());

                double extracharge = Double.parseDouble(lastBillItems.get("extracharge").toString())
                        - Double.parseDouble(lastBillItems.get("discountamount").toString());

                double additional = (extracharge / Double.parseDouble(lastBillItems.get("qty").toString()));
                double totaltaxableamountforitem = additional + totalnetamountfromdiamond + totalnetamountfromnetwt;

                double totalnetamountforitem = totaltaxableamountforitem + (gstpercent / 100) * totaltaxableamountforitem;
                if (billid > 0) {
                    purchasesListTableModel.setValueAt(lastBillItems.get("itemname"), table.getSelectedRow(), 1);
                    purchasesListTableModel.setValueAt(lastItemDetails.get(i).get(0), table.getSelectedRow(), 2);
                    purchasesListTableModel.setValueAt(lastItemDetails.get(i).get(1), table.getSelectedRow(), 3);
                    purchasesListTableModel.setValueAt(lastItemDetails.get(i).get(3), table.getSelectedRow(), 4);
                    if (!RealSettingsHelper.gettagNoIsTrue()) {
                        purchasesListTableModel.setValueAt(txtQty.getText(), table.getSelectedRow(), 5);
                    } else {
                        purchasesListTableModel.setValueAt("1", table.getSelectedRow(), 5);
                    }

                    purchasesListTableModel.setValueAt(String.format("%.2f", totaltaxableamountforitem), table.getSelectedRow(), 6);
                    purchasesListTableModel.setValueAt(lastBillItems.get("gstpercent"), table.getSelectedRow(), 7);
                    purchasesListTableModel.setValueAt(String.format("%.2f", totalnetamountforitem), table.getSelectedRow(), 8);
                    table.clearSelection();
//                purchasesListTableModel.setValueAt(lastBillItems.get("itemname"), table.getSelectedRow(), 1);
//                purchasesListTableModel.setValueAt(lastBillItems.get("itemname"), table.getSelectedRow(), 1);
                } else {
                    if (!RealSettingsHelper.gettagNoIsTrue()) {
                        purchasesListTableModel.addRow(new Object[]{
                            "",
                            lastBillItems.get("itemname"),
                            lastItemDetails.get(i).get(0),
                            lastItemDetails.get(i).get(1),
                            lastItemDetails.get(i).get(3),
                            txtQty.getText(),
                            String.format("%.2f", totaltaxableamountforitem),
                            lastBillItems.get("gstpercent"),
                            String.format("%.2f", totalnetamountforitem)
                        });
                    } else {
                        purchasesListTableModel.addRow(new Object[]{
                            "",
                            lastBillItems.get("itemname"),
                            lastItemDetails.get(i).get(0),
                            lastItemDetails.get(i).get(1),
                            lastItemDetails.get(i).get(3),
                            "1",
                            String.format("%.2f", totaltaxableamountforitem),
                            lastBillItems.get("gstpercent"),
                            String.format("%.2f", totalnetamountforitem)
                        });
                    }

                }
            }

            int rowscount = table.getRowCount();

            grossweight += grosswt;
            netweight += netwt;
            totaltaxableamount += taxableamt;
            totalAmount += netamt;
            txtcount.setText(String.valueOf(rowscount));
            txtgrosswt.setText(String.format("%.2f", grossweight));
            txtnetwt.setText(String.format("%.2f", netweight));
            txttotal.setText(String.format("%.2f", totalAmount));
            txttaxable.setText(String.format("%.2f", totaltaxableamount));
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void PurchaseItemsDetailDialogSubmitClicked(List<List<String>> details) {
        FeildFilledRedirectDetails.add(details);
        double netwt = 0.0, diowt = 0.0;

        for (int i = 0; i < details.size(); i++) {
            netwt += Double.parseDouble(details.get(i).get(3));
            diowt += Double.parseDouble(details.get(i).get(4));
        }

        txtNetWt.setText(String.format("%.2f", netwt));
        txtDiamondWt.setText(String.format("%.2f", diowt));
        txtDiamondRate.requestFocus();
    }

    private void initPopups() {
        pmItemNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblItemNameSuggestionsContainer = new javax.swing.JScrollPane();
        itemNameDetailsContainer = new javax.swing.JScrollPane();
        itemNameDetailsTable = new javax.swing.JTable();
        tblItemNameSuggestions = new javax.swing.JTable();
        pmPartyNameSuggestionsPopup = new javax.swing.JPopupMenu();
        spTblPartyNameSuggestionsContainer = new javax.swing.JScrollPane();
        tblPartyNameSuggestions = new javax.swing.JTable();
        itemNameDetailsPopup = new javax.swing.JPopupMenu();
        tagNoPopup = new javax.swing.JPopupMenu();
        tagnoTable = new javax.swing.JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);

                // Set alternating row colors
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        component.setBackground(Color.ORANGE);  // Color for even rows
                    } else {
                        component.setBackground(Color.WHITE);  // Color for odd rows
                    }
                } else {
                    component.setBackground(getSelectionBackground());  // Keep default color for selected rows
                }

                return component;
            }
        };
        tagnoTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tagnoContainer = new javax.swing.JScrollPane();
        
        pmItemNameSuggestionsPopup.setMinimumSize(new java.awt.Dimension(200, 200));
        itemNameDetailsPopup.setMinimumSize(new java.awt.Dimension(200, 200));
        tagNoPopup.setMinimumSize(new java.awt.Dimension(200, 200));
        
        spTblItemNameSuggestionsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsContainerFocusGained(evt);
            }
        });
        itemNameDetailsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemNameDetailsContainerFocusGained(evt);
            }
        });
        tagnoContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tagnoContainerFocusGained(evt);
            }
        });

        itemNameDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
               
                new Object[][]{},
                new String[]{
                    "Item TagNo.", "Item Name", "Item Group", "G.W", "B.W"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tblItemNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Item Name", "Item Group"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        
        tagnoTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Tag No.", "Item Group", "G.W", "B.W", "Huid"

                }
        ) {
            boolean[] canEdit = new boolean[]{
                false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tblItemNameSuggestions.setOpaque(false);
        itemNameDetailsTable.setOpaque(false);
        tagnoTable.setOpaque(false);

        tblItemNameSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemNameSuggestionsMouseClicked(evt);
            }
        });
        itemNameDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemNameDetailsTableMouseClicked(evt);
            }
        });
        tagnoTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemNameDetailsTableMouseClicked(evt);
            }
        });
        spTblItemNameSuggestionsContainer.setViewportView(tblItemNameSuggestions);
        itemNameDetailsContainer.setViewportView(itemNameDetailsTable);
        tagnoContainer.setViewportView(tagnoTable);

        tblPartyNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Party Name", "State", "GRP"
                }
        ) {
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

    }

    public void fillgrandtotal() {
        double netwt = 0.0, grosswt = 0.0, netAmt = 0, BasicAmt = 0, taxAmt = 0, billAmt = 0, txtqty = 0, lbrAmt = 0;
        txtcount.setText(Integer.toString(purchasesListTableModel.getRowCount()));

        for (int i = 0; i < purchasesListTableModel.getRowCount(); i++) {
            netAmt += Double.parseDouble(purchasesListTableModel.getValueAt(i, 8).toString());
            grosswt += Double.parseDouble(purchasesListTableModel.getValueAt(i, 3).toString());
            BasicAmt += Double.parseDouble(purchasesListTableModel.getValueAt(i, 6).toString());
            netwt += Double.parseDouble(purchasesListTableModel.getValueAt(i, 4).toString());

        }

        txtnetwt.setText(Double.toString(netwt));
        txtgrosswt.setText(Double.toString(grosswt));
        txttaxable.setText(Double.toString(BasicAmt));
        txttotal.setText(Double.toString(netAmt));

    }

    public static int getPartyBillNo(String partyName) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> partyBillNo = DBController.executeQuery("SELECT bill FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE partyname = "
                + "'" + partyName + "'");

        if (partyBillNo != null && partyBillNo.size() > 0) {
            return Integer.parseInt(partyBillNo.get(0).toString());
        }

        return 0;
    }

    private boolean partyPurchaseExists(String partyName, String date) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> partyPurchases = DBController.executeQuery("SELECT id FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE partyName = "
                + "'" + partyName + "' AND date = " + "'" + date + "'");

        return partyPurchases != null && partyPurchases.size() > 0;
    }

    private int getBillNo() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> billNo = DBController.executeQuery("SELECT billno FROM "
                + DatabaseCredentials.BILL_NO_COUNTER_TABLE);

        return (billNo.get(0) != null) ? Integer.parseInt(billNo.get(0).toString()) : null;
    }

    private int getPurchaseBillNo(String gst) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> gstNo = DBController.executeQuery("SELECT bill FROM "
                + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE gst = " + gst);

        return (gstNo.get(0) != null) ? Integer.valueOf(gstNo.get(0).toString()) : null;
    }

    private void populateSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
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

    private void populateitemSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        suggestionsTable.setRowCount(0);

        suggestions.forEach((suggestion) -> {
            suggestionsTable.addRow(new Object[]{
                (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                (suggestion.get(1) == null) ? "NULL" : suggestion.get(1)
            });
        });

    }

    private void populatetagnoTableFromDatabase(DefaultTableModel tagTable, String query) {

        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<List<Object>> suggestions = DBController.getDataFromTable(query);

        logger.info(String.valueOf(suggestions.size()));
        int[] i = {0};
        tagTable.setRowCount(0);
        suggestions.forEach((suggestion) -> {
            if("N.A".equals(suggestion.get(0))){
                tagTable.addRow(new Object[]{
                    (suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
                    (suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
                    (suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
                    (suggestion.get(3) == null) ? "NULL" : suggestion.get(3),
                    (suggestion.get(4) == null) ? "NULL" : suggestion.get(4),
                });
                i[0] = 1;
            }
        });
        
        
        
        if (i[0] == 1) {
            tagTable.insertRow(1, new Object[]{
            "New tag",
            itemgrp,
            "0.0",
            "0.0",
            "",
            });
        }else{
            tagTable.insertRow(0, new Object[]{
             "New tag",
             itemgrp,
             "0.0",
             "0.0",
             "",
            });
        }
         
        tagnoTable.requestFocus();
                
        tagnoTable.setRowSelectionInterval(0, 0);
        selectedrow++;
                   
        
        
        
        
        
        

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
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void setImageOnJLabel(javax.swing.JLabel component,
            String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth() - 5,
                        component.getHeight() - 5, Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    private void setItemRate(String itemName) {
        try {
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            List<Object> itemGroup = DBController.executeQuery("SELECT itemgroup,taxslab FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + itemName + "'");
//        logger.log(Level.SEVERE,null,itemName);
//              logger.log(Level.SEVERE,null,itemGroup.get(1).toString());
//              JOptionPane.showMessageDialog(this, itemName);
            txtGSTPercent.setText(itemGroup.get(1).toString());
            if (itemGroup != null && (!itemGroup.isEmpty())) {

                List<Object> itemRate = null;

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
            logger.log(Level.SEVERE, null, e);
        }
    }

    private boolean fieldsAreValidated() {
        if (txtItemName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the item name correctly");
            return false;
        }

        return true;
    }

    private void emptyTextFields() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
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

    private void clearLabels() {
        lblGST.setText("");
        lblPreviousBalance.setText("");
        lblState.setText("");
    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) table
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

    private void populatePurchasesListTable() {
        purchasesListTableModel.setRowCount(0);

        List<List<Object>> purchaseItems;

        if (DBController.isDatabaseConnected()) {
//            purchaseItems = DBController.getDataFromTable("SELECT id,"
//                + "itemname, huid, grosswt, netwt, qty, taxable_amt, gst_percent, net_amount FROM " 
//                + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE date = " 
//                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") +"'" + " AND bill= "+"'" + jTextField1.getText() + "'");

            purchaseItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, grosswt, netwt, qty, taxable_amt, gst_percent, net_amount FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE date = "
                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'" + " AND bill= " + "'" + jTextField1.getText() + "'");

            purchaseItems.forEach((item) -> {
                purchasesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // itemname
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // grosswt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // netwt
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // qty
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableAmount
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gst
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // netamount
                });
            });
        } else {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);

//            purchaseItems = DBController.getDataFromTable("SELECT id,"
//                + "itemname, huid, grosswt, netwt, qty, taxable_amt, gst_percent, net_amount FROM " 
//                + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE date = " 
//                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'"+ " AND bill= "+"'" + jTextField1.getText() + "'");
            purchaseItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, grosswt, netwt, qty, taxable_amt, gst_percent, net_amount FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE date = "
                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'" + " AND bill= " + "'" + jTextField1.getText() + "'");

            purchaseItems.forEach((item) -> {
                purchasesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // itemname
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // grosswt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // netwt
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // qty
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableAmount
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gst
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // netamount
                });
            });
        }
        fillgrandtotal();
    }

    private double calculateNetAmount(double taxableAmount, float gstPercent) {
        return taxableAmount + (taxableAmount
                * (UtilityMethods.numberToDecimalPoint(gstPercent)));
    }

    private double calculateBasicAmount(double netWeight, double rate, double diamondWeight,
            double diamondRate, double extraCharge) {
        return netWeight * rate + diamondWeight * diamondRate + extraCharge;
    }

    private double calculateBasicAmount(double netWeight, double rate, double extraCharge) {
        return netWeight * rate + extraCharge;
    }

    private double calculateTaxableAmount(double basicAmount, double discountAmount) {
        return basicAmount - discountAmount;
    }

    private void setPartyDetails(String partyName) {
        try {
            if (!DBController.isDatabaseConnected()) {
                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            }

            partyGSTAndBalance = DBController.executeQuery("SELECT gstno, opbal, state FROM "
                    + DatabaseCredentials.ACCOUNT_TABLE + " WHERE accountname = " + "'" + partyName + "'");

            if (partyGSTAndBalance.get(0) != null) {
                lblGST.setText(partyGSTAndBalance.get(0).toString());
            } else {
                lblGST.setText("Not Specified");
            }

            if (partyGSTAndBalance.get(1) != null) {
                String balance = outstandingAnalysisHelper.fillTableInDateGivenParty(partyName);
                if (Double.parseDouble(balance) >= 0) //if (Integer.parseInt(balance) >= 0 || Double.parseDouble(balance) >= 0)     
                {
                    lblPreviousBalance.setText(balance + " Dr");
                } else {
                    lblPreviousBalance.setText(balance + " Cr");
                }

            } else {
                lblPreviousBalance.setText("0.0");
            }

            if (partyGSTAndBalance.get(2) != null) {
                lblState.setText(partyGSTAndBalance.get(2).toString());
            } else {
                lblState.setText("---");
            }
        } catch (Exception e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void setTotalNetWtAndDiamondWt(String totalNetWt, String totalDiamondWt) {
        txtNetWt.setText(totalNetWt);
        txtDiamondWt.setText(totalDiamondWt);
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
        itemNameDetailsPopup = new javax.swing.JPopupMenu();
        tagnoPopup = new javax.swing.JPopupMenu();
        tagNoPopup = new javax.swing.JPopupMenu();
        pnlRootContainer = new javax.swing.JPanel();
        txtItemName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        txtNetWt = new javax.swing.JTextField();
        txtDiamondWt = new javax.swing.JTextField();
        txtDiamondRate = new javax.swing.JTextField();
        txtItemDescription = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtRate = new javax.swing.JTextField();
        txtExtraCharge = new javax.swing.JTextField();
        txtBasicAmt = new javax.swing.JTextField();
        txtDiscountPercent = new javax.swing.JTextField();
        txtDiscountAmt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel55 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
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
        jLabel10 = new javax.swing.JLabel();
        txtNetAmt = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        lblPreviousBalance = new javax.swing.JLabel();
        lblGST = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtcount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtgrosswt = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtnetwt = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txttaxable = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        pmItemNameSuggestionsPopup.setMinimumSize(new java.awt.Dimension(200, 200));
        pmItemNameSuggestionsPopup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pmItemNameSuggestionsPopupFocusLost(evt);
            }
        });

        spTblItemNameSuggestionsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsContainerFocusGained(evt);
            }
        });

        tblItemNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TagNo.", "Item Name", "Item Group", "G.W", "B.W"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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

        tagnoPopup.setBackground(new java.awt.Color(0, 51, 51));
        tagnoPopup.setPreferredSize(new java.awt.Dimension(200, 200));
        tagnoPopup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tagnoContainerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tagnoPopupFocusLost(evt);
            }
        });
        tagnoPopup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tagnoTablemouseClicked(evt);
            }
        });
        tagnoPopup.getAccessibleContext().setAccessibleName("");

        tagNoPopup.setBackground(new java.awt.Color(255, 204, 0));
        tagNoPopup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 204, 0), new java.awt.Color(255, 255, 255), null, null));
        tagNoPopup.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tagNoPopup.setPopupSize(new java.awt.Dimension(200, 200));
        tagNoPopup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tagNoPopupfocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tagNoPopupfocusLost(evt);
            }
        });
        tagNoPopup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tagNoPopupmouseClicked(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        pnlRootContainer.setBackground(new java.awt.Color(57, 68, 76));
        pnlRootContainer.setForeground(new java.awt.Color(189, 150, 117));

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("<html>Item Name<font color=\"red\">*</font></html>");

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

        txtDiamondRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiamondRateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondRateFocusLost(evt);
            }
        });
        txtDiamondRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondRateKeyReleased(evt);
            }
        });

        txtItemDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemDescriptionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemDescriptionFocusLost(evt);
            }
        });
        txtItemDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemDescriptionKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 188, 81));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("<html>Qty.<font color=\"red\">*</font></html>");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" Diamond Wt.");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Diamond Rate / Carat");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 188, 81));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Item Descriptions");

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(238, 188, 81));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Rate");

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(238, 188, 81));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Extra charge");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(238, 188, 81));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Basic Amt.");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(238, 188, 81));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Dis [%]");

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(238, 188, 81));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Dis Amt");

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

        txtDiscountPercent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountPercentFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountPercentFocusLost(evt);
            }
        });
        txtDiscountPercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountPercentKeyReleased(evt);
            }
        });

        txtDiscountAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountAmtFocusLost(evt);
            }
        });
        txtDiscountAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountAmtKeyReleased(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "HUID", "Gross Wt.", "Net Wt.", "Qty.", "Taxable Amt.", "GST [%]", "Net Amt."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(36);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jLabel55.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(238, 188, 81));
        jLabel55.setText("<html>Party Name<font color=\"red\">*</font></html>");

        txtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyNameFocusLost(evt);
            }
        });
        txtPartyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyNameKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Bill No.");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("GST No.");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Previous Balance");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("PURCHASE");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setText("<html>DATE<font color=\"red\">*</font></html>");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("<html>TERMS<font color=\"red\">*</font></html>");

        cmbTerms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit" }));
        cmbTerms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTermsActionPerformed(evt);
            }
        });
        cmbTerms.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbTermsKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Taxable Amt");

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
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 188, 81));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("GST [%]");

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

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Net Amt");

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

        jLabel56.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(238, 188, 81));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("State");

        lblPreviousBalance.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPreviousBalance.setForeground(new java.awt.Color(255, 255, 255));
        lblPreviousBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblGST.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblGST.setForeground(new java.awt.Color(255, 255, 255));

        lblState.setForeground(new java.awt.Color(255, 255, 255));
        lblState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblState.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 188, 81));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Net Wt.");

        jButton1.setBackground(new java.awt.Color(0, 255, 0));
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setText("Count:");

        txtcount.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setText("Gross Wt.");

        txtgrosswt.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 188, 81));
        jLabel24.setText("Net Wt.");

        txtnetwt.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 188, 81));
        jLabel21.setText("Taxable Amt.");

        txttaxable.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(238, 188, 81));
        jLabel25.setText("Total Amt.");

        txttotal.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setText("Close");
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

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 0, 0));
        jButton4.setText("Delete");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setText("Print");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setText("Refresh");
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

        javax.swing.GroupLayout pnlRootContainerLayout = new javax.swing.GroupLayout(pnlRootContainer);
        pnlRootContainer.setLayout(pnlRootContainerLayout);
        pnlRootContainerLayout.setHorizontalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3))
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(843, 843, 843))
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(110, 110, 110)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                                .addComponent(txtNetWt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtDiamondWt, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(txtDiamondRate, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(txtItemDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel22)))
                                        .addGap(348, 348, 348))))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtRate, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtExtraCharge)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBasicAmt)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDiscountPercent)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDiscountAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTaxableAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGSTPercent)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNetAmt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPreviousBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblGST, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(420, 420, 420))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGap(556, 556, 556)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(datePurchaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbTerms, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(252, 252, 252))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(jLabel56)
                                .addGap(18, 18, 18)
                                .addComponent(lblState, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtcount, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtgrosswt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnetwt, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttaxable, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlRootContainerLayout.setVerticalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlRootContainerLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(2, 2, 2)
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbTerms)
                                .addComponent(datePurchaseDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 25, Short.MAX_VALUE)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblGST, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPreviousBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(lblState, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(29, 29, 29)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiamondRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiamondWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNetWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel23)
                                .addComponent(jLabel18)
                                .addComponent(jLabel19)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel34))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtBasicAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiscountPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiscountAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtExtraCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel30)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTaxableAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGSTPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNetAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtgrosswt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnetwt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttaxable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlRootContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void getHuid(String name, String itemgrp) {
        String huid = null;
        String grosswt = null;
        String beedswt = null;
        String netwt = "0";
        String diamondwt = null;
        String carat = null;
        String gst = null;
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> itemGroup = DBController.executeQuery("SELECT huid,grosswt,beedswt,netwt,diamondwt,carats,taxslab FROM "
                + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + name + "' AND itemgroup = '" + itemgrp + "'");
        if (itemGroup != null && (!itemGroup.isEmpty())) {

//            List<Object> itemRate = null;
            if (itemGroup.get(0) != null) {
                huid = itemGroup.get(0).toString();
            }
            if (itemGroup.get(1) != null) {
                grosswt = itemGroup.get(1).toString();
            }
            if (itemGroup.get(2) != null) {
                beedswt = itemGroup.get(2).toString();
            }
            if (itemGroup.get(3) != null) {
                netwt = itemGroup.get(3).toString();
            }
            if (itemGroup.get(4) != null) {
                diamondwt = itemGroup.get(4).toString();
            }
            if (itemGroup.get(5) != null) {
                carat = itemGroup.get(5).toString();
            }
            if (itemGroup.get(6) != null) {
                gst = itemGroup.get(6).toString();
            }
        }
//        txthuid.setText(huid);
//        txtGrossWt.setText(grosswt);
//        txtBeedsWt.setText(beedswt);
//        txtNetWt.setText(netwt);
//        txtDiamondWt.setText(diamondwt);
        txtDiamondRate.setText(carat);
        txtGSTPercent.setText(gst.replaceAll("%", ""));
    }

    double getTotal(int x) {
        double amt = 0.0;
        try {
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            String query1 = "select net_amount from purchasehistory where bill = '" + x + "' and date = '" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "';";

            ResultSet rs = s.executeQuery(query1);
            while (rs.next()) {
                amt = amt + rs.getDouble("net_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return amt;
    }

    private void printjrxml() {

        connect = DBConnect.connect();
        InputStream jasper1 = null;
        String u = jTextField1.getText();
        String user = System.getProperty("user.dir") + "/src/jasper_reports/Bill.jrxml";

        File f = new File(user);

        try {

            jasper1 = new FileInputStream(f);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        String partyName = txtPartyName.getText();
        int x = getPartyBillNo(partyName);
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(jasper1);

            String query1 = "select itemname,netwt,bankamt,taxable_amt,net_amount from purchasehistory where bill = '" + x + "' and date = '" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "';";
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(query1);
            jasperDesign.setQuery(newQuery);

            Map<String, Object> hm = new HashMap<>();
            System.out.print(u);

            hm.put("customer_name", txtPartyName.getText());
            hm.put("bill_no", x);
            hm.put("state", lblState.getText());
            hm.put("total", getTotal(x));
            hm.put("date", dateFormat.format(datePurchaseDate.getDate()));
            File file1 = null;
            String icon = System.getProperty("user.dir");
            file1 = new File(icon);
            hm.put("path", file1.getPath());

//        File file1 = null;
//        String icon=System.getProperty("user.dir");
//        file1 = new File(icon);
//        hm.put("path",file1.getPath());
//         
//        try{
//             int rid=0;
//       Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/patho","root","");
//       Statement stmt = con.createStatement();
//      
//       ResultSet rs11=stmt.executeQuery("Select serialno from report_table");
//       while(rs11.next()){
//           rid=rs11.getInt("serialno");
//       }
//       rid=rid+1;
//       
//       String name=u+"_"+".pdf";
//       report_id="tr_"+Integer.toString(rid);
//       String query2 = "INSERT INTO report_table(pat_id,local_location,uploaded,report_id) values('"+u+"','"+name+"','"+"no"+"','"+report_id+"');";
//       stmt.executeUpdate(query2);   
//   }     catch (SQLException ex) {
//             Logger.getLogger(testreportsupport.class.getName()).log(Level.SEVERE, null, ex);
//         }
//        
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, connect);
            reportsavepath = System.getProperty("user.dir") + "/Bills/" + u + ".pdf";

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportsavepath);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void spTblItemNameSuggestionsContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_spTblItemNameSuggestionsContainerFocusGained
        JOptionPane.showMessageDialog(this, "Item name suggestions table 'focus gained'");
    }//GEN-LAST:event_spTblItemNameSuggestionsContainerFocusGained

	 private void itemNameDetailsContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemNameDetailsContainerFocusGained
             JOptionPane.showMessageDialog(this, "Item name suggestions table 'focus gained'");
    }//GEN-LAST:event_itemNameDetailsContainerFocusGained

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        if (txtItemName.isFocusOwner()) {
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                JOptionPane.showMessageDialog(this, "Down Key Was pressed");
            }
        }
    }//GEN-LAST:event_formKeyReleased

    private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            setPartyDetails(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                    .getSelectedRow(), 0).toString());
            pmPartyNameSuggestionsPopup.setVisible(false);
        }
    }//GEN-LAST:event_tblPartyNameSuggestionsMouseClicked

    private void itemNameDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemNameDetailsTableMouseClicked
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
            try {
                txtItemName.setText(itemNameDetailsTableModel.getValueAt(itemNameDetailsTable
                        .getSelectedRow(), 0).toString());
                setItemRate(itemNameDetailsTableModel.getValueAt(itemNameDetailsTable
                        .getSelectedRow(), 0).toString());
                getHuid(itemNameDetailsTableModel.getValueAt(itemNameDetailsTable
                        .getSelectedRow(), 0).toString(),
                        itemNameDetailsTableModel.getValueAt(itemNameDetailsTable
                        .getSelectedRow(), 1).toString());
                itemNameDetailsPopup.setVisible(false);
                JOptionPane.showMessageDialog(this, "running");
                txtQty.requestFocusInWindow();
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }

        }
    }//GEN-LAST:event_itemNameDetailsTableMouseClicked

    private void fillitemNameDetailsTable(String itemName) {
        try {
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT tagno,itemname, itemgroup,grosswt,beedswt  FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                    + " WHERE itemname = '" + itemName + "';");
            itemNameDetailsTableModel.setRowCount(0);
            while (rs.next()) {
                itemNameDetailsTableModel.addRow(new Object[]{
                    rs.getString("tagno"),
                    itemName,
                    rs.getString("itemgroup"),
                    String.format("%.2f", rs.getDouble("grosswt")),
                    String.format("%.2f", rs.getDouble("beedswt"))
                });
            }
            c.close();
            s.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
        }
    }

	private void tblItemNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemNameSuggestionsMouseClicked
            if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {

                int curselectedRow = tblItemNameSuggestions.getSelectedRow();
                String itemName = tblItemNameSuggestions.getValueAt(curselectedRow, 0).toString();
                txtItemName.setText(itemName);
//            itemNameDetailsContainer.requestFocusInWindow();
                pmItemNameSuggestionsPopup.setVisible(false);
                txtQty.requestFocus();
//                itemNameDetailsPopup.setVisible(true);
//                fillitemNameDetailsTable(itemName);

                txtItemName.setText(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                        .getSelectedRow(), 0).toString());
                setItemRate(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                        .getSelectedRow(), 0).toString());
                getHuid(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                        .getSelectedRow(), 0).toString(),
                        itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                                .getSelectedRow(), 1).toString());
//            txtGSTPercent.setText(itemNameSuggestionsTableModel
//                    .getValueAt(tblItemNameSuggestions.getSelectedRow(), 2).toString().replaceAll("%", ""));
            }
    }//GEN-LAST:event_tblItemNameSuggestionsMouseClicked

    private void tagnoTableMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {

            int curselectedRow = tagnoTable.getSelectedRow();

        }
    }

    private void verifyNetWeightCalculationFields() {
        double grossWt;
        double beedsWt;

//        if (txtGrossWt.getText().trim().isEmpty() && txtBeedsWt.getText().trim().isEmpty()) {
//            txtGrossWt.setText("1");
//            txtBeedsWt.setText("0");
//
//            grossWt = Double.valueOf(txtGrossWt.getText().trim());
//            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());
//
//            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
//
//        } else if (txtGrossWt.getText().trim().isEmpty()) {
//            txtGrossWt.setText("1");
//
//            grossWt = Double.valueOf(txtGrossWt.getText().trim());
//            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());
//
//            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
//        } else if (txtBeedsWt.getText().trim().isEmpty()) {
//            txtBeedsWt.setText("0");
//
//            grossWt = Double.valueOf(txtGrossWt.getText().trim());
//            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());
//
//            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
//        } else {
//            grossWt = Double.valueOf(txtGrossWt.getText().trim());
//            beedsWt = Double.valueOf(txtBeedsWt.getText().trim());
//
//            txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
//        }
    }

    private void clearTextFieldsEnter() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                if (component == txtPartyName) {
                    continue;
                }
                if (component == jTextField1) {
                    continue;
                }

                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }

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

    private void saveTableData() throws FileNotFoundException {
        /*
        * Saving data into purchasehistory table from tblPurchasedList
         */
        if (lblState.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Re select Party Name !!!! State Can't Be Blank");
        } else {
            try {
//                JOptionPane.showMessageDialog(this, "size of field " + FeildFilledRedirectDetails.size());

                for (int j = 0; j < FeildFilledRedirectDetails.size(); j++) {

                    Map<String, Object> currentBill = currentBillItems.get(j);
                    List<List<String>> RedirectedDetails = FeildFilledRedirectDetails.get(j);

                    for (int i = 0; i < RedirectedDetails.size(); i++) {
                        columnNames.clear();
                        data.clear();
                        if (DBController.isDatabaseConnected()) {
                            columnNames = DBController.getTableColumnNames(DatabaseCredentials.PURCHASE_HISTORY_TABLE);

                            // Remove the id as it is auto generated
                            columnNames.remove(0);
                        } else {
                            DBController
                                    .connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                                            DatabaseCredentials.DB_USERNAME,
                                            DatabaseCredentials.DB_PASSWORD);
                            columnNames = DBController.getTableColumnNames(DatabaseCredentials.PURCHASE_HISTORY_TABLE);

                            // Remove the id as it is auto generated
                            columnNames.remove(0);
                        }

                        double netwtcontribution = Double.parseDouble(RedirectedDetails.get(i).get(3));
                        double diawtcontribution = Double.parseDouble(RedirectedDetails.get(i).get(4));
                        double totalnetamountfromdiamond = Double.parseDouble(currentBill.get("diamondrate").toString()) * diawtcontribution;
                        double totalnetamountfromnetwt = Double.parseDouble(currentBill.get("rate").toString()) * netwtcontribution;
                        double gstpercent = Double.parseDouble(currentBill.get("gstpercent").toString());

                        double extracharge = Double.parseDouble(currentBill.get("extracharge").toString())
                                - Double.parseDouble(currentBill.get("discountamount").toString());

                        double additional = (extracharge / Double.parseDouble(currentBill.get("qty").toString()));
                        double totaltaxableamountforitem = additional + totalnetamountfromdiamond + totalnetamountfromnetwt;

                        double totalnetamountforitem = totaltaxableamountforitem + (gstpercent / 100) * totaltaxableamountforitem;

//                double netwt = Double.parseDouble(txtnetwt.getText()), perwt = Double.parseDouble(RedirectedDetails.get(i).get(3));
//                double netamount = (Double.parseDouble(currentBill.get("netamount").toString()) / netwt) * perwt;
                        data.add(currentBill.get("date"));
                        data.add(currentBill.get("cmbterms"));
//                        if(currentBill.get("cmbterms").toString().equalsIgnoreCase("credit"))
//                        {
//                           JOptionPane.showMessageDialog(this, "final amount =>"+totalnetamountforitem);
//                           totalnetamountforitem=-totalnetamountforitem; 
//                        }
                        data.add(currentBill.get("partyname"));
                        data.add(currentBill.get("bill"));
                        data.add(currentBill.get("gst"));
                        data.add(currentBill.get("previousbalance"));
                        data.add(currentBill.get("itemname"));
                        if (!RealSettingsHelper.gettagNoIsTrue()) {
                            data.add(currentBill.get("qty"));
                        } else {
                            data.add(1);
                        }

                        data.add(RedirectedDetails.get(i).get(2)); //beedwt
                        data.add(RedirectedDetails.get(i).get(3)); //netwt
                        data.add(RedirectedDetails.get(i).get(4)); //dimondwt
                        data.add(currentBill.get("diamondrate"));
                        data.add(RedirectedDetails.get(i).get(1)); //grosswt
                        if (currentBill.containsKey("description")) {
                            data.add(currentBill.get("description"));
                        } else {
                            data.add("");
                        }
                        data.add(currentBill.get("rate"));
                        data.add(totaltaxableamountforitem);
                        data.add(currentBill.get("gstpercent"));
                        data.add(totalnetamountforitem);
                        data.add(currentBill.get("extracharge"));
                        data.add(currentBill.get("basicamount"));
                        data.add(currentBill.get("discountpercent"));
                        data.add(currentBill.get("discountamount"));
                        data.add((RedirectedDetails.get(i).get(0) == "0") ? "" : RedirectedDetails.get(i).get(0)); //huid
                        data.add(tagNoOfSelectedId);
                        if (!DBController.updateTableData(DatabaseCredentials.PURCHASE_HISTORY_TABLE,
                                data, columnNames, "id", id)) {

                            //adding tag no for updating item if they want
                            String tagno = null;
                            Connection con1 = DBConnect.connect();
                            Statement st1 = con1.createStatement();
                            ResultSet rs1 = st1.executeQuery("SELECT tag_counter"
                                    + " FROM " + DatabaseCredentials.TAG_COUNTER_TABLE + ";");
                            while (rs1.next()) {
                                tagno = rs1.getString("tag_counter");
                                tagno = String.valueOf((Integer.parseInt(tagno) + 1));
                            }
                            String currentitemname = currentBill.get("itemname").toString();
                            String newTagNo = currentitemname.substring(0, 2) + tagno;
                            data.remove(data.size() - 1);
                            data.add(newTagNo);

                            if (DBController.insertDataIntoTable(DatabaseCredentials.PURCHASE_HISTORY_TABLE,
                                    columnNames, data)) {
                                try {
                                    Connection con = DBConnect.connect();
                                    Statement st = con.createStatement();
                                    Long l = Long.parseLong(tagno);

                                    String q = "UPDATE `tagcounter` SET `tag_counter`=" + l + "";
                                    logger.info(q);
                                    st.executeUpdate(q);
                                    con.close();
                                    st.close();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(this, "purchase save error " + e);
                                }
                            }
                        }
                    }
                }

                double netmoney = Double.parseDouble(txttotal.getText());
                Connection con = DBConnect.connect();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select dueamt from account where accountname = '" + txtPartyName.getText() + "';");
                while (rs.next()) {
                    netmoney += rs.getDouble("dueamt");
                }
                st.clearBatch();
                st.execute("update account set dueamt = " + (-netmoney) + " where accountname = '" + txtPartyName.getText() + "';");
                con.close();
                st.close();
                rs.close();

            } catch (Exception e) {
                Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
            }

        }
    }

    private void txtNetAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetAmtKeyReleased
        // TODO add your handling code here:
// this section is for entering data into PurchaseItemsDetailDialog
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!RealSettingsHelper.gettagNoIsTrue()) {

                List<List<String>> netwt = new ArrayList<List<String>>();

                Double netwet = Double.parseDouble(txtNetWt.getText().trim());
                Double diamond = Double.parseDouble(txtDiamondWt.getText().trim());
                List<String> datas = new ArrayList<>();
                netwt.removeAll(netwt);
                datas.removeAll(datas);
                datas.add("0");
                datas.add(String.format("%.2f", (netwet)));
                datas.add("0");
                datas.add(String.format("%.2f", (netwet)));
                datas.add(String.format("%.2f", (diamond)));
                for (int i = 0; i < 1; i++) {
                    netwt.add(datas);
//                 JOptionPane.showMessageDialog(this,i+" num "+ Integer.parseInt(txtQty.getText()));
                }
//            JOptionPane.showMessageDialog(this, Integer.parseInt(txtQty.getText()));

                PurchaseItemsDetailDialog obj = new PurchaseItemsDetailDialog(this, true, Integer.parseInt(txtQty.getText()), netwt, "");
                obj.setVisible(false);
                netwt.removeAll(netwt);
                datas.removeAll(datas);
            }
//            section close

            columnNames.clear();
            data.clear();
            try {
                if (fieldsAreValidated()) {

                    Map<String, Object> currentBill = new HashMap<>();
                    for (int i = 0; i < Integer.parseInt(txtQty.getText()); i++) {
                        currentBill.put("itemdescription", txtItemDescription.getText()); // description
                        currentBill.put("date", datePurchaseDate.getDate()); // date
                        currentBill.put("cmbterms", cmbTerms.getSelectedItem().toString());
                        currentBill.put("partyname", txtPartyName.getText());
                        currentBill.put("bill", jTextField1.getText());
                        currentBill.put("totaldiamondweight", txtDiamondWt.getText());
                        currentBill.put("totalnetwt", txtNetWt.getText());
                        currentBill.put("itemname", txtItemName.getText());
                        currentBill.put("gst", lblGST.getText());
                        currentBill.put("discountamount", txtDiscountAmt.getText());
                        currentBill.put("discountpercent", txtDiscountPercent.getText());
                        currentBill.put("basicamount", txtBasicAmt.getText());
                        currentBill.put("extracharge", txtExtraCharge.getText());
                        currentBill.put("netamount", txtNetAmt.getText());
                        currentBill.put("gstpercent", txtGSTPercent.getText());
                        currentBill.put("taxableamount", txtTaxableAmt.getText());
                        currentBill.put("rate", txtRate.getText());
                        currentBill.put("diamondrate", txtDiamondRate.getText());
                        currentBill.put("qty", txtQty.getText());
                        currentBill.put("itemgroup", itemgrp);
                    }
                    currentBillItems.add(currentBill);
                    fillTableWithAllEntries();
                    clearnontotal();
                    txtItemName.requestFocus();

                }
            } catch (Exception e) {
                Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, e);
            }
        }

    }//GEN-LAST:event_txtNetAmtKeyReleased

    private void clearnontotal() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                if (component == txtPartyName) {
                    continue;
                }
                if (component == jTextField1) {
                    continue;
                }
                if (component == txtcount) {
                    continue;
                }
                if (component == txtgrosswt) {
                    continue;
                }
                if (component == txtnetwt) {
                    continue;
                }
                if (component == txttaxable) {
                    continue;
                }
                if (component == txttotal) {
                    continue;
                }

                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }

    }

    private void txtNetAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusGained
        txtNetAmt.setBackground(new Color(245, 230, 66));
        double taxableAmt;
        float gstPercent;

        if (!(txtGSTPercent.getText().trim().equals("0")
                || txtGSTPercent.getText().trim().isEmpty())) {

            taxableAmt = Double.parseDouble(txtTaxableAmt.getText().trim());
            gstPercent = Float.parseFloat(txtGSTPercent.getText().trim());

            txtNetAmt.setText(String.format("%.3f", calculateNetAmount(taxableAmt, gstPercent)));

        } else {
            txtNetAmt.setText(txtTaxableAmt.getText());
        }
    }//GEN-LAST:event_txtNetAmtFocusGained

    private void txtGSTPercentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPercentKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtNetAmt);
            setBg();
            txtNetAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtGSTPercentKeyReleased

    private void txtTaxableAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxableAmtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtGSTPercent);
            setBg();
            txtGSTPercent.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtTaxableAmtKeyReleased

    private void txtTaxableAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxableAmtFocusGained
        txtTaxableAmt.setBackground(new Color(245, 230, 66));
        if (!(txtDiscountAmt.getText().trim().isEmpty()
                || txtDiscountPercent.getText().trim().isEmpty()
                || txtDiscountAmt.getText().trim().equals("0")
                || txtDiscountPercent.getText().trim().equals("0"))) {

            double basicAmt = Double.parseDouble(txtBasicAmt.getText().trim());
            double discountAmt = Double.parseDouble(txtDiscountAmt.getText().trim());

            txtTaxableAmt.setText(String.valueOf(calculateTaxableAmount(basicAmt, discountAmt)));

        } else {
            txtTaxableAmt.setText(txtBasicAmt.getText());
        }
    }//GEN-LAST:event_txtTaxableAmtFocusGained

    private void cmbTermsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTermsKeyReleased
//        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            if (cmbTerms.getSelectedItem().equals("Cash")) {
//                fetchAccountNames("Cash");
//                new CashPurchaseDialog(this, true).setVisible(true);
//            } else if (cmbTerms.getSelectedItem().equals("Credit")) {
//                fetchAccountNames("Credit");
//            }
//        }

        setFocus(evt, txtPartyName);
    }//GEN-LAST:event_cmbTermsKeyReleased
    int selectedrow = 0;
    private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased
//        if (!(accountNames == null || accountNames.isEmpty())) {
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
                pmPartyNameSuggestionsPopup.setVisible(false);
                break;
            case KeyEvent.VK_ENTER:
                setPartyDetails(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
                        .getSelectedRow(), 0).toString());
                pmPartyNameSuggestionsPopup.setVisible(false);
                txtItemName.requestFocus();
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
                    pmPartyNameSuggestionsPopup.setVisible(true);
                    populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
                            + "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE
                            + " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'  and grp = 'Supplier';");
                });
                break;
        }
//        }
    }//GEN-LAST:event_txtPartyNameKeyReleased

    private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusLost
        txtPartyName.setBackground(Color.white);
        pmPartyNameSuggestionsPopup.setVisible(false);
//        int x = getPartyBillNo(txtPartyName.getText());
//        if (x == 0) {
//            x = getBillNo() + 1;
//        }
//        jTextField1.setText(Integer.toString(x));
    }//GEN-LAST:event_txtPartyNameFocusLost

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        try {
            // JOptionPane.showMessageDialog(this,table.getValueAt(table.getSelectedRow(), 0));

            if (!(table.getValueAt(table.getSelectedRow(), 0).toString().trim().equals(""))) {

                selectedRow = table.getSelectedRow();

                if (DBController.isDatabaseConnected()) {
                    emptyTextFields();

                    fieldsData = DBController.executeQuery("SELECT * FROM "
                            + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE id = "
                            + "'" + table.getValueAt(selectedRow, 0) + "'"
                    );

                    id = Integer.valueOf(fieldsData.remove(0).toString());

                    try {
                        datePurchaseDate.setDate(dateFormat.parse(fieldsData.get(0).toString()));
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }

                    cmbTerms.setSelectedItem(fieldsData.get(1).toString());
                    txtPartyName.setText(fieldsData.get(2).toString());
                    jTextField1.setText(fieldsData.get(3).toString());

                    if (fieldsData.get(4) != null) {
                        lblGST.setText(fieldsData.get(4).toString());
                    }
                    if (fieldsData.get(5) != null) {
                        String balance = outstandingAnalysisHelper.fillTableInDateGivenParty(txtPartyName.getText());
                        if (Double.parseDouble(balance) >= 0) {
                            lblPreviousBalance.setText(balance + " Dr");
                        } else {
                            lblPreviousBalance.setText(balance + " Cr");
                        }
                    }
                    txtItemName.setText(fieldsData.get(6).toString());

                    if (fieldsData.get(7) != null) {
                        txtQty.setText(fieldsData.get(7).toString());
                    }

                    //            if(fieldsData.get(8) != null) {
                    //                txtBeedsWt.setText(fieldsData.get(8).toString());
                    //            }
                    if (fieldsData.get(9) != null) {
                        txtNetWt.setText(fieldsData.get(9).toString());
                    }

                    if (fieldsData.get(10) != null) {
                        txtDiamondWt.setText(fieldsData.get(10).toString());
                    }

                    if (fieldsData.get(11) != null) {
                        txtDiamondRate.setText(fieldsData.get(11).toString());
                    }

                    //            if(fieldsData.get(12) != null) {
                    //                txtGrossWt.setText(fieldsData.get(12).toString());
                    //            }
                    if (fieldsData.get(13) != null) {
                        txtItemDescription.setText(fieldsData.get(13).toString());
                    }

                    if (fieldsData.get(14) != null) {
                        txtRate.setText(fieldsData.get(14).toString());
                    }

                    //            cmbPer.setSelectedItem(fieldsData.get(15).toString());
                    if (fieldsData.get(15) != null) {
                        txtTaxableAmt.setText(fieldsData.get(15).toString());
                    }

                    if (fieldsData.get(16) != null) {
                        txtGSTPercent.setText(fieldsData.get(16).toString());
                    }

                    if (fieldsData.get(17) != null) {
                        txtNetAmt.setText(fieldsData.get(17).toString());
                    }

                    if (fieldsData.get(18) != null) {
                        txtExtraCharge.setText(fieldsData.get(18).toString());
                    }

                    if (fieldsData.get(19) != null) {
                        txtBasicAmt.setText(fieldsData.get(19).toString());
                    }

                    if (fieldsData.get(20) != null) {
                        txtDiscountPercent.setText(fieldsData.get(20).toString());
                    }

                    if (fieldsData.get(21) != null) {
                        txtDiscountAmt.setText(fieldsData.get(21).toString());
                    }
//            if (fieldsData.get(22) != null) {
//                txthuid.setText(fieldsData.get(24).toString());
//            }

                    setPartyDetails(fieldsData.get(2).toString());
                    fillgrandtotal();
                }
                PurchaseItemsDetailDialog obj = new PurchaseItemsDetailDialog(this, true, Integer.parseInt(txtQty.getText()), itemData);
                obj.setVisible(true);
//            updateallthefieldsinarray();

//              dispose();
//            purchaseItemsDetailsDialog.setVisible(true);
//            PurchaseItemsDetailDialogSubmitClicked(itemData);
            }
        } catch (Exception e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
        }

    }//GEN-LAST:event_tableMouseClicked

    private void txtDiscountAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountAmtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtTaxableAmt);
            setBg();
            txtTaxableAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiscountAmtKeyReleased

    private void txtDiscountAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountAmtFocusLost
        txtDiscountAmt.setBackground(Color.white);
        if (txtDiscountAmt.getText().trim().isEmpty()) {
            txtDiscountAmt.setText("0");
        }
    }//GEN-LAST:event_txtDiscountAmtFocusLost

    private void txtDiscountPercentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountPercentKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiscountAmt);
            setBg();
            txtDiscountAmt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiscountPercentKeyReleased

    private void txtDiscountPercentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountPercentFocusLost
        txtDiscountPercent.setBackground(new Color(245, 230, 66));
        if (txtDiscountPercent.getText().trim().isEmpty()) {
            txtDiscountPercent.setText("0");
        } else {
            double dp = Double.parseDouble(txtDiscountPercent.getText().trim());
            double basic = Double.parseDouble(txtBasicAmt.getText().trim());
            double dis = (dp / 100.0) * basic;
            txtDiscountAmt.setText(Double.toString(dis));
        }
    }//GEN-LAST:event_txtDiscountPercentFocusLost

    private void txtBasicAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicAmtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiscountPercent);
            setBg();
            txtDiscountPercent.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtBasicAmtKeyReleased

    private void txtBasicAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusGained
        txtBasicAmt.setBackground(new Color(245, 230, 66));
        double netWt;
        double rate;
        double diamondWt;
        double diamondRate;
        double extraCharge;

        if (!(txtDiamondWt.getText().trim().equals("0")
                || txtDiamondRate.getText().trim().equals("0")
                || txtDiamondWt.getText().trim().isEmpty()
                || txtDiamondRate.getText().trim().isEmpty())) {

            netWt = Double.parseDouble(txtNetWt.getText().trim());
            rate = Double.parseDouble(txtRate.getText().trim());
            diamondWt = Double.parseDouble(txtDiamondWt.getText().trim());
            diamondRate = Double.parseDouble(txtDiamondRate.getText().trim());
            extraCharge = Double.parseDouble(txtExtraCharge.getText().trim());

            txtBasicAmt.setText(String.valueOf(calculateBasicAmount(netWt, rate,
                    diamondWt, diamondRate, extraCharge)));

        } else if (!(txtExtraCharge.getText().trim().equals("0")
                || txtExtraCharge.getText().trim().isEmpty())) {

            netWt = Double.parseDouble(txtNetWt.getText().trim());
            rate = Double.parseDouble(txtRate.getText().trim());
            extraCharge = Double.parseDouble(txtExtraCharge.getText().trim());

            txtBasicAmt.setText(String.valueOf(calculateBasicAmount(netWt, rate, extraCharge)));

        } else {
            netWt = Double.parseDouble(txtNetWt.getText().trim());
            rate = Double.parseDouble(txtRate.getText().trim());

            txtBasicAmt.setText(String.valueOf(calculateBasicAmount(netWt, rate, 0)));
        }
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
            setFocus(evt, txtExtraCharge);
            setBg();
            txtExtraCharge.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtRateKeyReleased

    private void txtDiamondRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondRateKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtItemDescription);
            setBg();
            txtItemDescription.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiamondRateKeyReleased

    private void txtDiamondRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateFocusLost
        txtDiamondRate.setBackground(Color.white);
        if (txtDiamondRate.getText().trim().isEmpty()) {
            txtDiamondRate.setText("0");
        }
    }//GEN-LAST:event_txtDiamondRateFocusLost

    private void InsertTableHidingFields(String value) {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "UPDATE hidingFields set tagno='" + value + "'";
            int r = stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased

        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
                tagNoPopup.setVisible(false);

                break;
            case KeyEvent.VK_DOWN:
                tagnoTable.requestFocus();
                if (selectedrow == 0) {
                    tagnoTable.setRowSelectionInterval(0, 0);
                    selectedrow++;
                } else {
                    if (tagnoTable.getSelectedRow() < tagnoTable.getRowCount() - 1) {
                        tagnoTable.setRowSelectionInterval(tagnoTable.getSelectedRow() + 1, tagnoTable.getSelectedRow() + 1);
                    }
                }

                break;
            case KeyEvent.VK_UP:
                tagnoTable.requestFocus();

                if (tagnoTable.getSelectedRow() > 0) {
                    tagnoTable.setRowSelectionInterval(tagnoTable.getSelectedRow() - 1, tagnoTable.getSelectedRow() - 1);
                }

                break;

            case KeyEvent.VK_ENTER:
                tagNoPopup.setVisible(false);

                EventQueue.invokeLater(() -> {
                    if (!txtQty.getText().trim().isEmpty()) {
                        int curselectedRow = tagnoTable.getSelectedRow();
                        String tagselected = tagnoTable.getValueAt(curselectedRow, 0).toString();
                        if ("N.A".equals(tagselected)) {
                            txtNetWt.requestFocus();
                            InsertTableHidingFields("false");
                        } else {

                            PurchaseItemsDetailDialog obj = new PurchaseItemsDetailDialog(this, true, Integer.parseInt(txtQty.getText()));
                            obj.setVisible(true);
                            InsertTableHidingFields("true");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Quantity cannot be empty");
                        setBg();
                        txtQty.requestFocusInWindow();
                        txtQty.setBackground(Color.LIGHT_GRAY);
                    }
                });

                break;
            default:
                EventQueue.invokeLater(() -> {
                    tagNoPopup.setVisible(true);
                    String itemname = txtItemName.getText();
                    populatetagnoTableFromDatabase(tagnoTableModel, "SELECT tagno, itemgroup, grosswt,beedswt, huid FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE (itemname = '" + itemname + "' AND item_sold = 0AND itemgroup = '" + itemgrp + "')"
                            + " OR (tagno = 'N.A' AND itemname = '" + itemname + "' AND itemgroup = '" + itemgrp + "');");
                });
                break;
        }
    }//GEN-LAST:event_txtQtyKeyReleased

    
    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased
        switch (evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_BACK_SPACE:
                itemNameDetailsPopup.setVisible(false);
                pmItemNameSuggestionsPopup.setVisible(false);
                break;
            case KeyEvent.VK_DOWN:
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

                break;
            case KeyEvent.VK_UP:
                tblItemNameSuggestions.requestFocus();

                if (tblItemNameSuggestions.getSelectedRow() > 0) {
                    tblItemNameSuggestions.setRowSelectionInterval(tblItemNameSuggestions.getSelectedRow() - 1, tblItemNameSuggestions.getSelectedRow() - 1);
                }

                txtItemName.setText(tblItemNameSuggestions.getValueAt(tblItemNameSuggestions.getSelectedRow(), 0).toString().trim());

                break;

            case KeyEvent.VK_ENTER:
                itemNameDetailsPopup.setVisible(false);
                txtItemName.setText(txtItemName.getText());

                EventQueue.invokeLater(() -> {
                    if (!txtItemName.getText().trim().isEmpty()) {
                        int curselectedRow = tblItemNameSuggestions.getSelectedRow();
                        String itemName = tblItemNameSuggestions.getValueAt(curselectedRow, 0).toString();
                        txtItemName.setText(itemName);
                        itemgrp = tblItemNameSuggestions.getValueAt(curselectedRow, 1).toString();
//            itemNameDetailsContainer.requestFocusInWindow();
                        pmItemNameSuggestionsPopup.setVisible(false);
                        txtQty.requestFocus();
//                itemNameDetailsPopup.setVisible(true);
//                fillitemNameDetailsTable(itemName);

                        txtItemName.setText(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                                .getSelectedRow(), 0).toString());
                        setItemRate(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                                .getSelectedRow(), 0).toString());
                        getHuid(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                                .getSelectedRow(), 0).toString(),
                                itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
                                .getSelectedRow(), 1).toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Item name cannot be empty");
                        setBg();
                        txtItemName.requestFocusInWindow();
                        txtItemName.setBackground(Color.LIGHT_GRAY);
                    }
                });

                break;
            default:
                EventQueue.invokeLater(() -> {
                    pmItemNameSuggestionsPopup.setVisible(true);
                    populateitemSuggestionsTableFromDatabase(itemNameSuggestionsTableModel, "SELECT DISTINCT(itemname), "
                            + "itemgroup FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE
                            + " WHERE itemname LIKE " + "'" + txtItemName.getText() + "%'");
                });
                break;
        }
    }//GEN-LAST:event_txtItemNameKeyReleased


    private void txtItemNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusLost
        txtItemName.setBackground(Color.white);
        pmItemNameSuggestionsPopup.setVisible(false);
        itemNameDetailsPopup.setVisible(false);
    }//GEN-LAST:event_txtItemNameFocusLost

    private void txtItemDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemDescriptionKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtRate);
            setBg();
            txtRate.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtItemDescriptionKeyReleased

    private void pushDataIntoItemsTable() {
        try {
            int negCount = currentBillItems.size();
//            JOptionPane.showMessageDialog(this,"size of currentbill "+currentBillItems.size());
//                  JOptionPane.showMessageDialog(this,"size of field "+FeildFilledRedirectDetails.size());

            if (!RealSettingsHelper.gettagNoIsTrue()) {
                Connection con = DBConnect.connect();

                for (int i = 0; i < table.getRowCount(); i++) {
                    Statement stmt = con.createStatement();
                    String itemname = table.getValueAt(i, 1).toString();
                    String sql = "select netwt,tagno from entryitem where itemname='" + itemname.trim() + "' and CAST(opqty AS DECIMAL) > 1 limit 1  ";
//JOptionPane.showMessageDialog(this,sql);
                    ResultSet re = stmt.executeQuery(sql);
                    double netwt = 0;

                    String tagno = "";
                    while (re.next()) {
                        netwt = re.getDouble(1);
//                        JOptionPane.showMessageDialog(this,netwt);

                        tagno = re.getString(2);
                        double purchasenetwt = Double.parseDouble(table.getValueAt(i, 4).toString());
                        netwt += purchasenetwt;

                    }

                    re.close();
                    stmt.close();

                }

                con.close();
            } else {

                for (int j = 0; j < currentBillItems.size(); j++) {
                    Map<String, Object> currentBill = currentBillItems.get(j);
                    List<List<String>> fieldData = FeildFilledRedirectDetails.get(j);

                    for (int i = 0; i < fieldData.size(); i++) {

                        try {
                            Connection con = DBConnect.connect();
                            Statement st = con.createStatement();

                            ResultSet rs = st.executeQuery("SELECT itemimage, itemprefix, itemgroup, taxslab, hsncode, shortname, tagno, opqty"
                                    + " FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = '"
                                    + currentBill.get("itemname") + "' AND itemgroup ='"+currentBill.get("itemgroup")+"';");

                            String itemimage = null,
                                    itemprefix = null,
                                    itemgroup = null,
                                    taxslab = null,
                                    hsncode = null,
                                    shortname = null,
                                    tagno = null,
                                    opqty = "0";

                            int cnt = 0;

                            while (rs.next()) {
                                itemimage = rs.getString("itemimage");
                                itemprefix = rs.getString("itemprefix");
                                itemgroup = rs.getString("itemgroup");
                                taxslab = rs.getString("taxslab");
                                hsncode = rs.getString("hsncode");
                                shortname = rs.getString("shortname");
//                        tagno = rs.getString("tagno");
                                opqty = rs.getString("opqty");
                                cnt++;
                            }

                            Connection con1 = DBConnect.connect();
                            Statement st1 = con1.createStatement();
                            ResultSet rs1 = st1.executeQuery("SELECT tag_counter"
                                    + " FROM " + DatabaseCredentials.TAG_COUNTER_TABLE + ";");
                            while (rs1.next()) {
                                tagno = rs1.getString("tag_counter");
//                            tagno = String.valueOf((Integer.parseInt(tagno)));
                            }
                            tagno = String.valueOf(Integer.parseInt(tagno) - negCount);
                            String currentitemname = currentBill.get("itemname").toString();
                            String newTagNo = currentitemname.substring(0, 2) + tagno;
                            st.clearBatch();
                            List<Object> columnNames1 = new ArrayList<>();
                            List<Object> data1 = new ArrayList<>();
                            String fields = "itemname, huid, grosswt, beedswt, netwt, diamondwt, carats,"
                                    + "opqty";
                            columnNames1.add("itemname");
                            columnNames1.add("huid");
                            columnNames1.add("grosswt");
                            columnNames1.add("beedswt");
                            columnNames1.add("netwt");
                            columnNames1.add("diamondwt");
                            columnNames1.add("carats");
                            columnNames1.add("opqty");

                            String values = "'" + currentBill.get("itemname") + "', '"
                                    + fieldData.get(i).get(0) + "', "
                                    + fieldData.get(i).get(1) + ", "
                                    + fieldData.get(i).get(2) + ", "
                                    + fieldData.get(i).get(3) + ", "
                                    + fieldData.get(i).get(4) + ", "
                                    + currentBill.get("diamondrate") + ", "
                                    + "0";
                            data1.add(currentBill.get("itemname").toString());
                            data1.add(fieldData.get(i).get(0));
                            data1.add(fieldData.get(i).get(1));
                            data1.add(fieldData.get(i).get(2));
                            data1.add(fieldData.get(i).get(3));
                            data1.add(fieldData.get(i).get(4));
                            data1.add(currentBill.get("diamondrate").toString());
                            data1.add("0");

                            if (itemimage != null) {
                                fields += ", itemimage";

                                values += ", '" + itemimage + "'";
                                columnNames1.add("itemimage");
                                data1.add(itemimage);

                            }

                            if (itemprefix != null) {
                                fields += ", itemprefix";
                                values += ", '" + itemprefix + "'";
                                columnNames1.add("itemprefix");
                                data1.add(itemprefix);
                            }
                            if (itemgroup != null) {
                                fields += ", itemgroup";
                                values += ", '" + itemgroup + "'";
                                columnNames1.add("itemgroup");
                                data1.add(itemgroup);
                            }
                            if (taxslab != null) {
                                fields += ", taxslab";
                                values += ", '" + taxslab + "'";
                                columnNames1.add("taxslab");
                                data1.add(taxslab);
                            }
                            if (hsncode != null) {
                                fields += ", hsncode";
                                values += ", '" + hsncode + "'";
                                columnNames1.add("hsncode");
                                data1.add(hsncode);
                            }
                            if (shortname != null) {
                                fields += ", shortname";
                                values += ", '" + shortname + "'";
                                columnNames1.add("shortname");
                                data1.add(shortname);
                            }
                            if (!bool) {
                                if (newTagNo != null) {
                                    fields += ", tagno";
                                    values += ", '" + newTagNo + "'";
                                    columnNames1.add("tagno");
                                    data1.add(newTagNo);
                                }
                            }
                            if (!bool) {
                                String query = "INSERT INTO " + DatabaseCredentials.ENTRY_ITEM_TABLE
                                        + "(" + fields + ") " + "VALUES (" + values + ");";
                                logger.info(query);
                                int execute = st.executeUpdate(query);
                                negCount--;
//                            if (execute == 1) {
//                                Long l = Long.parseLong(tagno);
//                                
//                                String q = "UPDATE `tagcounter` SET `tag_counter`=" + l + "";
//                                logger.info(q);
//                                st.executeUpdate(q);
//                            }
                            } else {

                                DBController.updateTableData(DatabaseCredentials.ENTRY_ITEM_TABLE,
                                        data1, columnNames1, "tagno", tagNoOfSelectedId);
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            double netwt = 0.0, totalDis = 0.0, netAmt = 0, BasicAmt = 0, taxAmt = 0, billAmt = 0, txtqty = 0, grosswt = 0.0, beedswt = 0.0;

            for (int i = 0; i < table.getRowCount(); i++) {
                grosswt += Double.parseDouble(table.getValueAt(i, 3).toString());
                netAmt += Double.parseDouble(table.getValueAt(i, 8).toString());
                BasicAmt += Double.parseDouble(table.getValueAt(i, 6).toString());
                taxAmt = netAmt - BasicAmt + totalDis;
                txtqty += Double.parseDouble(table.getValueAt(i, 5).toString());
                netwt += Double.parseDouble(table.getValueAt(i, 4).toString());
                beedswt = grosswt - netwt;
            }

            if ("".equals(txtPartyName.getText()) || txtPartyName.getText() == null) {
                JOptionPane.showMessageDialog(null, "Invalid Purchase Entry!");
                return;
            }

            try {
                saveTableData();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
            }

            pushDataIntoItemsTable();

            currentBillItems.clear();
            FeildFilledRedirectDetails.clear();
            setBg();
            txtPartyName.requestFocus();
//        PurchaseRegisterScreen.populatePurchasesHistoryListTable();
            purchasesListTableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Item Purchased Successfully'");
            clearTextFieldsEnter();
            clearFields();
            updateBillNo();
            grossweight = 0;
            netweight = 0;
            totaltaxableamount = 0;
            totalAmount = 0;
            txtPartyName.setBackground(Color.LIGHT_GRAY);
            id = -97108105;
        } catch (Exception e) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, e);
        }
        InsertTableHidingFields("false");


    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtDiamondWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondWtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setFocus(evt, txtDiamondRate);
            setBg();
            txtDiamondRate.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtDiamondWtKeyReleased

    private void txtNetWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetWtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            setFocus(evt, txtDiamondWt);
            setBg();
            txtDiamondWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtNetWtKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        InsertTableHidingFields("false");
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        InsertTableHidingFields("false");
//        clearTextFields();
//        clearLabels();
//        grossweight = 0;
//        netweight = 0;
//        totaltaxableamount = 0;
//        totalAmount = 0;
//        currentBillItems.clear();
//        FeildFilledRedirectDetails.clear();
//        purchasesListTableModel.setRowCount(0);
        DashBoardScreen.tabbedPane.remove(DashBoardScreen.tabbedPane.getSelectedComponent());

//        updateBillNo();
//        dispose();
//        
    }//GEN-LAST:event_jButton2MouseClicked

    private void pmItemNameSuggestionsPopupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pmItemNameSuggestionsPopupFocusLost
        // TODO add your handling code here:
        pmItemNameSuggestionsPopup.setVisible(false);
    }//GEN-LAST:event_pmItemNameSuggestionsPopupFocusLost

//    private void tagnoPopupFocusLost(java.awt.event.FocusEvent evt) {                                                     
//        // TODO add your handling code here:
//        tagnoPopup.setVisible(false);
//    } 
    private void pmPartyNameSuggestionsPopupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pmPartyNameSuggestionsPopupFocusLost
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
    }//GEN-LAST:event_pmPartyNameSuggestionsPopupFocusLost

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsPopup.setVisible(false);
        tagNoPopup.setVisible(false);
    }//GEN-LAST:event_formMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        pmPartyNameSuggestionsPopup.setVisible(false);
        pmItemNameSuggestionsPopup.setVisible(false);
        tagNoPopup.setVisible(false);

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        jTextField1.setBackground(Color.white);
        if (verifyBillNumber()) {
            jTextField1.requestFocus();
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private boolean verifyBillNumber() {
        // returns true if billnumber exists
        int billnumber = Integer.parseInt(jTextField1.getText());
        Connection con;
        try {
            con = DBConnect.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(bill) FROM purchasehistory where bill='" + billnumber + "';");

            int count = 0;
            while (rs.next()) {
                count = rs.getInt("COUNT(bill)");
            }
            if (count > 0) {
                jTextField1.setBackground(Color.red);
                JOptionPane.showMessageDialog(this, "Bill Number already Exists! Please enter new Bill Number.");
                return true;
            } else {
                jTextField1.setBackground(Color.white);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PurchaseScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtItemName.requestFocus();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void cmbTermsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTermsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTermsActionPerformed

    private void txtNetAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNetAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNetAmtActionPerformed

    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new ItemEntryScreen().setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtDiamondWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtFocusLost
        txtDiamondWt.setBackground(Color.white);
        if (!RealSettingsHelper.gettagNoIsTrue()) {
            if (txtDiamondWt.getText().trim().isEmpty()) {
                txtDiamondWt.setText("0");
            }

        }
    }//GEN-LAST:event_txtDiamondWtFocusLost

    private void txtPartyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusGained
        txtPartyName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyNameFocusGained

    private void txtItemNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusGained
        txtItemName.setBackground(new Color(245, 230, 66));
        selectedrow = 0;        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNameFocusGained

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked

        if (!(table.getValueAt(table.getSelectedRow(), 0).toString().trim().equals(""))) {

            // JOptionPane.showMessageDialog(this, "enter in if condition");
            if (selectedRow != -1) {

                if (!DBController.isDatabaseConnected()) {
                    DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                            DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
                }

                if (JOptionPane.showConfirmDialog(PurchaseScreen.this,
                        "Are you sure you want delete the selected record", "Delete Record",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    boolean rowDeleted
                            = DBController.removeDataFromTable(DatabaseCredentials.PURCHASE_HISTORY_TABLE,
                                    "id", table.getValueAt(selectedRow, 0));

                    if (rowDeleted) {
                        populatePurchasesListTable();
                        JOptionPane.showMessageDialog(this, "Record deleted successfully");
                    }

                }
            }
        } else {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(table.getSelectedRow());
            // JOptionPane.showMessageDialog(this, "enter in else condition");

        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        printjrxml();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        clearTextFields();
        clearLabels();
        purchasesListTableModel.setRowCount(0);
        grossweight = 0;
        netweight = 0;
        totaltaxableamount = 0;
        totalAmount = 0;
        currentBillItems.clear();
        FeildFilledRedirectDetails.clear();
        InsertTableHidingFields("false");
        updateBillNo();
    }//GEN-LAST:event_jButton6MouseClicked

    private void txtItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemNameActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_jTextField1FocusGained

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        txtQty.setBackground(new Color(245, 230, 66));
        selectedrow = 0;
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        txtQty.setBackground(Color.white);
        tagNoPopup.setVisible(false);


    }//GEN-LAST:event_txtQtyFocusLost

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

    private void txtRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusGained
        txtRate.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtRateFocusGained

    private void txtRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusLost
        txtRate.setBackground(Color.white);
    }//GEN-LAST:event_txtRateFocusLost

    private void txtExtraChargeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraChargeFocusGained
        txtExtraCharge.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtExtraChargeFocusGained

    private void txtBasicAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusLost
        txtBasicAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtBasicAmtFocusLost

    private void txtDiscountPercentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountPercentFocusGained
        txtDiscountPercent.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiscountPercentFocusGained

    private void txtDiscountAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountAmtFocusGained
        txtDiscountAmt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiscountAmtFocusGained

    private void txtTaxableAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxableAmtFocusLost
        txtTaxableAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtTaxableAmtFocusLost

    private void txtGSTPercentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTPercentFocusGained
        txtGSTPercent.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGSTPercentFocusGained

    private void txtGSTPercentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTPercentFocusLost
        txtGSTPercent.setBackground(Color.white);
    }//GEN-LAST:event_txtGSTPercentFocusLost

    private void txtNetAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusLost
        txtNetAmt.setBackground(Color.white);
    }//GEN-LAST:event_txtNetAmtFocusLost

    private void tagnoPopupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tagnoPopupFocusLost
        tagnoPopup.setVisible(false);
    }//GEN-LAST:event_tagnoPopupFocusLost

    private void tagnoContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tagnoContainerFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tagnoContainerFocusGained

    private void tagnoTablemouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tagnoTablemouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1 && evt.getClickCount() <= 2) {

            String tagselected1 = tagnoTable.getValueAt(tagnoTable.getSelectedRow(), 0).toString();
            if ("N.A".equals(tagselected1)) {
                txtNetWt.requestFocus();
                InsertTableHidingFields("false");
            } else {
                PurchaseItemsDetailDialog obj = new PurchaseItemsDetailDialog(this, true, Integer.parseInt(txtQty.getText()));
                obj.setVisible(true);
                InsertTableHidingFields("true");
            }
            tagnoPopup.setVisible(false);
        }
    }//GEN-LAST:event_tagnoTablemouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        InsertTableHidingFields("false");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void tagNoPopupfocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tagNoPopupfocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tagNoPopupfocusGained

    private void tagNoPopupfocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tagNoPopupfocusLost
        tagNoPopup.setVisible(false);
    }//GEN-LAST:event_tagNoPopupfocusLost

    private void tagNoPopupmouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tagNoPopupmouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tagNoPopupmouseClicked

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void clearFields() {
        txtPartyName.setText("");
        lblState.setText("");
        lblGST.setText("");
        lblPreviousBalance.setText("");
    }

    public boolean sale_Bill(int bill) {
        purchasesListTableModel.setRowCount(0);
        jButton1.setText("Update");
        billid = bill;
        List<List<Object>> salesItems;
        jTextField1.setText(String.valueOf(bill));
        if (DBController.isDatabaseConnected()) {
            salesItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, grosswt, netwt,qty, taxable_amt, gst_percent, net_amount,partyname,beedswt,diamondwt,tagnoItems,terms FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE bill= " + "'" + bill + "'");

            salesItems.forEach((item) -> {
                txtPartyName.setText(item.get(9).toString());
                cmbTerms.setSelectedItem(item.get(13).toString());
                setPartyDetails(item.get(9).toString());
                purchasesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // huid
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // grosswt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // netwt
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // qty
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableamt
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gstper
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // netamount
                });
                itemData.add(Arrays.asList(
                        (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2).toString(),
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3).toString(),
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(10).toString(),
                        (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4).toString(),
                        item.get(11).toString()));
                tagNoOfSelectedId = item.get(12).toString();

            });

        } else {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);

            salesItems = DBController.getDataFromTable("SELECT id,"
                    + "itemname, huid, grosswt, netwt,qty, taxable_amt, gst_percent, net_amount,partyname,beedswt,diamondwt,tagnoItems,terms FROM "
                    + DatabaseCredentials.PURCHASE_HISTORY_TABLE + " WHERE bill= " + "'" + bill + "'");

            salesItems.forEach((item) -> {

                txtPartyName.setText(item.get(9).toString());
                cmbTerms.setSelectedItem(item.get(13).toString());
                setPartyDetails(item.get(9).toString());
                purchasesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // huid
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // grosswt
                    "0", // netwt
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // qty
                    (item.get(6) == null || item.get(6).toString().trim().isEmpty()) ? "NULL" : item.get(6), // taxableamt
                    (item.get(7) == null || item.get(7).toString().trim().isEmpty()) ? "NULL" : item.get(7), // gstper
                    (item.get(8) == null || item.get(8).toString().trim().isEmpty()) ? "NULL" : item.get(8), // netamount
                });
                itemData.add(Arrays.asList(
                        (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2).toString(),
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3).toString(),
                        (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(10).toString(),
                        (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4).toString(),
                        item.get(11).toString()));
                tagNoOfSelectedId = item.get(12).toString();

            });
        }

        fillgrandtotal();

        bool = true;
        return bool;
    }

    private javax.swing.JScrollPane itemNameDetailsContainer;
    private javax.swing.JScrollPane tagnoContainer;
    private javax.swing.JTable itemNameDetailsTable;
    private javax.swing.JTable tagnoTable;
//    private javax.swing.JPopupMenu tagnoPopup;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbTerms;
    private com.toedter.calendar.JDateChooser datePurchaseDate;
    private javax.swing.JPopupMenu itemNameDetailsPopup;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    public javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblGST;
    private javax.swing.JLabel lblPreviousBalance;
    private javax.swing.JLabel lblState;
    private javax.swing.JPopupMenu pmItemNameSuggestionsPopup;
    private javax.swing.JPopupMenu pmPartyNameSuggestionsPopup;
    public javax.swing.JPanel pnlRootContainer;
    private javax.swing.JScrollPane spTblItemNameSuggestionsContainer;
    private javax.swing.JScrollPane spTblPartyNameSuggestionsContainer;
    public static javax.swing.JTable table;
    private javax.swing.JPopupMenu tagNoPopup;
    private javax.swing.JPopupMenu tagnoPopup;
    private javax.swing.JTable tblItemNameSuggestions;
    private javax.swing.JTable tblPartyNameSuggestions;
    private javax.swing.JTextField txtBasicAmt;
    private javax.swing.JTextField txtDiamondRate;
    private javax.swing.JTextField txtDiamondWt;
    private javax.swing.JTextField txtDiscountAmt;
    private javax.swing.JTextField txtDiscountPercent;
    private javax.swing.JTextField txtExtraCharge;
    private javax.swing.JTextField txtGSTPercent;
    private javax.swing.JTextField txtItemDescription;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtNetAmt;
    private javax.swing.JTextField txtNetWt;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtTaxableAmt;
    private javax.swing.JTextField txtcount;
    private javax.swing.JTextField txtgrosswt;
    private javax.swing.JTextField txtnetwt;
    private javax.swing.JTextField txttaxable;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
