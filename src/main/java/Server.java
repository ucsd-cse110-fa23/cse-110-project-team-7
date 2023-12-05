import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Server {

  // initialize server port and hostname
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // create a map to store data
    Map<String, String> data = new HashMap<>();
    saveAccount accountManager = new saveAccount(); // Initialize saveAccount instance
    // create a server
    HttpServer server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0);

    server.createContext("/", new RequestHandler(data, accountManager));
    server.createContext("/recipe", new URLHandler(data, accountManager));

    server.setExecutor(threadPoolExecutor);

    server.start();

    System.out.println("Server started on port " + SERVER_PORT);

  }
}
