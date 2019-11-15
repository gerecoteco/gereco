package models;

public class GeneralMatch {
    private String modality;
    private String gender;
    private int stage;
    private String teamA;
    private String teamB;

    public GeneralMatch() {
    }

    public GeneralMatch(String modality, String gender, int stage,
                           String teamA, String teamB) {
        this.modality = modality;
        this.gender = gender;
        this.stage = stage;
        this.teamA = teamA;
        this.teamB =  teamB;
    }

    public String getModality() {
        return modality;
    }
    public void setModality(String modality) {
        this.modality = modality;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getStage() {
        return stage;
    }
    public void setStage(int stage) {
        this.stage = stage;
    }
    public String getTeamA() {
        return teamA;
    }
    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }
    public String getTeamB() {
        return teamB;
    }
    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }
}
