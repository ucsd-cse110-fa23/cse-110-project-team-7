import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import java.io.*;

public class DetailView extends FlowPane{
    TextArea recipeDetail = new TextArea();

    DetailView(Stage currStage, String response) throws Exception{
        recipeDetail.setPrefWidth(600);
        // get response from chatGPT and put into textArea.
        recipeDetail.setText(response);
        this.getChildren().add(recipeDetail);
    }

}