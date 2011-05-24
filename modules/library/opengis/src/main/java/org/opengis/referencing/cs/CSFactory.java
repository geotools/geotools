/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.cs;

import java.util.Map;
import javax.measure.unit.Unit;
import org.opengis.referencing.ObjectFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.annotation.Extension;


/**
 * Builds up complex {@linkplain CoordinateSystem coordinate systems} from simpler
 * objects or values. {@code CSFactory} allows applications to make {@linkplain
 * CoordinateSystem coordinate systems} that cannot be created by a {@link CSAuthorityFactory}.
 * This factory is very flexible, whereas the authority factory is easier to use.
 *
 * So {@link CSAuthorityFactory} can be used to make "standard" coordinate systems,
 * and {@code CSFactory} can be used to make "special" coordinate systems.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/cs/CSFactory.java $
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.crs.CRSFactory
 * @see org.opengis.referencing.datum.DatumFactory
 */
@Extension
public interface CSFactory extends ObjectFactory {
    /**
     * Creates a coordinate system axis from an abbreviation and a unit.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  abbreviation The coordinate axis abbreviation.
     * @param  direction The axis direction.
     * @param  unit The coordinate axis unit.
     * @return The axis for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    CoordinateSystemAxis createCoordinateSystemAxis(Map<String,?> properties,
                                                    String        abbreviation,
                                                    AxisDirection direction,
                                                    Unit<?>       unit) throws FactoryException;

    /**
     * Creates a two dimensional cartesian coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    CartesianCS createCartesianCS(Map<String, ?>  properties,
                                  CoordinateSystemAxis axis0,
                                  CoordinateSystemAxis axis1) throws FactoryException;

    /**
     * Creates a three dimensional cartesian coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    CartesianCS createCartesianCS(Map<String, ?>  properties,
                                  CoordinateSystemAxis axis0,
                                  CoordinateSystemAxis axis1,
                                  CoordinateSystemAxis axis2) throws FactoryException;

    /**
     * Creates a two dimensional coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    AffineCS createAffineCS(Map<String, ?>  properties,
                            CoordinateSystemAxis axis0,
                            CoordinateSystemAxis axis1) throws FactoryException;

    /**
     * Creates a three dimensional coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    AffineCS createAffineCS(Map<String, ?>  properties,
                            CoordinateSystemAxis axis0,
                            CoordinateSystemAxis axis1,
                            CoordinateSystemAxis axis2) throws FactoryException;

    /**
     * Creates a polar coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    PolarCS createPolarCS(Map<String, ?>  properties,
                          CoordinateSystemAxis axis0,
                          CoordinateSystemAxis axis1) throws FactoryException;

    /**
     * Creates a cylindrical coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    CylindricalCS createCylindricalCS(Map<String, ?>  properties,
                                      CoordinateSystemAxis axis0,
                                      CoordinateSystemAxis axis1,
                                      CoordinateSystemAxis axis2) throws FactoryException;

    /**
     * Creates a spherical coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    SphericalCS createSphericalCS(Map<String, ?>  properties,
                                  CoordinateSystemAxis axis0,
                                  CoordinateSystemAxis axis1,
                                  CoordinateSystemAxis axis2) throws FactoryException;

    /**
     * Creates an ellipsoidal coordinate system without ellipsoidal height.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    EllipsoidalCS createEllipsoidalCS(Map<String, ?>  properties,
                                      CoordinateSystemAxis axis0,
                                      CoordinateSystemAxis axis1) throws FactoryException;

    /**
     * Creates an ellipsoidal coordinate system with ellipsoidal height.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    EllipsoidalCS createEllipsoidalCS(Map<String, ?>  properties,
                                      CoordinateSystemAxis axis0,
                                      CoordinateSystemAxis axis1,
                                      CoordinateSystemAxis axis2) throws FactoryException;

    /**
     * Creates a vertical coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis The axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    VerticalCS createVerticalCS(Map<String, ?> properties,
                                CoordinateSystemAxis axis) throws FactoryException;

    /**
     * Creates a time coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis The axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    TimeCS createTimeCS(Map<String, ?> properties,
                        CoordinateSystemAxis axis) throws FactoryException;

    /**
     * Creates a linear coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis The axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    LinearCS createLinearCS(Map<String, ?> properties,
                            CoordinateSystemAxis axis) throws FactoryException;

    /**
     * Creates a two-dimensional user defined coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    UserDefinedCS createUserDefinedCS(Map<String, ?>  properties,
                                      CoordinateSystemAxis axis0,
                                      CoordinateSystemAxis axis1) throws FactoryException;

    /**
     * Creates a three-dimensional user defined coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @return The coordinate system for the given properties and axes.
     * @throws FactoryException if the object creation failed.
     */
    UserDefinedCS createUserDefinedCS(Map<String, ?>  properties,
                                      CoordinateSystemAxis axis0,
                                      CoordinateSystemAxis axis1,
                                      CoordinateSystemAxis axis2) throws FactoryException;
}
