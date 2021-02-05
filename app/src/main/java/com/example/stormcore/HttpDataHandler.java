package com.example.stormcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpDataHandler {

    public HttpDataHandler(){

    }
    //Begin Walter Driving
    public String getHTTPData(String req) {
        URL url;

        String resp = "";
        try {
            url = new URL(req);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(16000);
            conn.setConnectTimeout(16000);
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int respCd = conn.getResponseCode();
            if (respCd == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null)
                    resp += line;
            } else
                resp = "";
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resp;

    //end Walter Driving
    }

}
