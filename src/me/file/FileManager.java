package file;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import gui.NewDialog;
import gui.ProgressDialog;
import gui.YesNoDialog;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

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
	private final String TRANSLATEX = "TRANSLATEX";
	private final String TRANSLATEY = "TRANSLATEY";
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
	private final String EXPORTMAPNAME = "name";
	private final String EXPORTCAPITAL = "subregions_have_capitals";
	private final String EXPORTFLAG  = "subregions_have_flags";
	private final String EXPORTLEADERS = "subregions_have_leaders";

	private DataManager dataManager;
	private Controller controller;
	public static String regionPath; //After creating new Directory
	private String coordinatePath;
	private static double progress;
	private SimpleBooleanProperty saved;
	private boolean allLeaders, allCapital;
	// TODO add the save boolean and alert dialog for robust design


	public FileManager(DataManager dataManger, Controller controller) {
		this.dataManager = dataManger;
		this.controller = controller;
		saved = new SimpleBooleanProperty(true);
	}

	/**
	 * This method is called by the controller as handler to New Button
	 */
	public void processNewRequest() {
		try {
			if (!saved.getValue()) {
				boolean save = YesNoDialog.getSingleton().show("Would you like to save this work");
				if (save)
					processSaveRequest();
			}
			controller.unbind();
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
					Alert alert = new Alert(Alert.AlertType.ERROR, "Error Creating Folder");
					alert.show();
				}
			}
			try {
				loadSubRegionCoordinates(coordinatePath);
			} catch (IOException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error Loading Coordinate File");
				alert.show();
			}
			dataManager.setDirectoryPath(directoryPath);
			dataManager.setMapName(name);
			assignRandColor();
			controller.setBackgroundColorPicker();
			controller.reload();
			controller.disableButtons(false);
			saved.set(false);
		}catch(Exception ex){
			Alert alert = new Alert(Alert.AlertType.ERROR,"Some thing went wrong Please try again");
			alert.show();
		}
	}

	/**
	 * This method is called by the controller as handler to Load Button
	 */
	public void processLoadRequest() {
	    try {
		    if(!saved.getValue()){
			    boolean save = YesNoDialog.getSingleton().show("Would you like to save this work");
			    if(save)
				    processSaveRequest();
		    }
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.setInitialDirectory(new File("src/me/work"));
		    File file = fileChooser.showOpenDialog(null);
		    if (file != null) {
			    ProgressDialog pd = new ProgressDialog();
			    pd.show();
			    progress = 1;
			    Thread th = new Thread(() -> {
				    try {
				    	controller.unbind();
					    loadData(file.getPath());
					    Platform.runLater(() -> controller.reload());
				    } catch (IOException ex) {
					    ex.printStackTrace();
				    }
			    });
			    th.start();
		    }
	    }
	    catch (Exception ex){
		    Alert alert = new Alert(Alert.AlertType.ERROR,"Error Loading File");
		    alert.show();
	    }
	}

	public void processSaveRequest() throws IOException{
	    try{

		if(!controller.getMapNameText().equals(dataManager.getMapName())){
			String oldName = dataManager.getMapName();
			dataManager.setMapName(controller.getMapNameText());
			File oldPath = new File(regionPath);
			if(oldPath.exists()) {
				File newPath = new File(dataManager.getDirectoryPath()+ "/" + dataManager.getMapName());
				oldPath.renameTo(newPath);
				regionPath = newPath.toString();
			}
			File saveWork = new File("src/me/work/" + oldName);
			if(saveWork.exists())
				saveWork.delete();
			File oldRVM = new File(regionPath + "/" + oldName +".rvm");
			if(oldRVM.exists()) {
				File newRVM = new File(regionPath + "/" + dataManager.getMapName() + ".rvm");
				oldRVM.renameTo(newRVM);
			}
			File oldPNG = new File(regionPath + "/" + oldName +".png");
			if(oldPNG.exists()) {
				File newPNG = new File(regionPath + "/" + dataManager.getMapName() + ".png");
				oldPNG.renameTo(newPNG);
			}

		}

		File filePath = new File("src/me/work/" + dataManager.getMapName());

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		ObservableList<SubRegion> subRegions = dataManager.getSubRegions();
		for(SubRegion temp: subRegions){
			JsonObject jsonObject = Json.createObjectBuilder()
					.add(SUBREGIONNAME, temp.getName())
					.add(SUBREGIONCAPITAL, temp.getCapital())
					.add(SUBREGIONLEADER, temp.getLeader())
					.add(SUBREGIONCOLOR, temp.getColor().getBlue()).build();
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
					.add(TRANSLATEX, dataManager.getTranslatex())
					.add(TRANSLATEY, dataManager.getTranslatey())
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
		saved.set(true);
	    }catch(NullPointerException ex){
			Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong Saving");
		    alert.show();
	    }

	}

	public boolean processExportRequest() throws IOException{
	    try{
		if(dataManager.getAllName()){
			processSaveRequest();
			JsonArrayBuilder arrayBuilder = exportArray();
			boolean allFlag = dataManager.getAllFlag();
			JsonObject exportFile =  Json.createObjectBuilder()
					.add(EXPORTMAPNAME, dataManager.getMapName())
					.add(EXPORTCAPITAL, allCapital)
					.add(EXPORTFLAG, allFlag)
					.add(EXPORTLEADERS, allLeaders)
					.add("subRegions", arrayBuilder).build();

			Map<String, Object> properties = new HashMap<>(1);
			properties.put(JsonGenerator.PRETTY_PRINTING, true);
			JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
			StringWriter sw = new StringWriter();
			JsonWriter jsonWriter = writerFactory.createWriter(sw);
			jsonWriter.writeObject(exportFile);
			jsonWriter.close();
			OutputStream os = new FileOutputStream(regionPath + "/" + dataManager.getMapName()+".rvm");
			JsonWriter jsonFileWriter = Json.createWriter(os);
			jsonFileWriter.writeObject(exportFile);
			String prettyPrinted = sw.toString();
			PrintWriter pw = new PrintWriter(regionPath + "/" + dataManager.getMapName()+".rvm");
			pw.write(prettyPrinted);
			pw.close();
			saved.set(true);
			return true;

		}else{
			Alert alert = new Alert(Alert.AlertType.ERROR, "All Names are Required for Exporting");
			alert.show();
			return false;
		}
	    }catch(Exception ex){
			Alert alert = new Alert(Alert.AlertType.ERROR, "Something went Wrong Exporting");
	    }
	    return false;
	}

	public void processExitButton(){
		try {
			if (!saved.getValue()) {
				boolean save = YesNoDialog.getSingleton().show("Would you like to save this work");
				if (save)
					processSaveRequest();
			}
		}catch (Exception ex){
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Something Went Wrong Saving");
			alert.show();
		}
		System.exit(1);
	}


	/**
	 * this method is used to by loadrequest to load all the data from the last saved status
	 *
	 * @param filePath
	 * @throws IOException
	 */
	public void loadData(String filePath) throws IOException {
		JsonObject jsonObject = loadJSONFile(filePath);
		progress++;
		dataManager.setMapName(jsonObject.getString(NAMEOFMAP));
		progress++;
		dataManager.setBackgroundColor(jsonObject.getString(BACKGROUNDCOLOR));
		progress++;
		dataManager.setBorderColor(jsonObject.getString(BORDERCOLOR));
		progress++;
		dataManager.setBorderWidth(getDataAsDouble(jsonObject, BORDERWIDTH));
		progress++;
		dataManager.setTranslatex(getDataAsDouble(jsonObject, TRANSLATEX));
		progress++;
		dataManager.setTranslatey(getDataAsDouble(jsonObject, TRANSLATEY));
		progress++;
		dataManager.setZoomLevel(getDataAsDouble(jsonObject, ZOOMLEVEL));
		progress++;
		dataManager.setDirectoryPath(jsonObject.getString(PARENTDIRECTORY));
		progress++;
		regionPath = dataManager.getDirectoryPath() + "/" + dataManager.getMapName();
		progress++;
		JsonArray imageArray = jsonObject.getJsonArray(IMAGES);
		progress++;
		loadImages(imageArray);
		progress++;
		coordinatePath = jsonObject.getString(COORDINATESFILEPATH);
		progress++;
		loadSubRegionCoordinates(coordinatePath);
		progress++;
		JsonArray contentArray = jsonObject.getJsonArray(SUBREGIONCONTENT);
		progress++;
		loadSubRegionContents(contentArray);
		progress++;
		saved.set(true);
		controller.disableButtons(false);
	}

	private void loadSubRegionContents(JsonArray contentArray) {
		ObservableList<SubRegion> arra = dataManager.getSubRegions();
		for(int x = 0; x < arra.size(); x++){
			JsonObject jobject = contentArray.getJsonObject(x);
			SubRegion temp = arra.get(x);
			temp.setName(jobject.getString(SUBREGIONNAME));
			temp.setCapital(jobject.getString(SUBREGIONCAPITAL));
			temp.setLeader(jobject.getString(SUBREGIONLEADER));
			temp.setColor(javafx.scene.paint.Color.gray(getDataAsDouble(jobject, SUBREGIONCOLOR)));
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

		ObservableList<SubRegion> subRegions = FXCollections.observableArrayList();
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
			subRegions.add(temp);
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
			ImageDetail id = new ImageDetail(path, imx, imy, x);
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

	private JsonArrayBuilder exportArray(){
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		ObservableList<SubRegion> subRegions = dataManager.getSubRegions();
		allLeaders  = dataManager.getAllLeaders();
		allCapital = dataManager.getAllCapital();
		if(allLeaders && allCapital) {
			for (SubRegion temp : subRegions) {
				JsonObject jsonObject = Json.createObjectBuilder()
						.add("name", temp.getName())
						.add("capital", temp.getCapital())
						.add("leader", temp.getLeader())
						.add("red", (int)(temp.getColor().getRed() * 255))
						.add("blue", (int)(temp.getColor().getBlue() * 255))
						.add("green",(int)(temp.getColor().getGreen() * 255))   .build();
				arrayBuilder.add(jsonObject);
			}
		}
		else if(allLeaders ||allCapital){
			if(allCapital){
				for (SubRegion temp : subRegions) {
					JsonObject jsonObject = Json.createObjectBuilder()
							.add("name", temp.getName())
							.add("capital", temp.getCapital())
							.add("red", (int)(temp.getColor().getRed() * 255))
							.add("blue",(int) (temp.getColor().getBlue() * 255))
							.add("green", (int) (temp.getColor().getGreen() * 255)).build();
					arrayBuilder.add(jsonObject);
				}
			}
			if(allLeaders){
				for (SubRegion temp : subRegions) {
					JsonObject jsonObject = Json.createObjectBuilder()
							.add("name", temp.getName())
							.add("leader", temp.getLeader())
							.add("red", (int)(temp.getColor().getRed() * 255))
							.add("blue", (int)(temp.getColor().getBlue() * 255))
							.add("green", (int)(temp.getColor().getGreen() * 255)).build();
					arrayBuilder.add(jsonObject);
				}
			}
		}
		else{
			for (SubRegion temp : subRegions) {
				JsonObject jsonObject = Json.createObjectBuilder()
						.add("name", temp.getName())
						.add("RED", (int)(temp.getColor().getRed() * 255))
						.add("BLUE", (int)(temp.getColor().getBlue() * 255))
						.add("GREEN", (int)(temp.getColor().getGreen() * 255)).build();
				arrayBuilder.add(jsonObject);
			}
		}
		return arrayBuilder;
	}

	private void assignRandColor(){
		ObservableList<SubRegion> s = dataManager.getSubRegions();
		double number = .99 / s.size();
		double jump = number;
		for(SubRegion temp: s){
			temp.setColor(Color.gray(jump));
			jump += number;
		}
	}

	public void setCoordinatePath(String coordinatePath){
		this.coordinatePath = coordinatePath;
	}
	
	public static double getDouble(){
	    return progress;
	}

	public static String getRegionPath(){return regionPath;}

	public SimpleBooleanProperty savedProperty() {
		return saved;
	}

	public void setEdited() {saved.set(false);
	}

}
