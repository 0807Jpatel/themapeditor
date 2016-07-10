package file;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import gui.NewDialog;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private final String PARENTDIRECTORY = "PARENT_DIRECTORY";
	private final String COORDINATESFILEPATH = "COORDINATE_FILE_PATH";
	private final String SUBREGIONCONTENT = "SUBREGION_CONTENT";
	private final String SUBREGIONNAME = "NAME_OF_SUBREGION";
	private final String SUBREGIONCAPITAL = "CAPITAL_OF_SUBREGION";
	private final String SUBREGIONLEADER = "LEADER_OF_SUBREGION";
	private final String SUBREGIONCOLOR = "COLOR_OF_SUBREGION";
	private final String NUMBEROFSUBREGIONPOLYGON = "NUMBER_OF_SUBREGION_POLYGONS";
	private final String SUBREGIONPOLYGON = "SUBREGION_POLYGONS";


	private boolean edited;
	private DataManager dataManager;
	private Controller controller;
	private String regionPath; //After creating new Directory
	private String coordinatePath;

	// TODO add the save boolean and alert dialog for robust design


	public FileManager(DataManager dataManger, Controller controller) {
		this.dataManager = dataManger;
		this.controller = controller;
	}

	/**
	 * This method is called by the controller as handler to New Button
	 */
	public void processNewRequest() {
		NewDialog nd = new NewDialog();
		nd.show();
		String directoryPath = nd.getParentDirectory().toPath().toString();
		coordinatePath = nd.getCoordinateFile().toPath().toString();
		String name = nd.getName();
		regionPath = directoryPath + "/" + name;
		Path newDirectoryPath = Paths.get(regionPath);
		if (!Files.exists(newDirectoryPath)) {
			try {
				Files.createDirectory(newDirectoryPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			loadSubRegionCoordinates(coordinatePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataManager.setDirectoryPath(regionPath);
		dataManager.setMapName(name);
		controller.setBackgroundColorPicker();
		controller.reload();
	}

	/**
	 * This method is called by the controller as handler to Load Button
	 */
	public void processLoadRequest() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		try {
			loadData(file.getPath());
			System.out.println(dataManager.getBorderWidth());
			System.out.println(dataManager.getBackgroundColor());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSaveRequest() throws IOException{

		FileChooser fc = new FileChooser();
		File filePath = fc.showOpenDialog(null);

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		SubRegion[] subRegions = dataManager.getSubRegions();
		for(SubRegion temp: subRegions){
			JsonObject jsonObject = Json.createObjectBuilder()
					.add(SUBREGIONNAME, temp.getName())
					.add(SUBREGIONCAPITAL, temp.getCapital())
					.add(SUBREGIONLEADER, temp.getLeader())
					.add(SUBREGIONCOLOR, temp.getColor()).build();
			arrayBuilder.add(jsonObject);
		}
		JsonArray subRegionArray = arrayBuilder.build();

		JsonArrayBuilder imageBuilder = Json.createArrayBuilder();
		ArrayList<ImageDetail> imageDetails = dataManager.getImages();
		for(ImageDetail temp: imageDetails){
			JsonObject jsonObject = Json.createObjectBuilder()
					.add(IMAGEPATH, temp.getImage())
					.add(IMAGEX, temp.getX())
					.add(IMAGEY, temp.getY()).build();
			imageBuilder.add(jsonObject);
		}
		JsonArray imageArray = imageBuilder.build();
		JsonObject dataManagerJSO =  Json.createObjectBuilder()
					.add(NAMEOFMAP, dataManager.getMapName())
					.add(BACKGROUNDCOLOR, dataManager.getBackgroundColor())
					.add(BORDERCOLOR, dataManager.getBorderColor())
					.add(BORDERWIDTH, dataManager.getBorderWidth())
					.add(ZOOMLEVEL, dataManager.getZoomLevel())
					.add(PARENTDIRECTORY, dataManager.getDirectoryPath())
					.add(IMAGES, imageArray)
					.add(COORDINATESFILEPATH, coordinatePath)
					.add(SUBREGIONCONTENT, subRegionArray).build();

		Map<String, Object> properties = new HashMap<>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
		StringWriter sw = new StringWriter();
		JsonWriter jsonWriter = writerFactory.createWriter(sw);
		jsonWriter.writeObject(dataManagerJSO);
		jsonWriter.close();

		OutputStream os = new FileOutputStream(filePath.toString());
		JsonWriter jsonFileWriter = Json.createWriter(os);
		jsonFileWriter.writeObject(dataManagerJSO);
		String prettyPrinted = sw.toString();
		PrintWriter pw = new PrintWriter(filePath.toString());
		pw.write(prettyPrinted);
		pw.close();

	}

	/**
	 * this method is used to by loadrequest to load all the data from the last saved status
	 *
	 * @param filePath
	 * @throws IOException
	 */
	private void loadData(String filePath) throws IOException {
		JsonObject jsonObject = loadJSONFile(filePath);
		dataManager.setMapName(jsonObject.getString(NAMEOFMAP));
		dataManager.setBackgroundColor(jsonObject.getString(BACKGROUNDCOLOR));
		dataManager.setBorderColor(jsonObject.getString(BORDERCOLOR));
		dataManager.setBorderWidth(getDataAsDouble(jsonObject, BORDERWIDTH));
		dataManager.setZoomLevel(getDataAsDouble(jsonObject, ZOOMLEVEL));
		dataManager.setDirectoryPath(jsonObject.getString(PARENTDIRECTORY));
		JsonArray imageArray = jsonObject.getJsonArray(IMAGES);
		loadImages(imageArray);
		coordinatePath = jsonObject.getString(COORDINATESFILEPATH);
		loadSubRegionCoordinates(coordinatePath);
		JsonArray contentArray = jsonObject.getJsonArray(SUBREGIONCONTENT);
		loadSubRegionContents(contentArray);
		controller.reload();
	}

	private void loadSubRegionContents(JsonArray contentArray) {
		SubRegion[] arra = dataManager.getSubRegions();
		for(int x = 0; x < arra.length; x++){
			JsonObject jobject = contentArray.getJsonObject(x);
			SubRegion temp = arra[x];
			temp.setName(jobject.getString(SUBREGIONNAME));
			temp.setCapital(jobject.getString(SUBREGIONCAPITAL));
			temp.setLeader(jobject.getString(SUBREGIONLEADER));
			temp.setColor(getDataAsDouble(jobject, SUBREGIONCOLOR));
		}

	}

	/**
	 * Loading the x and y coordinates for all the subregions
	 *
	 * @param path path of the file used for loading
	 * @throws IOException
	 */
	private void loadSubRegionCoordinates(String path) throws IOException {
		JsonObject jsonObject = loadJSONFile(path);
		int numberOfSubRegions = getDataAsInt(jsonObject, NUMBEROFSUBREGION);
		JsonArray firstSubRegions = jsonObject.getJsonArray(SUBREGION);

		SubRegion[] subRegions = new SubRegion[numberOfSubRegions];
		for (int x = 0; x < firstSubRegions.size(); x++) {
			JsonObject sr = firstSubRegions.getJsonObject(x);
			SubRegion temp = new SubRegion();
			int numberOfSubRegionPolygon = getDataAsInt(sr, NUMBEROFSUBREGIONPOLYGON);
			JsonArray subRegionPolygonsArray = sr.getJsonArray(SUBREGIONPOLYGON);
			double[][] polygonsPoints = new double[numberOfSubRegionPolygon][];
			for (int q = 0; q < numberOfSubRegionPolygon; q++) {
				JsonArray xy = subRegionPolygonsArray.getJsonArray(q);
				double[] points = new double[xy.size() * 2];
				for (int w = 0; w < xy.size(); w++) {
					JsonObject xyObject = xy.getJsonObject(w);
					points[w * 2] = ((getDataAsDouble(xyObject, "X") + 180) / 360) * dataManager.getWidth();
					points[w * 2 + 1] = ((getDataAsDouble(xyObject, "Y") * -1 + 90) / 180) * dataManager.getHeight();
				}
				polygonsPoints[q] = points;
			}
			temp.setSubPoints(polygonsPoints);
			subRegions[x] = temp;
		}
		dataManager.setSubRegions(subRegions);
	}

	/**
	 * This method is used to load the ArrayList of Images that user added to the Map
	 *
	 * @param jsonArray Array Object that is used to load Image
	 */
	private void loadImages(JsonArray jsonArray) {
		ArrayList<ImageDetail> imageD = new ArrayList<>();
		for (int x = 0; x < jsonArray.size(); x++) {
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
	 *
	 * @param json     Object from witch the value has to be loaded
	 * @param dataName tag of the double value
	 * @return double value loaded from the json object
	 */
	private double getDataAsDouble(JsonObject json, String dataName) {
		JsonValue value = json.get(dataName);
		JsonNumber number = (JsonNumber) value;
		return number.bigDecimalValue().doubleValue();
	}

	private int getDataAsInt(JsonObject json, String dataName) {
		JsonValue value = json.get(dataName);
		JsonNumber number = (JsonNumber) value;
		return number.bigIntegerValue().intValue();
	}

	/**
	 * This method is copied form Prof. framework
	 *
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

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

}
