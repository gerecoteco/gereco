package services;

import com.google.gson.Gson;
import models.Event;
import models.Modality;

import java.util.List;

public class ModalityService {
    private EventService eventService = new EventService();

    public List<Modality> requestAllModalities(String eventId){
        Event event = new Gson().fromJson(eventService.requestOneEvent(eventId), Event.class);
        return event.getModalities();
    }

    public Modality requestOneModality(String eventId, int indexModality){
        Event event = new Gson().fromJson(eventService.requestOneEvent(eventId), Event.class);
        return event.getModalities().get(indexModality);
    }
}
