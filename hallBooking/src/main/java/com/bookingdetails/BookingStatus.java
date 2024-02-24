/*
* The BookingStatus class implements an application that 
* simply have the enum of Booking Status 
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.bookingdetails;

public enum BookingStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    // Method to convert a string to BookingStatus enum
    public static BookingStatus fromString(String statusStr) {
        statusStr = statusStr.toUpperCase(); // Convert to uppercase
        for (BookingStatus status : values()) {
            if (status.toString().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid booking status: " + statusStr);
    }
}
