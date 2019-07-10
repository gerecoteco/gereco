package services;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import models.Institution;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static helpers.Encrypt.encryptPassword;

public class InstitutionService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection("institution");

    public String insertInstitution(Institution newInstitution){
        if(requestInstitution(newInstitution.getEmail()) == null) {
            newInstitution.setPassword(encryptPassword(newInstitution.getPassword()));
            institutionCollection.insertOne(Document.parse(new Gson().toJson(newInstitution)));
            return "Cadastro efetuado com sucesso!";
        }
        return "Esse email já está cadastrado";
    }

    public void updateInstitution(Institution institution, String institutionId){
        institution.setPassword(encryptPassword(institution.getPassword()));
        institutionCollection.replaceOne(eq("_id", new ObjectId(institutionId)), Document.parse(new Gson().toJson(institution)));
    }

    public Institution requestInstitution(String institutionEmail){
        String institutionJson = institutionCollection.find(eq("email", institutionEmail)).first().toJson();
        return new Gson().fromJson(institutionJson, Institution.class);
    }
}
