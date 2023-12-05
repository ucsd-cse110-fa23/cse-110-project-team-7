import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGPTTest {

    class ChatGPTMock implements IChatGPT {

        @Override
        public String getCookingInstruction(String ingredients, String mealType) throws IOException, InterruptedException, URISyntaxException {
            return "Fake API: This is a fake cooking instruction for testing.";
        }

    }

    class ChatGPTMock2 implements IChatGPT{
        @Override
        public String getCookingInstruction(String ingredients, String mealType) throws IOException, InterruptedException, URISyntaxException {
            throw new IOException("Fake API exception");
        }
    }

    private IChatGPT fakeChatGPT;
    private IChatGPT fakeChatGPT2;
    private boolean real = false;

    @BeforeEach
    public void setup() {
        // Create a fake API instance with the mock strategy
        fakeChatGPT = new ChatGPTMock();
        fakeChatGPT2 = new ChatGPTMock2();
    }

    public String getCookingInstruction(String ingredients, String mealType) throws IOException, InterruptedException, URISyntaxException {
        if(real){
            return fakeChatGPT.getCookingInstruction(ingredients, mealType);
        }
        return fakeChatGPT2.getCookingInstruction(ingredients, mealType);
    }

    @Test
    public void testGetCookingInstructionSuccess() throws Exception {
        // Call the method as if it were the real API
        real = true;

        String response = getCookingInstruction("ingredients", "mealType");
        //assertEquals(recipe.getMealType(), "Lunch");
        // Assert the response
        assertEquals(response, "Fake API: This is a fake cooking instruction for testing.");
    }

    @Test
    public void testGetCookingInstructionException() throws Exception {
       Exception exception = assertThrows(IOException.class, () -> {
            getCookingInstruction("ingredients", "mealType");
        });

        // You can add more specific assertions on the exception if needed
        assertTrue(exception.getMessage().contains("Fake API exception"));
    
    }

}
