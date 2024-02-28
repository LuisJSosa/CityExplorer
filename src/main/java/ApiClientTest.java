import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.io.IOException;

public class ApiClientTest {
    public static void main(String[] args) {
        // URL of the API endpoint
        String url = "http://localhost:8080/api/Raleigh";

        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a HttpRequest for the given URL
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print status code and headers
            System.out.println("Status Code: " + response.statusCode());
            HttpHeaders headers = response.headers();
            headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

            // Print the response body (JSON)
            System.out.println("Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
