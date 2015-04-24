/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import bank.gui.BankClientController;
import bank.gui.MainApp;
import nl.illuminated.arduino.ArduinoPort;
import nl.illuminatedgroup.bankapplicatie.WithdrawRequest;
import nl.illuminatedgroup.bankapplicatie.WithdrawResponse;

/**
 *
 * @author Esat
 */

public class DataController extends Thread
{

    private boolean isRunning = true;
    private BankClientController bc = new BankClientController();
    private String rekeningNummer = "";
    String row = "";
    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

    public void run()
    {
	    ArduinoPort dataStream = bc.getStream();
	    prefs.put("pincode", "");
	    prefs.put("pinchar", "");
	    if(dataStream != null)
	    {
	    	while(isRunning)
	    	{
	    		try
	            {
	                row = (String) dataStream.getRxQueue().take();
	                System.out.println("read : " + row);
	                if(row.length() == 10)
	                {
	                    prefs.put("rekeningnummer", row);
	                    prefs.put("currentview", "enterpinview");
	                    Platform.runLater(new Runnable() {
	                        @Override
	                        public void run() {
	                        	MainApp.showView("view/EnterPin.fxml");
	                        }
	                    });
	                }
	                String currentView = prefs.get("currentview", null);
	    			if(currentView.equals("enterpinview"))
	    			{
		    				if(row.equals("#"))
		    				{
		    		            prefs.put("currentview", "selectionmenuview");
			                    Platform.runLater(new Runnable() {
			                        @Override
			                        public void run() {
			                        	//pin check
			                        	MainApp.showView("view/SelectionMenu.fxml");
			                        }
			                    });
		    				}
		    				else if(isNumber(row))
		    				{
		    					if(row.length() == 1)
		    					{
				                    Platform.runLater(new Runnable() {
				                        @Override
				                        public void run() {
				                        	prefs.put("pinchar", row);
				                        	MainApp.showView("view/EnterPin.fxml");
				                        }
				                    });
		    					}
		    				}
		    				else if(row.equals("*"))
		    				{
	                        	dataStream.SerialPortWriter();
		    		            prefs.put("currentview", "transactiegeannuleerdview");
	                        	prefs.put("rekeningnummer", "");
	                        	prefs.put("pincode", "");
	                        	prefs.put("pinchar", "");
			                    Platform.runLater(new Runnable() {
			                        @Override
			                        public void run() {
			                        	MainApp.showView("view/TransactieGeannuleerd.fxml");
			                        }
			                    });
		    				}
	    			}
		    		if(currentView.equals("selectionmenuview"))
		    		{
	    				if(row.equals("A"))
	    				{
	    		            prefs.put("currentview", "withdrawmenuview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	MainApp.showView("view/WithdrawMenu.fxml");
		                        }
		                    });
	    				}
	    				else if(row.equals("B"))
	    				{
	    		            prefs.put("currentview", "balanceinfoview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	MainApp.showView("view/BalanceInfo.fxml");
		                        }
		                    });
	    				}
	    				else if(row.equals("*"))
	    				{
                        	dataStream.SerialPortWriter();
	    		            prefs.put("currentview", "transactiegeannuleerdview");
                        	prefs.put("rekeningnummer", "");
                        	prefs.put("pincode", "");
                        	prefs.put("pinchar", "");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	MainApp.showView("view/TransactieGeannuleerd.fxml");
		                        }
		                    });
	    				}
		    		}
		    		if(currentView.equals("withdrawmenuview"))
		    		{
	    				if(row.equals("A"))
	    				{
                        	prefs.put("amount", "20");
	    		            prefs.put("currentview", "printmenuview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	//20 euri pinnen
		                        	MainApp.showView("view/PrintMenu.fxml");
		                        }
		                    });
	    				}
	    				if(row.equals("B"))
	    				{
                        	prefs.put("amount", "50");
	    		            prefs.put("currentview", "printmenuview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	//50 euri pinnen
		                        	MainApp.showView("view/PrintMenu.fxml");
		                        }
		                    });
	    				}
	    				if(row.equals("C"))
	    				{
                        	prefs.put("amount", "70");
	    		            prefs.put("currentview", "printmenuview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	//70 euri pinnen
		                        	MainApp.showView("view/PrintMenu.fxml");
		                        }
		                    });
	    				}
	    				if(row.equals("#"))
	    				{
	    		            prefs.put("currentview", "printmenuview");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	//custom euri pinnen
		                        	MainApp.showView("view/PrintMenu.fxml");
		                        }
		                    });
	    				}
	    				else if(row.equals("*"))
	    				{
                        	dataStream.SerialPortWriter();
	    		            prefs.put("currentview", "transactiegeannuleerdview");
                        	prefs.put("rekeningnummer", "");
                        	prefs.put("pincode", "");
                        	prefs.put("pinchar", "");
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                        	MainApp.showView("view/TransactieGeannuleerd.fxml");
		                        }
		                    });
	    				}
	    				else if(isNumber(row))
	    				{
	    					if(row.length() == 1)
	    					{
		    		            prefs.put("currentview", "withdrawmenuview");
			                    Platform.runLater(new Runnable() {
			                        @Override
			                        public void run() {
			                        	prefs.put("amountchar", row);
			                        	MainApp.showView("view/WithdrawMenu.fxml");
			                        }
			                    });
	    					}
	    				}
		    		}
	            }
	            catch (InterruptedException ex)
	            {
	                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
	            }
	    	}
	    }
    }
    public void getSaldo()
    {
        BankClientController bc = new BankClientController();
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String rekeningNummer = prefs.get("rekeningnummer", null);
        WithdrawResponse r = bc.balance(rekeningNummer);
        System.out.println(r.getResponse());
    	String saldo = r.getResponse().replace('.', ',');
        prefs.put("saldo", saldo);
    }
    public void withdraw(int amount)
    {
        BankClientController bc = new BankClientController();
        WithdrawRequest rq = new WithdrawRequest();
        rq.setAmount(amount);
        rq.setIBAN(rekeningNummer);
    	WithdrawResponse r = bc.withdraw(rq);
    }
    
    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
