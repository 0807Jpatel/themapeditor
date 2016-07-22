/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import file.FileManager;
import java.awt.Font;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author jappatel
 */
public class ProgressDialog {
    ProgressBar bar;
    Pane pane;
    int numTasks = 0;
    ReentrantLock progressLock;    
    boolean kg = true;
    public void show(){
	PropertiesManager prop = PropertiesManager.getPropertiesManager();
	pane = new Pane();
	pane.setMinWidth(800);
	progressLock = new ReentrantLock();
        bar = new ProgressBar(0);     
	bar.getStyleClass().add("progress-bar");

	bar.prefWidthProperty().bind(pane.widthProperty().subtract(10)); 
	pane.getChildren().add(bar);
        
        Scene scene = new Scene(pane);
	scene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
        Stage stage = new Stage();
	stage.setScene(scene);

	Task<Void> task = new Task<Void>() {
	    int task = numTasks++;
	    double max = 200;
	    double perc;
	    @Override
	    protected Void call() throws Exception {
		try {
		    progressLock.lock();
		while(kg) {
		    perc = FileManager.getDouble();
		    Platform.runLater(new Runnable() {
			@Override
			public void run() {
			    bar.setProgress(perc/19);
			    if(bar.getProgress() == 1){
				kg = false;
				stage.close();
			    }
			}
		    });
		    Thread.sleep(10);		    
		}}
		finally {
		    progressLock.unlock();
			}
		
		return null;
	    }
	};
	Thread thread = new Thread(task);
//	thread.setPriority(10);
	thread.start();                   
       stage.show();
    }
    
}
