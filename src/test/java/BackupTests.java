import org.junit.Test;
import services.EventService;

public class BackupTests {
    @Test
    public void updateBackup(){
        EventService mongoCollections = new EventService();
        mongoCollections.requestOne("5d0d49f5ff94054db4198a83");
    }

    @Test
    public void readBackup(){
        EventService mongoCollections = new EventService();
        mongoCollections.updateOne("5d0d49f5ff94054db4198a83");
    }
}
