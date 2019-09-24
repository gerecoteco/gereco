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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String validatePasswordAndReturnMessage(String password, String confirmPassword){
        String warning = null;

        if(!password.isEmpty() && !confirmPassword.isEmpty()){
            if(password.length() < 6)
                warning = "A Senha deve conter ao menos 6 caracteres!";
            else if(!passwordContainsSpecialCharacter(password))
                warning = "A Senha deve conter caracter especial!";
            else if(!passwordContainsUpperCase(password))
                warning = "A Senha deve conter letra maiúscula!";
            else if(!passwordContainsNumber(password))
                warning = "A senha deve conter número!";
            else if(!password.equals(confirmPassword))
                warning = "As senhas estão diferentes!";
        } else
            warning = "Preencha os campos!";

        return warning;
    }

    private static boolean passwordContainsSpecialCharacter(String password){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    private static boolean passwordContainsNumber(String password){
        Pattern pattern = Pattern.compile("([0-9])");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    private static boolean passwordContainsUpperCase(String password){
        return !password.toLowerCase().equals(password);
    }
}
