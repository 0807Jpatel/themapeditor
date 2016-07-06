package file;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import gui.NewDialog;
import javafx.stage.FileChooser;

import javax.json.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private final String NUMBEROFSUBREGION = "NUMBER_OF_SUBREGIONS";
	private final String SUBREGION = "SUBREGIONS";
	private final String NAMEOFSUBREGION = "NAME_OF_SUBREGION";
	private final String SUBREGIONCAPITAL = "CAPITAL_OF_SUBREGION";
	private final String SUBREGIONLEADER = "LEADER_OF_SUBREGION";
	private final String SUBREGIONCOLOR = "COLOR_OF_SUBREGION";
	private final String NUMBEROFSUBREGIONPOLYGON = "NUMBER_OF_SUBREGION_POLYGONS";
	private final String SUBREGIONPOLYGON = "SUBREGION_POLYGONS";

	private DataManager dataManager;
	private Controller controller;
	private String regionPath; //After creating new Directory

	// TODO add the save boolean and alert dialog for robust design



	public FileManager(DataManager dataManger, Controller controller){
		this.dataManager = dataManger;
		this.controller = controller;
	}

	/**
	 * This method is called by the controller as handler to New Button
	 */
	public void processNewRequest(){
		NewDialog nd = new NewDialog();
//		nd.show();
//		String directoryPath = nd.getParentDirectory().toPath().toString();
//		String coordinatePath = nd.getCoordinateFile().toPath().toString();
		FileChooser fc = new FileChooser();
		File coordinatePath = fc.showOpenDialog(null);
//		String name = nd.getName();
//		regionPath = directoryPath+"/"+name;
//		Path newDirectoryPath = Paths.get(regionPath);
//		if (!Files.exists(newDirectoryPath)) {
			try {
//				Files.createDirectory(newDirectoryPath);
				dataManager.setMapName("test");
				loadSubRegionCoordinates(coordinatePath.getPath().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}
		controller.reload();
	}

	/**
	 * This method is called by the controller as handler to Load Button
	 */
	public void processLoadRequest(){
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		try {
			loadData(file.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSubRegionCoordinates(String path) throws IOException{
		JsonObject jsonObject = loadJSONFile(path);
		int numberOfSubRegions = getDataAsInt(jsonObject, NUMBEROFSUBREGION);
		JsonArray firstSubRegions = jsonObject.getJsonArray(SUBREGION);

		SubRegion[] subRegions = new SubRegion[numberOfSubRegions];
		for(int x = 0; x < firstSubRegions.size(); x++){
			JsonObject sr = firstSubRegions.getJsonObject(x);
			SubRegion temp = new SubRegion();
			int numberOfSubRegionPolygon = getDataAsInt(sr, NUMBEROFSUBREGIONPOLYGON);
			JsonArray subRegionPolygonsArray = sr.getJsonArray(SUBREGIONPOLYGON);
			double[][] polygonsPoints = new double[numberOfSubRegionPolygon][];
			for(int q = 0; q < numberOfSubRegionPolygon; q++){
				JsonArray xy = subRegionPolygonsArray.getJsonArray(q);
				double[] points = new double[xy.size() * 2];
				for(int w = 0; w < xy.size(); w++){
					JsonObject xyObject = xy.getJsonObject(w);
					points[w*2] = (getDataAsDouble(xyObject, "X")) ;
					points[w*2+1] =(getDataAsDouble(xyObject, "Y"));
				}
				polygonsPoints[q] = points;
			}
			temp.setSubPoints(polygonsPoints);
			subRegions[x] = temp;
		}
		dataManager.setSubRegions(subRegions);
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
		dataManager.setImages(imageD);
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

	public int getDataAsInt(JsonObject json, String dataName) {
		JsonValue value = json.get(dataName);
		JsonNumber number = (JsonNumber)value;
		return number.bigIntegerValue().intValue();
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
