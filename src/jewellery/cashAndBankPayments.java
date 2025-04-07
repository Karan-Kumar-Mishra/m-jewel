/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jewellery;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JComboBox;

/**
 *
 * @author SACHIN MISHRA
 */
public class cashAndBankPayments extends javax.swing.JFrame {

    /**
     * Creates new form cashAndBankPayments
     */
    private String cashAmount;
    private String CardAmount1;
    private String cardAmount2;
    private String eWallteAmount;
    private String chequeAmount;
    private String chequno;
    private String BankName;
    private String ChequeDate;
    private String cardBank1;
    private String cardBank2;
    private String ewalletBank;
    private String chequeBank;
    private String card1Transectionno;
    private String card2Transectionno;
    private String ewalletTransectionno;
    private String chequeTransectionno;
    private String balanceamount;
    public String bal = "0";

    public cashAndBankPayments() {
        initComponents();
        // TODO add your handling code here:
        okbtn.setEnabled(false);
        getAllBankNamesFromDatabases(card1bank);

        getAllBankNamesFromDatabases(card2bank);        // TODO add your handling code here:

        getAllBankNamesFromDatabases(ewalletbank);        // TODO add your handling code here:

        getAllBankNamesFromDatabases(cheqbank);
        try {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
//                JOptionPane.showMessageDialog(null,"hh");
                    // This method will be called when the frame is opened/visible
                    if (cashamount.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount(cashamount.getText().trim());
                        balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText())));
                        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                            okbtn.setEnabled(true);
                        }
                    }
                    try {
                        if (jewellery.LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales != null) {
                            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
//             JOptionPane.showMessageDialog(null,CashAndBankPaymentsSales.getChequeDate());
                        }
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }
//           

                    // TODO add your handling code here:
                    if (cardamt1.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1(cardamt1.getText().trim());
                        if (!cashamount.getText().trim().isEmpty()) {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText())));
                        } else {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cardamt1.getText())));

                        }
                        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                            okbtn.setEnabled(true);
                        }
                    }

                    // TODO add your handling code here:
                    if (cardamt2.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2(cardamt2.getText().trim());
                        if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty()) {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText())));
                        } else {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(cardamt2.getText())));
                        }
                        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                            okbtn.setEnabled(true);
                        }
                    }

                    // TODO add your handling code here:
                    if (ewalletamt.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount(ewalletamt.getText().trim());
                        if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty()) {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText())));
                        } else {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
                        }
//            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
                        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                            okbtn.setEnabled(true);
                        }
                    }

                    // TODO add your handling code here:
                    if (chequeamt.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount(chequeamt.getText().trim());
                        if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty() && !chequeamt.getText().trim().isEmpty()) {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText()) - Double.parseDouble(chequeamt.getText())));
                        } else {
                            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(chequeamt.getText())));
                        }
                        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                            okbtn.setEnabled(true);
                        }
                    }

                    // TODO add your handling code here:
                    if (chequeno.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno(chequeno.getText().trim());
                    }

                    // TODO add your handling code here:
                    if (bankname.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName(bankname.getText().trim());
                    }

                    // TODO add your handling code here:
                    if (((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
                    }

                    // TODO add your handling code here:
                    if (card1bank.getSelectedIndex() == 0) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1(
                                card1bank.getSelectedItem().toString().trim());
                    }

                    // TODO add your handling code here:
                    if (ewalletbank.getSelectedIndex() == 0) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank(
                                ewalletbank.getSelectedItem().toString().trim());
                    }

                    // TODO add your handling code here:
                    if (card2bank.getSelectedIndex() == 0) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2(
                                card2bank.getSelectedItem().toString().trim());
                    }

                    // TODO add your handling code here:
                    if (cheqbank.getSelectedIndex() == 0) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank(
                                cheqbank.getSelectedItem().toString().trim());
                    }

                    // TODO add your handling code here:
                    if (trancard1.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno(trancard1.getText().trim());
                    }

                    // TODO add your handling code here:
                    if (trancard2.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno(trancard2.getText().trim());
                    }

                    // TODO add your handling code here:
                    if (tranewallet.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno(tranewallet.getText().trim());
                    }

                    // TODO add your handling code here:
                    if (trancheque.getText().trim().isEmpty()) {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno("");
                    } else {
                        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno(trancheque.getText().trim());
                    }

                    balanceAmount.setText(LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.getBalanceamount());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno("");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBalanceamount("");
//                System.out.println("running backup");
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales=new cashAndBankPayments();

            }
        });
    }

    public void deleteMethod(String bill) {
        Connection con = DBConnect.connect();
        try {
            Statement stmt = con.createStatement();

            String sqlForReceipt = "delete from receipt where sales_Bill='" + bill + "'";
            stmt.addBatch(sqlForReceipt);
//            JOptionPane.showMessageDialog(this, sqlForReceipt);
            String sqlForBankGrp = "delete  from bankledger where sales_Bill='" + bill + "'";
//            JOptionPane.showMessageDialog(this, sqlForBankGrp);
            stmt.addBatch(sqlForBankGrp);
            int[] i = stmt.executeBatch();
//            JOptionPane.showMessageDialog(this,"this is deleted query  " +i[0]+" "+i[1]);
            stmt.clearBatch();
            stmt.close();

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void fatchData(int bill) {
//        JOptionPane.showMessageDialog(this, "method running");
        Connection con = DBConnect.connect();
        String cashamt = "";
        String card1amt = "", card1tran = "", card11bank = "", card12amt = "", card12tran = "", card12bank = "", ewallateamt = "", ewallatetran = "", ewallatbank = "", chamt = "", chtran = "", chbank = "", chbankname = "", chno = "", chDate = "";
        try {
            Statement stmt = con.createStatement();
            String sqlForReceipt = "select amtpaid from receipt where sales_Bill='" + bill + "'";
            ResultSet re = stmt.executeQuery(sqlForReceipt);
            while (re.next()) {
                cashamt = re.getString(1);
            }
            cashamount.setText(cashamt);
            re.close();
            String sqlForBankGrp = "select * from bankledger where sales_Bill='" + bill + "'";
            re = stmt.executeQuery(sqlForBankGrp);
            while (re.next()) {

                String text = re.getString("remarks");
                if (!text.trim().isEmpty()) {
                    String[] allData = text.split(" ");
                    String areaToAdd = allData[0];
                    if (areaToAdd.trim().equals("1") && allData.length >= 2) {
                        card1amt = re.getString("amt");
                        card1tran = allData[1];
                        card11bank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("2") && allData.length >= 2) {
                        card12amt = re.getString("amt");
                        card12tran = allData[1];
                        card12bank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("3") && allData.length >= 2) {
                        ewallateamt = re.getString("amt");
                        ewallatetran = allData[1];
                        ewallatbank = re.getString("bankname");
                    } else if (areaToAdd.trim().equals("4") && allData.length >= 4) {
                        chamt = re.getString("amt");
                        chno = allData[1];
                        chbankname = allData[2];
                        chDate = allData[3];
                        chbank = re.getString("bankname");
                    }

                }

            }
            cardamt1.setText(card1amt);
            trancard1.setText(card1tran);
            card1bank.setSelectedItem(card11bank);
            cardamt2.setText(card12amt);
            trancard2.setText(card12tran);
            card2bank.setSelectedItem(card12bank);
            ewalletamt.setText(ewallateamt);
            tranewallet.setText(ewallatetran);
            ewalletbank.setSelectedItem(ewallatbank);
            chequeamt.setText(chamt);
            chequeno.setText(chno);
            bankname.setText(chbankname);
            ((JTextField) mdate.getDateEditor().getUiComponent()).setText(chDate);
            cheqbank.setSelectedItem(chbank);
            re.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }

    }

    public String getBalanceamount() {
        return balanceamount;
    }

    public void setBalanceamount(String balanceamount) {
        this.balanceamount = balanceamount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public String getChequeBank() {
        return chequeBank;
    }

    public void setChequeBank(String chequeBank) {
        this.chequeBank = chequeBank;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getCardAmount1() {
        return CardAmount1;
    }

    public void setCardAmount1(String CardAmount1) {
        this.CardAmount1 = CardAmount1;
    }

    public String getCardAmount2() {
        return cardAmount2;
    }

    public void setCardAmount2(String cardAmount2) {
        this.cardAmount2 = cardAmount2;
    }

    public String geteWallteAmount() {
        return eWallteAmount;
    }

    public void seteWallteAmount(String eWallteAmount) {
        this.eWallteAmount = eWallteAmount;
    }

    public String getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public String getChequno() {
        return chequno;
    }

    public void setChequno(String chequno) {
        this.chequno = chequno;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String ChequeDate) {
        this.ChequeDate = ChequeDate;
    }

    public String getCardBank1() {
        return cardBank1;
    }

    public void setCardBank1(String cardBank1) {
        this.cardBank1 = cardBank1;
    }

    public String getEwalletBank() {
        return ewalletBank;
    }

    public void setEwalletBank(String ewalletBank) {
        this.ewalletBank = ewalletBank;
    }

    public String getCard1Transectionno() {
        return card1Transectionno;
    }

    public void setCard1Transectionno(String card1Transectionno) {
        this.card1Transectionno = card1Transectionno;
    }

    public String getCard2Transectionno() {
        return card2Transectionno;
    }

    public void setCard2Transectionno(String card2Transectionno) {
        this.card2Transectionno = card2Transectionno;
    }

    public String getEwalletTransectionno() {
        return ewalletTransectionno;
    }

    public void setEwalletTransectionno(String ewalletTransectionno) {
        this.ewalletTransectionno = ewalletTransectionno;
    }

    public String getChequeTransectionno() {
        return chequeTransectionno;
    }

    public void setChequeTransectionno(String chequeTransectionno) {
        this.chequeTransectionno = chequeTransectionno;
    }

    public String getCardBank2() {
        return cardBank2;
    }

    public void setCardBank2(String cardBank2) {
        this.cardBank2 = cardBank2;
    }

    private void disposeMethod() {
        if (cashamount.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount(cashamount.getText().trim());
            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText())));
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
        try {
            if (jewellery.LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales != null) {
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
//             JOptionPane.showMessageDialog(null,CashAndBankPaymentsSales.getChequeDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//           

        // TODO add your handling code here:
        if (cardamt1.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1(cardamt1.getText().trim());
            if (!cashamount.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cardamt1.getText())));

            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }

        // TODO add your handling code here:
        if (cardamt2.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2(cardamt2.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(cardamt2.getText())));
            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }

        // TODO add your handling code here:
        if (ewalletamt.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount(ewalletamt.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
            }
//            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }

        // TODO add your handling code here:
        if (chequeamt.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount(chequeamt.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty() && !chequeamt.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText()) - Double.parseDouble(chequeamt.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(chequeamt.getText())));
            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }

        // TODO add your handling code here:
        if (chequeno.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno(chequeno.getText().trim());
        }

        // TODO add your handling code here:
        if (bankname.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName(bankname.getText().trim());
        }

        // TODO add your handling code here:
        if (((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
        }

        // TODO add your handling code here:
        if (card1bank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1(
                    card1bank.getSelectedItem().toString().trim());
        }

        // TODO add your handling code here:
        if (ewalletbank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank(
                    ewalletbank.getSelectedItem().toString().trim());
        }

        // TODO add your handling code here:
        if (card2bank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2(
                    card2bank.getSelectedItem().toString().trim());
        }

        // TODO add your handling code here:
        if (cheqbank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank(
                    cheqbank.getSelectedItem().toString().trim());
        }

        // TODO add your handling code here:
        if (trancard1.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno(trancard1.getText().trim());
        }

        // TODO add your handling code here:
        if (trancard2.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno(trancard2.getText().trim());
        }

        // TODO add your handling code here:
        if (tranewallet.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno(tranewallet.getText().trim());
        }

        // TODO add your handling code here:
        if (trancheque.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno(trancheque.getText().trim());
        }
        LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setVisible(false);
    }

    private void getAllBankNamesFromDatabases(JComboBox<String> combo) {
        try {
            Connection con = DBConnect.connect();
            Statement stmt = con.createStatement();
            String sql = "SELECT accountname FROM `account` WHERE `grp` = 'Bank';";
            ResultSet re = stmt.executeQuery(sql);
            combo.removeAllItems();
            combo.addItem("N.A");
            while (re.next()) {
                combo.addItem(re.getString(1));
            }
            re.close();
            con.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    @Override
    public String toString() {
        return "cashAndBankPayments{" + "cashAmount=" + cashAmount + ", CardAmount1=" + CardAmount1 + ", cardAmount2=" + cardAmount2 + ", eWallteAmount=" + eWallteAmount + ", chequeAmount=" + chequeAmount + ", chequno=" + chequno + ", BankName=" + BankName + ", ChequeDate=" + ChequeDate + ", cardBank1=" + cardBank1 + ", cardBank2=" + cardBank2 + ", ewalletBank=" + ewalletBank + ", chequeBank=" + chequeBank + ", card1Transectionno=" + card1Transectionno + ", card2Transectionno=" + card2Transectionno + ", ewalletTransectionno=" + ewalletTransectionno + ", chequeTransectionno=" + chequeTransectionno + '}';
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cashamount = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cardamt1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cardamt2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ewalletamt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        chequeamt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        card1bank = new javax.swing.JComboBox<>();
        card2bank = new javax.swing.JComboBox<>();
        ewalletbank = new javax.swing.JComboBox<>();
        cheqbank = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        trancard1 = new javax.swing.JTextField();
        trancard2 = new javax.swing.JTextField();
        tranewallet = new javax.swing.JTextField();
        trancheque = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        chequeno = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        bankname = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        mdate = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        balanceAmount = new javax.swing.JTextField();
        okbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 51, 51));
        setType(java.awt.Window.Type.POPUP);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Cash Amt.");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 36, -1, -1));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 0));
        jLabel2.setText("Payment Options");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(321, 6, -1, -1));

        cashamount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cashamountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cashamountFocusLost(evt);
            }
        });
        cashamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cashamountKeyPressed(evt);
            }
        });
        getContentPane().add(cashamount, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 30, 148, -1));

        jLabel3.setText("Card Amt. 1");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 84, -1, -1));

        cardamt1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cardamt1FocusLost(evt);
            }
        });
        cardamt1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardamt1KeyPressed(evt);
            }
        });
        getContentPane().add(cardamt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 78, 148, -1));

        jLabel4.setText("Card Amt. 2");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 130, -1, -1));

        cardamt2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cardamt2FocusLost(evt);
            }
        });
        cardamt2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cardamt2KeyPressed(evt);
            }
        });
        getContentPane().add(cardamt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 124, 148, -1));

        jLabel5.setText("eWallet Amt.");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 176, -1, -1));

        ewalletamt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ewalletamtFocusLost(evt);
            }
        });
        ewalletamt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ewalletamtKeyPressed(evt);
            }
        });
        getContentPane().add(ewalletamt, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 170, 148, -1));

        jLabel6.setText("Cheque Amt");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 222, -1, -1));

        chequeamt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chequeamtFocusLost(evt);
            }
        });
        chequeamt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chequeamtKeyPressed(evt);
            }
        });
        getContentPane().add(chequeamt, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 216, 148, -1));

        jLabel7.setText("Choose Account");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 56, 233, -1));

        card1bank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                card1bankFocusLost(evt);
            }
        });
        card1bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card1bankMouseClicked(evt);
            }
        });
        card1bank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                card1bankKeyPressed(evt);
            }
        });
        getContentPane().add(card1bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 79, 148, -1));

        card2bank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                card2bankFocusLost(evt);
            }
        });
        card2bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card2bankMouseClicked(evt);
            }
        });
        card2bank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                card2bankKeyPressed(evt);
            }
        });
        getContentPane().add(card2bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 125, 148, -1));

        ewalletbank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ewalletbankFocusLost(evt);
            }
        });
        ewalletbank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ewalletbankMouseClicked(evt);
            }
        });
        ewalletbank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ewalletbankKeyPressed(evt);
            }
        });
        getContentPane().add(ewalletbank, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 171, 148, -1));

        cheqbank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cheqbankFocusLost(evt);
            }
        });
        cheqbank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cheqbankMouseClicked(evt);
            }
        });
        cheqbank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cheqbankKeyPressed(evt);
            }
        });
        getContentPane().add(cheqbank, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 217, 148, -1));

        jButton1.setText("+");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 78, -1, -1));

        jButton2.setText("+");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 124, -1, -1));

        jButton3.setText("+");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 170, -1, -1));

        jButton4.setText("+");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 216, -1, -1));

        jLabel8.setText("Transaction Ref. No.");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 56, -1, -1));

        trancard1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                trancard1FocusLost(evt);
            }
        });
        trancard1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trancard1KeyPressed(evt);
            }
        });
        getContentPane().add(trancard1, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 78, 216, -1));

        trancard2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                trancard2FocusLost(evt);
            }
        });
        trancard2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trancard2KeyPressed(evt);
            }
        });
        getContentPane().add(trancard2, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 124, 216, -1));

        tranewallet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tranewalletFocusLost(evt);
            }
        });
        tranewallet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tranewalletKeyPressed(evt);
            }
        });
        getContentPane().add(tranewallet, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 170, 216, -1));

        trancheque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tranchequeFocusLost(evt);
            }
        });
        trancheque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tranchequeKeyPressed(evt);
            }
        });
        getContentPane().add(trancheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 216, 216, -1));

        jLabel9.setText("Cheque No.");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 268, -1, -1));

        chequeno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chequenoFocusLost(evt);
            }
        });
        chequeno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chequenoKeyPressed(evt);
            }
        });
        getContentPane().add(chequeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 262, 148, -1));

        jLabel10.setText("Bank Name");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 268, -1, -1));

        bankname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                banknameFocusLost(evt);
            }
        });
        bankname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                banknameKeyPressed(evt);
            }
        });
        getContentPane().add(bankname, new org.netbeans.lib.awtextra.AbsoluteConstraints(301, 262, 135, -1));

        jLabel11.setText("Cheque Date");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 268, -1, -1));

        mdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mdateFocusLost(evt);
            }
        });
        mdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mdatePropertyChange(evt);
            }
        });
        getContentPane().add(mdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(521, 262, 132, -1));

        jLabel12.setForeground(new java.awt.Color(255, 102, 51));
        jLabel12.setText("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 308, -1, -1));

        jLabel13.setText("Cash Tendered");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 354, -1, -1));
        getContentPane().add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 348, 119, -1));

        jLabel14.setText("Balance Amount");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 354, -1, -1));

        balanceAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balanceAmountActionPerformed(evt);
            }
        });
        getContentPane().add(balanceAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 348, 176, -1));

        okbtn.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        okbtn.setText("Ok");
        okbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okbtnMouseClicked(evt);
            }
        });
        okbtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okbtnKeyPressed(evt);
            }
        });
        getContentPane().add(okbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(556, 342, 94, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cashamountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cashamountFocusLost
        // TODO add your handling code here:
        if (cashamount.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCashAmount(cashamount.getText().trim());
            balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText())));
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cashamountFocusLost

    private void cardamt1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cardamt1FocusLost
        // TODO add your handling code here:
        if (cardamt1.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount1(cardamt1.getText().trim());
            if (!cashamount.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cardamt1.getText())));

            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cardamt1FocusLost

    private void cardamt2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cardamt2FocusLost
        // TODO add your handling code here:
        if (cardamt2.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardAmount2(cardamt2.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(cardamt2.getText())));
            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cardamt2FocusLost

    private void ewalletamtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ewalletamtFocusLost
        // TODO add your handling code here:
        if (ewalletamt.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.seteWallteAmount(ewalletamt.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
            }
//            balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(ewalletamt.getText())));
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
    }//GEN-LAST:event_ewalletamtFocusLost

    private void chequeamtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chequeamtFocusLost
        // TODO add your handling code here:
        if (chequeamt.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeAmount(chequeamt.getText().trim());
            if (!cashamount.getText().trim().isEmpty() && !cardamt1.getText().trim().isEmpty() && !cardamt2.getText().trim().isEmpty() && !ewalletamt.getText().trim().isEmpty() && !chequeamt.getText().trim().isEmpty()) {
                balanceAmount.setText(String.valueOf(Double.parseDouble(bal) - Double.parseDouble(cashamount.getText()) - Double.parseDouble(cardamt1.getText()) - Double.parseDouble(cardamt2.getText()) - Double.parseDouble(ewalletamt.getText()) - Double.parseDouble(chequeamt.getText())));
            } else {
                balanceAmount.setText(String.valueOf(Double.parseDouble(balanceAmount.getText()) - Double.parseDouble(chequeamt.getText())));
            }
            if (Double.parseDouble(balanceAmount.getText()) <= 0) {
                okbtn.setEnabled(true);
            }
        }
    }//GEN-LAST:event_chequeamtFocusLost

    private void chequenoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chequenoFocusLost
        // TODO add your handling code here:
        if (chequeno.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequno(chequeno.getText().trim());
        }
    }//GEN-LAST:event_chequenoFocusLost

    private void banknameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_banknameFocusLost
        // TODO add your handling code here:
        if (bankname.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setBankName(bankname.getText().trim());
        }
    }//GEN-LAST:event_banknameFocusLost

    private void mdateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mdateFocusLost
        // TODO add your handling code here:
        if (((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
        }
    }//GEN-LAST:event_mdateFocusLost

    private void card1bankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_card1bankFocusLost
        // TODO add your handling code here:
        if (card1bank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank1(
                    card1bank.getSelectedItem().toString().trim());
        }
    }//GEN-LAST:event_card1bankFocusLost

    private void ewalletbankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ewalletbankFocusLost
        // TODO add your handling code here:
        if (ewalletbank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletBank(
                    ewalletbank.getSelectedItem().toString().trim());
        }
    }//GEN-LAST:event_ewalletbankFocusLost

    private void card2bankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_card2bankFocusLost
        // TODO add your handling code here:
        if (card2bank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCardBank2(
                    card2bank.getSelectedItem().toString().trim());
        }
    }//GEN-LAST:event_card2bankFocusLost

    private void cheqbankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cheqbankFocusLost
        // TODO add your handling code here:
        if (cheqbank.getSelectedIndex() == 0) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeBank(
                    cheqbank.getSelectedItem().toString().trim());
        }
    }//GEN-LAST:event_cheqbankFocusLost

    private void trancard1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trancard1FocusLost
        // TODO add your handling code here:
        if (trancard1.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard1Transectionno(trancard1.getText().trim());
        }
    }//GEN-LAST:event_trancard1FocusLost

    private void trancard2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trancard2FocusLost
        // TODO add your handling code here:
        if (trancard2.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setCard2Transectionno(trancard2.getText().trim());
        }
    }//GEN-LAST:event_trancard2FocusLost

    private void tranewalletFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tranewalletFocusLost
        // TODO add your handling code here:
        if (tranewallet.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setEwalletTransectionno(tranewallet.getText().trim());
        }
    }//GEN-LAST:event_tranewalletFocusLost

    private void tranchequeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tranchequeFocusLost
        // TODO add your handling code here:
        if (trancheque.getText().trim().isEmpty()) {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno("");
        } else {
            LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeTransectionno(trancheque.getText().trim());
        }
    }//GEN-LAST:event_tranchequeFocusLost

    private void card1bankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card1bankMouseClicked
        // TODO add your handling code here:
        getAllBankNamesFromDatabases(card1bank);
    }//GEN-LAST:event_card1bankMouseClicked

    private void card2bankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_card2bankMouseClicked
        getAllBankNamesFromDatabases(card2bank);        // TODO add your handling code here:
    }//GEN-LAST:event_card2bankMouseClicked

    private void ewalletbankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ewalletbankMouseClicked
        getAllBankNamesFromDatabases(ewalletbank);        // TODO add your handling code here:
    }//GEN-LAST:event_ewalletbankMouseClicked

    private void cheqbankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cheqbankMouseClicked
        getAllBankNamesFromDatabases(cheqbank);        // TODO add your handling code here:
    }//GEN-LAST:event_cheqbankMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:

        this.setVisible(true);

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        this.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        this.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        this.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseClicked

    private void cashamountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashamountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cardamt1.requestFocus();
        }
    }//GEN-LAST:event_cashamountKeyPressed

    private void cardamt1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardamt1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            card1bank.requestFocus();
        }
    }//GEN-LAST:event_cardamt1KeyPressed

    private void trancard1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trancard1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cardamt2.requestFocus();
        }
    }//GEN-LAST:event_trancard1KeyPressed

    private void card1bankKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_card1bankKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trancard1.requestFocus();
        }
    }//GEN-LAST:event_card1bankKeyPressed

    private void cardamt2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardamt2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            card2bank.requestFocus();
        }
    }//GEN-LAST:event_cardamt2KeyPressed

    private void card2bankKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_card2bankKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trancard2.requestFocus();
        }
    }//GEN-LAST:event_card2bankKeyPressed

    private void trancard2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trancard2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ewalletamt.requestFocus();
        }
    }//GEN-LAST:event_trancard2KeyPressed

    private void ewalletamtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ewalletamtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ewalletbank.requestFocus();
        }
    }//GEN-LAST:event_ewalletamtKeyPressed

    private void ewalletbankKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ewalletbankKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tranewallet.requestFocus();
        }
    }//GEN-LAST:event_ewalletbankKeyPressed

    private void tranewalletKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tranewalletKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            chequeamt.requestFocus();
        }
    }//GEN-LAST:event_tranewalletKeyPressed

    private void chequeamtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chequeamtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cheqbank.requestFocus();
        }
    }//GEN-LAST:event_chequeamtKeyPressed

    private void cheqbankKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cheqbankKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trancheque.requestFocus();
        }
    }//GEN-LAST:event_cheqbankKeyPressed

    private void tranchequeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tranchequeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            chequeno.requestFocus();
        }
    }//GEN-LAST:event_tranchequeKeyPressed

    private void chequenoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chequenoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bankname.requestFocus();
        }
    }//GEN-LAST:event_chequenoKeyPressed

    private void banknameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_banknameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            okbtn.requestFocus();
        }
    }//GEN-LAST:event_banknameKeyPressed

    private void okbtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okbtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {

                disposeMethod();
                refreash();
//    JOptionPane.showMessageDialog(this,LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }

        }// TODO add your handling code here:
    }//GEN-LAST:event_okbtnKeyPressed

    private void mdatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mdatePropertyChange
        // TODO add your handling code here:
        try {
            if (LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales != null) {
                LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.setChequeDate(((JTextField) mdate.getDateEditor().getUiComponent()).getText().trim());
//             JOptionPane.showMessageDialog(null,CashAndBankPaymentsSales.getChequeDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            

    }//GEN-LAST:event_mdatePropertyChange
    private void refreash() {
//        cashamount.setText("");
//        cardamt1.setText("");
//        cardamt2.setText("");
//        ewalletamt.setText("");
//        chequeamt.setText("");
//        chequeno.setText("");
//        bankname.setText("");
//        trancard1.setText("");
//        trancard2.setText("");
//        trancheque.setText("");
//        tranewallet.setText("");
//        card1bank.setSelectedItem(CashAndBankPaymentsSales.getCardBank1());
//        card2bank.setSelectedItem(CashAndBankPaymentsSales.getCardBank2());
//        ewalletbank.setSelectedItem(CashAndBankPaymentsSales.getEwalletBank());
//        cheqbank.setSelectedItem(CashAndBankPaymentsSales.getChequeBank());
        ((JTextField) mdate.getDateEditor().getUiComponent()).setText(LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.getChequeDate());
//        balanceAmount.setText("");

    }
    private void balanceAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceAmountActionPerformed
        // TODO add your handling code here:
        if (Double.parseDouble(balanceAmount.getText()) <= 0) {
            okbtn.setEnabled(true);
        }
    }//GEN-LAST:event_balanceAmountActionPerformed

    private void okbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okbtnMouseClicked
        // TODO add your handling code here:

        try {

            disposeMethod();
            refreash();
//    JOptionPane.showMessageDialog(this,LoginPageRedesigned.staticdashboared.CashAndBankPaymentsSales.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_okbtnMouseClicked

    private void cashamountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cashamountFocusGained
        // TODO add your handling code here:
        bal = LoginPageRedesigned.staticdashboared.sc.CashAndBankPaymentsSales.getBalanceamount();
    }//GEN-LAST:event_cashamountFocusGained

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(cashAndBankPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(cashAndBankPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(cashAndBankPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(cashAndBankPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new cashAndBankPayments().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField balanceAmount;
    private javax.swing.JTextField bankname;
    private javax.swing.JComboBox<String> card1bank;
    private javax.swing.JComboBox<String> card2bank;
    private javax.swing.JTextField cardamt1;
    private javax.swing.JTextField cardamt2;
    private javax.swing.JTextField cashamount;
    private javax.swing.JComboBox<String> cheqbank;
    private javax.swing.JTextField chequeamt;
    private javax.swing.JTextField chequeno;
    private javax.swing.JTextField ewalletamt;
    private javax.swing.JComboBox<String> ewalletbank;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField12;
    private com.toedter.calendar.JDateChooser mdate;
    private javax.swing.JButton okbtn;
    private javax.swing.JTextField trancard1;
    private javax.swing.JTextField trancard2;
    private javax.swing.JTextField trancheque;
    private javax.swing.JTextField tranewallet;
    // End of variables declaration//GEN-END:variables
}
