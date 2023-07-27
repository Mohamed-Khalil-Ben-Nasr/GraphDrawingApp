package edu.lawrence.graphdrawing;

import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Edge {
    private Vertex source, destination;
    private Line line;
    private String weight;
    private Text text; 
    private boolean marked;
    
    public Edge(String w,Vertex src,Vertex dest) {
        weight = w;
        source = src;
        destination = dest;
        line = new Line(src.getCenterX(),src.getCenterY(),dest.getCenterX(),dest.getCenterY());
        //part added
        text = new Text((destination.getCenterX()+source.getCenterX())/2,(destination.getCenterY()+source.getCenterY())/2,weight);
    }
    
    public boolean isMarked() { return marked; }
    public void setMark(boolean m) {
        marked = m;
        if(marked == true)
            line.setStroke(Color.RED);
        else
            line.setStroke(Color.BLACK);
    }
    
    public void save(PrintWriter w) {
        w.println(source.getLabel()+ " " + destination.getLabel()+ "" + weight);
    }
    
    public void adjustLocations() {
        line.setStartX(source.getCenterX());
        line.setStartY(source.getCenterY());
        line.setEndX(destination.getCenterX());
        line.setEndY(destination.getCenterY());
        //part added
        Bounds bounds = text.getBoundsInLocal();
        text.setX((destination.getCenterX()+source.getCenterX())/2- bounds.getWidth()/2);
        text.setY((destination.getCenterY()+source.getCenterY())/2 + bounds.getHeight()/4);
    }
    public String getWeight() { return weight; }
    public Line getLine(){ return line;}
    public Vertex getSource() {return source;}
    public Vertex getDestination() {return destination;}
    
    public ArrayList<Shape> getShapes() {
        ArrayList<Shape> result = new ArrayList<Shape>();
        result.add(line);
        result.add(text);
        return result;
    }
    
    
}
