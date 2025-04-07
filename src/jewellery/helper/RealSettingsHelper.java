/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jewellery.DBConnect;

/**
 *
 * @author SACHIN MISHRA
 */
public class RealSettingsHelper {
    
    public static boolean gettagNoIsTrue(){
         
        Connection con = null;
        con = DBConnect.connect();
        ResultSet re = null;
        PreparedStatement pstm = null;
        boolean tagno=false ;
        try {
            String sql = "select * from hidingFields";
            pstm = con.prepareStatement(sql);
            re = pstm.executeQuery();
            while (re.next()) {
                tagno = re.getBoolean(2);
            }
            re.close();
            con.close();
            pstm.close();
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (re != null) {
                    re.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tagno;
    }
}
    

