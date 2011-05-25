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
package org.geotools.metadata.iso.spatial;

import org.opengis.metadata.spatial.GeometricObjects;
import org.opengis.metadata.spatial.GeometricObjectType;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Number of objects, listed by geometric object type, used in the dataset.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class GeometricObjectsImpl extends MetadataEntity implements GeometricObjects {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8755950031078638313L;

    /**
     * Total number of the point or vector object type occurring in the dataset.
     */
    private GeometricObjectType geometricObjectType;

    /**
     * Total number of the point or vector object type occurring in the dataset.
     */
    private Integer geometricObjectCount;

    /**
     * Constructs an initially empty geometric objects.
     */
    public GeometricObjectsImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public GeometricObjectsImpl(final GeometricObjects source) {
        super(source);
    }

    /**
     * Creates a geometric object initialized to the given type.
     */
    public GeometricObjectsImpl(final GeometricObjectType geometricObjectType) {
        setGeometricObjectType(geometricObjectType);
    }

    /**
     * Total number of the point or vector object type occurring in the dataset.
     */
    public GeometricObjectType getGeometricObjectType() {
        return geometricObjectType;
    }

    /**
     * Set the total number of the point or vector object type occurring in the dataset.
     */
    public synchronized void setGeometricObjectType(final GeometricObjectType newValue) {
        checkWritePermission();
        geometricObjectType = newValue;
    }

    /**
     * Total number of the point or vector object type occurring in the dataset.
     */
    public Integer getGeometricObjectCount() {
        return geometricObjectCount;
    }

    /**
     * Set the total number of the point or vector object type occurring in the dataset.
     */
    public synchronized void setGeometricObjectCount(final Integer newValue) {
        checkWritePermission();
        geometricObjectCount = newValue;
    }
}
