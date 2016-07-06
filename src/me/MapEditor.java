import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

public class MapEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
	    try {
		    boolean success = loadProperties("workspace_properties.xml");
		    if (success){
			    Parent root = FXMLLoader.load(getClass().getResource("gui/workspace.fxml"));
			    primaryStage.setTitle("Map Editor");
			    primaryStage.setScene(new Scene(root));
		    }
	    }catch (Exception e/*IOException ioe*/) {
		    e.printStackTrace();
	    }
		primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
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
