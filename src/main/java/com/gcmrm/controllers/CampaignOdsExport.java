package com.gcmrm.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jopendocument.dom.spreadsheet.SpreadSheet;

import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;


/**
 * Manages Image to .ODS Export
 */
public class CampaignOdsExport implements CampaignVisitor {
	
	/**
	 * default constructor (empty)
	 */
	public CampaignOdsExport() {
			
	}

	/**
	 * Check the list of image and create the corresponding spreadsheet (.ods)
	 * @param images all the images that will be in the file
	 * @param FilePath where the ods file will be saved
	 */
    @Override
    public void visit(ArrayList<Image> images, String FilePath) throws FileNotFoundException, IOException {
    	try {
    		// creation of the object that will be exported in the spreadsheet
	    	final Object[][] data= new Object[99][2];
	    	
	    	//each image corresponds to a line
	    	for(int imageIterator=0; imageIterator<images.size();imageIterator++) {
	    	
	    		//we get the url
	    		String urlImage=images.get(imageIterator).getUrl();
	    			
	    		//we get the points found
	    		ArrayList<Measurement> listM=images.get(imageIterator).getMeasurements();
	    		int nbPoint =listM.get(0).getPointsCount();
	    			
	    		//we insert them in the spreadsheet
	    		String namePhoto=urlImage.substring(urlImage.lastIndexOf(File.separator)+1);
	    		data[imageIterator]= new Object[] {namePhoto, nbPoint};
	    	}
	    	
	    	//add column name 
	    	String[] columns = new String[] { "Images", "Objets Comptés" };
	    	
	    	//tableModel for spreadSheet constructor
	    	TableModel model = new DefaultTableModel(data, columns);
	    	
	    	//creating file with path given as parameter
	    	 final File file = new File(FilePath);
	    	  
	    	 SpreadSheet.createEmpty(model).saveAs(file);
	    	 
    	}catch (IOException e) {
    		
			e.printStackTrace();
		}
    		
    }
    	

}

