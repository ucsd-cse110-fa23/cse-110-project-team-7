import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;


public class PerformRequest {
    public String performRequest(String method, String username, String password, String query) {
        // Implement your HTTP request logic here and return the response

        try {
            String urlString = "http://localhost:8100/";
            if (query != null) {
                urlString += "?=" + query;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(username + "," + password);
                out.flush();
                out.close();
            }
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //String response = "";
            
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String finalResponse = response.toString();

            // while ((inputLine = in.readLine()) != null) {
            //     response += inputLine;
            // }
            in.close();
            System.out.println("Perform request response: " + response);
            return finalResponse;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public ArrayList<Recipe> performRecipeRequest(String method, String title, String details, String query) {
    try {
        String urlString = "http://localhost:8100/";
        if (query != null) {
            urlString += "?=" + query;
        }
        URL url = new URI(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        // Check the response code to ensure the request was successful
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Received response: " + response.toString()); // Request Received

            ArrayList<Recipe> recipeList = parseRecipeResponse(response.toString());

            return recipeList;
        } else {
            System.out.println("HTTP request failed with response code: " + responseCode);
            return null;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}

private ArrayList<Recipe> parseRecipeResponse(String jsonResponse) {
    System.out.println("Received JSON response: " + jsonResponse);

    ArrayList<Recipe> recipeList = new ArrayList<>();

    try {
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonRecipe = jsonArray.getJSONObject(i);
            Recipe recipe = createRecipeFromJson(jsonRecipe);
            recipeList.add(recipe);
        }
    } catch (Exception arrayException) {
        // If parsing as an array fails, try to parse as a single object
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            Recipe recipe = createRecipeFromJson(jsonObject);
            recipeList.add(recipe);
        } catch (Exception objectException) {
            objectException.printStackTrace();
        }
    }

    return recipeList;
}

private Recipe createRecipeFromJson(JSONObject jsonRecipe) {
    // parse json and populate recipe 
    String title = jsonRecipe.getString("Title");
    String instructions = jsonRecipe.getString("Instructions");
    String ingredients = jsonRecipe.getString("Ingredients");
    String imageUrl = jsonRecipe.getString("Image");
    String type = jsonRecipe.getString("Meal Type");
    return new Recipe(title, instructions, ingredients, imageUrl, type); 
}

    
    
}