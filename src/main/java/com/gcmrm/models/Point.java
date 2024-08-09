package com.gcmrm.models;

/**
 * Model class: Point contains the coordinates of a point of interest.
 */
public class Point {
	private int id;
	private float x;
	private float y;
	private int quantity;
	
	/**
	 * Constructor.
	 */
	public Point() {
	}

	/**
	 * @param id int
	 * @param x float
	 * @param y float
	 * @param quantity int number of entity represented by this point.
	 */
	public Point(int id, float x, float y, int quantity) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.quantity = quantity;
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
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * @return the number of entity represented by this point
	 */
	public float getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity number of entity represented by this point
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
