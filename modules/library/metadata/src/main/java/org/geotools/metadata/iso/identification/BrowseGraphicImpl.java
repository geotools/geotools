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
package org.geotools.metadata.iso.identification;

import java.net.URI;
import org.opengis.metadata.identification.BrowseGraphic;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Graphic that provides an illustration of the dataset (should include a legend for the graphic).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class BrowseGraphicImpl extends MetadataEntity implements BrowseGraphic {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 1715873406472953616L;

    /**
     * Name of the file that contains a graphic that provides an illustration of the dataset.
     */
    private URI applicationSchemaInformation;

    /**
     * Name of the file that contains a graphic that provides an illustration of the dataset.
     */
    private URI fileName;

    /**
     * Text description of the illustration.
     */
    private InternationalString fileDescription;

    /**
     * Format in which the illustration is encoded.
     * Examples: CGM, EPS, GIF, JPEG, PBM, PS, TIFF, XWD.
     */
    private String fileType;

    /**
     * Constructs an initially empty browse graphic.
     */
    public BrowseGraphicImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public BrowseGraphicImpl(final BrowseGraphic source) {
        super(source);
    }

    /**
     * Creates a browse graphics initialized to the specified URI.
     */
    public BrowseGraphicImpl(final URI fileName) {
        setFileName(fileName);
    }

    /**
     * Name of the file that contains a graphic that provides an illustration of the dataset.
     */
    public URI getApplicationSchemaInformation() {
        return applicationSchemaInformation;
    }

    /**
     * Set the name of the file that contains a graphic that provides an illustration of the
     * dataset.
     */
    public synchronized void setApplicationSchemaInformation(final URI newValue) {
        checkWritePermission();
        applicationSchemaInformation = newValue;
    }

    /**
     * Name of the file that contains a graphic that provides an illustration of the dataset.
     */
    public URI getFileName() {
        return fileName;
    }

    /**
     * Set the name of the file that contains a graphic that provides an illustration of the
     * dataset.
     */
    public synchronized void setFileName(final URI newValue) {
        checkWritePermission();
        fileName = newValue;
    }

    /**
     * Text description of the illustration.
     */
    public InternationalString getFileDescription() {
        return fileDescription;
    }

    /**
     * Set the text description of the illustration.
     */
    public synchronized void setFileDescription(final InternationalString newValue)  {
        checkWritePermission();
        fileDescription = newValue;
    }

    /**
     * Format in which the illustration is encoded.
     * Examples: CGM, EPS, GIF, JPEG, PBM, PS, TIFF, XWD.
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Set the format in which the illustration is encoded.
     */
    public synchronized void setFileType(final String newValue)  {
        checkWritePermission();
        fileType = newValue;
    }
}
