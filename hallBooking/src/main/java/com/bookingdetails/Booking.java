/*
* The Booking class implements an application that 
* simply have the attributes and constructor of Bill
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.bookingdetails;

import java.time.Duration;
import java.time.LocalDateTime;

import com.bookingdetails.BookingStatus;
import com.hallbookingdriver.Colors;
import com.halldetails.Hall;


// Booking class representing a booking
public class Booking {
    private int id;
    private int hallId;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private Hall hall; // Add reference to HallDetails

    // Constructor and other methods

    // Method to calculate the duration in hours
    public int getDurationInHours() {
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.toHours();
    }
    // Constructor
 // Constructor
    public Booking(int id, int hallId, String username, LocalDateTime startTime, LocalDateTime endTime, BookingStatus status) {
        this.id = id;
        this.hallId = hallId;
        this.username = username; // Add this line to set the username
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public int getBookingId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
   

    @Override
    public String toString() {
        // Adjust toString() method as needed
        return "Booking [id=" + id + ", hallId=" + hallId + ", userId=" + username+
                ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + "]";
    }
	// Other methods
    public void view() {
        System.out.println("Booking Details:");
        System.out.println("ID: " + id);
        System.out.println("Hall ID: " + hallId);
        System.out.println("User ID: " + username);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Status: " + status);
    }

    public void cancel() {
        if (status == BookingStatus.PENDING) {
            System.out.println(Colors.BLUE+"Booking cancelled successfully."+Colors.RESET);
            status = BookingStatus.CANCELLED;
        } else {
            System.out.println(Colors.RED+"Cannot cancel booking. Status is not pending."+Colors.RESET);
        }
    }

    public void request() {
        if (status == BookingStatus.PENDING) {
            System.out.println("Booking request already pending.");
        } else if (status == BookingStatus.APPROVED) {
            System.out.println("Booking already confirmed.");
        } else if (status == BookingStatus.CANCELLED) {
            System.out.println("Booking has been cancelled.");
        } else {
            System.out.println(Colors.BLUE+"Booking requested successfully."+Colors.RESET);
            status = BookingStatus.PENDING;
        }
    }
}
