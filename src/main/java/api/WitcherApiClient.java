// src/main/java/api/WitcherApiClient.java

package api;
import api.models.Weapon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson; // Per il parsing JSON

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WitcherApiClient {

    private final OkHttpClient client = createInsecureOkHttpClient();

    private static final String BASE_URL = "http://localhost:5000";

    private final Gson gson = new Gson();

    private static OkHttpClient createInsecureOkHttpClient() {
        try {
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAllWeaponsJson() throws IOException {
        String fullUrl = BASE_URL + "/Weapon";
        Request request = new Request.Builder()
                .url(fullUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("fault");
                throw new IOException("Request to the DB failed: " + response.code());
            }
            System.out.println("json");
            return response.body().string();
        }
    }
    public Weapon getWeaponById(int id) throws IOException {
        String endpoint = "/Weapon/" + id;
        String fullUrl = BASE_URL + endpoint;
        Request request = new Request.Builder()
                .url(fullUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    return null;
                }
                throw new IOException("API request failed: " + response.code());
            }
            String jsonResponse = response.body().string();
            System.out.println(jsonResponse);
            return gson.fromJson(jsonResponse, Weapon.class);
        }
    }

    public byte[] downloadImage(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("It is not possible to download an image: " + response.code() + " URL: " + url);
            }
            return response.body().bytes();
        }
    }
}