/*
* The BillingOperations class implements an application that 
* simply have the operation of Billing and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.bookingdetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;

public class BillingOperations {
    public static double calculateTotalAmount(LocalDateTime startTime, LocalDateTime endTime, int hallId) {
        // Fetch price per hour from the hall table
        double pricePerHour = getPricePerHour(hallId);
        if (pricePerHour == -1) {
            // Handle if price per hour is not found
            return -1;
        }

        // Calculate duration in hours
        long duration = Duration.between(startTime, endTime).toHours();

        // Calculate total amount
        return duration * pricePerHour;
    }

    private static double getPricePerHour(int hallId) {
        double pricePerHour = -1; // Default value if not found
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT price_per_hour FROM SUVETHA.halldetails WHERE hall_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, hallId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        pricePerHour = resultSet.getDouble("price_per_hour");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error fetching price per hour: " + e.getMessage()+Colors.RESET);
        }
        return pricePerHour;
    }
    
    public static double showBilling(Scanner scanner) {
        BookingOperations bookingOperations = new BookingOperations();

        int bookingId;
        while (true) {
            try {
                System.out.println("Enter your booking ID: ");
                String input = scanner.nextLine();
                
                // Check if the input is an integer
                if (!input.matches("\\d+")) {
                    System.out.println("Error: Booking ID must be an integer. Please try again.\033[0m");
                    continue;
                }
                
                bookingId = Integer.parseInt(input);
                
                // Check if the entered Booking ID is valid
                if (!bookingOperations.isBookingIdValid(bookingId)) {
                    System.out.println("Error: Booking ID does not exist. Please try again.\033[0m");
                    continue;
                }
                
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Booking ID must be a valid integer. Please try again.\033[0m");
            }
        }

        // Retrieve booking details from the database
        Booking booking = null;
        try {
            booking = BookingOperations.getBookingById(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (booking == null) {
            System.out.println(Colors.YELLOW + "Booking not found." + Colors.RESET);
            return -1; // Return -1 to indicate an error
        }
        // Calculate total amount
        double totalAmount = BillingOperations.calculateTotalAmount(booking.getStartTime(), booking.getEndTime(), booking.getHallId());
        if (totalAmount == -1) {
            System.out.println(Colors.RED+"Error calculating total amount. Please try again later."+Colors.RESET);
            return -1; // Return -1 to indicate an error
        }

        // Display billing details
        System.out.println("Billing Details:");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Total Amount: $" + totalAmount);

        // Display billing details in invoice format
        BillingOperations.displayInvoice(booking, totalAmount);

        // Return the total amount
        return totalAmount;
    }



    public static void displayInvoice(Booking booking, double totalAmount) {
        // Display billing details in invoice format
        System.out.println("******************* Invoice *******************");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("-------------------------------------------------");
        System.out.println("Hall ID: " + booking.getHallId());
        System.out.println("-------------------------------------------------");
        System.out.println("Start Time: " + booking.getStartTime());
        System.out.println("End Time: " + booking.getEndTime());
        System.out.println("-------------------------------------------------");
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("*************************************************");
    }
}
