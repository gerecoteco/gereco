import helpers.InstitutionAuth;
import models.Institution;
import org.junit.Test;

import static helpers.InstitutionAuth.encryptPassword;
import static junit.framework.TestCase.*;

public class InstitutionAuthTests {
    private InstitutionAuth institutionAuth = new InstitutionAuth();

    @Test
    public void encyptPassword(){
        String encryptedPassword = "28970aab014cf552b44cf480a4e9a9db47cfc1d6ecd29d8a23a8b4a0aba7395f33ca1695fde9f9" +
                "dd36aa3f4eb4404c02a29454fb47632d68f16e26ffeb7bed42";
        assertEquals(encryptedPassword, encryptPassword("1234"));
    }

    @Test
    public void successLogin(){
        String institutionEmail = "etec@etec.com";
        String institutionPassword = "1234";
        assertTrue(institutionAuth.login(institutionEmail, institutionPassword));
    }

    @Test
    public void failureLogin(){
        String institutionEmail = "etec@etec.com";
        String institutionPassword = "1234";
        assertFalse(institutionAuth.login(institutionEmail, institutionPassword));
    }
}
