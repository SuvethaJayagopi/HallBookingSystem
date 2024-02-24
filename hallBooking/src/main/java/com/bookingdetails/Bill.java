/*
* The Bill class implements an application that 
* simply have the attributes and constructor of Bill
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.bookingdetails;

import java.time.LocalDateTime;

public class Bill {
    private int billId;
    private int bookingId;
    private int userId;
    private LocalDateTime generatedTime;
    private double totalAmount;

    // Constructor
    public Bill(int billId, int bookingId, int userId, LocalDateTime generatedTime, double totalAmount) {
        this.billId = billId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.generatedTime = generatedTime;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(LocalDateTime generatedTime) {
        this.generatedTime = generatedTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Calculate total amount based on the duration and price per hour
    public static double calculateTotalAmount(LocalDateTime startTime, LocalDateTime endTime, double pricePerHour) {
        long duration = java.time.Duration.between(startTime, endTime).toHours();
        return duration * pricePerHour;
    }

    // Generate a bill
    public static Bill generateBill(int billId, int bookingId, int userId, LocalDateTime generatedTime, double totalAmount) {
        return new Bill(billId, bookingId, userId, generatedTime, totalAmount);
    }
}
