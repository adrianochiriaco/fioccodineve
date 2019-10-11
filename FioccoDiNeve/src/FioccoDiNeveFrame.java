
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Adriano Chiriacò
 */
public class FioccoDiNeveFrame extends JFrame implements MouseMotionListener, MouseListener, ActionListener {

    private static final int WIDTH_CIRCLE = 16;
    private boolean closed = false;
    private List<Point> posPoints = new ArrayList<Point>();
    protected static Button taglia = new Button("Taglia");
    protected static Button exit = new Button("Esci");

    public FioccoDiNeveFrame(String title) {
        super(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension finestra = new Dimension(1024, 768);
        this.setSize(finestra);
        this.setMinimumSize(finestra);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setLayout(null);
        taglia.addActionListener(this);
        exit.addActionListener(this);

    }

    public static void main(String[] args) {
        FioccoDiNeveFrame fdnf = new FioccoDiNeveFrame("Fiocco Di Neve");

        taglia.setActionCommand("Taglia");
        taglia.setBounds(920, 70, 70, 70);
        fdnf.add(taglia);
        exit.setActionCommand("Esci");
        exit.setBounds(920, 640, 70, 70);
        fdnf.add(exit);

        fdnf.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        //Disegno Sfondo.
        Color azzurro = new Color(79, 101, 163);
        g.setColor(azzurro);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        int[] xPoints = {170, 410, 410};
        int[] yPoints = {150, 150, 660};

        //Disegno Triangolo.
        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 3);

        if (closed == false) {
            for (int i = 0; i < posPoints.size(); i++) {
                g.setColor(Color.RED);
                
                //Disegno cerchi
                g.fillOval((int) (posPoints.get(i).getX() - WIDTH_CIRCLE / 2), 
                        (int) (posPoints.get(i).getY() - WIDTH_CIRCLE / 2), 
                        WIDTH_CIRCLE, 
                        WIDTH_CIRCLE
                );
                
                if (i != 0) {
                    g.setColor(Color.GRAY);
                    
                    //Disegno righe
                    g.drawLine((int) posPoints.get(i - 1).getX(), 
                            (int) posPoints.get(i - 1).getY(), 
                            (int) posPoints.get(i).getX(), 
                            (int) posPoints.get(i).getY()
                    );
                    
                    g.setColor(Color.YELLOW);
                    
                    //Disegno riga di chiusura
                    g.drawLine(
                            (int) posPoints.get(posPoints.size() - 1).getX(),
                            (int) posPoints.get(posPoints.size() - 1).getY(), 
                            (int) posPoints.get(0).getX(), 
                            (int) posPoints.get(0).getY()
                    );
                    
                }
            }
        } else {
            int[] fillX = new int[posPoints.size()];
            int[] fillY = new int[posPoints.size()];
            for (int i = 0; i < posPoints.size(); i++) {
                fillX[i] = posPoints.get(i).x;
                fillY[i] = posPoints.get(i).y;
            }
            Polygon fill = new Polygon(fillX, 
                    fillY, 
                    posPoints.size()
            );
            g.setColor(azzurro);
            g.fillPolygon(fill);
        }
    }

    public void mouseDragged(MouseEvent e) {
        for (int i = 0; i < posPoints.size(); i++) {
            if (e.getPoint().distance(posPoints.get(i)) < WIDTH_CIRCLE / 2) {
                posPoints.set(i, e.getPoint());
                repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (closed == false) {
                posPoints.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            for (int i = 0; i < posPoints.size(); i++) {
                if (e.getPoint().distance(posPoints.get(i)) < WIDTH_CIRCLE / 2) {
                    posPoints.remove(i);
                    repaint();
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        if ("Taglia".equals(e.getActionCommand()) && posPoints.size() > 2) {
            this.closed = true;
            repaint();
        }else if ("Esci".equals(e.getActionCommand())) {
            dispose();
        }
    }
}
