package services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoConnection {
    private static MongoClient mongoClient;

    public void createConnection(){
        defineMongoLoggerLevel();
        mongoClient = MongoClients.create("mongodb+srv://user:user@clustergereco-rhvgo.mongodb.net");
    }

    MongoCollection<Document> getCollection(String collectionName){
        return getConnection().getCollection(collectionName);
    }

    private MongoDatabase getConnection(){
        return mongoClient.getDatabase("gereco");
    }

    private void defineMongoLoggerLevel(){
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }
}
