import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

class FooterOne extends HBox {

    private Button createButton;
    // private Button sortButton;
    // private Button saveCSVButton;

    FooterOne() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        createButton = new Button("Create Recipe"); // text displayed on add button
        createButton.setStyle(defaultButtonStyle); // styling the button
        // sortButton = new Button("Sort"); // text displayed on clear tasks button
        // sortButton.setStyle(defaultButtonStyle);
        // saveCSVButton = new Button("Save as CSV");
        // saveCSVButton.setStyle(defaultButtonStyle);
        
        // CSV Button should be implemeneted here

        this.getChildren().addAll(createButton);

        // this.getChildren().addAll(addButton, sortButton, saveCSVButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        // TODO: Create loadButton, saveButton and sortButton to the footer
    }

    public Button getCreateButton() {
        return createButton;
    }

    // public Button getSortButton() {
    //     return sortButton;
    // }

    // public Button getSaveCSVButton() {
    //     return saveCSVButton;
    // }

    // TODO: Add getters for loadButton, saveButton and sortButton
}