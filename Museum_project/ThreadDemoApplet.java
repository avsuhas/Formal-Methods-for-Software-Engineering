package cis461.chap3.museumproj;
// j.n.magee 11/11/96
// updated 20/10/97

// CIS 461: Formal Methods for Software Engineering
// Revised by Dr. Haiping Xu October 5, 2012



import java.awt.*;
import javax.swing.JApplet;

public class ThreadDemoApplet extends JApplet {
    private static final long serialVersionUID = 1L;
    private ThreadPanel A, B, C;

    public void init() {
        A = new ThreadPanel("Thread A", Color.blue);
        B = new ThreadPanel("Thread B", Color.blue);
        C = new ThreadPanel("Thread C", Color.blue);

        setLayout(new FlowLayout());
        add(A);
        add(B);
        add(C);
        setBackground(Color.lightGray);
    }

    public void start() {
        Thread t1 = new Thread(A);
        Thread t2 = new Thread(B);
        Thread t3 = new Thread(C);
        t1.start();
        t2.start();
        t3.start();
    }

    public void stop() {
        A.stop();
        B.stop();
        C.stop();
    }
}
