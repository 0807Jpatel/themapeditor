package file;

import data.DataManager;
import data.ImageDetail;
import javafx.stage.FileChooser;

import javax.json.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class FileManager {
	//String used for loading TAGS from JSON File
	private final String NAMEOFMAP = "NAME_OF_MAP";
	private final String BACKGROUNDCOLOR = "BACKGROUND_COLOR";
	private final String BORDERCOLOR = "BORDER_COLOR";
	private final String BORDERWIDTH = "BORDER_WIDTH";
	private final String ZOOMLEVEL = "ZOOM";
	private final String IMAGES = "IMAGES";
	private final String IMAGEPATH = "IMAGE_PATH";
	private final String IMAGEX = "IMAGE_X";
	private final String IMAGEY = "IMAGE_Y";
	private final String NUMBEROFSUBREGION = "NUMBER_OF_SUBREGION";
	private final String SUBREGION = "SUBREGION";
	private final String NAMEOFSUBREGION = "NAME_OF_SUBREGION";
	private final String SUBREGIONCAPITAL = "CAPITAL_OF_SUBREGION";
	private final String SUBREGIONLEADER = "LEADER_OF_SUBREGION";
	private final String SUBREGIONCOLOR = "COLOR_OF_SUBREGION";
	private final String NUMBEROFSUBREGIONPOLYGON = "NUMBER_OF_SUBREGION_POLYGON";
	private final String SUBREGIONPOLYGON = "SUBREGION_POLYGON";

	private DataManager dataManager;

	// TODO add the save boolean and alert dialog for robust design

	public FileManager(DataManager dataManger){
		this.dataManager = dataManger;
	}

	public void processNewRequest(){
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		try {
			loadData(file.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processLoadRequest(){

	}


	/**
	 * this method is used to by loadrequest to load all the data from the last saved status
	 * @param filePath
	 * @throws IOException
	 */
	private void loadData(String filePath) throws IOException{
		JsonObject jsonObject = loadJSONFile(filePath);
		dataManager.setMapName(jsonObject.getString(NAMEOFMAP));
		dataManager.setBackgroundColor(jsonObject.getString(BACKGROUNDCOLOR));
		dataManager.setBorderColor(jsonObject.getString(BORDERCOLOR));
		dataManager.setBorderWidth(getDataAsDouble(jsonObject, BORDERWIDTH));
		dataManager.setZoomLevel(getDataAsDouble(jsonObject, ZOOMLEVEL));
		JsonArray imageArray = jsonObject.getJsonArray(IMAGES);
		loadImages(imageArray);
		System.out.println(dataManager.getZoomLevel());
	}



	/**
	 * This method is used to load the ArrayList of Images that user added to the Map
	 * @param jsonArray Array Object that is used to load Image
	 */
	private void loadImages(JsonArray jsonArray){
		ArrayList<ImageDetail> imageD = new ArrayList<>();
		for(int x = 0; x < jsonArray.size(); x++){
			JsonObject jo = jsonArray.getJsonObject(x);
			String path = jo.getString(IMAGEPATH);
			double imx = getDataAsDouble(jo, IMAGEX);
			double imy = getDataAsDouble(jo, IMAGEY);
			ImageDetail id = new ImageDetail(path, imx, imy);
			imageD.add(id);
		}
	}


	/**
	 * This method is also taken from Prof. framework
	 * @param json Object from witch the value has to be loaded
	 * @param dataName tag of the double value
	 * @return double value loaded from the json object
	 */
	private double getDataAsDouble(JsonObject json, String dataName) {
		JsonValue value = json.get(dataName);
		JsonNumber number = (JsonNumber)value;
		return number.bigDecimalValue().doubleValue();
	}

	/**
	 * This method is copied form Prof. framework
	 * @param jsonFilePath path of the Json file
	 * @return returns json object
	 * @throws IOException thrown by FileInputStream
	 */
	private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
		InputStream is = new FileInputStream(jsonFilePath);
		JsonReader jsonReader = Json.createReader(is);
		JsonObject json = jsonReader.readObject();
		jsonReader.close();
		is.close();
		return json;
	}





}
