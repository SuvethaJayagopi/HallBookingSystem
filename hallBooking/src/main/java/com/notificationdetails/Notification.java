package com.notificationdetails;

public abstract class Notification implements Runnable {
    private int notificationId;
    private String message;

    public Notification(int notificationId, String message) {
        this.notificationId = notificationId;
        this.message = message;
    }

    // Getter and setter methods for notificationId and message

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    // Method to send notification
    public abstract void sendNotification();

    // Optional method for cancellation notification
    public void sendCancellationNotification() {
        // Default implementation for cancellation notification
        System.out.println("Notification (ID: " + notificationId + ") cancelled.");
    }
    
    @Override
    public void run() {
        sendNotification();
    }
}
