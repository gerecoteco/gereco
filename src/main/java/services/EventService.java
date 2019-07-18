package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import helpers.EventFilesManager;
import models.Event;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class EventService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> eventsCollection = mongoConnection.getCollection("events");
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection("institutions");

    public List<Event> requestAllEventsAndReturn(){
        String eventsJson = new Gson().toJson(eventsCollection.find().into(new ArrayList<>()));
        return new Gson().fromJson(eventsJson, new TypeToken<List<Event>>(){}.getType());
    }

    public String requestOneEvent(String eventId){
        return Objects.requireNonNull(eventsCollection.find(eq("_id", new ObjectId(eventId))).first()).toJson();
    }

    public void writeEvent(String eventId){
        String eventJson = requestOneEvent(eventId);
        EventFilesManager.write(eventId, eventJson);
    }

    public void updateEvent(String eventId){
        String eventJson = EventFilesManager.read(eventId);
        eventsCollection.replaceOne(eq("_id", new ObjectId(eventId)), Document.parse(eventJson));
    }

    public void insertEventInCollection(String eventJson, String institutionId){
        Document newEvent = Document.parse(eventJson);
        eventsCollection.insertOne(newEvent);
        String newEventId = Objects.requireNonNull(eventsCollection.find(newEvent).first()).get("_id").toString();

        institutionCollection.updateOne(eq("_id", new ObjectId(institutionId)),
                Updates.addToSet("events_id", newEventId));
    }
}
