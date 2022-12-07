package com.projectmanager.teamup.Modal;

public class CardModal {
    String TVTitle;
    String Description;

    CardModal() {
        //for firebase
    }

    public CardModal(String TVTitle, String Description) {
        this.TVTitle = TVTitle;
        this.Description = Description;
    }

    public String getTVTitle() {
        return TVTitle;
    }

    public void setTVTitle(String TVTitle) {
        this.TVTitle = TVTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}
