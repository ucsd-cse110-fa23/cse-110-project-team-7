import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Updates.pull;

import static com.mongodb.client.model.Filters.*;
import java.util.*;

public class saveAccount {

    String uri = "mongodb+srv://admin:cXgKxmLpdvsylEUR@cluster0.zth582l.mongodb.net/?retryWrites=true&w=majority";
    MongoClient mongoClient;
    MongoDatabase accountDb;
    MongoCollection<Document> accountsCollection;
    String username;

    public saveAccount() {
        try {
            mongoClient = MongoClients.create(uri);
            accountDb = mongoClient.getDatabase("account_db");
            accountsCollection = accountDb.getCollection("accounts");
            this.username = "";
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public saveAccount(MongoDatabase accountDb, MongoCollection<Document> accountsCollection) {
        this.accountDb = accountDb;
        this.accountsCollection = accountsCollection;
    }

    public boolean generateNewAccount(String userName, String password) {
        if (accountExist(userName) || userName.equals("")) {
            System.out.println("Enter a valid username");
            return false;
        }


        Document newUser = new Document("_id", userName)
                .append("password", password)
                .append("recipes", new ArrayList<>());;

        accountsCollection.insertOne(newUser);
        return true;
    }

    public boolean loginAccount(String userName, String password) {
        System.out.println("Entering loginAccount method");
        System.out.println("UserName: " + userName);
        if (!accountExist(userName)) {
            System.out.println("Account doesn't exist");
            
            return false;
        }
    
        Bson filter = and(eq("_id", userName), eq("password", password));
        Document result = accountsCollection.find(filter).first();
    
        if (result != null) {
            return true; // Successfully logged in
        } else {
            System.out.println("Incorrect username or password");
            return false; // Incorrect username or password
        }
    }

    // Check if the account exists
    public boolean accountExist(String userName) {
        Bson filter = eq("_id", userName);
        
        return accountsCollection.find(filter, Document.class).first() != null;
    }

    

    public void saveRecipesForUser(String userName, ArrayList<Recipe> recipeList) {
        if (userName.equals("")) {
            System.out.println("Error: Empty username");
            return;
        }
    
        Bson filter = eq("_id", userName);
        Document user = accountsCollection.find(filter).first();
    
        if (user != null) {
            List<Document> existingRecipes = (List<Document>) user.get("recipes", List.class);
    
            if (existingRecipes == null) {
                existingRecipes = new ArrayList<>();
                user.put("recipes", existingRecipes);
            } else {
                existingRecipes.clear();
            }
    
            for (Recipe recipe : recipeList) {
                Document recipeDocument = new Document("Title", recipe.getTitle())
                        .append("Ingredients", recipe.getIngredients())
                        .append("Instructions", recipe.getInstructions())
                        .append("Image", recipe.getImageUrl())
                        .append("Meal Type", recipe.getMealType());
    
                existingRecipes.add(recipeDocument);
            }
    
            accountsCollection.replaceOne(filter, user);
        } else {
            System.out.println("User not found: " + userName);
        }
    }
    

    public void deleteRecipeFromDatabase(String username, Recipe recipe) {
        Bson filter = eq("_id", username);
        Bson update = pull("recipes", new Document("Title", recipe.getTitle())
                                            .append("Ingredients", recipe.getIngredients())
                                            .append("Instructions", recipe.getInstructions())
                                            .append("Image", recipe.getImageUrl())
                                            .append("Meal Type", recipe.getMealType()));
    
        accountsCollection.updateOne(filter, update);
    }
    
    

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String user){
        this.username = user;
    }

    public ArrayList<Recipe> readDatabase(String username) {
        ArrayList<Recipe> result = new ArrayList<>();
    
        try {
            Bson filter = eq("_id", username);
    
            Document user = accountsCollection.find(filter).first();
    
            if (user != null) {
                List<Document> recipes = user.getList("recipes", Document.class);
    
                if (recipes != null) {
                    for (Document recipeDoc : recipes) {
                        String title = recipeDoc.getString("Title");
                        String ingredients = recipeDoc.getString("Ingredients");
                        String instructions = recipeDoc.getString("Instructions");
                        String image = recipeDoc.getString("Image");
                        String mealType = recipeDoc.getString("Meal Type");
    
                        Recipe recipe = new Recipe(title, ingredients, instructions, image, mealType);
                        result.add(recipe);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return result;
    }
    
    

}