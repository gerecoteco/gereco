import com.google.gson.Gson;
import models.Institution;
import org.junit.Test;
import services.EventService;
import services.InstitutionService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DataFlowTests {
    private EventService eventService = new EventService();
    private InstitutionService institutionService = new InstitutionService();

    @Test
    public void updateLocalEvent(){
        eventService.requestOneEvent("5d0d49f5ff94054db4198a83");
    }

    @Test
    public void readLocalEvent(){
        eventService.updateEvent("5d0d49f5ff94054db4198a83");
    }

    @Test
    public void insertNewEvent(){
        eventService.insertEvent(readAndReturnJson("event"), "5d1e81f16272763c4e85ac96");
    }

    @Test
    public void insertNewInstitution(){
        institutionService.insertInstitution(new Gson().fromJson(readAndReturnJson("institution"), Institution.class));
    }

    @Test
    public void updateInstitution(){
        institutionService.updateInstitution(new Gson().fromJson(readAndReturnJson("institution"), Institution.class), "5d1e827d0b7f8e7746315c82");
    }

    @Test
    public void requestInstitution(){
        String expectedJson = "{\"name\":\"ETEC JOÃO BELARMINO\",\"email\":\"etec@etec.com\",\"password\":\"28970aab014cf552b44cf480a4e9a9db47cfc1d6ecd29d8a23a8b4a0aba7395f33ca1695fde9f9dd36aa3f4eb4404c02a29454fb47632d68f16e26ffeb7bed42\",\"events_id\":[\"5d1e865417ec112ab6f500f9\",\"5d1e986b8b33d02853ae3599\"]}";
        assertEquals(expectedJson, new Gson().toJson(institutionService.requestInstitution("5d1e81f16272763c4e85ac96")));
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
