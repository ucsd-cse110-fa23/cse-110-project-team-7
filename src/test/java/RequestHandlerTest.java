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
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private FindIterable<Document> mockFindIterable;

    @Mock
    private MongoCursor<Document> mockCursor;

    @Mock
    private saveAccount saveAccount;

    @InjectMocks
    private RequestHandler requestHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any())).thenReturn(mockFindIterable);
        //saveAccount = new saveAccount(mockDatabase, mockCollection);
        requestHandler = new RequestHandler(new HashMap<>(), saveAccount);
   }

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

}
