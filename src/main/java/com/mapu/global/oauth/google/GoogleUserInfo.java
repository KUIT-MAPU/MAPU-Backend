package com.mapu.global.oauth.google;

import lombok.Data;

@Data
public class GoogleUserInfo {
    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;
}
