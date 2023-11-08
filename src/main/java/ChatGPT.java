import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

interface IChatGPT{
    String getCookingInstruction(Recipe recipe) throws IOException, InterruptedException, URISyntaxException;

}
public class ChatGPT implements IChatGPT{

    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    
    public String getCookingInstruction(Recipe recipe) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        Recipe currentRecipe = recipe;

        String ingredientInHand = currentRecipe.getIngredients();
        String mealType = currentRecipe.getMealType();


        String prompt = "I have following ingredients : "+ingredientInHand+" and I would like to get a budget friendly and easy to make recipe for my "+ mealType+" with a list of step by step instructions. Please put title in square brackets.";

        
        int maxTokens = 400;
        //System.out.println(prompt);

        //System.out.println(prompt);

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client


        HttpClient client = HttpClient.newHttpClient();


        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();


        // Send the request and receive the response
        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
        );

        // Process the response
        String responseBody = response.body();
        //System.out.println(responseBody);
        JSONObject responseJson = new JSONObject(responseBody);


        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");


        System.out.println(generatedText);
        return generatedText;
    }
    // public static void main(String[] args) throws Exception {
    //     getCookingInstruction();
    // }

}