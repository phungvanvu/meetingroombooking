package org.training.meetingroombooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthReponse {
    private boolean auth;
    private String message;
}
