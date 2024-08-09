package com.gcmrm.controllers;

import com.gcmrm.models.Image;

/**
 * Manages Images export Types
 */
public interface ImageVisitor {

   
    /**
     * Visit the image, to be overriden.
     * @param monitor Image object to handle
     * @throws Exception Visit Exception
     */
    public void visit(Image monitor) throws Exception;
}
