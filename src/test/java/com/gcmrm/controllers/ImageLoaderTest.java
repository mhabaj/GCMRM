package com.gcmrm.controllers;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testing image laoder class : load image from the path.
 */
public class ImageLoaderTest {

	/**
	 * Case: bad File/Folder INPUT PATH
	 */
	@Test
	public void imageLoaderBadParamTest() {

		ImageLoader il = new ImageLoader();
		try {
			il.loadImages("");

		} catch (Exception e) {
			return;
		}

	}
	
	
	/**
	 * Case: Good Import Path with existing folder.
	 */
	@Test
	public void imageLoaderGoodParamTest() {

		ImageLoader il = new ImageLoader();
		try {
			il.loadImages("src/test/resources/com/gcmrm/testImport/");
			if(il.getPathsList().size() == 1) {
				return;
			}

		} catch (Exception e) {
			fail("Couldn't import testImport Folder");
		}

	}
	
	

}