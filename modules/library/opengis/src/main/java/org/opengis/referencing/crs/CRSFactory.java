/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.crs;

import java.util.Map;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.referencing.ObjectFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;


/**
 * Builds up complex {@linkplain CoordinateReferenceSystem coordinate reference systems}
 * from simpler objects or values. {@code CRSFactory} allows applications to make
 * {@linkplain CoordinateReferenceSystem coordinate reference systems} that cannot be
 * created by a {@link CRSAuthorityFactory}. This factory is very flexible, whereas the
 * authority factory is easier to use.
 *
 * So {@link CRSAuthorityFactory} can be used to make "standard" coordinate reference systems,
 * and {@code CRSFactory} can be used to make "special" coordinate reference systems.
 *
 * For example, the EPSG authority has codes for USA state plane coordinate systems
 * using the NAD83 datum, but these coordinate systems always use meters.  EPSG does
 * not have codes for NAD83 state plane coordinate systems that use feet units.  This
 * factory lets an application create such a hybrid coordinate system.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/crs/CRSFactory.java $
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.cs.CSFactory
 * @see org.opengis.referencing.datum.DatumFactory
 */
@UML(identifier="CS_CoordinateSystemFactory", specification=OGC_01009)
public interface CRSFactory extends ObjectFactory {
    /**
     * Creates a compound coordinate reference system from an ordered
     * list of {@code CoordinateReferenceSystem} objects.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  elements ordered array of {@code CoordinateReferenceSystem} objects.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createCompoundCoordinateSystem", specification=OGC_01009)
    CompoundCRS createCompoundCRS(Map<String, ?>              properties,
                                  CoordinateReferenceSystem[] elements) throws FactoryException;

    /**
     * Creates a engineering coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Engineering datum to use in created CRS.
     * @param  cs The coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createLocalCoordinateSystem", specification=OGC_01009)
    EngineeringCRS createEngineeringCRS(Map<String, ?>   properties,
                                        EngineeringDatum datum,
                                        CoordinateSystem cs) throws FactoryException;

    /**
     * Creates an image coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Image datum to use in created CRS.
     * @param  cs The Cartesian or Oblique Cartesian coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    ImageCRS createImageCRS(Map<String, ?> properties,
                            ImageDatum     datum,
                            AffineCS       cs) throws FactoryException;

    /**
     * Creates a temporal coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Temporal datum to use in created CRS.
     * @param  cs The Temporal coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    TemporalCRS createTemporalCRS(Map<String, ?> properties,
                                  TemporalDatum  datum,
                                  TimeCS         cs) throws FactoryException;

    /**
     * Creates a vertical coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Vertical datum to use in created CRS.
     * @param  cs The Vertical coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createVerticalCoordinateSystem", specification=OGC_01009)
    VerticalCRS createVerticalCRS(Map<String, ?> properties,
                                  VerticalDatum  datum,
                                  VerticalCS     cs) throws FactoryException;

    /**
     * Creates a geocentric coordinate reference system from a {@linkplain CartesianCS
     * cartesian coordinate system}.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The cartesian coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    GeocentricCRS createGeocentricCRS(Map<String, ?> properties,
                                      GeodeticDatum  datum,
                                      CartesianCS    cs) throws FactoryException;

    /**
     * Creates a geocentric coordinate reference system from a {@linkplain SphericalCS
     * spherical coordinate system}.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The spherical coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    GeocentricCRS createGeocentricCRS(Map<String, ?> properties,
                                      GeodeticDatum  datum,
                                      SphericalCS    cs) throws FactoryException;

    /**
     * Creates a geographic coordinate reference system.
     * It could be <var>Latitude</var>/<var>Longitude</var> or
     * <var>Longitude</var>/<var>Latitude</var>.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The ellipsoidal coordinate system for the created CRS.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createGeographicCoordinateSystem", specification=OGC_01009)
    GeographicCRS createGeographicCRS(Map<String, ?> properties,
                                      GeodeticDatum  datum,
                                      EllipsoidalCS  cs) throws FactoryException;

    /**
     * Creates a derived coordinate reference system. If the transform is an affine
     * map performing a rotation, then any mixed axes must have identical units.
     * For example, a (<var>lat_deg</var>, <var>lon_deg</var>, <var>height_feet</var>)
     * system can be rotated in the (<var>lat</var>, <var>lon</var>) plane, since both
     * affected axes are in degrees. But the transform should not rotate this coordinate
     * system in any other plane.
     * <p>
     * The {@code conversionFromBase} shall contains the {@linkplain Conversion#getParameterValues
     * parameter values} required for the conversion. It may or may not contain the corresponding
     * "{@linkplain Conversion#getMathTransform base to derived}" transform, at user's choice. If
     * a transform is provided, this method may or may not use it at implementation choice.
     * Otherwise it shall creates the transform from the parameters.
     * <p>
     * It is the user's responsability to ensure that the conversion performs all required steps,
     * including unit conversions and change of axis order, if needed. Note that this behavior is
     * different than {@link #createProjectedCRS createProjectedCRS} because transforms other than
     * <cite>cartographic projections</cite> are not standardized.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  baseCRS Coordinate reference system to base the projection on. The number of axes
     *         must matches the {@linkplain OperationMethod#getSourceDimensions source dimensions}
     *         of the conversion from base.
     * @param  conversionFromBase The
     *         {@linkplain CoordinateOperationFactory#createDefiningConversion defining conversion}.
     * @param  derivedCS The coordinate system for the derived CRS. The number of axes must matches
     *         the {@linkplain OperationMethod#getTargetDimensions target dimensions} of the conversion
     *         from base.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     *
     * @see CoordinateOperationFactory#createDefiningConversion
     * @see MathTransformFactory#createBaseToDerived
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="createFittedCoordinateSystem", specification=OGC_01009)
    DerivedCRS createDerivedCRS(Map<String,?>          properties,
                                CoordinateReferenceSystem baseCRS,
                                Conversion     conversionFromBase,
                                CoordinateSystem derivedCS) throws FactoryException;

    /**
     * Creates a projected coordinate reference system from a defining conversion.
     * The {@code conversionFromBase} shall contains the {@linkplain Conversion#getParameterValues
     * parameter values} required for the projection. It may or may not contain the corresponding
     * "{@linkplain Conversion#getMathTransform base to derived}" transform, at user's choice. If
     * a transform is provided, this method may or may not use it at implementation choice.
     * Otherwise it shall creates the transform from the parameters.
     * <p>
     * The supplied conversion should <strong>not</strong> includes the operation steps for
     * performing {@linkplain CoordinateSystemAxis#getUnit unit} conversions and change of
     * {@linkplain CoordinateSystem#getAxis axis} order; those operations shall be inferred
     * by this constructor by some code equivalent to:
     *
     * <blockquote><code>
     * MathTransform baseToDerived = {@linkplain MathTransformFactory#createBaseToDerived
     * MathTransformFactory.createBaseToDerived}(baseCRS, parameters, derivedCS)
     * </code></blockquote>
     *
     * This behavior is different than {@link #createDerivedCRS createDerivedCRS} because
     * parameterized transforms are standardized for projections. See the {@linkplain
     * MathTransformFactory#createParameterizedTransform note on cartographic projections}.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  baseCRS Geographic coordinate reference system to base the projection on. The number
     *         of axes must matches the {@linkplain OperationMethod#getSourceDimensions source dimensions}
     *         of the conversion from base.
     * @param  conversionFromBase The
     *         {@linkplain CoordinateOperationFactory#createDefiningConversion defining conversion}.
     * @param  derivedCS The coordinate system for the projected CRS. The number of axes must matches
     *         the {@linkplain OperationMethod#getTargetDimensions target dimensions} of the conversion
     *         from base.
     * @return The coordinate reference system for the given properties.
     * @throws FactoryException if the object creation failed.
     *
     * @see CoordinateOperationFactory#createDefiningConversion
     * @see MathTransformFactory#createBaseToDerived
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="createProjectedCoordinateSystem", specification=OGC_01009)
    ProjectedCRS createProjectedCRS(Map<String,?> properties,
                                    GeographicCRS baseCRS,
                                    Conversion    conversionFromBase,
                                    CartesianCS   derivedCS) throws FactoryException;

    /**
     * Creates a coordinate reference system object from a XML string.
     *
     * @param  xml Coordinate reference system encoded in XML format.
     * @return The coordinate reference system for the given XML.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createFromXML", specification=OGC_01009)
    CoordinateReferenceSystem createFromXML(String xml) throws FactoryException;

    /**
     * Creates a coordinate reference system object from a string.
     * The <A HREF="../doc-files/WKT.html">definition for WKT</A>
     * is shown using Extended Backus Naur Form (EBNF).
     *
     * @param  wkt Coordinate system encoded in Well-Known Text format.
     * @return The coordinate reference system for the given WKT.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createFromWKT", specification=OGC_01009)
    CoordinateReferenceSystem createFromWKT(String wkt) throws FactoryException;
}
