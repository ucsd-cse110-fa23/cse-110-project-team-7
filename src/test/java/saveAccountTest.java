import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

public class saveAccountTest {

    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @Mock
    private FindIterable<Document> mockFindIterable;

    @Mock
    private MongoCursor<Document> mockCursor;

    @InjectMocks
    private saveAccount saveAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);

        saveAccount = new saveAccount(mockDatabase, mockCollection);
    }

    @Test
    public void testGenerateNewAccount() {
        String userName = "testUsername";
        String password = "testPassword";

        when(mockCollection.insertOne(any())).thenReturn(null);

        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);

        boolean result = saveAccount.generateNewAccount(userName, password);

        assertTrue(result);

        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any()))
                .thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(new Document());

        assertTrue(saveAccount.accountExist(userName)); 
    }

    @Test
    public void testGenerateExistingAccount() {
        String userName = "existingUser";
        String password = "existingPassword";

        when(mockCollection.insertOne(any())).thenReturn(null);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(new Document("_id", userName).append("password", password));

        saveAccount.generateNewAccount(userName, password);
        boolean result = saveAccount.generateNewAccount(userName, "newPassword");
        assertFalse(result);
    }

    @Test
    public void testLoginAccount() {
        String userName = "testLoginUser";
        String password = "testLoginPassword";
      

        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        when(mockFindIterable.first()).thenReturn(new Document("_id", userName).append("password", password));
        boolean r = saveAccount.accountExist(userName);

        assertTrue(r);
        
        boolean result = saveAccount.loginAccount(userName, password);

        // Assert
        assertTrue(result);
        
    }


    @Test
    public void testLoginIncorrectPassword() {
        String userName = "testLoginUser";
        String storedPassword = "storedPassword";
        String enteredPassword = "incorrectPassword";

        when(mockCursor.hasNext()).thenReturn(true);
        when(mockCursor.next()).thenReturn(new Document("_id", userName).append("password", storedPassword));

        boolean result = saveAccount.loginAccount(userName, enteredPassword);

        assertFalse(result); 

    }




    @Test
    public void testLoginNoAccount() {
        String userName = "nonExistingUser";
        String password = "nonExistingPassword";

        // Simulate not finding any documents
        when(mockCursor.hasNext()).thenReturn(false);

        boolean result = saveAccount.loginAccount(userName, password);

        assertFalse(result);
    }

}
