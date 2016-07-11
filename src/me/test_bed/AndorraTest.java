package test_bed;

import data.DataManager;
import data.SubRegion;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by jappatel on 7/11/16.
 */
public class AndorraTest{
	DataManager dataManager;

   /**
    * Test of main method, of class SaveTest.
     */
    @Test
    public void testMain() throws Exception {
	System.out.println("main");
    	File file = new File(getClass().getClassLoader().getResource("raw_map_data/Andorra.json").getFile());
	SaveTest.makeAndorra(file.toPath().toString());

	LoadTest.load("src/me/work/Andorra");
	dataManager = LoadTest.dataManager;

	assertEquals("Andorra", dataManager.getMapName());
	assertEquals("#dc6e00", dataManager.getBackgroundColor());
	assertEquals("#000000", dataManager.getBorderColor());
	assertEquals(0.1, dataManager.getBorderWidth(), .01);
	assertEquals(0.1, dataManager.getBorderWidth(), .01);
	assertEquals(400.0, dataManager.getZoomLevel(), .01);
	assertEquals("src/me/export/The World/Europe", dataManager.getDirectoryPath());

	SubRegion[] subRegions = dataManager.getSubRegions();

	assertEquals("Ordino", subRegions[0].getName());
	assertEquals("Ventura Espot", subRegions[0].getLeader());
	assertEquals("Oridino (Town)", subRegions[0].getCapital());

	assertEquals("Sant Julia de Loria", subRegions[6].getName());
	assertEquals("Josep Pintat Forné", subRegions[6].getLeader());
	assertEquals("Sant Julia de Loria (town)", subRegions[6].getCapital());
    }

}