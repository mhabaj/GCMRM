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
import java.io.StringWriter;
import java.awt.Color;
import java.util.Base64;

import com.gcmrm.models.Image;
import com.gcmrm.models.Measurement;
import com.gcmrm.models.Point;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

/**
 * Manages Image to .html Export, used mostly for interactive/display purpose.
 * to add css and js, use str.replace on %CSS% and %JS% with the link of the files.
 */
public class ImageHTMLExport implements ImageVisitor {
    private String result;

    /**
     * Constructor.
     */
    public ImageHTMLExport() {
        result = "";
    }

    /**
     * @return String Get the result of the visit.
     */
    public String getResult() {
        return result;
    }

    /**
     * Visit the image and create the html string to describe it.
     */
    @Override
    public void visit(Image image) throws Exception {
        int h = 0;
        int w = 0;
        String base64Image;
        DocumentType doctype;

        // Original image, can be tiff, jpg or even png.
        BufferedImage imageOriginal = null;

        // Read the image file.
        imageOriginal = ImageIO.read(new File(image.getUrl()));

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
        
        // set the doctype
        DOMImplementation domImpl = document.getImplementation();
        doctype = domImpl.createDocumentType("html", "-//W3C//DTD XHTML 1.1//EN", "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd");

        // create the html root element
        Element root = document.createElement("html");
        root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        document.appendChild(root);
        
        // create the head root element
        Element head = document.createElement("head");
        root.appendChild(head);
        
        // create the body root element
        Element body = document.createElement("body");
        root.appendChild(body);
        
        // Create the root svg element.
        Element svg = document.createElement("svg");
        svg.setAttribute("id", "svg");
        svg.setAttribute("viewBox", "0 0 " + w + " " + h);
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        body.appendChild(svg);

        // Create the title element.
        Element title = document.createElement("title");
        title.setTextContent("GCMRM image");
        head.appendChild(title);
        
        // Create css and js.
        Element css = document.createElement("style");
        css.setTextContent("{%CSS%}");
        head.appendChild(css);
        Element js = document.createElement("script");
        js.setAttribute("type", "application/javascript");
        js.setTextContent("{%JS%}");
        head.appendChild(js);
        
        // Create the magic meta element.
        Element meta = document.createElement("meta");
        meta.setAttribute("name", "viewport");
        meta.setAttribute("content", "width=device-width, initial-scale=1.0");
        head.appendChild(meta);

        // Create the background layer.
        Element lastLayer = document.createElement("g");
        svg.appendChild(lastLayer);

        // Create image object.
        Element imageObject = document.createElement("image");
        imageObject.setAttribute("href", "data:image/jpg;base64," + base64Image);
        imageObject.setAttribute("height", "" + h);
        imageObject.setAttribute("width", "" + w);
        lastLayer.appendChild(imageObject);

        // Create layer for each measurment.
        int i = 0;
        for (Measurement measurement : image.getMeasurements()) 
        { 
            Element layer = document.createElement("g");
            layer.setAttribute("class", "measurements");
            layer.setAttribute("id", "measurement-" + i);
            svg.appendChild(layer);

            // Create each point.
            for (Point point : measurement.getPoints()) 
            { 
                // Create a cross group.
                Element cross = document.createElement("g");
                cross.setAttribute("class", "points");
                cross.setAttribute("id", "point-" + point.getId());
                cross.setAttribute("data-quantity", "" + point.getQuantity());

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
            i++;
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans;
        
        trans = tf.newTransformer();
            
        StringWriter sw = new StringWriter();
        
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.setOutputProperty(OutputKeys.METHOD, "xml");
        trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        
        trans.transform(new DOMSource(document), new StreamResult(sw));

        result = sw.toString();

    }

}
