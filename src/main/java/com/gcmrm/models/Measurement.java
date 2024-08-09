package com.gcmrm.models;

import java.util.ArrayList;

/**
 * Model class: Measurement contains points of interest.
 */
public class Measurement {
	private ArrayList<Point> points;
	private boolean corrected;
	private boolean isActive;
	private String lastModificationDate;

	/**
	 * Constructor.
	 */
	public Measurement() {
		points = new ArrayList<Point>();
	}

	/**
	 * @param points ArrayList of points 
	 * @param corrected boolean 
	 * @param isActive boolean
	 * @param lastModificationDate String last date of modification
	 */
	public Measurement(ArrayList<Point> points, boolean corrected, boolean isActive, String lastModificationDate) {
		super();
		this.points = points;
		this.corrected = corrected;
		this.isActive = isActive;
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * @return Total Points count
	 */
	public int getPointsCount() {

		int count = 0;

		for (Point currentPoint : points) {
			count += currentPoint.getQuantity();
		}

		return count;
	}

	/**
	 * @return the points
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}

	/**
	 * Removes a point given it's id and coords
	 * 
	 * @param id int 
	 * @return true if successful
	 */
	public boolean removePoint(int id) {
		int loop = 0;
		for (Point currentPoint : points) {
			if (currentPoint.getId() == id) {
				points.remove(loop);
				return true;
			}
			loop++;

		}
		return false;
	}


	/**
	 * Edit a point
	 * @param id int
	 * @param newQuantity int
	 * @return boolean true if successful
	 */
	public boolean editPoint(int id, int newQuantity) {

		for (Point currentPoint : points) {
			if (currentPoint.getId() == id) {
				currentPoint.setQuantity(newQuantity);
				return true;
			}
		}
		return false;
	}

	/**
	 * generate the next available id
	 * @return int unused id
	 */
	public int getAvailableId() {
		int idAvailable = 0;
		int loop = 0;
		while (loop != points.size()) {

			if (points.get(loop).getId() >= idAvailable) {
				idAvailable = points.get(loop).getId() + 1;
			}
			loop++;

		}
		return idAvailable;
	}

	/**
	 * add new point with x and y coords
	 * @param x int
	 * @param y int
	 * @return boolean true if successful
	 */
	public boolean addPoint(int x, int y) {

		points.add(new Point(getAvailableId(), x, y, 1));
		return true;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	/**
	 * to get a point by his id
	 * @param id linked to a unique point
	 * @return point if found, null otherwise
	 */
	public Point getPoint(int id) {
		for (Point p : points) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	/**
	 * @return the corrected
	 */
	public boolean isCorrected() {
		return corrected;
	}

	/**
	 * @param corrected the corrected to set
	 */
	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate() {
		return lastModificationDate;
	}

	/**
	 * @param lastModificationDate no forced syntax, recommendation : localDateTime.toString
	 */
	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

}
