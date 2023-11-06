import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

class FooterTwo extends HBox {
    private Button updateButton;
    private Button deleteButton;
    private Button uploadImgButton;

    FooterTwo() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        // updateButton = new Button("Update"); // text displayed on add button
        // updateButton.setStyle(defaultButtonStyle); // styling the button
        // deleteButton = new Button("Delete"); // text displayed on clear tasks button
        // deleteButton.setStyle(defaultButtonStyle);
        // uploadImgButton = new Button("Image upload");
        // uploadImgButton.setStyle(defaultButtonStyle);

        // CSV Button should be implemeneted here

        // this.getChildren().addAll(updateButton, deleteButton, uploadImgButton); // adding buttons to footer
        // this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    // public Button getUpdateButton() {
    //     return this.updateButton;
    // }

    // public Button getDeleteButton() {
    //     return this.deleteButton;
    // }

    // public Button getUploadImgButton(){
    //     return this.uploadImgButton;
    // }

}
