import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

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
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
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
        // Existing code for handling GET requests
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    String username; 
    String password;
    // You can add specific handling for login-related GET requests if needed
    if (query != null) {
        // Parse query parameters
        username = query.substring(query.indexOf("=") + 1, query.indexOf("."));
        password = query.substring(query.indexOf(".") + 1);
        //Map<String, String> queryParams = parseQueryParameters(query);
        // Extract username and password
        

        // Perform login logic with MongoDB or other authentication mechanism
        boolean loginResult = accountManager.loginAccount(username, password);

        // Send response back to the client
        return loginResult ? "Login Successful" : "Login Failed";
    } else {
        return "Invalid Request Data";
    }
    /* 
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            boolean loginResult = accountManager.loginAccount(response, query);
            String value = query.substring(query.indexOf("=") + 1);
            String year = data.get(value); // Retrieve data from hashmap
            if (year != null) {
                response = year;
                System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;
*/
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
            System.out.println(username);
            System.out.println(password);
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
        String putData = scanner.nextLine();
        String[] parts = putData.split(",");
        if (parts.length == 2) {
            String username = parts[0].trim();
            String password = parts[1].trim();

            // Perform login logic with MongoDB
            boolean loginResult = accountManager.loginAccount(username, password);

            // Send response back to the client
            return loginResult ? "Login Successful" : "Login Failed";
        } else {
            return "Invalid Request Data";
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String language = query.substring(query.indexOf("=") + 1);
            String year = data.get(language); // Retrieve data from hashmap
            if (year != null) {
                data.remove(language);
                response = "Deleted entry {" + language + ", " + year + "}";
                System.out.println("Deleted entry {" + language + ", " + year + "}");
            } else {
                response = "No data found for " + language;
            }
        }
        return response;
    }

}
