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
package org.geotools.feature;

import com.vividsolutions.jts.geom.GeometryFactory;

//import org.geotools.cs.CoordinateSystem;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A CoordinateSystem aware Geometry AttributeType.
 * <p>
 * This class is the bridge between our FeatureType/AttributeType
 * classes and the CoordianteSystem.
 * </p>
 * <p>
 * This also allows access to the GeometryFactory used by this
 * GeometryAttributeType parse( Object ) method.
 * </p>
 * <p>
 * With JTS14 you can use GeometryFactory to to provide your own
 * CoordianteSequence representation. CoordinateSystem is given the
 * responsiblity of providing this class for the GeometryAttributeType as
 * only it knows the CoordianteSequence class and PercisionModel mosted
 * suitable. It also may know an SRID number suitable for the
 * GeometryFactory to use when constructing new Geometry objects.
 * </p>
 * It is recomended that the CoordinateSystem GeometryFactory also
 * supply the CoordinateSystem as the value for Geometry.getUserData().
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @source $URL$
 */
public interface GeometryAttributeType extends AttributeType, PrimativeAttributeType,
    GeometryDescriptor {
    /**
     * Type must be an sub type of Geometry.class.
     * <p>
     * If this was java I would say Class<X extends Geometry>.
     * Warning this will need revisiting for Geotools 2.2 when
     * GeoAPI Geometry enters use.
     * </p>
     * @see org.geotools.feature.AttributeType#getBinding()
     */
    Class getBinding();

    /**
     * Restriction is assumed to be in agreement with Geometry class indicated.
     *
     * @see org.geotools.feature.AttributeType#getRestriction()
     */
    Filter getRestriction();

    /**
     * Retrieve the CS_CoordinateSystem used by this GeometryAttributeType.
     * <p>
     * OUT OF DATE: The class CoordinateSystem holds a GeometryFactory
     * that is used for creating new content. By extension this includes the SRID,
     * PercisionModel and CoordinateSequenceFactory information.
     * </p>
     *
     * @return The coordinate reference system for this GeometryAttributeType
     */
    public CoordinateReferenceSystem getCoordinateSystem();

    /**
     * The Geometryfactory used for creating new content.
     * <p>
     * Replace with the following code:<pre><code>
     * Map hints = new HashMap();
     * hints.put( CoordinateReferneceSystem.class, type.getCoordinateSystem() );
     * GeometryFactory gf = FactoryFinder.getGeometryFactory( Map hints );
     *
     * // You can now use gf create methods
     * </code></pre>
     *
     * @return GeometryFactory used for new Content
     * @deprecated Please use GeometrFactory associated with your FeatureFactory
     * using the hinting system.
     */
    public GeometryFactory getGeometryFactory();

    /**
     * Must return <code>true</code>
     * @deprecated replace with type instnaceof GeometryAttribtueType
     * @see org.geotools.feature.AttributeType#isGeometry()
     */
    boolean isGeometry();
}
