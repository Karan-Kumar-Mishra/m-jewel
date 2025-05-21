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
import jewellery.viewItems;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXImageView;
import javax.swing.JFrame;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.BorderLayout;
import javax.swing.*; // If you're using Swing components
import java.awt.*; // This imports all AWT classes, including BorderLayout
import java.io.FileOutputStream;
import java.util.Set;
import java.util.TreeMap;
import jewellery.LoanEntry;
import jewellery.DBController;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jewellery.GetInterestAmount;

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
    public String paths = "";

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
                int totalDays = ((Number) value).intValue();
                int months = totalDays / 30;  // Calculate months
                int remainingDays = totalDays % 30;  // Calculate remaining days
                value = months + "(" + totalDays + ")";  // Format as months(totalDays)
            }
            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }
    }

    public LoanBook() {
        try {
        
            initComponents(); // Initialize all UI components first

            // Initialize date chooser before adding to panel
            // Initialize data with null check
            completeLoanData = GetLoanData.get();
            if (completeLoanData == null) {
                completeLoanData = new Object[0][];
            }

            // Initialize table model and populate data
            DefaultTableModel model = new DefaultTableModel(new Object[0][], columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            jTable1.setModel(model);

            // Populate the table with data
            //          populateTabpopulateTablele(completeLoanData);
            customizeTable(); // Customize after all components exist
            setColumnWidths();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error initializing Loan Book: " + e.getMessage(),
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setColumnWidths() {
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] columnWidths = {
            150, // Date
            100, // No.
            180, // Name
            200, // Loan Amt
            120, // Wt
            100, // Days (made narrower for simple numbers)
            180, // Int. Amt
            180, // Amount + int.
            100 // Current Value
        };

        for (int i = 0; i < columnWidths.length; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
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
                    String dateString = data[i][0].toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(dateString);
                    java.sql.Date startDate = new java.sql.Date(parsedDate.getTime());
                    rowDays = (System.currentTimeMillis() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                }
            } catch (Exception e) {
                rowDays = 0;
            }

            // Map data to display columns
            displayData[i][0] = data[i][0]; // START_DATE
            displayData[i][1] = data[i][1]; // SLIP_NO
            displayData[i][2] = data[i][2]; // PARTY_NAME

            // Handle numeric values with null checks
            try {
                displayData[i][3] = (data[i].length > 11 && data[i][12] != null)
                        ? Double.parseDouble(data[i][12].toString())
                        : 0.00; // AMOUNT_PAID
            } catch (NumberFormatException e) {
                displayData[i][3] = 0.00;
            }

            try {
                displayData[i][4] = (data[i].length > 9 && data[i][10] != null)
                        ? Double.parseDouble(data[i][10].toString())
                        : 0.00; // NET_WEIGHT
            } catch (NumberFormatException e) {
                displayData[i][4] = 0.0;
            }
            try {
                //FOR DATE
                displayData[i][5] = rowDays;
            } catch (NumberFormatException e) {
                displayData[i][5] = 0.0;
            }
            try {
                

                //FOR interest amount
                double loanAmt = (data[i].length > 11 && data[i][12] != null)
                        ? Double.parseDouble(data[i][12].toString()) : 0.00;
                double intAmt = (data[i].length > 6 && data[i][5] != null)
                        ? Double.parseDouble(data[i][5].toString())
                        : 0.00;

                double dailyInterest = (loanAmt * intAmt / 100) / 30;

                double totalInterest;
                if ((data[i][6].toString().equals("Day"))) {
                    totalInterest = (dailyInterest * (rowDays) / 30) * rowDays;// dayley 

                } else {
                    long month = rowDays / 30;
                    totalInterest = (dailyInterest * (rowDays)) * month;//mothly  
                }

              

                //displayData[i][6] = totalInterest;
                displayData[i][6] = data[i][21];
            } catch (NumberFormatException e) {
                displayData[i][6] = 0.0;
            }
            try {
                //FOR interest amount+loan amnt
                double loanAmt = (displayData[i][3] instanceof Number)
                        ? ((Number) displayData[i][3]).doubleValue()
                        : 0.00;

                double totalInterest = (displayData[i][6] instanceof Number)
                        ? ((Number) displayData[i][6]).doubleValue()
                        : 0.00;

                displayData[i][7] = totalInterest + loanAmt;
            } catch (NumberFormatException e) {
                displayData[i][7] = 0.0;
            }
            try {
                //FOR current value
                displayData[i][8] = 0.0;
            } catch (NumberFormatException e) {
                displayData[i][8] = 0.0;
            }

        }

        DefaultTableModel model = new DefaultTableModel(displayData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                }
                if (columnIndex == 5) {  // Days column
                    return Integer.class;
                }
                if (columnIndex >= 3 && columnIndex <= 8) {
                    return Double.class;
                }
                return Object.class;
            }
        };

        jTable1.setModel(model);
        setColumnWidths();
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new DaysRenderer());

        // Also ensure other renderers are set
        jTable1.setDefaultRenderer(Double.class, new DecimalRenderer());
        jTable1.setDefaultRenderer(java.sql.Date.class, new DateRenderer());
    }

    private void filterByDate() {
        java.util.Date selectedDate = jDateChooser1.getDate();

        if (selectedDate == null) {
            // If no date is selected, show all data
            populateTable(completeLoanData);
            return;
        }

        // Convert selected date to SQL date for comparison
        java.sql.Date sqlSelectedDate = new java.sql.Date(selectedDate.getTime());

        if (completeLoanData == null || completeLoanData.length == 0) {
            populateTable(new Object[0][]); // Show empty table if no data
            return;
        }

        // Filter the data based on date range (start date <= selected date)
        Object[][] filteredData = Arrays.stream(completeLoanData)
                .filter(row -> row != null && row.length > 1 && row[1] != null) // Check for valid row and date exists
                .filter(row -> {
                    try {
                        // The date to compare is in the original data at index 1 (START_DATE)
                        Object dateObj = row[1];

                        if (dateObj == null) {
                            return false;
                        }

                        java.util.Date rowDate;

                        if (dateObj instanceof java.sql.Date) {
                            rowDate = (java.sql.Date) dateObj;
                        } else if (dateObj instanceof String) {
                            // Try to parse the string date
                            SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
                            rowDate = parseFormat.parse(dateObj.toString());
                        } else if (dateObj instanceof java.util.Date) {
                            rowDate = (java.util.Date) dateObj;
                        } else {
                            return false;
                        }

                        // Compare dates - include if row date is on or before selected date
                        return !rowDate.after(sqlSelectedDate);
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
            setColumnWidths();
            JOptionPane.showMessageDialog(this,
                    "No records found before or on: " + new SimpleDateFormat("dd-MM-yyyy").format(selectedDate),
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
        setColumnWidths();

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
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new DaysRenderer());

        // Special renderer for Days column (simple number format)
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click detected
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < completeLoanData.length) {
                        openLoanEntryWindow(completeLoanData[selectedRow]);
                    }
                }
            }
        });
        // Customize other components
        customizeComponents();
    }

    private void openLoanEntryWindow(Object[] loanData) {
        // First validate if loanData exists and has enough elements
        if (loanData == null || loanData.length < 20) {
            JOptionPane.showMessageDialog(null,
                    "Invalid loan data provided!\nExpected 20 fields but got "
                    + (loanData == null ? "null" : loanData.length),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a formatted message to display all loan data
            StringBuilder message = new StringBuilder();
            message.append("<html><body><h3>Loan Data Review</h3>");
            message.append("<table border='1' cellpadding='5'>");

            // Define field labels for display
            String[] fieldLabels = {
                "Entry Date", "Slip No", "Party Name", "Remarks",
                "Start Date", "Interest Rate", "Weight Type", "Gold Weight",
                "Purity", "Net Weight", "Estimated Cost", "Amount Paid",
                "Item Details", "Guarantor Name", "Guarantor Address",
                "Guarantor Phone", "Documents", "Reminders", "Notes", "Item Location"
            };

            // Add each field to the display table
            for (int i = 0; i < fieldLabels.length; i++) {
                message.append("<tr>" + i + "<td><b>").append(fieldLabels[i]).append("</b></td>")
                        .append("<td>").append(loanData[i] != null ? loanData[i].toString() : "N/A")
                        .append("</td></tr>");
            }
            message.append("</table></body></html>");

            // Show confirmation dialog with all data
//            int option = JOptionPane.showConfirmDialog(
//                    null,
//                    message.toString(),
//                    "Confirm Loan Data",
//                    JOptionPane.OK_CANCEL_OPTION,
//                    JOptionPane.INFORMATION_MESSAGE
//            );
            // Only proceed if user confirms
            // if (option == JOptionPane.OK_OPTION) {
            if (true) {
                UpdateLoan lup = new UpdateLoan();
                lup.setInfo(
                        loanData[0].toString(), // entryDate
                        loanData[1].toString(), // slipNo
                        loanData[2].toString(), // partyName
                        loanData[3].toString(), // remarks
                        loanData[4].toString(), // startDate
                        loanData[5].toString(), // interestDatePercentage
                        loanData[6].toString(), //  interst type  
                        loanData[7].toString(), // weightType 
                        loanData[8].toString(), //goldWeight 
                        loanData[9].toString(), // purity 
                        loanData[10].toString(), // netWeight 
                        loanData[11].toString(), //estimatedCost 
                        loanData[12].toString(), //amountPaid 
                        loanData[13].toString(), // itemDetails 
                        loanData[14].toString(), // guarantorName 
                        loanData[15].toString(), // guarantorAddress 
                        loanData[16].toString(), //guarantorPhone 
                        loanData[17].toString(), // documents 
                        loanData[18].toString(), // reminders 
                        loanData[19].toString(), // notes 
                        loanData[20].toString()//itemLocation
                );
                UpdateLoan.deleteLonaEntry(loanData[2].toString());

                lup.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Loan update cancelled by user",
                        "Cancelled",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error processing loan data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        jButton5.setBackground(Color.red);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        filterByDate();
    }
    String selectedPartyname = "not found";

    public String GetSelectedPartyName() {

        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < completeLoanData.length) {
            Object[] rowData = completeLoanData[selectedRow];
            selectedPartyname = getStringValue(rowData, 2);
        }
        return selectedPartyname;
    }

    private void showSelectedLoanDetails() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < completeLoanData.length) {
            Object[] rowData = completeLoanData[selectedRow];

            // Create a table model for the JOptionPane
            String[] columnNames = {"Field", "Value"};
            Object[][] data = {
                {"1:", getStringValue(rowData, 1)},
                {"2:", getStringValue(rowData, 2)},
                {"3: ", getStringValue(rowData, 3)},
                {"4: ", getStringValue(rowData, 4)},
                {"5: ", getStringValue(rowData, 5)},
                {"6: ", getStringValue(rowData, 6)},
                {"7: ", getStringValue(rowData, 7)},
                {"Purity", getStringValue(rowData, 8)},
                {"Net Weight", getStringValue(rowData, 9)},
                {"Gross Weight", getStringValue(rowData, 10)},
                {"Item Details", getStringValue(rowData, 2)},
                {"Guarantor Name", getStringValue(rowData, 14)},
                {"Guarantor Address", getStringValue(rowData, 15)},
                {"Documents", getStringValue(rowData, 16)},
                {"Item Location", getStringValue(rowData, 19)},
                {"20", getStringValue(rowData, 20)}
            };

            JTable detailTable = new JTable(data, columnNames);
            detailTable.setEnabled(false); // Make the table non-editable
            detailTable.setFillsViewportHeight(true);
            detailTable.setRowHeight(25); // Set appropriate row height

            // Set preferred width for columns
            detailTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            detailTable.getColumnModel().getColumn(1).setPreferredWidth(250);

            JScrollPane scrollPane = new JScrollPane(detailTable);
            scrollPane.setPreferredSize(new Dimension(450, 200));

            // JOptionPane.showMessageDialog(this, scrollPane, "Loan Details", JOptionPane.INFORMATION_MESSAGE);
            // Update labels as before
            jLabel21.setText(getStringValue(rowData, 9)); // PURITY 
            jLabel22.setText(getStringValue(rowData, 10)); // net weight 
            jLabel9.setText(getStringValue(rowData, 8)); // NET_WEIGHT (index 10)
            jLabel34.setText(getStringValue(rowData, 13)); // ITEM_DETAILS (index 13)
            jLabel27.setText(getStringValue(rowData, 14)); // GUARNATOR_NAME (index 14)
            jLabel28.setText(getStringValue(rowData, 15)); // GUARNATOR_ADDRESS (index 15)
            jLabel29.setText(getStringValue(rowData, 17)); // DOCUMENTS (index 16)
            jLabel30.setText(getStringValue(rowData, 20)); // ITEM_LOCATION (index 19)
            // Also show the party name (from index 3)
            jLabel4.setText("Party Information: " + getStringValue(rowData, 2));

            String destinationPath = "assets/" + getStringValue(rowData, 2) + "/" + "GUARNATOR_PHOTO.png";
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
            double interestRate = Double.parseDouble(rowData[5].toString());
            // Get and parse the start date string
            String dateString = rowData[0].toString();

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
        jButton7 = new javax.swing.JButton();

        jLabel1.setText("LOAN BOOK");

        jCheckBox1.setText("Select All");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

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
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton2)
                                .addComponent(jButton4)
                                .addComponent(jButton5)
                                .addComponent(jButton6)
                                .addComponent(jButton1)
                                .addComponent(jButton3))
                            .addGap(19, 19, 19))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))))
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

        jButton7.setText("View items");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
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
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
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
                                .addGap(73, 73, 73))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel5)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel34)
                        .addGap(102, 102, 102))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                            .addComponent(jLabel30))
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel14))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jXImageView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9))
                                    .addGroup(layout.createSequentialGroup()
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
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton7)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31)
                        .addGap(71, 71, 71)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
      private void showImagePopup(JFrame parent, java.awt.event.MouseEvent evt) {
        JXImageView source = (JXImageView) evt.getSource();
        String imagePath = (String) source.getClientProperty("imagePath");

        try {
            // Create popup dialog
            JDialog popup = new JDialog(parent, "Image View", true);
            popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Load the image in original size
            BufferedImage img = ImageIO.read(new File(imagePath));
            JXImageView largeImageView = new JXImageView();
            largeImageView.setImage(img);

            // Add to scroll pane in case image is very large
            JScrollPane scrollPane = new JScrollPane(largeImageView);
            popup.add(scrollPane);

            // Set dialog size
            popup.setSize(800, 600);
            popup.setLocationRelativeTo(parent);
            popup.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error loading image: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    int imageNumber = 0;

    private void loadImages(JFrame frame, JScrollPane scrollPane) {
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 3, 5, 5)); // 3 columns, 5px gap

        String partyName = GetSelectedPartyName();
        if (partyName == null || partyName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No party selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use ArrayList to dynamically handle images
        List<String> imagePaths = new ArrayList<>();

        // Add default image first
        // imagePaths.add("C:/Users/91888/Pictures/Default.jpg");
        // Try to find item images
        Path imageDir = Paths.get("assets/" + partyName + "/itemsImages/");
        if (Files.exists(imageDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(imageDir, "ITEM_PHOTO*.png")) {
                for (Path path : stream) {
                    imagePaths.add(path.toString());
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading image directory: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Load all found images
        for (String path : imagePaths) {
            try {
                BufferedImage img = ImageIO.read(new File(path));
                if (img != null) {
                    JXImageView imageView = new JXImageView();
                    imageView.setImage(img);
                    imageView.setPreferredSize(new Dimension(200, 200));
                    imageView.putClientProperty("imagePath", path);
                    imageView.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            showImagePopup(frame, evt);
                        }
                    });
                    imagePanel.add(imageView);
                }
            } catch (IOException ex) {
                System.err.println("Couldn't load image: " + path);
            }
        }

        scrollPane.setViewportView(imagePanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    public void showViewItems() {
        // Create the frame
        JFrame frame = new JFrame("View Items");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Set layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load images
        loadImages(frame, scrollPane);

        // Set frame properties
        frame.pack();
        frame.setSize(850, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void exportToExcel() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");

        // Set default filename with timestamp
        String defaultFileName = "LoanBook_Export_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date())
                + ".xlsx";
        fileChooser.setSelectedFile(new File(defaultFileName));

        // Set file filter for Excel files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx"));

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        File fileToSave = fileChooser.getSelectedFile();
        paths = fileToSave.getAbsolutePath();

        // Ensure the file has .xlsx extension
        if (!paths.toLowerCase().endsWith(".xlsx")) {
            paths += ".xlsx";
            fileToSave = new File(paths);
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet ws = wb.createSheet("Loan Data");
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // Create header row
            XSSFRow headerRow = ws.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                headerRow.createCell(col).setCellValue(model.getColumnName(col));
            }

            // Create data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                XSSFRow dataRow = ws.createRow(row + 1); // +1 to skip header

                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    XSSFCell cell = dataRow.createCell(col);

                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // Auto-size columns
            for (int col = 0; col < model.getColumnCount(); col++) {
                ws.autoSizeColumn(col);
            }

            // Write the file
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                wb.write(fos);
                JOptionPane.showMessageDialog(this,
                        "File exported successfully to:\n" + fileToSave.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error writing file:\n" + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating Excel file:\n" + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        showViewItems();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
        exportToExcel();
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
    private javax.swing.JButton jButton7;
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
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXImageView jXImageView1;
    // End of variables declaration//GEN-END:variables
    private BufferedImage image;

}
