import java.util.ArrayList;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

public class DeleteRecipe{

    public static ArrayList<Recipe> deleteTargetRecipe(ArrayList<Recipe> recipeList, Recipe recipe){
        recipeList.remove(recipe);
        //recipeList.saveRecipe();
        // Update CSV file too somehow to remove list
        try{
            saveRecipe.saveToCSV(recipeList);
            saveRecipe.saveListToDatabase(recipeList);
        }catch(Exception e){
            System.err.println("Delete and Save failed");
            e.printStackTrace();
        }
        return recipeList;
    }
    public static void deleteFromDatabase(Recipe recipe){
        String uri = "mongodb+srv://admin:cXgKxmLpdvsylEUR@cluster0.zth582l.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase recipeDB = mongoClient.getDatabase("recipes_db");
                    MongoCollection<Document> recipesCollection = recipeDB.getCollection("recipes");

                    // Define the criteria to identify the document to delete
                    Document recipeDocument = new Document("Title", recipe.getTitle())
                        .append("Ingredients", recipe.getIngredients())
                        .append("Instructions", recipe.getInstructions());
                    // Delete a single document that matches the criteria
                    recipesCollection.deleteOne(recipeDocument);

                    System.out.println("Document deleted successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
}