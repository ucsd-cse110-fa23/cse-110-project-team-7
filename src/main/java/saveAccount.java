import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

public class saveAccount {

    String uri = "mongodb://ajkristanto:1g9rhJSw6pKCwJwj@ac-m9szzsy-shard-00-00.h0guhqq.mongodb.net:27017,ac-m9szzsy-shard-00-01.h0guhqq.mongodb.net:27017,ac-m9szzsy-shard-00-02.h0guhqq.mongodb.net:27017/?ssl=true&replicaSet=atlas-103fsu-shard-0&authSource=admin&retryWrites=true&w=majority";
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

    public saveAccount(MongoDatabase accountDb, MongoCollection<Document> accountsCollection) {
        this.accountDb = accountDb;
        this.accountsCollection = accountsCollection;
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

    public int loginAccount(String userName, String password) {
        if (!accountExist(userName)) {
            System.out.println("Account doesn't exist");
            return -1;
        }
    
        Bson filter = and(eq("_id", userName), eq("password", password));
        Document result = accountsCollection.find(filter).first();
    
        if (result != null) {
            return 1; // Successfully logged in
        } else {
            System.out.println("Incorrect username or password");
            return 0; // Incorrect username or password
        }
    }
    
    


    // Check if the account exists
    public boolean accountExist(String userName) {
        Bson filter = eq("_id", userName);
        Document result = accountsCollection.find(filter, Document.class).first();
        return result != null;
    }

}