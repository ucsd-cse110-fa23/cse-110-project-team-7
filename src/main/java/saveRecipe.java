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
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class saveRecipe {
   public static ArrayList<Recipe> saveARecipe(ArrayList<Recipe> recipeList, Recipe recipe) throws IOException {
        if(!recipeList.contains(recipe)){
            recipeList.add(0, recipe);
        }
        return recipeList;
        
   
   }

   public static void saveToCSV(ArrayList<Recipe> recipeList) throws IOException {


       
        try {

            FileWriter writeRecipe = new FileWriter("recipes.csv");
            
            //BufferedReader br = new BufferedReader(new FileWriter("recipeName.csv", false));
            
            
            for(Recipe rec : recipeList) {
            
                String title = rec.getTitle();
                String ingredients = rec.getIngredients();
                String instructions = rec.getInstructions();
                
                writeRecipe.append("Title: " + title);
                writeRecipe.append(',');
                writeRecipe.append("Ingredients: " + ingredients);
                writeRecipe.append(',');
                writeRecipe.append("Instructions: " + instructions);
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

    public static void saveListToDatabase(ArrayList<Recipe> recipeList) {
        String uri = "mongodb+srv://admin:cXgKxmLpdvsylEUR@cluster0.zth582l.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase recipeDB = mongoClient.getDatabase("recipes_db");
            MongoCollection<Document> recipesCollection = recipeDB.getCollection("recipes");

            for (Recipe recipe : recipeList) {
                Document recipeDocument = new Document("Title", recipe.getTitle())
                        .append("Ingredients", recipe.getIngredients())
                        .append("Instructions", recipe.getInstructions());

                recipesCollection.insertOne(recipeDocument);
            }
        }
    }
}