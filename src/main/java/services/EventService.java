package services;

import application.Session;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import helpers.EventFilesManager;
import models.Event;
import models.Institution;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class EventService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> eventsCollection = mongoConnection.getCollection("events");
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection(
            "institutions");

    public List<Event> requestAllEventsOfInstitution(List<String> eventsId){
        List<Event> institutionEvents = new ArrayList<>();

        eventsId.forEach(eventId ->
                institutionEvents.add(new Gson().fromJson(requestOneEvent(eventId), Event.class)));

        Collections.reverse(institutionEvents);
        return institutionEvents;
    }

    public String requestOneEvent(String eventId){
        return Objects.requireNonNull(eventsCollection.find(eq("_id",
                new ObjectId(eventId))).first()).toJson();
    }

    public void writeEvent(String eventId){
        String eventJson = requestOneEvent(eventId);
        EventFilesManager.write(eventId, eventJson);
    }

    public void updateEvent(String eventId){
        String eventJson = EventFilesManager.read(eventId);
        eventsCollection.replaceOne(eq("_id", new ObjectId(eventId)), Document.parse(eventJson));
    }

    public void insertEventInCollection(String eventJson, String institutionEmail){
        Document newEvent = Document.parse(eventJson);
        eventsCollection.insertOne(newEvent);
        String newEventId = Objects.requireNonNull(eventsCollection.find(newEvent).first()).get("_id").toString();

        institutionCollection.updateOne(eq("email", institutionEmail),
                Updates.addToSet("events_id", newEventId));

        updateSessionInstitution();
    }

    private void updateSessionInstitution(){
        InstitutionService institutionService = new InstitutionService();
        String institutionEmail = Session.getInstance().getInstitution().getEmail();

        Institution updatedInstitution = institutionService.findByEmail(institutionEmail);
        Session.getInstance().setInstitution(updatedInstitution);
    }
}
