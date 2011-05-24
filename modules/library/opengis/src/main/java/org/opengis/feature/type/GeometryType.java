/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The type of a GeometryAttribute.
 * <p>
 * Beyond AttributeType, this class stores the coordinate reference system that
 * geometries are defined in, see {@link #getCoordinateReferenceSystem()}.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/type/GeometryType.java $
 */
public interface GeometryType extends AttributeType {
    /**
     * The coordinate reference system in which geometries are defined.
     * <p>
     * This method may return <code>null</code>, but this should only occur in
     * cases where the actual crs is not known. A common case is when a shapefile
     * does not have an accompanied .prj file.
     * </p>
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();
}
