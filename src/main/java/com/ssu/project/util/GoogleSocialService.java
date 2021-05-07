package com.ssu.project.util;

import com.google.gson.JsonParser;
import com.ssu.project.dto.social.google.GoogleProfile;
import com.ssu.project.dto.social.google.GoogleToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GoogleSocialService {
    public GoogleToken getGoogleAccessToken(String code) throws Exception {

        URL url = new URL("https://oauth2.googleapis.com/token"); // 호출할 url
        Map<String, Object> params = new LinkedHashMap<>(); // 파라미터 세팅
        params.put("code", code);
        params.put("client_id", "491929978030-bcubi8usjo62rdm98ttf3oqf7sk010lu.apps.googleusercontent.com");
        params.put("client_secret", "Sghq6pzYkG6vwHH5ueDYYYP-");
        params.put("redirect_uri", "http://localhost:8080/googleCallback");
        params.put("grant_type", "authorization_code");

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        JsonParser jp = new JsonParser();

        return GoogleToken.builder()
                .token(jp.parse(sb.toString())
                        .getAsJsonObject()
                        .get("access_token").toString().replaceAll("\"", ""))
                .build();
    }

    public GoogleProfile getGoogleUserProfile(GoogleToken googleToken) throws Exception {
        String api_url = "https://www.googleapis.com/oauth2/v2/userinfo";

        StringBuilder urlBuilder = new StringBuilder(api_url);
        urlBuilder.append("?" + URLEncoder.encode("access_token","UTF-8") + "=" + URLEncoder.encode(googleToken.getToken(), "UTF-8"));
        System.out.println(urlBuilder.toString());
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + googleToken.getToken());

        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(sb.toString());

        } catch (Exception e ) {
            e.printStackTrace();
        }

        JSONObject jsonObj = (JSONObject) obj;
        String id = (String) jsonObj.get("id");
        String email = (String) jsonObj.get("email");
        System.out.println(id);
        System.out.println(email);

        return GoogleProfile.builder()
                .id(id)
                .email(email)
                .build();
    }
}
