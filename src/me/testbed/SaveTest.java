package testbed;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import file.FileManager;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;

import javax.json.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jappatel on 7/10/16.
 */
public class SaveTest {
	private DataManager dataManager;
	FileManager fileManager;
	private String coordinatePath = "src/me/raw_map_data/Andorra.json";

	@Before
	public void setUP() throws Exception{
		dataManager = new DataManager();
		fileManager = new FileManager(dataManager, new Controller());
		fileManager.setCoordinatePath(coordinatePath);
		makeAndorra();
	}

	private void makeAndorra() throws Exception{
		dataManager.setMapName("Andorra");
		dataManager.setBackgroundColor("#dc6e00");
		dataManager.setBorderColor("#000000");
		dataManager.setZoomLevel(400);
		dataManager.setBorderWidth(.1);

		dataManager.setHeight(536);
		dataManager.setWidth(802);
		dataManager.setDirectoryPath("World/Europe");

		SubRegion s1 = new SubRegion("Ordino", "Oridino (Town)", "Ventura Espot");
		s1.setColor(Color.grayRgb(200));

		SubRegion s2 = new SubRegion("Canillo", "Canillo (Town)", "Enric Casadevall Medrano");
		s2.setColor(Color.grayRgb(198));

		SubRegion s3 = new SubRegion("Encamp", "Encamp (Town)", "Miquel Alis Font");
		s3.setColor(Color.grayRgb(196));

		SubRegion s4 = new SubRegion("Escaldes-Engrodany", "Escaldes-Engordany (town)", "Montserrat Capdevila Pallarés");
		s4.setColor(Color.grayRgb(194));

		SubRegion s5 = new SubRegion("La Massana", "La Massana (town)","Josep Areny");
		s5.setColor(Color.grayRgb(192));

		SubRegion s6 = new SubRegion("Andorra la Vella", "Andorra la Vella (city)", "Maria Rosa Ferrer Obiols");
		s6.setColor(Color.grayRgb(190));

		SubRegion s7 = new SubRegion("Sant Julia de Loria", "Sant Julia de Loria (town)","Josep Pintat Forné");
		s7.setColor(Color.grayRgb(188));

		SubRegion[] subRegions = {s1, s2, s3, s4, s5, s6, s7};
		dataManager.setSubRegions(subRegions);

		ArrayList<ImageDetail> id = new ArrayList<>();
		id.add(new ImageDetail("src/me/export/The World/Europe/Andorra Flag.png", 580, 390));
		id.add(new ImageDetail("src/me/export/The World/Europe/image2.png", 6, 6));

		dataManager.setImages(id);


	}

	@After
	public void tearDown() throws Exception{

	}


	@org.junit.Test
	public void processSaveRequest() throws Exception {
		fileManager.processSaveRequest();
	}

}