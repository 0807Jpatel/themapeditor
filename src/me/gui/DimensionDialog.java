package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 * Created by jappatel on 6/29/16.
 */
public class DimensionDialog {
	private final String CSS_PATH = "css/dialog.css";

	double height;
	double widgth;
	public void show(){
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setTitle(prop.getProperty(PropertyType.DIMENSIONS_TITLE_LABEL));

		GridPane gp = new GridPane();

		gp.add(new Label(prop.getProperty(PropertyType.DIMENSION_HEIGHT_LABEL)), 0 , 0);
		TextField HeightTF = new TextField();
		gp.add(HeightTF, 1 , 0);

		gp.add(new Label(prop.getProperty(PropertyType.DIMENSION_WIDTH_LABEL)), 0 , 1);
		TextField WidthTF = new TextField();
		gp.add(WidthTF, 1 , 1);

		Button ok = new Button(prop.getProperty(PropertyType.OK_BUTTON));
		Button cancel = new Button(prop.getProperty(PropertyType.CANCEL_BUTTON));

		gp.add(cancel, 2 , 2);
		gp.add(ok, 0, 2);


		ok.setOnAction(e -> {
			height = Double.parseDouble(HeightTF.getText());
			widgth = Double.parseDouble(WidthTF.getText());
			dim.close();
		});

		cancel.setOnAction(e -> {
			e.consume();
			dim.close();
		});


		Scene scene = new Scene(gp);
		scene.getStylesheets().add(getClass().getClassLoader().getResource(CSS_PATH).toString());
		dim.setScene(scene);
		dim.showAndWait();
	}
}
