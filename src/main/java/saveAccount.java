import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

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
        setUsername(userName);
        return true;
    }

    public boolean loginAccount(String userName, String password) {
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
    
    // Source: ChatGPT 3.5 November 23
    // I asked how to make an ArrayList as a field in a collection and found out about
    // List<Document> which allowed us to save objects in collection 
    public boolean saveRecipeForUser(String userName, String title, String ingredients, String instructions, String imageUrl, String mealType) {
        if (userName.equals("")) {
            System.out.println("Error: Empty username");
            return false;
        }

        Bson filter = eq("_id", userName);
        Document user = accountsCollection.find(filter).first();

        if (user != null) {
            List<Document> existingRecipes = (List<Document>) user.get("recipes", List.class);

            if (existingRecipes == null) {
                existingRecipes = new ArrayList<>();
                user.put("recipes", existingRecipes);
            }

            // Check if the recipe with the given title already exists
            boolean recipeExists = false;
            for (Document recipeDoc : existingRecipes) {
                if (title.equals(recipeDoc.getString("Title"))) {
                    // Update existing recipe
                    recipeDoc.put("Ingredients", ingredients);
                    recipeDoc.put("Instructions", instructions);
                    recipeDoc.put("Image", imageUrl);
                    recipeDoc.put("Meal Type", mealType);
                    recipeExists = true;
                    break;
                }
            }

            if (!recipeExists) {
                // If the recipe doesn't exist, add a new one
                Document recipeDocument = new Document("Title", title)
                        .append("Ingredients", ingredients)
                        .append("Instructions", instructions)
                        .append("Image", imageUrl)
                        .append("Meal Type", mealType);

                existingRecipes.add(recipeDocument);
            }

            accountsCollection.replaceOne(filter, user);
            return true;
        } else {
            System.out.println("User not found: " + userName);
            return false;
        }
    }


    
    // Source: ChatGPT 3.5 November 23
    // Asked how to delete an element from a List<Document> because deleteOne works
    // for deleting fields and not elements from a list 
    public boolean deleteRecipeFromDatabase(String username, String title) {
        Bson filter = eq("_id", username);
        Bson update = pull("recipes", eq("Title", title));

        UpdateResult result = accountsCollection.updateOne(filter, update);

        // Check if the update was successful
        if (result.getModifiedCount() > 0) {
            System.out.println("Recipe deleted: " + title);
            return true; 
        } else {
            System.out.println("Recipe not found: " + title);
            return false; // Recipe not found or deletion unsuccessful
        }
    }
    
    
    public String getUsername(){
        return this.username;
    }

    public void setUsername(String user){
        this.username = user;
    }
    
    // Source: ChatGPT 3.5 November 23
    // I asked how to read from database and how to read from the list of documents
    // specifically 
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

    // ChatGPT 3.5 December 3 2023 
    // Asked how to find a specific recipe from a List of documents 
    public Recipe findRecipe(String username, String title) {
        Recipe result = null;

        try {
            Bson filter = and(eq("_id", username), eq("recipes.Title", title));

            Document user = accountsCollection.find(filter).first();
            
            if (user != null) {
                List<Document> recipes = user.getList("recipes", Document.class);

                if (recipes != null) {
                    for (Document recipeDoc : recipes) {
                        if (title.equals(recipeDoc.getString("Title"))) {
                            String ingredients = recipeDoc.getString("Ingredients");
                            String instructions = recipeDoc.getString("Instructions");
                            String image = recipeDoc.getString("Image");
                            String mealType = recipeDoc.getString("Meal Type");

                            result = new Recipe(title, ingredients, instructions, image, mealType);
                            return result; 
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    

}