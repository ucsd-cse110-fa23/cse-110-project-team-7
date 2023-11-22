import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

public class saveAccount {

    String uri = "mongodb+srv://admin:cXgKxmLpdvsylEUR@cluster0.zth582l.mongodb.net/?retryWrites=true&w=majority";
    MongoClient mongoClient;
    MongoDatabase accountDb;
    MongoCollection<Document> accountsCollection;

    public saveAccount() {
        try {
            mongoClient = MongoClients.create(uri);
            accountDb = mongoClient.getDatabase("account_db");
            accountsCollection = accountDb.getCollection("accounts");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public boolean generateNewAccount(String userName, String password) {
        if (accountExist(userName)) {
            System.out.println("Account already exists!");
            return false;
        }

        Document newUser = new Document("_id", userName)
                .append("password", password);

        accountsCollection.insertOne(newUser);
        return true;
    }

    public boolean loginAccount(String userName, String password){

        if(!accountExist(userName)){
            System.out.println("Account doesn't exist");
            return false;
        }
        Bson filter = Filters.and(eq("_id", userName), eq("password", password));
        return accountsCollection.find(filter).first() != null;
        
    }

    // Check if the account exists
    public boolean accountExist(String userName) {
        Bson filter = eq("_id", userName);
        return accountsCollection.find(filter).first() != null; 
    }
}
