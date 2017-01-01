/*  Suhas Vijaykumar Arudi (01585785)
	Sheetal Chabbenadu Sreedhar (01592141)
	Bibash Shrestha (01509056)
	
	CIS 461
	
	Assignment 5
	
*/


import java.util.Random;

public class WarringNeighbors {
    public final static WarringNeighbors Instance = new WarringNeighbors();
    private final int MaxPickingTime = 3; 
    private int totalBerries = 20; 
    private void init() {
        // Initializing the flags
    	System.out.println("Creating flag1 and flag2 shared objects ...");
        Flag flag1 = new Flag();
        Flag flag2 = new Flag();
        Thread n1 = new Thread(new Neighbor(1, flag1, flag2));
        Thread n2 = new Thread(new Neighbor(2, flag2, flag1));
      //Starting the neighbor threads
        n1.start();
        n2.start();
    }
    public static void main(String[] args) {
        WarringNeighbors wn = WarringNeighbors.Instance;
        wn.init();
    }
    private class Flag {
        private boolean up;
        public Flag() { up = false; }
        public synchronized void raise() { up = true; }
        public synchronized void lower()  { up = false; }
        public boolean isUp() { return up; }
    }
    private class Neighbor implements Runnable {
        private int id; // Id for the neighbor
        private Flag myFlag;
        private Flag neighborFlag;
        public Neighbor(int num, Flag flag1, Flag flag2) {
            id = num;
            myFlag = flag1;
            neighborFlag = flag2;
            System.out.format("Creating Neighbor%d thread ...\n", id);
        }
        public void pickBerries() {
            Random rand = new Random();
            int Time = rand.nextInt(1000 * MaxPickingTime);
            System.out.format("\tn%d is picking wild berries ..........\n", id);
            try {
                Thread.sleep(Time);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            totalBerries--;
        }
        public void run() {
            while(totalBerries > 0) {
                myFlag.raise();
                if (neighborFlag.isUp()) {
                    myFlag.lower();
                }
                else {
                    System.out.format("n%d enters the field\n", id);
                    pickBerries();
                    System.out.format("n%d exits the field\n", id);
                    myFlag.lower();
                }
            }
        }
    }
}