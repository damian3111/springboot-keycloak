package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.Consumer;


@Log4j2
@RequiredArgsConstructor
@RestController
public class CustomController {
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @GetMapping("/get1")
    public String get1(Authentication authentication){
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService
                .loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());

        String jwtAccessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        String jwtRefrechToken = oAuth2AuthorizedClient.getRefreshToken().getTokenValue();

        log.info(jwtAccessToken);

        return "get1 - response";
    }



    @GetMapping("/get2")
    public String get2(Authentication authentication){


        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService
                .loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
        String jwtAccessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        String jwtRefrechToken = oAuth2AuthorizedClient.getRefreshToken().getTokenValue();

        log.info(jwtAccessToken);
        log.info("refresh token: " + jwtRefrechToken);


        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + jwtAccessToken);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> exchange = rest.exchange(
                "http://localhost:8081/res1",
                HttpMethod.GET,
                entity,
                String.class);




        //String welcome = welcomeClient.getWelcome();
        return exchange.getBody();
    }

    @GetMapping("/get3")
    public String get3(@AuthenticationPrincipal OidcUser oidcUser){

        OidcIdToken idToken = oidcUser.getIdToken();

        String tokenValue = idToken.getTokenValue();

        return tokenValue;
    }

    @GetMapping("/get4")
    public String get4(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal){
        return principal.getAttribute("sub") + " is the subject";

    }

    @GetMapping("/get5")
    public String get5(@AuthenticationPrincipal OidcUser oidcUser) {

        log.info("authorities: " + oidcUser.getAuthorities());
        log.info("claims: " + oidcUser.getClaims());

        return "get5 - reponse";
    }

}
