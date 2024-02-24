package com.notificationdetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReminderNotification extends Notification {
    private LocalDateTime bookingDateTime;

    public ReminderNotification(int notificationId, LocalDateTime bookingDateTime) {
        super(notificationId, "");
        this.bookingDateTime = bookingDateTime;
    }

    @Override
    public void sendNotification() {
        // Calculate reminder time (24 hours before booking)
        LocalDateTime reminderTime = bookingDateTime.minusHours(24);

        // Format reminder time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedReminderTime = reminderTime.format(formatter);

        // Send reminder notification
        System.out.println("Sending Reminder Notification (ID: " + getNotificationId() + ") at " + formattedReminderTime);
        // Logic to send the reminder notification
    }

    @Override
    public void sendCancellationNotification() {
        // Implement cancellation notification for reminder
        System.out.println("Cancellation notification sent via reminder (ID: " + getNotificationId() + ").");
    }
}
