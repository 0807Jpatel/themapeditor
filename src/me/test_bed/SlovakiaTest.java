/**
 *
 * @author jappatel
 */
package test_bed;

import data.DataManager;
import data.SubRegion;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by  on 7/11/16.
 */
public class SlovakiaTest{
	DataManager dataManager;

   /**
    * Test of main method, of class SaveTest.
     */
    @Test
    public void testMain() throws Exception {
	System.out.println("main");
    	File file = new File(getClass().getClassLoader().getResource("raw_map_data/Slovakia.json").getFile());
	SaveTest.makeSlovakia(file.toPath().toString());

	LoadTest.load("src/me/work/Slovakia");
	dataManager = LoadTest.dataManager;

	assertEquals("Slovakia", dataManager.getMapName());
	assertEquals("#dc6e00", dataManager.getBackgroundColor());
	assertEquals("#000000", dataManager.getBorderColor());
	assertEquals(0.08, dataManager.getBorderWidth(), .01);
	assertEquals(50.0, dataManager.getZoomLevel(), .01);
	assertEquals("src/me/export/The World/Europe", dataManager.getDirectoryPath());

	SubRegion[] subRegions = dataManager.getSubRegions();

	assertEquals("Bratislava", subRegions[0].getName());
	assertEquals("Kosice", subRegions[7].getName());
	
    }

}