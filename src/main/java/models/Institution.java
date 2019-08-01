package models;

import java.util.ArrayList;
import java.util.List;

public class Institution {
    private String name;
    private String email;
    private String password;
    private List<String> events_id;

    public Institution(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.events_id = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getEvents_id() {
        return events_id;
    }
    public void setEvents_id(List<String> events_id) {
        this.events_id = events_id;
    }
}
