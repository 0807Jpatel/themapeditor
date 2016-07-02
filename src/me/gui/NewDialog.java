package gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

import java.io.File;

/**
 * Created by jappatel on 6/29/16.
 */
public class NewDialog {
	private final String GRIDPANE_STYLE = "gridpane";
	private final String BUTTON_WIDTH = "buttonWidth";
	private final String HBOX = "hBox";
	private final String BOT_PADDING = "bottompadding";
	private final String INVALID_INPUT = "\\/:*?\"<>|";

	private String name;
	private File parentDirectory;
	private File coordinateFile;



	public void show(){
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setHeight(350);
		dim.setWidth(550);
		dim.initModality(Modality.APPLICATION_MODAL);
		dim.setTitle(prop.getProperty(PropertyType.NEW_TITLE_LABEL));

		GridPane gp = new GridPane();

		gp.add(new Label(prop.getProperty(PropertyType.NEW_SUBNAME_LABEL)), 0 , 0);
		TextField regionName = new TextField();
		regionName.addEventFilter(KeyEvent.KEY_TYPED , validation());
		gp.add(regionName, 1 , 0);

		gp.add(new Label(prop.getProperty(PropertyType.NEW_DIRECTORY_LABEL)), 0, 1);
		HBox dir = new HBox();
		Button dirButton = new Button(prop.getProperty(PropertyType.CHOOSE_FILE_BUTTON));
		Label dirLabel = new Label(prop.getProperty(PropertyType.NO_DIR_SELECTED));
		dir.getChildren().addAll(dirButton, dirLabel);
		gp.add(dir, 1, 1);

		dirButton.setOnAction(e -> {
			DirectoryChooser dc = new DirectoryChooser();
			parentDirectory = dc.showDialog(dim);
			try {
				dirLabel.setText("  " + parentDirectory.getName());
			}catch (NullPointerException ignored){}
		});

		gp.add(new Label(prop.getProperty(PropertyType.NEW_COORDINATE_LABEL)), 0, 2);
		HBox cor = new HBox();
		cor.getStyleClass().addAll(BOT_PADDING);
		Button corButton = new Button(prop.getProperty(PropertyType.CHOOSE_FILE_BUTTON));
		Label corLabel = new Label(prop.getProperty(PropertyType.NO_FILE_SELECTED));
		cor.getChildren().addAll(corButton, corLabel);
		gp.add(cor, 1, 2);

		corButton.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			coordinateFile = fc.showOpenDialog(dim);
			try {
				corLabel.setText("  " + coordinateFile.getName());
			}catch (NullPointerException ignored){}
		});

		HBox hbox = new HBox();
		Button okButton = new Button(prop.getProperty(PropertyType.OK_BUTTON));
		Button cancelButton = new Button(prop.getProperty(PropertyType.CANCEL_BUTTON));
		okButton.getStyleClass().add(BUTTON_WIDTH);
		cancelButton.getStyleClass().add(BUTTON_WIDTH);
		hbox.getChildren().addAll(cancelButton, okButton);
		hbox.getStyleClass().add(HBOX);

		okButton.setOnAction(e -> {
			name = regionName.getText();
			dim.close();
		});

		cancelButton.setOnAction(e -> {
			e.consume();
			dim.close();
		});

		gp.add(hbox, 1, 3);

		gp.getStyleClass().add(GRIDPANE_STYLE);
		dirButton.getStyleClass().add(BUTTON_WIDTH);
		corButton.getStyleClass().add(BUTTON_WIDTH);

		Scene scene = new Scene(gp);
		scene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
		dim.setScene(scene);
		dim.showAndWait();
	}

	private EventHandler<KeyEvent> validation() {
		return e -> {
			String c = e.getCharacter();
			if(INVALID_INPUT.contains(c))
				e.consume();
		};
	}

}
