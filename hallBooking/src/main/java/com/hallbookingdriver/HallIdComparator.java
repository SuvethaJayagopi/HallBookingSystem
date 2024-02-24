package com.hallbookingdriver;

import java.util.Comparator;

import com.halldetails.Hall;

public class HallIdComparator implements Comparator<Hall> {
    @Override
    public int compare(Hall hall1, Hall hall2) {
        // Compare halls based on their IDs
        return hall1.getId() - hall2.getId();
    }
}
