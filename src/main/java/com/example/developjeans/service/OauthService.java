package com.example.developjeans.service;



import com.example.developjeans.dto.KaKaoUserInfo;
import com.example.developjeans.global.config.Response.BaseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class OauthService {

    public String getKakaoAccessToken(String code) {

        String access_token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            //access_token을 이용하여 사용자 정보 조회
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            StringBuilder result;

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터를 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream())));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ff5e8301eb86997e0e6c5bb9d24e92f2");
            sb.append("&redirect_uri=http://localhost:5173/redirect");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("result = " + result);
            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result.toString());
            access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);
            br.close();
            bw.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return access_token;
    }

    public KaKaoUserInfo getKaKaoUserInfo(String token){
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo();

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            //Gson 라이브러리로 JSON파싱
            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject jsonObject = element.getAsJsonObject();

            // id 처리
            JsonElement idElement = jsonObject.get("id");
            kaKaoUserInfo.setId(idElement != null && !idElement.isJsonNull() ? idElement.getAsLong() : 0L);

            // nickName 처리
            JsonElement nickNameElement = jsonObject.get("nickName");
            kaKaoUserInfo.setNickName(nickNameElement != null && !nickNameElement.isJsonNull() ? nickNameElement.getAsString() : "");

            // email 처리
            JsonElement emailElement = jsonObject.get("email");
            kaKaoUserInfo.setEmail(emailElement != null && !emailElement.isJsonNull() ? emailElement.getAsString() : "");
            //dto에 저장하기
//            kaKaoUserInfo.setId(element.getAsJsonObject().get("id").getAsLong());
//            kaKaoUserInfo.setNickName(element.getAsJsonObject().get("nickName").getAsString());
//            kaKaoUserInfo.setEmail(element.getAsJsonObject().get("email").getAsString());

            log.info(kaKaoUserInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kaKaoUserInfo;
    }

    public void createKakaoUser(String token) throws IOException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("id : " + id);
            System.out.println("email : " + email);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kakaoLogout(String token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }






}
