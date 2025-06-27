/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.ReadType;
import org.geotools.coverage.io.SpatialRequestHelper;
import org.geotools.coverage.io.SpatialRequestHelper.CoverageProperties;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities.ParameterBehaviour;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;

/**
 * A class to handle coverage requests to a reader for a single 2D layer..
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
class NetCDFRequest extends CoverageReadRequest {

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(NetCDFRequest.class);

    /** The Interpolation required to serve this request */
    // TODO: CUSTOMIZE INTERPOLATION request.getInterpolation();
    Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);

    NetCDFSource source;

    // on NetCDF adopt the direct read: see the google document (Which one? it seems it has been
    // deleted)
    ReadType readType = ReadType.DIRECT_READ;

    SpatialRequestHelper spatialRequestHelper;

    CoverageReadRequest originalRequest = null;

    String name = null;

    /** Build a new {@code CoverageRequest} given a set of input parameters. */
    public NetCDFRequest(NetCDFSource source, CoverageReadRequest request) throws IOException {
        this.source = source;
        this.originalRequest = request;
        name = source.getName(null).toString();
        // //
        //
        // Checking the request and filling the missing fields
        //
        // //
        checkRequest(request);
        this.spatialRequestHelper = new SpatialRequestHelper();

        BoundingBox requestedBBox = request.getGeographicArea();
        Rectangle requestedRasterArea = request.getRasterArea();
        MathTransform2D requestedG2W = request.getGridToWorldTransform();
        spatialRequestHelper.setRequestedBBox(requestedBBox);
        spatialRequestHelper.setRequestedRasterArea(requestedRasterArea);
        spatialRequestHelper.setRequestedGridToWorld((AffineTransform) requestedG2W);

        // initialize
        initInputCoverageProperties();
    }

    /** Initialize coverage input properties by collecting them from a {@link CoverageSourceWrapper} */
    private void initInputCoverageProperties() throws IOException {
        VariableAdapter.UnidataSpatialDomain spatialDomain =
                (org.geotools.imageio.netcdf.VariableAdapter.UnidataSpatialDomain) source.getSpatialDomain();

        // Getting spatial context
        final Set<? extends RasterLayout> rasterElements = spatialDomain.getRasterElements(false, null);
        final GridGeometry2D gridGeometry2D = spatialDomain.getGridGeometry();
        final AffineTransform gridToCRS = (AffineTransform) gridGeometry2D.getGridToCRS();
        final double[] coverageFullResolution = CoverageUtilities.getResolution(gridToCRS);
        final MathTransform raster2Model = gridGeometry2D.getGridToCRS();
        final ReferencedEnvelope bbox = spatialDomain.getReferencedEnvelope();
        final ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(bbox);
        final CoordinateReferenceSystem spatialReferenceSystem2D = spatialDomain.getCoordinateReferenceSystem2D();
        rasterArea = rasterElements.iterator().next().toRectangle();

        // Setting up Coverage info
        final CoverageProperties properties = new CoverageProperties();
        properties.setCrs2D(spatialReferenceSystem2D);
        properties.setFullResolution(coverageFullResolution);
        properties.setBbox(referencedEnvelope);

        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(bbox);
        try {
            wgs84Envelope = wgs84Envelope.transform(DefaultGeographicCRS.WGS84, true);
        } catch (TransformException | FactoryException e) {
            IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        }

        properties.setGeographicBBox(wgs84Envelope);
        properties.setGeographicCRS2D(wgs84Envelope.getCoordinateReferenceSystem());
        properties.setGridToWorld2D((MathTransform2D) raster2Model);
        properties.setRasterArea(rasterArea);
        spatialRequestHelper.setCoverageProperties(properties);
        spatialRequestHelper.prepare();
    }

    private void checkRequest(CoverageReadRequest request) throws IOException {
        BoundingBox requestedBoundingBox = request.getGeographicArea();

        // //
        //
        // Checking RequestedRasterArea setting
        //
        // //
        Rectangle requestedRasterArea = request.getRasterArea();
        VariableAdapter.UnidataSpatialDomain horizontalDomain =
                (VariableAdapter.UnidataSpatialDomain) source.getSpatialDomain();
        VariableAdapter.UnidataTemporalDomain temporalDomain =
                (VariableAdapter.UnidataTemporalDomain) source.getTemporalDomain();
        VariableAdapter.UnidataVerticalDomain verticalDomain =
                (VariableAdapter.UnidataVerticalDomain) source.getVerticalDomain();

        if (requestedRasterArea == null || requestedBoundingBox == null) {
            boolean bothNull = true;
            if (requestedRasterArea == null) {
                requestedRasterArea =
                        horizontalDomain.getGridGeometry().getGridRange2D().getBounds();
            } else {
                bothNull = false;
            }
            if (requestedBoundingBox == null) {
                requestedBoundingBox = horizontalDomain.getReferencedEnvelope();

            } else {
                bothNull = false;
            }

            if (bothNull) {
                try {
                    request.setDomainSubset(
                            requestedRasterArea,
                            horizontalDomain.getGridGeometry().getGridToCRS2D(),
                            horizontalDomain.getCoordinateReferenceSystem2D());
                } catch (TransformException e) {
                    request.setDomainSubset(requestedRasterArea, ReferencedEnvelope.reference(requestedBoundingBox));
                    LOGGER.log(
                            Level.SEVERE,
                            "Transform exception while setting the domain subset to: " + requestedRasterArea,
                            e);
                }
            } else {
                // TODO: Check for center/corner anchor point
                request.setDomainSubset(requestedRasterArea, ReferencedEnvelope.reference(requestedBoundingBox));
            }
        }

        // //
        //
        // Checking TemporalSubset setting
        //
        // //
        SortedSet<DateRange> temporalSubset = request.getTemporalSubset();
        if (temporalDomain != null) {
            if (temporalSubset.isEmpty()
                    && NetCDFUtilities.getParameterBehaviour(NetCDFUtilities.TIME_DIM)
                            == ParameterBehaviour.DO_NOTHING) {
                Set<DateRange> temporalExtent = temporalDomain.getTemporalExtent();
                if (temporalExtent != null) {
                    temporalSubset = new DateRangeTreeSet(temporalExtent);
                }
                request.setTemporalSubset(temporalSubset);
            }
        }

        // //
        //
        // Checking VerticalSubset setting
        //
        // //
        Set<NumberRange<Double>> verticalSubset = request.getVerticalSubset();
        if (verticalDomain != null) {
            Set<NumberRange<Double>> verticalExtent = verticalDomain.getVerticalExtent();
            if (verticalSubset.isEmpty()
                    && NetCDFUtilities.getParameterBehaviour(NetCDFUtilities.ELEVATION_DIM)
                            == ParameterBehaviour.DO_NOTHING) {
                if (verticalExtent != null) {
                    verticalSubset = new HashSet<>(verticalExtent);
                }
                request.setVerticalSubset(verticalSubset);
            } else {
                final NumberRange<Double> requestedVerticalEnv =
                        verticalSubset.iterator().next();

                if (verticalExtent != null
                        && !verticalExtent.isEmpty()
                        && !verticalExtent.iterator().next().contains(requestedVerticalEnv)) {
                    // Find the nearest vertical Envelope
                    NumberRange<Double> nearestEnvelope =
                            verticalExtent.iterator().next();

                    double minimumDistance = Math.abs(nearestEnvelope.getMinimum() - requestedVerticalEnv.getMinimum());
                    for (NumberRange<Double> env : verticalExtent) {
                        double distance = Math.abs(env.getMinimum() - requestedVerticalEnv.getMinimum());
                        if (distance < minimumDistance) {
                            nearestEnvelope = env;
                            minimumDistance = distance;
                        }
                    }
                    verticalSubset = new HashSet<>(1);
                    verticalSubset.add(nearestEnvelope);
                    request.setVerticalSubset(verticalSubset);
                }
            }
        }

        // //
        //
        // Checking RangeSubset setting
        //
        // //
        RangeType range = request.getRangeSubset();
        if (range == null) {
            request.setRangeSubset(source.getRangeType(null));
        }
    }

    ReadType getReadType() {
        return readType;
    }

    Interpolation getInterpolation() {
        return interpolation;
    }

    @Override
    public String toString() {
        return "NetCDFRequest [interpolation="
                + interpolation
                + ", source="
                + source
                + ", readType="
                + readType
                + ", spatialRequestHelper="
                + spatialRequestHelper
                + ", originalRequest="
                + originalRequest
                + ", name="
                + name
                + "]";
    }
}
