package org.training.meetingroombooking.dto.Response;

public class AuthResponse {
    private String accesstoken;
    private String refreshToken;

    public AuthResponse() {}

    public AuthResponse(String accesstoken, String refreshToken) {
        this.accesstoken = accesstoken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accesstoken;
    }

    public void setAccessToken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static class Builder {
        private String accesstoken;
        private String refreshToken;

        public Builder auth(AuthResponse response) {
            this.accesstoken = response.getAccessToken();
            this.refreshToken = response.getRefreshToken();
            return this;
        }

        public Builder accesstoken(String accesstoken) {
            this.accesstoken = accesstoken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(accesstoken, refreshToken);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
