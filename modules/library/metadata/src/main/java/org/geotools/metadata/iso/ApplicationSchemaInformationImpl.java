/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso;

import java.net.URI;
import org.opengis.metadata.ApplicationSchemaInformation;
import org.opengis.metadata.citation.Citation;

/**
 * Information about the application schema used to build the dataset.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 * @since 2.1
 */
public class ApplicationSchemaInformationImpl extends MetadataEntity
        implements ApplicationSchemaInformation {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -3109191272905767382L;

    /** Name of the application schema used. */
    private Citation name;

    /** Identification of the schema language used. */
    private String schemaLanguage;

    /** Formal language used in Application Schema. */
    private String constraintLanguage;

    /** Full application schema given as an ASCII file. */
    private URI schemaAscii;

    /** Full application schema given as a graphics file. */
    private URI graphicsFile;

    /** Full application schema given as a software development file. */
    private URI softwareDevelopmentFile;

    /** Software dependent format used for the application schema software dependent file. */
    private String softwareDevelopmentFileFormat;

    /** Construct an initially empty application schema information. */
    public ApplicationSchemaInformationImpl() {}

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ApplicationSchemaInformationImpl(final ApplicationSchemaInformation source) {
        super(source);
    }

    /** Creates a application schema information initialized to the specified values. */
    public ApplicationSchemaInformationImpl(
            final Citation name, final String schemaLanguage, final String constraintLanguage) {
        setName(name);
        setSchemaLanguage(schemaLanguage);
        setConstraintLanguage(constraintLanguage);
    }

    /** Name of the application schema used. */
    public Citation getName() {
        return name;
    }

    /** Set the name of the application schema used. */
    public void setName(final Citation newValue) {
        checkWritePermission();
        name = newValue;
    }

    /** Identification of the schema language used. */
    public String getSchemaLanguage() {
        return schemaLanguage;
    }

    /** Set the identification of the schema language used. */
    public void setSchemaLanguage(final String newValue) {
        checkWritePermission();
        schemaLanguage = newValue;
    }

    /** Formal language used in Application Schema. */
    public String getConstraintLanguage() {
        return constraintLanguage;
    }

    /** Set the formal language used in Application Schema. */
    public void setConstraintLanguage(final String newValue) {
        checkWritePermission();
        constraintLanguage = newValue;
    }

    /** Full application schema given as an ASCII file. */
    public URI getSchemaAscii() {
        return schemaAscii;
    }

    /** Set the full application schema given as an ASCII file. */
    public void setSchemaAscii(final URI newValue) {
        checkWritePermission();
        schemaAscii = newValue;
    }

    /** Full application schema given as a graphics file. */
    public URI getGraphicsFile() {
        return graphicsFile;
    }

    /** Set the full application schema given as a graphics file. */
    public void setGraphicsFile(final URI newValue) {
        checkWritePermission();
        graphicsFile = newValue;
    }

    /** Full application schema given as a software development file. */
    public URI getSoftwareDevelopmentFile() {
        return softwareDevelopmentFile;
    }

    /** Set the full application schema given as a software development file. */
    public void setSoftwareDevelopmentFile(final URI newValue) {
        checkWritePermission();
        softwareDevelopmentFile = newValue;
    }

    /** Software dependent format used for the application schema software dependent file. */
    public String getSoftwareDevelopmentFileFormat() {
        return softwareDevelopmentFileFormat;
    }

    /**
     * Set the software dependent format used for the application schema software dependent file.
     */
    public void setSoftwareDevelopmentFileFormat(final String newValue) {
        checkWritePermission();
        softwareDevelopmentFileFormat = newValue;
    }
}
