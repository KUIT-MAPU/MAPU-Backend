package com.mapu.infra.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUserInfo {
    public String id;
    public String email;

    @JsonProperty("verified_email")
    public Boolean verifiedEmail;

    public String name;

    @JsonProperty("given_name")
    public String givenName;

    @JsonProperty("family_name")
    public String familyName;

    public String picture;
    public String locale;
}

