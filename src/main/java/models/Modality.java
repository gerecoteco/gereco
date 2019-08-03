package models;

import java.util.List;

public class Modality {
    private String name;
    private List<Gender> genders;

    public Modality(String name, List<Gender> genders) {
        this.name = name;
        this.genders = genders;
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
