package org.training.meetingroombooking.dto.Request;

public class RefreshRequest {
    private String token;

    public RefreshRequest() {
    }

    public RefreshRequest(String token) {
        this.token = token;
    }

    public static RefreshRequestBuilder builder() {
        return new RefreshRequestBuilder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class RefreshRequestBuilder {
        private String token;

        public RefreshRequestBuilder token(String token) {
            this.token = token;
            return this;
        }

        public RefreshRequest build() {
            return new RefreshRequest(token);
        }
    }
}
