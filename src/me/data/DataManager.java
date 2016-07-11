package data;

import file.FileManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class DataManager {
	private SubRegion[] subRegions;
	private SimpleStringProperty mapName;
	private String backgroundColor;
	private String borderColor;
	private double zoomLevel;
	private double borderWidth;
	private ArrayList<ImageDetail> images;
	private double height;
	private double width;
	private String directoryPath;
	static Color[] subRegionColor;

	public DataManager(){
		images = new ArrayList<>(3);
		mapName = new SimpleStringProperty("");
		backgroundColor = "0x8099ffff";
		borderColor = "0x333333ff";
		zoomLevel = 1;
		borderWidth = .2;
		height = 536;
		width = 802;
		directoryPath = "";
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
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public double getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(double borderWidth) {
		this.borderWidth = borderWidth;
	}

	public ArrayList<ImageDetail> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageDetail> images) {
		this.images = images;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public boolean getAllName(){
		for(SubRegion temp: subRegions){
			if(temp.getName().equals(""))
				return false;
		}
		return true;
	}

	public boolean getAllFlag(){
		for(SubRegion temp: subRegions) {
			File file = new File(FileManager.regionPath + "/" + temp.getName()+ " Flag.png");
			if ((!file.exists()))
				return false;
		}
		return true;
	}
	public boolean getAllCapital(){
		for(SubRegion temp: subRegions){
			if(temp.getCapital().equals(""))
				return false;
		}
		return true;
	}

	public boolean getAllLeaders(){
		for(SubRegion temp: subRegions) {
			File file = new File(FileManager.regionPath + "/" + temp.getLeader() + ".png");
			if (temp.getLeader().equals("") || (!file.exists()))
				return false;
		}
		return true;
	}

}
