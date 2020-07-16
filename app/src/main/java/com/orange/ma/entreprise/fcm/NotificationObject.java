package com.orange.ma.entreprise.fcm;

public class NotificationObject {
    private String title;
    private String message;
    private String imageUrl;
    private String endPoint;
    private String endPointTitle;
    private String actionType;

    public NotificationObject() {
    }

    public NotificationObject(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEndPointTitle() {
        return endPointTitle;
    }

    public void setEndPointTitle(String endPointTitle) {
        this.endPointTitle = endPointTitle;
    }
}
