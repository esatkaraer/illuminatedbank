
package nl.illuminatedgroup.bankapplicatie;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawRequest {
    @JsonProperty
    private String IBAN;
    @JsonProperty
    private double amount;

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
