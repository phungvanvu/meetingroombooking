package org.training.meetingroombooking.dto.Response;

public class AuthResponse {
    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private String token;

        public Builder auth(AuthResponse response) {
            this.token = response.getToken();
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

}
