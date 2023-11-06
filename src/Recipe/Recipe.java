import javafx.scene.image.*;

class Recipe {

    private String recipeName;
    // private String contactPhoneNumber;
    // private String contactEmail;
    // private Image contactImage;

    // Contact(String name, String phoneNumber, String email, Image img) {
    //     this.contactName = name;
    //     this.contactPhoneNumber = phoneNumber;
    //     this.contactEmail = email;
    //     this.contactImage = img;
    // }

    Recipe(String name) {
        this.recipeName = name;
    }

    void setName(String newName) {
        this.recipeName = newName;
    }

    // void setPhoneNumber(String newPhoneNUmber) {
    //     this.contactPhoneNumber = newPhoneNUmber;
    // }

    // void setEmail(String newEmail) {
    //     this.contactEmail = newEmail;
    // }

    // void setImage(Image img){
    //     this.contactImage = img;
    // }

    String getName() {
        return this.recipeName;
    }

    // String getPhoneNumber() {
    //     return this.contactPhoneNumber;
    // }

    // String getEmail() {
    //     return this.contactEmail;
    // }

    // Image getImage(){
    //     return this.contactImage;
    // }

}
