package services;

import org.bson.Document;

import java.io.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoCollections {
    private MongoConnection mongoConnection = new MongoConnection();

    public void requestEvent(String key, String value){
        String jsonEvent = mongoConnection.getCollection("eventos").find(eq(key, value)).first().toJson();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("jsonFiles/evento.json"));
            bw.write(jsonEvent);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertEvent(){
        String jsonEvento = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader("jsonFiles/evento.json"));
            jsonEvento = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mongoConnection.getCollection("eventos").insertOne(Document.parse(jsonEvento));
    }
}
