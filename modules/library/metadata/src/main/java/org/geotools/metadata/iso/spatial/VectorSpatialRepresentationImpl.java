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

import java.util.Collection;
import org.opengis.metadata.spatial.GeometricObjects;
import org.opengis.metadata.spatial.TopologyLevel;
import org.opengis.metadata.spatial.VectorSpatialRepresentation;


/**
 * Information about the vector spatial objects in the dataset.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class VectorSpatialRepresentationImpl extends SpatialRepresentationImpl
        implements VectorSpatialRepresentation
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5643234643524810592L;

    /**
     * Code which identifies the degree of complexity of the spatial relationships.
    */
    private TopologyLevel topologyLevel;

    /**
     * Information about the geometric objects used in the dataset.
     */
    private Collection<GeometricObjects> geometricObjects;

    /**
     * Constructs an initially empty vector spatial representation.
     */
    public VectorSpatialRepresentationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public VectorSpatialRepresentationImpl(final VectorSpatialRepresentation source) {
        super(source);
    }

    /**
     * Code which identifies the degree of complexity of the spatial relationships.
     */
    public TopologyLevel getTopologyLevel() {
        return topologyLevel;
    }

    /**
     * Set the code which identifies the degree of complexity of the spatial relationships.
     */
    public synchronized void setTopologyLevel(final TopologyLevel newValue) {
        checkWritePermission();
        topologyLevel = newValue;
    }

    /**
     * Information about the geometric objects used in the dataset.
     */
    public synchronized Collection<GeometricObjects> getGeometricObjects() {
        return (geometricObjects = nonNullCollection(geometricObjects, GeometricObjects.class));
    }

    /**
     * Set information about the geometric objects used in the dataset.
     */
    public synchronized void setGeometricObjects(
            final Collection<? extends GeometricObjects> newValues)
    {
        geometricObjects = copyCollection(newValues, geometricObjects, GeometricObjects.class);
    }
}
