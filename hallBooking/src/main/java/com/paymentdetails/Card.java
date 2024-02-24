/*
* The Card class implements an application that 
* simply have the operation of Card 
* print the result to standard output
* @author Suvetha Jayagopi(Expleo)
* @since 21 FEB 2024
*
* 
*/
package com.paymentdetails;

import java.util.Scanner;

import com.hallbookingdriver.Colors;

// Card subclass
public class Card extends Payment {
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public Card(double amount, String cardNumber, String expiryDate, String cvv) {
        super(amount);
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    // Validate card details and process payment
    public void processPayment() {
        // Display payment amount
        System.out.println("Payment amount: $" + getAmount());

        // Ask for confirmation
        System.out.println("Do you want to proceed with the payment? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            // Validate card details
            if (validateCard()) {
                System.out.println(Colors.BLUE+"Payment successful."+Colors.RESET);
                setStatus(PaymentStatus.SUCCESSFUL);
                PaymentOperations.insertPayment(this);

            } else {
                System.out.println(Colors.RED+"Payment failed. Please check card details."+Colors.RESET);
                setStatus(PaymentStatus.FAILED);
                PaymentOperations.insertPayment(this);

            }
        } else {
            System.out.println(Colors.RED+"Payment cancelled."+Colors.RESET);
            setStatus(PaymentStatus.FAILED);
            PaymentOperations.insertPayment(this);

        }
    }

    // Validate card number using regular expression
    private boolean validateCardNumber(String cardNumber) {
        // Card number format: 16 digits
        String regex = "\\d{16}";
        return cardNumber.matches(regex);
    }

    // Validate expiry date using regular expression
    private boolean validateExpiryDate(String expiryDate) {
        // Expiry date format: MM/YY
        String regex = "(0[1-9]|1[0-2])/\\d{2}";
        return expiryDate.matches(regex);
    }

    // Validate CVV using regular expression
    private boolean validateCVV(String cvv) {
        // CVV format: 3 digits
        String regex = "\\d{3}";
        return cvv.matches(regex);
    }

    // Validate card details
    private boolean validateCard() {
        return validateCardNumber(cardNumber) && validateExpiryDate(expiryDate) && validateCVV(cvv);
    }
}
