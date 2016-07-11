package test_bed;

import controller.Controller;
import data.DataManager;
import data.ImageDetail;
import data.SubRegion;
import file.FileManager;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by jappatel on 7/11/16.
 */
public class SaveTest {
    private static DataManager dataManager = new DataManager();
    private static FileManager fileManager;
    public static void main(String[] args) throws Exception{
	    makeAndorra("raw_map_data/Andorra.json");
    }

    public static void makeAndorra(String path) throws Exception{
	dataManager = new DataManager();
	fileManager = new FileManager(dataManager, new Controller());
	fileManager.setCoordinatePath(path);
	dataManager.setMapName("Andorra");
	dataManager.setBackgroundColor("#dc6e00");
	dataManager.setBorderColor("#000000");
	dataManager.setZoomLevel(400);
	dataManager.setBorderWidth(.1);
	dataManager.setHeight(536);
	dataManager.setWidth(802);
	dataManager.setDirectoryPath("src/me/export/The World/Europe");

	SubRegion s1 = new SubRegion("Ordino", "Ventura Espot", "Oridino (Town)");
	s1.setColor(Color.grayRgb(200));

	SubRegion s2 = new SubRegion("Canillo",  "Enric Casadevall Medrano", "Canillo (Town)");
	s2.setColor(Color.grayRgb(198));

	SubRegion s3 = new SubRegion("Encamp",  "Miquel Alis Font", "Encamp (Town)");
	s3.setColor(Color.grayRgb(196));

	SubRegion s4 = new SubRegion("Escaldes-Engrodany", "Montserrat Capdevila Pallarés", "Escaldes-Engordany (town)");
	s4.setColor(Color.grayRgb(194));

	SubRegion s5 = new SubRegion("La Massana","Josep Areny", "La Massana (town)");
	s5.setColor(Color.grayRgb(192));

	SubRegion s6 = new SubRegion("Andorra la Vella", "Maria Rosa Ferrer Obiols", "Andorra la Vella (city)");
	s6.setColor(Color.grayRgb(190));

	SubRegion s7 = new SubRegion("Sant Julia de Loria", "Josep Pintat Forné", "Sant Julia de Loria (town)");
	s7.setColor(Color.grayRgb(188));

	SubRegion[] subRegions = {s1, s2, s3, s4, s5, s6, s7};
	dataManager.setSubRegions(subRegions);

	ArrayList<ImageDetail> id = new ArrayList<>();
	id.add(new ImageDetail("src/me/export/The World/Europe/Andorra Flag.png", 580, 390));
	id.add(new ImageDetail("src/me/export/The World/Europe/image2.png", 6, 6));

	dataManager.setImages(id);
	fileManager.processSaveRequest();
    }

    static void makeSanMarino(String path) throws Exception {
	dataManager = new DataManager();
	fileManager = new FileManager(dataManager, new Controller());
	fileManager.setCoordinatePath(path);
	dataManager.setMapName("SanMarino");
	dataManager.setBackgroundColor("#dc6e00");
	dataManager.setBorderColor("#000000");
	dataManager.setZoomLevel(400);
	dataManager.setBorderWidth(.1);
	dataManager.setHeight(536);
	dataManager.setWidth(802);
	dataManager.setDirectoryPath("src/me/export/The World/Europe");

	SubRegion s1 = new SubRegion("Acquaviva", "Lucia Tamagnini", "");
	s1.setColor(Color.grayRgb(225));

	SubRegion s2 = new SubRegion("Borgo Maggiore","Sergio Nanni", "");
	s2.setColor(Color.grayRgb(200));

	SubRegion s3 = new SubRegion("Chiesanuova","Franco Santi", "");
	s3.setColor(Color.grayRgb(175));

	SubRegion s4 = new SubRegion("Domagnano", "Gabriel Guidi", "");
	s4.setColor(Color.grayRgb(150));

	SubRegion s5 = new SubRegion("Faetano","Pier Mario Bedetti", "");
	s5.setColor(Color.grayRgb(125));

	SubRegion s6 = new SubRegion("Fiorentino", "Gerri Fabbri", "");
	s6.setColor(Color.grayRgb(100));

	SubRegion s7 = new SubRegion("Montegiardino", "Marta Fabbri", "");
	s7.setColor(Color.grayRgb(75));
	
	SubRegion s8 = new SubRegion("City of San Marino", "Maria Teresa Beccari", "");
	s8.setColor(Color.grayRgb(50));
	
	SubRegion s9 = new SubRegion("Serravalle", "Leandro Maiani", "");
	s9.setColor(Color.grayRgb(25));
	
	
	

	SubRegion[] subRegions = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
	dataManager.setSubRegions(subRegions);

	ArrayList<ImageDetail> id = new ArrayList<>();
	dataManager.setImages(id);

	fileManager.processSaveRequest();
    }
    
    public static void makeSlovakia(String path) throws Exception{
	dataManager = new DataManager();
	fileManager = new FileManager(dataManager, new Controller());
	fileManager.setCoordinatePath(path);
	dataManager.setMapName("Slovakia");
	dataManager.setBackgroundColor("#dc6e00");
	dataManager.setBorderColor("#000000");
	dataManager.setZoomLevel(200);
	dataManager.setBorderWidth(.1);
	dataManager.setHeight(536);
	dataManager.setWidth(802);
	dataManager.setDirectoryPath("src/me/export/The World/Europe");

	SubRegion s1 = new SubRegion("Bratislava", "", "");
	s1.setColor(Color.grayRgb(250));

	SubRegion s2 = new SubRegion("Trnava",  "", "");
	s2.setColor(Color.grayRgb(249));

	SubRegion s3 = new SubRegion("Trencin", "", "");
	s3.setColor(Color.grayRgb(248));
	
	SubRegion s4 = new SubRegion("Nitra",  "", "");
	s4.setColor(Color.grayRgb(247));

	SubRegion s5 = new SubRegion("Zilina", "", "");
	s5.setColor(Color.grayRgb(246));

	SubRegion s6 = new SubRegion("Banska Bystrica","", "");
	s6.setColor(Color.grayRgb(245));

	SubRegion s7 = new SubRegion("Presov", "", "");
	s7.setColor(Color.grayRgb(244));

	SubRegion s8 = new SubRegion("Kosice", "", "");
	s8.setColor(Color.grayRgb(188));

	SubRegion[] subRegions = {s1, s2, s3, s4, s5, s6, s7, s8};
	dataManager.setSubRegions(subRegions);

	ArrayList<ImageDetail> id = new ArrayList<>();
	id.add(new ImageDetail("src/me/export/The World/Europe/Solvakia Flag.png", 543, 369));
	id.add(new ImageDetail("src/me/export/The World/Europe/image3.png", 24, 32));

	dataManager.setImages(id);
	fileManager.processSaveRequest();
    }
}
