
package jewellery;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
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
import javax.swing.JButton;

/**
 *
 * @author ASUS
 */
public class SaleScreenNoGST   extends javax.swing.JFrame {

	private List<Object> columnNames = new ArrayList<>();
	private final List<Object> data = new ArrayList<>();
	private final List<Object> accountNames = new ArrayList<>();
	private List<Object> partyGSTAndBalance = new ArrayList<>();
	private static final List<Object> ITEM_NAMES = new ArrayList<>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static DefaultTableModel salesListTableModel;
	private ImageIcon imageIcon;

	private DateTimeFormatter dateTimeFormatter;
//	private final PurchaseItemsDetailDialog purchaseItemsDetailsDialog = new PurchaseItemsDetailDialog(this, false, 0);
	private LocalDateTime localDateTime;
	private final DefaultTableModel itemNameSuggestionsTableModel;
	private final DefaultTableModel partyNameSuggestionsTableModel;
	private List<Object> fieldsData;
	private int selectedRow = -1;
	private int id;
	private List<Object> states;

	private Connection connect;
	//String testReportLocation;
	//private String reportsavepath;
	public SaleScreenNoGST() {
		initComponents();

		salesListTableModel = (DefaultTableModel) tblPurchasesList.getModel();
		itemNameSuggestionsTableModel = (DefaultTableModel) tblItemNameSuggestions.getModel();
		partyNameSuggestionsTableModel = (DefaultTableModel) tblPartyNameSuggestions.getModel();
		JTableHeader header = tblPurchasesList.getTableHeader();
		header.setFont(new Font("Dialog", Font.BOLD, 18));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int h = d.height;
		int w = d.width;

		pnlRootContainer.setSize(w - 280, h - 100);

		centerTableCells();

		populateSalesListTable();

		setDateOnJCalender("yyyy-MM-dd");

		setImageOnJLabel(lblSaveButton, AssetsLocations.DOUBLE_CHECK_ICON);
		setImageOnJLabel(lblDeleteButton, AssetsLocations.TRASH_CAN_ICON);
		setImageOnJLabel(lblAttachButton, AssetsLocations.ATTACHMENT_ICON);
		setImageOnJLabel(lblPrintButton, AssetsLocations.PRINT_ICON);
		setImageOnJLabel(lblClearFields, AssetsLocations.CLEANING_BROOM_ICON);
		pmItemNameSuggestionsPopup.add(spTblItemNameSuggestionsContainer);
		pmItemNameSuggestionsPopup.setLocation(txtItemName.getX() + 6, txtItemName.getY() + 100);

		pmPartyNameSuggestionsPopup.add(spTblPartyNameSuggestionsContainer);
		pmPartyNameSuggestionsPopup.setLocation(txtPartyName.getX() + 6, txtPartyName.getY() + 100);

		txtBill.setText(String.valueOf(getBillNofromSales()));
		lblGST.setText("0");
		txtGSTPercent.setText("0");
		txtTaxableAmt.setText("0");
		lblGST.setVisible(false);
		txtGSTPercent.setVisible(false);
		txtTaxableAmt.setVisible(false);
		fetchAccountNames("Cash");
		fetchItemNames();
	}
	int getBillNofromSales(){
		int sno=0;
		try {
			Connection c = DBConnect.connect();
			Statement s = c.createStatement();

			ResultSet rs = s.executeQuery("select bill from sales");
			while(rs.next()){
				sno = rs.getInt("bill");
			}
			sno=sno+1;
			String q1="Update salebillnocounter set billno='"+sno+"'";
			s.executeUpdate(q1);
		} catch (SQLException ex) {
			Logger.getLogger(SaleScreenNoGST  .class.getName()).log(Level.SEVERE, null, ex);
		}
		return sno;
	}
	private void fetchAccountNames(String accountType) {
		if(!DBController.isDatabaseConnected()) {
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
		if(!DBController.isDatabaseConnected()) {
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
		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
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

	private void setImageOnJLabel(javax.swing.JLabel component
			, String resourceLocation) {
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

	private void setItemRate(String itemName) {
		if(!DBController.isDatabaseConnected()) {
			DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
					DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
		}

		List<Object> itemGroup = DBController.executeQuery("SELECT itemgroup FROM " 
				+ DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + itemName + "'");
		System.out.print(DatabaseCredentials.ENTRY_ITEM_TABLE);
		if(itemGroup != null && (!itemGroup.isEmpty())) {
			System.out.print("Entered if");
			List<Object> itemRate = null;

			switch(itemGroup.get(0).toString()) {
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

			if(itemRate != null && (!itemRate.isEmpty())) {
				txtRate.setText(itemRate.get(0).toString());
			}
		}
	}
	void getHuid(String name){
		String huid = null;
		if(!DBController.isDatabaseConnected()) {
			DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
					DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
		}

		List<Object> itemGroup = DBController.executeQuery("SELECT huid FROM " 
				+ DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = " + "'" + name + "'");

		if(itemGroup != null && (!itemGroup.isEmpty())) {

			List<Object> itemRate = null;

			huid=itemGroup.get(0).toString();
		}
		txthuid.setText(huid);
	}

	void getGST(String name){


		txtGSTPercent.setText("0");
	}
	private boolean fieldsAreValidated() {
		if(UtilityMethods.isTextFieldEmpty(txtPartyName) ||
				!UtilityMethods.inputOnlyContainsAlphabets(txtPartyName.getText())) {
			JOptionPane.showMessageDialog(this, "Please enter the party name correctly");
			return false;
		}
		else if(UtilityMethods.isTextFieldEmpty(txtItemName) ||
				!UtilityMethods.inputContainsAlphabetsAndNumbers(txtItemName.getText())) {
			JOptionPane.showMessageDialog(this, "Please enter the item name correctly");
			return false;
		}

		return true;
	}
	
	private void clearTextFields() {

		Component[] components = this.pnlRootContainer.getComponents();

		for(Component component : components) {
			if(component instanceof JTextField) {
				
				JTextComponent textComponent = (JTextComponent) component;
				textComponent.setText("");
				textComponent.setBackground(Color.white);
			}
		}

	}
	private void clearTextFields2() {

		Component[] components = this.pnlRootContainer.getComponents();

		for(Component component : components) {
			if(component instanceof JTextField) {
				if(component == txtPartyName) {
					continue;
				}
				if(component == txtBill) {
					continue;
				}
				JTextComponent textComponent = (JTextComponent) component;
				textComponent.setText("");
				textComponent.setBackground(Color.white);
			}
		}

	}
	private void setBgTextFields() {

		Component[] components = this.pnlRootContainer.getComponents();

		for(Component component : components) {
			if(component instanceof JTextField) {
				JTextComponent textComponent = (JTextComponent) component;

				textComponent.setBackground(Color.white);
			}
		}

	}

	private void clearLabels() {

		lblPreviousBalance.setText("");
		lblState.setText("");
		lblAddress.setText("");
	}

	private void centerTableCells() {
		((DefaultTableCellRenderer)tblPurchasesList
				.getDefaultRenderer(String.class))
		.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void setComponentsEnabledOrDisabled() {
		Component[] components = this.pnlRootContainer.getComponents();

		for(Component component : components) {
			if(component instanceof JTextField) {
				JTextComponent textComponent = (JTextComponent) component;
				textComponent.setEnabled(!textComponent.isEnabled());
			}
		}
	}

	private void populateSalesListTable() {
        salesListTableModel.setRowCount(0);
        
        List<List<Object>> salesItems;
        
        if(DBController.isDatabaseConnected()) {
            salesItems = DBController.getDataFromTable("SELECT id,"
                + "itemname, huid, netwt, qty,netamount FROM " 
                + DatabaseCredentials.SALES_TABLE + " WHERE date = " 
                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'"+ " AND bill= "+"'" + txtBill.getText() + "'");

            salesItems.forEach((item) -> {
                salesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // netamount
                });
            });
            
            if(salesListTableModel.getRowCount() > 0) {
                List<Object> aggregateFuncDataHolder = DBController.executeQuery("SELECT "
                        + "COUNT(id) FROM " + DatabaseCredentials.SALES_TABLE);
                txtcount.setText(aggregateFuncDataHolder.get(0).toString());
                aggregateFuncDataHolder.clear();

                aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(netwt) FROM "
                        + DatabaseCredentials.SALES_TABLE);
                
                jTextField2.setText(aggregateFuncDataHolder.get(0).toString());
                
                aggregateFuncDataHolder.clear();
                
                aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(netamount) FROM "
                        + DatabaseCredentials.SALES_TABLE);
                jTextField8.setText(aggregateFuncDataHolder.get(0).toString());
                aggregateFuncDataHolder.clear();
            }
        }
        else {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
            
            salesItems = DBController.getDataFromTable("SELECT id,"
                + "itemname, huid, netwt, qty,netamount FROM " 
                + DatabaseCredentials.SALES_TABLE + " WHERE date = " 
                    + "'" + UtilityMethods.getCurrentDate("yyyy-MM-dd") + "'"+ " AND bill= "+"'" + txtBill.getText() + "'");

            salesItems.forEach((item) -> {
                salesListTableModel.addRow(new Object[]{
                    (item.get(0) == null || item.get(0).toString().trim().isEmpty()) ? "NULL" : item.get(0),
                    (item.get(1) == null || item.get(1).toString().trim().isEmpty()) ? "NULL" : item.get(1), // itemname
                    (item.get(2) == null || item.get(2).toString().trim().isEmpty()) ? "NULL" : item.get(2), // grosswt
                    (item.get(3) == null || item.get(3).toString().trim().isEmpty()) ? "NULL" : item.get(3), // netwt
                    (item.get(4) == null || item.get(4).toString().trim().isEmpty()) ? "NULL" : item.get(4), // qty
                    (item.get(5) == null || item.get(5).toString().trim().isEmpty()) ? "NULL" : item.get(5), // netAmount
                    
                });
            });
            
            if(salesListTableModel.getRowCount() > 0) {
                List<Object> aggregateFuncDataHolder = DBController.executeQuery("SELECT "
                        + "COUNT(id) FROM " + DatabaseCredentials.SALES_TABLE);
                txtcount.setText(aggregateFuncDataHolder.get(0).toString());
                aggregateFuncDataHolder.clear();

                aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(netwt) FROM "
                        + DatabaseCredentials.SALES_TABLE);
                
                jTextField2.setText(aggregateFuncDataHolder.get(0).toString());
                
                aggregateFuncDataHolder.clear();
                
                aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(netamount) FROM "
                        + DatabaseCredentials.SALES_TABLE);
                jTextField8.setText(aggregateFuncDataHolder.get(0).toString());
                aggregateFuncDataHolder.clear();
            }

        }
        fillgrandtotal();
        
    }

	private void verifyNetWeightCalculationFields() {
		double grossWt;
		double beedsWt;

		if(txtGrossWt.getText().trim().isEmpty() && txtBeedsWt.getText().trim().isEmpty()) {
			txtGrossWt.setText("1");
			txtBeedsWt.setText("0");

			grossWt = Double.valueOf(txtGrossWt.getText().trim());
			beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

			txtNetWt.setText(String.valueOf((grossWt - beedsWt)));

		}
		else if(txtGrossWt.getText().trim().isEmpty()) {
			txtGrossWt.setText("1");

			grossWt = Double.valueOf(txtGrossWt.getText().trim());
			beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

			txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
		}
		else if(txtBeedsWt.getText().trim().isEmpty()) {
			txtBeedsWt.setText("0");

			grossWt = Double.valueOf(txtGrossWt.getText().trim());
			beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

			txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
		}
		else {
			grossWt = Double.valueOf(txtGrossWt.getText().trim());
			beedsWt = Double.valueOf(txtBeedsWt.getText().trim());

			txtNetWt.setText(String.valueOf((grossWt - beedsWt)));
		}
	}


	private void calculateBasicAmount() {
		String per=cmbPer.getSelectedItem().toString();
		double labour;
		if(!(txtNetWt.getText().trim().isEmpty() || txtDiamondWt.getText().trim().isEmpty() 
				|| txtDiamondRate.getText().trim().isEmpty() || txtExtraCharge.getText().trim().isEmpty())) {
			double labourper=Double.parseDouble(txtExtraCharge1.getText());
			double netWeight = Double.parseDouble(txtNetWt.getText());
			double rate=Double.parseDouble(txtRate.getText());
			if(per.equals("Gram")){
				labour=netWeight*labourper;
			}
			else if(per.equals("Pair")){
				labour=labourper;
			}
			else if(per.equals("Piece")){
				labour=labourper;
			}
			else{
				labour=(netWeight*rate)*(labourper/100.0);
			}
			double labourdis=Double.parseDouble(txtExtraCharge2.getText());
			double diamondWt = Double.parseDouble(txtDiamondWt.getText());
			double diamondRate = Double.parseDouble(txtDiamondRate.getText());
			double extraCharge = Double.parseDouble(txtExtraCharge.getText());

			double basicAmount = (netWeight*rate) + (diamondWt*diamondRate) + extraCharge+ (labour-labourdis);

			txtBasicAmt.setText(String.valueOf(basicAmount));

		}
	}




	private void setPartyDetails(String partyName) {
		partyGSTAndBalance = DBController.executeQuery("SELECT gstno, opbal, address, state FROM " + 
				DatabaseCredentials.ACCOUNT_TABLE + " WHERE accountname = " + "'" + partyName + "'");

		if(partyGSTAndBalance.get(0) != null) {
			lblGST.setText(partyGSTAndBalance.get(0).toString());
			lblGST.setText("0");
		}
		else {
			lblGST.setText("0");
		}

		if(partyGSTAndBalance.get(1) != null) {
			lblPreviousBalance.setText(partyGSTAndBalance.get(1).toString());
		}
		else {
			lblPreviousBalance.setText("0.0");
		}

		if(partyGSTAndBalance.get(2) != null) {
			lblAddress.setText(partyGSTAndBalance.get(2).toString());
		}
		else {
			lblAddress.setText("---");
		}

		if(partyGSTAndBalance.get(3) != null) {
			lblState.setText(partyGSTAndBalance.get(3).toString());
		}
		else {
			lblState.setText("---");
		}
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchasesList = new javax.swing.JTable();
        jLabel55 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtBill = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        datePurchaseDate = new com.toedter.calendar.JDateChooser();
        cmbTerms = new javax.swing.JComboBox<>();
        lblSaveButton = new javax.swing.JLabel();
        lblDeleteButton = new javax.swing.JLabel();
        lblAttachButton = new javax.swing.JLabel();
        lblPrintButton = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNetAmt = new javax.swing.JTextField();
        lblAddPartyName = new javax.swing.JLabel();
        lblEditPartyName = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        lblPreviousBalance = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lblClearFields = new javax.swing.JLabel();
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
        jTextField8 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        lblGST = new javax.swing.JTextField();
        txtGSTPercent = new javax.swing.JTextField();
        txtTaxableAmt = new javax.swing.JTextField();

        pmItemNameSuggestionsPopup.setMinimumSize(new java.awt.Dimension(200, 200));

        spTblItemNameSuggestionsContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                spTblItemNameSuggestionsContainerFocusGained(evt);
            }
        });

        tblItemNameSuggestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Item Group", "Tax Slab"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        txtItemName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemNameFocusLost(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("<html>Item Name<font color=\"red\">*</font></html>");

        txtQty.setEditable(false);
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

        txtBeedsWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBeedsWtKeyReleased(evt);
            }
        });

        txtNetWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetWtKeyReleased(evt);
            }
        });

        txtDiamondWt.addFocusListener(new java.awt.event.FocusAdapter() {
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
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondRateFocusLost(evt);
            }
        });
        txtDiamondRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondRateKeyReleased(evt);
            }
        });

        txtGrossWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGrossWtKeyReleased(evt);
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

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("<html>Qty.<font color=\"red\">*</font></html>");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Beeds Wt.");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Net Wt.");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Diamond Wt.");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Diamond Rate / Carat");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("<html>Gross Wt.<font color=\"red\">*</font></html>");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Item Descriptions");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("<html>Per<font color=\"red\">*</font></html>");

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Rate");

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Extra change");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Basic Amt");

        txtRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRateKeyReleased(evt);
            }
        });

        txtExtraCharge.addFocusListener(new java.awt.event.FocusAdapter() {
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

        cmbPer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gram", "Pair", "Piece", "Percentage %" }));
        cmbPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbPerKeyReleased(evt);
            }
        });

        tblPurchasesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "HUID", "Net Wt.", "Qty.", "Net Amt."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        jLabel55.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel55.setText("<html>Party Name<font color=\"red\">*</font></html>");

        txtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
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
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("<html>Bill<font color=\"red\">*</font></html>");

        txtBill.setEditable(false);
        txtBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Previous Balance");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Non GST SALE");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("<html>DATE<font color=\"red\">*</font></html>");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
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

        lblSaveButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSaveButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblSaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSaveButtonMouseClicked(evt);
            }
        });

        lblDeleteButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDeleteButtonMouseClicked(evt);
            }
        });

        lblAttachButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAttachButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblPrintButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrintButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPrintButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPrintButtonMouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Net Amt");

        txtNetAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNetAmtFocusGained(evt);
            }
        });
        txtNetAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetAmtKeyReleased(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("State");

        lblPreviousBalance.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPreviousBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Address");

        lblClearFields.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClearFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblClearFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClearFieldsMouseClicked(evt);
            }
        });

        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("<html>Labour<font color=\"red\">*</font></html>");

        txtExtraCharge1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExtraCharge1FocusLost(evt);
            }
        });
        txtExtraCharge1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExtraCharge1KeyReleased(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Labour Amt Discount");

        txtExtraCharge2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExtraCharge2FocusLost(evt);
            }
        });
        txtExtraCharge2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExtraCharge2KeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel27.setText("HUID");

        txthuid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthuidActionPerformed(evt);
            }
        });

        save.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        save.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8-ok-48.png"))); // NOI18N
        save.setText("Go to next Bill");
        save.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Count:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Net Weight:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Grand Total");

        txtcount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jTextField8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout pnlRootContainerLayout = new javax.swing.GroupLayout(pnlRootContainer);
        pnlRootContainer.setLayout(pnlRootContainerLayout);
        pnlRootContainerLayout.setHorizontalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtItemName)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                    .addComponent(txthuid))
                                .addGap(18, 18, 18)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGrossWt)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                                .addGap(5, 5, 5)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBeedsWt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNetWt, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel19))
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(txtDiamondWt, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDiamondRate, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(txtItemDescription))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(lblState, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBill, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .addComponent(lblPreviousBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel26)
                                        .addGap(23, 23, 23)
                                        .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addGap(63, 63, 63)
                                        .addComponent(lblGST, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtGSTPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTaxableAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(288, 288, 288))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                            .addComponent(txtRate))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtExtraCharge1)
                                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbPer, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtExtraCharge2)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtExtraCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                            .addComponent(txtBasicAmt))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNetAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtcount, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblAddPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEditPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
                                        .addComponent(datePurchaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbTerms, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(lblSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblDeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblAttachButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblPrintButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblClearFields, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlRootContainerLayout.setVerticalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClearFields, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAttachButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPrintButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datePurchaseDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbTerms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBill, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGST, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGSTPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTaxableAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblPreviousBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAddress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAddPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblEditPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblState, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)))
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txthuid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNetWt)
                            .addComponent(txtBeedsWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDiamondWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiamondRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQty)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGrossWt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlRootContainerLayout.createSequentialGroup()
                            .addComponent(jLabel29)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlRootContainerLayout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(29, 29, 29)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBasicAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNetAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtExtraCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtExtraCharge1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtExtraCharge2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcount, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(save))
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlRootContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void tblPurchasesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchasesListMouseClicked
		selectedRow = tblPurchasesList.getSelectedRow();

		if(DBController.isDatabaseConnected()) {
			//clearTextFields();

			fieldsData = DBController.executeQuery("SELECT * FROM " 
					+ DatabaseCredentials.SALES_TABLE + " WHERE id = "
					+ "'" + tblPurchasesList.getValueAt(selectedRow, 0) + "'");

			id = Integer.valueOf(fieldsData.remove(0).toString());

			try {
				datePurchaseDate.setDate(dateFormat.parse(fieldsData.get(0).toString()));
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}

			cmbTerms.setSelectedItem(fieldsData.get(1).toString());
			txtPartyName.setText(fieldsData.get(2).toString());

			if(fieldsData.get(3) != null) {
				txtBill.setText(fieldsData.get(3).toString());
			}

			if(fieldsData.get(4) != null) {
				lblGST.setText(fieldsData.get(4).toString());
			}

			lblPreviousBalance.setText(fieldsData.get(5).toString());
			txtItemName.setText(fieldsData.get(6).toString());
			txthuid.setText(fieldsData.get(7).toString());
			if(fieldsData.get(8) != null) {
				txtQty.setText(fieldsData.get(8).toString());
			}

			if(fieldsData.get(9) != null) {
				lblAddress.setText(fieldsData.get(9).toString());
			}
			else {
				lblAddress.setText("---");
			}

			if(fieldsData.get(10) != null) {
				txtBeedsWt.setText(fieldsData.get(10).toString());
			}

			if(fieldsData.get(11) != null) {
				txtNetWt.setText(fieldsData.get(11).toString());
			}

			if(fieldsData.get(12) != null) {
				txtDiamondWt.setText(fieldsData.get(12).toString());
			}

			if(fieldsData.get(13) != null) {
				txtDiamondRate.setText(fieldsData.get(13).toString());
			}

			if(fieldsData.get(14) != null) {
				txtGrossWt.setText(fieldsData.get(14).toString());
			}

			if(fieldsData.get(15) != null) {
				txtItemDescription.setText(fieldsData.get(15).toString());
			}

			cmbPer.setSelectedItem(fieldsData.get(16).toString());

			if(fieldsData.get(17) != null) {
				txtExtraCharge.setText(fieldsData.get(17).toString());
			}

			if(fieldsData.get(18) != null) {
				txtBasicAmt.setText(fieldsData.get(18).toString());
			}

			if(fieldsData.get(19) != null) {
				txtNetAmt.setText(fieldsData.get(19).toString());
			}

			if(fieldsData.get(20) != null) {
				txtRate.setText(fieldsData.get(20).toString());
			}

			if(fieldsData.get(21) != null) {
				txtTaxableAmt.setText(fieldsData.get(21).toString());
			}
			if(fieldsData.get(22) != null) {
				txtGSTPercent.setText(fieldsData.get(22).toString());
			}
			if(fieldsData.get(23) != null) {
				txtExtraCharge1.setText(fieldsData.get(23).toString());
			}
			if(fieldsData.get(24) != null) {
				txtExtraCharge2.setText(fieldsData.get(24).toString());
			}
			setPartyDetails(fieldsData.get(2).toString());
		}
	}//GEN-LAST:event_tblPurchasesListMouseClicked

	private void lblSaveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaveButtonMouseClicked
		columnNames.clear();
		data.clear();

		if(fieldsAreValidated()) {

			if(DBController.isDatabaseConnected()) {
				columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);

				// Remove the id as it is auto generated
				columnNames.remove(0);
			}
			else {
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

			data.add(lblGST.getText());

			data.add(lblPreviousBalance.getText());
			data.add(txtItemName.getText().trim());
			data.add(txthuid.getText().trim());
			if(!txtQty.getText().trim().isEmpty()) {
				data.add(txtQty.getText().trim());
			}
			else {
				columnNames.remove("qty");
			}

			if(!lblAddress.getText().isEmpty()) {
				data.add(lblAddress.getText());
			}
			else {
				columnNames.remove("address");
			}

			if(!txtBeedsWt.getText().trim().isEmpty()) {
				data.add(txtBeedsWt.getText().trim());
			}
			else {
				columnNames.remove("beedswt");
			}

			if(!txtNetWt.getText().trim().isEmpty()) {
				data.add(txtNetWt.getText().trim());
			}
			else {
				columnNames.remove("netwt");
			}

			if(!txtDiamondWt.getText().trim().isEmpty()) {
				data.add(txtDiamondWt.getText().trim());
			}
			else {
				columnNames.remove("diamondwt");
			}

			if(!txtDiamondRate.getText().trim().isEmpty()) {
				data.add(txtDiamondRate.getText().trim());
			}
			else {
				columnNames.remove("diamondrate");
			}

			if(!txtGrossWt.getText().trim().isEmpty()) {
				data.add(txtGrossWt.getText().trim());
			}
			else {
				columnNames.remove("grosswt");
			}

			if(!txtItemDescription.getText().trim().isEmpty()) {
				data.add(txtItemDescription.getText().trim());
			}
			else {
				columnNames.remove("itemdescription");
			}

			data.add(cmbPer.getSelectedItem());

			if(!txtExtraCharge.getText().trim().isEmpty()) {
				data.add(txtExtraCharge.getText().trim());
			}
			else {
				columnNames.remove("extrachange");
			}

			if(!txtBasicAmt.getText().trim().isEmpty()) {
				data.add(txtBasicAmt.getText().trim());
			}
			else {
				columnNames.remove("bankamt");
			}

			if(!txtNetAmt.getText().trim().isEmpty()) {
				data.add(txtNetAmt.getText().trim());
			}
			else {
				columnNames.remove("netamount");
			}

			if(!txtRate.getText().trim().isEmpty()) {
				data.add(txtRate.getText().trim());
			}
			else {
				columnNames.remove("rate");
			}

			if(!txtTaxableAmt.getText().trim().isEmpty()) {
				data.add("0");
			}
			else {
				data.add("0");
			}

			if(!txtGSTPercent.getText().trim().isEmpty()) {
				data.add(txtGSTPercent.getText().trim());
			}
			else {
				columnNames.remove("gstpercent");
			}

			if(!txtExtraCharge1.getText().trim().isEmpty()) {
				data.add(txtExtraCharge1.getText().trim());
			}
			else {
				columnNames.remove("labour");
			}
			if(!txtExtraCharge2.getText().trim().isEmpty()) {
				data.add(txtExtraCharge2.getText().trim());
			}
			else {
				columnNames.remove("labour_amt_discount");
			}
			if(!DBController.updateTableData(DatabaseCredentials.SALES_TABLE, 
					data, columnNames, "id", id)) {

				DBController.insertDataIntoTable(DatabaseCredentials.SALES_TABLE, 
						columnNames, data);

				//                setComponentsEnabledOrDisabled();

			}

			populateSalesListTable();
			fillgrandtotal();

			SaleRegisterScreen.populateSalesListTable();
			clearTextFields();
			setBgTextFields();
			txtItemName.requestFocus();
			txtItemName.setBackground(Color.LIGHT_GRAY);


		}

		id = -97108105;
	}//GEN-LAST:event_lblSaveButtonMouseClicked
	void fillgrandtotal(){
		double netwt = 0.0,total = 0.0;
		txtcount.setText(Integer.toString(salesListTableModel.getRowCount()));

		for(int i=0;i<salesListTableModel.getRowCount();i++){
			netwt+=Double.parseDouble(salesListTableModel.getValueAt(i, 3).toString());
			total+=Double.parseDouble(salesListTableModel.getValueAt(i, 5).toString());     
		}

		jTextField2.setText(Double.toString(netwt));
		jTextField8.setText(Double.toString(total));

	}
	public static int getSaleBillNo(String partyName) {
		if(!DBController.isDatabaseConnected()) {
			DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
					DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
		}

		List<Object> partyBillNo = DBController.executeQuery("SELECT bill FROM " 
				+ DatabaseCredentials.SALES_TABLE + " WHERE partyName = " 
				+ "'" + partyName + "'");

		if(partyBillNo != null && partyBillNo.size() > 0) {
			return Integer.parseInt(partyBillNo.get(0).toString());
		}

		return 0;
	}
	private void txtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyNameKeyReleased
		if (!(accountNames == null || accountNames.isEmpty())) {
			switch (evt.getKeyCode()) {
			case java.awt.event.KeyEvent.VK_BACK_SPACE:
				pmPartyNameSuggestionsPopup.setVisible(false);
				break;
			case KeyEvent.VK_ENTER:
				txtItemName.requestFocusInWindow();
				setBgTextFields();
				txtItemName.setBackground(Color.LIGHT_GRAY);
				break;
			default:
				EventQueue.invokeLater(() -> {
					pmPartyNameSuggestionsPopup.setVisible(true);
					populateSuggestionsTableFromDatabase(partyNameSuggestionsTableModel, "SELECT accountname, "
							+ "state, grp FROM " + DatabaseCredentials.ACCOUNT_TABLE 
							+ " WHERE accountname LIKE " + "'" + txtPartyName.getText() + "%'");
				});
				break;
			}
		}

	}//GEN-LAST:event_txtPartyNameKeyReleased
	private void populateSuggestionsTableFromDatabase(DefaultTableModel suggestionsTable, String query) {
		if(!DBController.isDatabaseConnected()) {
			DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
					DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
		}

		List<List<Object>> suggestions = DBController.getDataFromTable(query);

		suggestionsTable.setRowCount(0);

		suggestions.forEach((suggestion) -> {
			suggestionsTable.addRow(new Object[] {
					(suggestion.get(0) == null) ? "NULL" : suggestion.get(0),
							(suggestion.get(1) == null) ? "NULL" : suggestion.get(1),
									(suggestion.get(2) == null) ? "NULL" : suggestion.get(2),
			});
		});
		fillgrandtotal();
	}    
	private void txtBillKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillKeyReleased
		setFocus(evt, txtItemName);
		setBgTextFields();
		txtItemName.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtBillKeyReleased

	private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased
		if(!(ITEM_NAMES == null || ITEM_NAMES.isEmpty())) {
			switch (evt.getKeyCode()) {
			case java.awt.event.KeyEvent.VK_BACK_SPACE:
				pmItemNameSuggestionsPopup.setVisible(false);
				break;
			case KeyEvent.VK_ENTER:
				txtItemName.setText(txtItemName.getText());


				EventQueue.invokeLater(() -> {
					if (!txtItemName.getText().trim().isEmpty()) {
						setItemRate(txtItemName.getText().trim());
						getHuid(txtItemName.getText().trim());
						txtQty.setText("1");
					}

				});

				txtGrossWt.requestFocusInWindow();
				setBgTextFields();
				txtGrossWt.setBackground(Color.LIGHT_GRAY);
				if (!txtQty.getText().trim().isEmpty()) {
//                  new PurchaseItemsDetailDialog(this, true, 
//                          Integer.parseInt(txtQty.getText().trim())).setVisible(true);
//                  purchaseItemsDetailsDialog.setTableRowCount(Integer.parseInt(txtQty.getText().trim()));
//                  purchaseItemsDetailsDialog.setVisible(true);
//                  
//                  purchaseItemsDetailsDialog.populateItemsDetailsTableSale("SELECT huid, "
//                              + "grosswt, beedswt,netwt,diamondwt FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE + 
//                              " WHERE itemname LIKE " + "'" + txtItemName.getText() + "'");
              }

				break;
			default:
				EventQueue.invokeLater(() -> {
					pmItemNameSuggestionsPopup.setVisible(true);
					populateSuggestionsTableFromDatabase(itemNameSuggestionsTableModel, "SELECT itemname, "
							+ "itemgroup, taxslab FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE + 
							" WHERE itemname LIKE " + "'" + txtItemName.getText() + "%'");
					//                        autoComplete(ITEM_NAMES, txtItemName.getText(), txtItemName);
				});
				break;
			}
		}
	}//GEN-LAST:event_txtItemNameKeyReleased

	private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
		setFocus(evt, txtRate);
		setBgTextFields();
		txtRate.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtQtyKeyReleased

	private void txtBeedsWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBeedsWtKeyReleased
		if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
			verifyNetWeightCalculationFields();
		}

		setFocus(evt, txtNetWt);
		setBgTextFields();
		txtNetWt.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtBeedsWtKeyReleased

	private void txtNetWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetWtKeyReleased
		setFocus(evt, txtDiamondWt);
		setBgTextFields();
		txtDiamondWt.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtNetWtKeyReleased

	private void txtDiamondWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondWtKeyReleased
		setFocus(evt, txtDiamondRate);
		setBgTextFields();
		txtDiamondRate.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtDiamondWtKeyReleased

	private void txtDiamondRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondRateKeyReleased
		setFocus(evt, txtItemDescription);
		setBgTextFields();
		txtItemDescription.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtDiamondRateKeyReleased

	private void txtGrossWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossWtKeyReleased
		setFocus(evt, txtBeedsWt);
		setBgTextFields();
		txtBeedsWt.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtGrossWtKeyReleased

	private void txtItemDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemDescriptionKeyReleased
		setFocus(evt, txtRate);
		setBgTextFields();
		txtRate.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtItemDescriptionKeyReleased

	private void txtRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyReleased
		setFocus(evt,txtExtraCharge1);
		setBgTextFields();
		txtExtraCharge1.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtRateKeyReleased

	private void cmbPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPerKeyReleased
		setFocus(evt, txtExtraCharge2);
		setBgTextFields();
		txtExtraCharge2.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_cmbPerKeyReleased

	private void txtExtraChargeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraChargeKeyReleased
		setFocus(evt, txtBasicAmt);
		setBgTextFields();
		txtBasicAmt.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtExtraChargeKeyReleased

	private void cmbTermsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTermsActionPerformed
		if(cmbTerms.getSelectedItem().equals("Cash")) {
			fetchAccountNames("Cash");
			txtPartyName.setText("Cash");
		}
		else if(cmbTerms.getSelectedItem().equals("Credit")) {
			fetchAccountNames("Credit");
			txtPartyName.setText("Credit");
		}
	}//GEN-LAST:event_cmbTermsActionPerformed

	private void txtNetAmtKeyReleased(java.awt.event.KeyEvent evt) {                                      
		                                           
				columnNames.clear();
				data.clear();

				if(fieldsAreValidated()) {

					if(DBController.isDatabaseConnected()) {
						columnNames = DBController.getTableColumnNames(DatabaseCredentials.SALES_TABLE);

						// Remove the id as it is auto generated
						columnNames.remove(0);
					}
					else {
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

					data.add(lblGST.getText());

					data.add(lblPreviousBalance.getText());
					data.add(txtItemName.getText().trim());
					data.add(txthuid.getText().trim());
					if(!txtQty.getText().trim().isEmpty()) {
						data.add(txtQty.getText().trim());
					}
					else {
						columnNames.remove("qty");
					}

					if(!lblAddress.getText().isEmpty()) {
						data.add(lblAddress.getText());
					}
					else {
						columnNames.remove("address");
					}

					if(!txtBeedsWt.getText().trim().isEmpty()) {
						data.add(txtBeedsWt.getText().trim());
					}
					else {
						columnNames.remove("beedswt");
					}

					if(!txtNetWt.getText().trim().isEmpty()) {
						data.add(txtNetWt.getText().trim());
					}
					else {
						columnNames.remove("netwt");
					}

					if(!txtDiamondWt.getText().trim().isEmpty()) {
						data.add(txtDiamondWt.getText().trim());
					}
					else {
						columnNames.remove("diamondwt");
					}

					if(!txtDiamondRate.getText().trim().isEmpty()) {
						data.add(txtDiamondRate.getText().trim());
					}
					else {
						columnNames.remove("diamondrate");
					}

					if(!txtGrossWt.getText().trim().isEmpty()) {
						data.add(txtGrossWt.getText().trim());
					}
					else {
						columnNames.remove("grosswt");
					}

					if(!txtItemDescription.getText().trim().isEmpty()) {
						data.add(txtItemDescription.getText().trim());
					}
					else {
						columnNames.remove("itemdescription");
					}

					data.add(cmbPer.getSelectedItem());

					if(!txtExtraCharge.getText().trim().isEmpty()) {
						data.add(txtExtraCharge.getText().trim());
					}
					else {
						columnNames.remove("extrachange");
					}

					if(!txtBasicAmt.getText().trim().isEmpty()) {
						data.add(txtBasicAmt.getText().trim());
					}
					else {
						columnNames.remove("bankamt");
					}

					if(!txtNetAmt.getText().trim().isEmpty()) {
						data.add(txtNetAmt.getText().trim());
					}
					else {
						columnNames.remove("netamount");
					}

					if(!txtRate.getText().trim().isEmpty()) {
						data.add(txtRate.getText().trim());
					}
					else {
						columnNames.remove("rate");
					}

					if(!txtTaxableAmt.getText().trim().isEmpty()) {
						data.add("0");
					}
					else {
						data.add("0");
					}

					if(!txtGSTPercent.getText().trim().isEmpty()) {
						data.add(txtGSTPercent.getText().trim());
					}
					else {
						columnNames.remove("gstpercent");
					}

					if(!txtExtraCharge1.getText().trim().isEmpty()) {
						data.add(txtExtraCharge1.getText().trim());
					}
					else {
						columnNames.remove("labour");
					}
					if(!txtExtraCharge2.getText().trim().isEmpty()) {
						data.add(txtExtraCharge2.getText().trim());
					}
					else {
						columnNames.remove("labour_amt_discount");
					}
					if(!DBController.updateTableData(DatabaseCredentials.SALES_TABLE, 
							data, columnNames, "id", id)) {

						DBController.insertDataIntoTable(DatabaseCredentials.SALES_TABLE, 
								columnNames, data);

						//                setComponentsEnabledOrDisabled();

					}

					populateSalesListTable();
					fillgrandtotal();

					SaleRegisterScreen.populateSalesListTable();
					clearTextFields2();
					setBgTextFields();
					txtItemName.requestFocus();
					txtItemName.setBackground(Color.LIGHT_GRAY);
					fillgrandtotal();


				}

				id = -97108105;
			

	}                                     

	private void cmbTermsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTermsKeyReleased
		setFocus(evt, txtPartyName);
		setBgTextFields();
		txtPartyName.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_cmbTermsKeyReleased

	private void txtNetAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusGained

	}//GEN-LAST:event_txtNetAmtFocusGained

	private void lblDeleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDeleteButtonMouseClicked
		if(selectedRow != -1) {

			if(!DBController.isDatabaseConnected()) {
				DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
						DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
			}

			if (JOptionPane.showConfirmDialog(SaleScreenNoGST.this,
					"Are you sure you want delete the selected record", "Delete Record",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

				boolean rowDeleted
				= DBController.removeDataFromTable(DatabaseCredentials.SALES_TABLE,
						"id", tblPurchasesList.getValueAt(selectedRow, 0));

				if (rowDeleted) {
					populateSalesListTable();
					fillgrandtotal();
					JOptionPane.showMessageDialog(this, "Record deleted successfully");
				}

			}
		}
	}//GEN-LAST:event_lblDeleteButtonMouseClicked

	private void txtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyNameFocusLost
		pmPartyNameSuggestionsPopup.setVisible(false);
		txtBill.setText(Integer.toString(getBillNofromSales()));
	}//GEN-LAST:event_txtPartyNameFocusLost
	private int getBillNo() {
		if(!DBController.isDatabaseConnected()) {
			DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
					DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
		}

		List<Object> billNo = DBController.executeQuery("SELECT billno FROM " 
				+ DatabaseCredentials.SALE_BILL_NO_COUNTER_TABLE);
		if(billNo.get(0)!=null){
			return Integer.parseInt(billNo.get(0).toString());}

		return 0;

	}
	private void lblPrintButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPrintButtonMouseClicked
		printjrxml();
	}//GEN-LAST:event_lblPrintButtonMouseClicked
	private void printjrxml(){

            connect = DBConnect.connect();
		InputStream jasper1 = null;
		String u=txtBill.getText();
		String user= System.getProperty("user.dir") + "/jasper_reports/SaleBill.jrxml";

		File f = new File(user);

		try {

			jasper1 = new FileInputStream(f);

		} catch (FileNotFoundException ex) {
			Logger.getLogger(SaleScreenNoGST  .class.getName()).log(Level.SEVERE, null, ex);
		}
		String partyName=txtPartyName.getText();
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(jasper1);

			String query1="select itemname,huid,netwt,bankamt,labour,netamount from sales where bill = '"+u+"' and date = '" + UtilityMethods.getCurrentDate("yyyy-MM-dd") +"';";
			JRDesignQuery newQuery=new JRDesignQuery();
			newQuery.setText(query1);
			jasperDesign.setQuery(newQuery);

			Map<String,Object>hm = new HashMap<>();
			System.out.print(u);

			hm.put("customer_name", txtPartyName.getText());
			hm.put("bill_no",u);
			hm.put("state", lblState.getText());
			hm.put("total",jTextField8.getText());
			hm.put("date",dateFormat.format(datePurchaseDate.getDate()));
			File file1 = null;
			String icon=System.getProperty("user.dir");
			file1 = new File(icon);
			hm.put("path",file1.getPath());

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, connect);
			String reportsavepath= System.getProperty("user.dir") + "/SalesBills/"+"Sale_"+u+".pdf";

			JasperExportManager.exportReportToPdfFile(jasperPrint, reportsavepath);
			JasperViewer.viewReport(jasperPrint,false);    

		} catch (JRException ex) {
			Logger.getLogger(SaleScreenNoGST  .class.getName()).log(Level.SEVERE, null, ex);
		}

	}


	private void lblClearFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClearFieldsMouseClicked
		clearTextFields();
		clearLabels();
		salesListTableModel.setRowCount(0);
		lblGST.setText("0");
		txtGSTPercent.setText("0");
		txtTaxableAmt.setText("0");

	}//GEN-LAST:event_lblClearFieldsMouseClicked

	private void txtExtraCharge1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraCharge1KeyReleased
		setFocus(evt, cmbPer);
		setBgTextFields();
		cmbPer.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtExtraCharge1KeyReleased

	private void txtExtraCharge2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraCharge2KeyReleased
		setFocus(evt, txtExtraCharge);
		setBgTextFields();
		txtExtraCharge.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtExtraCharge2KeyReleased

	private void txtItemDescriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemDescriptionKeyTyped
		// TODO add your handling code here:
	}//GEN-LAST:event_txtItemDescriptionKeyTyped

	private void txtExtraChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraChargeFocusLost
		if(txtExtraCharge.getText().trim().isEmpty()) {
			txtExtraCharge.setText("0");
		}
	}//GEN-LAST:event_txtExtraChargeFocusLost

	private void txtExtraCharge2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge2FocusLost
		if(txtExtraCharge2.getText().trim().isEmpty()) {
			txtExtraCharge2.setText("0");
		}        // TODO add your handling code here:
	}//GEN-LAST:event_txtExtraCharge2FocusLost

	private void txtDiamondWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtFocusLost
		if(txtDiamondWt.getText().trim().isEmpty()) {
			txtDiamondWt.setText("0");
		}
	}//GEN-LAST:event_txtDiamondWtFocusLost

	private void txthuidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthuidActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txthuidActionPerformed

	private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
		try{
			int sno=0;
			Connection c = DBConnect.connect();
			Statement s = c.createStatement();

			ResultSet rs = s.executeQuery("select billno from salebillnocounter");
			while(rs.next()){
				sno = rs.getInt("billno");   
			}
			sno=sno+1;
			String q1="Update salebillnocounter set billno='"+sno+"'";
			s.executeUpdate(q1);


		}catch(Exception e){
			e.printStackTrace();
		}
		salesListTableModel.setRowCount(0);
		clearTextFields();
		clearLabels();
		lblGST.setText("0");
		txtGSTPercent.setText("0");
		txtTaxableAmt.setText("0");

	}//GEN-LAST:event_saveMouseClicked

	private void tblItemNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemNameSuggestionsMouseClicked
		if(evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
			txtItemName.setText(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
					.getSelectedRow(), 0).toString());
			setItemRate(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
					.getSelectedRow(), 0).toString());
			getHuid(itemNameSuggestionsTableModel.getValueAt(tblItemNameSuggestions
					.getSelectedRow(), 0).toString());
			txtGSTPercent.setText("0");
			txtQty.setText("1");
			txtGrossWt.requestFocusInWindow();
		}
	}//GEN-LAST:event_tblItemNameSuggestionsMouseClicked

	private void spTblItemNameSuggestionsContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_spTblItemNameSuggestionsContainerFocusGained
		JOptionPane.showMessageDialog(this, "Item name suggestions table 'focus gained'");
	}//GEN-LAST:event_spTblItemNameSuggestionsContainerFocusGained

	private void tblPartyNameSuggestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyNameSuggestionsMouseClicked
		if(evt.getClickCount() > 1 && evt.getClickCount() <= 2) {
			setPartyDetails(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
					.getSelectedRow(), 0).toString());
			txtPartyName.setText(partyNameSuggestionsTableModel.getValueAt(tblPartyNameSuggestions
					.getSelectedRow(), 0).toString());

			pmPartyNameSuggestionsPopup.setVisible(false);
		}
	}//GEN-LAST:event_tblPartyNameSuggestionsMouseClicked

	private void txtItemNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusLost
		pmItemNameSuggestionsPopup.setVisible(false);
	}//GEN-LAST:event_txtItemNameFocusLost

	private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_jTextField8ActionPerformed

	private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtQtyActionPerformed

	private void txtBasicAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicAmtKeyReleased
		setFocus(evt, txtNetAmt);
		setBgTextFields();
		txtNetAmt.setBackground(Color.LIGHT_GRAY);
	}//GEN-LAST:event_txtBasicAmtKeyReleased

	private void txtBasicAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusLost
		txtNetAmt.setText(txtBasicAmt.getText());
	}//GEN-LAST:event_txtBasicAmtFocusLost

	private void txtBasicAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBasicAmtFocusGained
		calculateBasicAmount();
	}//GEN-LAST:event_txtBasicAmtFocusGained

	private void txtDiamondRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateFocusLost
		if(txtDiamondRate.getText().trim().isEmpty()) {
			txtDiamondRate.setText("0");
		}        // TODO add your handling code here:
	}//GEN-LAST:event_txtDiamondRateFocusLost

	private void txtExtraCharge1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExtraCharge1FocusLost
		if(txtExtraCharge1.getText().trim().isEmpty()) {
			txtExtraCharge1.setText("0");
		}        // TODO add your handling code here:
	}//GEN-LAST:event_txtExtraCharge1FocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbPer;
    private javax.swing.JComboBox<String> cmbTerms;
    private com.toedter.calendar.JDateChooser datePurchaseDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JLabel lblAddPartyName;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAttachButton;
    private javax.swing.JLabel lblClearFields;
    private javax.swing.JLabel lblDeleteButton;
    private javax.swing.JLabel lblEditPartyName;
    private javax.swing.JTextField lblGST;
    private javax.swing.JLabel lblPreviousBalance;
    private javax.swing.JLabel lblPrintButton;
    private javax.swing.JLabel lblSaveButton;
    private javax.swing.JLabel lblState;
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
    private javax.swing.JTextField txtExtraCharge;
    private javax.swing.JTextField txtExtraCharge1;
    private javax.swing.JTextField txtExtraCharge2;
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
    private javax.swing.JTextField txthuid;
    // End of variables declaration//GEN-END:variables
}
