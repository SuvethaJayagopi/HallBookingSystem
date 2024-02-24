/*
* The EventType class implements an application that 
* simply have the enum of Event Types 
* @author Suvetha Jayagopi(Expleo)
* @since 19 FEB 2024
*
* 
*/
package com.halldetails;

public enum EventType {
    WEDDING,
    BIRTHDAY,
    CONFERENCE,
    CONCERT,
    EXHIBITION;
	
	@Override
	public String toString() {
	    return name(); // Returns the name of the enum constant
	}

	
	

	public static EventType fromString(String value) {
	    value = value.toUpperCase(); // Convert to uppercase
	    try {
	        return EventType.valueOf(value); // Get EventType enum from string
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Invalid event type: " + value);
	    }
	}


}
