import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.SwingPropertyChangeSupport;

class RecipeApp extends HBox {

    private Label index;
    private TextField recipeName;

    RecipeApp() {
        this.setPrefSize(500, 20); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background
                                                                                                     // color of recipe

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the recipe
        this.getChildren().add(index); // add index label to recipe

        recipeName = new TextField(); // create recipe name text field
        recipeName.setPrefSize(380, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(recipeName); // add textlabel to recipe

    }

    public void setRecipeIndex(int num) {
        this.index.setText(num + ""); // num to String
        this.recipeName.setPromptText("Recipe " + num);
    }

    public TextField getRecipeName() {
        return this.recipeName;
    }

}




class RecipeList extends VBox {

    private ArrayList<Recipe> recipes;
    private Stage currStage;
    private App currApp;


    RecipeList(Stage currStage, App currApp) {
        recipes = new ArrayList<Recipe>();
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.currStage = currStage;
        this.currApp = currApp;
        // recipes.add(recipe);
    }


    public ArrayList<Recipe> getRecipeList() {
        return recipes;
    }

    public void setRecipeList(ArrayList<Recipe> newList) {
        this.recipes = newList;
    }

    private Stage getStage() {
        return this.currStage;
    }

    private App getApp() {
        return this.currApp;
    }

    public void saveRecipe() {
        this.getChildren().clear();

        for (Recipe recipe : recipes) {
            Button recipeButton = new Button(recipe.getTitle());
            recipeButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            recipeButton.setPrefSize(500, 50);

            recipeButton.setOnAction(e -> {

                try {
                    DetailView detailFrame = new DetailView(this.getStage(), recipe, this.getApp(), this);
                    Scene scene = new Scene(detailFrame, 500, 600);
                    this.getStage().setTitle("Detail View");
                    this.getStage().setScene(scene);
                    this.getStage().setResizable(true);
                    this.getStage().show();

                } catch (Exception except) {
                    System.err.println("recipeButtonSetOnActionError");
                    except.printStackTrace();
                }
            });

            this.getChildren().add(recipeButton);
        }
    }
}

class Footer extends HBox {

    private Button addButton;
    private ComboBox<String> sortBox;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);

        ObservableList<String> options = FXCollections.observableArrayList(
                "Sort alphabetically (A-Z)",
                "Sort alphabetically (Z-A)",
                "Oldest to Newest",
                "Newest to Oldest");
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Create Recipe"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        this.getChildren().addAll(addButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
        sortBox = new ComboBox<>(options);
        this.getChildren().addAll(sortBox); 

    }

    public Button getAddButton() {
        return addButton;
    }

    public ComboBox<String> getSortBox() {
        return sortBox;
    }

}

class Header extends HBox {

    private Button logOutButton;

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;"); // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        logOutButton = new Button("Log Out"); // text displayed on add button
        logOutButton.setStyle(defaultButtonStyle); // styling the button
        
        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().addAll(logOutButton, titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
    public Button getLogOutButton(){
        return logOutButton;
    }
}

public class ListView extends BorderPane {
    private Scene recipeListScene;
    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button addButton;
    private Button logOutButton;
    private ComboBox<String> sortBox;

    ListView(Stage primaryStage, App currApp, RecipeList recipes) {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the recipes

        this.recipeList = recipes;

        // ArrayList<Recipe> list = ReadRecipes.readRecipes();
        //recipeList.setRecipeList(recipes);
        //recipeList.saveRecipe();

        // Initialise the Footer Object
        footer = new Footer();

        ScrollPane scroll = new ScrollPane(recipeList);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();
        logOutButton = header.getLogOutButton();
        recipeListScene = new Scene(this, 500, 600);

        sortBox = footer.getSortBox();
        recipeListScene = new Scene(this, 500, 600);

        // Call Event Listeners for the Buttons
        addListeners(primaryStage, currApp, recipeList);
    }

    public void addListeners(Stage primaryStage, App currApp, RecipeList recipeList) {

        ArrayList<Recipe> defaultList1 = recipeList.getRecipeList();


        // Add button functionality
        addButton.setOnAction(e -> {
            try {
                /*
                 * StackPane layout1 = new StackPane(addButton);
                 * Scene scene1 = new Scene(layout1, 300, 200);
                 * this.setScene(scene1);
                 */

                CreateRecipeAppFrame detailFrame = new CreateRecipeAppFrame(primaryStage, currApp, recipeList);
                Scene secondScene = new Scene(detailFrame, 500, 600);
                primaryStage.setTitle("Create Recipe");
                primaryStage.setScene(secondScene);
                primaryStage.setResizable(true);
                primaryStage.show();

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });
        logOutButton.setOnAction(e->{
            try{
                CreateAccount createAccount = new CreateAccount(primaryStage, currApp);
                Scene accountScene = new Scene(createAccount, 500,600);
                primaryStage.setScene(accountScene);
                primaryStage.setResizable(true);
                primaryStage.show();

            }catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });
        
        sortBox.setOnAction(e->{
            ArrayList<Recipe> defaultList = new ArrayList<>(defaultList1); // Create a copy of defaultList1
            
            switch(sortBox.getValue().toString()){
                case "Sort alphabetically (A-Z)":
                    //recipeList.setRecipeList(defaultList);
                    recipeList.setRecipeList(Sort.alphabeticalAZSort(defaultList));
                    recipeList.saveRecipe();
                    //recipeList.setRecipeList(defaultList);
                    break;
                case "Sort alphabetically (Z-A)":
                    //recipeList.setRecipeList(defaultList);
                    recipeList.setRecipeList(Sort.alphabeticalZASort(defaultList));
                    recipeList.saveRecipe();
                    //recipeList.setRecipeList(defaultList);
                    break;
                case "Oldest to Newest":
                    // recipeList.setRecipeList(defaultList);
                    recipeList.setRecipeList(Sort.oldestToNewestSort(defaultList));
                    recipeList.saveRecipe();

                    for(Recipe rec : recipeList.getRecipeList()) {
                        System.out.println("Recipe Name: " + rec.getTitle());
                    }
                   // recipeList.setRecipeList(defaultList);
                    break;
                case "Newest to Oldest":
                    
                    for(Recipe rec : recipeList.getRecipeList()) {
                        System.out.println("Recipe Name: " + rec.getTitle());
                    }
                    break;
            }
            
            recipeList.setRecipeList(defaultList);
        });

    }

    public Scene getRecipeListScene() {
        return this.recipeListScene;
    }
}
