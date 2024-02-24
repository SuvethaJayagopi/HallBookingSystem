package com.notificationdetails;

public class EmailNotification extends Notification {

    public EmailNotification(int notificationId, String message) {
        super(notificationId, message);
    }

    @Override
    public void sendNotification() {
        // Logic to send email notification
        System.out.println("Sending Email Notification (ID: " + getNotificationId() + "): " + getMessage());
    }

    @Override
    public void sendCancellationNotification() {
        // Logic to send cancellation notification for email
        System.out.println("Email Notification (ID: " + getNotificationId() + ") cancelled.");
    }
}
