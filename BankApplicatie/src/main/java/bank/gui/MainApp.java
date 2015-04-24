package bank.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import nl.illuminated.arduino.ArduinoPort;
import bank.gui.view.DataController;
import bank.gui.view.ViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;





public class MainApp extends Application {

    private Stage primaryStage;
    private static BorderPane rootLayout;
    private FXMLLoader welcomeLoader;
    private ViewController viewController;
    private DataController dc;
    private boolean isRunning = true;
	private BankClientController bc = new BankClientController();
	private String row;
	
	
    public MainApp() {
    }

    
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BankApp");
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    	prefs.put("pincode", "");
    	prefs.put("pinchar", "");
        prefs.put("currentview", "welcomeview");
        
        initRootLayout();
        System.out.println("RootLayout Loaded...");
        welcomeLoader = new FXMLLoader();
        showWelcomeView();
        System.out.println("Welcomeview Loaded...");
        viewController = (ViewController)welcomeLoader.getController();
        viewController.setMainApp(this);
        
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader rootLoader = new FXMLLoader();


            rootLoader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) rootLoader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Welcome View inside the root layout.
     */
    
    public void showWelcomeView() {
        try {
            welcomeLoader.setLocation(MainApp.class.getResource("view/WelcomeView.fxml"));
            AnchorPane view = (AnchorPane) welcomeLoader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception....");

        }  
       
        
        Platform.runLater(new Runnable() {
            @Override public void run() {
            	dc = new DataController();
                dc.start();
            }
        });
    }
    
    public static void showView(String viewpath) {
        

    	try {
    		
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(viewpath));
            AnchorPane view = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(view);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception....");

        }  	
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }	
}