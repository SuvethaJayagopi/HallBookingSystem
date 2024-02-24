/*
* The User class implements an application that 
* simply have the operation and functions of User and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 18 FEB 2024
*
* 
*/
package com.userdetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bookingdetails.Booking;
import com.bookingdetails.BookingOperations;
import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;
import com.hallbookingdriver.HallIdComparator;
import com.hallbookingdriver.HallViewer;
import com.halldetails.Hall;
import com.halldetails.HallOperations;


public class User extends Person implements HallViewer {
    private HallOperations hallOperations = new HallOperations();

    private String username; // Store the username
    private String password;
    public User() {
        super("", "", "", "", Role.USER); // Provide default values or modify as needed
    }

    public User(String username, String password, String name, String email, Role role, String phoneNumber, String address) {
        super(username, password, name, email, role);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user username: ");
        String userUsername = scanner.nextLine();
        System.out.print("Enter user password: ");
        String userPassword = scanner.nextLine();

        String sql = "SELECT * FROM SUVETHA.UserTable WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userUsername);
            preparedStatement.setString(2, userPassword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    setUsername(username);
                    setPassword(password);
                    setName(resultSet.getString("name")); // Set the name attribute
                    return true; // User login successful
                } else {
                    System.out.println(Colors.RED+"User login failed. Invalid username or password."+Colors.RESET);
                    return false; // User login failed
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error: " + e.getMessage()+Colors.RESET);
            return false; // User login failed due to database error
        }
    }


   
    public boolean registerUser() {
        Scanner scanner = new Scanner(System.in);

        // Enter username
        String newUsername;
        boolean isValidUsername = false;

        do {
            do {
                System.out.print("Enter new user username: ");
                newUsername = scanner.nextLine();
                if (newUsername.isEmpty()) {
                    System.out.println(Colors.YELLOW+"Username is required. Please enter a username."+Colors.RESET);
                }
            } while (newUsername.isEmpty());

            // Validate username
            if (!validateUsername(newUsername)) {
                System.out.println(Colors.RED+"Invalid username. Username must consist of alphanumeric characters and underscores, with a length between 3 and 50 characters."+Colors.RESET);
            } else if (checkIfUsernameExists(newUsername)) {
                System.out.println(Colors.YELLOW+"Username already exists. Please choose a different username."+Colors.RESET);
            } else {
                isValidUsername = true; // Set the flag to true if the username is valid and does not already exist
            }
        } while (!isValidUsername);

        // The loop will exit only when a valid and non-existing username is provided


        // Enter password
        String newPassword;

        do {
            do {
                System.out.print("Enter new user password: ");
                newPassword = scanner.nextLine();
                if (newPassword.isEmpty()) {
                    System.out.println(Colors.YELLOW+"Password is required. Please enter a password."+Colors.RESET);
                } else if (!validatePassword(newPassword)) {
                    System.out.println(Colors.RED+"Invalid password. Password must be at least 8 characters long and may contain letters, digits, and special characters (@, #, _)."+Colors.RESET);
                }
            } while (newPassword.isEmpty() || !validatePassword(newPassword));
        } while (!validatePassword(newPassword));

        // Enter name
        String newName;
        do {
            System.out.print("Enter user name: ");
            newName = scanner.nextLine();
            if (newName.isEmpty()) {
                System.out.println(Colors.YELLOW + "Name is required. Please enter a name." + Colors.RESET);
            } else if (!newName.matches("[a-zA-Z]+")) {
                System.out.println(Colors.RED + "Invalid name. Name should contain only alphabets." + Colors.RESET);
                newName = ""; // Reset the newName variable to continue the loop
            }
        } while (newName.isEmpty());


        // Enter email
        String newEmail;
        do {
            do {
                System.out.print("Enter user email: ");
                newEmail = scanner.nextLine();
                if (newEmail.isEmpty()) {
                    System.out.println(Colors.YELLOW+"Email is required. Please enter an email address."+Colors.RESET);
                } else if (!validateEmail(newEmail)) {
                    System.out.println(Colors.RED+"Invalid email address. Please enter a valid email address."+Colors.RESET);
                }
            } while (newEmail.isEmpty() || !validateEmail(newEmail));
        } while (!validateEmail(newEmail));


        // Enter phone number
        String newPhoneNumber;
        do {
            System.out.print("Enter user phone number: ");
            newPhoneNumber = scanner.nextLine();
            if (newPhoneNumber.isEmpty()) {
                System.out.println(Colors.YELLOW+"Phone number is required. Please enter a phone number."+Colors.RESET);
            } else if (!validatePhoneNumber(newPhoneNumber)) {
                System.out.println(Colors.RED+"Invalid phone number. Please enter a valid phone number."+Colors.RESET);
            }
        } while (newPhoneNumber.isEmpty() || !validatePhoneNumber(newPhoneNumber));

        // Enter address
        String newAddress;
        do {
            System.out.print("Enter user address: ");
            newAddress = scanner.nextLine();
            if (newAddress.isEmpty()) {
                System.out.println(Colors.YELLOW+"Address is required. Please enter an address."+Colors.RESET);
            }
        } while (newAddress.isEmpty());

        // Insert the new user into the database
        try {
            Connection connection = ConnectionClass.getConnection();
            String sql = "INSERT INTO SUVETHA.Usertable (username, password, name, email, role, phoneNumber, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newUsername);
            statement.setString(2, newPassword);
            statement.setString(3, newName);
            statement.setString(4, newEmail);
            statement.setString(5, "USER"); // Assuming 'USER' is the role for regular users
            statement.setString(6, newPhoneNumber);
            statement.setString(7, newAddress);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(Colors.BLUE+"User registered successfully for username: " + newUsername+Colors.RESET);
                return true;
            } else {
                System.out.println(Colors.RED+"Failed to register user."+Colors.RESET);
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error registering user: " + e.getMessage()+Colors.RESET);
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

    private boolean validatePhoneNumber(String phoneNumber) {
        // Regular expression for phone number validation
        String regex = "^[6-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private static boolean validateName(String name) {
        // Regular expression pattern for name validation (only alphabets, no numbers)
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
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
            String sql = "UPDATE SUVETHA.Usertable SET name = ?, email = ?, phoneNumber = ?, address = ? WHERE username = ?";
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
    
    
    @Override
    public void viewAllHalls() {
        System.out.println("**********The Available Halls**********");
        HallOperations hallOperations = new HallOperations();
        List<Hall> halls = hallOperations.getAllHalls();
        Collections.sort(halls, new HallIdComparator());
        hallOperations.displayAllHalls(halls);
    }

    private boolean checkIfUsernameExists(String username) {
        try {
            Connection connection = ConnectionClass.getConnection();
            String sql = "SELECT * FROM Suvetha.Usertable WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(Colors.RED+"Error checking username existence: " + e.getMessage()+Colors.RESET);
            return false;
        }
    }
    
    public boolean requestBooking(int hallId, LocalDateTime startDate, LocalDateTime endDate, String username) {
        BookingOperations bookingOperations = new BookingOperations();
        return bookingOperations.requestBooking(hallId, startDate, endDate, username);
    }




    public boolean cancelBooking(int bookingId) {
        BookingOperations bookingOperations = new BookingOperations();
        return bookingOperations.cancelBooking(bookingId);
    }

    public List<Booking> getBookings() {
        BookingOperations bookingOperations = new BookingOperations();
        return bookingOperations.getBookingsForUser(getUserId());
    }

    static User user = new User();
    static String name = user.getUsername(); // Call getUsername() on an instance of User

    public static int getUserId() {
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT user_id FROM SUVETHA.Usertable WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                //System.out.println("SQL query: " + preparedStatement.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("USER_ID");
                    } else {
                        System.out.println(Colors.YELLOW + "User ID not found for username: " + name + Colors.RESET);
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            // Print error message and stack trace
            System.out.println(Colors.RED + "Error getting user ID: " + e.getMessage() + Colors.RESET);
            e.printStackTrace();
        }
        // Return a default value or handle it appropriately
        return -1;
    }
    
    public int getUserIdFromUsername(String username) {
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "SELECT user_id FROM SUVETHA.UserTable WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("user_id");
                    } else {
                        System.out.println(Colors.YELLOW + "User ID not found for username: " + username + Colors.RESET);
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error getting user ID: " + e.getMessage() + Colors.RESET);
            e.printStackTrace();
        }
        return -1;
    }

    
    





}
