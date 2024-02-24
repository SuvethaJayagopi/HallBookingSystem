/*
* The Hall class implements an application that 
* simply have the attributes and constructor of hall
* @author Suvetha Jayagopi(Expleo)
* @since 19 FEB 2024
*
* 
*/
package com.halldetails;

public class Hall {
    private int id; // Auto-incremented ID from the database
    private String name;
    private int capacity;
    private double pricePerHour;
    private String facilities;
    private EventType eventType; // Enum representing the type of event the hall can host
    private String availability;
    private String location; // Location of the hall

    // Constructor
    public Hall(String name, int capacity, double pricePerHour, String facilities, EventType eventType, String location) {
        this.name = name;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.facilities = facilities;
        this.eventType = eventType;
        this.location = location;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public String getFacilities() {
        return facilities;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Hall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", pricePerHour=" + pricePerHour +
                ", facilities='" + facilities + '\'' +
                ", eventType=" + eventType +
                ", availability='" + availability + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
