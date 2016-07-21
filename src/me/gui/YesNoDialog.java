package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
* Created by jappatel on 7/21/16.
*/
public class YesNoDialog {

	static YesNoDialog singleton;
	private final String GRIDPANE_STYLE = "gridpane";
	private final String BUTTON_WIDTH = "buttonWidth";
	private final String HBOX = "hBox";
	private GridPane gridPane;
	private Scene messageScene;
	private Label messageLabel;
	private Button yesButton;
	private Button noButton;
	boolean answer;

	private YesNoDialog() {}

	public static YesNoDialog getSingleton() {
		if (singleton == null)
			singleton = new YesNoDialog();
		return singleton;
	}

	public boolean show(String Message) {
		Stage stage = new Stage();
		stage.setHeight(150);
		stage.setWidth(400);
		stage.initModality(Modality.WINDOW_MODAL);
		messageLabel = new Label(Message);
		yesButton = new Button("YES");
		noButton = new Button("NO");
		yesButton.setOnMouseClicked(e -> {
			answer = true;
			stage.close();
		});
		noButton.setOnAction(e -> {
			answer = false;
			stage.close();
		});
		gridPane = new GridPane();
		gridPane.add(messageLabel, 0, 0);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(noButton, yesButton);
		gridPane.add(hBox, 0, 1);
		messageScene = new Scene(gridPane);
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		gridPane.getStyleClass().add(GRIDPANE_STYLE);
		yesButton.getStyleClass().add(BUTTON_WIDTH);
		noButton.getStyleClass().add(BUTTON_WIDTH);
		hBox.getStyleClass().addAll(HBOX);
		messageScene.getStylesheets().add(getClass().getClassLoader().getResource(prop.getProperty(PropertyType.DIALOG_CSS)).toString());
		stage.setScene(messageScene);
		stage.showAndWait();
		return answer;
	}
}
