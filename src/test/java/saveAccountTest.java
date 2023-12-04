import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.pull;

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
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
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

    @Test
    public void saveRecipesForUserTest() {
        String userName = "testUser";
        ArrayList<Recipe> recipeList = new ArrayList<>(Arrays.asList(
                new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1"),
                new Recipe("Recipe2", "Ingredients2", "Instructions2", "Image2", "Meal Type 2")
        ));

        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(Mockito.mock(Document.class));

        saveAccount.saveRecipeForUser(userName, "Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1");
        saveAccount.saveRecipeForUser(userName, "Recipe2", "Ingredients2", "Instructions2", "Image2", "Meal Type 2");

        verify(mockCollection, times(2)).find(any(Bson.class));
        verify(mockCollection, times(2)).replaceOne(any(Bson.class), any());
    }

    @Test
    public void saveAfterEditRecipesForUserTest() {
        String userName = "testUser";
        ArrayList<Recipe> recipeList = new ArrayList<>(Arrays.asList(
                new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1"),
                new Recipe("Recipe2", "Ingredients2", "Instructions2", "Image2", "Meal Type 2")
        ));

        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(Mockito.mock(Document.class));

        saveAccount.saveRecipeForUser(userName, "Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1");
        saveAccount.saveRecipeForUser(userName, "Recipe2", "Ingredients2", "Instructions2", "Image2", "Meal Type 2");

        verify(mockCollection, times(2)).find(any(Bson.class));
        verify(mockCollection, times(2)).replaceOne(any(Bson.class), any());

        saveAccount.saveRecipeForUser(userName, "Recipe2", "Ingredients2", "Instructions3", "Image2", "Meal Type 2");

        verify(mockCollection, times(3)).find(any(Bson.class));
        verify(mockCollection, times(3)).replaceOne(any(Bson.class), any());

    }


    @Test
    public void deleteRecipeFromDatabaseTest() {
        String username = "testUser";
        // Recipe recipeToDelete = new Recipe("Test Recipe", "Test", "Test", "Test", "Test");

        when(mockCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(null);
        saveAccount.deleteRecipeFromDatabase(username, "Test Recipe");

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<Bson> updateCaptor = ArgumentCaptor.forClass(Bson.class);

        verify(mockCollection, times(1)).updateOne(filterCaptor.capture(), updateCaptor.capture());

        Bson actualFilter = filterCaptor.getValue();
        Bson actualUpdate = updateCaptor.getValue();

        assertEquals(eq("_id", username), actualFilter); 
        assertEquals(pull("recipes", new Document("Title", "Test Recipe")), actualUpdate);
    }


    @Test 
    public void readDatabaseTest(){
         // Arrange
        String username = "testUser";
        List<Document> mockRecipes = Arrays.asList(
                 new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions", "Instructions1"),
                 new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions", "Instructions2")
        );
        Document mockUser = new Document("_id", username).append("recipes", mockRecipes);
 
        when(mockCollection.find(eq("_id", username))).thenReturn(Mockito.mock(FindIterable.class));
        when(mockCollection.find(eq("_id", username)).first()).thenReturn(mockUser);
 
        // Act
        ArrayList<Recipe> result = saveAccount.readDatabase(username);
 
        // Assert
        assertEquals(2, result.size());
 
        Recipe recipe1 = result.get(0);
        assertEquals("Recipe1", recipe1.getTitle());
        assertEquals("Ingredients1", recipe1.getIngredients());
        assertEquals("Instructions1", recipe1.getInstructions());
 
        Recipe recipe2 = result.get(1);
        assertEquals("Recipe2", recipe2.getTitle());
        assertEquals("Ingredients2", recipe2.getIngredients());
        assertEquals("Instructions2", recipe2.getInstructions());
     

    }


}
