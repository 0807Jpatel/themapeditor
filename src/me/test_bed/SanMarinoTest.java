package test_bed;

import data.DataManager;
import data.SubRegion;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 * Created by  on 7/11/16.
 */
public class SanMarinoTest{
	DataManager dataManager;

   /**
    * Test of main method, of class SaveTest.
     */
    @Test
    public void testMain() throws Exception {
	System.out.println("main");
    	File file = new File(getClass().getClassLoader().getResource("raw_map_data/SanMarino.json").getFile());
	SaveTest.makeSanMarino(file.toPath().toString());

	LoadTest.load("src/me/work/SanMarino");
	dataManager = LoadTest.dataManager;

	assertEquals("SanMarino", dataManager.getMapName());
	assertEquals("#dc6e00", dataManager.getBackgroundColor());
	assertEquals("#000000", dataManager.getBorderColor());
	assertEquals(0.1, dataManager.getBorderWidth(), .01);
	assertEquals(0.1, dataManager.getBorderWidth(), .01);
	assertEquals(400.0, dataManager.getZoomLevel(), .01);
	assertEquals("src/me/export/The World/Europe", dataManager.getDirectoryPath());

	SubRegion[] subRegions = dataManager.getSubRegions();

	assertEquals("Acquaviva", subRegions[0].getName());
	assertEquals("Lucia Tamagnini", subRegions[0].getLeader());

	assertEquals("Serravalle", subRegions[8].getName());
	assertEquals("Leandro Maiani", subRegions[8].getLeader());

    }
}