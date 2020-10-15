package nl.altindag.client;

import nl.altindag.sslcontext.SSLFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        SSLFactory sslFactory = SSLFactory.builder()
                .withIdentityMaterial("client-identity.jks", "secret".toCharArray())
                .withTrustMaterial("client-truststore.jks", "secret".toCharArray())
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .sslParameters(sslFactory.getSslParameters())
                .sslContext(sslFactory.getSslContext())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://localhost:443/hello"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.printf("Received [%d] status code from the server%n", response.statusCode());
    }

}
