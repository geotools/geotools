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
package org.geotools.xsd.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.EntityResolver2;

/**
 * Handler for validating a document.
 *
 * <p>This class is used by the parser to validate while parsing.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class ValidatorHandler extends DefaultHandler2 {

    /** flag to control if an exception is thrown on a validation error */
    boolean failOnValidationError = false;

    /** entity resolver */
    EntityResolver2 entityResolver;

    /** list of validation errors */
    List<Exception> errors;

    public void setFailOnValidationError(boolean failOnValidationError) {
        this.failOnValidationError = failOnValidationError;
    }

    public boolean isFailOnValidationError() {
        return failOnValidationError;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = (EntityResolver2) entityResolver;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws IOException, SAXException {
        if (entityResolver != null) {
            return entityResolver.resolveEntity(publicId, systemId);
        } else {
            return super.resolveEntity(publicId, systemId);
        }
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        if (entityResolver != null) {
            return entityResolver.resolveEntity(name, publicId, baseURI, systemId);
        }
        return super.resolveEntity(name, publicId, baseURI, systemId);
    }

    @Override
    public void startDocument() throws SAXException {
        errors = new ArrayList();
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        // check fail on validation flag
        if (isFailOnValidationError()) {
            throw e;
        }

        errors.add(e);
    }

    public List<Exception> getErrors() {
        return errors;
    }
}
