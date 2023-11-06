import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text; 
import javafx.scene.image.*;

// import java.scene.image.*;

class RecipeDetail extends VBox {
    // private Image image;
    // private ImageView imageview;
    // private TextField nameField;
    // private TextField emailField;
    // private TextField numberField;
    // private Label nameLabel;
    // private Label emailLabel;
    // private Label numberLabel;
    // private Text promptText;

    // ContactDetail() {

    //     this.setPrefSize(550, 350); // sets size of task
    //     this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background
    //     this.setSpacing(20);
    //     this.setFillWidth(false);
    //     this.setAlignment(Pos.CENTER);
        

    //     imageview = new ImageView();
    //     imageview.setStyle("-fx-border-width: 5px; -fx-border-color: #000000");
    //     VBox tmpVbox = new VBox(imageview);
    //     tmpVbox.setPrefSize(300, 300);
    //     tmpVbox.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 3px; -fx-border-color:#000000; -fx-font-weight: bold;");
    //     tmpVbox.setAlignment(Pos.CENTER);
    //     this.getChildren().add(tmpVbox);

    //     HBox nameBox = new HBox();
    //     nameBox.setPrefSize(500, 20);
    //     nameBox.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
    //     nameField = new TextField();
    //     nameField.setPrefSize(400, 20);
    //     nameLabel = new Label("Here");
    //     nameLabel.setPrefSize(65, 20);
    //     nameLabel.setAlignment(Pos.CENTER);

    //     // promptText = new Text();
    //     // promptText.setText("Here aladkjflaksdjflkadsjklfdajsf");
    //     // promptText.setX(100);
    //     // promptText.setY(100);
    //     // promptText.setAlignment(Pos.CENTER);
        
    //     nameBox.getChildren().addAll(nameLabel, nameField);

    //     this.getChildren().add(nameBox);

    //     this.setSpacing(20); // sets spacing between tasks

    //     HBox emailBox = new HBox();
    //     emailBox.setPrefSize(500, 20);
    //     emailBox.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
    //     emailField = new TextField();
    //     emailField.setPrefSize(400, 20);
    //     emailLabel = new Label("Email");
    //     emailLabel.setPrefSize(65, 20);
    //     emailLabel.setAlignment(Pos.CENTER);
    //     emailBox.getChildren().addAll(emailLabel, emailField);
    //     this.getChildren().add(emailBox);

    //     this.setSpacing(20); // sets spacing between tasks

    //     HBox numberBox = new HBox();
    //     numberBox.setPrefSize(500, 20);
    //     numberBox.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
    //     numberField = new TextField();
    //     numberField.setPrefSize(400, 20);
    //     numberLabel = new Label("Phone #");
    //     numberLabel.setPrefSize(65, 20);
    //     numberLabel.setAlignment(Pos.CENTER);
    //     numberBox.getChildren().addAll(numberLabel, numberField);
    //     this.getChildren().add(numberBox);

    // }

    // TextField getName() {
    //     return this.nameField;
    // }

    // TextField getPhoneNumber() {
    //     return this.numberField;
    // }

    // TextField getEmail() {
    //     return this.emailField;
    // }

    // Image getImage(){
    //     return this.image;
    // }

    // void setName(String name) {
    //     this.nameField.setText(name);
    // }

    // void setEmail(String email) {
    //     this.emailField.setText(email);
    // }

    // void setNumber(String number) {
    //     this.numberField.setText(number);
    // }

    // void setImage(Image img){
    //     this.image = img;
    //     this.imageview.setImage(img);
    // }

}