package com.gcmrm.controllers;

import java.util.ArrayList;

import com.gcmrm.models.Image;

/**
 * Manages Campaign Exports types.
 */
public interface CampaignVisitor {
	
	/**
	 * @param images ArrayList of Image
	 * @param FilePath String
	 * @throws Exception Java Exception
	 */
	public void visit(ArrayList<Image> images, String FilePath) throws Exception;

}
