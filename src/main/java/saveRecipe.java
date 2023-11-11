import static com.mongodb.client.model.Filters.eq;

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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;


public class saveRecipe {
   public void saveToDB() throws IOException {


       // Replace the placeholder with your MongoDB deployment's connection string
       String uri = "mongodb+srv://admin:3rfvBhBvb5XQHphM@lab6.go8i6tt.mongodb.net/?retryWrites=true&w=majority";
       
       try (MongoClient mongoClient = MongoClients.create(uri)) {
           MongoDatabase database = mongoClient.getDatabase("project_db");
           MongoCollection<Document> collection = database.getCollection("Recipes");
           
           // FileWriter writeContact = new FileWriter("contacts.csv", false);
           /*
            * for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof Contact) {
                    Contact contact = (Contact) this.getChildren().get(i);
                    //getTaskName().getText() gives the task name
                    writeContact.write(contact.getContactName().getText() + ", ");
                    writeContact.write(contact.getContactPhoneNo().getText() + ", ");
                    writeContact.write(contact.getContactAddress().getText() + "\n");
                }
            }
            */
           //FileWriter writeRecipe = new FileWriter("recipeName.csv", false);
           try(BufferedReader br = new BufferedReader(new FileReader("recipe.csv"))) {
            String title = br.readLine();

            String line ="";
            String instructions = "";
            // Read the rest of the file
            while ((line = br.readLine()) != null) {
                instructions += line + "\n"; // Concatenate the line to instructions
            }
            Document recipesDocument = new Document("recipeTitle", title).append("Instructions", instructions);
            collection.insertOne(recipesDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       }
       
       
   }
}