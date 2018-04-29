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
 * The type of a Feature.
 *
 * <p>Beyond a complex type, a feature defines some additional information:
 *
 * <ul>
 *   <li>The default geometric attribute
 *   <li>The coordinate referencing system (derived from the default geometry)
 * </ul>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 * @source $URL$
 */
public interface FeatureType extends ComplexType {

    /**
     * Features are always identified.
     *
     * @return <code>true</code>
     */
    boolean isIdentified();

    /**
     * Describe the default geometric attribute for this feature.
     *
     * <p>This method returns <code>null</code> in the case where no such attribute exists.
     *
     * @return The descriptor of the default geometry attribute, or <code>null</code>.
     */
    GeometryDescriptor getGeometryDescriptor();

    /**
     * The coordinate reference system of the feature.
     *
     * <p>This value is derived from the default geometry attribute:
     *
     * <pre>
     *   ((GeometryType)getDefaultGeometry().getType()).getCRS();
     * </pre>
     *
     * <p>This method will return <code>null</code> in the case where no default geometric attribute
     * is defined.
     *
     * @return The coordinate referencing system, or <code>null</code>.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();
}
