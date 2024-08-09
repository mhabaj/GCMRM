package com.gcmrm.controllers;

import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;

/**
 * Manages Image to Image + .Txt Export.
 */
public class ImageTxtExport implements ImageVisitor {
    private String result;

    /**
     * Constructor.
     */
    public ImageTxtExport() {
        result = "";
    }

    /**
     * Get the result of the visit.
     * @return String
     */
    public String getResult() {
        return result;
    }

    /**
     * Visit the image and create the txt string to describe it.
     */
    @Override
    public void visit(Image image) {
        result = "Image : ";
        result += image.getUrl() + "\n";
        int i = 0;
        for (Measurement measurement : image.getMeasurements()) 
        { 
            result += "mesure " + i + " : ";
            result += measurement.getPoints().size() + "\n";
            i++;
        }
    }
}
