package es.tubalcain.security;


// Provider para OAuth2, IGNORAR POR AHORA
public class OAuthTokenProvider {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String authorizationUrl;
    private final String tokenUrl;
    private final String userInfoUrl;

    public OAuthTokenProvider(String clientId, String clientSecret, String redirectUri, String authorizationUrl, String tokenUrl, String userInfoUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }            
}