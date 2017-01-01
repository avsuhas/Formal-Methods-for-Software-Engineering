/***********************************************************
 * Program Assignment #3 (CIS 461, Fall 2016)              *
 * Submitted By: Bibash Shrestha, Suhas Vijaykumar Arudi, Sheetal chabbenadu Sreedhar   *
                
 * SID: 01509056,01585785,01592141                *
 * Date: Your submission date                              *
 ***********************************************************/

/**
 * CIS 461: Formal Methods for Software Engineering
 * Concert Hall Seat Reservation
 * The concert hall project class: ConcertHallProj.java
 *
 * @author Haiping Xu
 * Created on Oct. 10, 2016
 **/


package cis461.chap4.concerthallproj;

import java.util.Random;

/*
 * Thread-safe singleton implementation of class ConcertHallProj
 */
public class ConcertHallProj {
    public final static ConcertHallProj Instance = new ConcertHallProj();
    private final int MAX_ROWS = 11;       // The maximum number of rows, ignore row 0
    private final int MAX_COLS = 11;       // The maximum number of columns, ignore column 0
    private Terminal terminalA, terminalB;
    private int[][] map;    // The next seat configuration
    private SeatMapGUI gui;
    private int doubleBookingTimes = 0;

    private ConcertHallProj() {
        // Exists only to disallow instantiation.
        // System.out.println("Will be printed out only once during my life time!");
    }

    // Concert hall initialization
    private void init() {
        map = new int[MAX_ROWS][MAX_COLS];
        for (int i = 0; i <= 10; i++)
            for (int j = 0; j <= 10; j++)
                map[i][j] = 0;
        gui = new SeatMapGUI(map);
        gui.setTitle("CIS 461 Concert Hall Seat Reservation");
    }

    public void reset() {
        terminalA = null;
        terminalB = null;

        // wait until terminals are all stopped
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        for (int i = 0; i <= 10; i++)
            for (int j = 0; j <= 10; j++)
                map[i][j] = 0;

        gui.updateMap();
        doubleBookingTimes = 0;
        updateFailureTimes(1, 0);
        updateFailureTimes(2, 0);
        updateFailureTimes(3, doubleBookingTimes);
    }

    /*
     * When terminal == 1, failureTimes is the number of reservation failures from terminal A;
     * When terminal == 2, failureTimes is the number of reservation failures from terminal B;
     * When terminal == 3, failureTimes is the total number of double booking times.
     */
    public void updateFailureTimes(int terminal, int failureTimes) {
        gui.updateFailureTimes(terminal, failureTimes);
    }

    public boolean isTerminalStopped(int terminal) {
        if (terminal == 1) return (terminalA == null);
        else if (terminal == 2) return (terminalB == null);
        return false;
    }

    public boolean isFull() {
        for (int i = 1; i <= 10; i++)
            for (int j = 1; j <= 10; j++)
                if (map[i][j] == 0) return false;
       // isTerminalStopped(1);
        return true;
    }

    public boolean querySeat(int row, int column) {
        if (map[row][column] == 0) return true;
        else return false;
    }

    // The main method of ConcertHallProj
    public static void  main(String[] args) {
        ConcertHallProj ch = ConcertHallProj.Instance;
        ch.init();
    }

    //  ====>>>>> Complete the methods below this line! <<<<<====

    // map[row, column] == 0 : seat available
    // map[row, column] == 1 : reserved by terminal A
    // map[row, column] == 2 : reserved by terminal B
    // map[row, column] == 3 : reserved by both A and B (double booking)

    //
    // Simulate double booking as demonstrated by the given sample implementation
    // ConcertHallProj.jar
    //
    public boolean reserveSeat(int row, int column, int terminal) {
    	
    	// ==> 1. Add your code here!
    	//System.out.println("Reserve seat");
    	 if (querySeat(row,column)) {
    		 map[row][column]=terminal;
             gui.updateMap();
             return true;  
         } else {
        	 updateFailureTimes(3, ++doubleBookingTimes);
             map[row][column] = 3;
             gui.updateMap();
             return false;
         }
    }

    public void startTerminal(int terminal) {

    	// ==> 2. Add your code here!
    	if (terminal == 1) {
            terminalA = new Terminal(terminal);
            terminalA.start();
        } else if (terminal == 2) {
            terminalB = new Terminal(terminal);
            terminalB.start();
        }

    }

    public void stopTerminal(int terminal) {

    	// ==> 3. Add your code here!
    	 if (terminal == 1)
             terminalA = null;
         else if (terminal == 2)
             terminalB = null;

    }
}

class Terminal extends Thread {
    private ConcertHallProj ch = ConcertHallProj.Instance;
    private int terminal; // 1: terminal A; 2: terminal B
    private int failureTimes = 0; // reservation fails when a seat has been reserved.

    public Terminal (int terminal) {
        this.terminal = terminal;
    }

    public void run() {
    	//System.out.println("Check");
    	// ==> 4. Add your code here!
    	Random randomGenerator = new Random(); // Use to pick the next seat
        int x = 0;
        int y = 0;
       
        // Run as long as the terminal has not stopped
        while (!ch.isFull() ) { 
           
            // Loop to pick the next seat
            while(!ch.isFull() && !ch.querySeat(x, y)){
                x = randomGenerator.nextInt(11);
                y = randomGenerator.nextInt(11);
              //  System.out.println("Check 2");
                }
            
           	//ch.reserveSeat(x,y,terminal);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
           
            // Check for double booking
            if(ch.querySeat(x, y) && !ch.reserveSeat(x,y,terminal)){
                failureTimes++;
                ch.updateFailureTimes(terminal, failureTimes);
            }
        }
        
        ch.stopTerminal(1);
        ch.stopTerminal(2);
        

    }
}