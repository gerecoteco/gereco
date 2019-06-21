import org.junit.Test;
import services.MongoCollections;

public class BackupTests {
    @Test
    public void updateBackup(){
        MongoCollections mongoCollections = new MongoCollections();
        mongoCollections.requestEvent("name", "Interclasse");
    }

    @Test
    public void readBackup(){
        MongoCollections mongoCollections = new MongoCollections();
        mongoCollections.insertEvent();
    }
}
