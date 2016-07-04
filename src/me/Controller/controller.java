package Controller;

import data.DataManger;
import file.FileManager;
import gui.DimensionDialog;
import gui.NewDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class controller implements Initializable {
	@FXML
	Button newButton, loadButton, saveButton, ExportButton, ExitButton, addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;

	@FXML
	TableColumn nameColumn, leaderColumn, capitalColumn;

	@FXML
	TableView table;

	@FXML
	FlowPane flowPane;

	DataManger dataManger = new DataManger();
	FileManager fileManager = new FileManager(dataManger);


	public void setNewButton(){
		NewDialog nd = new NewDialog();
		nd.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(.3334));
		leaderColumn.prefWidthProperty().bind(table.widthProperty().multiply(.33334));
		capitalColumn.prefWidthProperty().bind(table.widthProperty().multiply(.33334));

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
