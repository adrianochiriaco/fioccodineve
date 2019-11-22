
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Adriano Chiriacò
 */
public class SnowFlakePanel extends JPanel implements MouseMotionListener, MouseListener, SmartPolygonListener {

    private static final Color LINE_COLOR = Color.ORANGE;
    private static final int WIDTH_CIRCLE = 8;
    private boolean cut = false;
    private ArrayList<Point> posPoints = new ArrayList<Point>();
    private ArrayList<SmartPolygon> smartPolygons = new ArrayList<SmartPolygon>();
    private final int[] vertexesXTriangle = {375, 575, 375};
    private final int[] vertexesYTriangle = {25, 25, 321};
    private Polygon triangolo = new Polygon(vertexesXTriangle, vertexesYTriangle, 3);
    private Area aTriangolo = new Area(triangolo);

    public SnowFlakePanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setLayout(null);
    }

    public void setCut(boolean cut) {
        this.cut = cut;
    }

    public void taglia() {
        setCut(true);
        repaint();
    }

    public void savePoints() {
        JFileChooser jfc = new JFileChooser("./");

        int returnValue = jfc.showSaveDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            if(selectedFile.exists()){
                JOptionPane.showInputDialog("Ciao");
            }
            System.out.println(selectedFile.getAbsolutePath());
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (this.cut == false) {

            g.fillPolygon(triangolo);
            g.setColor(Color.YELLOW);
            if (!posPoints.isEmpty()) {
                for (int i = 0; i < posPoints.size(); i++) {
                    g.fillOval((int) posPoints.get(i).getX() - WIDTH_CIRCLE / 2, (int) posPoints.get(i).getY() - WIDTH_CIRCLE / 2, WIDTH_CIRCLE, WIDTH_CIRCLE);
                }
                for (int i = 0; i < posPoints.size(); i++) {
                    g.setColor(LINE_COLOR);
                    if (i != posPoints.size() - 1) {
                        g.drawLine((int) posPoints.get(i).getX(), (int) posPoints.get(i).getY(), (int) posPoints.get(i + 1).getX(), (int) posPoints.get(i + 1).getY());
                    }
                }
            }
            if (!smartPolygons.isEmpty()) {
                for (int i = 0; i < smartPolygons.size(); i++) {
                    smartPolygons.get(i).paintComponent(g);
                }
            }
        } else {
            for (int i = 0; i < smartPolygons.size(); i++) {
                Polygon dum = new Polygon(smartPolygons.get(i).getVertexesX(), smartPolygons.get(i).getVertexesY(), smartPolygons.get(i).getSize());
                Area aDum = new Area(dum);
                aTriangolo.subtract(aDum);
            }
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.fill(aTriangolo);
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!posPoints.isEmpty()) {
                if (e.getPoint().distance(posPoints.get(0)) < WIDTH_CIRCLE / 2) {
                    smartPolygons.add(new SmartPolygon((ArrayList) posPoints.clone(), WIDTH_CIRCLE));
                    smartPolygons.get(smartPolygons.size() - 1).addSmartPolygonListener(this);
                    this.addMouseMotionListener(smartPolygons.get(smartPolygons.size() - 1));
                    this.addMouseListener(smartPolygons.get(smartPolygons.size() - 1));
                    posPoints.clear();
                    repaint();
                } else {
                    posPoints.add(e.getPoint());
                    repaint();
                }
            } else {
                posPoints.add(e.getPoint());
                repaint();
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

    public void confermaPoligono() {
    }

    public void vertexMoved() {
        repaint();
    }

    public void vertexRemoved() {
        repaint();
    }

}
