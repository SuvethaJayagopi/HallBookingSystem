/*
* The Cash class implements an application that 
* simply have the operation of Cash 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 21 FEB 2024
*
* 
*/
// Cash subclass
package com.paymentdetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;
import com.userdetails.User;


public class Cash extends Payment {
    private double totalAmount; // Store the total amount
    private String username; // Store the username

    public Cash(double totalAmount, String username) {
        super(totalAmount); // Call the superclass constructor with the totalAmount
        this.totalAmount = totalAmount; // Store the totalAmount
        this.username = username; // Store the username
    }

    // Method to indicate cash payment can be made within 48 hours
    public void makePayment() {
        System.out.println(Colors.BLUE + "You can pay the cash within 48 hours." + Colors.RESET);
        System.out.println("Total Amount: $" + totalAmount); // Display total amount
        System.out.println(Colors.PURPLE + "Thank you!!!" + Colors.RESET);
        PaymentOperations.insertPayment(this);

    }

    // Method to retrieve the user ID based on the current user's username



}
