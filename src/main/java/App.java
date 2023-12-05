import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.*;


public class App extends Application {
    private Scene createAccountScene;
    private Scene serverDownScene;
    private Label errorLabel;
    @Override
    public void start(Stage primaryStage) throws Exception {

        /*
         * If server is not active then return an error
         * perform a request to the server to see if server is up
         * if server active then do the following code
         */
        String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; -fx-font-size: 14";
        PerformRequest request = new PerformRequest();
        String serverCheck = request.performRequest("GET", null, null, null);
        if (serverCheck.contains("Error")) {
            errorLabel = new Label("Server is down.");
            errorLabel.setStyle(defaultLabelStyle);
            errorLabel.setVisible(true);
            serverDownScene = new Scene(errorLabel, 500, 600);
            primaryStage.setTitle("Server Down");
            primaryStage.setScene(serverDownScene);
        }
        else{
            CreateAccount account = new CreateAccount(primaryStage, this);
        
            boolean noAutoLogin = true;
            String filePath = "loginInfo.txt";

            // Create a File object
            File file = new File(filePath);

            // Check if the file exists
            if(file.exists()){
                noAutoLogin = account.getAutoCheckStr().contains("False");
            }
            if (noAutoLogin) {
                createAccountScene = new Scene(account, 500, 600);

                // Set the title of the app
                primaryStage.setTitle("PantryPal");
                // Create scene of mentioned size with the border pane
                primaryStage.setScene(createAccountScene);
            
            }
        }

        
         // Make window resizable
        primaryStage.setResizable(true);
        // Show the app  
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}