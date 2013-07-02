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
package org.geotools.coverage.io.grib1;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.util.Utilities;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.SliceDescriptor;
import org.geotools.imageio.VerticalExtent;
import org.geotools.referencing.CRS;
import org.geotools.util.NumberRange;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.ProgressListener;

/**
 * 
 *
 * @source $URL$
 */
public class GRIB1Source implements CoverageSource {

	/** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GRIB1Source.class.toString());
    
    private class GRIB1RasterDatasetDomainManager {
		/**
		 * Implementation of temporal domain
		 * @author Simone Giannecchini, GeoSolutions SAS
		 *
		 */
		private class GRIB1TemporalDomain extends TemporalDomain{
			
			private GRIB1TemporalDomain() {
			}
	
			@Override
			public CoordinateReferenceSystem getCoordinateReferenceSystem() {
				final CoordinateReferenceSystem crs = access.crsMap.get(GRIB1Source.this.name);
				return CRS.getTemporalCRS(crs);
			}
	
			@Override
			public SortedSet<? extends TemporalGeometricPrimitive> getTemporalElements(final ProgressListener listener) throws IOException {
				if(listener!=null)
					listener.started();
				if(listener!=null&&listener.isCanceled())
					return new TreeSet<TemporalGeometricPrimitive>();		
				try{
					final TreeSet<TemporalGeometricPrimitive> retVal = new TreeSet<TemporalGeometricPrimitive>(access.temporalExtentMap.get(name));
					return retVal;
				}catch (Throwable e) {
					listener.exceptionOccurred(e);
					return new TreeSet<TemporalGeometricPrimitive>();
				}
				finally{
					if(listener!=null)
						listener.complete();
				}
			}
			
		}
		
		private class GRIB1VerticalDomain extends VerticalDomain{
	
			@Override
			public CoordinateReferenceSystem getCoordinateReferenceSystem() {
				final CoordinateReferenceSystem crs = access.crsMap.get(GRIB1Source.this.name);
				return CRS.getVerticalCRS(crs);
			}
	
			@Override
			public SortedSet<? extends NumberRange<Double>> getVerticalElements(boolean overall, ProgressListener listener)
					throws IOException {
				if(listener!=null)
					listener.started();
				if(listener!=null&&listener.isCanceled())
					return new TreeSet<NumberRange<Double>>();
				try{
					final TreeSet<NumberRange<Double>> retVal = new TreeSet<NumberRange<Double>>(access.verticalExtentMap.get(name));
					return retVal;
				}catch (Throwable e) {
					listener.exceptionOccurred(e);
					return new TreeSet<NumberRange<Double>>();
				}
				finally{
					if(listener!=null)
						listener.complete();
				}
			}
			
		}
		
		private class GRIB1HorizontalDomain extends HorizontalDomain{
	
			@Override
			public CoordinateReferenceSystem getCoordinateReferenceSystem2D() {
				final CoordinateReferenceSystem crs = access.crsMap.get(GRIB1Source.this.name);
				return CRS.getHorizontalCRS(crs);
			}
	
			@Override
			public MathTransform2D getGridToWorldTransform(ProgressListener listener) throws IOException {
				if(listener!=null)
					listener.started();
				if(listener!=null&&listener.isCanceled())
					return null;
				try{
					final MathTransform2D g2w=access.gridGeometry2DMap.get(GRIB1Source.this.name).getGridToCRS2D(PixelOrientation.CENTER);
					return g2w;
				}catch (Throwable e) {
					listener.exceptionOccurred(e);
					return null;
				}
				finally{
					if(listener!=null)
						listener.complete();
				}
			}
	
			@Override
			public Set<? extends RasterLayout> getRasterElements(boolean overall, ProgressListener listener)
					throws IOException {
				if(listener!=null)
					listener.started();
				if(listener!=null&&listener.isCanceled())
					return null;
				try{
					final Rectangle bounds=access.gridGeometry2DMap.get(GRIB1Source.this.name).getGridRange2D().getBounds();
					final RasterLayout rl= new RasterLayout(bounds);
					return Collections.singleton(rl);
				}catch (Throwable e) {
					listener.exceptionOccurred(e);
					return null;
				}
				finally{
					if(listener!=null)
						listener.complete();
				}
			}
	
			@Override
			public Set<? extends BoundingBox> getSpatialElements(boolean overall, ProgressListener listener)
					throws IOException {
				if(listener!=null)
					listener.started();
				if(listener!=null&&listener.isCanceled())
					return null;
				try{
					return Collections.singleton(new ReferencedEnvelope((org.opengis.geometry.Envelope)access.gridGeometry2DMap.get(GRIB1Source.this.name).getEnvelope2D()));
				}catch (Throwable e) {
					listener.exceptionOccurred(e);
					return null;
				}
				finally{
					if(listener!=null)
						listener.complete();
				}
				
			}
			
		}
	
		
		public CoordinateReferenceSystem getCoordinateReferenceSystem() {
	        return access.crsMap.get(GRIB1Source.this.name);
		}
	
	
		public Extent getExtent() {
			return null;
		}
	
		
		public TemporalDomain getTemporalDomain() throws IOException {
			final  TemporalDomain td= new GRIB1TemporalDomain();
			return td;
		}
	
		
		public VerticalDomain getVerticalDomain() throws IOException {
			final VerticalDomain vd= new GRIB1VerticalDomain();
			return vd;
		}
	
		
		public HorizontalDomain getHorizontalDomain() throws IOException {
			final HorizontalDomain hd= new GRIB1HorizontalDomain();
			return hd;
		}
		
	}


    private GRIB1Access access;

    private Name name;

    private final static EnumSet<CoverageCapabilities> capabilities;

    static {
        capabilities = EnumSet.of(
                CoverageCapabilities.READ_HORIZONTAL_DOMAIN_SUBSAMBLING,
                CoverageCapabilities.READ_RANGE_SUBSETTING,
                CoverageCapabilities.READ_REPROJECTION,
                CoverageCapabilities.READ_SUBSAMPLING);
    }

    private GridCoverageFactory coverageFactory;

	private GRIB1RasterDatasetDomainManager domainManager;

    public synchronized void dispose() {
        this.access = null;
        this.name = null;
    }

    GRIB1Source(final GRIB1Access access, final Name name) {
        this.access = access;
        this.name = name;
        // TODO: Take hints from somewhere
        coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(null);
        
        
        // create the domain manager
        this.domainManager= new GRIB1RasterDatasetDomainManager();
    }
    
    public ResourceInfo getInfo(ProgressListener listener) {
        ensureNotDisposed();
        return null;
    }

    public String[] getMetadataNames(ProgressListener listener)
            throws IOException {
        ensureNotDisposed();
        return null;
    }

    public String getMetadataValue(String name, ProgressListener listener)
            throws IOException {
        ensureNotDisposed();
        return null;
    }

    public Name getName(ProgressListener listener) {
        ensureNotDisposed();
        return name;
    }


    private boolean isDisposed() {
        return name == null;
    }

    private void ensureNotDisposed() {
        if (isDisposed())
            throw new IllegalStateException("Disposed");
    }
    
    public CoverageResponse read(CoverageReadRequest request,
            ProgressListener listener) throws IOException {
        ensureNotDisposed();

        // //
        //
        // Checking the request and filling the missing fields
        //
        // //

        checkRequest(request);
        BoundingBox requestedBoundingBox = request.getGeographicArea();
        Rectangle requestedRasterArea = request.getRasterArea();
        Set<TemporalGeometricPrimitive> temporalSubset = request
                .getTemporalSubset();
        Set<NumberRange<Double>> verticalSubset = request.getVerticalSubset();
        RangeType range = request.getRangeSubset();
        Set<FieldType> fieldTypes = range.getFieldTypes();

        // TODO on NetCDF adopt the suggested policy: see the google document
        final boolean useJAI = false;
        // TODO: Parse coverageRequest additional parameters.

        final CoverageResponse response = new CoverageResponse();
        response.setRequest(request);

        Rectangle sourceRasterRegion = new Rectangle();
        GeneralEnvelope adjustedRequestedEnvelope2D;
        GeneralEnvelope requestedEnvelope2D = new GeneralEnvelope(requestedBoundingBox);
        final MathTransform2D grid2WorldTransform = request.getGridToWorldTransform();
        final ImageReadParam imageReadParam = new ImageReadParam();
        try {
            // //
            //
            // Set envelope and source region
            //
            // //
            adjustedRequestedEnvelope2D = Utilities.evaluateRequestedParams(
                    access.gridGeometry2DMap.get(this.name).getGridRange(),
                    access.baseEnvelope2DMap.get(this.name),
                    access.spatialReferenceSystem2DMap.get(this.name),
                    access.raster2ModelMap.get(this.name), requestedEnvelope2D,
                    sourceRasterRegion, requestedRasterArea,
                    grid2WorldTransform, access.wgs84BaseEnvelope2DMap
                            .get(this.name));

            // //
            //
            // Set specific imageIO parameters: type of read operation,
            // imageReadParams
            //
            // //
            // useJAI = requestUsesJaiImageread();
            // if (useMultithreading) {
            // imageReadParam = new DefaultCloneableImageReadParam();
            // } else {
            // imageReadParam = new ImageReadParam();
            // }
            if (adjustedRequestedEnvelope2D != null) {
                final GeneralEnvelope req = (adjustedRequestedEnvelope2D
                        .isEmpty()) ? requestedEnvelope2D
                        : adjustedRequestedEnvelope2D;
                Utilities.setReadParameters(null /* OverviewPolicy */,
                        imageReadParam, req, requestedRasterArea,
                        access.highestResMap.get(this.name),
                        access.gridGeometry2DMap.get(this.name).getGridRange(),
                        PixelInCell.CELL_CORNER);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            adjustedRequestedEnvelope2D = null;
        } catch (TransformException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            adjustedRequestedEnvelope2D = null;
        }
        if (adjustedRequestedEnvelope2D != null && sourceRasterRegion != null
                && !sourceRasterRegion.isEmpty()) {
            imageReadParam.setSourceRegion(sourceRasterRegion);
        }

        // A transformation is requested in case the requested envelope has been
        // adjusted
        final boolean needTransformation = (adjustedRequestedEnvelope2D != null && !adjustedRequestedEnvelope2D
                .isEmpty());

        // In case the adjusted requested envelope is null, no intersection
        // between requested envelope and base envelope have been found. Hence,
        // no valid coverage will be loaded and the request should be considered
        // as producing an empty result.
        final boolean emptyRequest = adjustedRequestedEnvelope2D == null;

        // //
        //
        // adding GridCoverages to the results list
        //
        // //
        for (FieldType fieldType : fieldTypes) {
            final Name name = fieldType.getName();

            // TODO: Fix this leveraging on the proper Band
            Set<SampleDimension> sampleDims = fieldType.getSampleDimensions();
            if (sampleDims == null || sampleDims.isEmpty()) {
                final RangeType innerRange = access.rangeMap.get(this.name);
                if (innerRange != null) {
                    final FieldType ft = innerRange.getFieldType(name
                            .getLocalPart());
                    if (ft != null)
                        sampleDims = ft.getSampleDimensions();
                }

            }

            final GridSampleDimension[] sampleDimensions = sampleDims
                    .toArray(new GridSampleDimension[sampleDims.size()]);
            final Collection<SliceDescriptor> sliceDescriptors = access.sliceDescriptorsMap.values();
            for (SliceDescriptor sd : sliceDescriptors) {
                if (name.getLocalPart().equals(sd.getElementName())) {
                    // //
                    //
                    // Has Time?
                    // 
                    // //
                    if (!temporalSubset.isEmpty()) {
                        for (TemporalGeometricPrimitive time : temporalSubset) {
                            TemporalGeometricPrimitive sdTime = sd
                                    .getTemporalExtent();
                            if (isTimeAccepted(time, sdTime)) {
                                addCoverage(response, verticalSubset, sd,
                                        needTransformation, emptyRequest,
                                        useJAI, imageReadParam,
                                        sampleDimensions);
                            }
                        }
                    } else {
                        addCoverage(response, verticalSubset, sd,
                                needTransformation, emptyRequest, useJAI,
                                imageReadParam, sampleDimensions);
                    }
                }
            }
        }
        response.setStatus(Status.SUCCESS);

        return response;
    }
    

//    public CoverageResponse read(CoverageReadRequest request,
//            ProgressListener listener) throws IOException {
//        ensureNotDisposed();
//
//        // //
//        //
//        // Checking the request and filling the missing fields
//        //
//        // //
//
//        checkRequest(request);
//        BoundingBox requestedBoundingBox = request.getGeographicArea();
//        Rectangle requestedRasterArea = request.getRasterArea();
//        Set<TemporalGeometricPrimitive> temporalSubset = request
//                .getTemporalSubset();
//        Set<Envelope> verticalSubset = request.getVerticalSubset();
//        RangeType range = request.getRangeSubset();
//        Set<FieldType> fieldTypes = range.getFieldTypes();
//
//        final DefaultCoverageResponseImpl response = new DefaultCoverageResponseImpl();
//        response.setRequest(request);
//
//        //
//
//        Rectangle sourceRasterRegion = new Rectangle();
//        GeneralEnvelope adjustedRequestedEnvelope2D;
//        GeneralEnvelope requestedEnvelope2D = new GeneralEnvelope(
//                requestedBoundingBox);
//        final MathTransform2D grid2WorldTransform = request
//                .getGridToWorldTransform();
//        final ImageReadParam imageReadParam = new ImageReadParam();
//        try {
//            // //
//            //
//            // Set envelope and source region
//            //
//            // //
//            adjustedRequestedEnvelope2D = Utilities.evaluateRequestedParams(
//                    access.gridGeometry2DMap.get(this.name).getGridRange(),
//                    access.baseEnvelope2DMap.get(this.name),
//                    access.spatialReferenceSystem2DMap.get(this.name),
//                    access.raster2ModelMap.get(this.name), requestedEnvelope2D,
//                    sourceRasterRegion, requestedRasterArea,
//                    grid2WorldTransform, access.wgs84BaseEnvelope2DMap
//                            .get(this.name));
//
//            // //
//            //
//            // Set specific imageIO parameters: type of read operation,
//            // imageReadParams
//            //
//            // //
//            // useJAI = requestUsesJaiImageread();
//            // if (useMultithreading) {
//            // imageReadParam = new DefaultCloneableImageReadParam();
//            // } else {
//            // imageReadParam = new ImageReadParam();
//            // }
//            if (adjustedRequestedEnvelope2D != null) {
//                final GeneralEnvelope req = (adjustedRequestedEnvelope2D
//                        .isEmpty()) ? requestedEnvelope2D
//                        : adjustedRequestedEnvelope2D;
//                Utilities.setReadParameters(null /* OverviewPolicy */,
//                        imageReadParam, req, requestedRasterArea,
//                        access.highestResMap.get(this.name),
//                        access.gridGeometry2DMap.get(this.name).getGridRange(),
//                        PixelInCell.CELL_CORNER);
//            }
//        } catch (IOException e) {
//            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
//            adjustedRequestedEnvelope2D = null;
//        } catch (TransformException e) {
//            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
//            adjustedRequestedEnvelope2D = null;
//        }
//        if (adjustedRequestedEnvelope2D != null && sourceRasterRegion != null
//                && !sourceRasterRegion.isEmpty()) {
//            imageReadParam.setSourceRegion(sourceRasterRegion);
//        }
//
//        // A transformation is requested in case the requested envelope has been
//        // adjusted
//        final boolean needTransformation = (adjustedRequestedEnvelope2D != null && !adjustedRequestedEnvelope2D
//                .isEmpty());
//
//        // In case the adjusted requested envelope is null, no intersection
//        // between requested envelope and base envelope have been found. Hence,
//        // no valid coverage will be loaded and the request should be considered
//        // as producing an empty result.
//        final boolean emptyRequest = adjustedRequestedEnvelope2D == null;
//
//        // //
//        //
//        // adding GridCoverages to the results list
//        //
//        // //
//        for (FieldType fieldType : fieldTypes) {
//            final Name name = fieldType.getName();
//
//            // TODO: Fix this leveraging on the proper Band
//            Set<Band> sampleDims = fieldType.getSampleDimensions();
//            final GridSampleDimension[] sampleDimensions = sampleDims
//                    .toArray(new GridSampleDimension[sampleDims.size()]);
//            for (SliceDescriptor sd : access.sliceDescriptorsMap.values()) {
//                if (name.getLocalPart().equals(sd.getElementName())) {
//                    for (TemporalGeometricPrimitive time : temporalSubset) {
//                        TemporalGeometricPrimitive sdTime = sd
//                                .getTemporalExtent();
//                        if (isTimeAccepted(time, sdTime)) {
//                            final int imageIndex = ((AbstractSliceDescriptor) sd)
//                                    .getImageIndex();
//                            if (!verticalSubset.isEmpty()) {
//                                for (Envelope envelope : verticalSubset) {
//                                    VerticalExtent ve = sd.getVerticalExtent();
//                                    if (ve != null) {
//                                        GeneralEnvelope verticalEnvelope = new GeneralEnvelope(
//                                                1);
//                                        verticalEnvelope.setEnvelope(ve
//                                                .getMinimumValue()
//                                                .doubleValue(), ve
//                                                .getMaximumValue()
//                                                .doubleValue());
//                                        if (verticalEnvelope.contains(envelope,
//                                                true)) {
//                                            GridCoverage gc = compute(
//                                                    BaseFileDriver
//                                                            .urlToFile(access
//                                                                    .getInput()),
//                                                    imageIndex,
//                                                    needTransformation,
//                                                    emptyRequest,
//                                                    imageReadParam,
//                                                    sampleDimensions);
//                                            if (gc != null)
//                                                response.addResult(gc);
//                                        }
//                                    }
//                                }
//                            } else {
//                                GridCoverage gc = compute(BaseFileDriver
//                                        .urlToFile(access.getInput()),
//                                        imageIndex, needTransformation,
//                                        emptyRequest, imageReadParam,
//                                        sampleDimensions);
//                                if (gc != null)
//                                    response.addResult(gc);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        response.setStatus(Status.SUCCESS);
//        return response;
//    }
    
    private void addCoverage(
    		final CoverageResponse response,
            Set<NumberRange<Double>> verticalSubset, 
            final SliceDescriptor sd,
            final boolean needTransformation, 
            final boolean emptyRequest,
            final boolean useJAI, 
            ImageReadParam imageReadParam,
            GridSampleDimension[] sampleDimensions) throws IOException {
        if (!verticalSubset.isEmpty()) {
            for (NumberRange<Double> envelope : verticalSubset) {
                VerticalExtent ve = sd.getVerticalExtent();
                if (ve != null) {
                	NumberRange<Double> verticalEnvelope = NumberRange.create(ve.getMinimumValue().doubleValue(), ve.getMaximumValue().doubleValue());
                    if (verticalEnvelope.contains(envelope)) {// TODO check this, it might not be correct
                        final int imageIndex = ((SliceDescriptor) sd)
                                .getImageIndex();
                        GridCoverage gc = Utilities.compute(DefaultFileDriver
                                .urlToFile(access.getInput()), imageIndex,
                                needTransformation, emptyRequest, useJAI,
                                imageReadParam, false, sampleDimensions, GRIB1Driver.spi, this.name.toString(), coverageFactory, access.raster2ModelMap.get(this.name),
                                access.spatialReferenceSystem2DMap.get(this.name), access.coverageEnvelope2DMap.get(this.name));
                        if (gc != null)
                            response.addResult(gc);
                    }
                }
            }
        } else {
            final int imageIndex = ((SliceDescriptor) sd)
                    .getImageIndex();
            GridCoverage gc = Utilities.compute(DefaultFileDriver.urlToFile(access
                    .getInput()), imageIndex, needTransformation, emptyRequest,
                    useJAI, imageReadParam, false, sampleDimensions, GRIB1Driver.spi, this.name.toString(), coverageFactory, access.raster2ModelMap.get(this.name),
                    access.spatialReferenceSystem2DMap.get(this.name), access.coverageEnvelope2DMap.get(this.name));
            if (gc != null)
                response.addResult(gc);
        }
    }
    

    private void checkRequest(CoverageReadRequest request) {
        BoundingBox requestedBoundingBox = request.getGeographicArea();

        // //
        //
        // Checking RequestedRasterArea setting
        //
        // //
        Rectangle requestedRasterArea = request.getRasterArea();
        if (requestedRasterArea == null || requestedBoundingBox == null) {
            if (requestedRasterArea == null) {
                // requestedRasterArea =
                // access.coverageGeometries.get(access.coverageNames.get(0))
                // .getGridRange2D().getBounds();
                requestedRasterArea = access.gridGeometry2DMap.get(this.name)
                        .getGridRange2D().getBounds();
            }
            if (requestedBoundingBox == null) {
                // requestedBoundingBox = (BoundingBox) new ReferencedEnvelope(
                // access.coverageGeometries.get(access.coverageNames.get(0)).getEnvelope());
                requestedBoundingBox = (BoundingBox) new ReferencedEnvelope(
                        access.gridGeometry2DMap.get(this.name).getEnvelope());

            }

            // TODO: Check for center/corner anchor point
            request.setDomainSubset(requestedRasterArea, ReferencedEnvelope.reference(requestedBoundingBox));
        }

        // //
        //
        // Checking TemporalSubset setting
        //
        // //
        SortedSet<TemporalGeometricPrimitive> temporalSubset = request
                .getTemporalSubset();
        if (temporalSubset.isEmpty()) {
            Set<TemporalGeometricPrimitive> temporalExtent = access.temporalExtentMap
                    .get(this.name);
            temporalSubset = new TreeSet<TemporalGeometricPrimitive>(
                    temporalExtent);
            request.setTemporalSubset(temporalSubset);
        }

     // //
        //
        // Checking VerticalSubset setting
        //
        // //
        Set<NumberRange<Double>> verticalSubset = request.getVerticalSubset();
        if (verticalSubset.isEmpty()) {
            Set<NumberRange<Double>> verticalExtent = access.verticalExtentMap.get(this.name);
            if (verticalExtent != null)
                verticalSubset = new HashSet<NumberRange<Double>>(verticalExtent);
            request.setVerticalSubset(verticalSubset);
        } else {
            final Set<NumberRange<Double>> availableSet = access.verticalExtentMap.get(this.name);
            final NumberRange<Double> requestedEnvelope = verticalSubset.iterator().next();
            if (!availableSet.contains(requestedEnvelope)) {//TODO is this correct?
                // TODO: Actually, support for a single vertical element search
                // Find the nearest vertical Envelope
            	NumberRange<Double> nearestEnvelope = availableSet.iterator().next();

                double minimumDistance = Math.abs(nearestEnvelope.getMinimum()- requestedEnvelope.getMinimum());
                for (NumberRange<Double> env : availableSet) {
                    double distance = Math.abs(env.getMinimum()- requestedEnvelope.getMinimum());
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

        // //
        //
        // Checking RangeSubset setting
        //
        // //
        RangeType range = request.getRangeSubset();
        if (range == null)
            // TODO: copy the range
            request.setRangeSubset(access.rangeMap.get(this.name));
    }

    public EnumSet<CoverageCapabilities> getCapabilities() {
        return EnumSet.copyOf(capabilities);
    }

    public Map<String, Parameter<?>> getReadParameterInfo() {
        return null;
    }

    public RangeType getRangeType(ProgressListener listener) throws IOException {
        ensureNotDisposed();
        return access.rangeMap.get(this.name);
    }
    
    /**
     * Move these methods to an Utility Class and improve the logic.
     * 
     * @param first
     * @param second
     * @return
     */
    private boolean isTimeAccepted(TemporalGeometricPrimitive first,
            TemporalGeometricPrimitive second) {
        boolean takeThis = Utilities.contains(first, second);
        if (!takeThis)
            takeThis = Utilities.contains(second, first);
        return takeThis;
    }


	public MetadataNode getMetadata(String metadataDomain,
			ProgressListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Name> getMetadataDomains() {
		// TODO Auto-generated method stub
		return null;
	}

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return this.domainManager.getCoordinateReferenceSystem();
    }

    public Extent getExtent() {
        return this.domainManager.getExtent();
    }

    public HorizontalDomain getHorizontalDomain() throws IOException {
        return this.domainManager.getHorizontalDomain();
    }

    public TemporalDomain getTemporalDomain() throws IOException {
        return this.domainManager.getTemporalDomain();
    }

    public VerticalDomain getVerticalDomain() throws IOException {
        return this.domainManager.getVerticalDomain();
    }

    public List<? extends RasterLayout> getOverviewsLayouts(ProgressListener listener)
            throws IOException {
        return Collections.emptyList();
    }

    public int getOverviewsNumber(ProgressListener listener) throws IOException {
        return 0;
    }
}
