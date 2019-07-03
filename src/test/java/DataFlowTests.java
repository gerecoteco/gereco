import org.junit.Test;
import services.EventService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataFlowTests {
    private EventService eventService = new EventService();

    @Test
    public void updateBackup(){
        eventService.requestOneEvent("5d0d49f5ff94054db4198a83");
    }

    @Test
    public void readBackup(){
        eventService.requestOneEvent("5d0d49f5ff94054db4198a83");
    }

    @Test
    public void insertNewEvent(){
        eventService.insertOneEvent(readAndReturnJson("event"), "5d1bd7803b502a069dea677b");
    }

    private String readAndReturnJson(String path){
        String jsonEvent = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/test/resources/" + path + ".json"));
            jsonEvent = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonEvent;
    }
}
