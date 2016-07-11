package testbed;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import file.FileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jappatel on 7/10/16.
 */
public class LoadTest {
	private DataManager dataManager;
	private FileManager fileManager;

	@Before
	public void setUP() throws Exception{
		dataManager = new DataManager();
		fileManager = new FileManager(dataManager, new Controller());
	}

	@Test
	public void processLoadRequest() throws Exception {
		String path = "work/Andorra";
		fileManager.loadData(path);
	}

	@After
	public void tearDown() throws Exception{
		System.out.println(dataManager.getMapName());
		System.out.println(dataManager.getBackgroundColor());
		System.out.println(dataManager.getBorderColor());
		System.out.println(dataManager.getZoomLevel());
		System.out.println(dataManager.getBorderWidth());
		System.out.println(dataManager.getHeight());
		System.out.println(dataManager.getWidth());
		System.out.println(dataManager.getDirectoryPath());
		ArrayList<ImageDetail> id = dataManager.getImages();
		for(int x = 0; x < id.size(); x++)
			System.out.println(id.get(x));
		SubRegion[] sub = dataManager.getSubRegions();
		for(SubRegion temp: sub)
			System.out.println(temp);
	}

}