/*
* The BookingOperations class implements an application that 
* simply have the operation of Booking and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.bookingdetails;

import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;
import com.userdetails.User;

public class BookingOperations {

	public List<Booking> getBookingsForUser(int userId) {
	    List<Booking> bookings = new ArrayList<>();
	    try (Connection connection = ConnectionClass.getConnection()) {
	        String sql = "SELECT b.bookingid, b.hall_id, b.start_date, b.end_date, b.status, u.username " +
	                     "FROM SUVETHA.bookings b " +
	                     "INNER JOIN SUVETHA.Usertable u ON b.user_id = u.USER_ID " +
	                     "WHERE b.user_id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, userId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    int bookingId = resultSet.getInt("bookingid");
	                    int hallId = resultSet.getInt("hall_id");
	                    LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
	                    LocalDateTime endDate = resultSet.getTimestamp("end_date").toLocalDateTime();
	                    BookingStatus status = BookingStatus.valueOf(resultSet.getString("status"));
	                    String username = resultSet.getString("username");
	                    Booking booking = new Booking(bookingId, hallId, username, startDate, endDate, status);
	                    bookings.add(booking);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED + "Error fetching bookings: " + e.getMessage() + Colors.RESET);
	    }
	    return bookings;
	}



	public static boolean cancelBooking(int bookingId) {
	    try (Connection connection = ConnectionClass.getConnection()) {
	        String sql = "UPDATE SUVETHA.bookings SET status = 'CANCELLED' WHERE bookingid = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, bookingId);
	            int rowsAffected = statement.executeUpdate();
	            return rowsAffected == 1;
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED+"Error cancelling booking: " + e.getMessage()+Colors.RESET);
	        return false;
	    }
	}

	public static boolean cancelBilling(int bookingId) {
	    boolean bookingCancelled = cancelBooking(bookingId);
	    if (bookingCancelled) {
	        System.out.println(Colors.BLUE+"Booking cancelled successfully."+Colors.RESET);
	        // Additional billing cancellation logic if needed
	    } else {
	        System.out.println(Colors.RED+"Failed to cancel booking."+Colors.RESET);
	    }
	    return bookingCancelled;
	}


	public boolean requestBooking(int hallId, LocalDateTime startDate, LocalDateTime endDate, String username) {
	    try (Connection connection = ConnectionClass.getConnection()) {
	        // Proceed with booking using the provided username
	        String insertBookingSql = "INSERT INTO SUVETHA.bookings (hall_id, username, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement insertBookingStatement = connection.prepareStatement(insertBookingSql)) {
	            insertBookingStatement.setInt(1, hallId);
	            insertBookingStatement.setString(2, username); // Use the provided username
	            insertBookingStatement.setTimestamp(3, Timestamp.valueOf(startDate));
	            insertBookingStatement.setTimestamp(4, Timestamp.valueOf(endDate));
	            insertBookingStatement.setString(5, BookingStatus.PENDING.toString());
	            
	            int rowsAffected = insertBookingStatement.executeUpdate();
	            return rowsAffected == 1;
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED + "Error requesting booking: " + e.getMessage() + Colors.RESET);
	        return false;
	    }
	}




	// Method to retrieve the user ID based on the username
	private int getUserIdByUsername(Connection connection, String username) throws SQLException {
	    String getUserSql = "SELECT USER_ID FROM SUVETHA.Usertable WHERE username = ?";
	    try (PreparedStatement getUserStatement = connection.prepareStatement(getUserSql)) {
	        getUserStatement.setString(1, username);
	        try (ResultSet userResultSet = getUserStatement.executeQuery()) {
	            if (userResultSet.next()) {
	                return userResultSet.getInt("USER_ID");
	            }
	        }
	    }
	    // User not found
	    return -1;
	}


    public boolean approveBookingRequest(Booking booking) {
        try (Connection connection = ConnectionClass.getConnection()) {
            // Check for conflicts with existing approved bookings
            if (isBookingConflict(booking)) {
                System.out.println("Booking conflicts with existing approved bookings. Cannot approve.");
                return false;
            }
            
            String updateBookingSql = "UPDATE SUVETHA.bookings SET status = ? WHERE bookingid = ?";
            String updateHallSql = "UPDATE SUVETHA.halls SET availability = 'not available' WHERE id = ?";
            
            try (PreparedStatement updateBookingStatement = connection.prepareStatement(updateBookingSql);
                 PreparedStatement updateHallStatement = connection.prepareStatement(updateHallSql)) {
                
                // Update booking status to APPROVED
                updateBookingStatement.setString(1, BookingStatus.APPROVED.toString());
                updateBookingStatement.setInt(2, booking.getBookingId());
                int bookingRowsAffected = updateBookingStatement.executeUpdate();
                
                // Update hall availability to 'not available'
                updateHallStatement.setInt(1, booking.getHallId());
                int hallRowsAffected = updateHallStatement.executeUpdate();
                
                // Return true if both updates were successful
                return bookingRowsAffected == 1 && hallRowsAffected == 1;
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error approving booking request: " + e.getMessage()+Colors.RESET);
            return false;
        }
    }

    private boolean isBookingConflict(Booking booking) {
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT * FROM SUVETHA.bookings WHERE hall_id = ? AND status = ? " +
                         "AND ((start_date < ? AND end_date > ?) OR " +
                         "(start_date < ? AND end_date > ?) OR " +
                         "(start_date >= ? AND end_date <= ?))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, booking.getHallId());
                preparedStatement.setString(2, BookingStatus.APPROVED.toString()); // Only check approved bookings
                preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getEndTime()));
                preparedStatement.setTimestamp(4, Timestamp.valueOf(booking.getStartTime()));
                preparedStatement.setTimestamp(5, Timestamp.valueOf(booking.getStartTime()));
                preparedStatement.setTimestamp(6, Timestamp.valueOf(booking.getEndTime()));
                preparedStatement.setTimestamp(7, Timestamp.valueOf(booking.getStartTime()));
                preparedStatement.setTimestamp(8, Timestamp.valueOf(booking.getEndTime()));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // If any overlapping approved booking is found, return true
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error checking booking conflict: " + e.getMessage()+Colors.RESET);
            // Log the error or handle it appropriately
        }
        // If no conflicts found or error occurred, return false
        return false;
    }




    public boolean rejectBookingRequest(Booking booking) {
        // Implement logic to reject booking requests
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "UPDATE SUVETHA.bookings SET status = ? WHERE bookingid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, BookingStatus.REJECTED.toString());
                preparedStatement.setInt(2, booking.getBookingId());
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected == 1;
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error rejecting booking request: " + e.getMessage()+Colors.RESET);
            return false;
        }
    }
    
    public void viewAllBookings() {
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT * FROM SUVETHA.bookings ORDER BY bookingid DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("All Bookings:");
                while (resultSet.next()) {
                    int bookingId = resultSet.getInt("bookingid");
                    int hallId = resultSet.getInt("hall_id");
                    int userId = resultSet.getInt("user_id");
                    LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
                    String status = resultSet.getString("status");
                    
                    // Display booking details with the correct approval status
                    System.out.println("Booking ID: " + bookingId);
                    System.out.println("Hall ID: " + hallId);
                    System.out.println("User ID: " + userId);
                    System.out.println("Start Time: " + startTime);
                    System.out.println("End Time: " + endTime);
                    System.out.println("Status: " + status); // Ensure correct status is displayed
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error viewing bookings: " + e.getMessage()+Colors.RESET);
        }
    }

    public void displayAllBookings(List<Booking> bookings) {
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-10s | %-10s | %-20s | %-20s | %-10s |\n", "Booking ID", "Hall ID", "Username", "Start Time", "End Time", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        for (Booking booking : bookings) {
            System.out.printf("| %-10d | %-10d | %-10s | %-20s | %-20s | %-10s |\n", booking.getBookingId(), booking.getHallId(), booking.getUsername(), booking.getStartTime(), booking.getEndTime(), booking.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
    }




    

    public List<Booking> getPendingBookings() {
        List<Booking> pendingBookings = new ArrayList<>();
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT * FROM SUVETHA.bookings WHERE status = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, BookingStatus.PENDING.toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookingId = resultSet.getInt("bookingid"); // Corrected column name
                        int hallId = resultSet.getInt("hall_id");
                        String username = resultSet.getString("username"); // Added username
                        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
                        LocalDateTime endDate = resultSet.getTimestamp("end_date").toLocalDateTime();
                        BookingStatus status = BookingStatus.valueOf(resultSet.getString("status"));
                        Booking booking = new Booking(bookingId, hallId, username, startDate, endDate, status); // Removed userId
                        pendingBookings.add(booking);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error fetching pending bookings: " + e.getMessage() + Colors.RESET);
        }
        return pendingBookings;
    }


    
    public static List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT * FROM SUVETHA.bookings";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookingId = resultSet.getInt("bookingid");
                    int hallId = resultSet.getInt("hall_id");
                    String username = resultSet.getString("username");
                    LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
                    LocalDateTime endDate = resultSet.getTimestamp("end_date").toLocalDateTime();
                    String statusStr = resultSet.getString("status");

                    // Convert status string to BookingStatus enum
                    BookingStatus status = BookingStatus.fromString(statusStr);

                    Booking booking = new Booking(bookingId, hallId, username, startDate, endDate, status);
                    allBookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error retrieving all bookings: " + e.getMessage() + Colors.RESET);
        }
        return allBookings;
    }





    public static Booking getBookingById(int bookingId) {
        Booking booking = null;
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT * FROM SUVETHA.bookings WHERE bookingid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, bookingId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int hallId = resultSet.getInt("hall_id");
                        String username = resultSet.getString("username");
                        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
                        LocalDateTime endDate = resultSet.getTimestamp("end_date").toLocalDateTime();
                        BookingStatus status = BookingStatus.valueOf(resultSet.getString("status"));
                        booking = new Booking(bookingId, hallId, username, startDate, endDate, status);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error fetching booking by ID: " + e.getMessage() + Colors.RESET);
        }
        return booking;
    }


        
        public boolean isBookingIdValid(int bookingId) {
            String sql = "SELECT COUNT(*) FROM SUVETHA.bookings WHERE bookingid = ?";
            try (Connection connection = ConnectionClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, bookingId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            } catch (SQLException e) {
                System.out.println(Colors.RED + "Error checking booking ID validity: " + e.getMessage() + Colors.RESET);
            }
            return false;
        }


       


    }
