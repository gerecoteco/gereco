package services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

class DBConnection {
    private MongoDatabase getConnection(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://admin_user:12345@clustergereco-rhvgo.mongodb.net");
        return mongoClient.getDatabase("gereco");
    }

    public MongoCollection<Document> getCollection(String nameCollection){
        return getConnection().getCollection(nameCollection);
    }
}