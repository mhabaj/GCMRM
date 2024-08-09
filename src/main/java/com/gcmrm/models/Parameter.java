package com.gcmrm.models;

import java.io.Serializable;

/**
 * Model class: Parameter contains algorithm parameters.
 */
public class Parameter implements Serializable {

	private static final long serialVersionUID = 3521760501418264693L;
	
	/**
	 * String name
	 */
	private String name;
    /**
     * String type. Must be Serializable.
     */
    private String type;
    /**
     * String value
     */
    private String value;
    
    /**
     * String description
     */
    private String description;
    
    /**
     * Constructor.
     */
    public Parameter() {
    }
    
    /**
     * @param name String
     * @param type String
     * @param value String
     * @param description String
     */
    public Parameter(String name, String type, String value, String description) {
        this.description = description;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}