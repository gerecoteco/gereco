package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Event;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EventService {
    private MongoConnection mongoConnection = new MongoConnection();

    public List<Event> requestAll(String id){
        String jsonEvents = new Gson().toJson(mongoConnection.getCollection("events").find().into(new ArrayList<>()));
        return new Gson().fromJson(jsonEvents, new TypeToken<List<Event>>(){}.getType());
    }

    public void requestOne(String id){
        String jsonEvent = mongoConnection.getCollection("events").find(eq("_id", new ObjectId(id))).first().toJson();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("eventFiles/" + id + ".json"));
            bw.write(jsonEvent);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateOne(String id){
        String jsonEvent = "";
        Document query = new Document("_id", new ObjectId(id));

        try{
            BufferedReader br = new BufferedReader(new FileReader("eventFiles/" + id + ".json"));
            jsonEvent = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document update = new Document("$set", Document.parse(jsonEvent));

        mongoConnection.getCollection("events").updateOne(query , update);
    }
}
