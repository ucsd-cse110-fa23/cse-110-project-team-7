import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGPTTest {

    class ChatGPTMock implements IChatGPT {

        @Override
        public String getCookingInstruction(Recipe recipe) throws IOException, InterruptedException, URISyntaxException {
            return "Fake API: This is a fake cooking instruction for testing.";
        }

    }

    class ChatGPTMock2 implements IChatGPT{
        @Override
        public String getCookingInstruction(Recipe recipe) throws IOException, InterruptedException, URISyntaxException {
            throw new IOException("Fake API exception");
        }
    }

    private IChatGPT fakeChatGPT;
    private IChatGPT fakeChatGPT2;

    @BeforeEach
    public void setup() {
        // Create a fake API instance with the mock strategy
        fakeChatGPT = new ChatGPTMock();
        fakeChatGPT2 = new ChatGPTMock2();
    }

    public String getCookingInstruction(Recipe recipe) throws IOException, InterruptedException, URISyntaxException {
        if(recipe.getMealType() == "lunch"){
            return fakeChatGPT.getCookingInstruction(recipe);
        }
        return fakeChatGPT2.getCookingInstruction(recipe);
    }

    @Test
    public void testGetCookingInstructionSuccess() throws Exception {
        // Call the method as if it were the real API
        Recipe recipe = new Recipe();
        recipe.setMealType("lunch");

        String response = getCookingInstruction(recipe);

        // Assert the response
        assertEquals(response, "Fake API: This is a fake cooking instruction for testing.");
    }

    @Test
    public void testGetCookingInstructionException() throws Exception {
       Exception exception = assertThrows(IOException.class, () -> {
            getCookingInstruction(new Recipe());
        });

        // You can add more specific assertions on the exception if needed
        assertTrue(exception.getMessage().contains("Fake API exception"));
    
    }

}