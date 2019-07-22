package helpers;

import application.Session;
import models.Institution;
import org.apache.commons.codec.binary.Hex;
import services.InstitutionService;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class InstitutionAuth {
    private InstitutionService institutionService = new InstitutionService();

    public boolean login(String email, String password) {
        Institution requestedInstitution = institutionService.findByEmail(email);

        if(requestedInstitution != null){
            String encryptedPassword = encryptPassword(password);

            if(requestedInstitution.getPassword().equals(encryptedPassword)) {
                Session.getInstance().setInstitution(requestedInstitution);
                return true;
            }
        }
        return false;
    }

    public static String encryptPassword(String originalPassword) {
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
