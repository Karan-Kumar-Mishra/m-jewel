package jewellery;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.SecureRandom;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author AR-LABS
 */
public class ItemEntryScreen extends javax.swing.JFrame {

    private List<Object> columnNames = new ArrayList<>();
    private final List<Object> data = new ArrayList<>();
    private int id;
    private String itemname;
    private ImageIcon imageIcon;
    private String selectedImage;
    private String selectedImageName;
    private String selectedImageSize;
    private String selectedImageLocation;
    private final DefaultTableModel itemsListTableModel;
    private int selectedRow = -1;
    private List<Object> fieldsData = new ArrayList<>();
    private final SecureRandom randomNumberGenerator = new SecureRandom();
    private int firstRandomNumber = 0;
    private int secondRandomNumber = 0;

    public ItemEntryScreen() {
        initComponents();

        itemsListTableModel = (DefaultTableModel) tblItemsList.getModel();

        centerTableCells();

//        populateItemsListTable();
        fillgrandtotal();
        setImageOnJLabel(lblEditFields, AssetsLocations.TEXT_FILE_EDIT_ICON);
        setImageOnJLabel(lblDelete, AssetsLocations.TRASH_CAN_ICON);
        setImageOnJLabel(lblList, AssetsLocations.NOTEBOOK_ICON);
        setImageOnJLabel(lblTaxSlabAdd, AssetsLocations.LIST_ADD_ICON);
        setImageOnJLabel(lblTaxSlabEdit, AssetsLocations.CONTENT_PEN_WRITE_ICON);
        setImageOnJLabel(lblItemGroupAdd, AssetsLocations.LIST_ADD_ICON);
        setImageOnJLabel(lblItemGroupEdit, AssetsLocations.CONTENT_PEN_WRITE_ICON);

      

        setComponentsEnabledOrDisabled();

        populateItemGroupComboBox();

        getTagCounter();

    }

    private int getTagCounter() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> counterValue = DBController.executeQuery("SELECT tag_counter "
                + "FROM " + DatabaseCredentials.TAG_COUNTER_TABLE);
int l=Integer.parseInt(counterValue.get(0).toString());
        return (l+1);
    }

//    private void resizeFrame() {
//        this.setSize(GLOBAL_VARS.tabbedPaneDimensions.width + 90,
//                GLOBAL_VARS.tabbedPaneDimensions.height);
//    }

    private void centerTableCells() {
        ((DefaultTableCellRenderer) tblItemsList
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setImageOnJLabel(javax.swing.JLabel component,
            String resourceLocation) {
        imageIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(resourceLocation))
                .getImage().getScaledInstance(component.getWidth() - 5,
                        component.getHeight() - 5, Image.SCALE_SMOOTH));
        component.setIcon(imageIcon);
    }

    public static void populateItemGroupComboBox() {
        cmbItemGroup.removeAllItems();

        cmbItemGroup.addItem("Gold 16 Carat");
        cmbItemGroup.addItem("Gold 18 Carat");
        cmbItemGroup.addItem("Gold 22 Carat");
        cmbItemGroup.addItem("Gold 24 Carat");
        cmbItemGroup.addItem("Silver");
    }

    private void setComponentsDisabled() {
        
    }
    
    private void setComponentsEnabledOrDisabled() {
        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {

                JTextComponent textComponent = (JTextComponent) component;

                if (component == txtItemName) {
                    textComponent.setEnabled(!textComponent.isEnabled());
                }
                if (component == txtItemPrefix) {
                    textComponent.setEnabled(!textComponent.isEnabled());
                }
                if (component == txtShortName) {
                    textComponent.setEnabled(!textComponent.isEnabled());
                }
                if (component == txtHSNCode) {
                    textComponent.setEnabled(!textComponent.isEnabled());
                }
                if (component == txtTagNo) {
                    textComponent.setEnabled(!textComponent.isEnabled());
                }

            }
        }
    }

    private ImageIcon getImageIcon(String fileLocation) {
        if (fileLocation != null && UtilityMethods.fileExists(fileLocation)) {
            ImagePlus img = IJ.openImage(fileLocation);
            ImagePlus resizedImage = img.resize(30, 30, null);
            return new ImageIcon(resizedImage.getImage());
        }

        return null;
    }
    
    public void ItemListsRedirect(String itemname) {
        txtItemName.setText(itemname);
        btnAddItem.setText("Update Items");
        populateItemsListTable();
    }


    private void populateItemsListTable() {
//        itemsListTableModel.setRowCount(0);
//        
//        List<List<Object>> items;
//        
//        if(!DBController.isDatabaseConnected()) {
//            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS, 
//                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
//        }
//        
////        items = DBController.getDataFromTable("SELECT "
////                + "itemimage, itemname, tagno, huid, grosswt, netwt, diamondwt, opqty "
////                + "FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE 
////                   + "WHERE itemname = "+"'"+txtItemName.getText()+"'");
//        items = DBController.getDataFromTable("SELECT "
//                + "itemimage, itemname, tagno, huid, grosswt, netwt, diamondwt, opqty "
//                + "FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE 
//                   + " WHERE "+ "'"+ "  itemname= "+"'" + txtItemName.getText() + "'");
//        
//        items.forEach((item) -> {
//            itemsListTableModel.addRow(new Object[]{
//                (item.get(0) == null || item.get(0).toString().isEmpty()) ? "NULL" : getImageIcon(item.get(0).toString()), // item image
//                (item.get(1) == null || item.get(1).toString().isEmpty()) ? "NULL" : item.get(1), // itemname
//                (item.get(2) == null || item.get(2).toString().isEmpty()) ? "NULL" : item.get(2), // tagno
//                (item.get(3) == null || item.get(3).toString().isEmpty()) ? "NULL" : item.get(3), // huid
//                (item.get(4) == null || item.get(4).toString().isEmpty()) ? "NULL" : item.get(4), // beeds wt
//                (item.get(5) == null || item.get(5).toString().isEmpty()) ? "NULL" : item.get(5), // gross wt
//                (item.get(6) == null || item.get(6).toString().isEmpty()) ? "NULL" : item.get(6), // net wt
//                (item.get(7) == null || item.get(7).toString().isEmpty()) ? "NULL" : item.get(7), // opqty
//            });
//        });

        DefaultTableModel m = (DefaultTableModel) tblItemsList.getModel();
        m.setRowCount(0);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT itemimage, itemname, tagno, huid, grosswt, netwt, diamondwt, opqty FROM entryitem where itemname = '" + txtItemName.getText() + "';");
            while (rs.next()) {
                String image = rs.getString("itemimage");
                String name = rs.getString("itemname");
                String tag = rs.getString("tagno");
                String ID = rs.getString("huid");
                String gross = rs.getString("grosswt");
                String net = rs.getString("netwt");
                String diamond = rs.getString("diamondwt");
                String op = rs.getString("opqty");
                m.addRow(new Object[]{image, name, tag, ID, gross, net, diamond, op});
            }
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int g = 0; g < tblItemsList.getColumnCount(); g++) {
                tblItemsList.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fillgrandtotal();
//        if(tblItemsList.getRowCount() > 0) {
//            List<Object> aggregateFuncDataHolder = DBController.executeQuery("SELECT "
//                    + "COUNT(tagno) FROM " + DatabaseCredentials.ENTRY_ITEM_TABLE);
//            txtTotalNoOfTags.setText(aggregateFuncDataHolder.get(0).toString());
//            aggregateFuncDataHolder.clear();
//
//            aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(netwt) FROM "
//                    + DatabaseCredentials.ENTRY_ITEM_TABLE);
//            
////            if(aggregateFuncDataHolder.get(0).toString().length() > 6) {
////                txtTotalNetWt.setText(aggregateFuncDataHolder.get(0).toString().substring(0, 6));
////            }
////            else {
////                txtTotalNetWt.setText(aggregateFuncDataHolder.get(0).toString());
////            }
//            txtTotalNetWt.setText(aggregateFuncDataHolder.get(0).toString());
//            
//            aggregateFuncDataHolder.clear();
//            
//            aggregateFuncDataHolder = DBController.executeQuery("SELECT SUM(opqty) FROM "
//                    + DatabaseCredentials.ENTRY_ITEM_TABLE);
//            txtTotalOpQty.setText(aggregateFuncDataHolder.get(0).toString());
//            aggregateFuncDataHolder.clear();
//        }

    }

    private boolean normalCaseValidation() {
        if (UtilityMethods.isTextFieldEmpty(txtItemName)
                || !UtilityMethods.inputContainsAlphabetsAndNumbers(txtItemName.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter item name");
            return false;
        }
        return true;
    }

    public void fillgrandtotal() {
        double netwt = 0.0, grosswt = 0.0, netAmt = 0, BasicAmt = 0, taxAmt = 0, billAmt = 0, txtqty = 0, lbrAmt = 0;
        txtTotalNoOfTags.setText(Integer.toString(itemsListTableModel.getRowCount()));
//		txtBillNo.setText(Integer.toString(purchasesListTableModel.getRowCount()));

        for (int i = 0; i < itemsListTableModel.getRowCount(); i++) {
//			netAmt+=Double.parseDouble(purchasesListTableModel.getValueAt(i, 8).toString());
//			lbrAmt+=Double.parseDouble(purchasesListTableModel.getValueAt(i, 6).toString());
//			grosswt+=Double.parseDouble(purchasesListTableModel.getValueAt(i, 3).toString());  
//			BasicAmt+=Double.parseDouble(purchasesListTableModel.getValueAt(i, 6).toString());
            txtqty += Double.parseDouble(itemsListTableModel.getValueAt(i, 7).toString());
            netwt += Double.parseDouble(itemsListTableModel.getValueAt(i, 5).toString());

        }

        txtTotalNetWt.setText(Double.toString(netwt));
//		txtDiscount.setText(Double.toString(totalDis));
//		txtTotalQty.setText(Double.toString(txtqty));
        txtTotalOpQty.setText(Double.toString(txtqty));
//		txtBillAmt.setText(Double.toString(billAmt));
//		txttaxable.setText(Double.toString(BasicAmt));
//		txtTaxableAmt.setText(Double.toString(BasicAmt));
//		txtTaxAmt.setText(Double.toString(taxAmt));
//		txttotal.setText(Double.toString(netAmt));

    }

    private boolean maintainTagValidation() {
        if (UtilityMethods.isTextFieldEmpty(txtItemName)
                || !UtilityMethods.inputContainsAlphabetsAndNumbers(txtItemName.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter item name");
            return false;
        } else if (UtilityMethods.isTextFieldEmpty(txtGrossWt)
                || !UtilityMethods.inputOnlyContainsNumbers(txtGrossWt.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter gross wt correctly");
            return false;
        }
//        else if(UtilityMethods.isTextFieldEmpty(txtBeedsWt) || 
//                !UtilityMethods.inputOnlyContainsNumbers(txtBeedsWt.getText())) {
//            JOptionPane.showMessageDialog(this, "Please enter beeds wt correctly");
//            return false;
//        }
//        else if(UtilityMethods.isTextFieldEmpty(txtNetWt) || 
//                !UtilityMethods.inputOnlyContainsNumbers(txtNetWt.getText())) {
//            JOptionPane.showMessageDialog(this, "Please enter Net wt correctly");
//            return false;
//        }
//        else if(UtilityMethods.isTextFieldEmpty(txtDiamondWtInCarat) || 
//                !UtilityMethods.inputOnlyContainsNumbers(txtDiamondWtInCarat.getText())) {
//            JOptionPane.showMessageDialog(this, "Please enter diamond wt in carat correctly");
//            return false;
//        }
//        else if(UtilityMethods.isTextFieldEmpty(txtDiamondRateInCarat) || 
//                !UtilityMethods.inputOnlyContainsNumbers(txtDiamondRateInCarat.getText())) {
//            JOptionPane.showMessageDialog(this, "Please enter diamond rate per carat correctly");
//            return false;
//        }
//        else if(UtilityMethods.isTextFieldEmpty(txtPolishPercent) || 
//                !UtilityMethods.inputOnlyContainsNumbers(txtPolishPercent.getText())) {
//            JOptionPane.showMessageDialog(this, "Please enter polish percentage correctly");
//            return false;
//        }
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

    private void emptyTextFieldAddBtn() {
        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {

            if (component instanceof JTextField) {
                if (component == txtItemName) {
                    continue;
                }
                if (component == txtItemPrefix) {
                    continue;
                }
                if (component == txtShortName) {
                    continue;
                }
                if (component == txtHSNCode) {
                    continue;
                }
//            	if(component == txtTagNo)
//            		continue;
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
            }
        }
    }

    private void addTagPrintingData() {

    }

    private void addItem() throws FileNotFoundException {
        columnNames.clear();
        data.clear();

//        String newItemName = txtItemName.getText();
//        try {
//             Connection con = DriverManager.getConnection(DatabaseCredentials.DB_ADDRESS, DatabaseCredentials.DB_USERNAME,
//                     DatabaseCredentials.DB_PASSWORD);
//            Statement st = con.createStatement();
//            String query = "SELECT id FROM entryitem WHERE itemname = '" + newItemName + "';";
//            ResultSet rs = st.executeQuery(query);
//            int lastId = -100;
//            while(rs.next()) {
//                lastId = rs.getInt("id");
//            }
//            con.close();
//            rs.close();
//            st.close();
//            if(lastId != -100) {
//                JFrame f = new JFrame();
//                JOptionPane.showMessageDialog(f, "Item Name Already Exists !");
//                return;
//            }
//        }
//        catch(SQLException e) {
//            java.util.logging.Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, e);
//        }
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME,
                    DatabaseCredentials.DB_PASSWORD);
        }

        columnNames = DBController.getTableColumnNames(DatabaseCredentials.ENTRY_ITEM_TABLE);

        // Remove the account id as it is auto generated
        columnNames.remove(0);

        data.add(txtItemName.getText().trim());

        if (!txtItemPrefix.getText().trim().isEmpty()) {
            data.add(txtItemPrefix.getText().trim());
        } else {
            columnNames.remove("itemprefix");
        }
        data.add(cmbItemGroup.getSelectedItem());
        data.add(cmbTaxSlab.getSelectedItem());

        if (!txtHSNCode.getText().trim().isEmpty()) {
            data.add(txtHSNCode.getText().trim());
        } else {
            columnNames.remove("hsncode");
        }

        if (!txtShortName.getText().trim().isEmpty()) {
            data.add(txtShortName.getText().trim());
        } else {
            columnNames.remove("shortname");
        }

        // Tag no
        data.add(txtTagNo.getText());

        if (!txtHUID.getText().trim().isEmpty()) {
            data.add(txtHUID.getText().trim());
        } else {
            columnNames.remove("huid");
        }

        if (!txtGrossWt.getText().trim().isEmpty()) {
            data.add(txtGrossWt.getText().trim());
        } else {
            columnNames.remove("grosswt");
        }

        if (!txtBeedsWt.getText().trim().isEmpty()) {
            data.add(txtBeedsWt.getText().trim());
        } else {
            columnNames.remove("beedswt");
        }

        if (!txtNetWt.getText().trim().isEmpty()) {
            data.add(txtNetWt.getText().trim());
        } else {
            data.add("0");
        }

        if (!txtDiamondWtInCarat.getText().trim().isEmpty()) {
            data.add(txtDiamondWtInCarat.getText().trim());
        } else {
            columnNames.remove("diamondwt");
        }

        if (!txtDiamondRateInCarat.getText().trim().isEmpty()) {
            data.add(txtDiamondRateInCarat.getText().trim());
        } else {
            columnNames.remove("carats");
        }

        if (!txtPolishPercent.getText().trim().isEmpty()) {
            data.add(txtPolishPercent.getText().trim());
        } else {
            columnNames.remove("polishpercent");
        }

        if (!txtOpQty.getText().trim().isEmpty()) {
            data.add(txtOpQty.getText().trim());
        } else {
            data.add("0");
        }

        data.add(cmbOpQtyType.getSelectedItem().toString());

        if (selectedImageLocation != null && !selectedImageLocation.isEmpty()) {
            data.add(selectedImageLocation);
        } else {
            columnNames.remove("itemimage");
        }
        columnNames.remove("item_sold");

        boolean itemUpdated = DBController.updateTableData(DatabaseCredentials.ENTRY_ITEM_TABLE,
                data, columnNames, "id", id);

        if (itemUpdated) {
            JOptionPane.showMessageDialog(this, "Item data updated successfully");
        } else if (!itemUpdated) {
            boolean insertionResult
                    = DBController.insertDataIntoTable(DatabaseCredentials.ENTRY_ITEM_TABLE,
                            columnNames, data);

            if (insertionResult) {
                if (!txtTagNo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Item added in the records successfully");

                    PurchaseScreen.fetchItemNames();

                    List<Object> counterTableData = new ArrayList<>();
                    counterTableData.add(getTagCounter());

                    List<Object> counterTableColumnNames
                            = DBController.getTableColumnNames(DatabaseCredentials.TAG_COUNTER_TABLE);

                    // remove the tag_counter_id columns from column names
                    counterTableColumnNames.remove(0);

                    DBController.updateTableData(DatabaseCredentials.TAG_COUNTER_TABLE,
                            counterTableData, counterTableColumnNames, "tag_counter_id", "1");
                }
            }
        }

//        if (!DBController.updateTableData(DatabaseCredentials.ENTRY_ITEM_TABLE,
//                data, columnNames, "id", id)) {
//
//            boolean insertionResult
//                    = DBController.insertDataIntoTable(DatabaseCredentials.ENTRY_ITEM_TABLE, 
//                            columnNames, data);
//
//            if (insertionResult) {
//                if (!txtTagNo.getText().trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Item added in the records successfully");
//
//                    List<Object> counterTableData = new ArrayList<>();
//                    counterTableData.add(getTagCounter() + 1);
//
//                    List<Object> counterTableColumnNames
//                            = DBController.getTableColumnNames(DatabaseCredentials.TAG_COUNTER_TABLE);
//
//                    // remove the tag_counter_id columns from column names
//                    counterTableColumnNames.remove(0);
//
//                    DBController.updateTableData(DatabaseCredentials.TAG_COUNTER_TABLE,
//                            counterTableData, counterTableColumnNames, "tag_counter_id", "1");
//                }
//            }
//        }
        populateItemsListTable();
        emptyTextFieldAddBtn();

        lblItemPic.setIcon(null);

        setComponentsEnabledOrDisabled();
        txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
        txtHUID.requestFocusInWindow();
//        cbMaintainTags.setSelected(false);

        id = -97108105;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlRootContainer = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        txtItemPrefix = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbItemGroup = new javax.swing.JComboBox<>();
        cmbTaxSlab = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtHSNCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtShortName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbMaintainTags = new javax.swing.JCheckBox();
        cbMaintainTags.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        		
        	}
        });
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTagNo = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtGrossWt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtBeedsWt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtNetWt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDiamondWtInCarat = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDiamondRateInCarat = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtPolishPercent = new javax.swing.JTextField();
        lblItemPic = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtOpQty = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItemsList = tblItemsList = new javax.swing.JTable() {
            public Class getColumnClass(int column) {
                return (column == 0) ? Icon.class : Object.class;
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDelete = new javax.swing.JLabel();
        lblList = new javax.swing.JLabel();
        btnSelectImage = new javax.swing.JButton();
        btnClearFields = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtHUID = new javax.swing.JTextField();
        lblTaxSlabAdd = new javax.swing.JLabel();
        lblTaxSlabEdit = new javax.swing.JLabel();
        cmbOpQtyType = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtTotalNetWt = new javax.swing.JTextField();
        txtTotalNoOfTags = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtTotalOpQty = new javax.swing.JTextField();
        lblEditFields = new javax.swing.JLabel();
        lblItemGroupAdd = new javax.swing.JLabel();
        lblItemGroupEdit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Item Name");

        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });

        txtItemPrefix.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemPrefixKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Item Prefix");

        cmbItemGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbItemGroupKeyReleased(evt);
            }
        });

        cmbTaxSlab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3%" }));
        cmbTaxSlab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbTaxSlabKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("HSN Code");

        txtHSNCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHSNCodeKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Short Name");

        txtShortName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtShortNameKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Item Group");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tax Slab");

        cbMaintainTags.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cbMaintainTags.setText("Maintain Tags");
        cbMaintainTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMaintainTagsActionPerformed(evt);
            }
        });
        cbMaintainTags.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbMaintainTagsKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 0, 0));
        jLabel9.setText("Enter Tag No. Available For This Item With Opening Book");

        jLabel10.setText("---------------------------------------------------------------------");

        jLabel11.setText("-------------------------------------------------------------------------------------------");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Tag No.");

        txtTagNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTagNoKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setText("Gross Wt.");

        txtGrossWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGrossWtKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setText("Beeds Wt.");

        txtBeedsWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBeedsWtKeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setText("Net Wt.");

        txtNetWt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetWtKeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setText("Diamond Wt. in carat");

        txtDiamondWtInCarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondWtInCaratKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText("Diamond rate / carat");

        txtDiamondRateInCarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondRateInCaratKeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setText("Polish %");

        txtPolishPercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPolishPercentKeyReleased(evt);
            }
        });

        lblItemPic.setBackground(new java.awt.Color(204, 204, 204));
        lblItemPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemPic.setText("512 x 512, 30KB");
        lblItemPic.setOpaque(true);

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setText("Op. Qty");

        txtOpQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOpQtyKeyReleased(evt);
            }
        });

        btnAddItem.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAddItem.setText("Add Item");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });
        btnAddItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnAddItemKeyReleased(evt);
            }
        });

        tblItemsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Image", "Item Name", "Tag No.", "HUID", "Gross Wt.", "Net Wt.", "Diamond. Wt.", "Op. Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemsList.setRowHeight(36);
        tblItemsList.getTableHeader().setReorderingAllowed(false);
        tblItemsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemsListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItemsList);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 153, 51));
        jLabel1.setText("Item Entry");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/goldenRingImage.png")));

        lblDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDelete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDeleteMouseClicked(evt);
            }
        });

        lblList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblListMouseClicked(evt);
            }
        });

        btnSelectImage.setText("Select Image");
        btnSelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectImageActionPerformed(evt);
            }
        });

        btnClearFields.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClearFields.setText("Clear Fields");
        btnClearFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFieldsActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("HUID");

        txtHUID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHUIDKeyReleased(evt);
            }
        });

        lblTaxSlabAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaxSlabAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTaxSlabAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaxSlabAddMouseClicked(evt);
            }
        });

        lblTaxSlabEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaxSlabEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTaxSlabEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaxSlabEditMouseClicked(evt);
            }
        });

        cmbOpQtyType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pair", "Peice" }));
        cmbOpQtyType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbOpQtyTypeKeyReleased(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel20.setText("Total No. of tags :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        jPanel3.add(jLabel20, gridBagConstraints);

        txtTotalNetWt.setEditable(false);
        txtTotalNetWt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 131;
        gridBagConstraints.ipady = 9;
        jPanel3.add(txtTotalNetWt, gridBagConstraints);

        txtTotalNoOfTags.setEditable(false);
        txtTotalNoOfTags.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 131;
        gridBagConstraints.ipady = 9;
        jPanel3.add(txtTotalNoOfTags, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel21.setText("Total Net Wt. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 8);
        jPanel3.add(jLabel21, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel23.setText("Total Op. Qty :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 8);
        jPanel3.add(jLabel23, gridBagConstraints);

        txtTotalOpQty.setEditable(false);
        txtTotalOpQty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 131;
        gridBagConstraints.ipady = 9;
        jPanel3.add(txtTotalOpQty, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        jPanel2.add(jPanel3, gridBagConstraints);

        lblEditFields.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEditFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblEditFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditFieldsMouseClicked(evt);
            }
        });

        lblItemGroupAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemGroupAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblItemGroupAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItemGroupAddMouseClicked(evt);
            }
        });

        lblItemGroupEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemGroupEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblItemGroupEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItemGroupEditMouseClicked(evt);
            }
        });
        
//        JButton btnClose = new JButton();
//        btnClose.addMouseListener(new MouseAdapter() {
//        	@Override
//        	public void mouseClicked(MouseEvent e) {
//        		DashBoardScreen.tpScreensHolder.setSelectedIndex(0);
//        	}
//        });
//        btnClose.setBackground(new Color(204, 0, 0));
//        btnClose.setText("Close");
//        btnClose.setFont(new Font("Times New Roman", Font.BOLD, 14));

        javax.swing.GroupLayout pnlRootContainerLayout = new javax.swing.GroupLayout(pnlRootContainer);
        pnlRootContainerLayout.setHorizontalGroup(
        	pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlRootContainerLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addComponent(jLabel11)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel9)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 314, Short.MAX_VALUE)
        					.addGap(62))
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGap(2)
        							.addComponent(lblItemPic, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
        						.addComponent(btnSelectImage, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGap(18)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel13, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtTagNo, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
        							.addGap(8)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtHUID, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        							.addGap(10)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtGrossWt, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
        							.addGap(14)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtBeedsWt, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(txtNetWt, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(txtDiamondWtInCarat)
        								.addComponent(jLabel17, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addGroup(pnlRootContainerLayout.createSequentialGroup()
        									.addGap(17)
        									.addComponent(jLabel18, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
        								.addGroup(pnlRootContainerLayout.createSequentialGroup()
        									.addPreferredGap(ComponentPlacement.UNRELATED)
        									.addComponent(txtDiamondRateInCarat, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))))
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGap(83)
        							.addComponent(btnClearFields, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(btnAddItem, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(jLabel22, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        								.addComponent(txtOpQty))
        							.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
        							.addComponent(cmbOpQtyType, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING)
        								.addComponent(jLabel19, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtPolishPercent, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
        							.addGap(0, 75, Short.MAX_VALUE)))
        					.addGap(51))
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel7)
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING)
        								.addComponent(txtHSNCode, 174, 174, 174)
        								.addGroup(pnlRootContainerLayout.createSequentialGroup()
        									.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        									.addGap(0, 104, Short.MAX_VALUE)))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel5)
        								.addComponent(txtShortName, 177, 177, 177)))
        						.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING, false)
        							.addComponent(txtItemName, Alignment.LEADING)
        							.addGroup(Alignment.LEADING, pnlRootContainerLayout.createSequentialGroup()
        								.addGap(13)
        								.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
        								.addGap(44)
        								.addComponent(jLabel2))))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
        							.addComponent(lblEditFields, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(lblDelete, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(lblList, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
        							.addGap(18))
        						.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        							.addGroup(pnlRootContainerLayout.createSequentialGroup()
        								.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        									.addComponent(jLabel3)
        									.addComponent(txtItemPrefix, 259, 259, 259))
        								.addGap(173)
        								.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        									.addComponent(cmbItemGroup, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
        									.addComponent(jLabel6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
        								.addGap(18))
        							.addGroup(pnlRootContainerLayout.createSequentialGroup()
        								.addComponent(cbMaintainTags, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
        								.addPreferredGap(ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
        								.addComponent(lblItemGroupAdd, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        								.addPreferredGap(ComponentPlacement.RELATED)
        								.addComponent(lblItemGroupEdit, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        								.addGap(41))))
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
//        						.addComponent(btnClose, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        						.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING, false)
        							.addComponent(cmbTaxSlab, 0, 83, Short.MAX_VALUE)
        							.addGroup(Alignment.TRAILING, pnlRootContainerLayout.createSequentialGroup()
        								.addComponent(lblTaxSlabAdd, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        								.addPreferredGap(ComponentPlacement.RELATED)
        								.addComponent(lblTaxSlabEdit, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        							.addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
        					.addGap(69))
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(jScrollPane1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 1062, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE))
        					.addGap(37))))
        );
        pnlRootContainerLayout.setVerticalGroup(
        	pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlRootContainerLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(lblEditFields, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
        					.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
        					.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        				.addComponent(lblDelete, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
        				.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING, false)
//        					.addComponent(btnClose, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(lblList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)))
        			.addGap(24)
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel7)
        				.addComponent(jLabel3)
        				.addComponent(jLabel6)
        				.addComponent(jLabel8))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(txtItemPrefix, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(cmbItemGroup, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        				.addComponent(cmbTaxSlab, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txtItemName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel4)
        						.addComponent(jLabel5))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(cbMaintainTags, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        						.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        							.addComponent(txtHSNCode, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        							.addComponent(txtShortName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
        				.addComponent(lblTaxSlabAdd, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblTaxSlabEdit, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblItemGroupAdd, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblItemGroupEdit, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel9)
        				.addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addComponent(lblItemPic, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnSelectImage, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
        				.addGroup(pnlRootContainerLayout.createSequentialGroup()
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(jLabel13)
        								.addComponent(jLabel14)
        								.addComponent(jLabel15)
        								.addComponent(jLabel16)
        								.addComponent(jLabel17)
        								.addComponent(jLabel18)
        								.addComponent(jLabel12))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(txtBeedsWt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        								.addComponent(txtNetWt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        								.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        									.addComponent(txtDiamondWtInCarat, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        									.addComponent(txtDiamondRateInCarat, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        								.addComponent(txtTagNo, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        								.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        									.addComponent(txtGrossWt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        									.addComponent(txtHUID, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addComponent(jLabel22, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(txtOpQty, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        								.addComponent(cmbOpQtyType, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
        					.addGap(20)
        					.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(pnlRootContainerLayout.createParallelGroup(Alignment.BASELINE)
        							.addComponent(btnClearFields)
        							.addComponent(btnAddItem))
        						.addGroup(pnlRootContainerLayout.createSequentialGroup()
        							.addComponent(jLabel19)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtPolishPercent, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
        					.addGap(0, 20, Short.MAX_VALUE)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlRootContainer.setLayout(pnlRootContainerLayout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlRootContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        if (cbMaintainTags.isSelected()) {
            if (maintainTagValidation()) {
                try {
                    addItem();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            
            if(tblItemsList.getSelectedRowCount()==0){
            txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());    
            }
            
            if (normalCaseValidation()) {
                try {
                    addItem();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnAddItemActionPerformed
//private void tblItemsListMouseClicked(java.awt.event.MouseEvent evt){
//    if(evt.getClickCount()==1){
//        JOptionPane.showMessageDialog(this,"enter");
//    }
//}
    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImageActionPerformed
        String[] selectedFileWithFileNameAndSize
                = UtilityMethods.selectFileAndReturnLocation("*.images", "png");

        if (selectedFileWithFileNameAndSize != null) {
            selectedImage = selectedFileWithFileNameAndSize[0];
            selectedImageName = selectedFileWithFileNameAndSize[1];
            selectedImageSize = selectedFileWithFileNameAndSize[2];
        }

        if (selectedImageSize != null && Double.valueOf(selectedImageSize) > 30) {
            JOptionPane.showMessageDialog(this, "Selected Image is larger than"
                    + "30kb, \n Maximum allowed size is 30kb");
            return;
        }

        if (!(selectedImage.trim().isEmpty() || selectedImage == null)) {
            ImagePlus image = IJ.openImage(selectedImage);
            ImagePlus resizedImage = image.resize(lblItemPic.getWidth(),
                    lblItemPic.getHeight(), null);
            lblItemPic.setText("");
            lblItemPic.setIcon(new ImageIcon(resizedImage.getImage()));

            firstRandomNumber = randomNumberGenerator.nextInt(10000);
            secondRandomNumber = randomNumberGenerator.nextInt(10000);

            new File(System.getProperty("user.dir") + "\\assets").mkdir();
            new FileSaver(image).saveAsPng(System.getProperty("user.dir")
                    + "\\assets\\" + firstRandomNumber + secondRandomNumber
                    + selectedImageName);

            selectedImageLocation = System.getProperty("user.dir")
                    + "\\assets\\" + firstRandomNumber + secondRandomNumber
                    + selectedImageName;
        }
    }//GEN-LAST:event_btnSelectImageActionPerformed

    private void lblDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDeleteMouseClicked

        int row = tblItemsList.getSelectedRow();
        String update;

        JFrame frame1 = new JFrame();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame1, "Please Select One Item from table");
        }

        int opt = JOptionPane.showConfirmDialog(null, "Are You sure you want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            try {
                String q;
                java.sql.Connection con = DBConnect.connect();
                java.sql.Statement stmt = con.createStatement();

                if (!tblItemsList.getValueAt(selectedRow, 2).equals("NULL")) {
                    update = tblItemsList.getValueAt(row, 2).toString();
                    q = "DELETE FROM entryitem where tagno = '" + update + "';";
                } else {
                    update = tblItemsList.getValueAt(row, 1).toString();
                    q = "DELETE FROM entryitem where itemname = '" + update + "';";
                }

                stmt.executeUpdate(q);

                stmt.close();
                con.close();
                JFrame f;
                f = new JFrame();
                JOptionPane.showMessageDialog(f, "Item is deleted successfully!");
                populateItemsListTable();

            } catch (SQLException ex) {
                JFrame f;
                f = new JFrame();
                JOptionPane.showMessageDialog(f, "Internal Server Error!!!");
                System.out.print("Error");
            }
        }

        emptyTextFields();

        lblItemPic.setIcon(null);
        lblItemPic.setText("512 x 512, 30KB");

        id = -97108105;


    }//GEN-LAST:event_lblDeleteMouseClicked

    private void lblListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblListMouseClicked
        JOptionPane.showMessageDialog(this, "List button was clicked");
    }//GEN-LAST:event_lblListMouseClicked

    private void tblItemsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsListMouseClicked
       
        try{
           selectedRow = tblItemsList.getSelectedRow();
        emptyTextFields();

        if (!tblItemsList.getValueAt(selectedRow, 2).equals("NULL")) {
            fieldsData = DBController.executeQuery("SELECT * FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE tagno = "
                    + "'" + tblItemsList.getValueAt(selectedRow, 2) + "'");
        } else {
            fieldsData = DBController.executeQuery("SELECT * FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = "
                    + "'" + tblItemsList.getValueAt(selectedRow, 1) + "'");
        }

        id = Integer.valueOf(fieldsData.remove(0).toString());
        itemname = fieldsData.get(0).toString();

        txtItemName.setText(fieldsData.get(0).toString());

        if (fieldsData.get(1) != null) {
            txtItemPrefix.setText(fieldsData.get(1).toString());
        }

        try {
            cmbItemGroup.setSelectedItem(fieldsData.get(2).toString());
        } catch (NullPointerException ex) {
            // For now leave this empty
            // TODO: handle the exception
        }

        cmbTaxSlab.setSelectedItem(fieldsData.get(3).toString());

        if (fieldsData.get(4) != null) {
            txtHSNCode.setText(fieldsData.get(4).toString());
        }

        if (fieldsData.get(5) != null) {
            txtShortName.setText(fieldsData.get(5).toString());
        }

        if (fieldsData.get(6) != null) {
            txtTagNo.setText(fieldsData.get(6).toString());
        }

        if (fieldsData.get(7) != null) {
            txtHUID.setText(fieldsData.get(7).toString());
        }

        if (fieldsData.get(8) != null) {
            txtGrossWt.setText(fieldsData.get(8).toString());
        }

        if (fieldsData.get(9) != null) {
            txtBeedsWt.setText(fieldsData.get(9).toString());
        }

        if (fieldsData.get(10) != null) {
            txtNetWt.setText(fieldsData.get(10).toString());
        }

        if (fieldsData.get(11) != null) {
            txtDiamondWtInCarat.setText(fieldsData.get(11).toString());
        }

        if (fieldsData.get(12) != null) {
            txtDiamondRateInCarat.setText(fieldsData.get(12).toString());
        }

        if (fieldsData.get(13) != null) {
            txtPolishPercent.setText(fieldsData.get(13).toString());
        }

        if (fieldsData.get(14) != null) {
            txtOpQty.setText(fieldsData.get(14).toString());
        }

        cmbOpQtyType.setSelectedItem(fieldsData.get(15).toString());

        lblItemPic.setIcon(null);

        if (fieldsData.get(16) != null) {
            if (UtilityMethods.fileExists(fieldsData.get(16).toString())) {
                ImagePlus image = IJ.openImage(fieldsData.get(16).toString());
                ImagePlus resizedImage = image.resize(lblItemPic.getWidth(),
                        lblItemPic.getHeight(), null);
                lblItemPic.setText("");
                lblItemPic.setIcon(new ImageIcon(resizedImage.getImage()));
            }
        } else if (lblItemPic.getText().isEmpty()) {
            lblItemPic.setText("512 x 512, 30KB");
        }
        }catch(Exception e){
           Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, e);
        }
 
    }//GEN-LAST:event_tblItemsListMouseClicked

    private void btnClearFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFieldsActionPerformed
        emptyTextFields();

        lblItemPic.setIcon(null);
        lblItemPic.setText("512 x 512, 30KB");

        id = -97108105;
    }//GEN-LAST:event_btnClearFieldsActionPerformed

    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtItemPrefix.requestFocusInWindow();
            setBg();
            txtItemPrefix.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtItemNameKeyReleased
    private void setBg() {

        Component[] components = this.pnlRootContainer.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setBackground(Color.white);
            }
        }

    }
    private void lblEditFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditFieldsMouseClicked
        setComponentsEnabledOrDisabled();
    }//GEN-LAST:event_lblEditFieldsMouseClicked

    private void txtItemPrefixKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemPrefixKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbItemGroup.requestFocusInWindow();
            setBg();
            cmbItemGroup.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtItemPrefixKeyReleased

    private void cmbItemGroupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemGroupKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbTaxSlab.requestFocusInWindow();
            setBg();
            cmbTaxSlab.setBackground(Color.LIGHT_GRAY);
        }

    }//GEN-LAST:event_cmbItemGroupKeyReleased

    private void txtDiamondRateInCaratFocusLost(java.awt.event.FocusEvent evt) {
        if (txtDiamondRateInCarat.getText().trim().isEmpty()) {
            txtDiamondRateInCarat.setText("0");
        }        // TODO add your handling code here:
    }

    private void txtDiamondWtInCaratFocusLost(java.awt.event.FocusEvent evt) {
        if (txtDiamondWtInCarat.getText().trim().isEmpty()) {
            txtDiamondWtInCarat.setText("0");
        }        // TODO add your handling code here:
    }

    private void txtOpQtyFocusLost(java.awt.event.FocusEvent evt) {
        if (txtOpQty.getText().trim().isEmpty()) {
            txtOpQty.setText("0");
        }        // TODO add your handling code here:
    }
    private void cmbTaxSlabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxSlabKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtHSNCode.requestFocusInWindow();
            setBg();
            txtHSNCode.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_cmbTaxSlabKeyReleased

    private void txtHSNCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHSNCodeKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtShortName.requestFocusInWindow();
            setBg();
            txtShortName.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtHSNCodeKeyReleased

    private void txtShortNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShortNameKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbMaintainTags.requestFocusInWindow();
            setBg();
//			cbMaintainTags.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtShortNameKeyReleased

    private void cbMaintainTagsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbMaintainTagsKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTagNo.requestFocusInWindow();
            setBg();
            txtTagNo.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_cbMaintainTagsKeyReleased

    private void txtTagNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTagNoKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtHUID.requestFocusInWindow();
            setBg();
            txtHUID.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtTagNoKeyReleased

    private void txtHUIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHUIDKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtGrossWt.requestFocusInWindow();
            setBg();
            txtGrossWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtHUIDKeyReleased

    private void txtGrossWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossWtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBeedsWt.requestFocusInWindow();
            setBg();
            txtBeedsWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtGrossWtKeyReleased

    private void txtBeedsWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBeedsWtKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!(txtGrossWt.getText().trim().isEmpty() || txtBeedsWt.getText().trim().isEmpty())) {

                double grossWeight = Double.parseDouble(txtGrossWt.getText());
                double beedsWeight = Double.parseDouble(txtBeedsWt.getText());

                if ((grossWeight - beedsWeight) < 0) {
                    txtNetWt.setText("0");
                } else {
                    txtNetWt.setText(String.format("%.2f", (grossWeight - beedsWeight)));
                }

            } else if (txtGrossWt.getText().trim().isEmpty()) {
                txtGrossWt.setText("0");
                txtBeedsWt.setText("0");
                txtNetWt.setText("0");
            } else if (txtBeedsWt.getText().trim().isEmpty()) {
                txtBeedsWt.setText("0");
                txtNetWt.setText(txtGrossWt.getText().trim());
            }
            txtNetWt.requestFocusInWindow();
        }


    }//GEN-LAST:event_txtBeedsWtKeyReleased

    private void txtNetWtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetWtKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDiamondWtInCarat.requestFocusInWindow();
            setBg();
            txtDiamondWtInCarat.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtNetWtKeyReleased

    private void txtDiamondWtInCaratKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondWtInCaratKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtDiamondWtInCarat.getText().trim().isEmpty()) {
                txtDiamondWtInCarat.setText("0");
                txtDiamondRateInCarat.requestFocusInWindow();
                setBg();
                txtDiamondRateInCarat.setBackground(Color.LIGHT_GRAY);
            } else {
                txtDiamondRateInCarat.requestFocusInWindow();
                setBg();
                txtDiamondRateInCarat.setBackground(Color.LIGHT_GRAY);
            }
        }

    }//GEN-LAST:event_txtDiamondWtInCaratKeyReleased

    private void txtDiamondRateInCaratKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiamondRateInCaratKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtDiamondRateInCarat.getText().trim().isEmpty()) {
                txtDiamondRateInCarat.setText("0");
                txtOpQty.requestFocusInWindow();
                setBg();
                txtOpQty.setBackground(Color.LIGHT_GRAY);
            } else {
                txtOpQty.requestFocusInWindow();
                setBg();
                txtOpQty.setBackground(Color.LIGHT_GRAY);
            }
        }
    }//GEN-LAST:event_txtDiamondRateInCaratKeyReleased

    private void txtOpQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOpQtyKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbOpQtyType.requestFocusInWindow();
            setBg();
            cmbOpQtyType.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtOpQtyKeyReleased

    private void cmbOpQtyTypeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOpQtyTypeKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPolishPercent.requestFocusInWindow();
            setBg();
            txtPolishPercent.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_cmbOpQtyTypeKeyReleased

    private void txtPolishPercentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPolishPercentKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAddItem.requestFocusInWindow();
            setBg();
            btnAddItem.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtPolishPercentKeyReleased

//    private void txtDiamondWtInCaratFocusLost(java.awt.event.FocusEvent evt) {                                          
//		if(txtDiamondWtInCarat.getText().trim().isEmpty()) {
//			txtDiamondWtInCarat.setText("0");
//		}        // TODO add your handling code here:
//	}    
//    private void txtDiamondRateInCaratFocusLost(java.awt.event.FocusEvent evt) {                                          
//		if(txtDiamondRateInCarat.getText().trim().isEmpty()) {
//			txtDiamondRateInCarat.setText("0");
//		}        // TODO add your handling code here:
//	}    
    private void btnAddItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddItemKeyReleased
        try {
            addItem();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddItemKeyReleased

    private void lblTaxSlabAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTaxSlabAddMouseClicked
        String inputtedTaxSlab = JOptionPane.showInputDialog(this, "Add Tax Slab");

        if ((inputtedTaxSlab != null && inputtedTaxSlab.length() > 0)
                && !inputtedTaxSlab.equals(cmbTaxSlab.getSelectedItem())) {

            System.out.println(inputtedTaxSlab);

        }
    }//GEN-LAST:event_lblTaxSlabAddMouseClicked

    private void lblTaxSlabEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTaxSlabEditMouseClicked
        String editedTaxSlab = JOptionPane.showInputDialog(this, "Edit Tax Slab",
                cmbTaxSlab.getSelectedItem());

        if ((editedTaxSlab != null && editedTaxSlab.length() > 0)
                && !editedTaxSlab.equals(cmbItemGroup.getSelectedItem())) {

            System.out.println(editedTaxSlab);

        }
    }//GEN-LAST:event_lblTaxSlabEditMouseClicked

    private void lblItemGroupAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemGroupAddMouseClicked
        String inputtedItemGroup = JOptionPane.showInputDialog(this, "Add Item Group");

        if ((inputtedItemGroup != null && inputtedItemGroup.length() > 0)
                && !inputtedItemGroup.equals(cmbItemGroup.getSelectedItem())) {

            cmbItemGroup.addItem(inputtedItemGroup);

        }
    }//GEN-LAST:event_lblItemGroupAddMouseClicked

    private void lblItemGroupEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblItemGroupEditMouseClicked
        String editedItemGroup = JOptionPane.showInputDialog(this, "Edit Item Group",
                cmbItemGroup.getSelectedItem());

        if ((editedItemGroup != null && editedItemGroup.length() > 0)
                && !editedItemGroup.equals(cmbItemGroup.getSelectedItem())) {

            System.out.println(editedItemGroup);

        }
    }//GEN-LAST:event_lblItemGroupEditMouseClicked

    private void cbMaintainTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMaintainTagsActionPerformed
        if (cbMaintainTags.isSelected()&& tblItemsList.getSelectedRowCount()==0) {
            if (!txtItemPrefix.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemPrefix.getText() + getTagCounter());
            } else if (!txtItemName.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
            }
        } else if (!cbMaintainTags.isSelected()&& tblItemsList.getSelectedRowCount()==0) {
            txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
        }
    }//GEN-LAST:event_cbMaintainTagsActionPerformed

    void fillItem(int selectedRow) {
//         selectedRow = tblItemsList.getSelectedRow();

        emptyTextFields();

        if (!tblItemsList.getValueAt(selectedRow, 2).equals("NULL")) {
            fieldsData = DBController.executeQuery("SELECT * FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE tagno = "
                    + "'" + tblItemsList.getValueAt(selectedRow, 2) + "'");
        } else {
            fieldsData = DBController.executeQuery("SELECT * FROM "
                    + DatabaseCredentials.ENTRY_ITEM_TABLE + " WHERE itemname = "
                    + "'" + tblItemsList.getValueAt(selectedRow, 1) + "'");
        }

        id = Integer.valueOf(fieldsData.remove(0).toString());
        itemname = fieldsData.get(0).toString();

        txtItemName.setText(fieldsData.get(0).toString());

        if (fieldsData.get(1) != null) {
            txtItemPrefix.setText(fieldsData.get(1).toString());
        }

        try {
            cmbItemGroup.setSelectedItem(fieldsData.get(2).toString());
        } catch (NullPointerException ex) {
            // For now leave this empty
            // TODO: handle the exception
        }

        cmbTaxSlab.setSelectedItem(fieldsData.get(3).toString());

        if (fieldsData.get(4) != null) {
            txtHSNCode.setText(fieldsData.get(4).toString());
        }

        if (fieldsData.get(5) != null) {
            txtShortName.setText(fieldsData.get(5).toString());
        }

        if (fieldsData.get(6) != null) {
            txtTagNo.setText(fieldsData.get(6).toString());
        }

        if (fieldsData.get(7) != null) {
            txtHUID.setText(fieldsData.get(7).toString());
        }

        if (fieldsData.get(8) != null) {
            txtGrossWt.setText(fieldsData.get(8).toString());
        }

        if (fieldsData.get(9) != null) {
            txtBeedsWt.setText(fieldsData.get(9).toString());
        }

        if (fieldsData.get(10) != null) {
            txtNetWt.setText(fieldsData.get(10).toString());
        }

        if (fieldsData.get(11) != null) {
            txtDiamondWtInCarat.setText(fieldsData.get(11).toString());
        }

        if (fieldsData.get(12) != null) {
            txtDiamondRateInCarat.setText(fieldsData.get(12).toString());
        }

        if (fieldsData.get(13) != null) {
            txtPolishPercent.setText(fieldsData.get(13).toString());
        }

        if (fieldsData.get(14) != null) {
            txtOpQty.setText(fieldsData.get(14).toString());
        }

        cmbOpQtyType.setSelectedItem(fieldsData.get(15).toString());

        lblItemPic.setIcon(null);

        if (fieldsData.get(16) != null) {
            if (UtilityMethods.fileExists(fieldsData.get(16).toString())) {
                ImagePlus image = IJ.openImage(fieldsData.get(16).toString());
                ImagePlus resizedImage = image.resize(lblItemPic.getWidth(),
                        lblItemPic.getHeight(), null);
                lblItemPic.setText("");
                lblItemPic.setIcon(new ImageIcon(resizedImage.getImage()));
            }
        } else if (lblItemPic.getText().isEmpty()) {
            lblItemPic.setText("512 x 512, 30KB");
        }
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnClearFields;
    private javax.swing.JButton btnSelectImage;
    private javax.swing.JCheckBox cbMaintainTags;
    private static javax.swing.JComboBox<String> cmbItemGroup;
    private javax.swing.JComboBox<String> cmbOpQtyType;
    private javax.swing.JComboBox<String> cmbTaxSlab;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDelete;
    private javax.swing.JLabel lblEditFields;
    private javax.swing.JLabel lblItemGroupAdd;
    private javax.swing.JLabel lblItemGroupEdit;
    private javax.swing.JLabel lblItemPic;
    private javax.swing.JLabel lblList;
    private javax.swing.JLabel lblTaxSlabAdd;
    private javax.swing.JLabel lblTaxSlabEdit;
    public javax.swing.JPanel pnlRootContainer;
    private javax.swing.JTable tblItemsList;
    private javax.swing.JTextField txtBeedsWt;
    private javax.swing.JTextField txtDiamondRateInCarat;
    private javax.swing.JTextField txtDiamondWtInCarat;
    private javax.swing.JTextField txtGrossWt;
    private javax.swing.JTextField txtHSNCode;
    private javax.swing.JTextField txtHUID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrefix;
    private javax.swing.JTextField txtNetWt;
    private javax.swing.JTextField txtOpQty;
    private javax.swing.JTextField txtPolishPercent;
    private javax.swing.JTextField txtShortName;
    private javax.swing.JTextField txtTagNo;
    private javax.swing.JTextField txtTotalNetWt;
    private javax.swing.JTextField txtTotalNoOfTags;
    private javax.swing.JTextField txtTotalOpQty;
    public static javax.swing.JTabbedPane tpScreensHolder;
}
