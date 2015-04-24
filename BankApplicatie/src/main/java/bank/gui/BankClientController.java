package bank.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import nl.illuminated.arduino.ArduinoPort;
import nl.illuminatedgroup.bankapplicatie.WithdrawRequest;
import nl.illuminatedgroup.bankapplicatie.WithdrawResponse;

import org.glassfish.jersey.jackson.JacksonFeature;

import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;

/**
 *
 * @author Esat
 */
public class BankClientController
{
    private Client client = ClientBuilder.newClient().register(JacksonFeature.class);
    private String target = "145.24.222.103:8080";
    
    public ArduinoPort getStream()
    {
        try
        {
            ArduinoPort port = new ArduinoPort("COM7", 9600);//COM1 of COM2 of COM3
            port.start();
            return port;
        }
        catch (NoSuchPortException | PortInUseException ex)
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public WithdrawResponse withdraw(WithdrawRequest request)
    {
        System.out.println("Iban is binnen : " + request.getIBAN() + " amount is binnen : " + request.getAmount());
        WithdrawResponse response = client
                .target("http://" + target)
                .path("/withdraw")
                .request()
                .post(Entity.entity(request,MediaType.APPLICATION_JSON),
                        WithdrawResponse.class);
        
        return response;
    }
    
    public WithdrawResponse balance(String IBAN)
    {
        System.out.println("Iban is binnen : " + IBAN);
         WithdrawResponse response = client
                .target("http://" + target)
                .path("/balance/" + IBAN).request().get(WithdrawResponse.class);
         return response;
    }
}
