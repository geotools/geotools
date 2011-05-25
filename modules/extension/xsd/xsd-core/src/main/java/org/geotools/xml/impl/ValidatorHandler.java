/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml.impl;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for validating a document.
 * <p>
 * This class is used by the parser to validate while parsing.
 *  </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class ValidatorHandler extends DefaultHandler {
    
    /** flag to control if an exception is thrown on a validation error */
    boolean failOnValidationError = false;
    
    /** list of validation errors */
    List<Exception> errors;

    public void setFailOnValidationError( boolean failOnValidationError ) {
        this.failOnValidationError = failOnValidationError;
    }
    
    public boolean isFailOnValidationError() {
        return failOnValidationError;
    }
    
    @Override
    public void startDocument() throws SAXException {
        errors = new ArrayList();
    }
    
    @Override
    public void error(SAXParseException e) throws SAXException {
        //check fail on validation flag
        if ( isFailOnValidationError() ) {
            throw e;
        }
        
        errors.add( e );
    }

    public List<Exception> getErrors() {
        return errors;
    }
}
