/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery;

/**
 *
 * @author shrey
 */
public class ExchangeJasperContentDetails {

    public ExchangeJasperContentDetails(String itemname, String grosswt, String fine, String netwt, String total) {
        this.itemname = itemname;
        this.grosswt = grosswt;
        this.fine = fine;
        this.netwt = netwt;
        this.total = total;
        this.rate = String.valueOf(Double.parseDouble(total)/Double.parseDouble(netwt));
    }

    private String itemname;
    private String grosswt;
    private String fine;
    private String netwt;
    private String total;
    private String rate;

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public void setGrosswt(String grosswt) {
        this.grosswt = grosswt;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public void setNetwt(String netwt) {
        this.netwt = netwt;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getItemname() {
        return itemname;
    }

    public String getGrosswt() {
        return grosswt;
    }

    public String getFine() {
        return fine;
    }

    public String getNetwt() {
        return netwt;
    }

    public String getTotal() {
        return total;
    }

    
}
