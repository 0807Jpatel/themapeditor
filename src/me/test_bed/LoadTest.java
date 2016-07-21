package test_bed;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import file.FileManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jappatel on 7/11/16.
 */
public class LoadTest {
	static DataManager dataManager;
	static FileManager fileManager;
	public static void main(String[] args) throws IOException{
		load("src/me/work/Andorra");
		print();
	}

	public static void load(String path) throws IOException{
		dataManager = new DataManager();
		fileManager = new FileManager(dataManager, new Controller());
		fileManager.loadData(path);
	}

	private static void print(){
		System.out.println(dataManager.getMapName());
		System.out.println(dataManager.getBackgroundColor());
		System.out.println(dataManager.getBorderColor());
		System.out.println(dataManager.getZoomLevel());
		System.out.println(dataManager.getBorderWidth());
		System.out.println(dataManager.getHeight());
		System.out.println(dataManager.getWidth());
		System.out.println(dataManager.getDirectoryPath());
		ArrayList<ImageDetail> id = dataManager.getImages();
		for (ImageDetail anId : id) System.out.println(anId);
////		SubRegion[] sub = dataManager.getSubRegions();
//		for(SubRegion temp: sub)
//			System.out.println(temp);
	}
}
