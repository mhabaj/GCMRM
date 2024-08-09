package com.gcmrm.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.gcmrm.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

/**
 * Class to handle selected algo and its parameters.
 */
public class AlgoSettings {
    private Algo selectedAlgo;
    private ArrayList<Parameter> selectedAlgoParams;
    private static String saveParams = "plugins/save.json";
    private static String saveAlgoName= "plugins/name.json";
    private boolean firstLaunch=true; //Boolean set to false after the first initialization no matter what
    
    /**
     * Constructor.
     */
    public AlgoSettings() {
        selectedAlgoParams = new ArrayList<Parameter>();
    }

    /**
     * Initialise current AlgoSettings Object with a selected Algo.
     * 
     * @param selectedAlgo Algo to setup
     * @throws Exception null parameter given to setupSelectedAlgo
     */
    public void setupSelectedAlgo(Algo selectedAlgo) throws Exception {
        if (selectedAlgo != null) {
            this.selectedAlgo = selectedAlgo;
            if(saveParamsExists() && firstLaunch)
            {
        		selectedAlgoParams=AlgoSettings.parseJsonFileToParams();
        		firstLaunch=false;
            }
        	else {
        		selectedAlgoParams = AlgoSettings.parseAlgoJsonToParams(selectedAlgo.getDescParam());
        		firstLaunch=false;
        	}
        } else {
            throw new Exception("Error: null parameter given to setupSelectedAlgo.\n");
        }
    }

    /**
     * Parse the Json file (string). Input example: 
     * [ { "name":"test1", "type":"int", "value":1, "description": "-----" }, { "name":"test2",
     * "type":"float", "value":0.0, "description": "-----" } ]
     * 
     * @param jsonInput an arrayList of parameter that has been jsoned
     * @return an arraylist of Parameter
     */
    public static ArrayList<Parameter> parseAlgoJsonToParams(String jsonInput) {
        Gson gson = new GsonBuilder().create();
        ArrayList<Parameter> listOfParams = gson.fromJson(jsonInput, new TypeToken<ArrayList<Parameter>>() {
        }.getType());
        return listOfParams;
    }
    
    /**
     * Parse the Json file contained in saveParams. Input example: 
     * [ { "name":"test1", "type":"int", "value":1, "description": "-----" }, { "name":"test2",
     * "type":"float", "value":0.0, "description": "-----" } ]
     * 
     * @throws JsonIOException wrong syntax in save.json
     * @throws IOException can't read the file
     * @return an arraylist of Parameter
     */
    public static ArrayList<Parameter> parseJsonFileToParams() throws JsonIOException, IOException
    {
    	try (FileReader fr = new FileReader(saveParams)) {
    		Gson gson = new GsonBuilder().create();
    		ArrayList<Parameter> listOfParams = gson.fromJson(fr, new TypeToken<ArrayList<Parameter>>() {
            }.getType());
    		return listOfParams;
    	}
    }
    
    /**
     * @return the selectedAlgo
     */
    public Algo getSelectedAlgo() {
        return selectedAlgo;
    }

    /**
     * @return the selectedAlgoParams
     */
    public ArrayList<Parameter> getSelectedAlgoParams() {
            return selectedAlgoParams;
    }
    
    /**
     * Save the name and its parameters of the last algorithm used in json file
     * @throws JsonIOException wrong syntax in name.json
     * @throws IOException can't read the file
     */
    public void saveAlgo() throws JsonIOException, IOException
    {
    	if(selectedAlgo!=null) {
	    	try (FileWriter fw = new FileWriter(saveAlgoName)) {
	    		Gson gson = new GsonBuilder().create();
	        	gson.toJson(selectedAlgo.getName(),fw);
	    	}
	    	try (FileWriter fw = new FileWriter(saveParams)) {
	    		Gson gson = new GsonBuilder().create();
	        	gson.toJson(selectedAlgoParams,fw);
	    	}
    	}
    }
    
    /**
     * Get the name of the last algorithm used
     * @throws IOException can't read the file
     * @throws FileNotFoundException can't find the file
     * @return name of the algo from name.json
     */
    public String getAlgoName() throws FileNotFoundException, IOException
    {
    	try (FileReader fr = new FileReader(saveAlgoName)) {
    		Gson gson = new GsonBuilder().create();
    		String nameOfAlgo = gson.fromJson(fr,String.class);
    		return nameOfAlgo;
    	}
    }
    
    /**
     * @return true if the file saveAlgoName exists, else false
     */
    public boolean saveNameExists()
    {
    	File fileParams = new File(saveAlgoName);
    	return fileParams.exists();
    }
    
    /**
     * @return true if the file saveParams exists, else false
     */
    public boolean saveParamsExists()
    {
    	File fileParams = new File(saveParams);
    	return fileParams.exists();
    }
    
}
