import org.junit.Test;
import services.InstitutionService;

import static junit.framework.TestCase.assertEquals;

public class AuthTests {
    @Test
    public void encyptPassword(){
        String encryptedPassword = "28970aab014cf552b44cf480a4e9a9db47cfc1d6ecd29d8a23a8b4a0aba7395f33ca1695fde9f9dd36aa3f4eb4404c02a29454fb47632d68f16e26ffeb7bed42";
        assertEquals(encryptedPassword, new InstitutionService().encryptPassword("1234"));
    }
}
