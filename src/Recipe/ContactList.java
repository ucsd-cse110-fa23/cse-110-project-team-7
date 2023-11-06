import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class ContactList extends VBox {

    private ArrayList<Contact> contacts;

    ContactList(ArrayList<Contact> clist) {
        contacts = clist;
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateContactList() {
        this.getChildren().clear();

        for (int i = 0; i < contacts.size(); i++) {
            Contact tmpContact = contacts.get(i);
            Button newButton = new Button(tmpContact.getName());
            newButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            newButton.setPrefSize(500, 50);
            final int location = i;
            newButton.setOnAction(e -> {
                Stage secondaryStage = new Stage();
                AppframeTwo detailFrame = new AppframeTwo(contacts, this, false, location, secondaryStage);
                Scene secondScene = new Scene(detailFrame, 500, 600);
                secondaryStage.setTitle("PantryPal Recipe");
                secondaryStage.setScene(secondScene);
                secondaryStage.setResizable(false);
                secondaryStage.show();

            });
            this.getChildren().add(newButton);
        }

    }

    // public void sortContactList() {
    //     CustomContactComparator custom = new CustomContactComparator();
    //     Collections.sort(contacts, custom);
    // }

    // private class CustomContactComparator implements Comparator<Contact> {
    
    //     public int compare(Contact c1, Contact c2) {
    //         return c1.getName().compareTo(c2.getName());
    //     }
    // }


}