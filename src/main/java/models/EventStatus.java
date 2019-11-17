package models;

import helpers.UTF8Control;

import java.util.ResourceBundle;

public enum EventStatus {
    PLANNING("planning"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    CANCELED("canceled");

    private String text;

    EventStatus(String text){
        this.text = text;
    }

    public String getText() {
        return ResourceBundle.getBundle("bundles.lang", new UTF8Control()).getString(text);
    }
}
