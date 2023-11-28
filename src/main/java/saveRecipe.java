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
}