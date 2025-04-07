/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import jewellery.helper.RealSettingsHelper;

/**
 *
 * @author SACHIN MISHRA
 */
public class ItemEntryScreen extends javax.swing.JFrame {

    /**
     * Creates new form ItemEntryScreen
     */
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
        jButton3.setVisible(false);
//        populateItemsListTable();
        fillgrandtotal();
        //setImageOnJLabel(lblEditFields, AssetsLocations.TEXT_FILE_EDIT_ICON);
       // setImageOnJLabel(lblDelete, AssetsLocations.TRASH_CAN_ICON);
       // setImageOnJLabel(lblList, AssetsLocations.NOTEBOOK_ICON);
        setImageOnJLabel(lblTaxSlabAdd, AssetsLocations.LIST_ADD_ICON);
        setImageOnJLabel(lblTaxSlabEdit, AssetsLocations.CONTENT_PEN_WRITE_ICON);
        setImageOnJLabel(lblItemGroupAdd, AssetsLocations.LIST_ADD_ICON);
        setImageOnJLabel(lblItemGroupEdit, AssetsLocations.CONTENT_PEN_WRITE_ICON);

        setComponentsEnabledOrDisabled();

        populateItemGroupComboBox();

        getTagCounter();
        cbMaintainTags.setSelected(true);

    }

    private int getTagCounter() {
        if (!DBController.isDatabaseConnected()) {
            DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
                    DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
        }

        List<Object> counterValue = DBController.executeQuery("SELECT tag_counter "
                + "FROM " + DatabaseCredentials.TAG_COUNTER_TABLE);
        int l = Integer.parseInt(counterValue.get(0).toString());
        return (l + 1);
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
            ImagePlus img = ij.IJ.openImage(fileLocation);
            ImagePlus resizedImage = img.resize(30, 30, null);
            return new ImageIcon(resizedImage.getImage());
        }

        return null;
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
            Labour.setText(fieldsData.get(13).toString());
        }

        if (fieldsData.get(14) != null) {
            txtOpQty.setText(fieldsData.get(14).toString());
        }

//        cmbOpQtyType.setSelectedItem(fieldsData.get(15).toString());

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

    public void ItemListsRedirect(String itemname) {
        txtItemName.setText(itemname);
        jButton3.setVisible(true);
        btnAddItem.setText("Update Items");
        jLabel1.setText("UPDATE ITEMS");
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
            ResultSet rs = stmt.executeQuery("SELECT itemimage, itemname, tagno, huid, grosswt, netwt, diamondwt, opqty,item_sold, itemgroup, taxslab FROM entryitem where itemname = '" + txtItemName.getText() + "';");
            while (rs.next()) {
                String image = rs.getString("itemimage");
                String name = rs.getString("itemname");
                String tag = rs.getString("tagno");
                String ID = rs.getString("huid");
                String gross = rs.getString("grosswt");
                String net = rs.getString("netwt");
                String diamond = rs.getString("diamondwt");
                String op = rs.getString("opqty");
                String soldQty = rs.getString("item_sold");
                String itemgrp = rs.getString("itemgroup");
                String taxslab = rs.getString("taxslab");

                if ("0".equals(op) || "".equals(op) || op == null) {
                        String query = "SELECT tagnoItems FROM purchasehistory ";
                        Statement stmt2 = con.createStatement();
                        ResultSet rt = stmt2.executeQuery(query);
                        while (rt.next()) {
                            if (rt.getString(1) == null ? tag == null : rt.getString(1).equals(tag)) {
                                op = "1";
                            }
//                       JOptionPane.showMessageDialog(this,tag +" and " +rt.getString(1));
                        }
                        stmt2.close();
                        rt.close();
                        m.addRow(new Object[]{image, name, tag, ID, gross, net, diamond, 0,itemgrp,taxslab});
                    } else {
                        m.addRow(new Object[]{image, name, tag, ID, gross, net, diamond, op, itemgrp,taxslab});
                    }

            }
            con.close();
            stmt.close();
            rs.close();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int g = 0; g < tblItemsList.getColumnCount(); g++) {
                tblItemsList.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
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
    public void refresh() {
        DefaultTableModel m = (DefaultTableModel) tblItemsList.getModel();
        m.setRowCount(0);
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT itemimage, itemname, tagno, huid, grosswt, netwt, diamondwt, opqty,item_sold, itemgroup, taxslab FROM entryitem where itemname = '" + txtItemName.getText() + "';");
            while (rs.next()) {
                String image = rs.getString("itemimage");
                String name = rs.getString("itemname");
                String tag = rs.getString("tagno");
                String ID = rs.getString("huid");
                String gross = rs.getString("grosswt");
                String net = rs.getString("netwt");
                String diamond = rs.getString("diamondwt");
                String op = rs.getString("opqty");
                String soldQty = rs.getString("item_sold");
                String itemgrp = rs.getString("itemgroup");
                String taxslab = rs.getString("taxslab");

                if ("0".equals(op) || "".equals(op) || op == null) {
                        String query = "SELECT tagnoItems FROM purchasehistory ";
                        Statement stmt2 = con.createStatement();
                        ResultSet rt = stmt2.executeQuery(query);
                        while (rt.next()) {
                            if (rt.getString(1) == null ? tag == null : rt.getString(1).equals(tag)) {
                                op = "1";
                            }
//                       JOptionPane.showMessageDialog(this,tag +" and " +rt.getString(1));
                        }
                        stmt2.close();
                        rt.close();
                        m.addRow(new Object[]{image, name, tag, ID, gross, net, diamond, 0,itemgrp,taxslab});
                    } else {
                        m.addRow(new Object[]{image, name, tag, ID, gross, net, diamond, op, itemgrp,taxslab});
                    }

            }
            con.close();
            stmt.close();
            rs.close();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int g = 0; g < tblItemsList.getColumnCount(); g++) {
                tblItemsList.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        fillgrandtotal();
        
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

        String newItemName = txtItemName.getText();
        String itemGroup = cmbItemGroup.getSelectedItem().toString();

        String itemTagNo;
        if ((!(cbMaintainTags.isSelected()) && tblItemsList.getSelectedRowCount() == 0)) {
            itemTagNo = "N.A";
        } else {
            itemTagNo = txtTagNo.getText();

        }

        try {

            Connection con = DBConnect.connect();

            String query = "SELECT COUNT(*) FROM entryitem WHERE itemname = ? AND itemgroup = ? AND tagno = ?";

            // Prepare the statement to prevent SQL injection
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, newItemName);
            pstmt.setString(2, itemGroup);
            pstmt.setString(3, itemTagNo);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Process the result
            if (rs.next()) {
                int count = rs.getInt(1);  // Get the result from the COUNT query
                if (count > 0 && btnAddItem.getText().trim().equalsIgnoreCase("add item")) {
                    JOptionPane.showMessageDialog(this, "This tag is already added");

                } else {

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
                    if ((!(cbMaintainTags.isSelected()) && tblItemsList.getSelectedRowCount() == 0)) {
                        data.add("N.A");
                    } else {
                        data.add(txtTagNo.getText());
                    }
                    if (!txtHUID.getText().trim().isEmpty()) {
                        data.add(txtHUID.getText().trim());
                    } else {
                        data.add("");
                    }

                    if (!txtGrossWt.getText().trim().isEmpty()) {
                        data.add(txtGrossWt.getText().trim());
                    } else {
                        data.add("0");
                    }

                    if (!txtBeedsWt.getText().trim().isEmpty()) {
                        data.add(txtBeedsWt.getText().trim());
                    } else {
                        data.add("0");
                    }

                    if (!txtNetWt.getText().trim().isEmpty()) {
                        data.add(txtNetWt.getText().trim());
                    } else {
                        data.add("0");
                    }

                    if (!txtDiamondWtInCarat.getText().trim().isEmpty()) {
                        data.add(txtDiamondWtInCarat.getText().trim());
                    } else {
                        data.add("0");
                    }

                    if (!txtDiamondRateInCarat.getText().trim().isEmpty()) {
                        data.add(txtDiamondRateInCarat.getText().trim());
                    } else {
                        columnNames.remove("carats");
                    }

                    if (!Labour.getText().trim().isEmpty()) {
                        data.add(Labour.getText().trim());
                    } else {
                        columnNames.remove("polishpercent");
                    }
                    columnNames.remove("op_qty_type");
                    if (!txtOpQty.getText().trim().isEmpty()) {
                        data.add(txtOpQty.getText().trim());
                    } else {
                        data.add("0");
                    }
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
            }

            // Close resources
            rs.close();
            pstmt.close();
            con.close();

        } catch (SQLException e) {
            // Handle SQL exception
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
        Labour = new javax.swing.JTextField();
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
        btnSelectImage = new javax.swing.JButton();
        btnClearFields = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtHUID = new javax.swing.JTextField();
        lblTaxSlabAdd = new javax.swing.JLabel();
        lblTaxSlabEdit = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtTotalNetWt = new javax.swing.JTextField();
        txtTotalNoOfTags = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtTotalOpQty = new javax.swing.JTextField();
        lblItemGroupAdd = new javax.swing.JLabel();
        lblItemGroupEdit = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlRootContainer.setBackground(new java.awt.Color(57, 68, 76));
        pnlRootContainer.setForeground(new java.awt.Color(57, 68, 76));
        pnlRootContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(238, 188, 81));
        jLabel7.setText("Item Name");
        pnlRootContainer.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 85, -1, -1));

        txtItemName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemNameFocusLost(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 108, 478, 32));

        txtItemPrefix.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtItemPrefixFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemPrefixFocusLost(evt);
            }
        });
        txtItemPrefix.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemPrefixKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtItemPrefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 108, 387, 32));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setText("Item Prefix");
        pnlRootContainer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 85, -1, -1));

        cmbItemGroup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbItemGroupFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbItemGroupFocusLost(evt);
            }
        });
        cmbItemGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbItemGroupKeyReleased(evt);
            }
        });
        pnlRootContainer.add(cmbItemGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(1056, 108, 130, 20));

        cmbTaxSlab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3%", "5%", " " }));
        cmbTaxSlab.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTaxSlabFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbTaxSlabFocusLost(evt);
            }
        });
        cmbTaxSlab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTaxSlabActionPerformed(evt);
            }
        });
        cmbTaxSlab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbTaxSlabKeyReleased(evt);
            }
        });
        pnlRootContainer.add(cmbTaxSlab, new org.netbeans.lib.awtextra.AbsoluteConstraints(1204, 108, 83, 20));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setText("HSN Code");
        pnlRootContainer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 146, 70, -1));

        txtHSNCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHSNCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHSNCodeFocusLost(evt);
            }
        });
        txtHSNCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHSNCodeKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtHSNCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 169, 235, 32));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setText("Short Name");
        pnlRootContainer.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 146, -1, -1));

        txtShortName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtShortNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShortNameFocusLost(evt);
            }
        });
        txtShortName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtShortNameKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtShortName, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 169, 237, 32));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(238, 188, 81));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Item Group");
        pnlRootContainer.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1056, 85, 130, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(238, 188, 81));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tax Slab");
        pnlRootContainer.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1204, 85, 83, -1));

        cbMaintainTags.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cbMaintainTags.setForeground(new java.awt.Color(238, 188, 81));
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
        pnlRootContainer.add(cbMaintainTags, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 169, 250, 26));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Enter Tag No. Available For This Item With Opening Book");
        pnlRootContainer.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 220, -1, -1));

        jLabel10.setForeground(new java.awt.Color(238, 188, 81));
        jLabel10.setText("---------------------------------------------------------------------");
        pnlRootContainer.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(729, 220, 559, 17));

        jLabel11.setForeground(new java.awt.Color(238, 188, 81));
        jLabel11.setText("-------------------------------------------------------------------------------------------");
        pnlRootContainer.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 219, -1, 18));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(238, 188, 81));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Tag No.");
        pnlRootContainer.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 109, -1));

        txtTagNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTagNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTagNoFocusLost(evt);
            }
        });
        txtTagNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTagNoKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtTagNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 266, 220, 32));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 188, 81));
        jLabel14.setText("Gross Wt.");
        pnlRootContainer.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 250, 65, -1));

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
        pnlRootContainer.add(txtGrossWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 266, 160, 32));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 188, 81));
        jLabel15.setText("Beeds Wt.");
        pnlRootContainer.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 250, 66, -1));

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
        pnlRootContainer.add(txtBeedsWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(776, 266, 250, 32));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(238, 188, 81));
        jLabel16.setText("Net Wt.");
        pnlRootContainer.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 250, 51, -1));

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
        pnlRootContainer.add(txtNetWt, new org.netbeans.lib.awtextra.AbsoluteConstraints(1061, 266, 240, 32));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 188, 81));
        jLabel17.setText("Diamond Wt. in carat");
        pnlRootContainer.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 136, -1));

        txtDiamondWtInCarat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiamondWtInCaratFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondWtInCaratFocusLost(evt);
            }
        });
        txtDiamondWtInCarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondWtInCaratKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtDiamondWtInCarat, new org.netbeans.lib.awtextra.AbsoluteConstraints(196, 320, 230, 32));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 188, 81));
        jLabel18.setText("Diamond rate / carat");
        pnlRootContainer.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 300, 130, -1));

        txtDiamondRateInCarat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiamondRateInCaratFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiamondRateInCaratFocusLost(evt);
            }
        });
        txtDiamondRateInCarat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiamondRateInCaratActionPerformed(evt);
            }
        });
        txtDiamondRateInCarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiamondRateInCaratKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtDiamondRateInCarat, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 320, 330, 32));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(238, 188, 81));
        jLabel19.setText("Labour");
        pnlRootContainer.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 300, 85, -1));

        Labour.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                LabourFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                LabourFocusLost(evt);
            }
        });
        Labour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LabourKeyReleased(evt);
            }
        });
        pnlRootContainer.add(Labour, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 320, 240, 32));

        lblItemPic.setBackground(new java.awt.Color(204, 204, 204));
        lblItemPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemPic.setText("512 x 512, 30KB");
        lblItemPic.setOpaque(true);
        pnlRootContainer.add(lblItemPic, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 243, 176, 97));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 188, 81));
        jLabel22.setText("Op. Qty");
        pnlRootContainer.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, -1, -1));

        txtOpQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtOpQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOpQtyFocusLost(evt);
            }
        });
        txtOpQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOpQtyKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtOpQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 320, 240, 32));

        btnAddItem.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAddItem.setText("Add Item");
        btnAddItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnAddItemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnAddItemFocusLost(evt);
            }
        });
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
        pnlRootContainer.add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 360, 570, -1));

        tblItemsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Image", "Item Name", "Tag No.", "HUID", "Gross Wt.", "Net Wt.", "Diamond. Wt.", "Op. Qty", "Group name", "Tax Slab"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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

        pnlRootContainer.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 396, 1344, 160));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setText("Item Entry");
        pnlRootContainer.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 6, 503, 55));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/goldenRingImage.png"))); // NOI18N
        pnlRootContainer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(528, 6, -1, 55));

        btnSelectImage.setText("Select Image");
        btnSelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectImageActionPerformed(evt);
            }
        });
        pnlRootContainer.add(btnSelectImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 346, 178, 34));

        btnClearFields.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClearFields.setText("Clear Fields");
        btnClearFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFieldsActionPerformed(evt);
            }
        });
        pnlRootContainer.add(btnClearFields, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 360, 520, -1));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 188, 81));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("HUID");
        pnlRootContainer.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 103, -1));

        txtHUID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHUIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHUIDFocusLost(evt);
            }
        });
        txtHUID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHUIDActionPerformed(evt);
            }
        });
        txtHUID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHUIDKeyReleased(evt);
            }
        });
        pnlRootContainer.add(txtHUID, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 266, 160, 32));

        lblTaxSlabAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaxSlabAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTaxSlabAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaxSlabAddMouseClicked(evt);
            }
        });
        pnlRootContainer.add(lblTaxSlabAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1205, 146, 38, 37));

        lblTaxSlabEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaxSlabEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTaxSlabEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaxSlabEditMouseClicked(evt);
            }
        });
        pnlRootContainer.add(lblTaxSlabEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1249, 146, 38, 37));

        jPanel2.setForeground(new java.awt.Color(57, 68, 76));
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

        pnlRootContainer.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 559, 1307, -1));

        lblItemGroupAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemGroupAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblItemGroupAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItemGroupAddMouseClicked(evt);
            }
        });
        pnlRootContainer.add(lblItemGroupAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1081, 146, 38, 37));

        lblItemGroupEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemGroupEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblItemGroupEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblItemGroupEditMouseClicked(evt);
            }
        });
        pnlRootContainer.add(lblItemGroupEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1125, 146, 38, 37));

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlRootContainer.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1240, 22, 70, 31));

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton3.setText("Delete");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        pnlRootContainer.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1022, 19, -1, 36));

        jToggleButton1.setBackground(new java.awt.Color(0, 204, 51));
        jToggleButton1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(0, 0, 0));
        jToggleButton1.setText("New Entry");
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton1MouseClicked(evt);
            }
        });
        pnlRootContainer.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 120, 36));

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
                .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtItemPrefix.requestFocusInWindow();
            setBg();
            txtItemPrefix.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtItemNameKeyReleased

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

    private void cbMaintainTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMaintainTagsActionPerformed
       
        if (cbMaintainTags.isSelected() && tblItemsList.getSelectedRowCount() == 0) {
            if (!txtItemPrefix.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemPrefix.getText() + getTagCounter());
            } else if (!txtItemName.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
            }
        } else if (!(cbMaintainTags.isSelected()) && tblItemsList.getSelectedRowCount() == 0) {
           // txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
           txtTagNo.setText("");

        }
    }//GEN-LAST:event_cbMaintainTagsActionPerformed

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

    private void LabourKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LabourKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAddItem.requestFocusInWindow();
            setBg();
            btnAddItem.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_LabourKeyReleased

    private void txtOpQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOpQtyKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Labour.requestFocusInWindow();
            setBg();
            Labour.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtOpQtyKeyReleased

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

            if (tblItemsList.getSelectedRowCount() == 0) {
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

    private void btnAddItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddItemKeyReleased
        try {
            addItem();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddItemKeyReleased

    private void tblItemsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsListMouseClicked

        try {
            selectedRow = tblItemsList.getSelectedRow();
            emptyTextFields();

            if (!tblItemsList.getValueAt(selectedRow, 2).equals("NULL")) {
               fieldsData = DBController.executeQuery("SELECT * FROM " +
                        DatabaseCredentials.ENTRY_ITEM_TABLE + " " +
                        "WHERE tagno = '" + tblItemsList.getValueAt(selectedRow, 2) + "' " +
                        "AND itemname = '" + tblItemsList.getValueAt(selectedRow, 1) + "'" +
                        "AND itemgroup = '" + tblItemsList.getValueAt(selectedRow, 8) + "'" +
                        "AND taxslab = '" + tblItemsList.getValueAt(selectedRow, 9) + "'");

            } else {
                fieldsData = DBController.executeQuery("SELECT * FROM " +
                        DatabaseCredentials.ENTRY_ITEM_TABLE + " " +
                        "WHERE tagno = '" + tblItemsList.getValueAt(selectedRow, 2) + "' " +
                        "AND itemname = '" + tblItemsList.getValueAt(selectedRow, 1) + "'" +
                        "AND itemgroup = '" + tblItemsList.getValueAt(selectedRow, 8) + "'" +
                        "AND taxslab = '" + tblItemsList.getValueAt(selectedRow, 9) + "'");
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
                Labour.setText(fieldsData.get(13).toString());
            }

            if (fieldsData.get(14) != null) {
                txtOpQty.setText(fieldsData.get(14).toString());
            }

//            cmbOpQtyType.setSelectedItem(fieldsData.get(15).toString());

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
        } catch (Exception e) {
            Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, e);
        }

    }//GEN-LAST:event_tblItemsListMouseClicked

    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImageActionPerformed
        String[] selectedFileWithFileNameAndSize
                = UtilityMethods.selectFileAndReturnLocation("*.images", "png");

        if (selectedFileWithFileNameAndSize != null) {
            selectedImage = selectedFileWithFileNameAndSize[0];
            selectedImageName = selectedFileWithFileNameAndSize[1];
            selectedImageSize = selectedFileWithFileNameAndSize[2];
        }

        if (selectedImageSize != null && Double.valueOf(selectedImageSize) > 10000) {
            JOptionPane.showMessageDialog(this, "Selected Image is larger than"
                    + "30kb, \n Maximum allowed size is 30kb");
            return;
        }

        if (!(selectedImage.trim().isEmpty() || selectedImage == null)) {
            ImagePlus image = ij.IJ.openImage(selectedImage);
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

    private void btnClearFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFieldsActionPerformed
        emptyTextFields();

        lblItemPic.setIcon(null);
        lblItemPic.setText("512 x 512, 30KB");

        id = -97108105;
    }//GEN-LAST:event_btnClearFieldsActionPerformed

    private void txtHUIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHUIDKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtGrossWt.requestFocusInWindow();
            setBg();
            txtGrossWt.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_txtHUIDKeyReleased

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

    private void txtTagNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTagNoFocusGained
        txtTagNo.setBackground(new Color(245, 230, 66));
        if (cbMaintainTags.isSelected() && tblItemsList.getSelectedRowCount() == 0) {
            if (!txtItemPrefix.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemPrefix.getText() + getTagCounter());
            } else if (!txtItemName.getText().trim().isEmpty()) {
                txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
            }
        }else if (!cbMaintainTags.isSelected() && tblItemsList.getSelectedRowCount() == 0) {
            txtTagNo.setText("");
        } 
//        } else if (!cbMaintainTags.isSelected() && tblItemsList.getSelectedRowCount() == 0) {
//            txtTagNo.setText(txtItemName.getText().substring(0, 2) + getTagCounter());
//        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtTagNoFocusGained

    private void txtOpQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOpQtyFocusLost
        txtOpQty.setBackground(Color.white);
        if (RealSettingsHelper.gettagNoIsTrue()) {
            
            //txtOpQty.setText("1");
        }

    }//GEN-LAST:event_txtOpQtyFocusLost

    private void txtOpQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOpQtyFocusGained
        txtOpQty.setBackground(new Color(245, 230, 66));
        //txtOpQty.setText("1");    
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOpQtyFocusGained

    private void cmbTaxSlabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTaxSlabActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTaxSlabActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
       dispose();
       

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtItemNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusGained
       txtItemName.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtItemNameFocusGained

    private void txtItemNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemNameFocusLost
        txtItemName.setBackground(Color.white);
    }//GEN-LAST:event_txtItemNameFocusLost

    private void txtItemPrefixFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemPrefixFocusGained
        txtItemPrefix.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtItemPrefixFocusGained

    private void txtItemPrefixFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemPrefixFocusLost
       txtItemPrefix.setBackground(Color.white);
    }//GEN-LAST:event_txtItemPrefixFocusLost

    private void txtHSNCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHSNCodeFocusGained
        txtHSNCode.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtHSNCodeFocusGained

    private void txtHSNCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHSNCodeFocusLost
        txtHSNCode.setBackground(Color.white);
    }//GEN-LAST:event_txtHSNCodeFocusLost

    private void txtShortNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShortNameFocusGained
       txtShortName.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtShortNameFocusGained

    private void txtShortNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShortNameFocusLost
        txtShortName.setBackground(Color.white);
    }//GEN-LAST:event_txtShortNameFocusLost

    private void txtTagNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTagNoFocusLost
       txtTagNo.setBackground(Color.white);
    }//GEN-LAST:event_txtTagNoFocusLost

    private void txtHUIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHUIDFocusGained
       txtHUID.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtHUIDFocusGained

    private void txtHUIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHUIDFocusLost
        txtHUID.setBackground(Color.white);
    }//GEN-LAST:event_txtHUIDFocusLost

    private void txtGrossWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGrossWtFocusGained
        txtGrossWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtGrossWtFocusGained

    private void txtGrossWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGrossWtFocusLost
       txtGrossWt.setBackground(Color.white);
    }//GEN-LAST:event_txtGrossWtFocusLost

    private void txtBeedsWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeedsWtFocusGained
        txtBeedsWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtBeedsWtFocusGained

    private void txtBeedsWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeedsWtFocusLost
        txtBeedsWt.setBackground(Color.white);
    }//GEN-LAST:event_txtBeedsWtFocusLost

    private void txtNetWtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWtFocusGained
        txtNetWt.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtNetWtFocusGained

    private void txtNetWtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWtFocusLost
       txtNetWt.setBackground(Color.white);
    }//GEN-LAST:event_txtNetWtFocusLost

    private void txtDiamondWtInCaratFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtInCaratFocusGained
        txtDiamondWtInCarat.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiamondWtInCaratFocusGained

    private void txtDiamondWtInCaratFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondWtInCaratFocusLost
       txtDiamondWtInCarat.setBackground(Color.white);
    }//GEN-LAST:event_txtDiamondWtInCaratFocusLost

    private void txtDiamondRateInCaratFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateInCaratFocusGained
       txtDiamondRateInCarat.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_txtDiamondRateInCaratFocusGained

    private void txtDiamondRateInCaratFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiamondRateInCaratFocusLost
       txtDiamondRateInCarat.setBackground(Color.white);
    }//GEN-LAST:event_txtDiamondRateInCaratFocusLost

    private void LabourFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_LabourFocusGained
       Labour.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_LabourFocusGained

    private void LabourFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_LabourFocusLost
        Labour.setBackground(Color.white);
    }//GEN-LAST:event_LabourFocusLost

    private void cmbItemGroupFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbItemGroupFocusGained
        cmbItemGroup.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbItemGroupFocusGained

    private void cmbItemGroupFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbItemGroupFocusLost
        cmbItemGroup.setBackground(Color.white);
    }//GEN-LAST:event_cmbItemGroupFocusLost

    private void cmbTaxSlabFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTaxSlabFocusGained
        cmbTaxSlab.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_cmbTaxSlabFocusGained

    private void cmbTaxSlabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTaxSlabFocusLost
        cmbTaxSlab.setBackground(Color.white);
    }//GEN-LAST:event_cmbTaxSlabFocusLost

    private void btnAddItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnAddItemFocusGained
        btnAddItem.setBackground(new Color(245, 230, 66));
    }//GEN-LAST:event_btnAddItemFocusGained

    private void btnAddItemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnAddItemFocusLost
        btnAddItem.setBackground(Color.white);
    }//GEN-LAST:event_btnAddItemFocusLost

    private void txtHUIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHUIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHUIDActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        String itemName = txtItemName.getText().trim();
        String tagno = txtTagNo.getText();
        String itemGrp = cmbItemGroup.getSelectedItem().toString();
        String taxgrp = cmbTaxSlab.getSelectedItem().toString();
        try {
            Connection con = DBConnect.connect();
            Statement st = con.createStatement();
            String query = "DELETE FROM entryitem WHERE itemname = '" + itemName + "' AND " +
               "itemgroup = '" + itemGrp + "' AND tagno = '" + tagno + "' AND taxslab = '"+taxgrp+"';";

            st.execute(query);
            refresh();
            con.close();
            st.close();
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "Item Deleted !");
            

        } catch (SQLException e) {
            Logger.getLogger(ItemEntryScreen.class.getName()).log(Level.SEVERE, null, e);
        } 
    }//GEN-LAST:event_jButton3MouseClicked

    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
        setComponentsEnabledOrDisabled();
    }//GEN-LAST:event_jToggleButton1MouseClicked

    private void txtDiamondRateInCaratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiamondRateInCaratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiamondRateInCaratActionPerformed

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
            java.util.logging.Logger.getLogger(ItemEntryScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemEntryScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemEntryScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemEntryScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemEntryScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Labour;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnClearFields;
    private javax.swing.JButton btnSelectImage;
    private javax.swing.JCheckBox cbMaintainTags;
    private static javax.swing.JComboBox<String> cmbItemGroup;
    private javax.swing.JComboBox<String> cmbTaxSlab;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblItemGroupAdd;
    private javax.swing.JLabel lblItemGroupEdit;
    private javax.swing.JLabel lblItemPic;
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
    private javax.swing.JTextField txtShortName;
    private javax.swing.JTextField txtTagNo;
    private javax.swing.JTextField txtTotalNetWt;
    private javax.swing.JTextField txtTotalNoOfTags;
    private javax.swing.JTextField txtTotalOpQty;
    // End of variables declaration//GEN-END:variables
}
