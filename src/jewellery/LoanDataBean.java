package jewellery;

import java.sql.Date;

public class LoanDataBean {
    private Date date;
    private String slipNo;
    private String partyName;
    private Double loanAmount;
    private Double weight;
    private Long days;
    private Double interestAmount;
    private Double totalAmount;
    private Double currentValue;

    // Constructor
    public LoanDataBean(Date date, String slipNo, String partyName, Double loanAmount, 
                       Double weight, Long days, Double interestAmount, Double totalAmount, 
                       Double currentValue) {
        this.date = date;
        this.slipNo = slipNo;
        this.partyName = partyName;
        this.loanAmount = loanAmount;
        this.weight = weight;
        this.days = days;
        this.interestAmount = interestAmount;
        this.totalAmount = totalAmount;
        this.currentValue = currentValue;
    }

    // Getters
    public Date getDate() { return date; }
    public String getSlipNo() { return slipNo; }
    public String getPartyName() { return partyName; }
    public Double getLoanAmount() { return loanAmount; }
    public Double getWeight() { return weight; }
    public Long getDays() { return days; }
    public Double getInterestAmount() { return interestAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public Double getCurrentValue() { return currentValue; }

    // Setters
    public void setDate(Date date) { this.date = date; }
    public void setSlipNo(String slipNo) { this.slipNo = slipNo; }
    public void setPartyName(String partyName) { this.partyName = partyName; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }
    public void setWeight(Double weight) { this.weight = weight; }
    public void setDays(Long days) { this.days = days; }
    public void setInterestAmount(Double interestAmount) { this.interestAmount = interestAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public void setCurrentValue(Double currentValue) { this.currentValue = currentValue; }
}