package com.medicine.app.Model;

public class Comments {
    private String idTopic;
    private String firstName;
    private String middleName;
    private String lastName;
    private String comments;
    private String timeStamp;
    private String ImageView;
    private String UidUser;

    public Comments() {
    }

    public Comments(String idTopic, String firstName, String middleName, String lastName, String comments, String timeStamp, String imageView, String uidUser) {
        this.idTopic = idTopic;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.comments = comments;
        this.timeStamp = timeStamp;
        ImageView = imageView;
        UidUser = uidUser;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageView() {
        return ImageView;
    }

    public void setImageView(String imageView) {
        ImageView = imageView;
    }

    public String getUidUser() {
        return UidUser;
    }

    public void setUidUser(String uidUser) {
        UidUser = uidUser;
    }
}
