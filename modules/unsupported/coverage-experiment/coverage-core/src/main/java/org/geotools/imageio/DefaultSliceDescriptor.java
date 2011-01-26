/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.metadata.Axis;
import org.geotools.imageio.metadata.Band;
import org.geotools.imageio.metadata.BoundedBy;
import org.geotools.imageio.metadata.CoordinateReferenceSystem;
import org.geotools.imageio.metadata.DefinedByConversion;
import org.geotools.imageio.metadata.Identification;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.imageio.metadata.SpatioTemporalMetadataFormat;
import org.geotools.imageio.metadata.BoundedBy.TemporalExtent;
import org.geotools.imageio.metadata.DefinedByConversion.ParameterValue;
import org.geotools.metadata.sql.MetadataException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.opengis.geometry.BoundingBox;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.cs.TimeCS;
import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;
import org.opengis.temporal.TemporalGeometricPrimitive;

/**
 * Default implementation of {@link SliceDescriptor}.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 */
public class DefaultSliceDescriptor extends AbstractSliceDescriptor {
    public DefaultSliceDescriptor(SpatioTemporalMetadata metadata) {
        super(metadata, ReferencingFactoryContainer.instance(null));
    }
    
    private final static java.util.logging.Logger LOGGER = Logger
    .getLogger("org.geotools.imageio");

    public enum CRSType {
        GEO3D, GEO2D, PROJECTED, DERIVED, PROJECTED3D, VERTICAL, TEMPORAL
    }

    /**
     * This array allows to know how {@link #coordinateReferenceSystem} is
     * composed. In case of a {@link CompoundCRS}, this field quickly allows to
     * know what CoordinateReferenceSystem are composing the main
     * CoordinateReferenceSystem.
     */
    private CRSType[] CRSTypes;

    /**
     * The object to use for parsing and formatting units.
     */
    private UnitFormat unitFormat;
    
    private static org.opengis.referencing.crs.CoordinateReferenceSystem wgs84Crs;
    
    static {
        try {
            wgs84Crs = CRS.decode("EPSG:4326", true);
        } catch (NoSuchAuthorityCodeException e) {
            wgs84Crs = DefaultGeographicCRS.WGS84;
        } catch (FactoryException e) {
            wgs84Crs = DefaultGeographicCRS.WGS84;
        }
}

    /**
     * Set of commonly used symbols for "metres".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] METERS = { "meter", "meters", "metre",
            "metres", "m" };

    /**
     * Set of commonly used symbols for "degrees".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] DEGREES = { "degree", "degrees", "deg", "°" };

    /**
     * Set of commonly used symbols for "seconds".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] SECONDS = { "second", "sec", "seconds since" };

    /**
     * Set of commonly used symbols for "seconds".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] MINUTES = { "minute", "min", "minutes since" };

    /**
     * Set of commonly used symbols for "seconds".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] HOURS = { "hour", "hh", "hours since" };

    /**
     * Set of commonly used symbols for "seconds".
     * 
     * @todo Needs a more general way to set unit symbols once the Unit API is
     *       completed.
     */
    private static final String[] DAYS = { "day", "dd", "days since" };

    /**
     * Set of {@linkplain DefaultEllipsoid ellipsoids} already defined.
     */
    private static final DefaultEllipsoid[] ELLIPSOIDS = new DefaultEllipsoid[] {
            DefaultEllipsoid.CLARKE_1866, DefaultEllipsoid.GRS80,
            DefaultEllipsoid.INTERNATIONAL_1924, DefaultEllipsoid.SPHERE,
            DefaultEllipsoid.WGS84 };

    /**
     * 
     * @param metadata
     */
    protected void setCoordinateReferenceSystem(SpatioTemporalMetadata metadata) {
        final ReferencingFactoryContainer factories = getFactoryContainer();
        final CSFactory csFactory = factories.getCSFactory();
        final DatumFactory datumFactory = factories.getDatumFactory();
        final CRSFactory crsFactory = factories.getCRSFactory();

        // ////
        // Check if a TemporalCRS is defined
        // ////
        TemporalCRS temporalCRS = null;
        // get the metadata for the temporal CRS
        org.geotools.imageio.metadata.TemporalCRS metaTemporalCRS = metadata.getTemporalCRS();

        try {
            if (metaTemporalCRS != null) {
                // Creating the Time Coordinate System
                String csName = metaTemporalCRS.getCoordinateSystem().getName();
                if (csName == null) {
                    csName = "Unknown";
                }
                final Map<String, String> csMap = Collections.singletonMap("name", csName);
                final Axis timeAxis = metaTemporalCRS.getAxis(0);
                final TimeCS timeCS = csFactory.createTimeCS(csMap, getAxis(timeAxis.getIdentification().getName(), getDirection(timeAxis.getDirection()), timeAxis.getUnits()));

                // Creating the Temporal Datum
                String datumName = metaTemporalCRS.getDatum().getName();
                if (datumName == null) {
                    datumName = "Unknown";
                }
                final Map<String, String> datumMap = Collections.singletonMap("name", datumName);
                final Position timeOrigin = new DefaultPosition(new SimpleInternationalString(metaTemporalCRS.getOrigin()));
                final TemporalDatum temporalDatum = datumFactory.createTemporalDatum(datumMap, timeOrigin.getDate());

                // Finally creating the Temporal CoordinateReferenceSystem
                String crsName = metaTemporalCRS.getIdentification() != null ? metaTemporalCRS.getIdentification().getName(): null;
                if (crsName == null) {
                    crsName = "Unknown";
                }
                final Map<String, String> crsMap = Collections.singletonMap("name", crsName);
                temporalCRS = crsFactory.createTemporalCRS(crsMap,temporalDatum, timeCS);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            temporalCRS = null;
        } catch (ParseException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            temporalCRS = null;
        }

        // ////
        // Check if a VerticalCRS is defined
        // ////
        VerticalCRS verticalCRS = null;
        org.geotools.imageio.metadata.VerticalCRS metaVerticalCRS = metadata.getVerticalCRS();

        try {
            if (metaVerticalCRS != null) {
                // Creating the Vertical Coordinate System
                String csName = metaVerticalCRS.getCoordinateSystem().getName();
                if (csName == null) {
                    csName = "Unknown";
                }
                final Map<String, String> csMap = Collections.singletonMap(
                        "name", csName);
                final Axis verticalAxis = metaVerticalCRS.getAxis(0);
                VerticalCS verticalCS = csFactory.createVerticalCS(csMap,getAxis(verticalAxis.getIdentification().getName(),getDirection(verticalAxis.getDirection()),verticalAxis.getUnits()));

                // Creating the Vertical Datum
                String datumName = metaVerticalCRS.getDatum().getName();
                if (datumName == null) {
                    datumName = "Unknown";
                }
                final Map<String, String> datumMap = Collections.singletonMap("name", datumName);
                final VerticalDatum verticalDatum = datumFactory.createVerticalDatum( datumMap, VerticalDatumType.valueOf(metaVerticalCRS .getVerticalDatumType()));

                // Finally creating the Vertical CoordinateReferenceSystem
                String crsName = metaVerticalCRS.getIdentification() != null ? metaVerticalCRS.getIdentification().getName(): null;
                if (crsName == null) {
                    crsName = "Unknown";
                }
                final Map<String, String> crsMap = Collections.singletonMap("name", crsName);
                verticalCRS = crsFactory.createVerticalCRS(crsMap,verticalDatum, verticalCS);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse vertical CRS", e);
            verticalCRS = null;
        }

        // ////
        // Creating the CoordinateReferenceSystem
        // ////
        org.opengis.referencing.crs.CoordinateReferenceSystem crs = null;
        final CoordinateReferenceSystem metaCRS = metadata.getCRS();
        String name = metaCRS.getCRS().getName();
        if (name == null) {
            name = "Unknown";
        }
        if (name.contains("WGS84") || name.contains("WGS 84")) {
            crs = (name.contains("3D")) ? DefaultGeographicCRS.WGS84_3D: wgs84Crs;
        }

        if (crs == null) {
            String type = metaCRS.getCrsType();
            if (type == null) {
                type = (metaCRS.getBaseCRS() == null) ? SpatioTemporalMetadataFormat.GEOGRAPHIC: SpatioTemporalMetadataFormat.PROJECTED;
            }
            final Map<String, String> map = Collections.singletonMap("name",
                    name);
            try {
                if (type.equalsIgnoreCase(SpatioTemporalMetadataFormat.GEOGRAPHIC) || type.equalsIgnoreCase(SpatioTemporalMetadataFormat.GEOGRAPHIC_3D)) {
                    crs = crsFactory.createGeographicCRS(map,(GeodeticDatum) getDatum(metaCRS),(EllipsoidalCS) getCoordinateSystem(metaCRS));
                } else {
                    final Map<String, String> baseMap = Collections.singletonMap("name", metaCRS.getBaseCRS().getName());
                    final GeographicCRS baseCRS = crsFactory.createGeographicCRS(baseMap,(GeodeticDatum) getDatum(metaCRS), DefaultEllipsoidalCS.GEODETIC_2D);
                    crs = crsFactory.createProjectedCRS(map, baseCRS,getProjection(metaCRS),(CartesianCS) getCoordinateSystem(metaCRS));
                }
            }catch (Throwable e) {
                crs = null;
            }
        }

        // ////
        // Finally setting up the Coordinate Reference System
        // ////
        if (temporalCRS != null && verticalCRS != null && crs != null) {
        	// build a compound crs
        	final CompoundCRS compoundCRS=
        		new DefaultCompoundCRS("CompoundCRS",
        				new org.opengis.referencing.crs.CoordinateReferenceSystem[] {temporalCRS, verticalCRS, crs});
            setCoordinateReferenceSystem(compoundCRS);
            
            //
            // set the crs types
            //
            CRSTypes = new CRSType[3];
            CRSTypes[0] = CRSType.TEMPORAL;
            CRSTypes[1] = CRSType.VERTICAL;
            
            // horizontal
            if(metaCRS.getBaseCRS() != null )	// Do we have a prohect crs
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[2] = CRSType.PROJECTED;
            	else
            		CRSTypes[2] = CRSType.PROJECTED3D;
            else
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[2] = CRSType.GEO2D;
            	else
            		CRSTypes[2] = CRSType.GEO3D;
            
        } else if (temporalCRS != null && verticalCRS == null && crs != null) {
            setCoordinateReferenceSystem(new DefaultCompoundCRS(
                    // metaCRS.getIdentification().getName(),
                    "CompoundCRS",
                    new org.opengis.referencing.crs.CoordinateReferenceSystem[] {temporalCRS,crs}));
            CRSTypes = new CRSType[2];
            CRSTypes[0] = CRSType.TEMPORAL;
            // horizontal
            if(metaCRS.getBaseCRS() != null )	// Do we have a prohect crs
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[1] = CRSType.PROJECTED;
            	else
            		CRSTypes[1] = CRSType.PROJECTED3D;
            else
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[1] = CRSType.GEO2D;
            	else
            		CRSTypes[1] = CRSType.GEO3D;
          
        } else if (temporalCRS == null && verticalCRS != null && crs != null) {
            setCoordinateReferenceSystem(new DefaultCompoundCRS(
                    // metaCRS.getIdentification().getName(),
                    "CompoundCRS",
                    new org.opengis.referencing.crs.CoordinateReferenceSystem[] {verticalCRS ,crs}));
            CRSTypes = new CRSType[2];
            CRSTypes[0] = CRSType.VERTICAL;
            // horizontal
            if(metaCRS.getBaseCRS() != null )	// Do we have a prohect crs
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[1] = CRSType.PROJECTED;
            	else
            		CRSTypes[1] = CRSType.PROJECTED3D;
            else
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[1] = CRSType.GEO2D;
            	else
            		CRSTypes[1] = CRSType.GEO3D;
        } else if (temporalCRS != null && verticalCRS != null && crs == null) {
            setCoordinateReferenceSystem(new DefaultCompoundCRS(
                    // metaCRS.getIdentification().getName(),
                    "CompoundCRS",
                    new org.opengis.referencing.crs.CoordinateReferenceSystem[] {temporalCRS,verticalCRS }));
            CRSTypes = new CRSType[2];
            CRSTypes[1] = CRSType.TEMPORAL;
            CRSTypes[0] = CRSType.VERTICAL;
        } else if (temporalCRS != null && verticalCRS == null && crs == null) {
            setCoordinateReferenceSystem(temporalCRS);
            CRSTypes = new CRSType[1];
            CRSTypes[0] = CRSType.TEMPORAL;

        } else if (temporalCRS == null && verticalCRS != null && crs == null) {
            setCoordinateReferenceSystem(verticalCRS);
            CRSTypes = new CRSType[1];
            CRSTypes[0] = CRSType.VERTICAL;
        } else if (temporalCRS == null && verticalCRS == null && crs != null) {
            setCoordinateReferenceSystem(crs);
            CRSTypes = new CRSType[1];
            // horizontal
            if(metaCRS.getBaseCRS() != null )	// Do we have a prohect crs
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[0] = CRSType.PROJECTED;
            	else
            		CRSTypes[0] = CRSType.PROJECTED3D;
            else
            	if(metaCRS.getDimension() < 3)
            		CRSTypes[0] = CRSType.GEO2D;
            	else
            		CRSTypes[0] = CRSType.GEO3D;
        }
    }

    /**
     * Get the {@link AxisDirection} object related to the specified direction
     * 
     * @param direction
     * @return
     */
    private AxisDirection getDirection(final String direction) {
        return AxisDirection.valueOf(direction);
    }

    /**
     * Returns the {@linkplain CoordinateSystem coordinate system}. The default
     * implementation builds a coordinate system using the
     * {@linkplain #getAxis axes} defined in the metadata.
     * 
     * @throws MetadataException
     *                 if there is less than 2 axes defined in the metadata, or
     *                 if the creation of the coordinate system failed.
     * 
     * @see #getAxis
     * @see CoordinateSystem
     */
    private  CoordinateSystem getCoordinateSystem(
            final CoordinateReferenceSystem metaCRS) throws Exception {
        String name = metaCRS.getCoordinateSystem().getName();
        if (name == null) {
            name = "Unknown";
        }
        final String type = (metaCRS.getBaseCRS() == null) ? SpatioTemporalMetadataFormat.ELLIPSOIDAL
                : SpatioTemporalMetadataFormat.CARTESIAN;
        final CSFactory factory = getFactoryContainer().getCSFactory();
        final Map<String, String> map = Collections.singletonMap("name", name);
        final int dimension = metaCRS.getDimension();
        if (dimension < 2) {
            throw new MetadataException("Number of dimension error : "
                    + dimension);
        }
        try {
            if (dimension < 3) {
                CoordinateSystemAxis axis1 = getAxis(metaCRS.getAxis(0)
                        .getIdentification().getName(), getDirection(metaCRS
                        .getAxis(0).getDirection()), metaCRS.getAxis(0).getUnits());
                CoordinateSystemAxis axis2 = getAxis(metaCRS.getAxis(1)
                        .getIdentification().getName(), getDirection(metaCRS
                        .getAxis(1).getDirection()), metaCRS.getAxis(1).getUnits());
                if (type.equalsIgnoreCase(SpatioTemporalMetadataFormat.CARTESIAN)) {
                    return factory.createCartesianCS(map, axis1, axis2);
                }
                if (type.equalsIgnoreCase(SpatioTemporalMetadataFormat.ELLIPSOIDAL)) {
                    return factory.createEllipsoidalCS(map, axis1, axis2);
                }
            } else {
                CoordinateSystemAxis axis1 = getAxis(metaCRS.getAxis(0)
                        .getIdentification().getName(), getDirection(metaCRS
                        .getAxis(0).getDirection()), metaCRS.getAxis(0).getUnits());
                CoordinateSystemAxis axis2 = getAxis(metaCRS.getAxis(1)
                        .getIdentification().getName(), getDirection(metaCRS
                        .getAxis(1).getDirection()), metaCRS.getAxis(1).getUnits());
                CoordinateSystemAxis axis3 = getAxis(metaCRS.getAxis(2)
                        .getIdentification().getName(), getDirection(metaCRS
                        .getAxis(2).getDirection()), metaCRS.getAxis(2).getUnits());
                if (type.equalsIgnoreCase(SpatioTemporalMetadataFormat.CARTESIAN)) {
                    return factory.createCartesianCS(map, axis1, axis2, axis3);
                }
                if (type.equalsIgnoreCase(SpatioTemporalMetadataFormat.ELLIPSOIDAL)) {
                    return factory.createEllipsoidalCS(map, axis1, axis2, axis3);
                }
            }
            /*
             * Should not happened, since the type value should be contained in
             * the {@link SpatioTemporalMetadataFormat#CS_TYPES} list.
             */
            throw new Exception("Coordinate system type not known : " + type);
        } catch (FactoryException e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the datum. The default implementation performs the following
     * steps:
     * <p>
     * <ul>
     * <li>Verifies if the datum name contains {@code WGS84}, and returns a
     * {@link DefaultGeodeticDatum#WGS84} geodetic datum if it is the case.
     * </li>
     * <li>Builds a {@linkplain PrimeMeridian prime meridian} using information
     * stored into the metadata tree. </li>
     * <li>Returns a {@linkplain DefaultGeodeticDatum geodetic datum} built on
     * the prime meridian. </li>
     * </ul>
     * </p>
     * 
     * @throws MetadataException
     *                 if the datum is not defined, or if the
     *                 {@link #getEllipsoid} method fails.
     * 
     * @todo: The current implementation only returns a
     *        {@linkplain GeodeticDatum geodetic datum}, other kind of datum
     *        have to be generated too.
     * 
     * @see #getEllipsoid
     */
    private Datum getDatum(final CoordinateReferenceSystem metaCRS) throws Exception {
        Identification id = metaCRS.getBaseCRS();
        org.geotools.imageio.metadata.Identification identDatum = id != null ? id : metaCRS.getIdentification();
        if (identDatum == null) {
            throw new Exception("The datum is not defined.");
        }
        final String name = identDatum.getName();
        if (name == null) {
            throw new Exception("Datum name not defined.");
        }
        if (name.toUpperCase().contains("WGS84")) {
            return DefaultGeodeticDatum.WGS84;
        }
        final String primeMeridianName = metaCRS.getPrimeMeridian().getName();
        final PrimeMeridian primeMeridian;
        /*
         * By default, if the prime meridian name is not defined, or if it is
         * defined with {@code Greenwich}, one chooses the {@code Greenwich}
         * meridian as prime meridian. Otherwise one builds it, using the
         * {@code greenwichLongitude} parameter.
         */
        if ((primeMeridianName == null)
                || (primeMeridianName != null && primeMeridianName.toLowerCase().contains("greenwich"))) {
            primeMeridian = DefaultPrimeMeridian.GREENWICH;
        } else {
            final double greenwichLon = Double.parseDouble(metaCRS.getGreenwichLongitude());
            primeMeridian = (Double.isNaN(greenwichLon)) ? DefaultPrimeMeridian.GREENWICH
                    : new DefaultPrimeMeridian(primeMeridianName, greenwichLon);
        }
        return new DefaultGeodeticDatum(name, getEllipsoid(metaCRS), primeMeridian);
    }

    /**
     * Returns the ellipsoid. Depending on whether
     * {@link ImageReferencing#semiMinorAxis} or
     * {@link ImageReferencing#inverseFlattening} has been defined, the default
     * implementation will construct an ellispoid using
     * {@link DatumFactory#createEllipsoid} or
     * {@link DatumFactory#createFlattenedSphere} respectively.
     * 
     * @throws MetadataException
     *                 if the operation failed to create the
     *                 {@linkplain Ellipsoid ellipsoid}.
     * 
     * @see #getUnit(String)
     */
    private Ellipsoid getEllipsoid(final CoordinateReferenceSystem metaCRS) throws Exception {
        final String name = metaCRS.getEllipsoid().getName();
        if (name != null) {
            for (final DefaultEllipsoid ellipsoid : ELLIPSOIDS) {
                if (ellipsoid.nameMatches(name)) {
                    return ellipsoid;
                }
            }
        } else {
            throw new Exception("Ellipsoid name not defined.");
        }
        // It has a name defined, but it is not present in the list of known
        // ellipsoids.
        final double semiMajorAxis = Double.parseDouble(metaCRS.getSemiMajorAxis());
        if (Double.isNaN(semiMajorAxis)) {
            throw new Exception("Ellipsoid semi major axis not defined.");
        }
        final String ellipsoidUnit = metaCRS.getEllipsoidUnit();
        if (ellipsoidUnit == null) {
            throw new Exception("Ellipsoid unit not defined.");
        }
        final Unit unit = getUnit(ellipsoidUnit);
        final Map<String, String> map = Collections.singletonMap("name", name);
        try {
            final DatumFactory datumFactory = getFactoryContainer().getDatumFactory();
            return (metaCRS.getSecondDefinigParameterType()
                    .equals(SpatioTemporalMetadataFormat.MD_DTM_GD_EL_SEMIMINORAXIS)) ? datumFactory
                    .createEllipsoid(map, semiMajorAxis, Double .parseDouble(metaCRS.getSecondDefinigParameterValue()), unit)
                    : datumFactory.createFlattenedSphere(map, semiMajorAxis,Double.parseDouble(metaCRS.getSecondDefinigParameterValue()), unit);
        } catch (FactoryException e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the unit which matches with the name given.
     * 
     * @param unitName
     *                The name of the unit. Should not be {@code null}.
     * @return The unit matching with the specified name.
     * @throws MetadataException
     *                 if the unit name does not match with the
     *                 {@linkplain #unitFormat unit format}.
     */
    private Unit<?> getUnit(final String unitName) throws FactoryException {
        if (contains(unitName, METERS)) {
            return SI.METER;
        } else if (contains(unitName, DEGREES)) {
            return NonSI.DEGREE_ANGLE;
        } else if (contains(unitName, SECONDS)) {
            return SI.SECOND;
        } else if (contains(unitName, MINUTES)) {
            return NonSI.MINUTE;
        } else if (contains(unitName, HOURS)) {
            return NonSI.HOUR;
        } else if (contains(unitName, DAYS)) {
            return NonSI.DAY;
        } else {
            if (unitFormat == null) {
                unitFormat = UnitFormat.getInstance();
            }
            try {
                return (Unit<?>) unitFormat.parseObject(unitName);
            } catch (ParseException e) {
                throw new FactoryException("Unit not known : " + unitName, e);
            }
        }
    }

    /**
     * Build a proper {@link CoordinateSystemAxis} given the set composed of
     * axisName, axisDirection and axis unit of measure.
     * 
     * @param axisName
     *                the name of the axis to be built.
     * @param direction
     *                the {@linkplain AxisDirection direction} of the axis.
     * @param unitName
     *                the unit of measure string.
     * @return a proper {@link CoordinateSystemAxis} instance or {@code null} if
     *         unable to build it.
     * @throws FactoryException
     */
    private  CoordinateSystemAxis getAxis(final String axisName,
            final AxisDirection direction, final String unitName)
            throws FactoryException {
        if (axisName == null) {
            return null;
        }
        final DefaultCoordinateSystemAxis axisFound = DefaultCoordinateSystemAxis.getPredefined(axisName, direction);
        if (axisFound != null) {
            return axisFound;
        }

        /*
         * The current axis defined in the metadata tree is not already known in
         * the Geotools implementation, so one will build it using those
         * information.
         */
        final Unit<?> unit = getUnit(unitName);
        final Map<String, String> map = Collections.singletonMap("name", axisName);
        try {
            return getFactoryContainer().getCSFactory().createCoordinateSystemAxis(map, axisName, direction, unit);
        } catch (FactoryException e) {
            throw new FactoryException(e.getLocalizedMessage());
        }
    }

    /**
     * Check if {@code toSearch} appears in the {@code list} array. Search is
     * case-insensitive. This is a temporary patch (will be removed when the
     * final API for JSR-108: Units specification will be available).
     */
    private static boolean contains(final String toSearch, final String[] list) {
        for (int i = list.length; --i >= 0;) {
            if (toSearch.toLowerCase().contains(list[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the projection. The default implementation performs the following
     * steps:
     * <p>
     * <ul>
     * <li>Gets a {@linkplain ParameterValueGroup parameter value group} from a
     * {@link MathTransformFactory}</li>
     * 
     * <li>Gets the metadata values for each parameters in the above step. If a
     * parameter is not defined in this {@code MetadataReader}, then it will be
     * left to its (projection dependent) default value. Parameters are
     * projection dependent, but will typically include
     * 
     * {@code "semi_major"}, {@code "semi_minor"} (or
     * {@code "inverse_flattening"}), {@code "central_meridian"},
     * {@code "latitude_of_origin"}, {@code "false_easting"} and
     * {@code "false_northing"}.
     * 
     * <li>Computes and returns a {@linkplain DefiningConversion conversion}
     * using the name of the projection and the parameter value group previously
     * filled.</li>
     * </ul>
     * </p>
     * 
     * @return The projection.
     * @throws MetadataException
     *                 if the operation failed for some other reason (for
     *                 example if a parameter value can't be parsed as a
     *                 {@code double}).
     * 
     */
    private Conversion getProjection(final CoordinateReferenceSystem metaCRS) throws Exception {
        final MathTransformFactory mathTransformFactory = getFactoryContainer().getMathTransformFactory();

        DefinedByConversion conversionDefinition = metaCRS.getDefinedByConversion();
        String projectionName = conversionDefinition.getIdentification().getName();
        if (projectionName == null) {
            throw new MetadataException("Projection name is not defined.");
        }
        final ParameterValueGroup paramValueGroup;
        try {
            paramValueGroup = mathTransformFactory.getDefaultParameters(projectionName);
        } catch (NoSuchIdentifierException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
        for (int i = 0; i < conversionDefinition.numParams(); i++) {
            final ParameterValue parameter = conversionDefinition.getParameterValue(i);
            final String name = parameter.getIdentification().getName();
            if (name == null) {
                continue;
            }
            final double value = parameter.getValue();
            if (Double.isNaN(value)) {
                continue;
            }
            try {
                paramValueGroup.parameter(name).setValue(value);
            } catch (ParameterNotFoundException e) {
                // Should not happened. Continue with the next parameter, the
                // current one will be ignored.
                continue;
            }
        }
        return new DefiningConversion(projectionName, paramValueGroup);
    }

    /**
     * Set the {@link GeneralEnvelope} object referring to this object given a
     * {@link BoundedBy} metadata element.
     * 
     * @param boundedBy
     *                the metadata node to be used to set envelope.
     */
    private void buildBoundingBox(BoundedBy boundedBy) {
        if (boundedBy == null)
            throw new NullPointerException("Specified boundedBy metadata node is null");
        final List<Double> minDP = new ArrayList<Double>();
        final List<Double> maxDP = new ArrayList<Double>();
        final double[] lowers = boundedBy.getLowerCorner();
        final double[] uppers = boundedBy.getUpperCorner();

        // temporal extent
        final TemporalGeometricPrimitive temporalExtent = getTemporalExtent();
        final VerticalExtent verticalExtent = getVerticalExtent();
        org.opengis.referencing.crs.CoordinateReferenceSystem coordinateReferenceSystem = getCoordinateReferenceSystem();

        // build the various elements
        int i=0;
        for (CRSType crsType : CRSTypes) {
        	switch (crsType) {
			case TEMPORAL:
				if (temporalExtent != null) {
					final TemporalCRS temporalCRS ;
					if(CRSTypes.length == 1)
						 temporalCRS=(TemporalCRS) coordinateReferenceSystem;
	                else {
	                	// get the compound crs
	                	final CompoundCRS compoundCRS=(CompoundCRS) coordinateReferenceSystem;
	                	temporalCRS=(TemporalCRS) compoundCRS.getCoordinateReferenceSystems().get(i);
	                }

	                if (temporalExtent instanceof Instant) {
	                    double time = DefaultTemporalCRS.wrap(temporalCRS).toValue(((Instant) temporalExtent).getPosition().getDate());
	                    minDP.add(time);
	                    maxDP.add(time);
	                } else if (temporalExtent instanceof Period) {
	                    double beginTime = DefaultTemporalCRS.wrap(temporalCRS).toValue(((Period) temporalExtent).getBeginning().getPosition().getDate());
	                    double endTime = DefaultTemporalCRS.wrap(temporalCRS).toValue(((Period) temporalExtent).getEnding().getPosition().getDate());
	                    minDP.add(beginTime);
	                    maxDP.add(endTime);
	                }
	            } 
				break;
			case VERTICAL:
				 if (verticalExtent != null) {
		                final VerticalExtent.VerticalLevelType verticalType = verticalExtent.getType();
		                if (verticalType == VerticalExtent.VerticalLevelType.SINGLE_NUMBER|| verticalType == VerticalExtent.VerticalLevelType.NUMBER_RANGE) {
		                    minDP.add(verticalExtent.getMinimumValue());
		                    maxDP.add(verticalExtent.getMaximumValue());
		                } // if UNKNOWN ???
		            }
				 break;
			case GEO2D:case PROJECTED: case PROJECTED3D: 
				final org.opengis.referencing.crs.CoordinateReferenceSystem spatialCRS ;
                if(CRSTypes.length == 1)
                	spatialCRS=coordinateReferenceSystem;
                else {
                	// get the compound crs
                	final CompoundCRS compoundCRS=(CompoundCRS) coordinateReferenceSystem;
                	spatialCRS=compoundCRS.getCoordinateReferenceSystems().get(i);
                }

                if (spatialCRS.getCoordinateSystem().getDimension() == lowers.length&& lowers.length == uppers.length) {
                    for (int j = 0; j < lowers.length; j++) {
                        minDP.add(lowers[j]);
                        maxDP.add(uppers[j]);
                    }
                } // not matching dimensions ???
                break;
			default:
				throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_COORDINATE_SYSTEM_$1,crsType.toString()));
			}

        	i++;
        }

        final double[] ordinates = new double[minDP.size() + maxDP.size()];
        int index = 0;
        for (Double value : minDP) {
            ordinates[index++] = value;
        }
        for (Double value : maxDP) {
            ordinates[index++] = value;
        }

        //TODO Improve checks: A slice could be without an Horizontal Extent
        final GeneralEnvelope generalEnvelope = new GeneralEnvelope(coordinateReferenceSystem);
        generalEnvelope.setEnvelope(ordinates);
        setGeneralEnvelope(generalEnvelope);
        
        // try to get the spatial BBOX
        try {
        	
        	GeneralEnvelope spatialBBOX =null;
        	
        	// we have a compound CRS
        	if (coordinateReferenceSystem instanceof CompoundCRS){
        		final org.opengis.referencing.crs.CoordinateReferenceSystem spatialCRS= CRS.getHorizontalCRS(coordinateReferenceSystem);
        		if(spatialCRS!=null){
        			// get the transform from the compound to the spatial crs
        			final MathTransform tr= CRS.findMathTransform(coordinateReferenceSystem, spatialCRS,true);
        			// extract the spatial envelope
        			spatialBBOX=CRS.transform(tr, generalEnvelope);
        			spatialBBOX.setCoordinateReferenceSystem(spatialCRS);
        		}
        	}
        	// we do not have a compound CRS
        	else if (coordinateReferenceSystem instanceof GeographicCRS || coordinateReferenceSystem instanceof ProjectedCRS)
        	{
        		spatialBBOX= new GeneralEnvelope(generalEnvelope);
        		spatialBBOX.setCoordinateReferenceSystem(coordinateReferenceSystem);
        	}

        	// at this point we might have not found a spatial CRS then the bbox should be empty      
        	if(spatialBBOX!=null)
        	{
        		setHorizontalExtent((BoundingBox) new ReferencedEnvelope(spatialBBOX));
        		return;
        	}
		} catch (FactoryException e) {
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
		} catch (TransformException e) {
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
		}

		// if we got here, either we got an error or we have no spatial crs
		setHorizontalExtent(null);
    }

    /**
     * Set the verticalExtent referring to this object given a {@link BoundedBy}
     * metadata element.
     * 
     * @param boundedBy
     *                the metadata node to be used to set envelope.
     */
    private void buildVerticalExtent(BoundedBy boundedBy) {
        if (boundedBy == null)
            throw new NullPointerException("Specified boundedBy metadata node is null");
        final org.geotools.imageio.metadata.BoundedBy.VerticalExtent metaVerticalExtent = boundedBy.getVerticalExtent();
        if (metaVerticalExtent != null) {
            Object value = metaVerticalExtent.getValue();
            if (value != null) {
                final VerticalExtent verticalExtent = new VerticalExtent();
                verticalExtent.setValue(value);
                setVerticalExtent(verticalExtent);
            }
        }
    }

    /**
     * Set the temporal extent referring to this object given a
     * {@link BoundedBy} metadata element.
     * 
     * @param boundedBy
     *                the metadata node to be used to set envelope.
     */
    private void buildTemporalExtent(BoundedBy boundedBy) {
        if (boundedBy == null)
            throw new NullPointerException("Specified boundedBy metadata node is null");
        final TemporalExtent metaTemporalExtent = boundedBy.getTemporalExtent();
        if (metaTemporalExtent != null) {
            final TemporalGeometricPrimitive temporalExtent = (TemporalGeometricPrimitive) metaTemporalExtent.getValue();
            setTemporalExtent(temporalExtent);
        }
    }

    protected void setBoundedBy(final SpatioTemporalMetadata metadata) {
    	BoundedBy bb = metadata.getBoundedBy();
        buildTemporalExtent(bb);
        buildVerticalExtent(bb);
        buildBoundingBox(bb);
    }

    @Override
    protected void setName(final SpatioTemporalMetadata metadata) {
    	final Band band = metadata.getBand(0);
        setElementName(band.getName());
    }
}
