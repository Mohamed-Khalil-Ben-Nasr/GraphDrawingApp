package edu.lawrence.graphdrawing;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PrimaryController implements Initializable {
    @FXML private VBox vBox;
    private GraphPane graphPane;
    
    @FXML
    private void newGraph(ActionEvent event) {
        graphPane.clear();
    }
    
    @FXML
    private void save(ActionEvent event) {
        graphPane.save();
    }
    
    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    private void newVertex(ActionEvent event)  {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("New Vertex");
        dialog.setHeaderText(null);
        dialog.setContentText("Label:");

        Optional<String> result = dialog.showAndWait();

        /*result.ifPresent(label -> {
            graphPane.newVertex(label);
        });*/
        if(result.isPresent()) {
            String label = result.get();
            graphPane.newVertex(label);
        }
    }
    
    @FXML
    private void newEdge(ActionEvent event)  {
        graphPane.newEdge();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphPane = new GraphPane();
        vBox.getChildren().add(graphPane);
    }
    
    @FXML
    public void showMST(){
        graphPane.computeMST();   
    }
}
