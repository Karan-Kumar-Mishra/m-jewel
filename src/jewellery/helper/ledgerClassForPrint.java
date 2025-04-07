/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery.helper;

import java.util.Date;

/**
 *
 * @author SACHIN MISHRA
 */
public class ledgerClassForPrint {
public Date startingdate,endingDate;
    public String party, remarks, balance;
    public Date date;
    public double credit;
    public double debit;
    public String type;

    

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Date getStartingdate() {
        return startingdate;
    }

    public void setStartingdate(Date startingdate) {
        this.startingdate = startingdate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public ledgerClassForPrint(Date startingdate, Date endingDate, String party, String remarks, String balance, Date date, double credit, double debit, String type) {
        this.startingdate = startingdate;
        this.endingDate = endingDate;
        this.party = party;
        this.remarks = remarks;
        this.balance = balance;
        this.date = date;
        this.credit = credit;
        this.debit = debit;
        this.type = type;
    }

  

   

   

    

    public String getRemarks() {
        return remarks;
    }

    public String getBalance() {
        return balance;
    }

    public Date getDate() {
        return date;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }

    public String getType() {
        return type;
    }

}
