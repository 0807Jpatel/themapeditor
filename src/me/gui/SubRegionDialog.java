package gui;

import data.SubRegion;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

	private String subRegionNameString;
	private String subRegionCapitalString;
	private String subRegionLeaderString;


	public String getSubRegionNameString() {
		return subRegionNameString;
	}

	public String getSubRegionCapitalString() {
		return subRegionCapitalString;
	}

	public String getSubRegionLeaderString() {
		return subRegionLeaderString;
	}

	public void show(SubRegion subRegion){
		subRegionNameString = subRegion.getName();
		subRegionCapitalString = subRegion.getCapital();
		subRegionLeaderString = subRegion.getLeader();
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setHeight(350);
		dim.setWidth(450);
		dim.initModality(Modality.APPLICATION_MODAL);
		dim.setTitle(prop.getProperty(PropertyType.SUBREGION_TITLE_LABEL));

		GridPane gp = new GridPane();


		HBox nextPrevious = new HBox();
		nextPrevious.getStyleClass().add(NEXTPREV_HBOX);
		Button previous = new Button();
		previous.getStyleClass().add(PREVIOUS_BUTTON);

		Button next = new Button();
		next.getStyleClass().addAll(NEXT_BUTTON);

		nextPrevious.getChildren().addAll(previous, next);
		gp.add(nextPrevious, 0, 0);
		GridPane.setColumnSpan(nextPrevious, 2);

		gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NAME)), 0 , 1);
		TextField subRegionName = new TextField();
		subRegionName.setText(subRegionNameString);
		gp.add(subRegionName, 1 , 1);

		gp.add(new Label(prop.getProperty(PropertyType.SUBREGON_CAPITAL)), 0 , 2);
		TextField subRegionCapital = new TextField();
		subRegionCapital.setText(subRegionCapitalString);
		gp.add(subRegionCapital, 1 , 2);

		gp.add(new Label(prop.getProperty(PropertyType.SUBREGON_LEADER)), 0 , 3);
		TextField subRegionLeader = new TextField();
		subRegionLeader.setText(subRegionLeaderString);
		gp.add(subRegionLeader, 1 , 3);

		try {
			String leaderPath = subRegion.getLeader() + ".png";
			ImageView leaderiv = new ImageView(leaderPath);
			leaderiv.setFitHeight(90);
			leaderiv.setFitWidth(100);
			gp.add(leaderiv, 0, 4);
		}catch (IllegalArgumentException ex){
			gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NO_PICTURE)), 0, 4);
		}

		try{
		String flagPath = subRegion.getName() + ".png";
		ImageView flagiv = new ImageView(flagPath);
		flagiv.setFitHeight(90);
		flagiv.setFitWidth(150);
		gp.add(flagiv, 1, 4);
		}catch (IllegalArgumentException ex){
			gp.add(new Label(prop.getProperty(PropertyType.SUBREGION_NO_PICTURE)), 1, 4);
		}

		HBox hbox = new HBox();
		Button okButton = new Button(prop.getProperty(PropertyType.OK_BUTTON));
		Button cancelButton = new Button(prop.getProperty(PropertyType.CANCEL_BUTTON));
		okButton.getStyleClass().add(BUTTON_WIDTH);
		cancelButton.getStyleClass().add(BUTTON_WIDTH);
		hbox.getChildren().addAll(cancelButton, okButton);
		hbox.getStyleClass().add(HBOX);

		gp.add(hbox, 1, 5);

		gp.getStyleClass().add(GRIDPANE_STYLE);
		okButton.getStyleClass().add(BUTTON_WIDTH);

		cancelButton.getStyleClass().add(BUTTON_WIDTH);

		okButton.setOnAction(e -> {
			subRegionNameString = subRegionName.getText();
			subRegionCapitalString = subRegionCapital.getText();
			subRegionLeaderString = subRegionLeader.getText();
			dim.close();
		});

		cancelButton.setOnAction(e -> {
			dim.close();
			e.consume();
		});


		Scene scene = new Scene(gp);
		scene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
		dim.setScene(scene);
		dim.showAndWait();
	}
}
