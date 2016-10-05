package ml.statshub.statshub;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 196128636 on 2016-10-05.
 */

public class HTTPRequest {

    public String queryToHDB(String requestURL){
        HttpURLConnection conn = null;
        String response = "";
        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            } else {
                response="";

            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(conn != null) conn.disconnect();
        }

        return response;

    }
}