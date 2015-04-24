package nl.illuminatedgroup.bankapplicatie;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class BankEndPoint 
{
    /*
     * GET - ophalen
     * POST - toevoegen
     * PUT - wijzigen
     * DELETE - verwijderen
     */
    
    @GET
    @Path("/balance/{rekeningNummer}")
    public double getSaldo(@PathParam("rekeningNummer") String rekeningNummer)
    {
        Database db = Database.getInstance();
        return db.getBalance(rekeningNummer);
    }
    
    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public WithdrawResponse withdraw(WithdrawRequest request)
    {
        WithdrawResponse response = new WithdrawResponse();
        if(request.getAmount() < 1000)
        {
            System.out.println("withdraw breakendpoint");
            Database db = Database.getInstance();
            String iban = request.getIBAN();
            double amount = request.getAmount();
            System.out.println("iban = " + iban + "amount = " + amount);
            boolean gelukt = db.withdraw(iban, amount);
            System.out.println("withdraw gelukt!");
            response.setResponse("Bedankt voor het printen");
        }
        else
        {
            response.setResponse("Je bent arm, ga weg....");
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
        }
        return response;
    }
}
