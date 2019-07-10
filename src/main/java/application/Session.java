package application;

import models.Institution;

public class Session {
    private static Session instance = null;
    private Institution institution;

    private Session() {

    }

    public Institution getInstitution() {
        return institution;
    }
    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    public static Session getInstance(){
        if(instance == null)
            instance = new Session();
        return instance;
    }
}
