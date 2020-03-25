package ru.testing.api.entities.authorization;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorizationResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
}