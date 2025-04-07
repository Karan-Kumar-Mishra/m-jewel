package jewellery;

import customcomponents.CustomOutputStream;
import java.io.PrintStream;

/**
 *
 * @author AR-LABS
 */
public class DebugAndLogsScreen extends javax.swing.JFrame {

    public final PrintStream standardOut;
    
    public DebugAndLogsScreen() {
        initComponents();
        
        PrintStream printStream = new PrintStream(new CustomOutputStream(this.textAreaOutputStream));
        standardOut = System.out;
        
        System.setOut(printStream);
        System.setErr(printStream);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaOutputStream = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Logs");

        textAreaOutputStream.setEditable(false);
        textAreaOutputStream.setBackground(new java.awt.Color(226, 226, 226));
        textAreaOutputStream.setColumns(20);
        textAreaOutputStream.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        textAreaOutputStream.setForeground(new java.awt.Color(255, 25, 25));
        textAreaOutputStream.setRows(5);
        jScrollPane1.setViewportView(textAreaOutputStream);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textAreaOutputStream;
    // End of variables declaration//GEN-END:variables
}
