package Controller;

import data.DataManager;
import data.SubRegion;
import file.FileManager;
import gui.DimensionDialog;
import gui.NewDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.ResourceBundle;

public class controller implements Initializable {
	@FXML Button newButton, loadButton, saveButton, ExportButton, ExitButton,
			addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;
	@FXML TableColumn nameColumn, leaderColumn, capitalColumn;
	@FXML TableView table;
	@FXML FlowPane flowPane;
	@FXML ColorPicker backgroundCP, borderCP;
	@FXML Slider zoom, borderWidth;

	DataManager DataManager = new DataManager();
	FileManager fileManager = new FileManager(DataManager);


	public void setNewButton(){
		fileManager.processNewRequest();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(.3334));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		leaderColumn.prefWidthProperty().bind(table.widthProperty().multiply(.33334));
		leaderColumn.setCellValueFactory(new PropertyValueFactory<>("leader"));
		capitalColumn.prefWidthProperty().bind(table.widthProperty().multiply(.33334));
		capitalColumn.setCellValueFactory(new PropertyValueFactory<>("capital"));

		ObservableList ob =  FXCollections.observableArrayList(
				new SubRegion("SubRegion1", "Leader1", "Capital1"),
				new SubRegion("SubRegion2", "Leader2", "Capital2"),
				new SubRegion("SubRegion3", "Leader3", "Capital3")
		);

		zoom.setMin(1);
		table.setItems(ob);
	}

	public void setDimensionButton(){
		DimensionDialog dd = new DimensionDialog();
		dd.show();
	}

	public void setBackgroundColorPicker(){
		Paint fill = backgroundCP.getValue();
		flowPane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY,	Insets.EMPTY)));
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
