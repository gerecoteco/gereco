package services;

import org.bson.Document;

import java.util.List;

public class MongoCollections {
    private MongoConnection mongoConnection = new MongoConnection();

    public void insertOneDocument(String collectionName, Document document){
        mongoConnection.getCollection(collectionName).insertOne(document);
    }

    public void insertManyDocuments(String collectionName, List<Document> documents){
        mongoConnection.getCollection(collectionName).insertMany(documents);
    }
}
