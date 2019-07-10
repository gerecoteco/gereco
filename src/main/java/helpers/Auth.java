package helpers;

import application.Session;
import models.Institution;
import services.InstitutionService;

public class Auth {
    private InstitutionService institutionService = new InstitutionService();

    public String login(Institution credentials) {
        Institution requestInstitution = institutionService.requestInstitution(credentials.getEmail());
        String credentialsPassword = Encrypt.encryptPassword(credentials.getPassword());

        if(requestInstitution != null){
            if(requestInstitution.getPassword().equals(credentialsPassword)) {
                Session.getInstance().setInstitution(requestInstitution);
                return "Login efetuado com sucesso!";
            }
        }
        return "Falha ao efetuar login";
    }
}
