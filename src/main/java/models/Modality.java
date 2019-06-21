package models;

public class Modality {
    private String name;
    private Gender[] genders;

    public Modality(String name, Gender[] genders) {
        this.name = name;
        this.genders = genders;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Gender[] getGenders() {
        return genders;
    }
    public void setGenders(Gender[] genders) {
        this.genders = genders;
    }
}
