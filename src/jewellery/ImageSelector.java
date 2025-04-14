package jewellery;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.FileDialog;
import java.awt.Frame;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import jewellery.LoanEntry;

public class ImageSelector extends javax.swing.JFrame {

    private DefaultListModel<String> imageListModel;
    private JList<String> imageList;
    private JButton uploadButton;
    private JButton closeButton;  // New close button
    private JLabel imageLabel;
    private int imageNumber = 0;

    public ImageSelector() {
        initComponents(); // Auto-generated method
        overrideDefaultCloseOperation();
        initCustomComponents(); // Initialize custom components
    }

    private void overrideDefaultCloseOperation() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dispose();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 497, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 463, Short.MAX_VALUE)
        );

        pack();
    }

    private void initCustomComponents() {
        imageListModel = new DefaultListModel<>();
        imageList = new JList<>(imageListModel);
        uploadButton = new JButton("Upload Image");
        closeButton = new JButton("Close");  // Initialize close button
        imageLabel = new JLabel();

        // Set up the image label
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Create a panel for the left side (list and buttons)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Image List"));

        // Add the list to a scroll pane
        JScrollPane listScrollPane = new JScrollPane(imageList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        // Create a panel for buttons (upload and close)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(uploadButton);
        buttonPanel.add(closeButton);  // Add close button next to upload button
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a panel for the right side (image display)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Selected Image"));
        rightPanel.add(imageLabel, BorderLayout.CENTER);

        // Add both panels to the main frame
        getContentPane().setLayout(new GridLayout(1, 2));
        getContentPane().add(leftPanel);
        getContentPane().add(rightPanel);

        // Add action listeners
        uploadButton.addActionListener(e -> uploadImage());
        closeButton.addActionListener(e -> dispose());  // Close the window when clicked

        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedImage();
            }
        });

        pack();
    }

    private void uploadImage() {
        FileDialog fileDialog = new FileDialog((Frame) null, "Select an Image", FileDialog.LOAD);
        fileDialog.setFilenameFilter((dir, name)
                -> name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".png") || name.endsWith(".gif")
        );

        fileDialog.setVisible(true);

        String selectedFile = fileDialog.getFile();
        String selectedDirectory = fileDialog.getDirectory();
        LoanEntry le = new LoanEntry();

        if (selectedFile != null) {
            String filePath = selectedDirectory + selectedFile;
            imageListModel.addElement(filePath);
            File imagesFolder = new File("assets/" + le.getSelectedPartyName() + "/itemsImages");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs(); // Changed to mkdirs() in case parent directories don't exist
            }
            try {
                Path sourcePath = Paths.get(filePath);
                Path destinationPath = Paths.get("assets/" + le.getSelectedPartyName() + "/itemsImages", "ITEM_PHOTO" + imageNumber + ".png");
                imageNumber++;
                // Handle duplicate by always replacing (as per your requirement)
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                //  imageListModel.addElement(destinationPath.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving image: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displaySelectedImage() {
        String selectedImagePath = imageList.getSelectedValue();
        if (selectedImagePath != null) {
            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage();

            int labelWidth = imageLabel.getWidth();
            int labelHeight = imageLabel.getHeight();

            if (labelWidth > 0 && labelHeight > 0) {
                Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } else {
            imageLabel.setIcon(null);
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ImageSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new ImageSelector().setVisible(true));
    }
}
