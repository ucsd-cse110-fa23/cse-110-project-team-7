import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestHandler implements HttpHandler {

    private final Map<String, String> data;
    private final saveAccount accountManager; // added saveAccount instance

    public RequestHandler(Map<String, String> data, saveAccount accountManager) {
        this.data = data;
        this.accountManager = accountManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    try {
        if (method.equals("GET") 
        && !query.contains("Recipe") 
        && !query.contains("Whisper") 
        && !query.contains("Create")
        && !query.contains("DallE")) {
            response = handleGet(httpExchange);
        } else if (method.equals("POST")) {
            response = handlePost(httpExchange);
        } else if (method.equals("PUT")) {
            response = handlePut(httpExchange);
        } else if (method.equals("DELETE")) {
            response = handleDelete(httpExchange);
        } else if (method.equals("GET") && query.contains("Recipe")) {
            ArrayList<Recipe> recipes = handleGetRecipes(httpExchange);
            String jsonResponse = convertRecipesToJSON(recipes);
            httpExchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(jsonResponse.getBytes());
            outStream.close();
            return;
        } else if (method.equals("GET") && query.contains("Whisper")) {
            response = handleWhisper(httpExchange);    
        } else if (method.equals("GET") && query.contains("Create")) {
            response = handleCreateRecipe(httpExchange);    
        } else if (method.equals("GET") && query.contains("DallE")) {
            response = handleDallE(httpExchange);    
        } else {
            throw new Exception("Not Valid Request Method");
        }
    } catch (Exception e) {
        System.out.println("An erroneous request");
        response = e.toString();
        e.printStackTrace();
    }

    // Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
}

    // ChatGPT 3.5 December 1 2023
    // Asked ChatGPT how to convert Recipes to JSON so that the handler can read it 
    private String convertRecipesToJSON(ArrayList<Recipe> recipes) {
        JSONArray jsonArray = new JSONArray();

        for (Recipe recipe : recipes) {
            JSONObject jsonRecipe = new JSONObject();
            jsonRecipe.put("Title", recipe.getTitle());
            jsonRecipe.put("Instructions", recipe.getInstructions());
            jsonRecipe.put("Ingredients", recipe.getIngredients());
            jsonRecipe.put("Image", recipe.getImageUrl());
            jsonRecipe.put("Meal Type", recipe.getMealType());

            jsonArray.put(jsonRecipe);
        }

        return jsonArray.toString();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        // Existing code for handling GET requests
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String username; 
        String password;
        // You can add specific handling for login-related GET requests if needed
        if (query != null) {
            // Extract username and password
            username = query.substring(query.indexOf("=") + 1, query.indexOf("."));
            password = query.substring(query.indexOf(".") + 1);

            // Perform login logic with MongoDB or other authentication mechanism
            boolean loginResult = accountManager.loginAccount(username, password);
            
            // Send response back to the client
            return loginResult ? "Login Successful" : "Login Failed";
        } else {
            return "Invalid Request Data";
        }
    }

    private ArrayList<Recipe> handleGetRecipes(HttpExchange httpExchange) throws IOException {
            URI uri = httpExchange.getRequestURI();
            String query = uri.getRawQuery();
            String username = query.substring(query.indexOf("=") + 1, query.indexOf(".")); 
            DallE dallE = new DallE();

            
            accountManager.setUsername(username);
            ArrayList<Recipe> userRecipes = accountManager.readDatabase(username);
            
    
            if (!userRecipes.isEmpty()) {
                for (Recipe r : userRecipes) {
                    try {
                        String url = dallE.createImage(r.getTitle());
                        r.setImageUrl(url);
                    } catch (IOException | InterruptedException | URISyntaxException ex) {
                        ex.printStackTrace(); 
                    }
                }
            }
            return userRecipes;
    
    }
    private String handleWhisper(HttpExchange httpExchange) throws IOException, URISyntaxException {
        Whisper whisper = new Whisper();
        return whisper.display();
    }
    
    private String handleCreateRecipe(HttpExchange httpExchange) throws IOException, URISyntaxException, InterruptedException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String ingredients; 
        String mealType;
        ChatGPT chatGPT;
        // You can add specific handling for login-related GET requests if needed
        if (query != null) {

            // Extract values for "ingredients" and "mealType"
            ingredients = query.substring(query.indexOf("=") + 1, query.indexOf("&"));
            if(ingredients.contains("%20")){
                ingredients = ingredients.replaceAll("%20", " ");

            }
            mealType = query.substring(query.indexOf("&") + 1, query.indexOf("_"));

            chatGPT = new ChatGPT();

            String response = chatGPT.getCookingInstruction(ingredients, mealType);
            
            
            return response;
            
        } else {
            return "Error";
        }
    }

    private String handleDallE(HttpExchange httpExchange) throws IOException, URISyntaxException, InterruptedException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String title; 
        DallE dallE = new DallE();
        if (query != null) {
            
            title = query.substring(query.indexOf("=") + 1, query.indexOf("."));
            if(title.contains("%20")){
                title = title.replaceAll("%20", " ");

            }
            return dallE.createImage(title);
        } else {
            return "Error";
        }
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        /*
         * Make this call generateNewAccount in the saveAccount class
         */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String[] parts = postData.split(",");
        if (parts.length == 2) {
            String username = parts[0].trim();
            String password = parts[1].trim();

            // Perform signup logic with MongoDB
            boolean signupResult = accountManager.generateNewAccount(username, password);

            // Send response back to the client
            return signupResult ? "Signup Successful" : "Signup Failed";
        } else {
            return "Invalid Request Data";
        }
    }
    
    private String handlePut(HttpExchange httpExchange) throws IOException {
   
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        StringBuilder putData = new StringBuilder();
        String inputLine;
        while (scanner.hasNextLine()) {
            inputLine = scanner.nextLine();
            putData.append(inputLine);
        }
        inputLine = putData.toString();

        scanner.close();

        String[] parts = inputLine.split("~");

        if (parts.length == 6) {
            String username = parts[0].trim();
            String title = parts[1].trim();
            String ingredients = parts[2].trim();
            String instructions = parts[3].trim();
            String imageUrl = parts[4].trim();
            String mealType = parts[5].trim();

            // Perform login logic with MongoDB
            boolean result = accountManager.saveRecipeForUser(username, title, ingredients, instructions, imageUrl, mealType);

            // Send response back to the client
            return result ? "Added recipe successfully" : "Failed adding recipe";
        } else {
            return "Invalid Request Data";
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String username; 
        String title;
        
        if (query != null) {
            // Extract username and password
            username = query.substring(query.indexOf("=") + 1, query.indexOf("_"));
            title = query.substring(query.indexOf("_") + 1);
            if(title.contains("%20")){
                title = title.replaceAll("%20", " ");
            }
            
            boolean loginResult = accountManager.deleteRecipeFromDatabase(username, title);
            
            // Send response back to the client
            return loginResult ? "Delete Successful" : "Delete Failed";
        } else {
            return "Invalid Request Data";
        }
    }

}
