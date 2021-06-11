package com.example.rashapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class HttpClientConnection
{
    public static String getData(URL url, JSONObject jsn) throws IOException, JSONException {
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");

        Iterator<String> iterator = jsn.keys();
        boolean first = true;
        String strSend = "";

        while(iterator.hasNext()) {
            String key = iterator.next();
            String value = jsn.get(key).toString();

            if (first) {
                first = false;
            } else {
                strSend += "&";
            }
            strSend += URLEncoder.encode(key, "UTF-8");
            strSend += "=";
            strSend += URLEncoder.encode(value, "UTF-8");
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bw.write(strSend);
        bw.close();

        int responseCode = con.getResponseCode();

        if(responseCode == 200)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String strResp = "";
            String line = "";

            while(line!=null)
            {
                line = br.readLine();
                strResp += line;
            }
            return strResp;
        }
        else
        {
            return "Error" + responseCode;
        }
    }
}