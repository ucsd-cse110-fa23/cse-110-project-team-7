package CreateRecipe;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.*;

import org.json.JSONException;

class mealType extends HBox{
    private TextField type;
    private Label prompt;

    mealType() throws Exception{
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        prompt = new Label();
        prompt.setText("meal type: "); // create index label
        prompt.setPrefSize(60, 20); // set size of Index label
        prompt.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(prompt); // add index label to task

        type = new TextField(); // create recipe name text field
        type.setPrefSize(380, 20); // set size of text field
        type.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        type.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(type); // add textlabel to recipe
        
    }

    public TextField getTypeField()  {
        return this.type;
    }
}

class Ingredient extends HBox{
    private TextField list;
    private Label prompt;
    Ingredient(){
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        prompt = new Label();
        prompt.setText("ingredient list: "); // create index label
        prompt.setPrefSize(80, 20); // set size of Index label
        prompt.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        prompt.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(prompt); // add index label to task

        list = new TextField(); // create recipe name text field
        list.setPrefSize(380, 20); // set size of text field
        list.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        list.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(list); // add textlabel to recipe
    }
    public TextField getTypeField()  {
        return this.list;
    }
}


class AppFrame extends FlowPane {
    private Ingredient ingredient;
    private mealType mType;
    private String currentTarget;
    private boolean mealSelected= true; 
    
    private Button startButton;
    private Button stopButton;
    private Button backButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    AppFrame() throws Exception {
        ingredient = new Ingredient();
        mType = new mealType();
        
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        this.getChildren().addAll(mType, ingredient);
        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        backButton = new Button("Back");
        backButton.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel, backButton);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
       
        // Start Button
        startButton.setOnAction(e -> {
            if(mealSelected){
                currentTarget = "meal type";
                mealSelected = false;
            }
            else{
                currentTarget = "ingredient list";
            }
            startRecording();
            
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
            try {
                Whisper whisper = new Whisper();
                String result = whisper.display();
                if(currentTarget.equals("meal type")){
                    mType.getTypeField().setText(result);
                    currentTarget = "";
                }
                if(currentTarget.equals("ingredient list")){
                    ingredient.getTypeField().setText(result);
                    currentTarget = "";
                }
                
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            

        });

        // Back Button later
        //backButton.setOnAction();
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        Thread t = new Thread(
            new Runnable(){
                @Override 
                public void run(){
                    try {
                        // the format of the TargetDataLine
                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                audioFormat);
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();
                        recordingLabel.setVisible(true);

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("recording.wav");
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                        recordingLabel.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }       
                }
            }
        );
        t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}

public class AudioRecorder extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Voice Input");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 400, 200));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
