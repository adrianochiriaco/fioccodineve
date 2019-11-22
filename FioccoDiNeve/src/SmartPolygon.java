
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * È un modello di un poligono intelligente.
 *
 * @version 8-nov-2019
 * @author Adriano Chiriacò
 */
public class SmartPolygon implements MouseMotionListener, MouseListener {

    /**
     * Lista di vertici che definiscono il poligono.
     */
    private ArrayList<Point> vertexes = new ArrayList<Point>();

    /**
     * Definisce il colore dei poligoni.
     */
    private static final Color POLYGONCOLOR = Color.RED;

    /**
     * La larghezza del cerchio generato sul vertice del poligono.
     */
    private int widthCircle = 2;

    /**
     * Lista di listener.
     */
    private ArrayList<SmartPolygonListener> listeners = new ArrayList<SmartPolygonListener>();

    /**
     * Metodo costruttore.
     *
     * @param vertexes La lista dei vertici che definiscono il poligono.
     */
    public SmartPolygon(ArrayList<Point> vertexes, int widthCircle) {
        this.vertexes = vertexes;
        this.widthCircle = widthCircle;
    }

    /**
     * Ritorna le coordinate X dei vertici di uno smartPolygon.
     * @return Ritorna le coordinate X dei vertici di uno smartPolygon.
     */
    public int[] getVertexesX(){
        int[] vertexesX = new int[this.vertexes.size()];
        for(int i = 0; i < vertexesX.length; i++){
            vertexesX[i] = (int)this.vertexes.get(i).getX();
        }
        return vertexesX;
    }
    
    /**
     * Ritorna le coordinate X dei vertici di uno smartPolygon.
     * @return Ritorna le coordinate X dei vertici di uno smartPolygon.
     */
    public int[] getVertexesY(){
        int[] vertexesY = new int[this.vertexes.size()];
        for(int i = 0; i < vertexesY.length; i++){
            vertexesY[i] = (int)this.vertexes.get(i).getY();
        }
        return vertexesY;
    }
    
    /**
     * Ritorna la grandezza di un smartPolygon.
     * @return Ritorna la grandezza di un smartPolygon. 
     */
    public int getSize(){
        return this.vertexes.size();
    }
    
    /**
     * Metodo di painting.
     *
     * @param g Il componente grafico.
     */
    public void paintComponent(Graphics g) {
        int[] vertexesX = new int[this.vertexes.size()];
        int[] vertexesY = new int[this.vertexes.size()];
        for (int i = 0; i < this.vertexes.size(); i++) {
            vertexesX[i] = (int) this.vertexes.get(i).getX();
            vertexesY[i] = (int) this.vertexes.get(i).getY();
        }
        g.setColor(POLYGONCOLOR);
        g.fillPolygon(vertexesX, vertexesY, vertexes.size());
        
            for(int i = 0; i < vertexes.size(); i++){
                g.fillOval((int)vertexes.get(i).getX()- widthCircle/2, (int)vertexes.get(i).getY()-widthCircle/2, widthCircle, widthCircle);
            }
    }

    public void mouseDragged(MouseEvent e) {
        for (int i = 0; i < vertexes.size(); i++) {
            if (e.getPoint().distance(vertexes.get(i)) < widthCircle / 2) {
                vertexes.set(i, e.getPoint());
                for (SmartPolygonListener element : listeners) {
                    element.vertexMoved();
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (int i = 0; i < vertexes.size(); i++) {
                if (e.getPoint().distance(vertexes.get(i)) < widthCircle / 2) {
                    vertexes.remove(i);
                    for (SmartPolygonListener element : listeners) {
                        element.vertexRemoved();
                    }
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

    /**
     * Aggiunge un ascoltatore alla lista listeners.
     *
     * @param listeners Elemento da aggiungere a listeners.
     */
    public void addSmartPolygonListener(SmartPolygonListener listener) {
        listeners.add(listener);
    }

    /**
     * Rimuove un ascoltatore dalla lista listeners.
     *
     * @param listeners Elemento da aggiungere a listeners.
     */
    public void removeSmartPolygonListener(SmartPolygonListener listener) {
        listeners.remove(listener);
    }
}
