package com.gcmrm.plugins;

import java.util.ArrayList;

import com.gcmrm.controllers.AlgoLoader;
import com.gcmrm.models.Algo;

import junit.framework.TestCase;

/***
 * Test the AlgoLoader class : how we read Algo from jars
 */
public class AlgoLoaderTest extends TestCase {
	
	/***
	 * Assure that no plugins are found when there isn't any plugins 
	 */
	public void testEmptyFolder() {
		ArrayList<Algo> al = AlgoLoader.getAlgoList("./src/test/resources/com/gcmrm");
		assertEquals("no plugins should be found",0,al.size());
	}
	
	/***
	 * Test on valid folder with valid plugins
	 */
	public void testFolder() {
		//load predefined plugins : 
		//		two first jar where the classes have the same path (package/nom.class)
		//		One jar with two class implementing the interface
		//		one last jar corresponding to a real algorithm 
		ArrayList<Algo> al = AlgoLoader.getAlgoList("./src/test/resources/com/gcmrm/testjars");
		
		assertEquals("five plugins should be found",5,al.size());
		assertEquals("alog's name should be TestImageJ",al.get(4).getName(),"TestImageJ");
		assertNotNull("alog's descritpion not accessible",al.get(0).getDescAlgo());
		assertNotNull("param's description not accessible",al.get(0).getDescParam());
		assertNotSame("the two loaded algo should be different event if they have the same path",al.get(0).getName(),al.get(1).getName());
		assertNotSame("the two loaded algo should be different event if they came from the same jar",al.get(2).getName(),al.get(3).getName());

	}
	
	/***
	 * Test measure can be done with valid photoPath
	 */
	public void testMeasure() {
		ArrayList<Algo> al = AlgoLoader.getAlgoList("./src/test/resources/com/gcmrm/testjars");
		
		assertNotNull(al.get(4).getMeasures("./src/test/resources/com/gcmrm/pictures/cphoto1.jpg",""));	
		
		}
	
}
