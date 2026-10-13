package smp_mod.smp_mod.MixinHelperMethods;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordAdminCommandSender {


    public static void send(String webhookUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"content\": \"" + message.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode(); // Request absenden
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
