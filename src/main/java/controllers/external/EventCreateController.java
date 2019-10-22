package controllers.external;

import com.google.gson.Gson;
import com.jfoenix.controls.*;
import controllers.home.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Event;
import models.Gender;
import models.Modality;
import services.EventService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class EventCreateController implements Initializable {
    public JFXChipView chipModalities;
    public JFXComboBox cbxModalities;
    public JFXCheckBox chbMale;
    public JFXCheckBox chbFemale;
    public JFXCheckBox chbMixed;
    public HBox paneGenders;
    public JFXTextField txtName;

    private Event newEvent;
    private EventService eventService;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        newEvent = new Event();
        eventService = new EventService();
    }

    @FXML
    protected void appendModalities(){
        if(!chipModalities.getChips().isEmpty()){
            chipModalities.setDisable(true);
            cbxModalities.setItems(chipModalities.getChips());
            generateModalities();
        } else
            HomeController.showToastMessage(strings.getString("modalitiesFirst"));
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
        String genderName = checkBox.equals(chbMale) ? "male" : checkBox.equals(chbFemale) ? "female" : "mixed";

        if(checkBox.isSelected())
            findModalityByName().getGenders().add(new Gender(genderName));
        else
            removeGender(checkBox.getText());
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
        final int NUMBER_OF_GENDERS = 3;
        IntStream.range(0, NUMBER_OF_GENDERS).forEach(this::selectGender);
    }

    private void selectGender(int genderNameIndex){
        final List<String> GENDER_NAMES = Arrays.asList("male", "female", "mixed");

        JFXCheckBox chbGender = (JFXCheckBox) paneGenders.getChildren().get(genderNameIndex);
        chbGender.setSelected(findGenderByName(GENDER_NAMES.get(genderNameIndex)) != null);
    }

    private Gender findGenderByName(String genderName){
        return findModalityByName().getGenders().stream().filter(gender ->
                gender.getName().equals(genderName)).findFirst().orElse(null);
    }

    @FXML
    protected void saveEvent(ActionEvent event){
        boolean validEvent = !txtName.getText().isEmpty() && chipModalities.isDisable()
                && !modalitiesWithEmptyGenders();

        if(validEvent){
            newEvent.setName(txtName.getText());
            String eventJson = new Gson().toJson(newEvent);
            eventService.insertEventInCollection(eventJson);

            HomeController.loadEventListView();
            getActualStage(event).close();
            HomeController.showToastMessage(strings.getString("successEventCreation"));
        } else
            HomeController.showToastMessage(strings.getString("error.emptyFields"));
    }

    private boolean modalitiesWithEmptyGenders(){
        for (Modality modality : newEvent.getModalities())
            if(modality.getGenders().isEmpty()) return true;
        return false;
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
