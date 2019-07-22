package helpers;

import java.io.*;

public class EventFilesManager {
    public static void write(String eventId, String eventJson){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("eventFiles/" + eventId + ".json"));
            bw.write(eventJson);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String eventId){
        String eventJson = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader("eventFiles/" + eventId + ".json"));
            eventJson = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventJson;
    }
}
