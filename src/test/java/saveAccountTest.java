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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

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

    @Captor
    private ArgumentCaptor<Bson> filterCaptor;

    @InjectMocks
    private saveAccount saveAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the behavior of the MongoDB objects
        when(mockDatabase.getCollection(anyString(), eq(Document.class))).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), eq(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
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
        when(mockFindIterable.first()).thenReturn(new Document()); // Mock with a non-null Document

        assertTrue(saveAccount.accountExist(userName)); // Ensure the account exists
    }

    @Test
    public void testGenerateExistingAccount() {
        String userName = "existingUser";
        String password = "existingPassword";

        // Mock the necessary MongoDB operations
        when(mockCollection.insertOne(any())).thenReturn(null);

        // Mock the behavior of the find method to return a non-null FindIterable
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);

        // Mock the behavior of the first() method on the FindIterable
        when(mockFindIterable.first()).thenReturn(new Document("_id", userName).append("password", password));

        saveAccount.generateNewAccount(userName, password);
        boolean result = saveAccount.generateNewAccount(userName, "newPassword");
        assertFalse(result);
    }
    @Test
    public void testLoginAccount() {
        String userName = "testLoginUser";
        String password = "testLoginPassword";

        // Mock the cursor to return a document with the stored password
        Document userDocument = new Document("_id", userName).append("password", password);
        when(mockCursor.hasNext()).thenReturn(true);
        when(mockCursor.next()).thenReturn(userDocument);

        // Mock the Filters class correctly using any() for Bson arguments
        when(mockCollection.find(any(Bson.class), eq(Document.class))).thenReturn(mockFindIterable);

        int result = saveAccount.loginAccount(userName, password);

        // Ensure the login is successful
        assertEquals(1, result);

        // Verify that the correct filter was used
        verify(mockCollection).find(filterCaptor.capture(), eq(Document.class));
        Bson capturedFilter = filterCaptor.getValue();
        assertNotNull(capturedFilter);

        // Additional assertion to check the expected filter
        Bson expectedFilter = Filters.and(Filters.eq("_id", userName), Filters.eq("password", password));
        assertEquals(expectedFilter.toString(), capturedFilter.toString());
    }


    
    @Test
    public void testLoginIncorrectPassword() {
        String userName = "testLoginUser";
        String storedPassword = "storedPassword";
        String enteredPassword = "incorrectPassword";
    
        // Mock the necessary MongoDB operations
        when(mockCollection.find(filterCaptor.capture(), eq(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true);
    
        // Mock the cursor to return a document with the stored password
        Document userDocument = new Document("_id", userName).append("password", storedPassword);
        when(mockCursor.next()).thenReturn(userDocument);
    
        int result = saveAccount.loginAccount(userName, enteredPassword);
    
        // Ensure the login fails due to incorrect password
        assertEquals(0, result);
    
        // Verify that the correct filter was used
        Bson capturedFilter = filterCaptor.getValue();
        assertNotNull(capturedFilter);
        assertTrue(((Document) capturedFilter).containsKey("_id"));
        assertTrue(((Document) capturedFilter).containsKey("password"));
    
        // Verify that the cursor's next method was called
        verify(mockCursor).next();
    }
    



    @Test
    public void testLoginNoAccount() {
        String userName = "nonExistingUser";
        String password = "nonExistingPassword";

        // Mock the necessary MongoDB operations
        when(mockCollection.find(filterCaptor.capture(), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(false);

        int result = saveAccount.loginAccount(userName, password);

        assertEquals(-1, result);
    }

}
