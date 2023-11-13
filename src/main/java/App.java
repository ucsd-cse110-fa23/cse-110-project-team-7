import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


class RecipeApp extends HBox {

    private Label index;
    private TextField recipeName;

    RecipeApp() {
        this.setPrefSize(500, 20); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe

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
        //recipes.add(recipe);
    }
    
    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipeList(){
        return recipes;
    }
    
    private Stage getStage(){
        return this.currStage;
    }

    private App getApp(){
        return this.currApp;
    }
    
    public void saveRecipe(){
        this.getChildren().clear();

        for(Recipe recipe : recipes) {
            Button recipeButton = new Button(recipe.getTitle());
            recipeButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            recipeButton.setPrefSize(500, 50);

            recipeButton.setOnAction(e->{
                
                
                try{
                    DetailView detailFrame = new DetailView(this.getStage(), recipe, this.getApp(),this);
                    Scene scene = new Scene(detailFrame, 500, 600);
                    this.getStage().setTitle("Detail View");
                    this.getStage().setScene(scene);
                    this.getStage().setResizable(false);
                    this.getStage().show();

                }catch(Exception except){
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
    // private Button clearButton;
    // private Button loadButton;
    // private Button saveButton;
    // private Button sortButton;
    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Create Recipe"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button
        
        this.getChildren().addAll(addButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

       
    }
        
    public Button getAddButton() {
        return addButton;
    }


}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private Header header;
    private Footer footer;
    private RecipeList recipeList;

    private Button addButton;

    AppFrame(Stage primaryStage, App currApp)
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the recipes
        recipeList = new RecipeList(primaryStage, currApp);
        Recipe recipe = new Recipe();
        boolean seenIngredients = false;
        boolean seenInstructions = false;
        int counter = 0;
        try {
            BufferedReader input = new BufferedReader(new FileReader("recipes.csv"));
            String text = input.readLine();
            // while input is not at end of file
            while (text != null) {
                if(text.startsWith("Title: ")){
                    if(counter != 0){
                        recipeList.addRecipe(recipe);
                        seenInstructions = false;
                        recipe = new Recipe();
                    }
                    recipe.setRecipeTitle(text.substring(7, text.indexOf(",")));
                    counter++;
                }
                if(text.startsWith("Ingredients:")){   
                    seenIngredients = true;
                }
                if(text.startsWith("Instructions:")){
                    seenIngredients = false;
                    seenInstructions = true;
                }
                if(seenIngredients){
                    recipe.setIngredients(recipe.getIngredients() + "\n" + text);
                }
                if(seenInstructions){
                    recipe.loadInstructions(recipe.getInstructions() + "\n" + text);
                }
               text = input.readLine();
            }

            input.close(); 
            recipeList.addRecipe(recipe);
            recipeList.saveRecipe();
        

        } catch (Exception e) {
            e.printStackTrace();
        }

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
       

        // Call Event Listeners for the Buttons
        addListeners(primaryStage, currApp, recipeList);
    }

    public void addListeners(Stage primaryStage, App currApp, RecipeList recipeList)
    {

        // Add button functionality
        addButton.setOnAction(e -> {
            try {
                /* 
                StackPane layout1 = new StackPane(addButton);
                Scene scene1 = new Scene(layout1, 300, 200);
                this.setScene(scene1);
                */ 
                
                CreateRecipeAppFrame detailFrame = new CreateRecipeAppFrame(primaryStage, currApp, recipeList);
                Scene secondScene = new Scene(detailFrame, 500, 600);
                primaryStage.setTitle("Create Recipe");
                primaryStage.setScene(secondScene);
                primaryStage.setResizable(false);
                primaryStage.show();

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            

        });
    }
}

public class App extends Application {
    private Scene recipeListScene;
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame(primaryStage, this);
        recipeListScene = new Scene(root, 500, 600);

        // Set the title of the app
        primaryStage.setTitle("Recipe List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(recipeListScene);
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public Scene getRecipeListScene() {
        return this.recipeListScene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}