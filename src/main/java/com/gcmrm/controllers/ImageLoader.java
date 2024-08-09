package com.gcmrm.controllers;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

/**
 * Manages image import and loading from files and folders.
 */
public class ImageLoader {
	private ArrayList<String> pathsList;

	/**
	 * Parse files and folders for images to fill pathsList. This function is
	 * recursive.
	 * 
	 * @param nextDirectoryUrl String Url of the Directory to analyse.
	 * @throws Exception error reading the files
	 */
	public void loadImages(String nextDirectoryUrl) throws Exception {
		try {
			File currentFile = new File(nextDirectoryUrl);

			File[] imageFiles = currentFile.listFiles(); // This gets all of the files inside 'file', if 'file' is a
															// folder
			for (File f : imageFiles) {

				// File extension filter (only jpg and tiff)
				if (FilenameUtils.getExtension(f.getAbsolutePath()).equals("jpg")
						|| FilenameUtils.getExtension(f.getAbsolutePath()).equals("tiff")
						|| FilenameUtils.getExtension(f.getAbsolutePath()).equals("tif")
						|| FilenameUtils.getExtension(f.getAbsolutePath()).equals("png")
						|| FilenameUtils.getExtension(f.getAbsolutePath()).equals("jpeg")
						|| FilenameUtils.getExtension(f.getAbsolutePath()).equals("JPEG")) {

					String pathBuffer = f.getAbsolutePath();
					pathsList.add(pathBuffer);

				} else if (f.isDirectory()) {
					loadImages(f.getAbsolutePath());
				}

			}
		} catch (Exception e) {
			throw new Exception("Erreur loadImages");

		}
	}

	

	/**
	 * Get the file with the specified path.
	 * 
	 * @param path String of the file's path.
	 * 
	 * @return File
	 */
	public File getFileFromPath(String path) {
		try {
			File file = new File(path);
			return file;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	/**
	 * @return the pathsList.
	 */
	public ArrayList<String> getPathsList() {
		return pathsList;
	}

	/**
	 * @param pathsList the pathsList to set.
	 */
	public void setPathsList(ArrayList<String> pathsList) {
		this.pathsList = pathsList;
	}

	/**
	 * Constructor : init the arraylist.
	 */
	public ImageLoader() {
		pathsList = new ArrayList<String>();
	}

}
