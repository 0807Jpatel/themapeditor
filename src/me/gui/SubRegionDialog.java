package gui;

import data.DataManager;
import data.SubRegion;
import file.FileManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

import java.io.File;

/**
 * Created by jappatel on 6/29/16.
 */
public class SubRegionDialog {
	private final String GRIDPANE_STYLE = "gridpane";
	private final String BUTTON_WIDTH = "buttonWidth";
	private final String HBOX = "hBox";
	private final String PREVIOUS_BUTTON = "previousButton";
	private final String NEXT_BUTTON = "nextButton";
	private final String NEXTPREV_HBOX = "nextPrev";

	private GridPane gp;
	private TextField subRegionName;
	private TextField subRegionCapital;
	private TextField subRegionLeader;
	private SubRegion subRegion;
	private PropertiesManager prop;
	private ImageView leaderiv;
	private ImageView flagiv;
	private SubRegion[] data;
	private IntegerProperty index;
	private TableView table;

	public SubRegionDialog(SubRegion[] data, TableView table){
		this.data = data;
		this.table = table;
	}

	public void show(int i){
		this.index = new SimpleIntegerProperty(i);
		subRegion = data[index.get()];
		table.getSelectionModel().select(index.get());
		table.getFocusModel().focus(index.get());
		prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setHeight(400);
		dim.setWidth(450);
		dim.initModality(Modality.APPLICATION_MODAL);
		dim.setTitle(prop.getProperty(PropertyType.SUBREGION_TITLE_LABEL));

		gp = new GridPane();


		HBox nextPrevious = new HBox();
		nextPrevious.getStyleClass().add(NEXTPREV_HBOX);
		Button previous = new Button();
		previous.getStyleClass().add(PREVIOUS_BUTTON);

		Button next = new Button();
		next.getStyleClass().addAll(NEXT_BUTTON);

		nextPrevious.getChildren().addAll(previous, next);
		gp.add(nextPrevious, 0, 0);
		GridPane.setColumnSpan(nextPrevious, 2);
		next.setOnMouseClicked(e -> next());
		previous.setOnMouseClicked(e -> previous());
		next.disableProperty().bind(Bindings.createBooleanBinding((() -> index.get() >= data.length-1), index));
		previous.disableProperty().bind(Bindings.createBooleanBinding((() -> index.get() <= 0), index));
		gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NAME)), 0 , 1);
		subRegionName = new TextField();
		gp.add(subRegionName, 1 , 1);

		gp.add(new Label(prop.getProperty(PropertyType.SUBREGON_CAPITAL)), 0 , 2);
		subRegionCapital = new TextField();
		gp.add(subRegionCapital, 1 , 2);

		gp.add(new Label(prop.getProperty(PropertyType.SUBREGON_LEADER)), 0 , 3);
		subRegionLeader = new TextField();
		gp.add(subRegionLeader, 1 , 3);

		Button okButton = new Button(prop.getProperty(PropertyType.OK_BUTTON));
		okButton.getStyleClass().add(BUTTON_WIDTH);
		gp.add(okButton, 1, 5);
		gp.getStyleClass().add(GRIDPANE_STYLE);
		okButton.getStyleClass().add(BUTTON_WIDTH);

		current();

		okButton.setOnAction(e -> {
			dim.close();
		});

		Scene scene = new Scene(gp);
		scene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
		dim.setScene(scene);
		dim.showAndWait();
	}


	private void current(){
		setWindow(0);
	}


	private void next(){
		setWindow(1);
	}

	private void previous(){
		setWindow(-1);
	}

	private void setWindow(int x){
		data[index.get()].nameProperty().unbind();
		data[index.get()].capitalProperty().unbind();
		data[index.get()].leaderProperty().unbind();
		index.set(index.get() + x);
		table.getFocusModel().focus(index.get());
		table.getSelectionModel().select(index.get());
		subRegionName.setText(data[index.get()].getName());
		subRegionLeader.setText(data[index.get()].getLeader());
		subRegionCapital.setText(data[index.get()].getCapital());
		data[index.get()].nameProperty().bind(subRegionName.textProperty());
		data[index.get()].leaderProperty().bind(subRegionLeader.textProperty());
		data[index.get()].capitalProperty().bind(subRegionCapital.textProperty());
		addImage();
	}

	private void addImage(){
		gp.getChildren().removeAll(leaderiv, flagiv);
		try {
			String leaderPath = "file:" + FileManager.getRegionPath() + "/" + data[index.get()].getLeader() + ".png";
			leaderiv = new ImageView(leaderPath);
			leaderiv.setFitHeight(150);
			leaderiv.setFitWidth(200);
			gp.add(leaderiv, 0, 4);
		}catch (IllegalArgumentException ex){
			gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NO_PICTURE)), 0, 4);
		}

		try{
			String flagPath = "file:" + FileManager.regionPath +"/"+ data[index.get()].getName() + " Flag.png";
//			System.out.println(flagPath);
			flagiv = new ImageView(flagPath);
			flagiv.setFitHeight(150);
			flagiv.setFitWidth(200);
			gp.add(flagiv, 1, 4);
		}catch (IllegalArgumentException ex){
			gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NO_PICTURE)), 1, 4);
		}
	}
}
