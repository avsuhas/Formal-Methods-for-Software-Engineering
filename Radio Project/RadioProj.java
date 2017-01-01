/***********************************************************
 * Program Assignment #1 (CIS 461, Fall 2016)              *
 * Submitted By: Bibash , Suhas , Sheetal !!!                       *
 * SID: Your new SIDs starting with " 01509056 ", "01585785", "01592141"                 *
 * Date: 09/30/2016 *
 ***********************************************************/

/**
 * CIS 461: Formal Methods for Software Engineering
 * FM Radio Demonstration
 * The radio project class: RadioProj.java
 *
 * @author Haiping Xu
 * Revised on Sept. 15, 2016
 **/

//@package cis461.chap2.radioproj;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RadioProj implements Runnable, ActionListener {
    private final static int freqTop = 108;
    private final static int freqBottom = 88;
    private float frequency = freqTop;
    private static int i=0;
    private double[] lockFrequency = {105.9, 101.8, 98.5, 95.6, 92.1};
    private DisplayPanel display;
    private JButton on;
    private JButton off;
    private JButton scan;
    private JButton reset;
    private JButton end;


    private volatile Thread searchChannel;

    public void init() {
        JFrame frame = new JFrame("FM Radio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();

        display=new DisplayPanel("FM Radio");
        pane.add(display, BorderLayout.CENTER);

        JPanel p = new JPanel();
        on = new JButton("on");
        on.addActionListener(this);
        p.add(on);

        off = new JButton("off");
        off.addActionListener(this);
        p.add(off);
        off.setVisible(false);

        scan = new JButton("scan");
        scan.addActionListener(this);
        p.add(scan);
       scan.setVisible(false);

        reset = new JButton("reset");
        reset.addActionListener(this);
        p.add(reset);
        reset.setVisible(false);
        
        end = new JButton("end");
        scan.addActionListener(this);
        p.add(end);
        end.setVisible(false);

        pane.add(p, BorderLayout.SOUTH);

        frame.pack();
        frame.setSize(350, 300);
        frame.setVisible(true);
    }

    public static void main(String[] argv) {
        RadioProj radio = new RadioProj();
        radio.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    	
        if (e.getActionCommand().equals("on")) {
            display.turnOn();
            frequency =  freqTop;
            display.setValue(frequency);
            on.setVisible(false);
            off.setVisible(true);
            off.addActionListener(this);
            reset.setVisible(true);
            scan.setVisible(true);
            scan.addActionListener(this);
            end.setVisible(true);
            end.addActionListener(this);
            
        }
        else if (e.getActionCommand().equals("off")) {
            display.turnOff();
            // searchChannel.stop(); -- deprecated, do not use
            searchChannel = null;
            on.setVisible(true);
            scan.setVisible(false);
            off.setVisible(false);
            end.setVisible(false);
            reset.setVisible(false);
        }
        else if (e.getActionCommand().equals("scan")) {
            if (display.isOn()) scanning();
        }
        else if (e.getActionCommand().equals("reset")) {
            if (display.isOn()) reset();
        }
        else if (e.getActionCommand().equals("end")) {
            if (display.isOn()) end();
        };
    }


    //  ====>>>>> Complete the methods below this line! <<<<<====

    private void scanning() {

        // ==> 1. Add your code here!
    	searchChannel= new Thread(this);
    	searchChannel.start();
    	

    }

    private void reset() {
    	display.turnOn();
    	frequency = freqTop;
    	i=0;
    	display.setValue(frequency);
    	scan.setVisible(true);
    	end.setVisible(true);
    }
    
    private void end(){
    	display.turnOn();
    	frequency = freqBottom;
    	display.setValue(frequency);
    	scan.setVisible(false);
    	end.setVisible(false);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        // Used to simulate the process of searching a channel, where the frequency is continuously
        // decreased until a channel is found or the bottom frequency is reached.

        // ==> 3. Add your code here!
    	
    	 	while(true){
    		if(searchChannel==null){return;}
    		
    		/*if(i<7&&frequency>freqBottom){
    			if(Math.abs(frequency -lockFrequency[i])<0.01){
    				locked();
    				i++;
    				System.out.println(i);
    			}
    			else{
    				tick();
	       			frequency-=0.01f;
    			}
    		}*/
    		else{
    		
	    		if(i<lockFrequency.length){
	    			if(Math.abs(frequency- lockFrequency[i])<=0.01){
	    				i++;
		    			locked();
		    		
	    			}
	    		}
	    			
	    		/*if(Math.abs(frequency- lockFrequency[1])<0.01){
	    			locked();
	    		}
	    		if(Math.abs(frequency- lockFrequency[2])<0.01){
	    			locked();
	    		}
	    		if(Math.abs(frequency- lockFrequency[3])<0.01){
	    			locked();
	    		}
	    		if(Math.abs(frequency- lockFrequency[4])<0.01){
	    			locked();
	    		}*/
	    		if(Math.abs(frequency- freqBottom)<0.01){
	    			i=0;
	    			locked();
	    			scan.setVisible(false);
	    			end.setVisible(false);
	    		}
	    		
	    		else{
	       			tick();
	       			frequency-=0.01f;
	    		}
    		}
	    		
    	}

    }
    public void tick(){
    	
     	display.turnOn();
    	display.setValue(frequency);
    	
    	try{ Thread.sleep(10);}
    	catch (InterruptedException e){}
    }
    
    public void locked(){
    	searchChannel=null;
    	display.setValue(frequency);  
    }
}