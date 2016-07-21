package controller;

import audio_manager.AudioManager;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import file.FileManager;
import gui.DimensionDialog;
import gui.DraggableImageView;
import gui.SelectableNode;
import gui.SubRegionDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Controller implements Initializable {
	@FXML Button newButton, loadButton, saveButton, exportButton, exitButton,
			addPictureButton, removePictureButton, playButton, recolorButton, dimensionButton;
	@FXML TableColumn nameColumn, leaderColumn, capitalColumn;
	@FXML TableView<SubRegion> table;
	@FXML Pane pane;
	@FXML ColorPicker backgroundCP, borderCP;
	@FXML Slider zoom, borderWidth;
	@FXML TextField mapNameTF;
	@FXML ScrollPane scrollPane;


	private ObservableList<SubRegion> ob = FXCollections.observableArrayList();
	private DataManager dataManager = new DataManager();
	private FileManager fileManager = new FileManager(dataManager, this);
	AudioManager audioManager = new AudioManager();
	private SelectableNode polygonGroup;
	private SelectableNode imageGroup;
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
		nameColumn.setSortable(false);
		leaderColumn.setSortable(false);
		capitalColumn.setSortable(false);

		InnerShadow is = new InnerShadow(0, 2, 2, Color.rgb(206,74,73));
		polygonGroup = new SelectableNode(is) {
			@Override
			public void onMouseClickedHook(MouseEvent e) {
				if(e.getClickCount() == 1){
					table.scrollTo(polygonGroup.getSelectionModel().getSelectedIndex());
					table.getSelectionModel().select(polygonGroup.getSelectionModel().getSelectedIndex());
					table.getFocusModel().focus(polygonGroup.getSelectionModel().getSelectedIndex());
				}
				if(e.getClickCount() == 2){
					subRegionHandler(polygonGroup.getSelectionModel().getSelectedIndex(), table);
				}
			}
		};

		table.setRowFactory(a -> {
			final TableRow<SubRegion> row = new TableRow<>();
			row.setOnMouseClicked(e -> {
				if(row.getIndex()>= table.getItems().size()){
					table.getSelectionModel().clearSelection();
				}
				if(e.getClickCount() == 1){
					if(table.getSelectionModel().getFocusedIndex() >= 0) {
						polygonGroup.setSelected(table.getSelectionModel().getSelectedIndex());
						table.getFocusModel().focus(table.getSelectionModel().getSelectedIndex());
					}else{
						polygonGroup.deselect();
					}
				}
				if(e.getClickCount() == 2){
					if(table.getSelectionModel().getSelectedItem() != null)
						subRegionHandler(table.getSelectionModel().getSelectedIndex(), table);
				}
			});
			return row;
		});

		table.getFocusModel().focusedIndexProperty().addListener(e -> {
			if(table.getSelectionModel().getFocusedIndex() >= 0)
				polygonGroup.setSelected(table.getFocusModel().getFocusedIndex());
		});

		borderCP.setOnAction(e -> dataManager.setBorderColor(borderCP.getValue().toString()));
		backgroundCP.setOnAction(e -> {
			setBackgroundColorPicker();
			dataManager.setBackgroundColor(backgroundCP.getValue().toString());
		});
		zoom.valueProperty().bindBidirectional(dataManager.zoomLevelProperty());
		borderWidth.setOnMouseReleased(e -> dataManager.setBorderWidth(borderWidth.getValue()));
		setKeyPress();
		zoom.setMin(1);
		zoom.setMax(1200);
		borderWidth.setMax(10);
		disableButtons(true);
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
			boolean picture = fileManager.processExportRequest();
			if(picture) {
				polygonGroup.deselect();
				imageGroup.deselect();
				SnapshotParameters snapshotParameters = new SnapshotParameters();
				snapshotParameters.setTransform(Transform.translate(scrollPane.getLayoutX() - pane.getLayoutX(), scrollPane.getLayoutY() - pane.getLayoutY()));
				snapshotParameters.setViewport(new Rectangle2D(0, 0, dataManager.getWidth(), dataManager.getHeight()));
				WritableImage image = pane.snapshot(snapshotParameters, null);
				File file = new File(FileManager.getRegionPath() +"/"+dataManager.getMapName()+ ".png");
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Something went Wrong Exporting");
			alert.show();
		}
	}

	public void setExitButton() {
		System.exit(0);
	}

	public void setAddPictureButton() {
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
		fc.getExtensionFilters().add(ef);
		File image = fc.showOpenDialog(null);
		File dest = new File("src/me/addedImage/"+image.getName());
		try {
			Files.copy(image.toPath(), dest.toPath());
			dataManager.getImages().add(new ImageDetail(dest.getPath().toString(), 0, 0, dataManager.getImages().size()));
		} catch (FileAlreadyExistsException e){
			dataManager.getImages().add(new ImageDetail("src/me/addedImage/"+image.getName(), 0, 0, dataManager.getImages().size()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		addImage();
	}

	public void setRemovePictureButton() {
		if(imageGroup.getSelectionModel().getSelectedItem() != null) {
			dataManager.remove(Integer.parseInt(imageGroup.getSelectionModel().getSelectedItem().getId()));
			imageGroup.getChildren().remove(imageGroup.getSelectionModel().getSelectedItem());
		}
//		ArrayList<ImageDetail> id = dataManager.getImages();
//		for(ImageDetail temp: id)
//			System.out.println(temp);
	}

	public void setPlayButton() {
		String song = dataManager.getMapName() + " National Anthem.mid";
		if(audioManager.isPlaying(song)) {
			audioManager.stop(song);
			File file = new File("src/me/images/Play.png");
			playButton.setGraphic(new ImageView(file.toURI().toString()));
		}else{
			try {
				audioManager.loadAudio(song, FileManager.getRegionPath() + "/" + dataManager.getMapName() + " National Anthem.mid");
				audioManager.play(song, false);
				File file = new File("src/me/images/Stop.png");
				playButton.setGraphic(new ImageView(file.toURI().toString()));
			} catch (UnsupportedAudioFileException e) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "Unsupported Audio Type (supported files : .mid)");
				alert.show();
			} catch (FileNotFoundException ex) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "No National Anthem found in F2older");
				alert.show();
			}
			catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "Something went wrong Loading file");
				alert.show();
			}
		}

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
	}

	public void setBackgroundColorPicker() {
		Paint fill = backgroundCP.getValue();
		pane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	public void reload(){
		pane.getChildren().remove(polygonGroup);
		pane.getChildren().remove(imageGroup);
		init();
		SubRegion[] region = dataManager.getSubRegions();
		for (int i = 0; i < region.length; i++) {
			SubRegion temp = region[i];
			if(first)
				ob.add(temp);
			double[][] ww = temp.getSubPoints();
			Group g = new Group();
			for (double[] f : ww) {
				Polygon polygon = new Polygon(f);
				int j = i;
				polygon.setFill(temp.getColor());
				polygon.strokeWidthProperty().bind(borderWidth.valueProperty().divide(zoom.valueProperty()));
				polygon.strokeProperty().bind(borderCP.valueProperty());
				g.getChildren().add(polygon);
			}
			polygonGroup.add(g);
		}
		polygonGroup.add(new Group());
		pane.getChildren().add(polygonGroup);
		polygonGroup.setTranslateX(dataManager.getTranslatex());
		polygonGroup.setTranslateY(dataManager.getTranslatey());

		table.setItems(ob);
		addImages(); pane.getChildren().add(imageGroup);
		first = false;
		mapNameTF.setText(dataManager.getMapName());
	}

	private void subRegionHandler(int i, TableView table){
		SubRegionDialog srd = new SubRegionDialog(dataManager.getSubRegions(), table);
		table.scrollTo(i);
		table.getSelectionModel().select(i);
		srd.show(i);
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
		pane.setPrefSize(dataManager.getWidth(), dataManager.getHeight());
		polygonGroup.scaleXProperty().bind(zoom.valueProperty());
		polygonGroup.scaleYProperty().bind(zoom.valueProperty());
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
		DropShadow ds = new DropShadow();
		ds.setOffsetY(6);
		ds.setOffsetY(6);
		imageGroup = new SelectableNode(ds) {
			@Override
			public void onMouseClickedHook(MouseEvent e) {}
		};
		for (ImageDetail temp : id) {
			Image image = new Image("file:" + temp.getImage());
			DraggableImageView iv = new DraggableImageView(image, temp);
			iv.setId(temp.getKey() + "");
			iv.setX(temp.getX());
			iv.setY(temp.getY());
			imageGroup.add(iv);
		}
	}

	private void addImage(){
		ArrayList<ImageDetail> id = dataManager.getImages();
		ImageDetail temp = id.get(id.size()-1);
		Image image = new Image("file:" + temp.getImage());
		DraggableImageView iv = new DraggableImageView(image, temp);
		iv.setId(temp.getKey() + "");
		iv.setX(temp.getX());
		iv.setY(temp.getY());
		imageGroup.add(iv);
	}

	public void disableButtons(boolean value) {
		saveButton.setDisable(value);
		exportButton.setDisable(value);
		exitButton.setDisable(value);
		addPictureButton.setDisable(value);
		playButton.setDisable(value);
		recolorButton.setDisable(value);
		dimensionButton.setDisable(value);
		mapNameTF.setDisable(value);
		zoom.setDisable(value);
		borderWidth.setDisable(value);
	}

	public String getMapNameText(){
		return mapNameTF.getText();
	}

	private void setKeyPress(){
		scrollPane.setOnKeyPressed(e -> {
			switch(e.getCode()){
				case UP :
					polygonGroup.setTranslateY(polygonGroup.getTranslateY() - 10);
					dataManager.setTranslatey(polygonGroup.getTranslateY());
					break;
				case DOWN :
					polygonGroup.setTranslateY(polygonGroup.getTranslateY() + 10);
					dataManager.setTranslatey(polygonGroup.getTranslateY());
					break;
				case LEFT :
					polygonGroup.setTranslateX(polygonGroup.getTranslateX() - 10);
					dataManager.setTranslatex(polygonGroup.getTranslateX());
					break;
				case RIGHT :
					polygonGroup.setTranslateX(polygonGroup.getTranslateX() + 10);
					dataManager.setTranslatex(polygonGroup.getTranslateX());
					break;
				default:
					e.consume();
			}

		});
	}
}
