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
public class tableObject {

    public Date date;
    public String name;
    public double credit;
    public double debit;
    public int creditOrDebit;  // 0 is credit, 1 is debit
    public String remark;
    public String type;

    public tableObject() {

    }

    public tableObject(Date date, String name, double credit, double debit, int creditOrDebit, String remark, String type) {
        this.date = date;
        this.name = name;
        this.credit = credit;
        this.debit = debit;
        this.creditOrDebit = creditOrDebit;
        this.remark = remark;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }

    public int getCreditOrDebit() {
        return creditOrDebit;
    }

    public String getRemark() {
        return remark;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "tableObject{" + "date=" + date + ", name=" + name + ", credit=" + credit + ", debit=" + debit + ", creditOrDebit=" + creditOrDebit + ", remark=" + remark + ", type=" + type + '}';
    }

}
