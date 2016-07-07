package controller;

import data.DataManager;
import data.SubRegion;
import file.FileManager;
import gui.DimensionDialog;
import gui.SubRegionDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	@FXML Button newButton, loadButton, saveButton, ExportButton, ExitButton,
			addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;
	@FXML TableColumn nameColumn, leaderColumn, capitalColumn;
	@FXML TableView<SubRegion> table;
	@FXML AnchorPane pane;
	@FXML ColorPicker backgroundCP, borderCP;
	@FXML Slider zoom, borderWidth;
	@FXML TextField mapNameTF;

	private ObservableList<SubRegion> ob = FXCollections.observableArrayList();
	private DataManager dataManager = new DataManager();
	private FileManager fileManager = new FileManager(dataManager, this);
	private SubRegion[] subRegionsdata;
	private Color[] grayScaleArray;
	private Group gp;

	/**
	 * All the binding and new data and stuff goes here
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(.32));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		leaderColumn.prefWidthProperty().bind(table.widthProperty().multiply(.32));
		leaderColumn.setCellValueFactory(new PropertyValueFactory<>("leader"));
		capitalColumn.prefWidthProperty().bind(table.widthProperty().multiply(.32));
		capitalColumn.setCellValueFactory(new PropertyValueFactory<>("capital"));
		zoom.setMin(15);
		zoom.setMax(150);
		borderWidth.setMin(1);
		table.setOnMouseClicked(e -> {
			if(e.getClickCount() == 2) {
				SubRegion sb = table.getSelectionModel().getSelectedItem();
				subRegionHandler(sb);
			}
		});
	}


	public void setNewButton() {
		fileManager.processNewRequest();
	}

	public void setLoadButton() {
		fileManager.processLoadRequest();
	}

	public void setSaveButton() {

	}

	public void setExportButton() {

	}

	public void setExitButton() {
		System.exit(0);
	}

	public void setAddPictureButton() {

	}

	public void setRemovePictureButton() {

	}

	public void setPlayButton() {

	}

	public void setRecolorButton() {
		randomizeColor();
		reload();
	}

	public void setDimensionButton() {
		DimensionDialog dd = new DimensionDialog();
		dd.show();
		dataManager.setHeight(dd.getHeight());
		dataManager.setWidth(dd.getWidth());
		pane.getChildren().remove(gp);
		reload();
	}

	public void setBackgroundColorPicker() {
		Paint fill = backgroundCP.getValue();
		pane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	public void enableButtons() {
		saveButton.setDisable(false);
		ExportButton.setDisable(false);
		ExitButton.setDisable(false);
		addPictureButton.setDisable(false);
		playButton.setDisable(false);
		recolorButton.setDisable(false);
		dimensionButton.setDisable(false);
	}

	public void reload(){
		pane.getChildren().remove(gp);
		pane.setMaxWidth(dataManager.getWidth());
		pane.setMaxHeight(dataManager.getHeight());
		gp = new Group();
		pane.scaleXProperty().bind(zoom.valueProperty().divide(15));
		pane.scaleYProperty().bind(zoom.valueProperty().divide(15));
		randomAssignment();
		subRegionsdata = dataManager.getSubRegions();
		for (int i = 0; i < subRegionsdata.length; i++) {
			SubRegion temp = subRegionsdata[i];
			ob.add(temp);
			double[][] ww = temp.getSubPoints();
			for (double[] f : ww) {
				Polygon polygon = new Polygon(f);
				polygon.setId(i+"");
				polygon.setFill(grayScaleArray[i]);
//				System.out.println(polygon.getFill());
				polygon.setOnMouseClicked(e->{
					if(e.getClickCount() == 2)
						subRegionHandler(temp);
				});
				polygon.strokeWidthProperty().bind(borderWidth.valueProperty().divide(14));
				polygon.strokeProperty().bind(borderCP.valueProperty());
				gp.getChildren().add(polygon);
			}
		}
		pane.getChildren().add(gp);
		table.setItems(ob);
		mapNameTF.setText(dataManager.getMapName());
	}

	private void subRegionHandler(SubRegion subRegion){
		SubRegionDialog srd = new SubRegionDialog();
		srd.show(subRegion);
		subRegion.setName(srd.getSubRegionNameString());
		subRegion.setCapital(srd.getSubRegionCapitalString());
		subRegion.setLeader(srd.getSubRegionLeaderString());
		table.scrollTo(subRegion);
	}

	private void randomAssignment(){
		int subregions = dataManager.getSubRegions().length;
		int numberOfColors = 254/subregions;
		grayScaleArray = new Color[subregions];
		int jump = numberOfColors;
		for(int x = 0; x < subregions; x++){
			jump += numberOfColors;
			grayScaleArray[x] = Color.grayRgb(jump);
		}
		randomizeColor();
	}

	private void randomizeColor(){
		Random rgen = new Random();  // Random number generator
		for (int i=0; i<grayScaleArray.length; i++) {
			int randomPosition = rgen.nextInt(grayScaleArray.length);
			Color temp = grayScaleArray[i];
			grayScaleArray[i] = grayScaleArray[randomPosition];
			grayScaleArray[randomPosition] = temp;
		}
	}
}
