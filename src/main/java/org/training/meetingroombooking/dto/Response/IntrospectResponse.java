package org.training.meetingroombooking.dto.Response;

public class IntrospectResponse {
    private boolean valid;

    public IntrospectResponse() {
    }

    public IntrospectResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public static class Builder {
        private boolean valid;

        public Builder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public IntrospectResponse build() {
            return new IntrospectResponse(valid);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
