package nl.illuminatedgroup.bankapplicatie;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawResponse 
{
    public WithdrawResponse()
    {

    }
    public WithdrawResponse(double d)
    {
        response = Double.toString(d);
    }
    @JsonProperty
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
