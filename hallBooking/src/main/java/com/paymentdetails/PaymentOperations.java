/*
* The PaymentOperations class implements an application that 
* simply have the operation of Payment and 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 21 FEB 2024
*
* 
*/
package com.paymentdetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.hallbookingdriver.Colors;
import com.hallbookingdriver.ConnectionClass;

public class PaymentOperations {

    public static void insertPayment(Payment payment) {
        String sql = "INSERT INTO SUVETHA.Payments (amount, status) VALUES (?, ?)";

        try (Connection connection = ConnectionClass.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, payment.getAmount());
            preparedStatement.setString(2, payment.getStatus().toString());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(Colors.BLUE + "Payment record inserted successfully." + Colors.RESET);
            } else {
                System.out.println(Colors.RED + "Failed to insert payment record." + Colors.RESET);
            }
        } catch (SQLException e) {
            System.out.println(Colors.RED + "Error inserting payment record: " + e.getMessage() + Colors.RESET);
        }
    }
}
