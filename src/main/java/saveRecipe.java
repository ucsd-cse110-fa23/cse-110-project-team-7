
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/* 
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import javafx.scene.control.ListView;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
*/

public class saveRecipe {
   public static void saveToCSV(RecipeList recipeList, Recipe recipe) throws IOException {


       // Replace the placeholder with your MongoDB deployment's connection string
       // String uri = "Your uri";
       
       /* From Mini-Project code
        * try {
            FileWriter writeContact = new FileWriter("contacts.csv", false);
            writeContact.write("Contact Name, " + "Phone Number, " + "Address\n");
            for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof Contact) {
                    Contact contact = (Contact) this.getChildren().get(i);
                    //getTaskName().getText() gives the task name
                    writeContact.write(contact.getContactName().getText() + ", ");
                    writeContact.write(contact.getContactPhoneNo().getText() + ", ");
                    writeContact.write(contact.getContactAddress().getText() + "\n");
                }
            }
            writeContact.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        */
        /* From lab 1 code
         *  try {
            FileWriter writeTask = new FileWriter("tasks.txt", false);
            for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof Task) {
                    Task task = (Task) this.getChildren().get(i);
                    //getTaskName().getText() gives the task name
                    writeTask.write(task.getTaskName().getText() + "\n");
                }
            }
            writeTask.close();
        }
         */


                // FileWriter outWriter = new FileWriter("save.csv");

                // for(int i = 0 ; i < clist.size(); i++){
                 
                //     String name = clist.get(i).getName();
                //     String email = clist.get(i).getEmail();
                //     String number = clist.get(i).getPhoneNumber();
    
                //     outWriter.append(name);
                //     outWriter.append(',');
                //     outWriter.append(email);
                //     outWriter.append(',');
                //     outWriter.append(number);
                //     outWriter.append('\n');
    
                // }

                // outWriter.close();
           
        try {

            FileWriter writeRecipe = new FileWriter("recipes.csv");
            
            //BufferedReader br = new BufferedReader(new FileWriter("recipeName.csv", false));
            
            recipeList.addRecipe(recipe);
            recipeList.saveRecipe();
            
            for(Recipe rec : recipeList.getRecipeList()) {
            
                String title = rec.getTitle();
                String ingredients = rec.getIngredients();
                String instructions = rec.getInstructions();
                
                writeRecipe.append(title);
                writeRecipe.append(',');
                writeRecipe.append(ingredients);
                writeRecipe.append(',');
                writeRecipe.append(instructions);
                writeRecipe.append('\n');
            }
            // String line ="";
            // String instructions = "";
            // // Read the rest of the file
            // while ((line = br.readLine()) != null) {
            //     instructions += line + "\n"; // Concatenate the line to instructions
            // }
            
            writeRecipe.close();

            // RecipeList recipeList = new RecipeList();
            // private Button recipeButton;
            // recipeButton = new Button(title);
            // recipeButton.setStyle(defaultButtonStyle);
            // this.getChildren().addAll


        } catch (Exception e) {
            e.printStackTrace();
        }
   
   }
}     
   