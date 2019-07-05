package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import models.Event;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class EventService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> eventsCollection = mongoConnection.getCollection("events");
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection("institution");

    public List<Event> requestAllEvents(){
        String eventsJson = new Gson().toJson(eventsCollection.find().into(new ArrayList<>()));
        return new Gson().fromJson(eventsJson, new TypeToken<List<Event>>(){}.getType());
    }

    public void requestOneEvent(String eventId){
        String eventJson = Objects.requireNonNull(eventsCollection.find(eq("_id", new ObjectId(eventId))).first()).toJson();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("eventFiles/" + eventId + ".json"));
            bw.write(eventJson);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(String eventId){
        String eventJson = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader("eventFiles/" + eventId + ".json"));
            eventJson = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        eventsCollection.replaceOne(eq("_id", new ObjectId(eventId)), Document.parse(eventJson));
    }

    public void insertEvent(String eventJson, String institutionId){
        Document newEvent = Document.parse(eventJson);
        eventsCollection.insertOne(newEvent);
        String newEventId = Objects.requireNonNull(eventsCollection.find(newEvent).first()).get("_id").toString();

        institutionCollection.updateOne(eq("_id", new ObjectId(institutionId)),
                Updates.addToSet("events_id", newEventId));
    }
}
