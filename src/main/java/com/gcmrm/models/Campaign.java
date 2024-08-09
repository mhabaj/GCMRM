package com.gcmrm.models;

import java.util.ArrayList;

/**
 * Model class: Campaign contains all Assays.
 */
public class Campaign {

	private ArrayList<Assay> assayList;

	/**
	 * Constructor.
	 */
	public Campaign() {
	}

	/**
	 * @param assayList ArrayList of Assay 
	 */
	public Campaign(ArrayList<Assay> assayList) {
		super();
		this.assayList = assayList;
	}

	/**
	 * @return ArrayList of Assay
	 */
	public ArrayList<Assay> getAssayList() {
		return assayList;
	}

	/**
	 * @param assayList ArrayList of assay
	 */
	public void setAssayList(ArrayList<Assay> assayList) {
		this.assayList = assayList;
	}

}
