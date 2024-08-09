package com.gcmrm.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;
import com.gcmrm.models.Point;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Test some of the visitors
 */
public class VisitorTest {
    /**
     * Image url for the test.
     */
	public static final String IMAGE_URL = "./src/test/resources/com/gcmrm/pictures/cphoto1.jpg";

    /**
     * Rule for expected exception if no image url is given.
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    /**
	 * Case: ImageTxtExport
	 */
	@Test
	public void imageTxtExport() {
        Image a = new Image();
        Image b = new Image();
        Image c = new Image();
        a.setId(0);
        a.setUrl(IMAGE_URL);
        a.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));

        b.setId(0);
        b.setUrl(IMAGE_URL);
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().get(0).getPoints().add(new Point(0, 50, 50, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(1, 100, 100, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(2, 150, 150, 3));

        c.setId(0);
        c.setUrl("");

        ImageTxtExport ite = new ImageTxtExport();
        ite.visit(a);

        System.out.println(ite.getResult());

        assertNotNull("result must be not null", ite.getResult());

        assertEquals("result must be valid", ite.getResult(), "Image : ./src/test/resources/com/gcmrm/pictures/cphoto1.jpg\nmesure 0 : 0\n");

        ImageTxtExport ite2 = new ImageTxtExport();
        ite2.visit(b);

        System.out.println(ite2.getResult());

        assertNotNull("result must be not null", ite2.getResult());

        assertEquals("result must be valid", ite2.getResult(), "Image : ./src/test/resources/com/gcmrm/pictures/cphoto1.jpg\nmesure 0 : 3\nmesure 1 : 0\nmesure 2 : 0\n");

        ImageTxtExport ite3 = new ImageTxtExport();
        ite3.visit(c);

        System.out.println(ite3.getResult());

        assertNotNull("result must be not null", ite3.getResult());

        assertEquals("result must be valid", ite3.getResult(), "Image : \n");
	}

    /**
	 * Case: ImageTxtExport
     * @throws Exception
	 */
	@Test
	public void imageSvgExport() throws Exception {
        Image a = new Image();
        Image b = new Image();
        Image c = new Image();
        a.setId(0);
        a.setUrl(IMAGE_URL);
        a.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));

        b.setId(0);
        b.setUrl(IMAGE_URL);
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));

        b.getMeasurements().get(0).getPoints().add(new Point(0, 50, 50, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(1, 100, 100, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(2, 150, 150, 3));

        c.setId(0);
        c.setUrl("");

        ImageSVGExport ite = new ImageSVGExport();

        ite.visit(a);

        System.out.println(ite.getResult());

        assertNotNull("result must be not null", ite.getResult());

        // We can't test automatically the result, so you need to look at the generated svg in the app.

        ImageSVGExport ite2 = new ImageSVGExport();

        ite2.visit(b);

        System.out.println(ite2.getResult());

        assertNotNull("result must be not null", ite2.getResult());

        // We can't test automatically the result, so you need to look at the generated svg in the app.

        ImageSVGExport ite3 = new ImageSVGExport();
        // Except an exception when the image url is invalid
        exception.expect(Exception.class);

        ite3.visit(c);

	}
	
	 /**
	 * Case: CSVexport
	 * @throws IOException can't write the file
	 * @throws FileNotFoundException 
	 */
	@Test
	public void imageOdsExport() throws FileNotFoundException, IOException {
		
		Image a = new Image();
        Image b = new Image();
        Image c = new Image();
        a.setId(0);
        a.setUrl("imageA");
        a.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));

        b.setId(0);
        b.setUrl("imageB");
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        b.getMeasurements().get(0).getPoints().add(new Point(0, 50, 50, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(1, 100, 100, 1));
        b.getMeasurements().get(0).getPoints().add(new Point(2, 150, 150, 3));
        
        c.setId(0);
        c.setUrl("imageC");
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().add(new Measurement(new ArrayList<Point>(), false, true, LocalDateTime.of(2015, 7, 12, 15, 23, 11).toString()));
        c.getMeasurements().get(0).getPoints().add(new Point(0, 50, 50, 1));
        c.getMeasurements().get(0).getPoints().add(new Point(1, 100, 100, 1));
        c.getMeasurements().get(0).getPoints().add(new Point(2, 150, 150, 3));
        c.getMeasurements().get(0).getPoints().add(new Point(0, 50, 50, 1));
        c.getMeasurements().get(0).getPoints().add(new Point(1, 100, 100, 1));
        c.getMeasurements().get(0).getPoints().add(new Point(2, 150, 150, 3));
        
        ArrayList<Image> images= new ArrayList<Image>();
        images.add(a);
        images.add(b);
        images.add(c);
        
        CampaignOdsExport export= new CampaignOdsExport();
        
        export.visit(images, "./src/test/resources/com/gcmrm/pictures/testCsv.csv");
        
        
        
        
        
        

	}
}