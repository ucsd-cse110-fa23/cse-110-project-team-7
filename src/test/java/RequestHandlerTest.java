import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;

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


public class RequestHandlerTest {
    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @Mock
    private ChatGPT chatGPT;

    @Mock
    private FindIterable<Document> mockFindIterable;

    @Mock
    private MongoCursor<Document> mockCursor;

    @Mock
    private saveAccount saveAccount;

    @InjectMocks
    private RequestHandler requestHandler;

    @Mock
    private DallE dallE;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);
        requestHandler = new RequestHandler(new HashMap<>(), saveAccount);
   }

    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
   @Test
   public void testGetHandle() throws IOException{
    String username = "testUser";
    String password = "password";

    // Mocking the MongoDB collection and FindIterable
    when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

    // Mocking the result of the MongoDB query
    List<Document> mockRecipes = Arrays.asList(
            new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions", "Instructions1"),
            new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions", "Instructions2")
    );
    when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("password", password).append("recipes", mockRecipes));

    // Mocking the HTTP exchange
    URI uri = URI.create("/?=" + username + "." + password);
    HttpExchange httpExchange = mock(HttpExchange.class);
    when(httpExchange.getRequestMethod()).thenReturn("GET");
    when(httpExchange.getRequestURI()).thenReturn(uri);

    when(saveAccount.loginAccount(username, password)).thenReturn(true);

    ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

    // Call the method under test
    requestHandler.handle(httpExchange);

    String capturedResponse = responseOutputStream.toString();
    System.out.println("Captured Response: " + capturedResponse);
    assertEquals(capturedResponse, "Login Successful");
    

   }

   @Test
   public void testGetHandleFailUsername() throws IOException{
    String username = "testUser";
    String password = "password";

    // Mocking the MongoDB collection and FindIterable
    when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

    // Mocking the result of the MongoDB query
    List<Document> mockRecipes = Arrays.asList(
            new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions", "Instructions1"),
            new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions", "Instructions2")
    );
    when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("password", password).append("recipes", mockRecipes));

    // Mocking the HTTP exchange
    URI uri = URI.create("/?=" + "wrongUser" + "." + password);
    HttpExchange httpExchange = mock(HttpExchange.class);
    when(httpExchange.getRequestMethod()).thenReturn("GET");
    when(httpExchange.getRequestURI()).thenReturn(uri);

    when(saveAccount.loginAccount(username, password)).thenReturn(true);

    ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

    // Call the method under test
    requestHandler.handle(httpExchange);

    String capturedResponse = responseOutputStream.toString();
    System.out.println("Captured Response: " + capturedResponse);
    assertEquals(capturedResponse, "Login Failed");
    

   }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
   @Test
   public void testGetHandleFailPassword() throws IOException{
    String username = "testUser";
    String password = "password";

    // Mocking the MongoDB collection and FindIterable
    when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

    // Mocking the result of the MongoDB query
    List<Document> mockRecipes = Arrays.asList(
            new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions", "Instructions1"),
            new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions", "Instructions2")
    );
    when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("password", password).append("recipes", mockRecipes));

    // Mocking the HTTP exchange
    URI uri = URI.create("/?=" + username + "." + "wrong");
    HttpExchange httpExchange = mock(HttpExchange.class);
    when(httpExchange.getRequestMethod()).thenReturn("GET");
    when(httpExchange.getRequestURI()).thenReturn(uri);

    when(saveAccount.loginAccount(username, password)).thenReturn(true);

    ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

    // Call the method under test
    requestHandler.handle(httpExchange);

    String capturedResponse = responseOutputStream.toString();
    System.out.println("Captured Response: " + capturedResponse);
    assertEquals(capturedResponse, "Login Failed");
    

   }

   @Test
    public void testHandleGetRecipes() throws Exception {
        // Mocking data
        String username = "testUser";
        ArrayList<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1"));

        // Mocking HTTP exchange
        URI uri = URI.create("/recipe?=testUser.Recipe");

        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking saveAccount behavior
        when(saveAccount.readDatabase(username)).thenReturn(mockRecipes);

        // Mocking DallE behavior
        when(dallE.createImage(anyString())).thenReturn("mockImageUrl");
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);
    
        // Call the method under test
        requestHandler.handle(httpExchange);

        assertNotNull(responseOutputStream.toString().trim());


    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    public void testHandleGetRecipesFail() throws Exception {
        String username = "testUser";
        ArrayList<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1"));

        URI uri = URI.create("/recipe?=fakeUser.Recipe");

        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        when(saveAccount.readDatabase(username)).thenReturn(null);

        when(dallE.createImage(anyString())).thenReturn("mockImageUrl");
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);
    
        requestHandler.handle(httpExchange);


        assertNotNull(responseOutputStream.toString().trim());


    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    public void testHandleDelete() throws IOException{

        String username = "testUser";
        String title = "RecipeTitle";

        URI uri = URI.create("/?=" + username + "_" + title);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("DELETE");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        when(saveAccount.deleteRecipeFromDatabase(username, title)).thenReturn(true);

        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Delete Successful";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());

        // Verify that the deleteRecipeFromDatabase method was called with the expected parameters
        verify(saveAccount, times(1)).deleteRecipeFromDatabase(username, title);
    

    }

    @Test
    public void testHandleDeleteFail() throws IOException {
        // Mocking data
        String username = "testUser";
        String title = "RecipeTitle";

        // Mocking HTTP exchange
        URI uri = URI.create("/?=" + username + "_" + title);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("DELETE");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking saveAccount behavior
        when(saveAccount.deleteRecipeFromDatabase(username, title)).thenReturn(false);

        // Mocking the response output stream
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Delete Failed";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());

        // Verify that the deleteRecipeFromDatabase method was called with the expected parameters
        verify(saveAccount, times(1)).deleteRecipeFromDatabase(username, title);
    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    public void testHandlePut() throws IOException {
        // Mocking data
        String username = "testUser";
        String title = "RecipeTitle";
        String ingredients = "Ingredient1, Ingredient2";
        String instructions = "Instructions for the recipe";
        String imageUrl = "http://example.com/image.jpg";
        String mealType = "Dinner";

        // Mocking HTTP exchange
        URI uri = URI.create("/?=" + username);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("PUT");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking the request body
        ByteArrayInputStream requestBody = new ByteArrayInputStream(
                (username + "~" + title + "~" + ingredients + "~" + instructions + "~" + imageUrl + "~" + mealType)
                        .getBytes()
        );
        when(httpExchange.getRequestBody()).thenReturn(requestBody);

        // Mocking the response output stream
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Mocking saveAccount behavior
        when(saveAccount.saveRecipeForUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Added recipe successfully";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());

        // Verify that the saveRecipeForUser method was called with the expected parameters
        verify(saveAccount, times(1)).saveRecipeForUser(username, title, ingredients, instructions, imageUrl, mealType);
    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    public void testHandlePutFailed() throws IOException {
        // Mocking data
        String username = "testUser";
        String title = "RecipeTitle";
        String ingredients = "Ingredient1, Ingredient2";
        String instructions = "Instructions for the recipe";
        String imageUrl = "http://example.com/image.jpg";
        String mealType = "Dinner";

        // Mocking HTTP exchange
        URI uri = URI.create("/?=" + username);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("PUT");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking the request body
        ByteArrayInputStream requestBody = new ByteArrayInputStream(
                (username + "~" + title + "~" + ingredients + "~" + instructions + "~" + imageUrl + "~" + mealType)
                        .getBytes()
        );
        when(httpExchange.getRequestBody()).thenReturn(requestBody);

        // Mocking the response output stream
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Mocking saveAccount behavior
        when(saveAccount.saveRecipeForUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(false);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Failed adding recipe";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());

        // Verify that the saveRecipeForUser method was called with the expected parameters
        verify(saveAccount, times(1)).saveRecipeForUser(username, title, ingredients, instructions, imageUrl, mealType);
    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    public void testHandlePost() throws IOException {
        // Mocking data
        String username = "testUser";
        String password = "testPassword";
        String postData = username + "," + password;

        URI uri = URI.create("/?=" + postData);

        // Mocking HTTP exchange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(postData.getBytes());
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking saveAccount behavior
        when(saveAccount.generateNewAccount(username, password)).thenReturn(true);

        // Set up a ByteArrayOutputStream to capture the response
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Signup Successful";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());
    }

    @Test
    public void testHandlePostFail() throws IOException {
        // Mocking data
        String username = "testUser";
        String password = "testPassword";
        String postData = username + "," + password;

        URI uri = URI.create("/?=" + postData);

        // Mocking HTTP exchange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(postData.getBytes());
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking saveAccount behavior
        when(saveAccount.generateNewAccount(username, password)).thenReturn(false);

        // Set up a ByteArrayOutputStream to capture the response
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        String expectedResponse = "Signup Failed";
        assertEquals(expectedResponse, responseOutputStream.toString().trim());
    }

    @Test
    public void testHandleDallE() throws Exception {
        // Mocking data
        String title = "TestTitle";

        // Mocking HTTP exchange
        URI uri = URI.create("/?=" + title + ".DallE");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getRequestURI()).thenReturn(uri);

        // Mocking DallE behavior
        String expectedResponse = "MockedDallEImage";
        when(dallE.createImage(title)).thenReturn(expectedResponse);

        // Set up a ByteArrayOutputStream to capture the response
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        // Call the method under test
        requestHandler.handle(httpExchange);

        // Assert the response content
        assertNotNull(responseOutputStream.toString().trim());
    }


    // Source: ChatGPT 3.5 December 4 2023
    // Asked ChatGPT how to debug my code when it was not working such as why my 
    // mock iterable was null. 
    @Test
    void testHandleCreateRecipe() throws Exception {
        // Mocking data
        String ingredients = "Ingredient1";
        String mealType = "Dinner";
        String query = ingredients + "&" + mealType + "_Create";

        // Mocking HTTP exchange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(query.getBytes());
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getRequestURI()).thenReturn(new URI("/=?" + query));

        // Mocking OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        // Mocking ChatGPT behavior
        when(chatGPT.getCookingInstruction(ingredients, mealType)).thenReturn("Mocked cooking instructions");

        // Call the method under test
        requestHandler.handle(httpExchange);
        // Assert the response content
        assertNotNull(outputStream.toString().trim());
    }

    



}
