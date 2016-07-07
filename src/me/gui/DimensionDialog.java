package gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 * Created by jappatel on 6/29/16.
 */
public class DimensionDialog {
	private final String GRIDPANE_STYLE = "gridpane";
	private final String BUTTON_WIDTH = "buttonWidth";
	private final String HBOX = "hBox";
	private final String VALID_INPUT = "1234567890.";

	private double height;
	private double width;

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void show(){
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setHeight(230);
		dim.setWidth(350);
		dim.initModality(Modality.APPLICATION_MODAL);
		dim.setTitle(prop.getProperty(PropertyType.DIMENSIONS_TITLE_LABEL));

		GridPane gp = new GridPane();

		gp.add(new Label(prop.getProperty(PropertyType.DIMENSION_HEIGHT_LABEL)), 0 , 0);
		TextField HeightTF = new TextField();
		HeightTF.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation());
		gp.add(HeightTF, 1 , 0);

		gp.add(new Label(prop.getProperty(PropertyType.DIMENSION_WIDTH_LABEL)), 0 , 1);
		TextField WidthTF = new TextField();
		WidthTF.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation());
		gp.add(WidthTF, 1 , 1);

		HBox hbox = new HBox();
		Button okButton = new Button(prop.getProperty(PropertyType.OK_BUTTON));
		Button cancelButton = new Button(prop.getProperty(PropertyType.CANCEL_BUTTON));
		okButton.getStyleClass().add(BUTTON_WIDTH);
		cancelButton.getStyleClass().add(BUTTON_WIDTH);
		hbox.getChildren().addAll(cancelButton, okButton);
		hbox.getStyleClass().add(HBOX);

		gp.add(hbox, 1, 2);

		gp.getStyleClass().add(GRIDPANE_STYLE);
		okButton.getStyleClass().add(BUTTON_WIDTH);
		cancelButton.getStyleClass().add(BUTTON_WIDTH);

		okButton.setOnAction(e -> {
			height = Double.parseDouble(HeightTF.getText());
			width = Double.parseDouble(WidthTF.getText());
			dim.close();
		});

		cancelButton.setOnAction(e -> {
			e.consume();
			dim.close();
		});


		Scene scene = new Scene(gp);
		scene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
		dim.setScene(scene);
		dim.showAndWait();
	}


	private EventHandler<KeyEvent> numeric_Validation() {
		return e -> {
			String c = e.getCharacter();
			if(!VALID_INPUT.contains(c))
				e.consume();
		};
	}
}
