package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Register {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public Register() {
    }

    public Register(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
