package services;

import application.Session;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import models.Institution;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static helpers.InstitutionAuth.encryptPassword;

public class InstitutionService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection(
            "institutions");

    public boolean insertInstitution(Institution newInstitution){
        if(findByEmail(newInstitution.getEmail()) == null) {
            newInstitution.setPassword(encryptPassword(newInstitution.getPassword()));
            institutionCollection.insertOne(Document.parse(new Gson().toJson(newInstitution)));
            return true;
        }
        return false;
    }

    public void updateInstitution(Institution updatedInstitution){
        updatedInstitution.setPassword(updatedInstitution.getPassword());
        institutionCollection.replaceOne(eq("email", Session.getInstance().getInstitution().getEmail()),
                Document.parse(new Gson().toJson(updatedInstitution)));
    }

    public Institution findByEmail(String institutionEmail){
        Document institutionDocument = institutionCollection.find(eq("email", institutionEmail)).first();
        return institutionDocument == null ?
                null : new Gson().fromJson(institutionDocument.toJson(), Institution.class);
    }

    void updateSessionInstitution(){
        String institutionEmail = Session.getInstance().getInstitution().getEmail();

        Institution updatedInstitution = findByEmail(institutionEmail);
        Session.getInstance().setInstitution(updatedInstitution);
    }
}
