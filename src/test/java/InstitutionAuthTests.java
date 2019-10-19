import helpers.InstitutionAuth;
import models.Institution;
import org.junit.Test;
import org.mockito.Mockito;
import services.InstitutionService;

import static helpers.InstitutionAuth.encryptPassword;
import static junit.framework.TestCase.*;

public class InstitutionAuthTests {
    private static final String PASSWORD = "1234";
    private static final String ENCRYPTED_PASSWORD = "28970aab014cf552b44cf480a4e9a9db47cfc1d6ecd29d8a23a8b4a0aba7395f33ca1695fde9f9" +
            "dd36aa3f4eb4404c02a29454fb47632d68f16e26ffeb7bed42";

    private InstitutionService institutionService = Mockito.mock(InstitutionService.class);

    private InstitutionAuth institutionAuth = new InstitutionAuth(institutionService);

    @Test
    public void encyptPassword(){
        assertEquals(ENCRYPTED_PASSWORD, encryptPassword(PASSWORD));
    }

    @Test
    public void successLogin(){
        String institutionEmail = "etec@etec.com";
        Institution institution = new Institution("institution name", institutionEmail, ENCRYPTED_PASSWORD);
        Mockito.when(institutionService.findByEmail(institutionEmail)).thenReturn(institution);

        assertTrue(institutionAuth.login(institutionEmail, PASSWORD));
    }

    @Test
    public void failureLogin(){
        String institutionEmail = "etec@etec.com";
        String incorrectInstitutionPassword = "12345";
        Institution institution = new Institution("institution name", institutionEmail, ENCRYPTED_PASSWORD);
        Mockito.when(institutionService.findByEmail(institutionEmail)).thenReturn(institution);

        assertFalse(institutionAuth.login(institutionEmail, incorrectInstitutionPassword));
    }
}
