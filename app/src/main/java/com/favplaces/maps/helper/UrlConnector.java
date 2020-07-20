package com.favplaces.maps.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConnector {

    public String readUrl(String myUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;

        HttpURLConnection urlConnection  = null;

        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            //to read the variable line by line
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                stringBuffer.append(line);

            data = stringBuffer.toString();
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null)
                inputStream.close();

            urlConnection.disconnect();
        }

        return data;
    }



}
