package models;

import java.util.ArrayList;
import java.util.List;

public class Modality {
    private String name;
    private List<Gender> genders;

    public Modality(String name) {
        this.name = name;
        this.genders = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Gender> getGenders() {
        return genders;
    }
    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }
}
