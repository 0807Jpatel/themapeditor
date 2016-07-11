package controller;

import data.DataManager;
import data.ImageDetail;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	@FXML Button newButton, loadButton, saveButton, exportButton, exitButton,
			addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;
	@FXML TableColumn nameColumn, leaderColumn, capitalColumn;
	@FXML TableView<SubRegion> table;
	@FXML Pane pane;
	@FXML ColorPicker backgroundCP, borderCP;
	@FXML Slider zoom, borderWidth;
	@FXML TextField mapNameTF;

	private ObservableList<SubRegion> ob = FXCollections.observableArrayList();
	private DataManager dataManager = new DataManager();
	private FileManager fileManager = new FileManager(dataManager, this);
	private Group gp;
	private boolean first = true;
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

		dataManager.mapNameProperty().bindBidirectional(mapNameTF.textProperty());

		borderCP.setOnAction(e -> dataManager.setBorderColor(borderCP.getValue().toString()));
		backgroundCP.setOnAction(e -> {
			setBackgroundColorPicker();
			dataManager.setBackgroundColor(backgroundCP.getValue().toString());
		});
		zoom.setOnMouseReleased(e -> dataManager.setZoomLevel(zoom.getValue()));
		borderWidth.setOnMouseReleased(e -> dataManager.setBorderWidth(borderWidth.getValue()));

		zoom.setMin(1);
		zoom.setMax(1000);
		borderWidth.setMax(2);
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
		try {
			fileManager.processSaveRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExportButton() {
		try {
			fileManager.processExportRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		exportButton.setDisable(false);
		exitButton.setDisable(false);
		addPictureButton.setDisable(false);
		playButton.setDisable(false);
		recolorButton.setDisable(false);
		dimensionButton.setDisable(false);
	}

	public void reload(){
		pane.getChildren().remove(gp);
		init();
		SubRegion[] region = dataManager.getSubRegions();
		for (int i = 0; i < region.length; i++) {
			SubRegion temp = region[i];
			if(first)
				ob.add(temp);
			double[][] ww = temp.getSubPoints();
			for (double[] f : ww) {
				Polygon polygon = new Polygon(f);
				polygon.setId(i+"");
				polygon.setFill(temp.getColor());
//				System.out.println(temp.getColor());
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
		addImages();
		first = false;
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

	private void randomizeColor(){
		Random rgen = new Random();  // Random number generator
		SubRegion[] x = dataManager.getSubRegions();
		for (SubRegion aX : x) {
			int randomPosition = rgen.nextInt(x.length);
			Color temp = aX.getColor();
			aX.setColor(x[randomPosition].getColor());
			x[randomPosition].setColor(temp);
		}
	}

	private void init(){
		pane.setMaxWidth(dataManager.getWidth());
		pane.setMinWidth(dataManager.getWidth());
		pane.setMaxHeight(dataManager.getHeight());
		pane.setMinHeight(dataManager.getHeight());

		gp = new Group();
		gp.scaleXProperty().bind(zoom.valueProperty());
		gp.scaleYProperty().bind(zoom.valueProperty());
	    Rectangle clip = new Rectangle(dataManager.getWidth(), dataManager.getHeight());
	    pane.setClip(clip);
		mapNameTF.setText(dataManager.getMapName());
		backgroundCP.setValue(Color.valueOf(dataManager.getBackgroundColor()));
		borderCP.setValue(Color.valueOf(dataManager.getBorderColor()));
		setBackgroundColorPicker();
		zoom.setValue(dataManager.getZoomLevel());
		borderWidth.setValue(dataManager.getBorderWidth());

	}

	private void addImages(){
		ArrayList<ImageDetail> id = dataManager.getImages();
		for(ImageDetail temp : id){
			Image image = new Image("file:" + temp.getImage());
			ImageView iv = new ImageView(image);
			iv.setX(temp.getX());
			iv.setY(temp.getY());
			pane.getChildren().add(iv);
		}
	}
}
