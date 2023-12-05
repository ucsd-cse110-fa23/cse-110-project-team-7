import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

interface IDallE{
    String createImage(String prompt) throws IOException, InterruptedException, URISyntaxException;

}
public class DallE implements IDallE{

    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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";
    private static final String MODEL = "dall-e-2";

    public String createImage(String prompt) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        int n = 1;

        // Create a request body which you will pass into the request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", n);
        requestBody.put("size", "256x256");
        String generatedImageURL = "";
        // Create the HTTP client
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
                HttpResponse.BodyHandlers.ofString());
        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        // Check if the "data" array exists
        if (responseJson.has("data")) {
            // Get the first element from the "data" array
            JSONObject dataObject = responseJson.getJSONArray("data").optJSONObject(0);

            if (dataObject != null && dataObject.has("url")) {
                generatedImageURL = dataObject.getString("url");

                return generatedImageURL;

            } else {
                System.out.println("Error: Unable to find 'url' in the response.");
            }
        } else {
            System.out.println("Error: Unable to find 'data' array in the response.");
        }
        return generatedImageURL;
        
    }
}
