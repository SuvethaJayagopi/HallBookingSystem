package com.feedbackdetails;

import java.time.LocalDateTime;

public class Feedback {
    private int rating; // Rating out of 5 stars
    private String comments;
    private LocalDateTime timestamp;

    public Feedback(int rating, String comments, LocalDateTime timestamp) {
        this.rating = rating;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    // Getter and setter methods
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "rating=" + rating +
                ", comments='" + comments + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
