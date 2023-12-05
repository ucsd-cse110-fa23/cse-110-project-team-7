import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class URLHandlerTest {

    
    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @Mock
    private FindIterable<Document> mockFindIterable;

    @Mock
    private MongoCursor<Document> mockCursor;

    @Mock
    private saveAccount saveAccount;

    @InjectMocks
    private URLHandler urlHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);
        urlHandler = new URLHandler(new HashMap<>(), saveAccount);
   }

    // Source: ChatGPT 3.5 December 3 2023 11:00 PM. 
    // Asked how to debug my code and organize it correctly
    // And asked how to get the response from HTML (ByteArrayOutputStream)
   @Test
   void handleGetValidRequest() throws IOException {
       String username = "testUser";
       String recipeTitle = "Recipe1";

       when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

       List<Document> mockRecipes = Arrays.asList(
               new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions", "Instructions1"),
               new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions", "Instructions2")
       );
       when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("recipes", mockRecipes));

       URI uri = URI.create("/recipe?=" + username + "_" + recipeTitle);
       HttpExchange httpExchange = mock(HttpExchange.class);
       when(httpExchange.getRequestMethod()).thenReturn("GET");
       when(httpExchange.getRequestURI()).thenReturn(uri);

       Recipe mockRecipe = new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1");
       when(saveAccount.findRecipe(username, recipeTitle)).thenReturn(mockRecipe);

       ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
       when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

       urlHandler.handle(httpExchange);

       String expectedResponse = "<html><body><h1>Recipe1</h1><img src=\"Image1\"> <br>" +
               "<p>Inputted Ingredients: Ingredients1</p> <br>" +
               "<p>Instructions1</p></body></html>";

       assertEquals(expectedResponse, responseOutputStream.toString());
   }


    @Test
    void handleGetFail() throws IOException {
        // Arrange
        URI uri = URI.create("/recipe?=testUser_NonexistentRecipe");
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        when(saveAccount.findRecipe("testUser", "NonexistentRecipe")).thenReturn(null);

        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

    
        urlHandler.handle(httpExchange);

        String expectedResponse = "<html><body><h1>No Recipes Found</h1></body></html>";

        assertEquals(expectedResponse, responseOutputStream.toString());

    }
}
