package jewellery;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/**
 *
 * @author AR-LABS
 */
public class CashPurchaseDialog extends javax.swing.JDialog {

    public CashPurchaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        addEscapeListener(CashPurchaseDialog.this);
        setLocationRelativeTo(DashBoardScreen.tabbedPane);

        
        CashPurchaseDialog.this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                CashPurchaseDialog.this.dispose();
            }
        });
    }
    
    private void setFocus(KeyEvent event, JComponent component) {
        if(event.getKeyCode() == KeyEvent.VK_ENTER) {
            component.requestFocusInWindow();
        }
    }
    
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = (ActionEvent e) -> {
            dialog.dispose();
        };

        dialog.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    private boolean fieldsAreValidated() {
        Component[] components = this.pnlRootContainer.getComponents();
        
        for(Component component : components) {
            if(component instanceof JTextField) {
                JTextComponent textComponent = (JTextComponent) component;
                
                if(textComponent.getText().trim().isEmpty()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void submitDetails() {
        /**
         * fields in purchase screen made non-static, so line 86 throws error, 
         * this class not used anywhere, so commented this part
         */
        
//        if(fieldsAreValidated()) {
//            if (!DBController.isDatabaseConnected()) {
//                DBController.connectToDatabase(DatabaseCredentials.DB_ADDRESS,
//                        DatabaseCredentials.DB_USERNAME, DatabaseCredentials.DB_PASSWORD);
//            }
//
//            List<Object> colNames = DBController.getTableColumnNames(DatabaseCredentials
//                    .CASH_PURCHASE_DETAILS_TABLE);
//
//            // Remove id since we don't need it
//            colNames.remove(0);
//            
//            List<Object> data = Arrays.asList(txtName.getText().trim(), txtAddress.getText().trim(),
//                    txtAdharNo.getText().trim(), txtPanNo.getText().trim(), txtContactNo.getText().trim(),
//                    PurchaseScreen.lblBill.getText());
//
//            boolean recordInserted = DBController.insertDataIntoTable(DatabaseCredentials
//                    .CASH_PURCHASE_DETAILS_TABLE, colNames, data);
//
//            if (recordInserted) {
//                // Will do something later.
//                // For now, let's leave it empty
//            }
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRootContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtAdharNo = new javax.swing.JTextField();
        txtPanNo = new javax.swing.JTextField();
        txtContactNo = new javax.swing.JTextField();
        btnSubmitDetails = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Additional Purchase Details");
        setAlwaysOnTop(true);
        setUndecorated(true);
        setResizable(false);

        pnlRootContainer.setBackground(new java.awt.Color(57, 68, 76));
        pnlRootContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlRootContainer.setForeground(new java.awt.Color(204, 153, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(238, 188, 81));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Name:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 188, 81));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Address:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(238, 188, 81));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Aadhar No:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 188, 81));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Pan No:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(238, 188, 81));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Contact No:");

        txtName.setToolTipText("");
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        txtAdharNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAdharNoKeyReleased(evt);
            }
        });

        txtPanNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPanNoKeyReleased(evt);
            }
        });

        txtContactNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContactNoKeyReleased(evt);
            }
        });

        btnSubmitDetails.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnSubmitDetails.setText("Submit Details");
        btnSubmitDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitDetailsActionPerformed(evt);
            }
        });
        btnSubmitDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnSubmitDetailsKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlRootContainerLayout = new javax.swing.GroupLayout(pnlRootContainer);
        pnlRootContainer.setLayout(pnlRootContainerLayout);
        pnlRootContainerLayout.setHorizontalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmitDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlRootContainerLayout.createSequentialGroup()
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAdharNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPanNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnlRootContainerLayout.setVerticalGroup(
            pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtName)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAdharNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPanNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlRootContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSubmitDetails)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRootContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitDetailsActionPerformed
        submitDetails();
        this.dispose();
    }//GEN-LAST:event_btnSubmitDetailsActionPerformed

    private void btnSubmitDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSubmitDetailsKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            submitDetails();
            this.dispose();
        }
    }//GEN-LAST:event_btnSubmitDetailsKeyReleased

    private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
        setFocus(evt, txtAddress);
    }//GEN-LAST:event_txtNameKeyReleased

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        setFocus(evt, txtAdharNo);
    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtAdharNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdharNoKeyReleased
        setFocus(evt, txtPanNo);
    }//GEN-LAST:event_txtAdharNoKeyReleased

    private void txtPanNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPanNoKeyReleased
        setFocus(evt, txtContactNo);
    }//GEN-LAST:event_txtPanNoKeyReleased

    private void txtContactNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNoKeyReleased
        setFocus(evt, btnSubmitDetails);
    }//GEN-LAST:event_txtContactNoKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSubmitDetails;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel pnlRootContainer;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAdharNo;
    private javax.swing.JTextField txtContactNo;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPanNo;
    // End of variables declaration//GEN-END:variables
}
