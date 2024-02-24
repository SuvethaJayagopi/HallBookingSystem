/*
* The FeedbackSubmission class implements an application that 
* simply have the operation of Feedback Submission 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 20 FEB 2024
*
* 
*/
package com.feedbackdetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;

public class FeedbackSubmission {
    private static Scanner scanner = new Scanner(System.in);

    // Method to submit feedback
 // Method to submit feedback
    public static void submitFeedback() {
        // Capture the current timestamp
        LocalDateTime timestamp = LocalDateTime.now();

        // Prompt the user for their rating (out of 5)
        System.out.print("Please rate our service (out of 5 stars): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Prompt the user for any additional comments
        System.out.print("Please provide any additional comments (optional): ");
        String comments = scanner.nextLine();

        // Create a new Feedback object with the provided data
        Feedback feedback = new Feedback(rating, comments, timestamp);

        saveFeedback(feedback);
        
        System.out.println("Feedback submitted successfully:");
        //System.out.println(feedback);
    }

    
    private static void saveFeedback(Feedback feedback) {
        try (Connection connection = ConnectionClass.getConnection()) {
            String sql = "INSERT INTO SUVETHA.Feedback (rating, comments, submission_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, feedback.getRating());
                preparedStatement.setString(2, feedback.getComments());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(Colors.BLUE + "Feedback saved to database successfully." + Colors.RESET);
                } else {
                    System.out.println(Colors.RED + "Failed to save feedback to database." + Colors.RESET);
                }
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error saving feedback to database: " + e.getMessage() + Colors.RESET);
        }
    }


}
