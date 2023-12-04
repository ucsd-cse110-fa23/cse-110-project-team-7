import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DetailView extends BorderPane{
    private Header2 header2;
    private Footer2 footer2;
    private Details details;

    private Button backButton;
    private Button editButton;
    private Button deleteButton;
    private Button saveButton;
    private Button refreshButton;
    private Button shareButton; 
    
    private boolean inEditMode = false; 
    String instructions;
    String ingredients = ""; 
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    DetailView(Stage currStage, Recipe response, App currApp, RecipeList recipeList, saveAccount saveAccount) throws Exception{
        String title = response.getTitle();
        header2 = new Header2(title);
        footer2 = new Footer2(recipeList, response);
        if(!response.getInstructions().contains("Inputted Ingredients: ")){
            ingredients = "Inputted Ingredients: " + response.getIngredients() + "\n";
        }

        details = new Details(ingredients + response.getInstructions(), response.getImageUrl());
        instructions = details.getInstructions().getText();
        
        this.setTop(header2);
        this.setBottom(footer2);
        this.setCenter(details);
        
        backButton = header2.getBackButton();
        editButton = footer2.getEditButton();
        deleteButton = footer2.getDeleteButton();
        saveButton = footer2.getSaveButton();
        refreshButton = footer2.getRefreshButton();
        shareButton = footer2.getShareButton();

        

        currStage.setResizable(true);
        

        // Add button functionality
        editButton.setOnAction(e -> {
            details.setEditable(true);
            inEditMode = true;
            instructions = details.getInstructions().getText();
            


        });

        saveButton.setOnAction(e ->{
            
            details.setEditable(false);
            inEditMode = false; 
            instructions = details.getInstructions().getText();
            response.loadInstructions(instructions);
            try {
                PerformRequest request = new PerformRequest();

                ArrayList<Recipe> newRecipe = saveRecipe.saveARecipe(recipeList.getRecipeList(), response);
                /*
                 * make a request to server using newRecipe's information as parameters
                 */

                System.out.println("RECIPE: " + response.getTitle() + "/" +
                response.getIngredients() + "/" +
                response.getInstructions() + "/" +
                response.getMealType()+ "/" +
                response.getImageUrl());
                System.out.println("USERNAME: " + saveAccount.getUsername());
                String result = request.performRecipeSaving("PUT", 
                saveAccount.getUsername(), 
                response.getTitle(), 
                response.getIngredients(), 
                response.getInstructions(), 
                response.getMealType(),
                response.getImageUrl(), null);

                System.out.println("SAVE TO MONGODB RESULT: " + result);
                recipeList.setRecipeList(newRecipe);
                recipeList.saveRecipe();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
            PerformRequest request = new PerformRequest();
            String oldTitle = response.getTitle();
            if(response.getTitle().contains(" ")){
                    oldTitle = response.getTitle().replaceAll(" ", "%20");
            }
            String result = request.performRequest("DELETE",
            null,
            null, 
            saveAccount.getUsername() + "_" + oldTitle);
            recipeList.saveRecipe();
            System.out.println(result);

        });

        shareButton.setOnAction(e->{

            // we pull from database 
            // we somehow add it to a page 
            // and create a link to that page
            //PerformRequest request = new PerformRequest();
            //request.
            String linkTitle = response.getTitle();
            if(response.getTitle().contains(" ")){
                linkTitle = response.getTitle().replaceAll(" ", "%20");
            }
            footer2.setText("http://localhost:8100/recipe/?=" + saveAccount.getUsername() + 
            "_" + linkTitle);

            footer2.showShareLink(); // show link 
            

        });

        refreshButton.setOnAction(e->{
            try {
                System.out.println("RECIPE INGREDIENT: " + response.getIngredients());
                if(response.getIngredients() != "" && response.getMealType() != ""){
                    PerformRequest request = new PerformRequest();
                    System.out.println("RECIPE INGREDIENTS #22222222222: " + response.getIngredients());
                    String ingredients = response.getIngredients();
                    if(response.getIngredients().contains(" ")){
                        ingredients = response.getIngredients().replaceAll(" ", "%20");
                    }
                    String result = request.performRequest("GET", 
                    null, 
                    null,
                    ingredients + "&" + response.getMealType() + "_" + "Create");
                    System.out.println("RESPONSE: " + result);
                    response.setTitle(result);
                    
                    System.out.println("TITLE: "+ response.getTitle());
                    response.setInstructions(result);
                    String newTitle = response.getTitle();
                    if(newTitle.contains(" ")){
                        newTitle = response.getIngredients().replaceAll(" ", "%20");
                    }
                    String url = request.performRequest("GET", 
                    null, 
                    null, 
                    newTitle + "." + "DallE");
                    System.out.println(url);
                    response.setImageUrl(url);
                    System.out.println(response.getImageUrl());
                    DetailView detailFrame = new DetailView(currStage, response, currApp, recipeList, saveAccount);
                    Scene scene = new Scene(detailFrame, 500, 600);
                    currStage.setTitle("Detail View");
                    currStage.setScene(scene);
                    currStage.setResizable(true);
                    currStage.show();
                }
                
                
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            

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
    private Button refreshButton; 
    private Button shareButton; 
    private TextArea shareLink;


    Footer2(RecipeList recipeList, Recipe r) {
        this.setPrefSize(500, 100);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";
        String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

        shareLink = new TextArea("No Link"); 
        shareLink.setStyle(defaultLabelStyle);
        shareLink.setVisible(true);
        shareLink.setPrefSize(200, 50);
        shareLink.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color
        shareLink.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text area
        shareLink.setEditable(false);
        
        editButton = new Button("Edit"); // text displayed on add button
        editButton.setStyle(defaultButtonStyle); // styling the button

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);

        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);

        refreshButton = new Button("Refresh");
        refreshButton.setStyle(defaultButtonStyle);

        shareButton = new Button("Share");
        shareButton.setStyle(defaultButtonStyle);

        if(r.recipeExists(recipeList.getRecipeList())){
            refreshButton.setVisible(false);
        }

        


       // Create a HBox for shareLink
        HBox shareLinkHBox = new HBox();
        shareLinkHBox.getChildren().add(shareLink);

        // Create an HBox for buttons
        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(editButton, saveButton, deleteButton, shareButton, refreshButton);

        // Create a StackPane to stack shareLinkVBox on top of buttonsHBox
        //StackPane stackPane = new StackPane();
        //stackPane.getChildren().addAll(buttonsHBox, shareLinkHBox);

        this.getChildren().addAll(buttonsHBox, shareLinkHBox);
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
    public Button getRefreshButton(){
        return refreshButton;
    }
    public Button getShareButton(){
        return shareButton;
    }
    public void showShareLink() {
        shareLink.setVisible(true);
    }
    public void setText(String text){
        shareLink.setText(text);
    }

}

class Details extends VBox {
    private TextArea instructions;
    private ImageView imageView;

    Details(String response, String url) {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setAlignment(Pos.CENTER); // Center the content vertically and horizontally

        
        Image image = new Image(url);
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
    public ImageView getImageView() {
        return imageView;
    }
    public void setEditable(boolean editable) {
        instructions.setEditable(editable);
    }

    
}