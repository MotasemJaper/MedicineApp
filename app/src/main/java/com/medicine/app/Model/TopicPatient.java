package com.medicine.app.Model;

public class TopicPatient {

    private String id;
    private String titleTopic;
    private String details;
    private String imageTopic;
    private String videoTopic;

    public TopicPatient() {
    }

    public TopicPatient(String id, String titleTopic, String details, String imageTopic, String videoTopic) {
        this.id = id;
        this.titleTopic = titleTopic;
        this.details = details;
        this.imageTopic = imageTopic;
        this.videoTopic = videoTopic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleTopic() {
        return titleTopic;
    }

    public void setTitleTopic(String titleTopic) {
        this.titleTopic = titleTopic;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageTopic() {
        return imageTopic;
    }

    public void setImageTopic(String imageTopic) {
        this.imageTopic = imageTopic;
    }

    public String getVideoTopic() {
        return videoTopic;
    }

    public void setVideoTopic(String videoTopic) {
        this.videoTopic = videoTopic;
    }
}
