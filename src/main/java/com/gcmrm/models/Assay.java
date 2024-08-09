package com.gcmrm.models;

import java.util.ArrayList;
import java.util.Date;


/**
 * Model class: Assay contains all images.
 */
public class Assay {

	private ArrayList<Image> imageList;
	private int id;
	private Date creationDate;

	/**
	 * Constructor
	 */
	public Assay() {
        imageList = new ArrayList<Image>();
	}

	/**
	 * @param imageList ArrayList of image
	 * @param id int Assay's ID
	 * @param creationDate Date date of creation
	 */
	public Assay(ArrayList<Image> imageList, int id, Date creationDate) {

		this.imageList = imageList;
		this.id = id;
		this.creationDate = creationDate;
	}

	/**
	 * @return the imageList
	 */
	public ArrayList<Image> getImageList() {
		return imageList;
	}

	/**
	 * @param imageList the imageList to set
	 */
	public void setImageList(ArrayList<Image> imageList) {
		this.imageList = imageList;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
