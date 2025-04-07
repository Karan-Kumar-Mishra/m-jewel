/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery;

/**
 *
 * @author Sachin
 */
public class SaleJasperContentDetails {
    
    private String itemdesc;
    private String hsn;
    
    private String pcs;
    private String marka;
    private String huid;
    private String grosswt;
    private String netwt;
    private String diowt;
    private String rate;
    private String makingper;
    private String amount;
    private String tagno;
    private String gst;
    private String tx_amt;
    private String makingprice;
    private String lbr_per;
 

    public SaleJasperContentDetails(String itemdesc, String hsn, String pcs, String marka, String huid, String grosswt, String netwt, String diowt, String rate, String makingper, String amount, String tagno,String gst, String tx_amt, String makingprice, String lbr_per) {
        this.itemdesc = itemdesc;
        this.hsn = hsn;
        this.pcs = pcs;
        this.marka = marka;
        this.huid = huid;
        this.grosswt = grosswt;
        this.netwt = netwt;
        this.diowt = diowt;
        this.rate = rate;
        this.makingper = makingper;
        this.amount = amount;
        this.tagno = tagno;
        this.gst = gst;
        this.tx_amt = tx_amt;
        this.makingprice = makingprice;
        this.lbr_per = lbr_per;
    }

    public String getTagno() {
        return tagno;
    }

    public void setTagno(String tagno) {
        this.tagno = tagno;
    }
    public String getlbr_per() {
        return lbr_per;
    }

    public void setlbr_per(String lbr_per) {
        this.lbr_per = lbr_per;
    }
    public String getmakingprice() {
        return makingprice;
    }

    public void setmakingprice(String makingprice) {
        this.makingprice = makingprice;
    }
    
    public String gettx_amt() {
        return tx_amt;
    }

    public void settx_amt(String tx_amt) {
        this.tx_amt = tx_amt;
    }
    
    public String getgst() {
        return gst;
    }

    public void setgst(String gst) {
        this.gst = gst;
    }

    public SaleJasperContentDetails() {
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getHuid() {
        return huid;
    }

    public void setHuid(String huid) {
        this.huid = huid;
    }

    public String getGrosswt() {
        return grosswt;
    }

    public void setGrosswt(String grosswt) {
        this.grosswt = grosswt;
    }

    public String getNetwt() {
        return netwt;
    }

    public void setNetwt(String netwt) {
        this.netwt = netwt;
    }

    public String getDiowt() {
        return diowt;
    }

    public void setDiowt(String diowt) {
        this.diowt = diowt;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMakingper() {
        return makingper;
    }

    public void setMakingper(String makingper) {
        this.makingper = makingper;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
    
}
