/*
* The Admin class implements an application that 
* simply have the operation and functions of admin and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 18 FEB 2024
*
* 
*/
package com.userdetails;

import java.sql.Connection;
import com.hallbookingdriver.HallIdComparator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;
import com.hallbookingdriver.HallViewer;
import com.halldetails.EventType;
import com.halldetails.Hall;
import com.halldetails.HallOperations;
import com.bookingdetails.Booking;
import com.bookingdetails.BookingOperations;
import com.bookingdetails.BookingStatus;

public class Admin extends Person implements HallViewer {

    private HallOperations hallOperations;
    private BookingOperations bookingOperations;


    public Admin() {
        super("", "", "", "", Role.ADMIN);
        this.hallOperations = new HallOperations();// Provide default values or modify as needed
        this.bookingOperations = new BookingOperations();
    }

    public Admin(int admin_id, String username, String password, String name, String email, Role role) {
        super(username, password, name, email, role);
    }


    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        String sql = "SELECT * FROM SUVETHA.Admin WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, adminUsername);
            preparedStatement.setString(2, adminPassword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    setUsername(resultSet.getString("username"));
                    setPassword(resultSet.getString("password"));
                    setName(resultSet.getString("name"));
                    return true; // Admin login successful
                } else {
                    System.out.println(Colors.RED+"Admin login failed. Invalid username or password."+Colors.RESET);
                    return false; // Admin login failed
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error: " + e.getMessage()+Colors.RESET);
            return false; // Admin login failed due to database error
        }
    }

    public boolean register() {
        Scanner scanner = new Scanner(System.in);

        // Enter username
        String newUsername;
        boolean isValidUsername = false;

        do {
            do {
                System.out.print("Enter new admin username: ");
                newUsername = scanner.nextLine();
                if (newUsername.isEmpty()) {
                    System.out.println(Colors.YELLOW+"Username is required. Please enter a username."+Colors.RESET);
                }
            } while (newUsername.isEmpty());

            // Validate username
            if (!validateUsername(newUsername)) {
                System.out.println(Colors.RED+"Invalid username. Username must consist of alphanumeric characters and underscores, with a length between 3 and 50 characters."+Colors.RESET);
                continue; // Continue to prompt for username if it's invalid
            }

            // Check if the username already exists in the database
            if (checkIfUsernameExists(newUsername)) {
                System.out.println(Colors.YELLOW+"Username already exists. Please choose a different username."+Colors.RESET);
            } else {
                isValidUsername = true; // Set the flag to true if the username is valid and does not exist
            }
        } while (!isValidUsername);

        // Enter password
        String newPassword;
        boolean isValidPassword = false;

        do {
            do {
                System.out.print("Enter new admin password: ");
                newPassword = scanner.nextLine();
                if (newPassword.isEmpty()) {
                    System.out.println(Colors.YELLOW+"Password is required. Please enter a password."+Colors.RESET);
                }
            } while (newPassword.isEmpty());

            // Validate password
            if (!validatePassword(newPassword)) {
                System.out.println(Colors.RED+"Invalid password. Password must be at least 8 characters long and may contain letters, digits, and special characters (@, #, _)."+Colors.RESET);
            } else {
                isValidPassword = true; // Set the flag to true if the password is valid
            }
        } while (!isValidPassword);

        
        String newName;
        do {
            System.out.print("Enter admin name: ");
            newName = scanner.nextLine();
            if (newName.isEmpty()) {
                System.out.println(Colors.YELLOW + "Name is required. Please enter a name." + Colors.RESET);
            } else if (!validateName(newName)) {
                System.out.println(Colors.RED + "Invalid name. Name should contain only alphabets." + Colors.RESET);
            }
        } while (!validateName(newName));


        // Enter email
        String newEmail;
        do {
            System.out.print("Enter admin email: ");
            newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) {
                System.out.println(Colors.YELLOW+"Email is required. Please enter an email address."+Colors.RESET);
            } else if (!validateEmail(newEmail)) {
                System.out.println(Colors.RED+"Invalid email address. Please enter a valid email address."+Colors.RESET);
            }
        } while (newEmail.isEmpty() || !validateEmail(newEmail));

        // Connect to the database and insert the new admin
        try {
            Connection connection = ConnectionClass.getConnection();
            if (connection != null) {
                String sql = "INSERT INTO SUVETHA.Admin (username, password, name, email, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, newName);
                preparedStatement.setString(4, newEmail);
                preparedStatement.setString(5, "ADMIN"); // Assuming 'ADMIN' is the role for administrators
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println(Colors.BLUE+"Admin registration successful for username: " + newUsername+Colors.RESET);
                    return true;
                } else {
                    System.out.println(Colors.RED+"Failed to register admin."+Colors.RESET);
                }
                preparedStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error connecting to the database: " + e.getMessage()+Colors.RESET);
        }
        return false;
    }

    private boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_@#]{8,50}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validateEmail(String email) {
        // Regular expression for email validation
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    private static boolean validateName(String name) {
        // Regular expression pattern for name validation (only alphabets, no numbers)
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }


    private boolean checkIfUsernameExists(String username) {
        String sql = "SELECT COUNT(*) AS count FROM SUVETHA.Admin WHERE username = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // If count is greater than 0, username already exists
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error checking if username exists: " + e.getMessage()+Colors.RESET);
        }
        return false; // Default to false in case of any error
    }

    
    public boolean updateProfile() {
        Scanner scanner = new Scanner(System.in);

        // Enter new details
        System.out.println("Update Profile:");
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String newPhoneNumber = scanner.nextLine();
        System.out.print("Enter new address: ");
        String newAddress = scanner.nextLine();

        // Update the user's details in the database
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "UPDATE SUVETHA.Users SET name = ?, email = ?, phoneNumber = ?, address = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newEmail);
                preparedStatement.setString(3, newPhoneNumber);
                preparedStatement.setString(4, newAddress);
                preparedStatement.setString(5, getUsername());

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println(Colors.BLUE+"User profile updated successfully."+Colors.RESET);
                    setName(newName);
                    setEmail(newEmail);
                    setPhoneNumber(newPhoneNumber);
                    setAddress(newAddress);
                    return true;
                } else {
                    System.out.println(Colors.RED+"Failed to update user profile."+Colors.RESET);
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error updating user profile: " + e.getMessage()+Colors.RESET);
        }
        return false;
    }
    

    public void addHall() {
        Scanner scanner = new Scanner(System.in);

        // Get hall details from the admin
        System.out.print("Enter hall name: ");
        String name = scanner.nextLine();
        
        int capacity;
        while (true) {
            System.out.print("Enter hall capacity: ");
            String capacityInput = scanner.nextLine();
            try {
                capacity = Integer.parseInt(capacityInput);
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("\033[31mError: Capacity must be a valid integer. Please try again.\033[0m");
            }
        }
        
        double pricePerHour;
        while (true) {
            System.out.print("Enter price per hour: ");
            String priceInput = scanner.nextLine();
            try {
                pricePerHour = Double.parseDouble(priceInput);
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("\033[31mError: Price must be a valid number. Please try again.\033[0m");
            }
        }
        
        System.out.print("Enter facilities: ");
        String facilities = scanner.nextLine();

        // Prompt for event type until a valid one is provided
        EventType eventType = null;
        while (eventType == null) {
            System.out.print("Enter event type (WEDDING, BIRTHDAY, CONFERENCE, CONCERT, EXHIBITION): ");
            String eventTypeStr = scanner.nextLine().toUpperCase(); // Convert input to uppercase
            try {
                eventType = EventType.fromString(eventTypeStr); // Get EventType enum from string
            } catch (IllegalArgumentException e) {
                System.out.println(Colors.RED + "Invalid event type. Please enter a valid event type." + Colors.RESET);
            }
        }
        
        System.out.print("Enter location: ");
        String location = scanner.nextLine();

        // Create Hall object with the provided details
        Hall hall = new Hall(name, capacity, pricePerHour, facilities, eventType,location);

        // Add the hall using HallOperations
        hallOperations.addHall(hall);
    }


	public static void updateHallDetails() {
	    Scanner scanner = new Scanner(System.in); // Declare and initialize scanner locally

	    System.out.print("Enter the ID of the hall you want to update: ");
	    int hallId = scanner.nextInt();
	    scanner.nextLine(); // Consume newline

	    // Retrieve updated details from the admin
	    Hall updatedHall = retrieveUpdatedHallDetails();

	    // Call the HallOperations method to update hall details
	    HallOperations hallOperations = new HallOperations();
	    hallOperations.updateHallDetails(hallId, updatedHall);

	    scanner.close(); // Close the scanner when done
	}

	
	private static Hall retrieveUpdatedHallDetails() {
	    Scanner scanner = new Scanner(System.in);
	    
	    System.out.print("Enter the updated name of the hall: ");
	    String name = scanner.nextLine();

	    System.out.print("Enter the updated capacity of the hall: ");
	    int capacity = scanner.nextInt();
	    scanner.nextLine(); // Consume newline

	    System.out.print("Enter the updated price per hour: ");
	    double pricePerHour = scanner.nextDouble();
	    scanner.nextLine(); // Consume newline

	    System.out.print("Enter the updated facilities: ");
	    String facilities = scanner.nextLine();

	    System.out.print("Enter the updated event type: ");
	    String eventTypeStr = scanner.nextLine().toUpperCase(); // Assuming eventTypeStr is a string representation of EventType enum
	    EventType eventType = EventType.fromString(eventTypeStr); // Convert string to enum

	    System.out.print("Enter location: ");
        String location = scanner.nextLine();
	    // Create and return a new Hall object with the updated details
	    return new Hall(name, capacity, pricePerHour, facilities, eventType, location);
	}


	public void deleteHall() {
	    Scanner scanner = new Scanner(System.in);
	    HallOperations hallOperations = new HallOperations();

	    int hallId;
	    while (true) {
	        try {
	            System.out.print("Enter hall ID to delete: ");
	            String input = scanner.nextLine();
	            
	            // Check if the input is an integer
	            if (!input.matches("\\d+")) {
	                System.out.println("Error: Hall ID must be an integer. Please try again.\033[0m");
	                continue;
	            }
	            
	            hallId = Integer.parseInt(input);
	            
	            // Check if the entered Hall ID exists and is available
	            if (!hallOperations.isHallIdValid(hallId)) {
	                System.out.println("Error: Hall ID does not exist or is not available. Please try again.\033[0m");
	                continue;
	            }
	            
	            break; // Exit the loop if input is valid
	        } catch (NumberFormatException e) {
	            System.out.println("Error: Hall ID must be a valid integer. Please try again.\033[0m");
	        }
	    }
        // Delete the hall using HallOperations
        hallOperations.deleteHall(hallId);
    }

    public void displayAllHalls() {
        // Retrieve and display all halls using HallOperations
        System.out.println("All Halls:");
        hallOperations.getAllHalls().forEach(System.out::println);
    }
    
   
    @Override
    public void viewAllHalls() {
        System.out.println("**********The Available Halls**********");
        HallOperations hallOperations = new HallOperations();
        List<Hall> halls = hallOperations.getAllHalls();
        Collections.sort(halls, new HallIdComparator());
        hallOperations.displayAllHalls(halls);
    }


    public void viewPendingBookings() {
        System.out.println("Pending Bookings:");
        bookingOperations.getPendingBookings().forEach(System.out::println);
    }

    public static void viewAllBookings() {
        BookingOperations bookingOperations = new BookingOperations();
        List<Booking> allBookings = bookingOperations.getAllBookings();
        System.out.println("**********All Bookings**********");
        bookingOperations.displayAllBookings(allBookings);
    }


    
    public boolean approveBooking() {
        Scanner scanner = new Scanner(System.in);
        BookingOperations bookingOperations = new BookingOperations();

        int bookingId;
        while (true) {
            try {
                System.out.print("Enter booking ID to approve: ");
                String input = scanner.nextLine();
                
                // Check if the input is an integer
                if (!input.matches("\\d+")) {
                    System.out.println("Error: Booking ID must be an integer. Please try again.\033[0m");
                    continue;
                }
                
                bookingId = Integer.parseInt(input);
                
                // Check if the entered Booking ID exists
                if (bookingOperations.getBookingById(bookingId) == null) {
                    System.out.println("Error: Booking with ID " + bookingId + " not found. Please try again.\033[0m");
                    continue;
                }
                
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Booking ID must be a valid integer. Please try again.\033[0m");
            }
        }

        // Approve the booking using BookingOperations
        Booking booking = bookingOperations.getBookingById(bookingId);
        boolean approved = bookingOperations.approveBookingRequest(booking);
        if (approved) {
            System.out.println(Colors.BLUE + "Booking with ID " + bookingId + " has been approved successfully." + Colors.RESET);
            return true;
        } else {
            System.out.println(Colors.RED + "Failed to approve booking with ID " + bookingId + "." + Colors.RESET);
            return false;
        }
    }




    public boolean rejectBooking() {
        Scanner scanner = new Scanner(System.in);
        BookingOperations bookingOperations = new BookingOperations();

        int bookingId;
        while (true) {
            try {
                System.out.print("Enter booking ID to reject: ");
                String input = scanner.nextLine();
                
                // Check if the input is an integer
                if (!input.matches("\\d+")) {
                    System.out.println("Error: Booking ID must be an integer. Please try again.\033[0m");
                    continue;
                }
                
                bookingId = Integer.parseInt(input);
                
                // Check if the entered Booking ID exists
                if (bookingOperations.getBookingById(bookingId) == null) {
                    System.out.println("Error: Booking with ID " + bookingId + " not found. Please try again.\033[0m");
                    continue;
                }
                
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Booking ID must be a valid integer. Please try again.\033[0m");
            }
        }

        // Reject the booking using BookingOperations
        Booking booking = bookingOperations.getBookingById(bookingId);
        boolean rejected = bookingOperations.rejectBookingRequest(booking);
        if (rejected) {
            System.out.println(Colors.BLUE + "Booking with ID " + bookingId + " has been rejected successfully." + Colors.RESET);
            return true;
        } else {
            System.out.println(Colors.RED + "Failed to reject booking with ID " + bookingId + "." + Colors.RESET);
            return false;
        }
    }

    
    public void logout() {
        System.out.println(Colors.PURPLE+"Logging out..."+Colors.RESET);
    }

}

