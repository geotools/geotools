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
package org.geotools.metadata.iso.content;

import java.util.Collection;
import org.opengis.metadata.content.CoverageContentType;
import org.opengis.metadata.content.CoverageDescription;
import org.opengis.metadata.content.RangeDimension;
import org.opengis.util.RecordType;


/**
 * Information about the content of a grid data cell.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 */
public class CoverageDescriptionImpl extends ContentInformationImpl implements CoverageDescription {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -326050615789333559L;;

    /**
     * Description of the attribute described by the measurement value.
     */
    private RecordType attributeDescription;

    /**
     * Type of information represented by the cell value.
     */
    private CoverageContentType contentType;

    /**
     * Information on the dimensions of the cell measurement value.
     */
    private Collection<RangeDimension> dimensions;

    /**
     * Constructs an empty coverage description.
     */
    public CoverageDescriptionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public CoverageDescriptionImpl(final CoverageDescription source) {
        super(source);
    }

    /**
     * Returns the description of the attribute described by the measurement value.
     */
    public RecordType getAttributeDescription() {
        return attributeDescription;
    }

    /**
     * Set the description of the attribute described by the measurement value.
     */
    public synchronized void setAttributeDescription(final RecordType newValue) {
        checkWritePermission();
        attributeDescription = newValue;
    }

    /**
     * Returns the type of information represented by the cell value.
     */
    public CoverageContentType getContentType() {
        return contentType;
    }

    /**
     * Set the type of information represented by the cell value.
     */
    public synchronized void setContentType(final CoverageContentType newValue) {
        checkWritePermission();
        contentType = newValue;
    }

    /**
     * Returns the information on the dimensions of the cell measurement value.
     *
     * @since 2.4
     */
    public synchronized Collection<RangeDimension> getDimensions() {
        return (dimensions = nonNullCollection(dimensions, RangeDimension.class));
    }

    /**
     * Set the information on the dimensions of the cell measurement value.
     *
     * since 2.4
     */
    public synchronized void setDimensions(final Collection<? extends RangeDimension> newValues) {
        dimensions = copyCollection(newValues, dimensions, RangeDimension.class);
    }
}
