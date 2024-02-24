/*
* The Payment class implements an application that 
* simply have the attributes and constructor of Payment
* @author Suvetha Jayagopi(Expleo)
* @since 21 FEB 2024
*
* 
*/
package com.paymentdetails;

// Enum for payment status
enum PaymentStatus {
    PENDING, SUCCESSFUL, FAILED;
}

// Payment class with common attributes
public class Payment {
    private int id;
    private double amount;
    private PaymentStatus status;

    public Payment(double amount) {
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
