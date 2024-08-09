package com.gcmrm.plugins;

import java.util.ArrayList;

import org.junit.Test;

import com.gcmrm.controllers.AlgoSettings;
import com.gcmrm.models.Parameter;

import junit.framework.TestCase;

/**
 * Testing algoSetting class : fetch and handle the selected algo and its parameters.
 */
public class AlgoSettingsControllerTest extends TestCase {

	private String validJson = "[\r\n" + "  {\r\n" + "    \"name\":\"test1\",\r\n" + "    \"type\":\"int\",\r\n"
			+ "    \"value\":1,\r\n" + "    \"description\": \"nombre entre 0 et 3\"\r\n" + "  },\r\n" + "  {\r\n"
			+ "    \"name\":\"test2\",\r\n" + "    \"type\":\"float\",\r\n" + "    \"value\":0.0,\r\n"
			+ "    \"description\": \"valeur gamma\"\r\n" + "  }\r\n" + "]";
	private String emptyJson = "";
	private Parameter param1 = new Parameter("test1", "int", "1", "nombre entre 0 et 3");
	private Parameter param2 = new Parameter("test2", "float", "0.0", "valeur gamma");

    /**
     * test parsing parameters with valid json.
     */
	@Test
	public void testParseAlgoJsonToParamsWithValidJson() {
		AlgoSettings algo = new AlgoSettings();
		ArrayList<Parameter> params = algo.parseAlgoJsonToParams(validJson);

        // Test the parameters one by one.
		assertEquals(param1.getName(), params.get(0).getName());
		assertEquals(param1.getType(), params.get(0).getType());
		assertEquals(param1.getDescription(), params.get(0).getDescription());
		assertEquals(param1.getValue(), params.get(0).getValue());
		assertEquals(param2.getName(), params.get(1).getName());
		assertEquals(param2.getType(), params.get(1).getType());
		assertEquals(param2.getDescription(), params.get(1).getDescription());
		assertEquals(param2.getValue(), params.get(1).getValue());
	}

    /**
     * test parsing parameters with empty json.
     */
	@Test
	public void testParseAlgoJsonToParamsWithEmptyJson() {
		AlgoSettings algo = new AlgoSettings();
		ArrayList<Parameter> params = algo.parseAlgoJsonToParams(emptyJson);

		assertEquals(params, null);
	}

}