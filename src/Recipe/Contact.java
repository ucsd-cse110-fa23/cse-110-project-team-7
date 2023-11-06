import javafx.scene.image.*;

class Contact {

    private String contactName;
    // private String contactPhoneNumber;
    // private String contactEmail;
    // private Image contactImage;

    // Contact(String name, String phoneNumber, String email, Image img) {
    //     this.contactName = name;
    //     this.contactPhoneNumber = phoneNumber;
    //     this.contactEmail = email;
    //     this.contactImage = img;
    // }

    Contact(String name) {
        this.contactName = name;
    }

    void setName(String newName) {
        this.contactName = newName;
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
        return this.contactName;
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
