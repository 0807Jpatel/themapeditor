package data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class DataManager {
	private SubRegion[] subRegions;
	private SimpleStringProperty mapName;
	private SimpleStringProperty backgroundColor;
	private SimpleStringProperty borderColor;
	private SimpleDoubleProperty zoomLevel;
	private SimpleDoubleProperty borderWidth;
	private ArrayList<ImageDetail> images;
	private SimpleDoubleProperty height;
	private SimpleDoubleProperty width;

	public DataManager(){
		images = new ArrayList<>(3);
		mapName = new SimpleStringProperty("");
		backgroundColor = new SimpleStringProperty("#ffffff");
		borderColor = new SimpleStringProperty("#000000");
		zoomLevel = new SimpleDoubleProperty(0);
		borderWidth = new SimpleDoubleProperty(1);
		height = new SimpleDoubleProperty(536);
		width = new SimpleDoubleProperty(802);
	}

	public SubRegion[] getSubRegions() {
		return subRegions;
	}

	public void setSubRegions(SubRegion[] subRegions) {
		this.subRegions = subRegions;
	}

	public String getMapName() {
		return mapName.get();
	}

	public SimpleStringProperty mapNameProperty() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName.set(mapName);
	}

	public String getBackgroundColor() {
		return backgroundColor.get();
	}

	public SimpleStringProperty backgroundColorProperty() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor.set(backgroundColor);
	}

	public String getBorderColor() {
		return borderColor.get();
	}

	public SimpleStringProperty borderColorProperty() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor.set(borderColor);
	}

	public double getZoomLevel() {
		return zoomLevel.get();
	}

	public SimpleDoubleProperty zoomLevelProperty() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel.set(zoomLevel);
	}

	public double getBorderWidth() {
		return borderWidth.get();
	}

	public SimpleDoubleProperty borderWidthProperty() {
		return borderWidth;
	}

	public void setBorderWidth(double borderWidth) {
		this.borderWidth.set(borderWidth);
	}

	public ArrayList<ImageDetail> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageDetail> images) {
		this.images = images;
	}

	public double getHeight() {
		return height.get();
	}

	public SimpleDoubleProperty heightProperty() {
		return height;
	}

	public void setHeight(double height) {
		this.height.set(height);
	}

	public double getWidth() {
		return width.get();
	}

	public SimpleDoubleProperty widthProperty() {
		return width;
	}

	public void setWidth(double width) {
		this.width.set(width);
	}
}
