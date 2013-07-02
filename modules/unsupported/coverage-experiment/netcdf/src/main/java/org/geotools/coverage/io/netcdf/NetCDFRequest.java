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
package org.geotools.coverage.io.netcdf;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.ReadType;
import org.geotools.coverage.io.SpatialRequestHelper;
import org.geotools.coverage.io.SpatialRequestHelper.CoverageProperties;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.unidata.UnidataVariableAdapter;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;

/**
 * A class to handle coverage requests to a reader for a single 2D layer..
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
class NetCDFRequest extends CoverageReadRequest{
    
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(NetCDFRequest.class);

    /** The Interpolation required to serve this request */
    // TODO: CUSTOMIZE INTERPOLATION request.getInterpolation();
    Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);

    NetCDFSource source;

    // on NetCDF adopt the direct read: see the google document (Which one? it seems it has been deleted)
    ReadType readType = ReadType.DIRECT_READ;

    SpatialRequestHelper spatialRequestHelper;

    CoverageReadRequest originalRequest = null;

    String name = null;
    
    /**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     * 
     * @param params The {@code GeneralParameterValue}s to initialize this request
     * @param baseGridCoverage2DReader
     * @throws IOException 
     */
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
        spatialRequestHelper.setRequestedGridToWorld((AffineTransform)requestedG2W);
        
        // initialize
        initInputCoverageProperties();
    }
    
    /**
     * Initialize coverage input properties by collecting them from a {@link CoverageSourceWrapper}
     * @param wrapper
     * @throws IOException
     */
    private void initInputCoverageProperties() throws IOException {
        UnidataVariableAdapter.UnidataSpatialDomain spatialDomain = (org.geotools.imageio.unidata.UnidataVariableAdapter.UnidataSpatialDomain) (source.getSpatialDomain());

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
        
        // Note that currently, we only support geographic CRS
        properties.setBbox(referencedEnvelope);
        properties.setGeographicBBox(referencedEnvelope);
        properties.setGeographicCRS2D(spatialReferenceSystem2D);
        properties.setGridToWorld2D((MathTransform2D)raster2Model);
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
        UnidataVariableAdapter.UnidataSpatialDomain horizontalDomain = (UnidataVariableAdapter.UnidataSpatialDomain)source.getSpatialDomain();
        UnidataVariableAdapter.UnidataTemporalDomain temporalDomain = (UnidataVariableAdapter.UnidataTemporalDomain) source.getTemporalDomain();
        UnidataVariableAdapter.UnidataVerticalDomain verticalDomain = (UnidataVariableAdapter.UnidataVerticalDomain) source.getVerticalDomain();
        
        
        if (requestedRasterArea == null || requestedBoundingBox == null) {
            if (requestedRasterArea == null) {
                requestedRasterArea = horizontalDomain.getGridGeometry().getGridRange2D().getBounds();
            }
            if (requestedBoundingBox == null) {
                requestedBoundingBox = horizontalDomain.getReferencedEnvelope();

            }

            // TODO: Check for center/corner anchor point
            request.setDomainSubset(requestedRasterArea, ReferencedEnvelope.reference(requestedBoundingBox));
        }

        // //
        //
        // Checking TemporalSubset setting
        //
        // //
        SortedSet<DateRange> temporalSubset = request.getTemporalSubset();
        if (temporalDomain != null) {
            if (temporalSubset.isEmpty()) {
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
            if (verticalSubset.isEmpty()) {
                if (verticalExtent != null) {
                    verticalSubset = new HashSet<NumberRange<Double>>(verticalExtent);
                }
                request.setVerticalSubset(verticalSubset);
            } else {
                final NumberRange<Double> requestedVerticalEnv = verticalSubset.iterator().next();
                if (!verticalExtent.contains(requestedVerticalEnv)) {
                    // Find the nearest vertical Envelope
                    NumberRange<Double> nearestEnvelope = verticalExtent.iterator().next();

                    double minimumDistance = Math.abs(nearestEnvelope.getMinimum() - requestedVerticalEnv.getMinimum());
                    for (NumberRange<Double> env : verticalExtent) {
                        double distance = Math.abs(env.getMinimum() - requestedVerticalEnv.getMinimum());
                        if (distance < minimumDistance) {
                            nearestEnvelope = env;
                            minimumDistance = distance;
                        }
                    }
                    verticalSubset = new HashSet<NumberRange<Double>>(1);
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
}