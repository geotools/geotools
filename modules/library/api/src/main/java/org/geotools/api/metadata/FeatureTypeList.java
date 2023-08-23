/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata;

/**
 * List of names of feature types with the same spatial representation (same as spatial attributes).
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface FeatureTypeList {
    /**
     * Instance of a type defined in the spatial schema.
     *
     * @return Instance of a type defined in the spatial schema.
     */
    String getSpatialObject();

    /**
     * Name of the spatial schema used.
     *
     * @return Name of the spatial schema used.
     */
    String getSpatialSchemaName();
}
