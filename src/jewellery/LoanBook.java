package jewellery;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.JXImageView; // For SwingX component
import javax.swing.*; // For JOptionPane
import java.awt.Image; // For manual scaling
import java.awt.image.BufferedImage; // For image handling
import javax.imageio.ImageIO; // For reading images
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;

public class LoanBook extends javax.swing.JPanel {

    // Custom cell renderer for dates
    // Custom cell renderer for decimal numbers
    private final String[] columnNames = {
        "Date",
        "No.",
        "Name",
        "Loan Amt",
        "Wt",
        "Days",
        "Int. Amt",
        "Loan Amount + int.",
        "Current Value"
    };
    private Object[][] completeLoanData;
    private com.toedter.calendar.JDateChooser todate;

    class DateRenderer extends DefaultTableCellRenderer {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof java.sql.Date) {
                value = dateFormat.format(value);
            } else if (value instanceof java.util.Date) {
                value = dateFormat.format(new java.sql.Date(((java.util.Date) value).getTime()));
            }
            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }
    }

    class DecimalRenderer extends DefaultTableCellRenderer {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                value = decimalFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }
    }

    class DaysRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                value = ((Number) value).intValue(); // Convert to simple integer
            }
            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }
    }

    public LoanBook() {
        try {
            initComponents(); // Initialize all UI components first

            // Initialize date chooser before adding to panel
            todate = new com.toedter.calendar.JDateChooser();
            todate.setDateFormatString("dd-MM-yyyy");
            todate.setVisible(true);
            //   todate.setPreferredSize(new Dimension(150, 25));

            // Initialize data with null check
            completeLoanData = GetLoanData.get();
            if (completeLoanData == null) {
                completeLoanData = new Object[0][];
            }

            // Initialize table model and populate data
            DefaultTableModel model = new DefaultTableModel(new Object[0][], columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            };
            jTable1.setModel(model);

            // Populate the table with data
            // populateTable(completeLoanData);
            customizeTable(); // Customize after all components exist

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error initializing Loan Book: " + e.getMessage(),
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTable(Object[][] data) {
        if (data == null || data.length == 0) {
            jTable1.setModel(new DefaultTableModel(new Object[0][], columnNames));
            return;
        }

        Object[][] displayData = new Object[data.length][columnNames.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null || data[i].length < 4) {
                continue;
            }

            // Calculate days for this row
            long rowDays = 0;
            try {
                if (data[i][1] != null) { // START_DATE is at index 1
                    String dateString = data[i][1].toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(dateString);
                    java.sql.Date startDate = new java.sql.Date(parsedDate.getTime());
                    rowDays = (System.currentTimeMillis() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                }
            } catch (Exception e) {
                rowDays = 0;
            }

            // Map data to display columns
            displayData[i][0] = data[i][1]; // START_DATE
            displayData[i][1] = data[i][2]; // SLIP_NO
            displayData[i][2] = data[i][3]; // PARTY_NAME

            // Handle numeric values with null checks
            try {
                displayData[i][3] = (data[i].length > 12 && data[i][12] != null)
                        ? Double.parseDouble(data[i][12].toString())
                        : 0.00; // AMOUNT_PAID
            } catch (NumberFormatException e) {
                displayData[i][3] = 0.00;
            }

            try {
                displayData[i][4] = (data[i].length > 10 && data[i][10] != null)
                        ? Double.parseDouble(data[i][10].toString())
                        : 0.00; // NET_WEIGHT
            } catch (NumberFormatException e) {
                displayData[i][4] = 0.00;
            }

            displayData[i][5] = rowDays; // Calculated days

            try {
                displayData[i][6] = (data[i].length > 6 && data[i][6] != null)
                        ? Double.parseDouble(data[i][6].toString())
                        : 0.00; // INTEREST_DATE_PERCENTAGE
            } catch (NumberFormatException e) {
                displayData[i][6] = 0.00;
            }

            // Calculate Amount + interest
            try {
                double loanAmt = (displayData[i][3] instanceof Number)
                        ? ((Number) displayData[i][3]).doubleValue()
                        : 0.00;
                double intAmt = (displayData[i][6] instanceof Number)
                        ? ((Number) displayData[i][6]).doubleValue()
                        : 0.00;
                displayData[i][7] = loanAmt + intAmt;
            } catch (Exception e) {
                displayData[i][7] = 0.00;
            }

            displayData[i][8] = 0.00; // Current Value (placeholder)
        }

        DefaultTableModel model = new DefaultTableModel(displayData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Make table non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                }
                if (columnIndex >= 3 && columnIndex <= 8) {
                    return Double.class;
                }
                return Object.class;
            }
        };

        jTable1.setModel(model);
    }

    private void filterByDate() {
        java.util.Date selectedDate = todate.getDate();

        if (selectedDate == null) {
            // If no date is selected, show all data
            populateTable(completeLoanData);
            return;
        }

        // Convert selected date to SQL date for comparison
        java.sql.Date sqlSelectedDate = new java.sql.Date(selectedDate.getTime());

        // Format for display (matches your DateRenderer format)
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
        // Format for parsing dates from your data
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (completeLoanData == null || completeLoanData.length == 0) {
            populateTable(new Object[0][]); // Show empty table if no data
            return;
        }

        // Filter the data based on the selected date
        Object[][] filteredData = Arrays.stream(completeLoanData)
                .filter(row -> row != null && row.length > 1 && row[0] != null) // Check for valid row and date exists (date is at index 0 in display)
                .filter(row -> {
                    try {
                        // The date to compare is in the display data at index 0
                        Object dateObj = row[0]; // This is the date from the display data

                        if (dateObj == null) {
                            return false;
                        }

                        java.util.Date rowDate;

                        if (dateObj instanceof java.sql.Date) {
                            rowDate = (java.sql.Date) dateObj;
                        } else if (dateObj instanceof String) {
                            // Try to parse the string date
                            try {
                                rowDate = parseFormat.parse(dateObj.toString());
                            } catch (Exception e) {
                                // If parsing fails, try display format
                                rowDate = displayFormat.parse(dateObj.toString());
                            }
                        } else if (dateObj instanceof java.util.Date) {
                            rowDate = (java.util.Date) dateObj;
                        } else {
                            return false;
                        }

                        // Compare just the dates (ignore time)
                        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyyMMdd");
                        String selectedDateStr = compareFormat.format(sqlSelectedDate);
                        String rowDateStr = compareFormat.format(rowDate);

                        return selectedDateStr.equals(rowDateStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .toArray(Object[][]::new);

        // Populate the table with filtered data
        populateTable(filteredData);

        // Show message if no results found
        if (filteredData.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "No records found for date: " + displayFormat.format(selectedDate),
                    "No Data",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public javax.swing.JPanel getContentPane() {
        return this;
    }

    private void customizeTable() {
        jTable1.setRowHeight(25);
        jTable1.setShowGrid(true);
        jTable1.setGridColor(Color.LIGHT_GRAY);
        jTable1.setSelectionBackground(new Color(51, 153, 255));
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths according to the image
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] columnWidths = {
            100, // Date
            80, // No.
            120, // Name
            100, // Loan Amt
            100, // Wt
            80, // Days (made narrower for simple numbers)
            100, // Int. Amt
            120, // Amount + int.
            100 // Current Value
        };

        for (int i = 0; i < columnWidths.length; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Center align all columns except Name
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            if (i != 2) { // Don't center the Name column
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Right align numeric columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Loan Amt
        jTable1.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Wt
        jTable1.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Int. Amt
        jTable1.getColumnModel().getColumn(7).setCellRenderer(rightRenderer); // Amount + int.
        jTable1.getColumnModel().getColumn(8).setCellRenderer(rightRenderer); // Current Value

        // Special renderer for Days column (simple number format)
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new DaysRenderer());

        jScrollPane1.setViewportView(jTable1);

        // Add selection listener to show details when a row is selected
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedLoanDetails();
            }
        });

        // Custom renderer for numbers
        jTable1.setDefaultRenderer(Double.class, new DecimalRenderer());
        jTable1.setDefaultRenderer(java.sql.Date.class, new DateRenderer());
        // Customize other components
        customizeComponents();
    }

    private void customizeComponents() {
        jPanel1.setBackground(new java.awt.Color(56, 68, 76));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel2.setForeground(java.awt.Color.white);
        jLabel3.setForeground(java.awt.Color.white);
        jCheckBox1.setForeground(java.awt.Color.BLACK);
        jLabel1.setFont(new Font("Serif", Font.BOLD, 24));

        jLabel1.setBackground(java.awt.Color.BLACK);
        jCheckBox1.setFont(new Font("Serif", Font.BOLD, 15));
        jLabel4.setFont(new Font("Serif", Font.BOLD, 20));
        jLabel4.setVerticalAlignment(SwingConstants.CENTER);
        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setBackground(new java.awt.Color(56, 68, 76));
        jLabel4.setForeground(java.awt.Color.white);
        jLabel14.setForeground(java.awt.Color.white);

        jLabel4.setOpaque(true);
        jLabel14.setBackground(new java.awt.Color(56, 68, 76));
        jLabel14.setOpaque(true);
        Font boldFont = new Font("Serif", Font.BOLD, 12);
        jLabel23.setFont(boldFont);
        jLabel24.setFont(boldFont);
        jLabel25.setFont(boldFont);
        jLabel26.setFont(boldFont);

        // Right-align numeric values
        jLabel23.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel24.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel25.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel26.setHorizontalAlignment(SwingConstants.RIGHT);

        // Set foreground color
        Color valueColor = new Color(0, 0, 200); // Dark blue
        jLabel23.setForeground(valueColor);
        jLabel24.setForeground(valueColor);
        jLabel25.setForeground(valueColor);
        jLabel26.setForeground(valueColor);
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        filterByDate();
    }

    private void showSelectedLoanDetails() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < completeLoanData.length) {
            Object[] rowData = completeLoanData[selectedRow];

            // Map database columns to labels
            jLabel21.setText(getStringValue(rowData, 9)); // GOLD_WEIGHT (index 8)
            jLabel22.setText(getStringValue(rowData, 10)); // PURITY (index 9)
            jLabel9.setText(getStringValue(rowData, 11)); // NET_WEIGHT (index 10)
            jLabel34.setText(getStringValue(rowData, 13)); // ITEM_DETAILS (index 13)
            jLabel27.setText(getStringValue(rowData, 14)); // GUARNATOR_NAME (index 14)
            jLabel28.setText(getStringValue(rowData, 15)); // GUARNATOR_ADDRESS (index 15)
            jLabel29.setText(getStringValue(rowData, 16)); // DOCUMENTS (index 16)
            jLabel30.setText(getStringValue(rowData, 19)); // ITEM_LOCATION (index 19)

            // Also show the party name (from index 3)
            jLabel4.setText("Party Information: " + getStringValue(rowData, 3));
            String destinationPath = "assets/" + getStringValue(rowData, 3) + "/" + "GUARNATOR_PHOTO.png";
            try {
                BufferedImage image = ImageIO.read(new File(destinationPath));

                // Basic scaling approach
                Image scaledImage = image.getScaledInstance(
                        jXImageView1.getWidth(),
                        jXImageView1.getHeight(),
                        Image.SCALE_SMOOTH);
                jXImageView1.setImage(scaledImage);

                jXImageView1.revalidate();
                jXImageView1.repaint();
            } catch (IOException e) {
                e.printStackTrace();

                // Handle error - maybe show a placeholder or error message
                JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            calculateAndDisplayInterest(selectedRow);
        }
    }

    private String getStringValue(Object[] row, int index) {
        return (index < row.length && row[index] != null) ? row[index].toString() : "";
    }

    private void calculateAndDisplayInterest(int row) {
        try {
            Object[] rowData = completeLoanData[row];

            // Get loan amount - using index 12 as per your structure
            double loanAmount = Double.parseDouble(rowData[12].toString());

            // Get interest rate - using index 6 as per your structure
            double interestRate = Double.parseDouble(rowData[6].toString());
            // Get and parse the start date string
            String dateString = rowData[1].toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // adjust format to match your date string
            java.util.Date parsedDate = dateFormat.parse(dateString);
            java.sql.Date startDate = new java.sql.Date(parsedDate.getTime());

            // Calculate days between start date and current date
            long days = (System.currentTimeMillis() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            // Calculate interest values
            double dailyInterest = (loanAmount * interestRate / 100) / 30;
            double monthlyInterest = loanAmount * interestRate / 100;
            double totalInterest = dailyInterest * days;

            // Format values with 2 decimal places
            DecimalFormat df = new DecimalFormat("#,##0.00");

            // Set values in the labels
            jLabel23.setText(df.format(totalInterest)); // Interest Amt
            jLabel24.setText(df.format(dailyInterest)); // Interest Per Day
            jLabel25.setText(df.format(monthlyInterest)); // Int. Per Month
            jLabel26.setText(df.format(interestRate) + "%"); // Interest Rate

        } catch (Exception e) {
            Logger.getLogger(LoanBook.class.getName()).log(Level.SEVERE, "Error calculating interest", e);
            // Set default/error values
            jLabel23.setText("0.00");
            jLabel24.setText("0.00");
            jLabel25.setText("0.00");
            jLabel26.setText("0%");

            JOptionPane.showMessageDialog(this,
                    "Error calculating interest. Please check:\n"
                    + "1. Loan Amount is at index 12\n"
                    + "2. Interest Rate is at index 7\n"
                    + "3. Start Date format at index 1 (expected yyyy-MM-dd)\n"
                    + "Error: " + e.getMessage(),
                    "Calculation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePainter1 = new org.jdesktop.swingx.painter.ImagePainter();
        imagePainter2 = new org.jdesktop.swingx.painter.ImagePainter();
        imagePainter3 = new org.jdesktop.swingx.painter.ImagePainter();
        imagePainter4 = new org.jdesktop.swingx.painter.ImagePainter();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jXImageView1 = new org.jdesktop.swingx.JXImageView();
        jLabel9 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();

        jLabel1.setText("LOAN BOOK");

        jCheckBox1.setText("Select All");

        jLabel2.setText("Gold Rate:");

        jLabel3.setText("Silver Rate:");

        jButton1.setText("OK");

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Export");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("SMS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Close");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addGap(94, 94, 94)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel3))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setText("Party Information");

        jLabel10.setText("Guarantor name:");

        jLabel11.setText("Guarantor Address:");

        jLabel12.setText("Document/Proof:");

        jLabel13.setText("Item Location:");

        jLabel14.setText("Ledger Balance");

        jLabel15.setText("Interest Amt:");

        jLabel16.setText("Interest Per Day:");

        jLabel17.setText("Int. Per Month:");

        jLabel18.setText("Interest Rate:");

        jLabel19.setText("Purity[%]:");

        jLabel20.setText("Gold Wt:");

        jLabel6.setText("Net Wt:");

        jLabel8.setText("Item Name:");

        javax.swing.GroupLayout jXImageView1Layout = new javax.swing.GroupLayout(jXImageView1);
        jXImageView1.setLayout(jXImageView1Layout);
        jXImageView1Layout.setHorizontalGroup(
            jXImageView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );
        jXImageView1Layout.setVerticalGroup(
            jXImageView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        jLabel31.setText("Guarantor Photo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jXImageView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                        .addGap(0, 117, Short.MAX_VALUE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(73, 73, 73)
                        .addComponent(jLabel5)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel34))
                                .addGap(102, 102, 102))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel15)
                                                    .addComponent(jLabel23))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel20)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel24))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel19)
                                                    .addComponent(jLabel21)
                                                    .addComponent(jLabel17)
                                                    .addComponent(jLabel25))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel22)
                                                    .addComponent(jLabel18)
                                                    .addComponent(jLabel26))
                                                .addGap(20, 20, 20)
                                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel10)
                                                    .addComponent(jLabel27))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel11)
                                                    .addComponent(jLabel28))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel12)
                                                    .addComponent(jLabel29))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jLabel30))))
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel14)
                                        .addGap(0, 12, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jXImageView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(71, 71, 71)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        javax.swing.JTabbedPane tabbedPane = (javax.swing.JTabbedPane) this.getParent();
        if (tabbedPane != null) {
            // Remove this tab
            tabbedPane.remove(this);
        }
        // TODO add your handling code here:

    }// GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        completeLoanData = GetLoanData.get();
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

    }// GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.painter.ImagePainter imagePainter1;
    private org.jdesktop.swingx.painter.ImagePainter imagePainter2;
    private org.jdesktop.swingx.painter.ImagePainter imagePainter3;
    private org.jdesktop.swingx.painter.ImagePainter imagePainter4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXImageView jXImageView1;
    // End of variables declaration//GEN-END:variables
    private BufferedImage image;

}
