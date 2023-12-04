import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class URLHandler implements HttpHandler {
    private final Map<String, String> data;
    private final saveAccount accountManager;
    public URLHandler(Map<String, String> data, saveAccount accountManager) {
        this.data = data;
        this.accountManager = accountManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
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

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String username; 
        String title;
        Recipe recipe = new Recipe();
    // You can add specific handling for login-related GET requests if needed
        if (query != null) {
        // Extract username and password
            username = query.substring(query.indexOf("=") + 1, query.indexOf("_"));
            title = query.substring(query.indexOf("_") + 1);
            if(title.contains("%20")){
                title = title.replaceAll("%20", " ");
            }

            recipe = accountManager.findRecipe(username, title);
            
            
        }
        StringBuilder htmlBuilder = new StringBuilder();
        if (recipe != null) {
            
            htmlBuilder
                    .append("<html>")
                    .append("<body>")
                    .append("<h1>")
                    .append(recipe.getTitle())
                    .append("</h1>")
                    .append("<img src=\"")
                    .append(recipe.getImageUrl())
                    .append("\"> <br>")
                    .append("<p>Inputted Ingredients: ")
                    .append(recipe.getIngredients())
                    .append("</p> <br>")
                    .append("<p>")
                    .append(recipe.getInstructions())
                    .append("</p>")
                    .append("</body>")
                    .append("</html>");

            // encode HTML content
            response = htmlBuilder.toString();
        }
        else{
            htmlBuilder
            .append("<html>")
            .append("<body>")
            .append("<h1>")
            .append("No Recipes Found")
            .append("</h1>")
            .append("</body>")
            .append("</html>");
            response = htmlBuilder.toString();
        }

        return response;
    }

}