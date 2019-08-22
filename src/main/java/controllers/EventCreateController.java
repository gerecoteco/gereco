package controllers;

import application.Session;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Event;
import models.Gender;
import models.Modality;
import services.EventService;

import java.util.Arrays;
import java.util.List;

public class EventCreateController {
    public JFXChipView chipModalities;
    public JFXComboBox cbxModalities;
    public JFXCheckBox chbMale;
    public JFXCheckBox chbFemale;
    public JFXCheckBox chbMixed;
    public HBox paneGenders;
    public JFXTextField txtName;

    private Event newEvent;
    private EventService eventService;

    @FXML
    public void initialize() {
        newEvent = new Event();
        eventService = new EventService();
    }

    @FXML
    protected void appendModalities(){
        chipModalities.setDisable(true);
        cbxModalities.setItems(chipModalities.getChips());
        generateModalities();
    }

    private void generateModalities(){
        chipModalities.getChips().forEach(modalityName ->
                newEvent.getModalities().add(new Modality(modalityName.toString())));
    }

    @FXML
    protected void changeModality(){
        checkGenderState();
    }

    @FXML
    protected void genderChecked(ActionEvent event){
        JFXCheckBox checkBox = (JFXCheckBox) event.getSource();
        Gender newGender = new Gender(checkBox.getText());

        if(checkBox.isSelected())
           findModalityByName().getGenders().add(newGender);
        else
            removeGender(checkBox.getText());

        listGenders();
    }

    private void listGenders(){
        System.out.print(cbxModalities.getValue() + " -> ");
        findModalityByName().getGenders().forEach(gender -> System.out.print(gender.getName() + " "));
        System.out.println();
    }

    private Modality findModalityByName(){
        return newEvent.getModalities().stream().filter(modality ->
                modality.getName().equals(cbxModalities.getValue())).findFirst().orElse(null);
    }

    private void removeGender(String newGenderName){
        Gender removedGender = findGenderByName(newGenderName);
        findModalityByName().getGenders().remove(removedGender);
    }

    private void checkGenderState(){
        for(int x = 0; x < 3; x++)
           selectGender(x);
    }

    private void selectGender(int genderNameIndex){
        List<String> genderNames = Arrays.asList("Masculino", "Feminino", "Misto");

        JFXCheckBox chbGender = (JFXCheckBox) paneGenders.getChildren().get(genderNameIndex);
        chbGender.setSelected(findGenderByName(genderNames.get(genderNameIndex)) != null);
    }

    private Gender findGenderByName(String genderName){
        return findModalityByName().getGenders().stream().filter(gender ->
                gender.getName().equals(genderName)).findFirst().orElse(null);
    }

    @FXML
    protected void saveEvent(ActionEvent event){
        newEvent.setName(txtName.getText());
        String eventJson = new Gson().toJson(newEvent);
        eventService.insertEventInCollection(eventJson, Session.getInstance().getInstitution().getEmail());

        HomeController.loadView(getClass().getResource("/views/home/event-list.fxml"));
        getActualStage(event).close();
    }

    @FXML
    protected void cancelEventCreation(ActionEvent event){
        getActualStage(event).close();
    }

    private Stage getActualStage(ActionEvent event){
        Button btn = (Button) event.getSource();
        return (Stage) btn.getScene().getWindow();
    }
}
