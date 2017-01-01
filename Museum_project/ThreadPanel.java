package cis461.chap3.museumproj;
//@author: j.n.magee 11/11/96 revised 8.8.2004

// CIS 461: Formal Methods for Software Engineering
// Revised by Dr. Haiping Xu October 5, 2012



import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ThreadPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private volatile boolean stop = false;
    private JButton run;
    private JButton pause;

    private GraphicCanvas display;
    private volatile boolean suspended = true;
    private int angle = 0;
    private int rate;
    private final static int step = 5;

    public ThreadPanel(String title, Color c) {
        this.setFont(new Font("Helvetica", Font.BOLD, 14));
        JPanel p = new JPanel();
        p.add(run = new JButton("Run"));

        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activate();
            }
        });

        p.add(pause = new JButton("Pause"));

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passivate();
            }
        });

        setLayout(new BorderLayout());
        add("South", p);
        display = new GraphicCanvas(title, c);
        add("North", display);
        setBackground(Color.lightGray);
    }

    public void stop() {
        stop = true;
        //this.interrupt();
    }

    void passivate() {
        if (!suspended) {
            suspended = true;
            display.setColor(Color.yellow);
        }
    }

    void activate() {
        if (suspended) {
            suspended = false;
            display.setColor(Color.green);
            // synchronized(this) { notify(); }
        }
    }

    public void run() {
        stop = false;
        rate = 100;    // 100 milliseconds
        suspended = true;
        display.setColor(Color.yellow);

        try {
            //synchronized(this) { while (suspended) wait(); }
            while (suspended) Thread.sleep(rate);
            display.setColor(Color.green);
            display.setAngle(0);
            while(!stop) {         // rotate
                //synchronized(this) { while (suspended) wait(); }
                while (suspended) Thread.sleep(rate);
                angle = (angle + step) % 360;
                display.setAngle(angle);
                Thread.sleep(rate);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     }
}


class GraphicCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    private int angle = 0;
    private String title;
    private Color arcColor = Color.blue;
    private int segStart = 9999;
    private int segEnd = 9999;
    private Color segColor = Color.yellow;

    private Font f1 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    private final static int Cx = 30;
    private final static int Cy = 45;

    public GraphicCanvas(String title, Color c) {
        super();
        this.title = title;
        setSize(150,150);
        arcColor = c;
      }

    public void setColor(Color c){
        setBackground(c);
        repaint();
    }

    public void setAngle(int a){
        angle = a;
        repaint();
    }

    public void paint(Graphics g){
        update(g);
    }

    private Image offscreen;
    private Dimension offscreensize;
    private Graphics offgraphics;

    public synchronized void update(Graphics g){
        Dimension d = getSize();
        if ((offscreen == null) || (d.width != offscreensize.width)
                                || (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
         }
        offgraphics.setColor(getBackground());
        offgraphics.fillRect(0, 0, d.width, d.height);

        // Display the title
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f1);
        FontMetrics fm = offgraphics.getFontMetrics();
        int w = fm.stringWidth(title);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h;
        offgraphics.drawString(title, x, y);
        offgraphics.drawLine(x, y+3, x+w, y+3);
        
        // Display the arc
        if (angle > 0) {
            if (angle<segStart || segStart==segEnd) {
                offgraphics.setColor(arcColor);
                offgraphics.fillArc(Cx, Cy, 90, 90, 0, angle);
            } else if ( angle>=segStart && angle<segEnd) {
                offgraphics.setColor(arcColor);
                offgraphics.fillArc(Cx, Cy, 90, 90, 0, segStart);
                if (angle-segStart > 0) {
                    offgraphics.setColor(segColor);
                    offgraphics.fillArc(Cx, Cy, 90, 90, segStart, angle-segStart);
                }
            } else  {
                offgraphics.setColor(arcColor);
                offgraphics.fillArc(Cx, Cy, 90, 90, 0, segStart);
                offgraphics.setColor(segColor);
                offgraphics.fillArc(Cx, Cy, 90, 90, segStart, segEnd-segStart);
                if (angle-segEnd > 0){
                    offgraphics.setColor(arcColor);
                    offgraphics.fillArc(Cx, Cy, 90, 90, segEnd, angle-segEnd);
                }
            }
        }
        g.drawImage(offscreen, 0, 0, null);
    }
}