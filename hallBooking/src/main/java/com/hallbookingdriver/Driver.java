/*
* The Driver class implements an application that 


* simply have the main menu operation of hall booking system and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.hallbookingdriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bookingdetails.BillingOperations;
import com.bookingdetails.Booking;
import com.bookingdetails.BookingOperations;
import com.feedbackdetails.FeedbackSubmission;
import com.halldetails.Hall;
import com.halldetails.HallOperations;
import com.notificationdetails.EmailNotification;
import com.notificationdetails.Notification;
import com.notificationdetails.ReminderNotification;
import com.notificationdetails.SMSNotification;
import com.paymentdetails.Card;
import com.paymentdetails.Cash;
import com.paymentdetails.Payment;
import com.paymentdetails.PaymentOperations;
import com.userdetails.Admin;
import com.userdetails.Guest;
import com.userdetails.User;

public class Driver {
    private static Scanner scanner = new Scanner(System.in);
    private static Admin admin = new Admin();
    private static User user = new User();
	private static Booking booking;

    public static void main(String[] args) {
    	System.out.println(Colors.PINK + "*****************************************");
    	System.out.println("*      Welcome to CelebrationHub        *");
    	System.out.println("*         Hall Booking System!          *");
    	System.out.println("*****************************************" + Colors.RESET);

    	    
    	    while (true) {
    	    	try {
                System.out.println(Colors.GREEN+"*****************************");
                System.out.println("*         MENU              *");
                System.out.println("*       1. Login            *");
                System.out.println("*       2. Register         *");
                System.out.println("*       3. Guest            *");
                System.out.println("*       4. Exit             *");
                System.out.println("*****************************"+Colors.RESET);
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    Guest guest = new Guest();
                    guest.viewAllHalls();
                    break;
                case 4:
                    System.out.println(Colors.YELLOW+"Exiting..."+Colors.RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(Colors.RED+"Invalid option."+Colors.RESET);
                    break;
            }
        }catch (InputMismatchException e) {
            System.out.println(Colors.RED +"Invalid input. Please enter a valid integer choice." +Colors.RESET);
            scanner.nextLine(); // consume invalid input
        }
    }
  }
    
    private static void viewAllHalls() {
        System.out.println("**********The Available Halls**********");
        HallOperations hallOperations = new HallOperations();
        List<Hall> halls = hallOperations.getAllHalls();
        hallOperations.displayAllHalls(halls);
    }



    private static void login() {
    	boolean backToMainMenu = false;
    	System.out.println(Colors.GREEN+"*****************************");
        System.out.println("*         MENU              *");
        System.out.println("*     1. Admin Login        *");
        System.out.println("*     2. User Login         *");
        System.out.println("*     3. Back to Main Menu  *");
        System.out.println("*     4. Exit               *");
        System.out.println("*****************************"+Colors.RESET);
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (option) {
            case 1:
                adminLogin();
                break;
            case 2:
                userLogin();
                break;
            case 3:
                backToMainMenu = true; // Set flag to true to exit login menu loop
                break;
            case 4:
                System.out.println("Exiting...");
                System.exit(0); // Exit the entire program
                break;
            default:
                System.out.println(Colors.RED+"Invalid option."+Colors.RESET);
                break;
        }
    }

    private static void adminLogin() {
        Admin admin = new Admin(); // Instantiate an Admin object
        
        if (admin.login()) {
            System.out.println(Colors.BLUE + "Admin login successful." + Colors.RESET);
            String adminName = admin.getName(); // Fetch admin's name from the Person class
            if (adminName != null) {
                System.out.println("Welcome, " + adminName + "!");
            } else {
                System.out.println("Welcome, Admin!"); // Default welcome message if name retrieval fails
            }
            adminOptions();
        } else {
            System.out.println(Colors.RED + "Admin login failed." + Colors.RESET);
        }
    }


    
    private static void userLogin() {
        User user = new User(); // Instantiate a User object
        
        if (user.login()) {
            System.out.println(Colors.BLUE + "User login successful." + Colors.RESET);
            String userName = user.getName(); // Fetch user's name from the Person class
            if (userName != null) {
                System.out.println(Colors.PINK+"Welcome, " + userName + "!"+Colors.RESET);
            } else {
                System.out.println("Welcome, User!"); // Default welcome message if name retrieval fails
            }
            userOptions(); // Provide user options if login successful
        } else {
            System.out.println(Colors.RED + "User login failed." + Colors.RESET);
        }
    }


    
    private static void register() {
        boolean backToMainMenu = false;
    	System.out.println(Colors.GREEN+"******************************");
        System.out.println("*     1. Admin Register      *");
        System.out.println("*     2. User Register       *");
        System.out.println("*     3. Back to Main Menu  *");
        System.out.println("*     4. Exit               *");
    	System.out.println("******************************"+Colors.RESET);
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (option) {
            case 1:
                adminRegister();
                break;
            case 2:
                userRegister();
                break;
            case 3:
                backToMainMenu = true; // Set flag to true to exit login menu loop
                break;
            case 4:
                System.out.println("Exiting...");
                System.exit(0); // Exit the entire program
                break;
            default:
                System.out.println(Colors.RED+"Invalid option."+Colors.RESET);
                break;
        }
    }

    private static void adminRegister() {
        if (admin.register()) {
            System.out.println(Colors.BLUE+"Admin registered successfully."+Colors.RESET);
        } else {
            System.out.println(Colors.RED+"Failed to register admin."+Colors.RESET);
        }
    }

    private static void userRegister() {
        if (user.registerUser()) {
            System.out.println(Colors.BLUE+"User registered successfully."+Colors.RESET);
        } else {
            System.out.println(Colors.RED+"Failed to register user."+Colors.RESET);
        }
    }

    private static void adminOptions() {
    	try {
        while (true) {
        	System.out.println(Colors.GREEN+"***********************************");
            System.out.println("*         Admin Options           *");
            System.out.println("*      1. View All Halls          *");
            System.out.println("*      2. Add Hall                *");
            System.out.println("*      3. Delete Hall             *");
            System.out.println("*      4. View All Booking        *");
            System.out.println("*      5. Approve Booking         *");
            System.out.println("*      6. Reject Booking          *");
            System.out.println("*      7. Update Profile          *");
            System.out.println("*      8. Update Halls            *");
            System.out.println("*      9. Logout                  *");
            System.out.println("***********************************"+Colors.RESET);
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                	admin.viewAllHalls();
                    break;
                case 2:
                    admin.addHall();
                    break;
                case 3:
                    admin.deleteHall();
                    break;
                case 4:
                	admin.viewAllBookings();
                    break;
                case 5:
                    admin.approveBooking();
                    break;
                case 6:
                    admin.rejectBooking();
                    break;
                    
                case 7:
                    // Update profile
                    admin.updateProfile();
                    break; 
                    
                case 8:
                    // Update profile
                	admin.updateHallDetails();
                	break; 
                
                case 9:
                    System.out.println(Colors.YELLOW+"Logging out..."+Colors.RESET);
                    return;
                default:
                    System.out.println(Colors.RED+"Invalid option."+Colors.RESET);
                    break;
            }
        }
    }
        catch (NoSuchElementException e) {
            System.out.println(Colors.RED+"Error: Input not provided. Please provide valid input."+Colors.RESET);
            scanner.nextLine(); // Consume newline
            adminLogin(); // Recursive call to handle the exception and retry
        }
    }

    
   
    private static void userOptions() {
    	try {
        while (true) {
        	System.out.println(Colors.GREEN+"***********************************");
            System.out.println("*          User Options           *");
            System.out.println("*       1. View All Halls         *");
            System.out.println("*       2. Request Booking        *");
            System.out.println("*       3. Cancel Booking         *");
            System.out.println("*       4. Booking History        *");
            System.out.println("*       5. Show Billing           *");
            System.out.println("*       6. Update Profile         *"); 
            System.out.println("*       7. Logout                 *");
            System.out.println("***********************************"+Colors.RESET);
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                	user.viewAllHalls();
                    break;
                case 2:
                    requestBooking();
                    break;
                case 3:
                	cancelBooking(); // Call cancelBooking method with the booking ID
                    break;
                case 4:
                    displayAllBookings();
                    break;
                case 5:
                	double totalAmount;
                	boolean proceedWithPayment = false;
                	do {
                	    totalAmount = BillingOperations.showBilling(scanner);
                	    if (totalAmount == -1) {
                	        System.out.println(Colors.RED + "Error retrieving billing details. Payment cannot be processed." + Colors.RESET);
                	        break;
                	    }

                	    System.out.println("Do you want to proceed with payment? (yes/no)");
                	    String proceed = scanner.nextLine();
                	    if (proceed.equalsIgnoreCase("yes")) {
                	        proceedWithPayment = true;
                	    } else {
                	        // User wants to see the bill again
                	        continue;
                	    }

                	    boolean choosingPaymentMethod = true;
                	    while (choosingPaymentMethod) {
                	        System.out.println(Colors.GREEN+"*****************************");
                	        System.out.println("*   Choose Payment Method   *");
                	        System.out.println("*         1. Cash           *");
                	        System.out.println("*         2. Card           *");
                	        System.out.println("*         3. Back           *");
                	        System.out.println("*****************************"+Colors.RESET);
                	        System.out.print("Enter your choice: ");
                	        
                	        if (!scanner.hasNextInt()) {
                	            System.out.println(Colors.RED + "Error: Input should be an integer." + Colors.RESET);
                	            scanner.nextLine(); // Consume the invalid input
                	            continue;
                	        }
                	        
                	        int paymentMethodChoice = scanner.nextInt();
                	        scanner.nextLine(); // Consume newline

                	        switch (paymentMethodChoice) {
                	            case 1:
                	                // Process cash payment...
                	                Payment cashPayment = new Cash(totalAmount, proceed); // Pass totalAmount as the billing amount
                	                ((Cash) cashPayment).makePayment();
                	                choosingPaymentMethod = false;
                	                break;
                	            case 2:
                	                // Process card payment
                	                System.out.println("Enter card details:");
                	                String cardNumber;
                	                String expiryDate;
                	                String cvv;

                	                do {
                	                    System.out.print("Card number: ");
                	                    cardNumber = scanner.nextLine();
                	                    if (!Card.validateCardNumber(cardNumber)) {
                	                        System.out.println(Colors.RED+"Invalid card number. Please enter a valid card number."+Colors.RESET);
                	                    }
                	                } while (!Card.validateCardNumber(cardNumber));

                	                do {
                	                    System.out.print("Expiry date (MM/YY): ");
                	                    expiryDate = scanner.nextLine();
                	                    if (!Card.validateExpiryDate(expiryDate)) {
                	                        System.out.println(Colors.RED +"Invalid expiry date. Please enter a valid expiry date in MM/YY format."+Colors.RESET);
                	                    }
                	                } while (!Card.validateExpiryDate(expiryDate));

                	                do {
                	                    System.out.print("CVV: ");
                	                    cvv = scanner.nextLine();
                	                    if (!Card.validateCVV(cvv)) {
                	                        System.out.println(Colors.RED+"Invalid CVV. Please enter a valid 3-digit CVV."+Colors.RESET);
                	                    }
                	                } while (!Card.validateCVV(cvv));

                	                Payment cardPayment = new Card(totalAmount, cardNumber, expiryDate, cvv);
                	                ((Card) cardPayment).processPayment(); // Process payment
                	                choosingPaymentMethod = false;
                	                break;

                	            case 3:
                	                // User wants to go back to see the bill again
                	                choosingPaymentMethod = false;
                	                proceedWithPayment = false;
                	                break;
                	            default:
                	                System.out.println(Colors.RED + "Invalid payment method." + Colors.RESET);
                	                break;
                	        }
                	    }

                	    if (proceedWithPayment) {
                	        boolean selectingNotificationPreference = true;
                	        while (selectingNotificationPreference) {
                	            System.out.println(Colors.GREEN+"***************************************");
                	            System.out.println("*  Select Notification Preference     *");
                	            System.out.println("*           1. SMS                    *");
                	            System.out.println("*           2. Email                  *");
                	            System.out.println("*           3. Back                   *");
                	            System.out.println("***************************************"+Colors.RESET);
                	            System.out.print("Enter your choice: ");
                	            int notificationPreference = scanner.nextInt();
                	            scanner.nextLine(); // Consume newline

                	            switch (notificationPreference) {
                	            case 1:
                	                // Send SMS notification
                	                Notification smsNotification = new SMSNotification(1, "Thank you for booking! Enjoy your moments.");
                	                Thread smsThread = new Thread(smsNotification);
                	                smsThread.start();
                	                selectingNotificationPreference = false;
                	                break;
                	            case 2:
                	                // Send email notification
                	                Notification emailNotification = new EmailNotification(1, "Thank you for booking! Enjoy your moments.");
                	                Thread emailThread = new Thread(emailNotification);
                	                emailThread.start();
                	                selectingNotificationPreference = false;
                	                break;
                	            case 3:
                	                // User wants to go back to payment method selection
                	                selectingNotificationPreference = false;
                	                proceedWithPayment = false;
                	                break;

                	            default:
                	                System.out.println(Colors.RED + "Invalid notification preference." + Colors.RESET);
                	                break;
                	        }
                	    }

                	        // After successful payment, set a reminder notification for 24 hours before the booking
                	        if (proceedWithPayment) {
                	            LocalDateTime bookingDateTime = LocalDateTime.now(); // Replace this with actual booking date-time
                	            ReminderNotification reminderNotification = new ReminderNotification(1, bookingDateTime);
                	            reminderNotification.sendNotification();

                	            // Submit feedback
                	            FeedbackSubmission.submitFeedback();
                	        }
                	    } else {
                	        System.out.println(Colors.RED + "Payment canceled." + Colors.RESET);
                	    }
                	} while (!proceedWithPayment);

                    break;
                    
                case 6:
                    user.updateProfile();
                    break;

                case 7:
                    System.out.println(Colors.BLUE+"Logging out..."+Colors.RESET);
                    return;
                default:
                    System.out.println(Colors.RED+"Invalid option."+Colors.RESET);
                    break;
            }
        }
    }catch (NoSuchElementException e) {
        System.out.println(Colors.RED+"Error: Input not provided. Please provide valid input."+Colors.RESET);
        scanner.nextLine(); // Consume newline
        userLogin(); // Recursive call to handle the exception and retry
    }
  }


    private static void requestBooking() {
        HallOperations hallOperations = new HallOperations();
        while (true) {
            System.out.println("Enter your preferred location:");
            String preferredLocation = scanner.nextLine();

            // Get all halls from the database
            List<Hall> allHalls = hallOperations.getAllHalls();

            // Filter the halls based on the preferred location
            List<Hall> filteredHalls = allHalls.stream()
                    .filter(hall -> hall.getLocation().equalsIgnoreCase(preferredLocation))
                    .collect(Collectors.toList());

            // If filteredHalls is empty, display an error message and continue to prompt for location
            if (filteredHalls.isEmpty()) {
                System.out.println("Location not found. Please try again.");
                continue;
            }

            // Display the filtered halls to the user
            System.out.println("Halls available at " + preferredLocation + ":");
            hallOperations.displayAllHalls(filteredHalls);

            // If halls are found for the location, break out of the loop
            break;
        }
        int hallId;
        String username;
        while (true) {
            try {
            	System.out.println("Enter your Name:");
                username = scanner.nextLine();
            	
                if (!validateName(username)) {
                    System.out.println(Colors.RED + "Error: Name must contain only alphabetic characters and have a minimum length of 5 characters. Please try again." + Colors.RESET);
                    continue;
                }
                
                while (true) { // Nested loop for Hall ID input
                    System.out.println("Enter Hall ID:");
                    hallId = Integer.parseInt(scanner.nextLine());

                    // Check if the entered Hall ID is valid
                    if (!hallOperations.isHallIdValid(hallId)) {
                        System.out.println(Colors.RED + "Error: Hall ID does not exist. Please try again." + Colors.RESET);
                        continue; // Continue to the next iteration if Hall ID is invalid
                    }

                    break; // Exit the nested loop if Hall ID is valid
                }

                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED+"Error: Hall ID must be a valid integer. Please try again."+Colors.RESET);
            }
        }
        
        
        LocalDateTime startDate;
        LocalDateTime endDate;
        while (true) {
            try {
                System.out.println("Enter Start Date and Time (YYYY-MM-DD HH:mm):");
                String startDateStr = scanner.nextLine();
                startDate = LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                break; // Exit the loop if input is valid
            } catch (DateTimeParseException e) {
                System.out.println(Colors.RED+"Error: Invalid date and time format. Please enter in YYYY-MM-DD HH:mm "+Colors.RESET);
            }
        }

        while (true) {
            try {
                System.out.println("Enter End Date and Time (YYYY-MM-DD HH:mm):");
                String endDateStr = scanner.nextLine();
                endDate = LocalDateTime.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                break; // Exit the loop if input is valid
            } catch (DateTimeParseException e) {
                System.out.println(Colors.RED+"Error: Invalid date and time format. Please enter in YYYY-MM-DD HH:mm format."+Colors.RESET);
            }
        }
        // Format the start date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        
        // Print the formatted date and time
        System.out.println("Formatted Start Date and Time: " + formattedStartDate);
        System.out.println("Formatted End Date and Time: " + formattedEndDate);

        if (user.requestBooking(hallId, startDate, endDate,username)) {
            System.out.println(Colors.BLUE+"Booking requested successfully."+Colors.RESET);
        } else {
            System.out.println(Colors.RED+"Failed booking."+Colors.RESET);
        }
    }


    private static boolean validateName(String name) {
        String regex = "^[a-zA-Z]{5,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

// Call the sendCancellationNotification method when a booking is cancelled
    private static void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
        BookingOperations bookingOperations = new BookingOperations();

        int bookingId;
        while (true) {
            try {
                System.out.println("Enter Booking ID:");
                String input = scanner.nextLine();
                
                // Check if the input is an integer
                if (!input.matches("\\d+")) {
                    System.out.println("Error: Booking ID must be an integer. Please try again.");
                    continue;
                }
                
                bookingId = Integer.parseInt(input);
                
                // Check if the entered Booking ID is valid
                if (!bookingOperations.isBookingIdValid(bookingId)) {
                    System.out.println("Error: Booking ID does not exist. Please try again.");
                    continue;
                }
                
                // Cancel the booking
                if (bookingOperations.cancelBooking(bookingId)) {
                    System.out.println("Booking with ID " + bookingId + " has been cancelled successfully.");
                } else {
                    System.out.println("Error: Failed to cancel booking with ID " + bookingId);
                }

                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a valid Booking ID.");
            }
        }
    }



    private static void displayAllBookings() {
        BookingOperations bookingOperations = new BookingOperations();
        List<Booking> bookings = bookingOperations.getAllBookings(); // Assuming getAllBookings() returns all bookings
        bookingOperations.displayAllBookings(bookings);
    }

}
