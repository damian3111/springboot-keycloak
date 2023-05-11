package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;

@SpringBootApplication
public class KeycloackApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloackApplication.class, args);
	}

}
