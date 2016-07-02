package Controller;

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

	public void setNewButton(){
		NewDialog nd = new NewDialog();
		nd.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setDimensionButton(){
		DimensionDialog dd = new DimensionDialog();
		dd.show();
	}
}
