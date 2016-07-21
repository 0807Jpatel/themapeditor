package Driver;

import gui.DimensionDialog;
import javafx.application.Application;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * Created by jappatel on 7/2/16.
 */
public class DimensionDriver extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			boolean success = loadProperties("workspace_properties.xml");
			if (success){
				DimensionDialog dd = new DimensionDialog();
//				dd.show();
			}
		}catch (Exception e/*IOException ioe*/) {
			System.out.println("Error2");
		}
	}

	private boolean loadProperties(String propertiesFileName) {
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		try {
			// LOAD THE SETTINGS FOR STARTING THE APP
			props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, "./data/");
			props.loadProperties(propertiesFileName, "properties_schema.xsd");
			return true;
		} catch (InvalidXMLFileFormatException ixmlffe) {
			// SOMETHING WENT WRONG INITIALIZING THE XML FILE
			System.out.println("Error");
			return false;
		}
	}
}
