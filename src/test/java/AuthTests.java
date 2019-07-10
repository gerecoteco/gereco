import helpers.Auth;
import models.Institution;
import org.junit.Test;

import static helpers.Encrypt.encryptPassword;
import static junit.framework.TestCase.assertEquals;

public class AuthTests {
    private Auth auth = new Auth();

    @Test
    public void encyptPassword(){
        String encryptedPassword = "28970aab014cf552b44cf480a4e9a9db47cfc1d6ecd29d8a23a8b4a0aba7395f33ca1695fde9f9dd36aa3f4eb4404c02a29454fb47632d68f16e26ffeb7bed42";
        assertEquals(encryptedPassword, encryptPassword("1234"));
    }

    @Test
    public void successLogin(){
        Institution institution = new Institution(null, "etec@etec.com", "1234", null);
        assertEquals("Login efetuado com sucesso!", auth.login(institution));
    }

    @Test
    public void failureLogin(){
        Institution institution = new Institution(null, "etec@etec.com", "123", null);
        assertEquals("Falha ao efetuar login", auth.login(institution));
    }
}
