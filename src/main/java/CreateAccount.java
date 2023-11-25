import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class CreateAccount extends BorderPane {
    private Header3 header3;
    private AccountLogIn accountLogIn;

    private Button loginButton;
    private Button signUpButton;
    private CheckBox autoLogin;
    // private Button autoLoginButton;
    
    private Username username; 
    private Password password;

    private Label errorUsernameLabel; 
    private Label errorPasswordLabel;
    private VBox errorContainer;  // Container for error labels


    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; -fx-font-size: 14";

    CreateAccount(Stage currStage, App currApp) throws Exception {
        header3 = new Header3("Sign Up/Log in");
        accountLogIn = new AccountLogIn();
        this.setTop(header3);

        loginButton = accountLogIn.getLoginButton();
        signUpButton = accountLogIn.getSignUpButton();
        autoLogin = accountLogIn.getAutoLogin();

        errorContainer = new VBox();
        errorContainer.setSpacing(5);
        this.setBottom(errorContainer);

        errorUsernameLabel = new Label("Invalid Username");
        errorUsernameLabel.setStyle(defaultLabelStyle);

        errorPasswordLabel = new Label("Invalid Password");
        errorPasswordLabel.setStyle(defaultLabelStyle);
        errorPasswordLabel.setVisible(false);
        errorUsernameLabel.setVisible(false);

        currStage.setResizable(true);
        this.setCenter(accountLogIn);


        // Add button functionality
        loginButton.setOnAction(e -> {
            errorContainer.getChildren().clear();
            saveAccount sAccount = new saveAccount();
            int result = sAccount.loginAccount(username.getUsernameField().getText(), password.getPasswordField().getText());

            if(result == -1){
                errorContainer.getChildren().add(errorUsernameLabel);
                errorUsernameLabel.setVisible(true);  
                errorPasswordLabel.setVisible(false);


            }
            else if(result == 0){
                errorContainer.getChildren().add(errorPasswordLabel);
                errorUsernameLabel.setVisible(false); 
                errorPasswordLabel.setVisible(true);

            }
            else{
                ListView login = new ListView(currStage, currApp);
                currStage.setScene(login.getRecipeListScene());
                errorUsernameLabel.setVisible(false);  
                errorPasswordLabel.setVisible(false);

            }
           
        });

        // Add button functionality
        signUpButton.setOnAction(e -> {
            errorContainer.getChildren().clear();
            //use saveAccount class to save user's new account to database    
            saveAccount sAccount = new saveAccount();
            boolean saved = sAccount.generateNewAccount(username.getUsernameField().getText(), password.getPasswordField().getText());
            if(!saved){
                errorContainer.getChildren().add(errorUsernameLabel);
                errorUsernameLabel.setVisible(true);  // explicitly set visibility
                errorPasswordLabel.setVisible(false);

            }
            else{
                ListView login = new ListView(currStage, currApp);
                currStage.setScene(login.getRecipeListScene());            
            }

        });

        // Add button functionality
        autoLogin.setOnAction(e -> {
            
        });


    }

    class Header3 extends HBox {
        private Text titleText;

        Header3(String title) {
            this.setPrefSize(500, 60); // Size of the header
            this.setStyle("-fx-background-color: #FFFFFF;");
            titleText = new Text(title); // Text of the Header
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(titleText);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
        }
    }

class Password extends HBox{
    
    private TextField password;
    private Label prompt;

    Password() throws Exception{
        
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        prompt = new Label();
        prompt.setText("Password: "); // create password prompt
        prompt.setPrefSize(60, 20); // set size of Index label
        prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(prompt); // add index label to task

        password = new TextField(); // create recipe name text field
        password.setPrefSize(380, 20); // set size of text field
        password.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        password.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        password.setEditable(true);
        this.getChildren().add(password); // add textlabel to password
        
    }

    public TextField getPasswordField()  {
        return this.password;
    }
}

class Username extends HBox{
    
    private TextField username;
    private Label prompt;
    
    Username(){
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        prompt = new Label();
        prompt.setText("Username: "); // create index label
        prompt.setPrefSize(80, 20); // set size of Index label
        prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(prompt); // add index label to task

        username = new TextField(); // create recipe name text field
        username.setPrefSize(380, 20); // set size of text field
        username.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        username.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        username.setEditable(true);
        this.getChildren().add(username); // add textlabel to recipe
    }
    
    public TextField getUsernameField()  {
        return this.username;
    }
}

    class AccountLogIn extends VBox {
       
        AccountLogIn() throws Exception {

            this.setSpacing(5); // sets spacing between tasks
            this.setPrefSize(500, 560);
            this.setStyle("-fx-background-color: #FFFFFF;");
            
            username = new Username();
            password = new Password();

            this.getChildren().addAll(username, password);

            
            loginButton = new Button("Log In");
            signUpButton = new Button("Sign Up");
            autoLogin = new CheckBox("Auto Login");
            //autoLoginButton = new Button("Automatic Log in");

            this.getChildren().addAll(loginButton, signUpButton, autoLogin);
        }

        public Button getLoginButton() {
            return loginButton;
        }

        public Button getSignUpButton() {
            return signUpButton;
        }

        public CheckBox getAutoLogin() {
            return autoLogin;
        }
    }


}