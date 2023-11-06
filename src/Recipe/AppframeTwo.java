import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.util.ArrayList;

import javafx.scene.image.*;

class AppframeTwo extends BorderPane {
    
    ArrayList<Contact> contacts;
    ContactList workingList;
    // private HeaderTwo headertwo;
    // private FooterTwo footertwo;
    // private ContactDetail contactdetail;
    // private Button updateButton;
    // private Button deleteButton;
    // private Button uploadImageButton;
    // private boolean create_new;
    // private int calLocation;
    // private Stage secondaryStage;

    AppframeTwo(ArrayList<Contact> clist, ContactList list, boolean create_new, int location, Stage currStage) {

        // this.workingList = list;
        // this.create_new = create_new;
        // this.contacts = clist;
        // this.calLocation = location;
        // this.secondaryStage = currStage;
        // headertwo = new HeaderTwo();
        // footertwo = new FooterTwo();
        // contactdetail = new ContactDetail();

        // if (!create_new) {
        //     contactdetail.setName(contacts.get(calLocation).getName());
        //     contactdetail.setEmail(contacts.get(calLocation).getEmail());
        //     contactdetail.setNumber(contacts.get(calLocation).getPhoneNumber());
        //     contactdetail.setImage(contacts.get(calLocation).getImage());
        // }

        // this.setTop(headertwo);
        // this.setCenter(contactdetail);
        // this.setBottom(footertwo);

        // updateButton = footertwo.getUpdateButton();
        // deleteButton = footertwo.getDeleteButton();
        // uploadImageButton = footertwo.getUploadImgButton();

        // updateListeners();
    }

    // public void updateListeners() {
    //     updateButton.setOnAction(e -> {

    //         if (create_new) {
    //             String name = contactdetail.getName().getText();
    //             String phoneNumber = contactdetail.getPhoneNumber().getText();
    //             String email = contactdetail.getEmail().getText();
    //             Image img = contactdetail.getImage();
    //             Contact newContact = new Contact(name, phoneNumber, email, img);
    //             contacts.add(newContact);
    //             workingList.updateContactList();
                

    //         } else {
    //             String name = contactdetail.getName().getText();
    //             String phoneNumber = contactdetail.getPhoneNumber().getText();
    //             String email = contactdetail.getEmail().getText();
    //             Image prevImage = contacts.get(calLocation).getImage();
    //             Contact newContact = new Contact(name, phoneNumber, email, prevImage);
    //             contacts.set(calLocation, newContact);
    //         }
    //         workingList.updateContactList();
    //         secondaryStage.close();
    //     });

    //     deleteButton.setOnAction(e -> {
    //         contacts.remove(calLocation);
    //         workingList.updateContactList();
    //         secondaryStage.close();
    //     });

    //     uploadImageButton.setOnAction(e->{
    //         FileChooser chooseFile = new FileChooser();
    //         chooseFile.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    //         File chosenFile = chooseFile.showOpenDialog(secondaryStage);
    //         if(chosenFile != null){
    //             Image newImg = new Image(chosenFile.toURI().toString(),300,300,false,false);
    //             contactdetail.setImage(newImg);
    //             if(!create_new){
    //                 contacts.get(calLocation).setImage(newImg);
    //             }
    //         }
            
    //     });
        

    // }
    
}
