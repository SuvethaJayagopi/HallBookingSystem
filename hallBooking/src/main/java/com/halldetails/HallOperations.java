/*
* The HallOperations class implements an application that 
* simply have the operation of hall and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 19 FEB 2024
*
* 
*/
package com.halldetails;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;

public class HallOperations {

	public void addHall(Hall hall) {
	    if (isHallExists(hall.getName())) {
	        System.out.println("Hall with the same name already exists. Cannot add.");
	        return;
	    }

	    String sql = "INSERT INTO SUVETHA.halls (name, capacity, price_per_hour, facilities, event_type, availability, location) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try (Connection connection = ConnectionClass.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, hall.getName());
	        statement.setInt(2, hall.getCapacity());
	        statement.setDouble(3, hall.getPricePerHour());
	        statement.setString(4, hall.getFacilities());
	        statement.setString(5, hall.getEventType().toString());
	        statement.setString(6, "available");
	        statement.setString(7, hall.getLocation()); // Set the location

	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected == 1) {
	            System.out.println(Colors.BLUE+"Hall added successfully."+Colors.RESET);
	        } else {
	            System.out.println(Colors.RED+"Failed to add hall."+Colors.RESET);
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED+"Error adding hall: " + e.getMessage()+Colors.RESET);
	    }
	}

	
	public static void updateHallDetails(int hallId, Hall updatedHall) {
	    String sql = "UPDATE SUVETHA.halls SET name = ?, capacity = ?, price_per_hour = ?, facilities = ?, event_type = ?, availability = ?, location = ? WHERE id = ?";
	    try (Connection connection = ConnectionClass.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, updatedHall.getName());
	        statement.setInt(2, updatedHall.getCapacity());
	        statement.setDouble(3, updatedHall.getPricePerHour());
	        statement.setString(4, updatedHall.getFacilities());
	        statement.setString(5, updatedHall.getEventType().toString());
	        statement.setString(6, "available");
	        statement.setString(7, updatedHall.getLocation()); // Set the location
	        statement.setInt(8, hallId);

	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected == 1) {
	            System.out.println(Colors.BLUE+"Hall details updated successfully."+Colors.RESET);
	        } else {
	            System.out.println(Colors.RED+"Failed to update hall details."+Colors.RESET);
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED+"Error updating hall details: " + e.getMessage()+Colors.RESET);
	    }
	}



	public List<Hall> getAllHalls() {
	    List<Hall> halls = new ArrayList<>();
	    String sql = "SELECT * FROM SUVETHA.halls";
	    try (Connection connection = ConnectionClass.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String name = resultSet.getString("name");
	            int capacity = resultSet.getInt("capacity");
	            double pricePerHour = resultSet.getDouble("price_per_hour");
	            String facilities = resultSet.getString("facilities");
	            String eventTypeStr = resultSet.getString("event_type").toUpperCase();
	            EventType eventType = EventType.fromString(eventTypeStr);
	            String availability = resultSet.getString("availability");
	            String location = resultSet.getString("location"); // Retrieve location
	            Hall hall = new Hall(name, capacity, pricePerHour, facilities, eventType, location); // Include location
	            hall.setId(id);
	            hall.setAvailability(availability); // Set availability
	            halls.add(hall);
	        }
	    } catch (SQLException e) {
	        System.out.println(Colors.RED+"Error retrieving halls: " + e.getMessage()+Colors.RESET);
	        // You might want to throw an exception or handle this error more appropriately
	    }
	    return halls;
	}



	public void displayAllHalls(List<Hall> halls) {
	    System.out.println("----------------------------------------------------------------------------------------------------------------------------");
	    System.out.printf("| %-5s | %-20s | %-10s | %-15s | %-15s | %-15s | %-13s | %-7s |\n", "ID", "Name", "Capacity", "Price per Hour", "Facilities", "Event Type", "Availability", "Location");
	    System.out.println("----------------------------------------------------------------------------------------------------------------------------");
	    for (Hall hall : halls) {
	        System.out.printf("| %-5d | %-20s | %-10d | $%-14.2f | %-15s | %-15s | %-13s | %-7s |\n", hall.getId(), hall.getName(), hall.getCapacity(), hall.getPricePerHour(), hall.getFacilities(), hall.getEventType(), hall.getAvailability(), hall.getLocation());
	    }
	    System.out.println("----------------------------------------------------------------------------------------------------------------------------");
	}






    public void deleteHall(int hallId) {
        String sql = "DELETE FROM SUVETHA.halls WHERE ID = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hallId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println(Colors.BLUE+"Hall deleted successfully."+Colors.RESET);
            } else {
                System.out.println(Colors.RED+"Failed to delete hall."+Colors.RESET);
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error deleting hall: " + e.getMessage()+Colors.RESET);
        }
    }

    public boolean isHallAvailable(int hallId, LocalDateTime startDate, LocalDateTime endDate) {
        boolean isAvailable = true;
        String sql = "SELECT COUNT(*) FROM SUVETHA.bookings WHERE hall_id = ? AND ((start_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?))";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hallId);
            statement.setTimestamp(2, Timestamp.valueOf(startDate));
            statement.setTimestamp(3, Timestamp.valueOf(endDate));
            statement.setTimestamp(4, Timestamp.valueOf(startDate));
            statement.setTimestamp(5, Timestamp.valueOf(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        isAvailable = false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error checking hall availability: " + e.getMessage()+Colors.RESET);
        }
        return isAvailable;
    }
    
    private boolean isHallExists(String hallName) {
        String sql = "SELECT COUNT(*) FROM SUVETHA.halls WHERE name = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hallName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error checking hall existence: " + e.getMessage()+Colors.RESET);
        }
        return false;
    }
    
    public boolean isHallIdValid(int hallId) {
        String sql = "SELECT COUNT(*) FROM SUVETHA.halls WHERE ID = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hallId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error checking hall ID validity: " + e.getMessage() + Colors.RESET);
        }
        return false;
    }

}
