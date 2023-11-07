package main;


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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


class Footer extends HBox {

    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        editButton = new Button("Edit"); // text displayed on add button
        editButton.setStyle(defaultButtonStyle); // styling the button
        
        saveButton = new Button("Save"); // text displayed on add button
        saveButton.setStyle(defaultButtonStyle); // styling the button

        deleteButton = new Button("Delete"); // text displayed on add button
        deleteButton.setStyle(defaultButtonStyle); // styling the button

        this.getChildren().addAll(editButton, saveButton, deleteButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

       
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

}

class Header extends HBox {
    private Button backButton;

    Header() {
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #D3D3D3;  -fx-font-weight: bold; -fx-font: 11 arial;";

        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");

        Text titleText = new Text("Recipe Detail"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        backButton = new Button("Back"); // text displayed on add button
        backButton.setStyle(defaultButtonStyle); // styling the button

        this.getChildren().addAll(backButton);
        
    }
}

class RecipeDetail extends BorderPane{

    private Header header;
    private Footer footer;
    private RecipeList recipeList;

    private Button editButton;
    private Button saveButton;
    private Button deleteButton;


    RecipeDetail()
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        recipeList = new RecipeList();
        
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
        editButton = footer.getEditButton();
        deleteButton = footer.getDeleteButton();
        saveButton = footer.getSaveButton();


        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
        
    }
}

public class DetailedView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        RecipeDetail root = new RecipeDetail();

        // Set the title of the app
        primaryStage.setTitle("Recipe Details");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
