/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import jewellery.DBConnect;

/**
 *
 * @author SACHIN MISHRA
 */
public class outstandingAnalysisHelper {

    static double balance = 0.0;

    private static String getGroupName(String partyname) {
        String grp = null;
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "select distinct grp from account where accountname='" + partyname + "'";
            ResultSet re = stmt.executeQuery(sql);
            while (re.next()) {
                grp = re.getString("grp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grp;
    }
    private static List<tableObject> initialTableData = new ArrayList<>();

    public static String fillTableInDateGivenParty(String party) throws ParseException {
//        ArrayList<String> data = new ArrayList<>();
        balance = 0.0;
//JOptionPane.showMessageDialog(null,party);
          try {

//            m.setRowCount(0);

         Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            Connection c = DBConnect.connect();
            Statement s = c.createStatement();
            String query = "select distinct(bill) from sales where partyname = '" + party + "'";
            if (!party.equals("Cash")) {
                query += " ";
            }
            if (party.equals("Cash")) {
                query = "select distinct(bill) from sales where terms='Cash'";
            }
//JOptionPane.showMessageDialog(this, query);
            ResultSet rs = stmt.executeQuery(query);
            DecimalFormat df = new DecimalFormat("0.00");
            while (rs.next()) {
                Statement st = con.createStatement();
                int billno = rs.getInt("bill");
                query = "select sum(netamount),date,partyname,receivedamount,terms from sales where bill=" + billno;
//                query = "select sum(sales.netamount),sales.date,sales.partyname from sales join receipt on sales.bill=receipt.sales_Bill where not receipt.sales_Bill=" + billno;

                ResultSet rest = st.executeQuery(query);
                Date date = new Date();
                String partyName = "";
                double exchangeAmt = 0;
                double netamount = 0;
                double receivedamt = 0;
                String terms = "";
                while (rest.next()) {
                    date = rest.getDate("date");
                    netamount = rest.getDouble("sum(netamount)");
                    partyName = rest.getString("partyname");
                    receivedamt = rest.getDouble("receivedamount");
                    terms = rest.getString("terms");

//                    date = rest.getDate("sales.date");
//                    netamount = rest.getDouble("sum(sales.netamount)");
//                      partyName=rest.getString("sales.partyname");
                }
                rest.close();
                st.close();
                String q = "select total from exchange where bill='" + billno + "'";
                Statement stmtexchange = con.createStatement();
                ResultSet rex = stmtexchange.executeQuery(q);
                while (rex.next()) {
                    exchangeAmt = rex.getDouble(1);
                }
                rex.close();
                stmtexchange.close();
                String remark = "Bill No." + billno;
                if (party.equals("Cash")) {
                    Statement stmtt = con.createStatement();
                    Statement stmttt = con.createStatement();
                    ResultSet r = stmtt.executeQuery("select sales_Bill from receipt where sales_Bill=" + billno + "");
                    ResultSet r2 = stmttt.executeQuery("select sales_Bill from bankledger where sales_Bill=" + billno + "");
                    if (!r.next() && !r2.next()) {
//                        JOptionPane.showMessageDialog(this, "running sales1");
                        if (netamount != 0) {
//                    JOptionPane.showMessageDialog(this, remark);
                            tableObject tableObj = new tableObject(date, partyName, 0.0, (int) netamount - receivedamt - exchangeAmt, 1, remark + "," + netamount, "Sale");
                            initialTableData.add(tableObj);
                        }
                    }
                    r.close();
                    stmtt.close();
                } else {
                    if (netamount != 0) {
//                    JOptionPane.showMessageDialog(this, remark);
                        if (terms.trim().equalsIgnoreCase("Cash")) {
                            Statement stmtt = con.createStatement();
                            Statement stmttt = con.createStatement();
                            ResultSet r = stmtt.executeQuery("select sales_Bill,amtpaid from receipt where sales_Bill='" + billno + "'");
                            ResultSet r2 = stmttt.executeQuery("select sales_Bill,SUM(amt) as am from bankledger where sales_Bill='" + billno + "' group by sales_Bill");
                            double money1 = 0;
                            double money2 = 0.0;
                            while (r.next()) {
                                money1 = r.getDouble(2);
                            }
                            while (r2.next()) {
                                money2 = r2.getDouble(2);
                            }
                            r2.close();
                            r.close();
                            stmtt.close();
                            stmttt.close();
                            tableObject tableObj = new tableObject(date, partyName, 0.0, 0.0, 1, remark + ", " + (netamount), "Sale");
                            initialTableData.add(tableObj);
                            if(money1!=0&& money2!=0){
                            tableObject tableObj1 = new tableObject(date, partyName, 0.0, Double.parseDouble(df.format((int) netamount - money1 - money2 - exchangeAmt)), 1, remark + "," + netamount, "Sale");
                            initialTableData.add(tableObj1);
                            }
                        } else {
                            tableObject tableObj = new tableObject(date, partyName, 0.0, netamount - receivedamt - exchangeAmt, 1, remark + "," + netamount, "Sale");
                            initialTableData.add(tableObj);
                        }

                    }
                }

            }
            con.close();
            stmt.close();
            rs.close();

            query = "select * from receipt where Name = '" + party + "' and not Receiptno='-1';";
            if (party.equals("Cash")) {
                query = "select * from receipt ";
            }
            if (getGroupName(party).equals("Bank")) {
                query = "select * from receipt where mop='" + party + "' ";
            }
            ResultSet rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");
                String mop = rs1.getString("mop");
                int salesbill = rs1.getInt("sales_Bill");
                String remark = null;
                if (!getGroupName(mop).equals("Bank") && party.equals("Cash")) {
                    if (salesbill > -1) {
                        remark = "Bill No." + String.valueOf(salesbill);
//                        hjg
//                    remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                        tableObject tableObj1 = new tableObject(date, name, 0, 0, 1, remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)), "Sale");
                        initialTableData.add(tableObj1);
                        tableObject tableObj = new tableObject(date, name, 0, amount, 1, remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)), "Sale");
                        initialTableData.add(tableObj);
                    } else {
                        remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                        tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Receipt");
                        initialTableData.add(tableObj);
                    }
                } else if (!party.equals("Cash") && getGroupName(party).equals("Bank")) {
                    remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Receipt");
                    initialTableData.add(tableObj);
                } else {

                    remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Receipt");
                    initialTableData.add(tableObj);

                }
            }

            c.close();
            s.close();
            rs1.close();

            query = "select * from bankledger where name = '" + party + "';";

            c = DBConnect.connect();
            s = c.createStatement();

            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("name");
                double amount = rs1.getDouble("amt");
                Date date = rs1.getDate("date");
                String remark = "Rcpt. No." + String.valueOf(rs1.getInt("rno")) + ", ";
                int salesbill = rs1.getInt("sales_Bill");
                String type = rs1.getString("type");
                tableObject tableObj;
                if (salesbill == -1) {
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "withdraw");
                        initialTableData.add(tableObj);
                    } else {
                        tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "deposit");
                        initialTableData.add(tableObj);
                    }

                }

            }

            s.clearBatch();

            query = "select * from bankledger where bankname = '" + party + "' order by sales_Bill;";
            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("bankname");
                double amount = rs1.getDouble("amt");
                Date date = rs1.getDate("date");
                String remark = "Rcpt. No." + String.valueOf(rs1.getInt("rno")) + ", ";
                int salesbill = rs1.getInt("sales_Bill");
                String type = rs1.getString("type");
                tableObject tableObj;
                if (salesbill > -1) {
                    remark = "Bill No." + String.valueOf(salesbill);
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, 0.0, amount, 1, remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)), "Sale");

                    } else {
                        tableObj = new tableObject(date, name, amount, 0.0, 0, remark + "," + outstandingAnalysisHelper.GetNetAmount(String.valueOf(salesbill)), "Sale");
                    }
                } else {
                    if ("withdraw".equals(type)) {
                        tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Deposit");

                    } else {
                        tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "withdraw");
                    }
                }
                initialTableData.add(tableObj);
            }

            c.close();
            s.close();
            rs1.close();

            c = DBConnect.connect();
            s = c.createStatement();

            query = "select * from payments where  Name = '" + party + "' or mop='" + party + "';";
            if (party.equals("Cash")) {
                query = "select * from payments where mop = 'Cash';";
            }

            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                String name = rs1.getString("Name");
                double amount = rs1.getDouble("amtpaid");
                Date date = rs1.getDate("date");

                String remark = null;
                remark = "Rcpt. No." + String.valueOf(rs1.getInt("ReceiptNo")) + ", " + rs1.getString("mop");
                // tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Payment");
                if (getGroupName(party).equals("Bank") || getGroupName(party).equals("Customer")) {
//                   JOptionPane.showMessageDialog(this, party+ "  running bank");
                    tableObject tableObj = new tableObject(date, name, amount, 0.0, 0, remark, "Payment");
                    initialTableData.add(tableObj);
                } else {
                    tableObject tableObj = new tableObject(date, name, 0.0, amount, 1, remark, "Payment");
                    initialTableData.add(tableObj);
                }

            }

            c.close();
            s.close();
            rs1.close();

            c = DBConnect.connect();
            s = c.createStatement();

            query = "select distinct(bill) from purchasehistory where partyname = '" + party + "'";
            if (!party.equals("Cash")) {
                query = "select distinct(bill) from purchasehistory where partyname = '" + party + "'";
                query += " and  terms = 'Credit'";
//                JOptionPane.showMessageDialog(this, query);
//System.out.println("query after the purchase"+query);

            } else {
                query = "select distinct(bill) from purchasehistory where ";
                //notjing write previous coder
                query += "  terms = 'Cash'";
            }
//            JOptionPane.showMessageDialog(this, query);
            rs1 = s.executeQuery(query);
            while (rs1.next()) {
                Statement st = c.createStatement();
                int billno = rs1.getInt("bill");
                query = "select sum(Cast(net_amount as decimal(10,2))) as nt,date from purchasehistory where bill='" + billno + "'";

                ResultSet rest = st.executeQuery(query);
                Date date = new Date();
                double netamount = 0;
                while (rest.next()) {
                    date = rest.getDate("date");
                    netamount = rest.getDouble("nt");
                }
                rest.close();
                st.close();

                String remark = "Bill. No." + billno;
                tableObject tableObj = new tableObject(date, party, netamount, 0.0, 0, remark, "Purchase");
                initialTableData.add(tableObj);
            }

            c.close();
            s.close();
            rs1.close();


            Collections.sort(initialTableData, new Comparator<tableObject>() {
                @Override
                public int compare(tableObject p1, tableObject p2) {
                    boolean res = p1.date.before(p2.date);
                    if (res) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            double balance = 0.0;
            double credit = 0.0, debit = 0.0;
            Connection newCon = DBConnect.connect();
            Statement st = newCon.createStatement();
            ResultSet res = st.executeQuery("SELECT opbal FROM account where accountname = '" + party + "';");
            while (res.next()) {
                if (res.getObject("opbal") != null) {
                    balance += res.getDouble("opbal");
                }
            }
//         OpBal.setText(String.valueOf(balance));
            newCon.close();
            st.close();
            res.close();

            for (tableObject i : initialTableData) {

                balance += i.debit - i.credit;

                credit += i.credit;
                debit += i.debit;
//                    if (i.creditOrDebit == 0) // credit
//                    {
//                        if (balance <= 0) {
//                            data.clear();
//                            data.add(String.valueOf(balance));
//                            data.add("Cr");
////                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit, String.format("%.2f", -balance) + " Cr"});
//                        } else {
//                            data.clear();
//                            data.add(String.valueOf(balance));
//                            data.add("Dr");
////                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit, String.format("%.2f", balance) + " Dr"});
//                        }
//
//                    } else {
//                       if (balance <= 0) {
//                            data.clear();
//                            data.add(String.valueOf(balance));
//                            data.add("Cr");
////                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit, String.format("%.2f", -balance) + " Cr"});
//                        } else {
//                            data.clear();
//                            data.add(String.valueOf(balance));
//                            data.add("Dr");
////                            m.addRow(new Object[]{i.date, i.type, i.name, i.remark, i.credit, i.debit, String.format("%.2f", balance) + " Dr"});
//                        }
//                    }
            }
            initialTableData.clear();
            return String.valueOf(balance);
//            return data;
//            totalbalancefield.setText(String.format("%.2f",debit-credit));
//            totalcreditfield.setText(String.format("%.2f", credit));
//            totaldebitfield.setText(String.format("%.2f",debit ));

//
//            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//            centerRenderer.setHorizontalAlignment(JLabel.LEFT);
//            for (int g = 0; g < table.getColumnCount(); g++) {
//                table.getColumnModel().getColumn(g).setCellRenderer(centerRenderer);
//            }
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e);
        }
        return "0";
    }
     public static Double GetNetAmount(String Bill) {
        Connection con = DBConnect.connect();
        try {
            Statement stmt = con.createStatement();
            String query = "select sum(netamount) from sales where bill=" + Bill + "";
            ResultSet re = stmt.executeQuery(query);
            while (re.next()) {
                return re.getDouble(1);
            }
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "getnet " + e);
        }
        return 0.0;
    }
     public static double getBankDetail(String Bill){
         
         Connection con = DBConnect.connect();
        try {
            Statement stmt = con.createStatement();
            String query = "select sum(netamount) from sales where bill=" + Bill + "";
            ResultSet re = stmt.executeQuery(query);
            while (re.next()) {
                return re.getDouble(1);
            }
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "getnet " + e);
        }
         
         return 0.0;
     }
}
