package com.gcmrm.controllers;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.awt.Color;
import java.util.Base64;

import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;
import com.gcmrm.models.Point;

/**
 * Manages Image to .SVG Export
 */
public class ImageSVGExport implements ImageVisitor {
    private String result;

    /**
     * Constructor.
     */
    public ImageSVGExport() {
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
     * Visit the image and create the svg string to describe it.
     */
    @Override
    public void visit(Image image) throws Exception {
        int h = 0;
        int w = 0;
        String base64Image;

        // Original image, can be tiff, jpg or even png.
        BufferedImage imageOriginal = null;

        // Read the image file.
        try {
            imageOriginal = ImageIO.read(new File(image.getUrl()));
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Get dimension.
        h = imageOriginal.getHeight();
        w = imageOriginal.getWidth();

        // Create a new empty image.
        BufferedImage newBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);

        // Copy the original image inside.
        newBufferedImage.createGraphics().drawImage(imageOriginal, 0, 0, Color.white, null);

        // Create a output stream.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // write the copied image as jpg inside the stream. (always a jpg, so no error inside the svg)
        ImageIO.write(newBufferedImage, "jpg", baos);

        // Convert jpg inside the steeam into a byte array.
        final byte[] authBytes = baos.toByteArray();

        // Encode image as base64.
        base64Image = Base64.getEncoder().encodeToString(authBytes);

        // Create SVG/XML Document.
        DocumentBuilderFactory XMLfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder XMLbuilder;

        // create the document.
        XMLbuilder = XMLfactory.newDocumentBuilder();
        Document document = XMLbuilder.newDocument();

        // Create the root svg element.
        Element svg = document.createElement("svg");
        svg.setAttribute("width", "" + w);
        svg.setAttribute("height", "" + h);
        svg.setAttribute("viewBox", "0 0 " + w + " " + h);
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        svg.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        document.appendChild(svg);

        // Create the title element.
        Element title = document.createElement("title");
        title.setTextContent("Titre du svg");
        svg.appendChild(title);

        // Create the background layer.
        Element lastLayer = document.createElement("g");
        svg.appendChild(lastLayer);

        // Create image object.
        Element imageObject = document.createElement("image");
        imageObject.setAttribute("xlink:href", "data:image/jpg;base64," + base64Image);
        imageObject.setAttribute("height", "" + h);
        imageObject.setAttribute("width", "" + w);
        lastLayer.appendChild(imageObject);

        // Create layer for each measurment.
        for (Measurement measurement : image.getMeasurements()) 
        { 
            Element layer = document.createElement("g");
            svg.appendChild(layer);

            // Create each point.
            for (Point point : measurement.getPoints()) 
            { 
                // Create a cross group.
                Element cross = document.createElement("g");

                // First line.
                Element firstLine = document.createElement("line");
                firstLine.setAttribute("x1", "-5");
                firstLine.setAttribute("x2", "5");
                firstLine.setAttribute("y1", "-5");
                firstLine.setAttribute("y2", "5");
                if(point.getQuantity() > 1) {
                    firstLine.setAttribute("stroke", "yellow");
                } else {
                    firstLine.setAttribute("stroke", "red");
                }
                firstLine.setAttribute("stroke-width", "2");

                // Second line
                Element secondLine = document.createElement("line");
                secondLine.setAttribute("x1", "-5");
                secondLine.setAttribute("x2", "5");
                secondLine.setAttribute("y1", "5");
                secondLine.setAttribute("y2", "-5");
                if(point.getQuantity() > 1) {
                    secondLine.setAttribute("stroke", "yellow");
                } else {
                    secondLine.setAttribute("stroke", "red");
                }
                secondLine.setAttribute("stroke-width", "2");

                // Node for quantity.
                if(point.getQuantity() > 1) {
                    Element quantity = document.createElement("text");
                    quantity.setTextContent("" + (int)point.getQuantity());
                    quantity.setAttribute("x", "-5");
                    quantity.setAttribute("y", "20");
                    quantity.setAttribute("fill", "white");

                    cross.appendChild(quantity);
                }

                cross.appendChild(firstLine);
                cross.appendChild(secondLine);

                // Move the cross to the right place.
                cross.setAttribute("transform", "translate(" + point.getX() + " " + point.getY()  + ")");

                layer.appendChild(cross);
            }
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans;
        
        trans = tf.newTransformer();
            
        StringWriter sw = new StringWriter();
        
        trans.transform(new DOMSource(document), new StreamResult(sw));

        result = sw.toString();

    }

}
