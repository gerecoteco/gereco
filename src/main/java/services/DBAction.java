package services;

import org.bson.Document;

import java.util.List;

public class DBAction {
    DBConnection dbc = new DBConnection();

    public void insertOneDocument(String nameCollection, Document document){
        dbc.getCollection(nameCollection).insertOne(document);
    }

    public void insertManyDocuments(String nameCollection, List<Document> documents){
        dbc.getCollection(nameCollection).insertMany(documents);
    }
}
