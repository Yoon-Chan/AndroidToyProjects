package com.chan.oauthserver.member.service;

import com.chan.oauthserver.member.dto.AccessTokenDto;
import com.chan.oauthserver.member.dto.GoogleProfileDto;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class GoogleService {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    public AccessTokenDto getAccessToken(String code) {
        //인가코드, clientId, client_secret, redirect_uri, grant_type
        //Spring6부터 RestTemplate 은 비추천 대신, RestClient 권장
        RestClient restClient = RestClient.create();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                //retrieve는 응답 body값만을 추출한다.
                .retrieve()
                .toEntity(AccessTokenDto.class);

        return response.getBody();
    }

    public GoogleProfileDto getGoogleProfile(String token) {
        RestClient restClient = RestClient.create();

        ResponseEntity<GoogleProfileDto> response = restClient.post()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header("Authorization", "Bearer " + token)
                //retrieve는 응답 body값만을 추출한다.
                .retrieve()
                .toEntity(GoogleProfileDto.class);

        return response.getBody();
    }
}
