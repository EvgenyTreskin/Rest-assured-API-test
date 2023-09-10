package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTime {
    @JsonProperty("name")
    private String name;
    @JsonProperty("job")
    private String job;

    public UserTime() {
    }

    public UserTime(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
