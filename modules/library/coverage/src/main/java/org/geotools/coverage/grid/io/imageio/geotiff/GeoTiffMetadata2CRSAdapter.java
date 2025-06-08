/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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
/*
 * NOTICE OF RELEASE TO THE PUBLIC DOMAIN
 *
 * This work was created by employees of the USDA Forest Service's
 * Fire Science Lab for internal use.  It is therefore ineligible for
 * copyright under title 17, section 105 of the United States Code.  You
 * may treat it as you would treat any public domain work: it may be used,
 * changed, copied, or redistributed, with or without permission of the
 * authors, for free or for compensation.  You may not claim exclusive
 * ownership of this code because it is already owned by everyone.  Use this
 * software entirely at your own risk.  No warranty of any kind is given.
 *
 * A copy of 17-USC-105 should have accompanied this distribution in the file
 * 17USC105.html.  If not, you may access the law via the US Government's
 * public websites:
 *   - http://www.copyright.gov/title17/92chap1.html#105
 *   - http://www.gpoaccess.gov/uscode/  (enter "17USC105" in the search box.)
 */
package org.geotools.coverage.grid.io.imageio.geotiff;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchIdentifierException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ImageCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.datum.DatumFactory;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.datum.ImageDatum;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.datum.PrimeMeridian;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffCoordinateTransformationsCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffPCSCodes;
import org.geotools.measure.Units;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.factory.AllAuthoritiesFactory;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import si.uom.NonSI;
import si.uom.SI;

/**
 * The <code>GeoTiffMetadata2CRSAdapter</code> is responsible for interpreting the metadata provided by the <code>
 * GeoTiffIIOMetadataDecoder</code> for the purposes of constructing a CoordinateSystem object representative of the
 * information found in the tags.
 *
 * <p>This class implements the flow indicated by the following diagram:
 *
 * <p align="center"><img src="../../../../../../../doc-files/GeoTiffFlow.png">
 *
 * <p>To use this class, the <CODE>GeoTiffReader</CODE> should create an instance with the <code>
 * CoordinateSystemAuthorityFactory</code> specified by the <CODE>GeoTiffFormat</CODE> instance which created the
 * reader. The image specific metadata should then be set with the appropriate accessor methods. Finally, the <code>
 * createCoordinateSystem()</code> method is called to produce the <code>CoordinateReferenceSystem</code> object
 * specified by the metadata.
 *
 * @author Bryce Nordgren / USDA Forest Service
 * @author Simone Giannecchini
 * @author Daniele Romagnoli
 */
public final class GeoTiffMetadata2CRSAdapter {

    /** {@link Logger}. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffMetadata2CRSAdapter.class);

    /**
     * This {@link AffineTransform} can be used when the underlying geotiff declares to work with
     * {@link GeoTiffConstants#RasterPixelIsArea} pixel interpretation in order to convert the transformation back to
     * using the {@link GeoTiffConstants#RasterPixelIsPoint} convention which is the one OGC requires for coverage.
     */
    private static final AffineTransform PixelIsArea2PixelIsPoint = AffineTransform.getTranslateInstance(0.5, 0.5);

    private static final String UNNAMED = "unnamed";

    /** EPSG Factory for creating {@link GeodeticDatum}objects. */
    private final DatumFactory datumObjFactory;

    /** {@link Hints} to control the creation of the factories for this {@link GeoTiffMetadata2CRSAdapter} object. */
    private Hints hints;

    /** Cached {@link MathTransformFactory} for building {@link MathTransform} objects. */
    private final MathTransformFactory mtFactory;

    /** EPSG factories for various purposes. */
    private final AllAuthoritiesFactory allAuthoritiesFactory;

    /**
     * Creates a new instance of GeoTiffMetadata2CRSAdapter
     *
     * @param hints a map of hints to locate the authority and object factories. (can be null)
     */
    public GeoTiffMetadata2CRSAdapter(Hints hints) {

        this.hints = hints != null ? hints.clone() : GeoTools.getDefaultHints().clone();

        // various authority related factories
        allAuthoritiesFactory = new AllAuthoritiesFactory(this.hints);

        // various factories
        datumObjFactory = ReferencingFactoryFinder.getDatumFactory(this.hints);
        mtFactory = ReferencingFactoryFinder.getMathTransformFactory(this.hints);
    }

    /**
     * This method creates a <code>CoordinateReferenceSystem</code> object from the metadata which has been set earlier.
     * If it cannot create the <code>CoordinateReferenceSystem</code>, then one of three exceptions is thrown to
     * indicate the error.
     *
     * @return the <code>CoordinateReferenceSystem</code> object representing the file data
     * @throws IOException if there is unexpected data in the GeoKey tags.
     * @throws NullPointerException if the <code>csAuthorityFactory</code>, <code>datumFactory
     *     </code>, <code>crsFactory</code> or <code>metadata</code> are uninitialized
     * @throws UnsupportedOperationException if the coordinate system specified by the GeoTiff file is not supported.
     */
    public CoordinateReferenceSystem createCoordinateSystem(final GeoTiffIIOMetadataDecoder metadata) throws Exception {

        // the first thing to check is the Model Type.
        // is it "Projected" or is it "Geographic"?
        // "Geocentric" is not supported.
        // "User-defined" only supported if citation is WKT
        switch (getGeoKeyAsInt(GeoTiffConstants.GTModelTypeGeoKey, metadata)) {
            case GeoTiffPCSCodes.ModelTypeProjected:
                return createProjectedCoordinateReferenceSystem(metadata);

            case GeoTiffGCSCodes.ModelTypeGeographic:
                return createGeographicCoordinateReferenceSystem(metadata);

            case GeoTiffGCSCodes.ModelTypeUserDefined:
                String citation = metadata.getGeoKey(GeoTiffPCSCodes.PCSCitationGeoKey);
                if (citation != null) {
                    if (citation.startsWith("ESRI PE String =")) {
                        String wkt = citation.substring(17);
                        try {
                            return CRS.parseWKT(wkt);
                        } catch (FactoryException unavailable) {
                            throw new UnsupportedOperationException(
                                    "GeoTiffMetadata2CRSAdapter::createCoordinateSystem: User-defined requires citation of form 'ESRI PE String = <wkt>'");
                        }
                    }
                }

            default:
                throw new UnsupportedOperationException(
                        "GeoTiffMetadata2CRSAdapter::createCoordinateSystem:Only Geographic & Projected Systems are supported."
                                + "User-defined partially supported with citation of form 'ESRI PE String = <wkt>'");
        }
    }

    /**
     * This code is responsible for creating a projected coordinate reference system as specified in the GeoTiff
     * specification. User defined values are supported throughout the evolution of this specification with except of
     * the coordinate transformation which must be one of the supported types.
     *
     * @param metadata to use for building a {@link ProjectedCRS}.
     */
    private ProjectedCRS createProjectedCoordinateReferenceSystem(GeoTiffIIOMetadataDecoder metadata) throws Exception {

        // //
        //
        // Get the projection reference system code in case we have one by
        // lookig for the ProjectedCSTypeGeoKey key
        //
        // //
        String projCode = metadata.getGeoKey(GeoTiffPCSCodes.ProjectedCSTypeGeoKey);
        if (projCode == null) {
            projCode = UNNAMED;
        } else {
            projCode = projCode.trim();
        }

        // //
        //
        // getting the linear unit used by this coordinate reference system
        // since we will use it anyway.
        //
        // //
        Unit<?> linearUnit;
        try {
            linearUnit = createUnit(
                    GeoTiffPCSCodes.ProjLinearUnitsGeoKey,
                    GeoTiffPCSCodes.ProjLinearUnitSizeGeoKey,
                    SI.METRE,
                    SI.METRE,
                    metadata);
        } catch (GeoTiffException e) {
            linearUnit = null;
        }
        // //
        //
        // if it's user defined, there's a lot of work to do, we have to parse
        // many information.
        //
        // //
        if (projCode.equalsIgnoreCase(UNNAMED) || projCode.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            return createUserDefinedPCS(metadata, linearUnit);
        }
        // //
        //
        // if it's not user defined, just use the EPSG factory to create the
        // coordinate system
        //
        // //
        try {
            if (!projCode.toUpperCase().startsWith("EPSG:")) {
                projCode = "EPSG:" + projCode;
            }
            // it is an EPSG crs let's create it.
            final ProjectedCRS pcrs =
                    (ProjectedCRS) allAuthoritiesFactory.createCoordinateReferenceSystem(projCode.toString());
            // //
            //
            // We have nothing to do with the unit of measure
            //
            // //
            boolean isDefaultUnit = metadata.getGeoKey(GeoTiffPCSCodes.ProjLinearUnitsGeoKey) == null
                    && linearUnit != null
                    && linearUnit.equals(SI.METRE);
            if (linearUnit == null
                    || isDefaultUnit
                    || linearUnit.equals(pcrs.getCoordinateSystem().getAxis(0).getUnit())) {
                return pcrs;
            }

            // //
            //
            // Creating a new projected CRS replacing the CS unit
            //
            // //
            final Conversion conversionFromBase = new DefiningConversion(
                    pcrs.getConversionFromBase().getName().getCode(),
                    pcrs.getConversionFromBase().getParameterValues());
            return new DefaultProjectedCRS(
                    java.util.Collections.singletonMap("name", DefaultEllipsoidalCS.getName(pcrs, Citations.EPSG)),
                    conversionFromBase,
                    pcrs.getBaseCRS(),
                    pcrs.getConversionFromBase().getMathTransform(),
                    createProjectedCS(linearUnit));

        } catch (FactoryException fe) {
            final IOException ex = new GeoTiffException(metadata, fe.getLocalizedMessage(), fe);
            throw ex;
        }
    }

    /**
     * Creation of a geographic coordinate reference system as specified in the GeoTiff specification. User defined
     * values are supported for all the possible levels of the above mentioned specification.
     *
     * @param metadata to use for building a {@link GeographicCRS}.
     */
    private GeographicCRS createGeographicCoordinateReferenceSystem(final GeoTiffIIOMetadataDecoder metadata)
            throws IOException {
        GeographicCRS gcs = null;

        //
        // Get the crs code
        //
        final String tempCode = metadata.getGeoKey(GeoTiffGCSCodes.GeographicTypeGeoKey);
        // lookup the angular units used in this geotiff image
        Unit<?> angularUnit = null;
        try {
            angularUnit = createUnit(
                    GeoTiffGCSCodes.GeogAngularUnitsGeoKey,
                    GeoTiffGCSCodes.GeogAngularUnitSizeGeoKey,
                    SI.RADIAN,
                    NonSI.DEGREE_ANGLE,
                    metadata);
        } catch (GeoTiffException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            angularUnit = null;
        }
        if (angularUnit == null) {
            angularUnit = NonSI.DEGREE_ANGLE;
        }

        // linear unit
        Unit<?> linearUnit = null;
        try {
            linearUnit = createUnit(
                    GeoTiffGCSCodes.GeogLinearUnitsGeoKey,
                    GeoTiffGCSCodes.GeogLinearUnitSizeGeoKey,
                    SI.METRE,
                    SI.METRE,
                    metadata);
        } catch (GeoTiffException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            linearUnit = null;
        }
        if (linearUnit == null) {
            linearUnit = SI.METRE;
        }
        // if it's user defined, there's a lot of work to do
        if (tempCode == null || tempCode.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            //
            // it is user-defined we have to parse a lot of information in order
            // to built it.
            //
            gcs = createUserDefinedGCS(metadata, linearUnit, angularUnit);

        } else {
            try {

                // ////////////////////////////////////////////////////////////////////
                //
                // If it's not user defined, just use the EPSG factory to create
                // the coordinate system but check if the user specified a
                // different angular unit. In this case we need to create a
                // user-defined GCRS.
                //
                // ////////////////////////////////////////////////////////////////////
                final StringBuilder geogCode = new StringBuilder(tempCode);
                if (!tempCode.startsWith("EPSG") && !tempCode.startsWith("epsg")) {
                    geogCode.insert(0, "EPSG:");
                }
                gcs = (GeographicCRS) allAuthoritiesFactory.createCoordinateReferenceSystem(geogCode.toString());
                if (angularUnit != null
                        && !angularUnit.equals(
                                gcs.getCoordinateSystem().getAxis(0).getUnit())) {
                    // //
                    //
                    // Create a user-defined GCRS using the provided angular
                    // unit.
                    //
                    // //
                    gcs = new DefaultGeographicCRS(
                            DefaultEllipsoidalCS.getName(gcs, Citations.EPSG),
                            gcs.getDatum(),
                            DefaultEllipsoidalCS.GEODETIC_2D.usingUnit(angularUnit));
                }
            } catch (FactoryException fe) {
                final IOException ex = new GeoTiffException(metadata, fe.getLocalizedMessage(), fe);

                throw ex;
            }
        }

        return gcs;
    }

    /**
     * Getting a specified geotiff geo key as a int. It is somehow tolerant in the sense that in case such a key does
     * not exist it retrieves 0.
     *
     * @param key we want to get the value for.
     * @param metadata containing the key we are looking for.
     */
    private static int getGeoKeyAsInt(final int key, final GeoTiffIIOMetadataDecoder metadata) {

        try {
            return Integer.parseInt(metadata.getGeoKey(key));
        } catch (NumberFormatException ne) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, ne.getLocalizedMessage(), ne);
            return GeoTiffConstants.UNDEFINED;
        }
    }

    /**
     * Create the grid to world (or raster to model) transformation for this source respecting ALWAYS the OGC
     * {@link PixelInCell#CELL_CENTER} convention for the {@link ImageDatum} of the underlying {@link ImageCRS}.
     *
     * @see <a href="http://lists.maptools.org/pipermail/geotiff/2006-January/000213.html">this email post</a>
     * @param metadata containing the information to build the {@link MathTransform} for going from grid to world.
     */
    public static MathTransform getRasterToModel(final GeoTiffIIOMetadataDecoder metadata) throws GeoTiffException {

        // checks
        Utilities.ensureNonNull("metadata", metadata);

        //
        // Load initials
        //
        final boolean hasTiePoints = metadata.hasTiePoints();
        final boolean hasPixelScales = metadata.hasPixelScales();
        final boolean hasModelTransformation = metadata.hasModelTrasformation();

        // decide raster type
        int rasterType = getGeoKeyAsInt(GeoTiffConstants.GTRasterTypeGeoKey, metadata);
        // geotiff spec says that PixelIsArea is the default
        if (rasterType == GeoTiffConstants.UNDEFINED) {
            rasterType = GeoTiffConstants.RasterPixelIsArea;
        }

        // prepare final transform
        MathTransform xform = null;
        if (hasTiePoints && hasPixelScales) {

            //
            // we use tie points and pixel scales to build the grid to world
            //
            // model space
            final TiePoint[] tiePoints = metadata.getModelTiePoints();
            final PixelScale pixScales = metadata.getModelPixelScales();

            // logging
            if (LOGGER.isLoggable(Level.FINE)) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Logging tiePoints:").append("\n");
                for (TiePoint t : tiePoints) {
                    builder.append(t.toString()).append("\n");
                }
                builder.append("Logging pixScales:").append("\n");
                builder.append(pixScales.toString()).append("\n");
                LOGGER.log(Level.FINE, builder.toString());
            }

            // here is the matrix we need to build
            final GeneralMatrix gm = new GeneralMatrix(3);
            final double scaleRaster2ModelLongitude = pixScales.getScaleX();
            final double scaleRaster2ModelLatitude = -pixScales.getScaleY();
            // "raster" space
            final double tiePointColumn =
                    tiePoints[0].getValueAt(0) + (rasterType == GeoTiffConstants.RasterPixelIsArea ? -0.5 : 0);

            // coordinates
            // (indicies)
            final double tiePointRow =
                    tiePoints[0].getValueAt(1) + (rasterType == GeoTiffConstants.RasterPixelIsArea ? -0.5 : 0);

            // compute an "offset and scale" matrix
            gm.setElement(0, 0, scaleRaster2ModelLongitude);
            gm.setElement(1, 1, scaleRaster2ModelLatitude);
            gm.setElement(0, 1, 0);
            gm.setElement(1, 0, 0);

            gm.setElement(0, 2, tiePoints[0].getValueAt(3) - scaleRaster2ModelLongitude * tiePointColumn);
            gm.setElement(1, 2, tiePoints[0].getValueAt(4) - scaleRaster2ModelLatitude * tiePointRow);

            // make it a LinearTransform
            xform = ProjectiveTransform.create(gm);

        } else if (hasModelTransformation) {

            AffineTransform modelTransformation = metadata.getModelTransformation();
            if (modelTransformation == null) {
                throw new GeoTiffException(metadata, "Null modelTransformation", null);
            }
            // logging
            if (LOGGER.isLoggable(Level.FINE)) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Logging ModelTransformation:").append("\n");
                builder.append(modelTransformation.toString()).append("\n");
                LOGGER.log(Level.FINE, builder.toString());
            }
            if (rasterType == GeoTiffConstants.RasterPixelIsArea) {
                final AffineTransform tempTransform = new AffineTransform(modelTransformation);
                tempTransform.concatenate(PixelIsArea2PixelIsPoint);
                xform = ProjectiveTransform.create(tempTransform);

            } else {
                assert rasterType == GeoTiffConstants.RasterPixelIsPoint;
                xform = ProjectiveTransform.create(modelTransformation);
            }
        } else {
            throw new GeoTiffException(metadata, "Unknown Raster to Model configuration.", null);
        }

        // final check, is this invertible at all?
        try {
            xform.inverse();
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Invalid model transformation found!\n" + e.getLocalizedMessage(), e);
            }

            return null;
        }
        return xform;
    }

    /**
     * Maps the RasterType GeoKey for this geotiff the {@link PixelInCell}
     *
     * @param metadata the metadata instance to extract the information from.
     * @return an instance of the {@link PixelInCell} enum that capture the raster type.
     */
    public static PixelInCell getRasterType(GeoTiffIIOMetadataDecoder metadata) {
        int rasterType = getGeoKeyAsInt(GeoTiffConstants.GTRasterTypeGeoKey, metadata);

        // geotiff spec says that PixelIsArea is the default
        if (rasterType == GeoTiffConstants.UNDEFINED) {
            rasterType = GeoTiffConstants.RasterPixelIsArea;
        }
        if (rasterType == GeoTiffConstants.RasterPixelIsArea) {
            return PixelInCell.CELL_CORNER;
        } else if (rasterType == GeoTiffConstants.RasterPixelIsPoint) {
            return PixelInCell.CELL_CENTER;
        }
        throw new IllegalArgumentException("Unsupported rasterType");
    }

    /**
     * Getting a specified geotiff geo key as a double. It is somehow tolerant in the sense that in case such a key does
     * not exist it returns <code>Double.NaN</code>.
     *
     * @param key we want to get the value for.
     * @param metadata containing the key we are looking for.
     * @return the value for the provided key.
     */
    private static double getGeoKeyAsDouble(final int key, final GeoTiffIIOMetadataDecoder metadata) {

        try {
            final String geoKey = metadata.getGeoKey(key);
            if (geoKey != null) return Double.parseDouble(geoKey);
            else return Double.NaN;
        } catch (NumberFormatException ne) {
            if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, ne.getLocalizedMessage(), ne);
            return Double.NaN;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return Double.NaN;
        }
    }

    /**
     * We have a user defined {@link ProjectedCRS}, let's try to parse it.
     *
     * @param linearUnit is the UoM that this {@link ProjectedCRS} will use. It could be null.
     * @return a user-defined {@link ProjectedCRS}.
     */
    private ProjectedCRS createUserDefinedPCS(final GeoTiffIIOMetadataDecoder metadata, Unit<?> linearUnit)
            throws Exception {

        //
        // At the top level a user-defined PCRS is made by
        // <ol>
        // <li>PCSCitationGeoKey (NAME)
        // <li>ProjectionGeoKey
        // <li>GeographicTypeGeoKey
        // </ol>
        //
        //

        //
        // GEOGRAPHIC CRS is the baseCRS for the projection
        //
        final GeographicCRS baseCRS = createGeographicCoordinateReferenceSystem(metadata);

        //
        // NAME of the user defined projected coordinate reference system.
        //
        String pcsCitationGeoKey = metadata.getGeoKey(GeoTiffPCSCodes.PCSCitationGeoKey);
        String projectedCrsName = pcsCitationGeoKey;
        if (projectedCrsName == null) {
            projectedCrsName = UNNAMED.intern();
        } else {
            projectedCrsName = cleanName(projectedCrsName);
        }

        //
        // PROJECTION geo key for this projected coordinate reference system.
        // get the projection code for this PCRS to build it from the GCS.
        //
        // In case i is user defined it requires:
        // PCSCitationGeoKey
        // ProjCoordTransGeoKey
        // ProjLinearUnitsGeoKey
        //
        final String projCode = metadata.getGeoKey(GeoTiffPCSCodes.ProjectionGeoKey);
        boolean projUserDefined = false;
        if (projCode == null || projCode.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            projUserDefined = true;
        }

        // is it user defined?
        final Conversion conversionFromBase;
        String projectionName = null;
        final ParameterValueGroup parameters;
        final MathTransform transform;
        if (projUserDefined) {
            String citationName = metadata.getGeoKey(GeoTiffGCSCodes.GTCitationGeoKey);
            if ((projectedCrsName == null || UNNAMED.equalsIgnoreCase(projectedCrsName)) && citationName != null) {
                // Fallback on GTCitation
                projectedCrsName = citationName;
            }
            // A user defined projection is made up by
            // <ol>
            // <li>PCSCitationGeoKey (NAME)
            // <li>ProjCoordTransGeoKey
            // <li>ProjLinearUnitsGeoKey
            // </ol>
            // NAME of this projection coordinate transformation
            // getting user defined parameters
            projectionName = pcsCitationGeoKey;
            if (projectionName == null) {
                // Fall back on citation
                projectionName = citationName != null ? citationName : UNNAMED;
            }

            // //
            //
            // getting default parameters for this projection and filling them
            // with the values found
            // inside the geokeys list.
            //
            // //
            parameters = createUserDefinedProjectionParameter(projectionName, metadata);
            if (parameters == null) {
                throw new GeoTiffException(
                        metadata,
                        "GeoTiffMetadata2CRSAdapter::createUserDefinedPCS:Projection is not supported.",
                        null);
            }
            // set the remaining parameters.
            refineParameters(baseCRS, parameters);

            // create math transform
            transform = mtFactory.createParameterizedTransform(parameters);

            // create the conversion from base, aka the projection transform
            conversionFromBase = new DefiningConversion(
                    Collections.singletonMap("name", cleanName(projectionName)),
                    new DefaultOperationMethod(transform),
                    transform);
        } else {
            // create the conversion from EPSG code
            conversionFromBase = (Conversion) this.allAuthoritiesFactory.createCoordinateOperation(
                    new StringBuilder("EPSG:").append(projCode).toString());

            // prepare the parameters
            parameters = conversionFromBase.getParameterValues();

            refineParameters(baseCRS, parameters);

            // create math transform
            transform = mtFactory.createParameterizedTransform(parameters);
        }

        //
        // PROJECTED CRS
        //
        // //
        //
        // I am putting particular attention on the management of the unit
        // of measure since it seems that very often people change the unit
        // of measure to feet even if the standard UoM for the request
        // projection is M.
        //
        // ///
        if (projUserDefined) {

            // user defined projection

            // standard unit of measure
            if (linearUnit != null && linearUnit.equals(SI.METRE)) {
                return new DefaultProjectedCRS(
                        java.util.Collections.singletonMap("name", projectedCrsName),
                        conversionFromBase,
                        baseCRS,
                        transform,
                        DefaultCartesianCS.PROJECTED);
            }

            // unit of measure is not standard
            return new DefaultProjectedCRS(
                    java.util.Collections.singletonMap("name", projectedCrsName),
                    conversionFromBase,
                    baseCRS,
                    transform,
                    DefaultCartesianCS.PROJECTED.usingUnit(linearUnit));
        }

        // standard projection
        if (linearUnit != null && !linearUnit.equals(SI.METRE)) {
            return new DefaultProjectedCRS(
                    Collections.singletonMap("name", projectedCrsName),
                    conversionFromBase,
                    baseCRS,
                    transform,
                    DefaultCartesianCS.PROJECTED.usingUnit(linearUnit));
        }
        return new DefaultProjectedCRS(
                Collections.singletonMap("name", projectedCrsName),
                conversionFromBase,
                baseCRS,
                transform,
                DefaultCartesianCS.PROJECTED);
    }

    /** */
    private void refineParameters(final GeographicCRS baseCRS, final ParameterValueGroup parameters)
            throws InvalidParameterValueException, ParameterNotFoundException {
        // set the remaining parameters.
        final GeodeticDatum tempDatum = baseCRS.getDatum();
        final DefaultEllipsoid tempEll = (DefaultEllipsoid) tempDatum.getEllipsoid();
        double inverseFlattening = tempEll.getInverseFlattening();
        double semiMajorAxis = tempEll.getSemiMajorAxis();
        // setting missing parameters
        parameters.parameter("semi_minor").setValue(semiMajorAxis * (1 - 1 / inverseFlattening));
        parameters.parameter("semi_major").setValue(semiMajorAxis);
    }

    /**
     * Clean the provided parameters <code>tiffName</code> from strange strings like it happens with erdas imagine.
     *
     * @param tiffName is the {@link String} to clean up.
     * @return a cleaned up {@link String}.
     */
    private static final String cleanName(String tiffName) {
        // look for strange chars
        // $
        int index = tiffName.lastIndexOf('$');
        if (index != -1) tiffName = tiffName.substring(index + 1);
        // \n
        index = tiffName.lastIndexOf('\n');
        if (index != -1) tiffName = tiffName.substring(index + 1);
        // \r
        index = tiffName.lastIndexOf('\r');
        if (index != -1) tiffName = tiffName.substring(index + 1);
        return tiffName;
    }

    /**
     * Creates a {@link CartesianCS} for a {@link ProjectedCRS} given the provided {@link Unit}.
     *
     * @todo consider caching this items
     * @param linearUnit to be used for building this {@link CartesianCS}.
     * @return an instance of {@link CartesianCS} using the provided {@link Unit},
     */
    private DefaultCartesianCS createProjectedCS(Unit<?> linearUnit) {
        if (linearUnit == null)
            throw new NullPointerException("Error when trying to create a PCS using this linear UoM ");
        if (!linearUnit.isCompatible(SI.METRE))
            throw new IllegalArgumentException(
                    "Error when trying to create a PCS using this linear UoM " + linearUnit.toString());
        final Map<String, String> props = new HashMap<>();
        props.put(
                "name", Vocabulary.formatInternational(VocabularyKeys.PROJECTED).toString());
        props.put(
                "alias",
                Vocabulary.formatInternational(VocabularyKeys.PROJECTED).toString());
        return new DefaultCartesianCS(
                props,
                new DefaultCoordinateSystemAxis(
                        Vocabulary.formatInternational(VocabularyKeys.EASTING), "E", AxisDirection.EAST, linearUnit),
                new DefaultCoordinateSystemAxis(
                        Vocabulary.formatInternational(VocabularyKeys.NORTHING), "N", AxisDirection.NORTH, linearUnit));
    }

    /**
     * Creating a prime meridian for the gcs we are creating at an higher level. As usual this method tries to follow
     * the geotiff specification.
     *
     * @param angularUnit to use for building this {@link PrimeMeridian}.
     * @return a {@link PrimeMeridian} built using the provided {@link Unit} and the provided metadata.
     */
    private PrimeMeridian createPrimeMeridian(final GeoTiffIIOMetadataDecoder metadata, Unit angularUnit)
            throws IOException {
        // look up the prime meridian:
        // + could be an EPSG code
        // + could be user defined
        // + not defined = greenwich
        final String pmCode = metadata.getGeoKey(GeoTiffGCSCodes.GeogPrimeMeridianGeoKey);
        final String customPrimeMeridian = Optional.ofNullable(metadata.getGeographicCitation())
                .map(c -> c.getPrimem())
                .orElse(null);
        PrimeMeridian pm = null;

        try {
            if (GeoTiffConstants.GTUserDefinedGeoKey_String.equals(pmCode) || customPrimeMeridian != null) {
                double pmNumeric = Optional.ofNullable(metadata.getGeoKey(GeoTiffGCSCodes.GeogPrimeMeridianLongGeoKey))
                        .map(Double::parseDouble)
                        .orElse(0d);
                String name = Optional.ofNullable(customPrimeMeridian).orElse(UNNAMED);
                final Map<String, String> props = new HashMap<>();
                props.put("name", name);
                @SuppressWarnings("unchecked")
                Unit<Angle> angleUnit = angularUnit;
                pm = datumObjFactory.createPrimeMeridian(props, pmNumeric, angleUnit);
            } else if (pmCode == null) {
                pm = DefaultPrimeMeridian.GREENWICH;
            } else {
                pm = this.allAuthoritiesFactory.createPrimeMeridian("EPSG:" + pmCode);
            }
        } catch (FactoryException fe) {
            final IOException io = new GeoTiffException(metadata, fe.getLocalizedMessage(), fe);
            throw io;
        }

        return pm;
    }

    /**
     * Looks up the Geodetic Datum as specified in the GeoTIFFWritingUtilities file. The geotools definition of the
     * geodetic datum includes both an ellipsoid and a prime meridian, but the code in the GeoTIFFWritingUtilities file
     * does NOT include the prime meridian, as it is specified separately. This code currently does not support user
     * defined datum.
     *
     * @param unit to use for building this {@link GeodeticDatum}.
     * @return a {@link GeodeticDatum}.
     */
    private GeodeticDatum createGeodeticDatum(
            final Unit<?> unit, final Unit<?> angularUnit, final GeoTiffIIOMetadataDecoder metadata)
            throws IOException {
        // lookup the datum (w/o PrimeMeridian), error if "user defined"
        GeodeticDatum datum = null;
        final String datumCode = metadata.getGeoKey(GeoTiffGCSCodes.GeogGeodeticDatumGeoKey);

        if (datumCode == null) {
            throw new GeoTiffException(
                    metadata,
                    "GeoTiffMetadata2CRSAdapter::createGeodeticDatum(Unit unit):A user defined Geographic Coordinate system must include a predefined datum!",
                    null);
        }

        if (datumCode.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            /**
             * USER DEFINED DATUM
             *
             * <p>From the spec:
             *
             * <p>GeoKey Requirements for User-Defined Horizontal Datum: GeogCitationGeoKey GeogEllipsoidGeoKey
             */
            // datum name
            String name = Optional.ofNullable(metadata.getGeographicCitation())
                    .map(c -> c.getDatum())
                    .orElse(UNNAMED);

            // is it WGS84?
            if (name.trim().equalsIgnoreCase("WGS84")) {
                return DefaultGeodeticDatum.WGS84;
            }

            // ELLIPSOID
            final Ellipsoid ellipsoid = createEllipsoid(unit, metadata);

            // PRIME MERIDIAN
            // lookup the Prime Meridian.
            final PrimeMeridian primeMeridian = createPrimeMeridian(metadata, angularUnit);

            // DATUM
            datum = new DefaultGeodeticDatum(cleanName(name), ellipsoid, primeMeridian);
        } else {
            /** NOT USER DEFINED DATUM */

            // we are going to use the provided EPSG code
            try {
                datum = (GeodeticDatum) this.allAuthoritiesFactory.createDatum("EPSG:" + datumCode);
            } catch (Exception cce) {
                final GeoTiffException ex = new GeoTiffException(metadata, cce.getLocalizedMessage(), cce);
                throw ex;
            }
        }

        return datum;
    }

    /**
     * Creating an ellipsoid following the GeoTiff spec.
     *
     * @param unit to build this {@link Ellipsoid}..
     * @return an {@link Ellipsoid}.
     */
    private Ellipsoid createEllipsoid(final Unit unit, final GeoTiffIIOMetadataDecoder metadata)
            throws GeoTiffException {
        // /////////////////////////////////////////////////////////////////////
        //
        // Getting the ellipsoid key in order to understand if we are working
        // against a common ellipsoid or a user defined one.
        //
        // /////////////////////////////////////////////////////////////////////
        // ellipsoid key
        final String ellipsoidKey = metadata.getGeoKey(GeoTiffGCSCodes.GeogEllipsoidGeoKey);
        String temp = null;
        // is the ellipsoid user defined?
        if (ellipsoidKey.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            //
            // USER DEFINED ELLIPSOID
            //
            String name = Optional.ofNullable(metadata.getGeographicCitation())
                    .map(c -> c.getEllipsoid())
                    .map(n -> cleanName(n))
                    .orElse(UNNAMED);
            // is it the default for WGS84?
            if (name.trim().equalsIgnoreCase("WGS84")) {
                return DefaultEllipsoid.WGS84;
            }

            // //
            //
            // It is worth to point out that I ALWAYS use the inverse flattening
            // along with the semi-major axis to builde the Flattened Sphere.
            // This
            // has to be done in order to comply with the opposite process of
            // going from CRS to metadata where this coupls is always used.
            //
            // //
            // getting temporary parameters
            temp = metadata.getGeoKey(GeoTiffGCSCodes.GeogSemiMajorAxisGeoKey);
            final double semiMajorAxis = temp != null ? Double.parseDouble(temp) : Double.NaN;
            temp = metadata.getGeoKey(GeoTiffGCSCodes.GeogInvFlatteningGeoKey);
            final double inverseFlattening;
            if (temp != null) {
                inverseFlattening = temp != null ? Double.parseDouble(temp) : Double.NaN;
            } else {
                temp = metadata.getGeoKey(GeoTiffGCSCodes.GeogSemiMinorAxisGeoKey);
                final double semiMinorAxis = temp != null ? Double.parseDouble(temp) : Double.NaN;
                inverseFlattening = semiMajorAxis / (semiMajorAxis - semiMinorAxis);
            }
            // look for the Ellipsoid first then build the datum
            @SuppressWarnings("unchecked")
            Unit<Length> lengthUnit = unit;
            return DefaultEllipsoid.createFlattenedSphere(name, semiMajorAxis, inverseFlattening, lengthUnit);
        }

        try {
            //
            // EPSG STANDARD ELLIPSOID
            //
            return this.allAuthoritiesFactory.createEllipsoid(
                    new StringBuilder("EPSG:").append(ellipsoidKey).toString());
        } catch (FactoryException fe) {
            final GeoTiffException ex = new GeoTiffException(metadata, fe.getLocalizedMessage(), fe);
            throw ex;
        }
    }

    /**
     * The GeoTIFFWritingUtilities spec requires that a user defined GCS be comprised of the following:
     *
     * <ul>
     *   <li>a citation
     *   <li>a datum definition
     *   <li>a prime meridian definition (if not Greenwich)
     *   <li>an angular unit definition (if not degrees)
     * </ul>
     *
     * @param metadata to use for building this {@link GeographicCRS}.
     * @return a {@link GeographicCRS}.
     */
    private GeographicCRS createUserDefinedGCS(
            final GeoTiffIIOMetadataDecoder metadata, Unit<?> linearUnit, Unit<?> angularUnit) throws IOException {
        // //
        //
        // coordinate reference system name (GeogCitationGeoKey)
        //
        // //
        String name = Optional.ofNullable(metadata.getGeographicCitation())
                .map(c -> c.getGcsName())
                .map(n -> cleanName(n))
                .orElse(UNNAMED);

        // lookup the Geodetic datum
        final GeodeticDatum datum = createGeodeticDatum(linearUnit, angularUnit, metadata);

        // coordinate reference system
        // property map is reused
        final Map<String, String> props = new HashMap<>();
        // make the user defined GCS from all the components...
        props.put("name", name);
        return new DefaultGeographicCRS(props, datum, DefaultEllipsoidalCS.GEODETIC_2D.usingUnit(angularUnit));
    }

    /**
     * @todo we should somehow try to to support user defined coordinate transformation even if for the moment is not so
     *     clear to me how we could achieve that since if we have no clue about the coordinate transform what we are
     *     supposed to do in order to build a conversion, guess it? How could we pick up the parameters, should look for
     *     all and then guess the right transformation?
     * @param name indicates the name for the projection.
     * @param metadata to use for building this {@link ParameterValueGroup}.
     * @return a {@link ParameterValueGroup} that can be used to trigger this projection.
     */
    private ParameterValueGroup createUserDefinedProjectionParameter(
            String name, final GeoTiffIIOMetadataDecoder metadata) throws IOException, FactoryException {
        // //
        //
        // Trying to get the name for the coordinate transformation involved.
        //
        // ///
        final String coordTrans = metadata.getGeoKey(GeoTiffPCSCodes.ProjCoordTransGeoKey);

        // throw descriptive exception if ProjCoordTransGeoKey not defined
        if (coordTrans == null || coordTrans.equalsIgnoreCase(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            throw new GeoTiffException(
                    metadata,
                    "GeoTiffMetadata2CRSAdapter::createUserDefinedProjectionParameter(String name):User defined projections must specify"
                            + " coordinate transformation code in ProjCoordTransGeoKey",
                    null);
        }

        // getting math transform factory
        return setParametersForProjection(name, coordTrans, metadata);
    }

    /**
     * Set the projection parameters basing its decision on the projection name. I found a complete list of projections
     * on the geotiff website at address http://www.remotesensing.org/geotiff/proj_list.
     *
     * <p>I had no time to implement support for all of them therefore you will not find all of them. If you want go
     * ahead and add support for the missing ones. I have tested this code against some geotiff files you can find on
     * the geotiff website under the ftp sample directory but I can say that they are a real mess! I am respecting the
     * specification strictly while many of those fields do not! I could make this method trickier and use workarounds
     * in order to be less strict but I will not do this, since I believe it is may lead us just on a very dangerous
     * path.
     *
     * @param metadata to use fo building this {@link ParameterValueGroup}.
     */
    private ParameterValueGroup setParametersForProjection(
            String name, final String coordTransCode, final GeoTiffIIOMetadataDecoder metadata)
            throws GeoTiffException {
        ParameterValueGroup parameters = null;
        try {
            int code = 0;
            if (coordTransCode != null) code = Integer.parseInt(coordTransCode);
            if (name == null) name = UNNAMED;
            /** Transverse Mercator */
            if (name.equalsIgnoreCase("transverse_mercator")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_TransverseMercator) {
                parameters = mtFactory.getDefaultParameters("transverse_mercator");
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters
                        .parameter("scale_factor")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Equidistant Cylindrical - Plate Caree - Equirectangular */
            if (name.equalsIgnoreCase("Equidistant_Cylindrical")
                    || name.equalsIgnoreCase("Plate_Carree")
                    || name.equalsIgnoreCase("Equidistant_Cylindrical")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_Equirectangular) {
                parameters = mtFactory.getDefaultParameters("Equidistant_Cylindrical");
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));

                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }
            /** Mercator_1SP Mercator_2SP */
            if (name.equalsIgnoreCase("mercator_1SP")
                    || name.equalsIgnoreCase("Mercator_2SP")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_Mercator) {

                final double standard_parallel_1 = getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel1GeoKey, metadata);
                boolean isMercator2SP = false;
                if (!Double.isNaN(standard_parallel_1)) {
                    parameters = mtFactory.getDefaultParameters("Mercator_2SP");
                    isMercator2SP = true;
                } else parameters = mtFactory.getDefaultParameters("Mercator_1SP");

                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));
                if (isMercator2SP) parameters.parameter("standard_parallel_1").setValue(standard_parallel_1);
                else parameters.parameter("scale_factor").setValue(getScaleFactor(metadata));
                return parameters;
            }

            /** Lambert_conformal_conic_1SP */
            if (name.equalsIgnoreCase("lambert_conformal_conic_1SP")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_LambertConfConic_Helmert) {
                parameters = mtFactory.getDefaultParameters("lambert_conformal_conic_1SP");
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters
                        .parameter("scale_factor")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Lambert_conformal_conic_2SP */
            if (name.equalsIgnoreCase("lambert_conformal_conic_2SP")
                    || name.equalsIgnoreCase("lambert_conformal_conic_2SP_Belgium")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_LambertConfConic_2SP) {
                parameters = mtFactory.getDefaultParameters("lambert_conformal_conic_2SP");
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters
                        .parameter("standard_parallel_1")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel1GeoKey, metadata));
                parameters
                        .parameter("standard_parallel_2")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel2GeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Krovak */
            if (name.equalsIgnoreCase("Krovak")) {
                parameters = mtFactory.getDefaultParameters("Krovak");
                parameters.parameter("longitude_of_center").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_center").setValue(getOriginLat(metadata));
                parameters
                        .parameter("azimuth")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel1GeoKey, metadata));
                parameters
                        .parameter("pseudo_standard_parallel_1")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel2GeoKey, metadata));
                parameters.parameter("scale_factor").setValue(getFalseEasting(metadata));

                return parameters;
            }

            // if (name.equalsIgnoreCase("equidistant_conic")
            // || code == GeoTiffMetadata2CRSAdapter.CT_EquidistantConic) {
            // parameters = mtFactory
            // .getDefaultParameters("equidistant_conic");
            // parameters.parameter("central_meridian").setValue(
            // getOriginLong());
            // parameters.parameter("latitude_of_origin").setValue(
            // getOriginLat());
            // parameters
            // .parameter("standard_parallel_1")
            // .setValue(
            // this
            // .getGeoKeyAsDouble(GeoTiffIIOMetadataDecoder.ProjStdParallel1GeoKey));
            // parameters
            // .parameter("standard_parallel_2")
            // .setValue(
            // this
            // .getGeoKeyAsDouble(GeoTiffIIOMetadataDecoder.ProjStdParallel2GeoKey));
            // parameters.parameter("false_easting").setValue(
            // getFalseEasting());
            // parameters.parameter("false_northing").setValue(
            // getFalseNorthing());
            //
            // return parameters;
            // }

            /** STEREOGRAPHIC */
            if (name.equalsIgnoreCase("stereographic")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_Stereographic) {
                parameters = mtFactory.getDefaultParameters("stereographic");
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));

                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));

                parameters
                        .parameter("scale_factor")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** POLAR_STEREOGRAPHIC. */
            if (name.equalsIgnoreCase("polar_stereographic")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_PolarStereographic) {
                parameters = mtFactory.getDefaultParameters("polar_stereographic");

                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters
                        .parameter("scale_factor")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));

                return parameters;
            }

            /** Oblique Stereographic */
            if (name.equalsIgnoreCase("oblique_stereographic")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_ObliqueStereographic) {
                parameters = mtFactory.getDefaultParameters("Oblique_Stereographic");

                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters
                        .parameter("scale_factor")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));
                return parameters;
            }

            /** OBLIQUE_MERCATOR. */
            if (name.equalsIgnoreCase("oblique_mercator")
                    || name.equalsIgnoreCase("hotine_oblique_mercator")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_ObliqueMercator) {
                parameters = mtFactory.getDefaultParameters("oblique_mercator");

                parameters.parameter("scale_factor").setValue(getScaleFactor(metadata));
                parameters
                        .parameter("azimuth")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjAzimuthAngleGeoKey, metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));
                parameters.parameter("longitude_of_center").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_center").setValue(getOriginLat(metadata));
                return parameters;
            }

            /** albers_Conic_Equal_Area */
            if (name.equalsIgnoreCase("albers_Conic_Equal_Area")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_AlbersEqualArea) {
                parameters = mtFactory.getDefaultParameters("Albers_Conic_Equal_Area");
                parameters
                        .parameter("standard_parallel_1")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel1GeoKey, metadata));
                parameters
                        .parameter("standard_parallel_2")
                        .setValue(getGeoKeyAsDouble(GeoTiffPCSCodes.ProjStdParallel2GeoKey, metadata));
                parameters.parameter("latitude_of_center").setValue(getOriginLat(metadata));
                parameters.parameter("longitude_of_center").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Orthographic */
            if (name.equalsIgnoreCase("Orthographic")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_Orthographic) {
                parameters = mtFactory.getDefaultParameters("orthographic");

                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters.parameter("longitude_of_origin").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Lambert Azimuthal Equal Area */
            if (name.equalsIgnoreCase("Lambert_Azimuthal_Equal_Area")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_LambertAzimEqualArea) {
                parameters = mtFactory.getDefaultParameters("Lambert_Azimuthal_Equal_Area");

                parameters.parameter("latitude_of_center").setValue(getOriginLat(metadata));
                parameters.parameter("longitude_of_center").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Azimuthal Equidistant */
            if (name.equalsIgnoreCase("Azimuthal_Equidistant")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_AzimuthalEquidistant) {
                parameters = mtFactory.getDefaultParameters("Azimuthal_Equidistant");
                parameters.parameter("longitude_of_center").setValue(getOriginLong(metadata));
                parameters.parameter("latitude_of_center").setValue(getOriginLat(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));
                return parameters;
            }

            /** New Zealand Map Grid */
            if (name.equalsIgnoreCase("New_Zealand_Map_Grid")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_NewZealandMapGrid) {
                parameters = mtFactory.getDefaultParameters("New_Zealand_Map_Grid");

                parameters.parameter("latitude_of_origin").setValue(getOriginLat(metadata));
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** World Van der Grinten I */
            if (name.equalsIgnoreCase("World_Van_der_Grinten_I")
                    || code == GeoTiffCoordinateTransformationsCodes.CT_VanDerGrinten) {
                parameters = mtFactory.getDefaultParameters("World_Van_der_Grinten_I");

                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }

            /** Sinusoidal */
            if (name.equalsIgnoreCase("Sinusoidal") || code == GeoTiffCoordinateTransformationsCodes.CT_Sinusoidal) {
                parameters = mtFactory.getDefaultParameters("Sinusoidal");
                parameters.parameter("central_meridian").setValue(getOriginLong(metadata));
                parameters.parameter("false_easting").setValue(getFalseEasting(metadata));
                parameters.parameter("false_northing").setValue(getFalseNorthing(metadata));

                return parameters;
            }
        } catch (NoSuchIdentifierException e) {
            throw new GeoTiffException(metadata, e.getLocalizedMessage(), e);
        }

        return parameters;
    }

    /**
     * Retrieve the scale factor parameter as defined by the geotiff specification.
     *
     * @param metadata to use for searching the scale factor.
     * @return the scale factor
     */
    private static double getScaleFactor(final GeoTiffIIOMetadataDecoder metadata) {
        String scale = metadata.getGeoKey(GeoTiffPCSCodes.ProjScaleAtCenterGeoKey);
        if (scale == null) scale = metadata.getGeoKey(GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey);
        if (scale == null) return 1.0;
        return Double.parseDouble(scale);
    }

    /**
     * Getting the false easting with a minimum of tolerance with respect to the parameters name. I saw that often
     * people use the wrong geokey to store the false easting, we cannot be too picky we need to get going pretty
     * smoothly.
     *
     * @param metadata to use for searching the false easting.
     * @return double False easting.
     */
    private static double getFalseEasting(final GeoTiffIIOMetadataDecoder metadata) {
        String easting = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseEastingGeoKey);
        if (easting == null) easting = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseOriginEastingGeoKey);
        if (easting == null) return 0.0;
        return Double.parseDouble(easting);
    }

    /**
     * Getting the false northing with a minimum of tolerance with respect to the parameters name. I saw that often
     * people use the wrong geokey to store the false easting, we cannot be too picky we need to get going pretty
     * smoothly.
     *
     * @param metadata to use for searching the false northing.
     * @return double False northing.
     */
    private static double getFalseNorthing(final GeoTiffIIOMetadataDecoder metadata) {
        String northing = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseNorthingGeoKey);
        if (northing == null) northing = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseOriginNorthingGeoKey);
        if (northing == null) return 0.0;
        return Double.parseDouble(northing);
    }

    /**
     * Getting the origin long with a minimum of tolerance with respect to the parameters name. I saw that often people
     * use the wrong geokey to store the false easting, we cannot be too picky we need to get going pretty smoothly.
     *
     * @param metadata to use for searching the originating longitude.
     * @return double origin longitude.
     */
    private static double getOriginLong(final GeoTiffIIOMetadataDecoder metadata) {
        String origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjCenterLongGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjNatOriginLongGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseOriginLongGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjStraightVertPoleLongGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseNorthingGeoKey);
        if (origin == null) return 0.0;
        return Double.parseDouble(origin);
    }

    /**
     * Getting the origin lat with a minimum of tolerance with respect to the parameters name. I saw that often people
     * use the wrong geokey to store the false easting, we cannot be too picky we need to get going pretty smoothly.
     *
     * @param metadata to use for searching the origin latitude.
     * @return double origin latitude.
     */
    private static double getOriginLat(final GeoTiffIIOMetadataDecoder metadata) {
        String origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjCenterLatGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjNatOriginLatGeoKey);
        if (origin == null) origin = metadata.getGeoKey(GeoTiffPCSCodes.ProjFalseOriginLatGeoKey);
        if (origin == null) return 0.0;

        return Double.parseDouble(origin);
    }

    /**
     * This code creates an <code>javax.measure.Unit</code> object out of the <code>
     * ProjLinearUnitsGeoKey</code> and the <code>ProjLinearUnitSizeGeoKey</code>. The unit may either be specified as a
     * standard EPSG recognized unit, or may be user defined.
     *
     * @return <code>Unit</code> object representative of the tags in the file.
     * @throws IOException if the<code>ProjLinearUnitsGeoKey</code> is not specified or if unit is user defined and
     *     <code>ProjLinearUnitSizeGeoKey</code> is either not defined or does not contain a number.
     */
    private <Q extends Quantity<Q>> Unit<Q> createUnit(
            int key, int userDefinedKey, Unit<Q> base, Unit<Q> def, final GeoTiffIIOMetadataDecoder metadata)
            throws IOException {
        final String unitCode = metadata.getGeoKey(key);

        // //
        //
        // if not defined, return the default unit of measure
        //
        // //
        if (unitCode == null) {
            return def;
        }
        // //
        //
        // if specified, retrieve the appropriate unit code. There are two case
        // to keep into account, first case is when the unit of measure has an
        // EPSG code, alternatively it can be instantiated as a conversion from
        // meter.
        //
        // //
        if (unitCode.equals(GeoTiffConstants.GTUserDefinedGeoKey_String)) {
            try {
                final String unitSize = metadata.getGeoKey(userDefinedKey);

                // throw descriptive exception if required key is not there.
                if (unitSize == null) {
                    throw new GeoTiffException(
                            metadata,
                            "GeoTiffMetadata2CRSAdapter::createUnit:Must define unit length when using a user defined unit",
                            null);
                }

                double sz = Double.parseDouble(unitSize);
                Unit<Q> factor = base.multiply(sz);

                return Units.autoCorrect(factor);
            } catch (NumberFormatException nfe) {
                final IOException ioe = new GeoTiffException(metadata, nfe.getLocalizedMessage(), nfe);
                throw ioe;
            }
        } else {
            try {
                // using epsg code for this unit
                @SuppressWarnings("unchecked")
                Unit<Q> result = (Unit<Q>) this.allAuthoritiesFactory.createUnit("EPSG:" + unitCode);
                return result;
            } catch (FactoryException fe) {
                final IOException io = new GeoTiffException(metadata, fe.getLocalizedMessage(), fe);
                throw io;
            }
        }
    }
}
