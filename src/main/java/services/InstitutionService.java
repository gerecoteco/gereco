package services;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import models.Institution;
import org.apache.commons.codec.binary.Hex;
import org.bson.Document;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.print.Doc;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class InstitutionService {
    private MongoConnection mongoConnection = new MongoConnection();
    private MongoCollection<Document> institutionCollection = mongoConnection.getCollection("institution");

    public void insertOneInstitution(Institution newInstitution){
        newInstitution.setPassword(encryptPassword(newInstitution.getPassword()));
        institutionCollection.insertOne(Document.parse(new Gson().toJson(newInstitution)));
    }

    public String encryptPassword(String originalPassword) {
        String hashedPassword;
        char[] passwordChar = originalPassword.toCharArray();
        byte[] saltBytes = "1234".getBytes();

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(passwordChar, saltBytes, 10000, 512);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();

            hashedPassword = Hex.encodeHexString(res);
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException(e);
        }
        return hashedPassword;
    }
}
