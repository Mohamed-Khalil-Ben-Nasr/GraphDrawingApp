package edu.lawrence.graphdrawing;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class GraphPane extends Pane {
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;
    private Vertex draggingV;
    private double lastX;
    private double lastY;
    private Vertex dragSource;
    private Line dragLine;
    private boolean drawingEdge;
    
    public void computeMST(){ 
        //step 1 in algorithm
        for(Vertex v : vertices){
                v.getCircle().setStroke(Color.BLACK);       
        }
        for(Edge e: edges){
            e.getLine().setFill(Color.BLACK);
        }
        
        //step 2 in algorithm
        Vertex initial = vertices.get(0);
        initial.getCircle().setStroke(Color.RED);
        /** I added this: **/ initial.setMark(true);
        //step 3 in algorithm
        Timeline MSTAnimation = new Timeline(
                new KeyFrame(new Duration(1000.0), t -> {
                    int minWeightCo = 100000000;
                    Edge edgeUsed = null;
                    for (Edge e: edges){
                        Vertex src = e.getSource();
                        Vertex dest = e.getDestination(); 
                        if (src.isMarked() != dest.isMarked()){
                            int w = Integer.parseInt(e.getWeight());
                            if (w < minWeightCo){
                                minWeightCo = w;
                                edgeUsed = e;
                            }
                        }
                    } 
                    /** I added this if statement: **/
                    if(edgeUsed != null) {
                        edgeUsed.setMark(true);
                        edgeUsed.getDestination().setMark(true);
                        edgeUsed.getSource().setMark(true);
                    }
                })
        );
        MSTAnimation.setCycleCount(vertices.size());
        MSTAnimation.play();     
    }
    
    public GraphPane() {
        vertices = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        draggingV = null;
        dragSource = null;
        dragLine = null;
        drawingEdge = false;
        Scanner input = null;
        try {
            input = new Scanner(new File("graph.txt"));
            TreeMap<String,Vertex> map = new TreeMap<String,Vertex>();
            int vCount = input.nextInt();
            for(int n = 0;n < vCount;n++) {
                String label = input.next();
                double x = input.nextDouble();
                double y = input.nextDouble();
                Vertex v = new Vertex(label,x,y);
                map.put(label, v);
                vertices.add(v);
                this.getChildren().addAll(v.getShapes());
                v.adjustLocations();
            }
            int eCount = input.nextInt();
            for(int n = 0;n < eCount;n++) {
                String source = input.next();
                String dest = input.next();
                String w = input.next();
                Vertex src = map.get(source);
                Vertex dst = map.get(dest);
                if(src != null && dst != null && w!=null) {
                    Edge e = new Edge(w,src,dst);
                    edges.add(e);
                    this.getChildren().addAll(0,e.getShapes());
                }
            }
        } catch(Exception ex) {
        }
        this.setOnMousePressed(e -> startDrag(e));
        this.setOnMouseDragged(e -> continueDrag(e));
        this.setOnMouseReleased(e -> endDrag(e));
    }
    
    public void clear() {
        vertices.clear();
        edges.clear();
        this.getChildren().clear();
    }
    
    public void save() {
       PrintWriter output = null;
        try { 
            output = new PrintWriter(new File("graph.txt"));
            output.println(vertices.size());
            for(Vertex v : vertices) {
                v.save(output);
            }
            output.println(edges.size());
            for(Edge e : edges) {
                e.save(output);
            }
            output.close();
        } catch(Exception ex) {
            System.out.println("Error writing data to text file");
        } 
    }
    
    public void newVertex(String label) {
        Vertex v = new Vertex(label,this.getWidth()/2,this.getHeight()/2);
        vertices.add(v);
        this.getChildren().addAll(v.getShapes());
        v.adjustLocations();
        drawingEdge = false;
    }
    
    public void newEdge() {
        drawingEdge = true;
    }
    
    public void startDrag(MouseEvent e) {
        if(drawingEdge) {
            dragSource = null;
            dragLine = null;
            for(Vertex v : vertices) {
                if(v.containsPoint(e.getX(), e.getY()))
                    dragSource = v;
            }
            if(dragSource != null) {
                dragLine = new Line(dragSource.getCenterX(),dragSource.getCenterY(),e.getX(),e.getY());
                this.getChildren().add(dragLine);
            }
        } else {
            draggingV = null;
            for(Vertex v : vertices) {
                if(v.containsPoint(e.getX(), e.getY()))
                    draggingV = v;
            }
            lastX = e.getX();
            lastY = e.getY();
        }
        

    }

    public void continueDrag(MouseEvent e) {
        if(drawingEdge) {
            if(dragLine != null) {
                dragLine.setEndX(e.getX());
                dragLine.setEndY(e.getY());
            }
        } else {
            if(draggingV != null) {
                double deltaX = e.getX() - lastX;
                double deltaY = e.getY() - lastY;
                draggingV.moveBy(deltaX, deltaY);
                adjustEdges();
                lastX = e.getX();
                lastY = e.getY();
            }
        }
    }
    
    public void endDrag(MouseEvent e) {
        if(drawingEdge) {
            Vertex dragDest = null;
            for(Vertex v : vertices) {
                if(v.containsPoint(e.getX(), e.getY()))
                    dragDest = v;
            }
            if(dragDest != null && dragDest != dragSource) {
                // Put up a dialog to ask the user for the weight
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("New Edge");
                dialog.setHeaderText(null);
                dialog.setContentText("Weight:");

                Optional<String> result = dialog.showAndWait();
                String weight = null;
                /*result.ifPresent(label -> {
                    graphPane.newVertex(label);
                });*/
                if(result.isPresent()) {
                    weight = result.get();
                    Edge edge = new Edge(weight,dragSource,dragDest);
                    edges.add(edge);
                    this.getChildren().addAll(0, edge.getShapes());
                    edge.adjustLocations();
                }  
                
            }
            this.getChildren().remove(dragLine);
        }
    }
    
    public void adjustEdges() {
        for(Edge e : edges) {
            e.adjustLocations();
        }
    }
    
    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }

    @Override
    protected double computePrefWidth(double height) {
        return 400;
    }
    
    
}


/** Almost perfect, with one very small error. See my comment above for details.
 * 
 *  59/60
 */