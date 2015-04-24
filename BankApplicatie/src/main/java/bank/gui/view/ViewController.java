package bank.gui.view;

import LabelWriter;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import nl.illuminated.arduino.ArduinoPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import bank.gui.BankClientController;
import bank.gui.MainApp;
import bank.gui.ViewUpdater;


public class ViewController{


	//Welcome
	@FXML
	private Label failReadLabel;
	
	
	//EnterPin
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label enterPinLabel;
	@FXML
	private Button pinTest;
	@FXML
	private Button enterPinAbort;
	
	//SelectionMenu
	@FXML
	private Button selectionMenuWithdrawButton;
	@FXML
	private Button selectionMenuInformationButton;
	@FXML
	private Label selectionMenuDateLabel;
	@FXML
	private Button selectionMenuAbort;
	
	//BalanceInfo
	@FXML
	private Button balanceInfoWithdrawButton;

	@FXML
	private Button balanceInfoAbort;
	@FXML
	private Label balanceInfoLabel;
	
	
	//WithdrawMenu
	@FXML
	private Button withdrawA;
	@FXML
	private Button withdrawB;
	@FXML
	private Button withdrawC;
	@FXML
	private Button withdrawD;
	@FXML
	private Button withdrawConfirm;
	@FXML
	private Button withdrawCancel;
	@FXML
	private TextField withdrawTextField;
	@FXML 
	private Label maxWithdrawLabel;
	
	
	
	//PrintMenu
	@FXML
	private Button printMenuConfirmButton;
	@FXML
	private Button printMenuAbortButton;
	@FXML
	public Label printMenuLabel;
	
	
	private String pin = "0000";
	private int failedPinAmount = 0;
	//private static String s;
	
    // Reference to the main application.
	private MainApp mainApp;

  
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    	String pinchar = prefs.get("pinchar", null);
    	String currentView = prefs.get("currentview", null);
    	if(!pinchar.isEmpty())
    	{
    		if(passwordField != null)
    		{
	        	String pincode = prefs.get("pincode", null); 
	        	pincode += pinchar;
	      		passwordField.setText(pincode);
	      		prefs.put("pincode",pincode);
	      		prefs.put("pinchar", "");
    		}
    		else
    		{
	      		prefs.put("pincode","");
	      		prefs.put("pinchar", "");
    		}
    	}
    	
    	if(currentView.equals("transactiegeannuleerdview"))
    	{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	MainApp.showView("view/WelcomeView.fxml");
                }
            });
    	}
    	
    	if(currentView.equalsIgnoreCase("balanceinfoview"))
    	{
    		DataController dc = new DataController();
    		dc.getSaldo();
			balanceInfoLabel.setText("Uw saldo bedraagt : € " + prefs.get("saldo", null));
    	}
    	
    	if(currentView.equalsIgnoreCase("withdrawmenuview"))
    	{
    		
    	}
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleAbort(){
    	System.out.println("returning to welcomeview");
    	MainApp.showView("view/WelcomeView.fxml");
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put("currentview", "welcomeview");
    	// TODO fields leegmaken
   
    }
    @FXML
    private void handleWelcomePassScanned(){
    	
    	MainApp.showView("view/EnterPin.fxml");
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put("currentview", "enterpinview");
    	// TODO failedPinAmount verwerken
    }
    
    @FXML
    private void handlePinTest(){
    	System.out.println("testing pin switch");
    	if(failedPinAmount<3){
	    	if(passwordField.getText().equals(pin)){
	    		System.out.println("Correct PIN");
	    		failedPinAmount = 0;
	        	MainApp.showView("view/SelectionMenu.fxml");
	            Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	            prefs.put("currentview", "selectionmenuview");
	    	}
	    	else{
	    		System.out.println("Wrong Pin");
	    		failedPinAmount++;
	    	}
    	}
    	else{
    		System.out.println("Too many failed attempts");
    	}
    }
    
    @FXML
    private void handleSelectionBalanceInfo(){
    	System.out.println("testing BalanceInfo Switch");
    		MainApp.showView("view/BalanceInfo.fxml");
            Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
            prefs.put("currentview", "balanceinfoview");
        }
    
    @FXML
    private void handleSelectionWithdrawMenu(){
    	System.out.println("testing WithdrawMenu Switch");
    	MainApp.showView("view/WithdrawMenu.fxml");
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put("currentview", "withdrawmenuview");
    }
    
    @FXML
    private void handleFastWithdraw20(){
    	handleSelectionPrintMenu(20);
    }
    @FXML
    private void handleFastWithdraw50(){
    	handleSelectionPrintMenu(50);
    }
    @FXML
    private void handleFastWithdraw70(){
    	handleSelectionPrintMenu(70);
    }
    @FXML
    private void handleCustomWithdraw(){
    	String selection=withdrawTextField.getText();
    	System.out.println("Selection: "+selection);
    	
		int i = Integer.valueOf(selection);
		System.out.println("Selection in int: "+i);
    	handleSelectionPrintMenu(i);
	
    	
    }
    
    
    @FXML
    private void handleSelectionPrintMenu(int i){
    	System.out.println("Withdrawing "+i+" euros");
    	MainApp.showView("view/PrintMenu.fxml");
    	String string = "U heeft "+i+" euro opgenomen. Wilt u een bon?";
    	System.out.println(string);
    	//StringProperty withdrawAmount = new SimpleStringProperty();

    	//withdrawAmount.bind(maxWithdrawLabel.textProperty());

    	//maxWithdrawLabel.setText(string);
    	Label testLabel = new Label();
    	testLabel.setText(string);
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put("currentview", "printmenuview");
    	
    }
    @FXML
    private void handlePrintMenuA(){
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		//get current date time with Date()
		Date date = new Date();
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put("transactienr", "0001");
        String accnr = prefs.get("rekeningnr", "0000000000");
        String shortenedaccnr = accnr.substring(accnr.length() - 3);
		
		String[] text = new String[14];
		  text[0] = "Geldopnamebon";
		  text[1] = "ILLUMINATED Group";
		  
		  text[2] = "Automaatnr: ILMG-0001";
		  text[3] = "Transactienr: "+prefs.get("transactienr", "0000");
		  text[4] = "";
		  
		  text[5] = ("Rekeningnr: xxxxxxx"+shortenedaccnr);
		  text[6] = "Pasnr: 101";
		  text[7] = "";

		  text[8] = "Datum: "+dateFormat.format(date);
		  text[9] = "";

		  text[10] = "Withdraw Amount: ";
		  text[11] = "";
		  text[12] = "";

		  text[13] = "";//"Illuminated Group";
		   
		  LabelWriter labelwriter = new LabelWriter(); 
		  labelwriter.printLabel(text);
    }


}
