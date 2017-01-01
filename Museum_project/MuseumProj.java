package cis461.chap3.museumproj;
/***********************************************************
 * Program Assignment #2 (CIS 461, Fall 2016)              *
 * Submitted By: Bibash Shrestha , Suhas Vijaykumar Arudi , Sheetal Chabbenadu shreedhar!!!                     *
 * SID: 01509056 , 01585785 , 01585785                  *
 * Date: 10/14/2016
                              *
 ***********************************************************/

/**
 * CIS 461: Formal Methods for Software Engineering
 * Museum Demonstration
 * The museum project class: MuseumProj.java
 *
 * @author Haiping Xu
 * Created on Sept. 30, 2016
 **/



import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.JApplet;

@SuppressWarnings("serial")
public class MuseumProj extends JFrame{
    protected static Thread museumControl, westExit, eastEntrance;
    private DisplayCanvas museumDisplay, westDisplay, eastDisplay;
    private JButton openButton, closeButton;
    static int east= 20;
    static int control =0;
    static int west =0;

    public void init() {
        setTitle("CIS 461 Multi-Threaded Program: Museum");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up Display
        JPanel canvasPanel = new JPanel();
        museumDisplay = new DisplayCanvas("Museum", Color.cyan);
        westDisplay = new DisplayCanvas("Exit", Color.green);
        eastDisplay = new DisplayCanvas("Entrance", Color.green);
        museumDisplay.setSize(150, 100);
        westDisplay.setSize(150, 100);
        eastDisplay.setSize(150, 100);
        canvasPanel.setLayout(new FlowLayout());
        canvasPanel.add(westDisplay);
        museumDisplay.closeEastDoor();
        museumDisplay.closeWestDoor();
        eastDisplay.setValue(20);
        
        canvasPanel.add(museumDisplay);
        canvasPanel.add(eastDisplay);

        westDisplay.setDisplayType(1);   // 1: West Exit
        museumDisplay.setDisplayType(2); // 2: Museum Display
        eastDisplay.setDisplayType(3);   // 3: East Entrance

        // Set up Director's Buttons
        JLabel director = new JLabel(" Director: ");

        openButton = new JButton("Open Museum");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Control.open = true;  // signals the controller that museum is open
                museumDisplay.openWestDoor();
                museumDisplay.openEastDoor();
            }
        });

        closeButton = new JButton("Close Museum");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Control.open = false; // signals the controller that museum is closed
                museumDisplay.closeEastDoor();
                eastEntrance = null;
                museumControl = null;
                westExit = null;
                
                
            }
        });

        JPanel directorPanel= new JPanel();
        directorPanel.add(director);
        directorPanel.add(openButton);
        directorPanel.add(closeButton);

        getContentPane().add(canvasPanel, BorderLayout.CENTER);
        getContentPane().add(directorPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public DisplayCanvas getEastDisplay() {
        return eastDisplay;
    }

    public DisplayCanvas getWestDisplay() {
        return westDisplay;
    }

    public DisplayCanvas getMuseumDisplay() {
        return museumDisplay;
    }

    public void simulateArrival() {
        int delay = (int) (Math.random() * 1000);
        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
        for (int j = 0; j < 37; j++) {
            try { Thread.sleep(80); } catch (InterruptedException e) { e.printStackTrace(); }
            if (j == 36 && Control.open) eastDisplay.arrive(-1); // if open, erase the walkingman
            else eastDisplay.arrive(j); // walk towards the door
        }
    }

    public void simulateDeparture() {
        int delay = (int) (Math.random() * 3000) + 1000;
        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
        for (int j = 0; j < 37; j++) {
            try { Thread.sleep(80); } catch (InterruptedException e) { e.printStackTrace(); }
            if (j == 36) westDisplay.depart(-1); // erase the walkingman
            else westDisplay.depart(j); // walk away from the door
        }
    }


    //  ====>>>>> Complete the methods below this line! <<<<<====

    // Note: you are free to add any new data fields and/or new methods below this line (if needed)


    public static void main(String[] args) {
        // the main thread serves as the director ...
        MuseumProj museum = new MuseumProj();
        museum.init();

        // ==> 1. Add your code here!

        // create and start the threads ...
        if(Control.open == false){
	        EastEntrance A= new EastEntrance(museum);
	        eastEntrance = new Thread(A);
	        eastEntrance.start();
	        Control C= new Control(museum);
	        museumControl = new Thread(C);
	        museumControl.start();
	        if(east>0){
		        WestExit B = new WestExit(museum);
		        westExit = new Thread(B);
		        westExit.start();
	        }
        }      
    }
    
  
}


class Control extends MuseumProj implements Runnable {
    protected final static int MAX = 20;
    protected static volatile boolean open, allowEnter, allowLeave;
    protected static volatile int count;
    private MuseumProj museum;
    private DisplayCanvas display;

    public Control(MuseumProj museum) {
        this.museum = museum;
        display = museum.getMuseumDisplay();
    }

    public void run() {
    	synchronized(this){
    	//int x= 0;
    	for(int i =0; i<40; i++){
    		control = 21- east-west;
    	}
    		
    	}
    	
    	
        // ==> 2. Add your code here!
    	

    }
    
}


class EastEntrance extends MuseumProj implements Runnable {
    protected static volatile boolean arrival;
    private MuseumProj museum;
    private DisplayCanvas display;

    public EastEntrance(MuseumProj museum) {
        this.museum = museum;
        display = museum.getEastDisplay();
    }

    public void run() {

        // ==> 3. Add your code here!
       	while(east>=0){
    		if(Control.open == true){
	    		//control++;
	    		//display.setDisplayType(3);
	    		display.setValue(east);
	    		display.depart(1);
	    		museum.simulateArrival();
	    		DisplayCanvas museumDisplay = museum.getMuseumDisplay();
	    		control = 21 - east - west;
	    		museumDisplay.setValue(control);
	    		east--;
	    	}
    	}

    }
}

class WestExit extends MuseumProj implements Runnable {
    protected static volatile boolean departure;
    private MuseumProj museum;
    private DisplayCanvas display;

    public WestExit(MuseumProj museum) {
        this.museum = museum;
        display = museum.getWestDisplay();
    }

    public void run() {

        // ==> 4. Add your code here!
    	int x=0;
    	for(int i = 0; i<20; i++){
    			
	    		try {Thread.sleep(3500); }
	    		catch (InterruptedException e){}
	    		museum.simulateDeparture();
	    		display.arrive(1);
	    		west++;
	    		display.setValue(west);
	    		
	    		DisplayCanvas museumDisplay = museum.getMuseumDisplay();
	    		control = 21- east - west-1;
	    		museumDisplay.setValue(control);
	    		
	    		
	    		if(west == 20 && east == -1  && control == 0) {
	    			eastEntrance=null;
	    			westExit=null;
	    			museumControl = null;
	    		}
    			
    		
 		
    		
    		
    		
    		
    	}

    }
}

