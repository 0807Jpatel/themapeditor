package Controller;

import data.DataManger;
import file.FileManager;
import gui.DimensionDialog;
import gui.NewDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class controller implements Initializable {
	@FXML
	Button newButton, loadButton, saveButton, ExportButton, ExitButton, addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;

	DataManger dataManger = new DataManger();
	FileManager fileManager = new FileManager(dataManger);


	public void setNewButton(){
		fileManager.processNewRequest();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setDimensionButton(){
		DimensionDialog dd = new DimensionDialog();
		dd.show();
	}

	public void enableButtons(){
		saveButton.setDisable(false);
		ExportButton.setDisable(false);
		ExitButton.setDisable(false);
		addPictureButton.setDisable(false);
		playButton.setDisable(false);
		recolorButton.setDisable(false);
		dimensionButton.setDisable(false);
	}
}
