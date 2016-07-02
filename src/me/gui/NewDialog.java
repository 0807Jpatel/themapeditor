package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 * Created by jappatel on 6/29/16.
 */
public class NewDialog {

	public void show(){
		PropertiesManager prop = PropertiesManager.getPropertiesManager();
		Stage dim = new Stage();
		dim.setHeight(250);
		dim.setWidth(350);
		dim.initModality(Modality.APPLICATION_MODAL);
		dim.setTitle(prop.getProperty(PropertyType.DIMENSIONS_TITLE_LABEL));

		GridPane gp = new GridPane();

		gp.add(new Label(prop.getProperty(PropertyType.DIMENSION_HEIGHT_LABEL)), 0 , 0);
		TextField regionName = new TextField();
		regionName.addEventFilter(KeyEvent.KEY_TYPED , validation());
		gp.add(regionName, 1 , 0);


	}

	private EventHandler<KeyEvent> validation() {
		return e -> {
			String c = e.getCharacter();
			if("\\/:*?\"<>|".contains(c))
				e.consume();
		};
	}

}
