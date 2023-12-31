import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;

public class CreateAccount extends BorderPane {
    private Header3 header3;
    private AccountLogIn accountLogIn;

    private Button loginButton;
    private Button signUpButton;
    private CheckBox autoLogin;
    // private Button autoLoginButton;

    private Username username;
    private Password password;
    private String autoCheckStr;
    private Label errorLabel;
    private VBox errorContainer; // Container for error labels
    private PerformRequest request;

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

        errorLabel = new Label("Invalid Username/Password");
        errorLabel.setStyle(defaultLabelStyle);
        errorLabel.setVisible(false);

        currStage.setResizable(true);
        this.setCenter(accountLogIn);

        // Auto Login functionality
        try {

            String filePath = "loginInfo.txt";

            // Create a File object
            File file = new File(filePath);

            // Check if the file exists
            if (file.exists()) {

                BufferedReader inputLine = new BufferedReader(new FileReader("loginInfo.txt"));
                // String textFromText = "";
                autoCheckStr = inputLine.readLine();
                String userNameStr = inputLine.readLine();
                String passwordStr = inputLine.readLine();
                inputLine.close();

                if (autoCheckStr.contains("True")) {
                    request = new PerformRequest();
                    String result = request.performRequest("GET",
                            null,
                            null,
                            userNameStr + "." + passwordStr);
                    if (result.contains("Error")) {
                        errorLabel.setText("Server is down.");
                        errorLabel.setVisible(true);
                    }
                    saveAccount sAccount = new saveAccount();
                    ArrayList<Recipe> userRecipes = request.performRecipeRequest("GET",
                            null,
                            null,
                            userNameStr + ".Recipe");
                    if (result.contains("Error")) {
                        errorLabel.setText("Server is down.");
                        errorLabel.setVisible(true);
                    }
                    sAccount.setUsername(userNameStr);
                    RecipeList recipeList = new RecipeList(currStage, currApp, sAccount);

                    recipeList.setRecipeList(userRecipes);

                    ListView loginListView = new ListView(currStage, currApp, recipeList, sAccount);
                    currStage.setScene(loginListView.getRecipeListScene());
                    
                }

            } 

        } catch (Exception e2) {
            e2.printStackTrace();
        }

        // Add button functionality
        loginButton.setOnAction(e -> {
            request = new PerformRequest();
            String response = request.performRequest("GET",
                    null,
                    null,
                    username.getUsernameField().getText() + "." + password.getPasswordField().getText());
            if (response.contains("Error")) {
                errorLabel.setText("Server is down.");
                errorLabel.setVisible(true);
            }
            errorContainer.getChildren().clear();
            saveAccount sAccount = new saveAccount();

            if (response.equals("Login Successful")) {
                if (autoLoginIsChecked()) {
                    // write to text file autologin = true and username and password used in
                    // successful login
                    try {
                        FileWriter writeLoginInfo = new FileWriter("loginInfo.txt", false);
                        writeLoginInfo.write("AutoLogin: True \n" +
                                username.getUsernameField().getText() + "\n" +
                                password.getPasswordField().getText());
                        writeLoginInfo.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                }

                ArrayList<Recipe> userRecipes = request.performRecipeRequest("GET",
                        null,
                        null,
                        username.getUsernameField().getText() + ".Recipe");

                sAccount.setUsername(username.getUsernameField().getText());
                RecipeList recipeList = new RecipeList(currStage, currApp, sAccount);

                recipeList.setRecipeList(userRecipes);

                Scene currentScene = currStage.getScene();
                if (currentScene != null) {
                    ListView loginListView = new ListView(currStage, currApp, recipeList, sAccount);
                    currStage.setScene(loginListView.getRecipeListScene());
                }
                errorLabel.setVisible(false);
            } else {
                errorContainer.getChildren().add(errorLabel);
                errorLabel.setVisible(true);
            }
        });

        // Add button functionality
        signUpButton.setOnAction(e -> {
            request = new PerformRequest();
            String response = request.performRequest("POST",
                    username.getUsernameField().getText(),
                    password.getPasswordField().getText(),
                    null);
            if (response.contains("Error")) {
                errorLabel.setText("Server is down.");
                errorLabel.setVisible(true);
            }
            
        });

    }

    public boolean autoLoginIsChecked() {
        return this.autoLogin.isSelected();
    }

    public String getAutoCheckStr() {
        return autoCheckStr;
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

    class Password extends HBox {

        private TextField password;
        private Label prompt;

        Password() throws Exception {

            this.setPrefSize(500, 20);
            this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets
                                                                                                         // background
                                                                                                         // color of
                                                                                                         // recipe

            prompt = new Label();
            prompt.setText("Password: "); // create password prompt
            prompt.setPrefSize(80, 20); // set size of Index label
            prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
            this.getChildren().add(prompt); // add index label to task

            password = new TextField(); // create recipe name text field
            password.setPrefSize(380, 20); // set size of text field
            password.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of
                                                                                      // texfield
            password.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
            password.setEditable(true);
            this.getChildren().add(password); // add textlabel to password

        }

        public TextField getPasswordField() {
            return this.password;
        }
    }

    class Username extends HBox {

        private TextField username;
        private Label prompt;

        Username() {
            this.setPrefSize(500, 20);
            this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets
                                                                                                         // background
                                                                                                         // color of
                                                                                                         // recipe

            prompt = new Label();
            prompt.setText("Username: "); // create index label
            prompt.setPrefSize(80, 20); // set size of Index label
            prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
            this.getChildren().add(prompt); // add index label to task

            username = new TextField(); // create recipe name text field
            username.setPrefSize(380, 20); // set size of text field
            username.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of
                                                                                      // texfield
            username.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
            username.setEditable(true);
            this.getChildren().add(username); // add textlabel to recipe
        }

        public TextField getUsernameField() {
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

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(5);

            hBox.getChildren().addAll(loginButton, signUpButton, autoLogin);
            this.getChildren().addAll(hBox);
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