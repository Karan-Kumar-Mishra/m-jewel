/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery;

/**
 *
 * @author Sachin
 */
public class JasperFillClass {
    private String date;
    private String type;
    private String partyname;
    private String remarks;
    private String credit;
    private String debit;
    private String balance;
    
    public JasperFillClass(String date, String type, String partyname, String remarks, String credit, String debit, String balance) {
        this.date = date;
        this.type = type;
        this.partyname = partyname;
        this.remarks = remarks;
        this.credit = credit;
        this.debit = debit;
        this.balance = balance;
    }
    
    public JasperFillClass() {  
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    
}
