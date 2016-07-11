/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import file.FileManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author jappatel
 */
public class ProgressDialog {
    ProgressBar progressBar;
    
    
    public void show(){
    
    	Task task = new Task() {
	    @Override
	    protected Object call() throws Exception {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
			progressBar.setProgress(FileManager.getDouble()/6.0);
		    }
		});
		return null;
	    }
	};
	
	
	
    }
    
}
