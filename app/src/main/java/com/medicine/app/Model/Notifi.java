package com.medicine.app.Model;

public class Notifi {
    private String idTopic;
    private String firstName;
    private String middleName;
    private String lastName;
    private String imageUrl;
    private String dateTopic;
    private String idNoti;
    private String not;

    public Notifi() {
    }

    public Notifi(String idTopic, String firstName, String middleName, String lastName, String imageUrl, String dateTopic, String idNoti, String not) {
        this.idTopic = idTopic;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.dateTopic = dateTopic;
        this.idNoti = idNoti;
        this.not = not;
    }

    public String getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(String idTopic) {
        this.idTopic = idTopic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateTopic() {
        return dateTopic;
    }

    public void setDateTopic(String dateTopic) {
        this.dateTopic = dateTopic;
    }

    public String getIdNoti() {
        return idNoti;
    }

    public void setIdNoti(String idNoti) {
        this.idNoti = idNoti;
    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }
}
