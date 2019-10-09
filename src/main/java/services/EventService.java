package services;

import application.Session;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import models.Event;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class EventService {
    private MongoConnection mongoConnection = new MongoConnection();
    private InstitutionService institutionService = new InstitutionService();
    private MongoCollection<Document> eventsCollection = mongoConnection.getCollection("events");
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection(
            "institutions");

    public List<Event> requestAllEventsOfInstitution(List<String> eventsId){
        List<Event> institutionEvents = new ArrayList<>();

        eventsId.forEach(eventId -> {
            Event requestedEvent = new Gson().fromJson(requestOneEvent(eventId), Event.class);
            requestedEvent.setId(eventId);
            institutionEvents.add(requestedEvent);
        });

        Collections.reverse(institutionEvents);
        return institutionEvents;
    }

    private String requestOneEvent(String eventId){
        return Objects.requireNonNull(eventsCollection.find(eq("_id",
                new ObjectId(eventId))).first()).toJson();
    }

    public void updateEvent(String eventId, Event updatedEvent){
        eventsCollection.replaceOne(eq("_id", new ObjectId(eventId)),
                Document.parse(new Gson().toJson(updatedEvent)));
    }

    public void insertEventInCollection(String eventJson){
        Document newEvent = Document.parse(eventJson);
        eventsCollection.insertOne(newEvent);
        String newEventId = Objects.requireNonNull(eventsCollection.find(newEvent).first()).get("_id").toString();

        institutionCollection.updateOne(eq("email", Session.getInstance().getInstitution().getEmail()),
                Updates.addToSet("events_id", newEventId));

        institutionService.updateSessionInstitution();
    }

    public void deleteEvent(String eventId){
        eventsCollection.deleteOne(eq("_id", new ObjectId(eventId)));
        institutionCollection.updateOne(eq("email", Session.getInstance().getInstitution().getEmail()),
                Updates.pull("events_id", eventId));

        institutionService.updateSessionInstitution();
    }
}
