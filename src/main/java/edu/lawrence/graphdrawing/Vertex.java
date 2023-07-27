package edu.lawrence.graphdrawing;

import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Vertex {
    private String label;
    private Circle circle;
    private Text text;
    private boolean marked;
    
    public Vertex(String lbl,double x,double y) {
        label = lbl;
        circle = new Circle(x,y,15);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        text = new Text(x,y,label);
        marked = false;
    }
    
    public boolean isMarked() { return marked; }
    public void setMark(boolean m) {
        marked = m;
        if(marked == true)
            circle.setStroke(Color.RED);
        else
            circle.setStroke(Color.BLACK);
    }
    public void save(PrintWriter w) {
        w.println(label+ " " + circle.getCenterX() + " " + circle.getCenterY());
    }
    public Circle getCircle() {return circle;}
    public String getLabel() { return label; }
    public double getCenterX() { return circle.getCenterX(); }
    public double getCenterY() { return circle.getCenterY(); }
    
    public void moveBy(double deltaX,double deltaY) {
        circle.setCenterX(circle.getCenterX() + deltaX);
        circle.setCenterY(circle.getCenterY() + deltaY);
        adjustLocations();
    }
    
    public boolean containsPoint(double x,double y) {
        double deltaX = x - getCenterX();
        double deltaY = y - getCenterY();
        return deltaX*deltaX + deltaY*deltaY <= 15*15;
    }
    
    public ArrayList<Shape> getShapes() {
        ArrayList<Shape> result = new ArrayList<Shape>();
        result.add(circle);
        result.add(text);
        return result;
    }
    
    public void adjustLocations() {
        Bounds bounds = text.getBoundsInLocal();
        
        text.setX(circle.getCenterX()- bounds.getWidth()/2);
        text.setY(circle.getCenterY() + bounds.getHeight()/4);
    }
}
