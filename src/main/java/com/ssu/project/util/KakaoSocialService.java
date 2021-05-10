package com.ssu.project.util;

import com.google.gson.JsonParser;
import com.ssu.project.dto.social.kakao.KakaoProfile;
import com.ssu.project.dto.social.kakao.KakaoToken;
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
public class KakaoSocialService {

    public KakaoToken getKakaoAccessToken(String code) throws Exception {
        URL url = new URL("https://kauth.kakao.com/oauth/token"); // 호출할 url

        Map<String, Object> params = new LinkedHashMap<>(); // 파라미터 세팅
        params.put("grant_type", "authorization_code");
        params.put("client_id", "7db4066f7f7786e895ec8781c1cb2976");
        params.put("redirect_uri", "http://localhost:8080/kakaoCallback");
        params.put("code", code);
        params.put("client_secret", "hDIyw2cGm2yVN6lkJygDHuskZZg5DRnO");

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
            postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

        return KakaoToken.builder()
                .token(jp.parse(sb.toString())
                        .getAsJsonObject()
                        .get("access_token").toString().replaceAll("\"", ""))
                .build();
    }

    public KakaoProfile getKakaoUserProfile(KakaoToken kakaoToken) throws Exception {
        String api_url = "https://kapi.kakao.com/v2/user/me";

        StringBuilder urlBuilder = new StringBuilder(api_url);
        System.out.println(urlBuilder.toString());
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "Bearer " + kakaoToken.getToken());

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

        System.out.println("JSON");
        JSONObject jsonObj = (JSONObject) obj;

        Long id = (Long) jsonObj.get("id");
        JSONObject response_obj = (JSONObject)jsonObj.get("kakao_account");
        String email = (String)response_obj.get("email");
        System.out.println(id);
        System.out.println(email);

        return KakaoProfile.builder()
                .id(id.toString())
                .email(email)
                .build();
    }
}