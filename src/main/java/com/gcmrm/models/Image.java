package com.gcmrm.models;

import java.util.ArrayList;

import com.gcmrm.controllers.ImageVisitor;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.CanvasResizer;
import ij.process.ImageProcessor;

/**
 * Model class: Image contains all measurements and URL to this specific image.
 */
public class Image {
	private ArrayList<Measurement> measurements; // first one is the latest.
	private int id;
	private String url;

	/**
	 * Constructor.
	 */
	public Image() {
		measurements = new ArrayList<Measurement>();
	}

	/**
	 * @param measurements ArrayList of Measurement
	 * @param id int Image's ID
	 * @param url String url of image
	 */
	public Image(ArrayList<Measurement> measurements, int id, String url) {
		super();
		this.measurements = measurements;
		this.id = id;
		this.url = url;
	}

	/**
	 * @return the measurements
	 */
	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}

	/**
	 * @param measurements the measurements to set
	 */
	public void setMeasurements(ArrayList<Measurement> measurements) {
		this.measurements = measurements;
	}

	/**
	 * add a measure to the existing list of measurement
	 * 
	 * @param measure Measurement Object to add
	 */
	public void addMeasure(Measurement measure) {
		measurements.add(measure);
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
	 * @return String the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

    /**
     * Accept the visitor.
     * @param visitor image visitor
     * @throws Exception Multiple Exception types (not specific Exception)
     */
    public void accept(ImageVisitor visitor) throws Exception {
        visitor.visit(this);
    }
    
    /**
     * Save the image as jpg after adding cross from the latest Measurement
     * @param path needs to end by .jpg (ex: "C:/file/image.jpg")
     */
    public void saveJPG(String path) {
		ImagePlus imOriginale = IJ.openImage(url);
		ImagePlus imOutput = imOriginale.duplicate();
		ImageProcessor ip = imOutput.getProcessor();	  
		imOutput.setProcessor(ip);
		ImageProcessor ipOut = imOutput.getProcessor();
		ipOut.setColor(java.awt.Color.RED);
		ipOut.setFontSize(12);
		ipOut.setLineWidth(2);
		
		int delta = 5; // cross size
		int width = imOutput.getWidth();
		int height = imOutput.getHeight();
		
		//if the image has no measurements it's just a copy
		if (measurements.size()>0) { 
			//we draw each cross
			for(Point p : measurements.get(0).getPoints()) {
				int xcell = (int)p.getX();
				int ycell = (int)p.getY();
				int ncell = (int)p.getQuantity();
				// cross coordinate
				int x1 = Math.max(0,xcell-delta);
				int y1 = Math.max(0,ycell-delta);
				int x2 = Math.min(width,xcell+delta);
				int y2 = Math.min(width,ycell+delta);
				int x3 = Math.max(0,xcell-delta);
				int y3 = Math.min(width,ycell+delta);
				int x4 = Math.min(width,xcell+delta);
				int y4 = Math.max(0,ycell-delta);
			 
				//colors variation
				if ( ncell > 1 ) {
					ipOut.setColor(java.awt.Color.YELLOW);
					ipOut.drawString(""+ncell, xcell-5, ycell+20);
				}
				else
					ipOut.setColor(java.awt.Color.RED);
				 
				ipOut.drawLine(x1, y1, x2, y2);
				ipOut.drawLine(x3, y3, x4, y4);
			
			}
	
			
			//number of counted objects is printed at the bottom
			CanvasResizer cr = new CanvasResizer();
			ipOut = cr.expandImage(ipOut, width, height+35, 0, 0);
			ipOut.setColor(java.awt.Color.WHITE);
			ipOut.setFontSize(20);
			ipOut.drawString("Total : "+measurements.get(0).getPointsCount(), 25, height+30);
			imOutput.setProcessor(ipOut);
			
			IJ.save(imOutput,path);
		}
    }
}
