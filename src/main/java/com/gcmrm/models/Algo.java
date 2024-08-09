package com.gcmrm.models;

/**
 * Interface for handling image detection algorithms.
 * This interface must be inplemented by the plugins to work.
 */
public interface Algo {
	
	/***
	 * @return Name of the algo
	 */
	public String getName();
	
	/***
	 * @return Description of the algo
	 */
	public String getDescAlgo();
	
	/**
	 * @return string describing what is counted
	 */
	public String getCountedObject();
	
	/**
	 * @return informations about the parameter
	 */
	public String getPrecondition();
	
	/**
	 * @return informations about the return of getMeasures
	 */
	public String getPostcondition();
	
	/***
	 * get information about parameters syntax
	 * @return structure example :
	 * [
	 *	  {
	 *	    "name":"test1",
	 *	    "type":"int",
	 *	    "value":1,
	 *	    "description": "nombre entre 0 aet 3"
	 *	  },
	 *	  {
	 *	    "name":"test2",
	 *	    "type":"float",
	 *	    "value":0.0,
	 *	    "description": "valeur gamma"
	 *	  }
	 * ]
	 */
	public String getDescParam();
	
	/***
	 * get the coordinates of the nucleus
	 * @param photoPath path of the image to scan
	 * @param algoParam json string following @see getDescParam
	 * @return Json string corresponding to the class Measurement
	 */
	public String getMeasures(String photoPath, String algoParam);
	
	@Override
	/**
	 * @return a short string to recognize the algo
	 */
	public String toString();
}
