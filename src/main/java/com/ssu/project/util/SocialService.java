package com.ssu.project.util;

import com.google.gson.JsonParser;
import com.ssu.project.dto.social.Profile;
import com.ssu.project.dto.social.Token;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class SocialService {

    public Token getAccessToken(String code, String state) throws Exception {
        String api_url = "https://nid.naver.com/oauth2.0/token";

        StringBuilder urlBuilder = new StringBuilder(api_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("grant_type","UTF-8") + "=" + URLEncoder.encode("authorization_code", "UTF-8")); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("client_id","UTF-8") + "=" + URLEncoder.encode("tJnDoWnlch9tVLpV5vd7", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("client_secret","UTF-8") + "=" + URLEncoder.encode("48PTBeodnZ", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("code","UTF-8") + "=" + URLEncoder.encode(code, "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(state, "UTF-8")); /*하단 참고자료 참조*/
        System.out.println(urlBuilder.toString());

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
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

        JsonParser jp = new JsonParser();

        return Token.builder()
                .token(jp.parse(sb.toString())
                    .getAsJsonObject()
                    .get("access_token").toString().replaceAll("\"", ""))
                .build();
    }

    public Profile getUserProfile(Token token) throws Exception {
        String api_url = "https://openapi.naver.com/v1/nid/me";


        StringBuilder urlBuilder = new StringBuilder(api_url);
        System.out.println(urlBuilder.toString());
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token.getToken());

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
        JSONObject response_obj = (JSONObject)jsonObj.get("response");
        String id = (String)response_obj.get("id");
        String email = (String)response_obj.get("email");
        System.out.println(id);
        System.out.println(email);

        return Profile.builder()
                .id(id)
                .email(email)
                .build();
    }
}
