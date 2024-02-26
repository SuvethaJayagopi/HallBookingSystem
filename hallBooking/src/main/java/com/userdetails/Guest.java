package com.userdetails;

import java.util.Collections;
import java.util.List;

import com.hallbookingdriver.HallIdComparator;
import com.hallbookingdriver.HallViewer;
import com.halldetails.Hall;
import com.halldetails.HallOperations;

public class Guest implements HallViewer {

	@Override
    public void viewAllHalls() {
        System.out.println("**********The Available Halls**********");
        HallOperations hallOperations = new HallOperations();
        List<Hall> halls = hallOperations.getAllHalls();
        Collections.sort(halls, new HallIdComparator());
        hallOperations.displayAllHalls(halls);
    }
}