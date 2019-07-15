package services;

import models.Gender;

public class GenderService {
    private ModalityService modalityService = new ModalityService();

    public Gender[] requestAllGenders(String eventId, int indexModality){
        return modalityService.requestOneModality(eventId, indexModality).getGenders();
    }

    public Gender requestOneGender(String eventId, int indexModality, int indexGender){
        return modalityService.requestOneModality(eventId, indexModality).getGenders()[indexGender];
    }
}
