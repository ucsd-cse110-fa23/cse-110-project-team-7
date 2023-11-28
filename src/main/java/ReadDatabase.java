import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class ReadDatabase {

    String uri = "mongodb://ajkristanto:1g9rhJSw6pKCwJwj@ac-m9szzsy-shard-00-00.h0guhqq.mongodb.net:27017,ac-m9szzsy-shard-00-01.h0guhqq.mongodb.net:27017,ac-m9szzsy-shard-00-02.h0guhqq.mongodb.net:27017/?ssl=true&replicaSet=atlas-103fsu-shard-0&authSource=admin&retryWrites=true&w=majority";
    MongoClient mongoClient;
    MongoDatabase accountDb;
    MongoCollection<Document> accountsCollection;

    public ReadDatabase() {
        try {
            mongoClient = MongoClients.create(uri);
            accountDb = mongoClient.getDatabase("account_db");
            accountsCollection = accountDb.getCollection("accounts");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public ReadDatabase(MongoDatabase accountDb, MongoCollection<Document> accountsCollection) {
        this.accountDb = accountDb;
        this.accountsCollection = accountsCollection;
    }

    public ArrayList<Recipe> readDatabase(String username) {
        ArrayList<Recipe> result = new ArrayList<>();

        try {
            Bson filter = eq("username", username);

            MongoCursor<Document> cursor = accountsCollection.find(filter).iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();
                String title = document.getString("title");
                String ingredients = document.getString("ingredients");
                String instructions = document.getString("instructions");

                Recipe recipe = new Recipe(title, ingredients, instructions);
                result.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
