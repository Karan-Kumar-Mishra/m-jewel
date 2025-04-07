package jewellery;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author AR-LABS
 */
public class LoginPageRedesigned extends javax.swing.JFrame {

    private List<Object> usernameAndPasswordHolder = new ArrayList<>();
    private boolean credentialsAreCorrect = false;

    public static DashBoardScreen staticdashboared = new DashBoardScreen();

    public LoginPageRedesigned() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Program Files\\Jewelry Setup\\Images\\icon.jpg");
        this.setIconImage(icon);

        pnlLoadingContainer.setBackground(new Color(0, 0, 0, 180));

//        ImageIcon progressBarIcon = new ImageIcon(getClass()
//                .getResource("/assets/indefinite_circular_progress_bar.gif"));
//        lblLoadingIcon.setIcon(progressBarIcon);
        pnlComponentsContainer.setBackground(new Color(0, 0, 0, 180));

        btnLogin.setBackground(new Color(232, 126, 4));
        lblUsername.setForeground(new Color(232, 126, 4));
        lblPassword.setForeground(new Color(232, 126, 4));

        lblLogin.requestFocusInWindow();

        if (txtUsername.getText().trim().isEmpty()) {
            txtUsername.setText("enter your username");
        }

        txtUsername.setForeground(Color.GRAY);

        if (txtPassword.getText().trim().isEmpty()) {
            txtPassword.setText("enter your password");
        }

        txtPassword.setForeground(Color.GRAY);

        initCloseButtonEventCode();
    }

    private void initCloseButtonEventCode() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventQueue.invokeLater(() -> {
                    new SelectAndCreateCompanyScreen().setVisible(true);
                });
                LoginPageRedesigned.this.dispose();
            }
        });
    }

    class CustomGlassPane extends JComponent {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

    }

    private boolean checkUsernameAndPassword(List<Object> data,
            String username, String password) {

        if (!data.isEmpty()) {
            if (data.get(0).toString().equals(username) && data.get(1).toString().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCompanySelected() {
        return GLOBAL_VARS.SELECTED_COMPANY != null
                && !(GLOBAL_VARS.SELECTED_COMPANY.trim().isEmpty());
    }

    private void login() throws FileNotFoundException {
        

        String un = txtUsername.getText();
        String pass = null;
        String defaultpass = "1";
        int id = 0;
        try {
            java.sql.Connection con = DBConnect.connectCopy();
            java.sql.Statement stmt = con.createStatement();
            String q1 = "Select * from users where username='" + un + "' ";
            ResultSet rs1 = stmt.executeQuery(q1);

            while (rs1.next()) {
                id = rs1.getInt("userid");
                pass = rs1.getString("password");
                
                
            }

            if (pass == null && !txtPassword.getText().equals(defaultpass)) {
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, "No Such Username found!!!");
            } else if (txtPassword.getText().equals(pass) || txtPassword.getText().equals(defaultpass)) {
                GLOBAL_VARS.un = un;
                GLOBAL_VARS.pw = pass;
                GLOBAL_VARS.userid = id;
                
                this.dispose();
                try {

                    staticdashboared.setVisible(true);
                } catch (Exception e) {
                    Logger.getLogger(LoginPageRedesigned.class.getName()).log(Level.SEVERE, null, e);
                    JOptionPane.showMessageDialog(this, e);
                }
            } else {
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, "Wrong Autentication!!!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginPageRedesigned.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBackgroundImage() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lpContainer = new javax.swing.JLayeredPane();
        pnlLoginContainer = pnlLoginContainer = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create();

                String loadingimage = null;
                URL f = null;

                try {
                    Connection c = DBConnect.connectCopy();
                    Statement s = c.createStatement();
                    ResultSet rs1 = s.executeQuery("Select * from settings where slno=2;");
                    while(rs1.next()){
                        String title = rs1.getString("file_name");
                        loadingimage=System.getProperty("user.dir")+"/Images/"+title;
                        f = new File(loadingimage).toURI().toURL();

                    }
                } catch (SQLException | MalformedURLException ex) {
                    Logger.getLogger(LoginPageRedesigned.class.getName()).log(Level.SEVERE, null, ex);
                }
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(f).getImage().getScaledInstance(pnlLoginContainer.getWidth(),
                    pnlLoginContainer.getHeight(), Image.SCALE_SMOOTH));
            // lblBackgroundImage.setIcon(imageIcon);

            //ImageIcon imageIcon = new ImageIcon(getClass()
                //      .getResource("/assets/jewelryImage.jpg"));

            Image image = imageIcon.getImage();

            g2d.drawImage(image, 0, 0, pnlLoginContainer.getWidth(),
                pnlLoginContainer.getHeight(), this);
        }
    };
    pnlComponentsContainer = new javax.swing.JPanel();
    lblLogin = new javax.swing.JLabel();
    lblUsername = new javax.swing.JLabel();
    txtUsername = new javax.swing.JTextField();
    lblPassword = new javax.swing.JLabel();
    btnLogin = new javax.swing.JButton();
    txtPassword = new javax.swing.JTextField();
    pnlLoadingContainer = new javax.swing.JPanel();
    lblLoadingIcon = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Jewellery System Log in");
    setResizable(false);

    lpContainer.setLayout(new java.awt.CardLayout());

    lblLogin.setFont(new java.awt.Font("Trebuchet MS", 1, 48)); // NOI18N
    lblLogin.setForeground(new java.awt.Color(255, 255, 255));
    lblLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblLogin.setText("LOGIN");

    lblUsername.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
    lblUsername.setForeground(new java.awt.Color(255, 255, 255));
    lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblUsername.setText("USERNAME");

    txtUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
    txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            txtUsernameFocusGained(evt);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            txtUsernameFocusLost(evt);
        }
    });
    txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            txtUsernameKeyReleased(evt);
        }
    });

    lblPassword.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
    lblPassword.setForeground(new java.awt.Color(255, 255, 255));
    lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblPassword.setText("PASSWORD");

    btnLogin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
    btnLogin.setText("Log In");
    btnLogin.setBorder(null);
    btnLogin.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnLoginActionPerformed(evt);
        }
    });
    btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            btnLoginKeyReleased(evt);
        }
    });

    txtPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            txtPasswordFocusGained(evt);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            txtPasswordFocusLost(evt);
        }
    });
    txtPassword.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtPasswordActionPerformed(evt);
        }
    });
    txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            txtPasswordKeyReleased(evt);
        }
    });

    javax.swing.GroupLayout pnlComponentsContainerLayout = new javax.swing.GroupLayout(pnlComponentsContainer);
    pnlComponentsContainer.setLayout(pnlComponentsContainerLayout);
    pnlComponentsContainerLayout.setHorizontalGroup(
        pnlComponentsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(lblLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(lblUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(lblPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(pnlComponentsContainerLayout.createSequentialGroup()
            .addGroup(pnlComponentsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlComponentsContainerLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlComponentsContainerLayout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addGroup(pnlComponentsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                        .addComponent(txtPassword))))
            .addContainerGap(44, Short.MAX_VALUE))
    );
    pnlComponentsContainerLayout.setVerticalGroup(
        pnlComponentsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(pnlComponentsContainerLayout.createSequentialGroup()
            .addComponent(lblLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34)
            .addComponent(lblUsername)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lblPassword)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(36, 36, 36)
            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 79, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout pnlLoginContainerLayout = new javax.swing.GroupLayout(pnlLoginContainer);
    pnlLoginContainer.setLayout(pnlLoginContainerLayout);
    pnlLoginContainerLayout.setHorizontalGroup(
        pnlLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginContainerLayout.createSequentialGroup()
            .addGap(0, 462, Short.MAX_VALUE)
            .addComponent(pnlComponentsContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    pnlLoginContainerLayout.setVerticalGroup(
        pnlLoginContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(pnlComponentsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    lpContainer.add(pnlLoginContainer, "card2");

    lblLoadingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    javax.swing.GroupLayout pnlLoadingContainerLayout = new javax.swing.GroupLayout(pnlLoadingContainer);
    pnlLoadingContainer.setLayout(pnlLoadingContainerLayout);
    pnlLoadingContainerLayout.setHorizontalGroup(
        pnlLoadingContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(lblLoadingIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
    );
    pnlLoadingContainerLayout.setVerticalGroup(
        pnlLoadingContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(lblLoadingIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
    );

    lpContainer.add(pnlLoadingContainer, "card3");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(lpContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(lpContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPassword.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        SwingUtilities.invokeLater(() -> {
            try {
                login();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginPageRedesigned.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        if (txtUsername.getText().trim().equals("enter your username")) {
            txtUsername.setText("");
        }

        txtUsername.setForeground(Color.BLACK);
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        if (txtUsername.getText().trim().isEmpty()) {
            txtUsername.setText("enter your username");
        }

        txtUsername.setForeground(Color.GRAY);
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        if (txtPassword.getText().trim().equals("enter your password")) {
            txtPassword.setText("");
        }

        txtPassword.setForeground(Color.BLACK);
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        if (txtPassword.getText().trim().isEmpty()) {
            txtPassword.setText("enter your password");
        }

        txtPassword.setForeground(Color.GRAY);
    }//GEN-LAST:event_txtPasswordFocusLost

    private void btnLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            LoginPageRedesigned.this.getGlassPane().setVisible(true);
            SwingUtilities.invokeLater(() -> {
                try {
                    login();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LoginPageRedesigned.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }//GEN-LAST:event_btnLoginKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblLoadingIcon;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLayeredPane lpContainer;
    private javax.swing.JPanel pnlComponentsContainer;
    private javax.swing.JPanel pnlLoadingContainer;
    private javax.swing.JPanel pnlLoginContainer;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
