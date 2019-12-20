
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Adriano Chiriacò
 */
public class SnowFlakePanel extends JPanel implements MouseMotionListener, MouseListener, SmartPolygonListener {

    private static final Color LINE_COLOR = Color.BLACK;
    private static final int WIDTH_CIRCLE = 10;
    private boolean cut = false;
    private ArrayList<Point> posPoints = new ArrayList<Point>();
    public ArrayList<SmartPolygon> smartPolygons = new ArrayList<SmartPolygon>();
    private final int[] vertexesXTriangle = {374, 578, 374};
    private final int[] vertexesYTriangle = {25, 25, 374};
    private Point apiceTriangolo = new Point(vertexesXTriangle[0],vertexesYTriangle[0]);
    private Polygon triangolo = new Polygon(vertexesXTriangle, vertexesYTriangle, 3);
    private Area aTriangolo = new Area(triangolo);
    private List<Shape> fioccoGenerato = new ArrayList<Shape>();
    private boolean generato = false;

    public SnowFlakePanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setLayout(null);
    }



    public void taglia() {
        this.cut = true;
        repaint();
    }

    public void savePoints() {
        JFileChooser jfc = new JFileChooser("./");

        //int returnValue = jfc.showSaveDialog(null);
        jfc.setDialogTitle("Specify a file to save");

        int userSelection = jfc.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = jfc.getSelectedFile();

            if (!fileToSave.exists()) {
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                try {
                    File file = new File(fileToSave.getAbsolutePath() + ".points");
                    boolean fvar = file.createNewFile();
                    if (fvar) {
                        System.out.println("File has been created successfully");
                    } else {
                        System.out.println("File already present at the specified location");
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave.getAbsolutePath() + ".points"));
                    String str = "";
                    for (int i = 0; i < smartPolygons.size(); i++) {
                        

                        for (int j = 0; j < smartPolygons.get(i).getSize(); j++) {
                            str += smartPolygons.get(i).vertexes.get(j).x + "," + smartPolygons.get(i).vertexes.get(j).y + "\n";
                        }
                        str += "p" + (i + 1) + "\n";
                    }
                    writer.write(str);
                    writer.close();

                } catch (IOException e) {
                    System.out.println("Exception Occurred:");
                }
            } else {
                System.out.println("ERRORE");
            }
        }

    }

    public void reset(){
        this.cut = false;
        this.posPoints.clear();
        this.smartPolygons.clear();
        this.aTriangolo = new Area(triangolo);
        this.generato = false;
        repaint();
    }
    
    public void loadPoints() {
        
        JFileChooser jfc = new JFileChooser("./");
        int returnVal = jfc.showOpenDialog(null);
        File file;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
        } else {
            System.out.println("File doesn't exists.");
            file = null;
        }
        if (file != null) {
            smartPolygons.clear();
            ArrayList listDots = new ArrayList<>();
            try {
                listDots = (ArrayList) Files.readAllLines(file.toPath());
            } catch (IOException ex) {
                System.out.println("Errore di lettura del File");
            }

            int numPolys = 0;
            boolean switchPolygon = false;
            for (int i = 0; i < listDots.size(); i++) {
                String dum = (String) listDots.get(i);
                if (dum.charAt(0) == 'p') {
                    numPolys++;
                }
            }
            String[] data = {"", ""};
            ArrayList<Integer> vertX = new ArrayList<Integer>();
            ArrayList<Integer> vertY = new ArrayList<Integer>();
            System.out.println(listDots.size());
            for (int i = 0; i < listDots.size(); i++) {
                String dum = (String) listDots.get(i);
                if (!(dum.charAt(0) == 'p')) {
                    data = dum.toString().split(",");
                    vertX.add(Integer.parseInt(data[0]));
                    vertY.add(Integer.parseInt(data[1]));
                } else {
                    ArrayList<Point> listaPunti = new ArrayList<Point>();
                    if (i != 0) {
                        for (int j = 0; j < vertX.size(); j++) {
                            Point pDum = new Point((vertX.get(j)), (vertY.get(j)));
                            listaPunti.add(pDum);
                        }
                        smartPolygons.add(new SmartPolygon(listaPunti, WIDTH_CIRCLE));
                        smartPolygons.get(smartPolygons.size()-1).addSmartPolygonListener(this);
                        this.addMouseMotionListener(smartPolygons.get(smartPolygons.size() - 1));
                        this.addMouseListener(smartPolygons.get(smartPolygons.size() - 1));
                        
                        vertX.clear();
                        vertY.clear();
                    }
                }
            }
            System.out.print(numPolys);
        }
        repaint();
    }

    public Shape ruotaTriangolo(Shape fiocco,double angolo){
        AffineTransform ruota = new AffineTransform(); 
        ruota.rotate(Math.toRadians(angolo),apiceTriangolo.getX(),apiceTriangolo.getX());
        return ruota.createTransformedShape(fiocco);
    }
    
    public Shape specchiaTriangolo(Area fiocco){
        AffineTransform af = new AffineTransform();
        af.scale(-1,1);
        AffineTransform afCentralize = new AffineTransform();
        afCentralize.translate(-(apiceTriangolo.getX()* 2),0);
        AffineTransform afTotal = new AffineTransform();
        afTotal.concatenate(af);
        afTotal.concatenate(afCentralize);
        return afTotal.createTransformedShape(fiocco);
    }
    
    public void genera(){
        this.generato = true;
        fioccoGenerato.clear();
        for(int i = 0; i < 36; i+=6){
            Shape gira = specchiaTriangolo(aTriangolo);
            fioccoGenerato.add(ruotaTriangolo(gira,i*10));
            fioccoGenerato.add(ruotaTriangolo(aTriangolo,i*10));
        }
        repaint();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Color background = new Color(207,237,242);
        Color coloreTriangolo = new Color(23,161,200);
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g.create();
        if(!generato){
            if (this.cut == false) {
                g.setColor(coloreTriangolo);
                g.fillPolygon(triangolo);
                g.setColor(Color.YELLOW);
                if (!posPoints.isEmpty()) {

                    for (int i = 0; i < posPoints.size(); i++) {
                        g.setColor(LINE_COLOR);
                        if (i != posPoints.size() - 1) {
                            g.drawLine((int) posPoints.get(i).getX(), (int) posPoints.get(i).getY(), (int) posPoints.get(i + 1).getX(), (int) posPoints.get(i + 1).getY());
                        }
                    }
                    for (int i = 0; i < posPoints.size(); i++) {
                        g.fillOval((int) posPoints.get(i).getX() - WIDTH_CIRCLE / 2, (int) posPoints.get(i).getY() - WIDTH_CIRCLE / 2, WIDTH_CIRCLE, WIDTH_CIRCLE);
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
                g2d.setColor(coloreTriangolo);
                
                g2d.fill(aTriangolo);
            }
        }else{
            g2d.setColor(coloreTriangolo);
            Area totale = new Area();
            for(int i = 0; i < fioccoGenerato.size(); i++){
                Area aDum = new Area(fioccoGenerato.get(i));
                totale.add(aDum);
                
            }
            g2d.translate(50, -10);
            g2d.fill(totale);
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
