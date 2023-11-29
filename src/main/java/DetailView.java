import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;
import java.util.ArrayList;

public class DetailView extends BorderPane{
    private Header2 header2;
    private Footer2 footer2;
    private Details details;

    private Button backButton;
    private Button editButton;
    private Button deleteButton;
    private Button saveButton;
    private boolean inEditMode = false; 
    String instructions;
    String ingredients = ""; 

    DetailView(Stage currStage, Recipe response, App currApp, RecipeList recipeList, saveAccount saveAccount) throws Exception{
        String title = response.getTitle();
        header2 = new Header2(title);
        footer2 = new Footer2();
        if(!response.getInstructions().contains("Inputted Ingredients: ")){
            ingredients = "Inputted Ingredients: " + response.getIngredients() + "\n";
        }
        response.setInstructions(ingredients + response.getInstructions());

        details = new Details(response.getInstructions(), response.getTitle());
        instructions = details.getInstructions().getText();
        
        this.setTop(header2);
        this.setBottom(footer2);
        this.setCenter(details);
        
        backButton = header2.getBackButton();
        editButton = footer2.getEditButton();
        deleteButton = footer2.getDeleteButton();
        saveButton = footer2.getSaveButton();

        currStage.setResizable(true);
        

        // Add button functionality
        editButton.setOnAction(e -> {
            details.setEditable(true);
            inEditMode = true;
            instructions = details.getInstructions().getText();
            


        });

        saveButton.setOnAction(e ->{
            // if in edit mode 
            if(inEditMode){
                details.setEditable(false);
                inEditMode = false; 
                instructions = details.getInstructions().getText();
                response.loadInstructions(instructions);
                try {
                    ArrayList<Recipe> newRecipe = saveRecipe.saveARecipe(recipeList.getRecipeList(), response);
                    recipeList.setRecipeList(newRecipe);
                    saveAccount.saveRecipesForUser(saveAccount.getUsername(), newRecipe);

                    recipeList.saveRecipe();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            else {  
                try {

        
                    ArrayList<Recipe> newRecipe = saveRecipe.saveARecipe(recipeList.getRecipeList(), response);
                    recipeList.setRecipeList(newRecipe);
                    saveAccount.saveRecipesForUser(saveAccount.getUsername(), newRecipe);
                    recipeList.saveRecipe();
                    

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
        });

        // if in edit mode, revert changes
        backButton.setOnAction(e->{
            if(inEditMode){
                details.getInstructions().setText(instructions);
                details.setEditable(false);
                inEditMode = false; 
            }
            else {
                ListView listview = new ListView(currStage, currApp, recipeList, saveAccount);
                currStage.setScene(listview.getRecipeListScene());
            }
        });        

        deleteButton.setOnAction(e->{

            ArrayList<Recipe> newList = DeleteRecipe.deleteTargetRecipe(recipeList.getRecipeList(), response);
            recipeList.setRecipeList(newList);
            saveAccount.deleteRecipeFromDatabase(saveAccount.getUsername(), response);
            recipeList.saveRecipe();

        });

        
    }

}

class Header2 extends HBox{
    private Button backButton;
    private Text titleText;
    Header2(String title) {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        titleText = new Text(title); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        backButton = new Button("Back"); 
        backButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(backButton, titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }

    public Button getBackButton(){
        return backButton;
    }
    public Text getTitleText() {
        return titleText;
    }
}
class Footer2 extends HBox {
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton; 

    Footer2() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        editButton = new Button("Edit"); // text displayed on add button
        editButton.setStyle(defaultButtonStyle); // styling the button

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);

        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);


        this.getChildren().addAll(editButton, saveButton, deleteButton); // adding buttons to footer
    }

    public Button getEditButton(){
        return editButton;
    }

    public Button getSaveButton(){
        return saveButton;
    }

    public Button getDeleteButton(){
        return deleteButton;
    }

}

class Details extends VBox {
    private TextArea instructions;
    private ImageView imageView;

    Details(String response, String title) {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setAlignment(Pos.CENTER); // Center the content vertically and horizontally

        // Add ImageView
        String prompt = title.replaceAll("\\s", "");
        String url = "recipeImage" + prompt + ".jpg";
        System.out.println(url);
        File imageFile = new File(url);

        Image image = new Image(imageFile.toURI().toString());


        imageView = new ImageView(image); // Specify the path to your image
        imageView.setFitWidth(150); // Set the width of the image
        imageView.setFitHeight(150); // Set the height of the image
        imageView.setPreserveRatio(true); // Maintain the aspect ratio


        instructions = new TextArea(response); // Use TextArea for multi-line text
        instructions.setPrefSize(500, 560); // set size of text area
        instructions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color
        instructions.setWrapText(true); // Enable text wrapping for multi-line content
        instructions.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text area
        instructions.setEditable(false);
        this.getChildren().addAll(imageView, instructions); // add text area to the VBox
    }

    public TextInputControl getInstructions() {

        return instructions;
    }
    public void setEditable(boolean editable) {
        instructions.setEditable(editable);
    }

    
}