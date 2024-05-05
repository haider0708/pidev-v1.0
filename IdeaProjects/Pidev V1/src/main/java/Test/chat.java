package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class chat {
    public static void chatGPT(String text) throws Exception {
        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer sk-proj-6bORIttwNDTWEcJ1zxpCT3BlbkFJUuznR1JqCTX6wO2CfIiX");
//sk-proj-xPvo8asXb7pOmsT6ij3yT3BlbkFJkt2SxljBoZvtmVFwOmCZ

        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
        data.put("prompt", text);
        data.put("max_tokens", 256);
        data.put("temperature", 0.7);
        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Request failed with HTTP error code: " + responseCode);
        }
    }

    public static void main(String[] args) throws Exception {
        chatGPT("Hello, how are you?");
    }
}
//sk-proj-6bORIttwNDTWEcJ1zxpCT3BlbkFJUuznR1JqCTX6wO2CfIiX
//sk-proj-6bORIttwNDTWEcJ1zxpCT3BlbkFJUuznR1JqCTX6wO2CfIiX