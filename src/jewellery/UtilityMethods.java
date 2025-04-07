package jewellery;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author AR-LABS
 */
public class UtilityMethods {
    
    private static ImageIcon image;
    private static DateTimeFormatter dateTimeFormatter;
    private static LocalDateTime localDateTime;
    private static final ProcessBuilder PROCESS_BUILDER = new ProcessBuilder();
    private static Process proc;
    
    public static String typeOfVar(Object obj) {
        return obj.getClass().getSimpleName();
    }
    
    public static boolean inputOnlyContainsNumbers(String inputText) {
        return inputText.matches("[-+]?[0-9]*\\.?[0-9]*");
    }
      
    public static boolean inputOnlyContainsAlphabets(String inputText) {
        return inputText.matches("^[a-zA-Z\\s]*$");
    }
    
    public static boolean inputContainsAlphabetsAndNumbers(String inputText) {
        return inputText.matches("^[a-zA-Z0-9\\s]*$");
    }
    
    public static boolean isTextFieldEmpty(javax.swing.JTextField comp) {
        return comp.getText().trim().isEmpty();
    }
    
    public static boolean hasDateBeenPicked(com.toedter.calendar.JDateChooser datePicker) {
        return datePicker.getDate() != null;
    }
    
    public static void setImageOnJLabel(javax.swing.JLabel component, String resourceLocation) {
        image = new ImageIcon(new ImageIcon(resourceLocation)
                .getImage().getScaledInstance(component.getWidth(), 
                        component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(image);
    }
    
    public static void setImageOnJButton(javax.swing.JButton component, String resourceLocation) {
        image = new ImageIcon(new ImageIcon(resourceLocation)
                .getImage().getScaledInstance(component.getWidth(), 
                        component.getHeight(), Image.SCALE_SMOOTH));
        component.setIcon(image);
    }
       
    public static double getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 ;
    }
    
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    public static String[] selectFileAndReturnLocation(String fileType, 
            String fileTypeExtension) {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        
        int returnVal = fileChooser.showOpenDialog(null);
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return new String[]{fileChooser.getSelectedFile().getAbsolutePath(),
            fileChooser.getSelectedFile().getName(), String.valueOf(getFileSizeKiloBytes(fileChooser.getSelectedFile()))};
        }
        
        return null;
    }
    
    public static void startMySQLServer(String executablePath) {
        PROCESS_BUILDER.command(executablePath);
        
        try {
            proc = PROCESS_BUILDER.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }   
    }
    
    public static String getCurrentDate(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        localDateTime = LocalDateTime.now();
        
        return dateTimeFormatter.format(localDateTime);
    }
    
    public static void showReport(String reportLocation, String query, 
            String databaseAddress, String username, String password) {
        
        Connection connectionObject = null;
        
        connectionObject = DBConnect.connect();
        
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(UtilityMethods.class.getClass().getResourceAsStream(reportLocation));
            
            if(!(query != null && query.trim().isEmpty() && query.trim().equals(" "))) {
                JRDesignQuery reportDataQuery = new JRDesignQuery();
                reportDataQuery.setText(query);
                jasperDesign.setQuery(reportDataQuery);
            }
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint;
            
            jasperPrint = JasperFillManager.fillReport(jasperReport, null, connectionObject);
            
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public static float numberToDecimalPoint(float val) {
        if(val > 0) {
            return val / 100;
        }
        
        return 0.0f;
    }
}
