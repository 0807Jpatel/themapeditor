package Driver;

import data.SubRegion;
import gui.NewDialog;
import gui.SubRegionDialog;
import javafx.application.Application;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * Created by jappatel on 7/2/16.
 */
public class SubRegionDriver extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			boolean success = loadProperties("workspace_properties.xml");
			if (success){

				SubRegion sr = new SubRegion("Name", "Leader", "Capital");
//				SubRegionDialog sd = new SubRegionDialog();
//				sd.show();
			}
		}catch (Exception e/*IOException ioe*/) {
			e.printStackTrace();
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
