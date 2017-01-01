/***********************************************************
 * Program Assignment #4 (CIS 461, Fall 2016)              *
 * Submitted By: Your name(s) here Sheetal chabbenadu sreedhar, Suhas Vijaykumar Arudi , Bibash Shrestha       *
 * SID: Your new SID(s) starting with “01592141 ,01585785, 01509056”           *
 * Date: 11/21/2016                              *
 ***********************************************************/


package cis461;

class Pot{
	static final int MISSIONARIES=3;  //total number of missionaries
	int missionaries=0;
			
	public synchronized void GetServing(int id) throws InterruptedException{
		while(missionaries==0) wait(); 
		missionaries--;
		System.out.println("savage["+id+"] is eating ... [remaining servings= " + missionaries + "]");
		if(missionaries==0)   //when no missionaries notify cook. avoid wasting CPU
			notifyAll();
	}
	
	public synchronized void FillPot()	throws InterruptedException{
		while(missionaries>0) wait();
		missionaries=MISSIONARIES;
		System.out.println("Cook refilled the pot ...  [remaining servings = " + missionaries + "]");
		notifyAll();
	}
	public static void main(String[] args){
		final int SAVAGES=5;
		Pot pot=new Pot();
		
		//create 5 savage threads
		Thread[] savages=new Thread[SAVAGES+1];		//do not use savages[0]
		for(int i=1;i<=5;i++){
			System.out.println("Creating savage[" +i +"] thread ...");
			savages[i]=new Thread(new Savage(i,pot));
			savages[i].start();
		}
		
		//create 1 cook thread
		System.out.println("Creating cook thread ... ");
		Thread cook=new Thread(new Cook(pot));
		cook.start();
				
	}
	
}

class Savage implements Runnable{
	Pot pot;
	int id;
	public Savage(int id,Pot p){
		this.id=id;
		pot=p;
	}
        @Override
	public void run(){
		while(true){
			try {
				Thread.sleep(500);
				pot.GetServing(id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Cook implements Runnable{
	Pot pot;
	public Cook(Pot p){
		pot=p;
	}
	public void run(){
		while(true){
			try {
				pot.FillPot();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


