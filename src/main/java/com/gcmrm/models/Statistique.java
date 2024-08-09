package com.gcmrm.models;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Used to compute recall score and precision score for each algo in the app
 * @author Jean-Malo
 *
 */
public class Statistique {

	private float false_positive = 0; // How many points removed
	private float false_negative = 0; // How many points added
	private float total_count = 0; // Total points count (after correction)
	private float recall_score;
	private float precision_score;

	/**
	 * Constructor
	 * Look if there is a json file corresponding to the name of the algo
	 * 
	 * @param algoName name of the algo
	 */
	public Statistique(String algoName) {
		String saveStats = "plugins/" + algoName + "Stats.json";
		File fileStats = new File(saveStats);
		ArrayList<Float> statsToSave = new ArrayList<Float>();

		if (fileStats.exists()) {
			try (FileReader fr = new FileReader(saveStats)) {
				Gson gson = new GsonBuilder().create();
				statsToSave = gson.fromJson(fr, new TypeToken<ArrayList<Float>>() {
				}.getType());
				total_count += statsToSave.get(0);
				false_negative += statsToSave.get(1);
				false_positive += statsToSave.get(2);

			}
			catch(IOException e) {
				System.out.println("Error parsing "+algoName+"stat.json (delete the file to fix)");
				e.printStackTrace();
			}
		}

	}

	/**
	 * Actualize the stats when a point is add
	 */
	public void ajoutPoint() {
		false_negative++;
	}

	/**
	 * Actualize the stats when a point is remove
	 */
	public void retraitPoint() {
		false_positive++;
	}

	/**
	 * compute recall and precision based on total_count, false_positive and false_negative
	 */
	public void calcStats() {
		// recall
		if(total_count - false_positive + false_negative == 0)
			recall_score = 0;
		else
			recall_score = (total_count - false_positive) / (total_count - false_positive + false_negative); 

		// Precision
		if(total_count == 0)
			precision_score=0;
		else
			precision_score = (total_count - false_positive) / total_count; 
	}

	/**
	 * @param algoName String to specify witch algo is saved
	 */
	public void saveStats(String algoName) {
		String saveStats = "plugins/" + algoName + "Stats.json";
		
		if (total_count != 0) {
			calcStats();
			ArrayList<Float> statsToSave = new ArrayList<Float>();
			
			statsToSave.add((float) total_count);
			statsToSave.add((float) false_negative);
			statsToSave.add((float) false_positive);
			statsToSave.add((float) recall_score);
			statsToSave.add((float) precision_score);
			
			try (FileWriter fw = new FileWriter(saveStats)) {
				Gson gson = new GsonBuilder().create();

				gson.toJson(statsToSave, fw);

			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the false_positive
	 */
	public float getFalse_positive() {
		return false_positive;
	}

	/**
	 * update flase positive value
	 * @param false_positive of type int so the value is valid
	 */
	public void setFalse_positive(int false_positive) {
		this.false_positive = false_positive;
	}

	/**
	 * @return float the false_negative
	 */
	public float getFalse_negative() {
		return false_negative;
	}

	/**
	 * Update false Negative attribute
	 * @param false_negative of type int so the value is valid
	 */
	public void setFalse_negative(int false_negative) {
		this.false_negative = false_negative;
	}

	/**
	 * @return the total_count
	 */
	public float getTotal_count() {
		return total_count;
	}

	/**
	 * update total count value
	 * @param total_count of type int so the value is valid
	 */
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	/**
	 * @return the recall_score
	 */
	public float getRecall_score() {
		if(total_count - false_positive + false_negative != 0)
			recall_score = (total_count - false_positive) / (total_count - false_positive + false_negative); 
		else
			recall_score=0;
		return recall_score;
	}

	/**
	 * @param recall_score the recall_score to set
	 */
	public void setRecall_score(float recall_score) {
		this.recall_score = recall_score;
	}

	/**
	 * @return the precision_score
	 */
	public float getPrecision_score() {
		if(total_count != 0)
			precision_score = (total_count - false_positive) / total_count; 
		else
			precision_score = 0;
		return precision_score;
	}

	/**
	 * @param precision_score the precision_score to set
	 */
	public void setPrecision_score(float precision_score) {
		this.precision_score = precision_score;
	}

	/**
	 * Update the number of object found by this algo
	 * @param imageList ArrayList of Image just analyzed
	 */
	public void updateTotalCount(ArrayList<Image> imageList) {
		for (int i = 0; i < imageList.size(); i++) {
			total_count += imageList.get(i).getMeasurements().get(0).getPointsCount();
		}
		
	}
}