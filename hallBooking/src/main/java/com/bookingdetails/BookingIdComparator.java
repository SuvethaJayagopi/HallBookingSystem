package com.bookingdetails;

import java.util.Comparator;

public class BookingIdComparator implements Comparator<Booking> {
    @Override
    public int compare(Booking booking1, Booking booking2) {
        // Compare bookings based on their IDs
        return booking1.getId() - booking2.getId();
    }
}
