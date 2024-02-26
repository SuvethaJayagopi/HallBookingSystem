package com.notificationdetails;

public class SMSNotification extends Notification {

    public SMSNotification(int notificationId, String message) {
        super(notificationId, message);
    }

    @Override
    public void sendNotification() {
        // Logic to send SMS notification
        System.out.println("Sending SMS Notification : " + getMessage());
    }

    @Override
    public void sendCancellationNotification() {
        // Logic to send cancellation notification for SMS
        System.out.println("SMS Notification (ID: " + getNotificationId() + ") cancelled.");
    }
}
