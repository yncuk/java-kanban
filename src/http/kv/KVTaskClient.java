package http.kv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String URL;
    private final String API_TOKEN;

    public KVTaskClient(String URL) {
        this.URL = URL; //"http://localhost:8078/";
        API_TOKEN = register();
    }

    private String register() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "register"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void put(String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + API_TOKEN))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(send.statusCode());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + API_TOKEN))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
