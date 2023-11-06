import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.util.ArrayList;

class ListView extends BorderPane {

    private ArrayList<Recipe> rlist;
    private HeaderOne headerone;
    private FooterOne footerone;
    private RecipeList recipeList;

    private Button addButton;
    // private Button sortButton;
    // private Button saveCSVButton;

    ListView() {
        rlist = new ArrayList<Recipe>();
        headerone = new HeaderOne();
        footerone = new FooterOne();
        recipeList = new RecipeList(rlist);

        this.setTop(headerone);
        // this.setCenter(contactList);
        this.setBottom(footerone);

        addButton = footerone.getCreateButton();
        // sortButton = footerone.getSortButton();
        // saveCSVButton = footerone.getSaveCSVButton();

        ScrollPane scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        this.setCenter(scrollPane);

        addListeners();
    }

    public void addListeners() {
        addButton.setOnAction(e -> {
            Stage secondaryStage = new Stage();
            CreateView detailFrame = new CreateView(rlist, recipeList, true, -1, secondaryStage);
            Scene secondScene = new Scene(detailFrame, 500, 600);
            secondaryStage.setTitle("Create Recipe");
            secondaryStage.setScene(secondScene);
            secondaryStage.setResizable(false);
            secondaryStage.show();

        });


        // saveCSVButton.setOnAction(e ->{
        //     try{
        //         FileWriter outWriter = new FileWriter("save.csv");

        //         for(int i = 0 ; i < clist.size(); i++){
                 
        //             String name = clist.get(i).getName();
        //             String email = clist.get(i).getEmail();
        //             String number = clist.get(i).getPhoneNumber();
    
        //             outWriter.append(name);
        //             outWriter.append(',');
        //             outWriter.append(email);
        //             outWriter.append(',');
        //             outWriter.append(number);
        //             outWriter.append('\n');
    
        //         }

        //         outWriter.close();
        //     }
        //     catch(Exception exc){
        //         System.out.println("File writing error");
        //     }
            
        // });

        // sortButton.setOnAction(e -> {
        //     contactList.sortContactList();
        //     contactList.updateContactList();
        // });
    }

}
